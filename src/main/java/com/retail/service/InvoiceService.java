/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.service;

import com.retail.dao.InvoiceDAO;
import com.retail.dao.InvoiceDetailDAO;
import com.retail.dao.CustomerDAO;
import com.retail.dao.ProductDAO;
import com.retail.dao.InventoryDAO;
import com.retail.model.Invoice;
import com.retail.model.InvoiceDetail;
import com.retail.model.Customer;
import com.retail.model.Product;
import com.retail.model.Inventory;
import java.math.BigDecimal;
import java.util.List;

public class InvoiceService {
    private InvoiceDAO invoiceDAO;
    private InvoiceDetailDAO invoiceDetailDAO;
    private CustomerDAO customerDAO;
    private ProductDAO productDAO;
    private InventoryDAO inventoryDAO;  // Thêm InventoryDAO

    public InvoiceService() {
        invoiceDAO = new InvoiceDAO();
        invoiceDetailDAO = new InvoiceDetailDAO();
        customerDAO = new CustomerDAO();
        productDAO = new ProductDAO();
        inventoryDAO = new InventoryDAO();
    }

    /**
     * Tạo hóa đơn mới và xử lý nghiệp vụ:
     * - Kiểm tra tồn kho cho từng sản phẩm.
     * - Tạo hóa đơn, ghi chi tiết hóa đơn.
     * - Cập nhật tồn kho cho từng sản phẩm.
     */
    public boolean createInvoice(Invoice invoice, List<InvoiceDetail> details) {
        // Kiểm tra tồn kho cho mỗi sản phẩm trước 
        for (InvoiceDetail detail : details) {
            Inventory inventory = inventoryDAO.getInventoryByProductId(detail.getProductId());
            if (inventory == null) {
                System.out.println("❌ Không tìm thấy bản ghi tồn kho cho sản phẩm ID " + detail.getProductId());
                return false;
            }
            if (inventory.getStockQuantity() < detail.getQuantity()) {
                System.out.println("❌ Sản phẩm không đủ hàng (ID " + detail.getProductId() + ")");
                return false;
            }
        }
        
        // Tạo hóa đơn
        boolean invoiceCreated = invoiceDAO.createInvoice(invoice);
        if (!invoiceCreated) {
            return false;
        }
        
        // Ghi chi tiết hóa đơn và cập nhật tồn kho cho từng sản phẩm
        for (InvoiceDetail detail : details) {
            detail.setInvoiceId(invoice.getInvoiceId());
            boolean detailCreated = invoiceDetailDAO.addInvoiceDetail(detail);
            if (!detailCreated) {
                return false; 
            }
            
            // Cập nhật tồn kho: trừ số lượng sản phẩm đã bán
            Inventory inventory = inventoryDAO.getInventoryByProductId(detail.getProductId());
            if (inventory == null) {
                System.out.println("❌ Không tìm thấy bản ghi tồn kho cho sản phẩm ID " + detail.getProductId());
                return false;
            }
            int newQuantity = inventory.getStockQuantity() - detail.getQuantity();
            if (newQuantity < 0) {
                System.out.println("❌ Không đủ hàng cho sản phẩm ID " + detail.getProductId());
                return false;
            }
            inventory.setStockQuantity(newQuantity);
            boolean inventoryUpdated = inventoryDAO.updateInventory(inventory);
            if (!inventoryUpdated) {
                return false;
            }
        }
        return true;
    }
    
    public boolean updateInvoice(Invoice invoice, List<InvoiceDetail> details) {
    // 1. Cập nhật hóa đơn chính (sử dụng UPDATE query trong InvoiceDAO)
    boolean updated = invoiceDAO.updateInvoice(invoice);
    if (!updated) {
        return false;
    }
    // 2. Xoá các chi tiết hóa đơn cũ (có thể dùng InvoiceDetailDAO.deleteByInvoiceId)
    boolean detailsDeleted = invoiceDetailDAO.deleteInvoiceDetailsByInvoiceId(invoice.getInvoiceId());
    if (!detailsDeleted) {
        return false;
    }
    // 3. Ghi các chi tiết hóa đơn mới và cập nhật tồn kho cho từng sản phẩm
    for (InvoiceDetail detail : details) {
        detail.setInvoiceId(invoice.getInvoiceId());
        boolean detailCreated = invoiceDetailDAO.addInvoiceDetail(detail);
        if (!detailCreated) {
            return false;
        }
        // Cập nhật tồn kho: trừ số lượng bán theo hóa đơn mới
        Product product = productDAO.getProductById(detail.getProductId());
        if (product == null) {
            return false;
        }
        Inventory inv = inventoryDAO.getInventoryByProductId(detail.getProductId());
        if (inv == null) {
            return false;
        }
        int newStock = inv.getStockQuantity() - detail.getQuantity();
        if (newStock < 0) {
            return false;
        }
        inv.setStockQuantity(newStock);
        boolean inventoryUpdated = inventoryDAO.updateInventory(inv);
        if (!inventoryUpdated) {
            return false;
        }
    }
    return true;
    }

}
