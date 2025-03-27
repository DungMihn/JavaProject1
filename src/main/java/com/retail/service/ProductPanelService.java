package com.retail.service;

import com.retail.controller.ProductController;
import com.retail.model.Product;
import com.retail.model.Inventory;
import com.retail.model.Supplier;
import com.retail.view.EditProductDialog;
import com.retail.view.ProductPanel;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class ProductPanelService {
    private ProductPanel panel;
    private ProductController controller;
    
    public ProductPanelService(ProductPanel panel) {
        this.panel = panel;
        this.controller = new ProductController();
        initComboBoxes();
        initEventHandlers();
        loadProductData();
    }
    

    public void syncProject() {
        panel.getTxtSearch().setText("");
        // Làm mới dữ liệu comboBox và đặt lựa chọn mặc định về "Tất cả"
        initComboBoxes();
        // Load lại danh sách sản phẩm từ database
        loadProductData();
    }
    
    // Khởi tạo dữ liệu cho comboBox: Loại sản phẩm và Nhà cung cấp
    public void initComboBoxes() {
        // Loại sản phẩm
        panel.getCmbCategory().removeAllItems();
        panel.getCmbCategory().addItem("Tất cả");
        List<String> categories = controller.getDistinctCategories();
        for (String cat : categories) {
            panel.getCmbCategory().addItem(cat);
        }
        // Đặt lựa chọn mặc định về "Tất cả"
        panel.getCmbCategory().setSelectedIndex(0);
        
        // Nhà cung cấp
        panel.getCmbSupplier().removeAllItems();
        panel.getCmbSupplier().addItem("Tất cả");
        List<Supplier> suppliers = controller.getAllSuppliers();
        for (Supplier s : suppliers) {
            panel.getCmbSupplier().addItem(s.getName());
        }
        // Đặt lựa chọn mặc định về "Tất cả"
        panel.getCmbSupplier().setSelectedIndex(0);
    }
    
    // Gán sự kiện cho các thành phần UI
    private void initEventHandlers() {
        // DocumentListener cho ô tìm kiếm
        panel.getTxtSearch().getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { searchProduct(); }
            @Override
            public void removeUpdate(DocumentEvent e) { searchProduct(); }
            @Override
            public void changedUpdate(DocumentEvent e) { searchProduct(); }
        });
        
        // ActionListeners cho nút tìm kiếm và thay đổi lựa chọn comboBox
        panel.getBtnSearch().addActionListener(e -> searchProduct());
        panel.getCmbCategory().addActionListener(e -> searchProduct());
        panel.getCmbSupplier().addActionListener(e -> searchProduct());
        
        // ActionListener cho nút sửa
        panel.getBtnEdit().addActionListener(e -> editProduct());
        
        // MouseListener cho bảng: double-click để sửa sản phẩm
        panel.getProductTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    editProduct();
                }
            }
        });
    }
    
    // Load dữ liệu sản phẩm từ controller và hiển thị lên bảng
    public void loadProductData() {
        DefaultTableModel tableModel = panel.getTableModel();
        tableModel.setRowCount(0); // Xóa dữ liệu cũ

        // Lấy tất cả sản phẩm từ controller và cập nhật lại bảng
        List<Product> productList = controller.getAllProducts();
        for (Product product : productList) {
            int stock = 0;
            Inventory inventory = controller.getInventoryByProductId(product.getProductId());
            if (inventory != null) {
                stock = inventory.getStockQuantity();
            }
            Supplier supplier = controller.getSupplierById(product.getSupplierId());
            String supplierName = (supplier != null) ? supplier.getName() : "";
            
            Object[] rowData = {
                product.getProductId(),
                product.getName(),
                product.getCategory(),
                product.getPrice(),
                product.getUnit(),
                stock,
                product.getBarcode(),
                supplierName,
                product.getPurchasePrice()
            };
            tableModel.addRow(rowData);
        }
    }
    
    // Tìm kiếm sản phẩm theo từ khóa, loại và nhà cung cấp
    public void searchProduct() {
        String keyword = panel.getTxtSearch().getText().trim().toLowerCase();
        
        // Kiểm tra giá trị trả về của getSelectedItem() để tránh null
        Object categoryObj = panel.getCmbCategory().getSelectedItem();
        Object supplierObj = panel.getCmbSupplier().getSelectedItem();
        String categoryFilter = (categoryObj == null) ? "Tất cả" : categoryObj.toString();
        String supplierFilter = (supplierObj == null) ? "Tất cả" : supplierObj.toString();
        
        DefaultTableModel tableModel = panel.getTableModel();
        tableModel.setRowCount(0);
        List<Product> productList = controller.searchProducts(keyword);
        for (Product product : productList) {
            // Lọc theo loại sản phẩm
            if (!"Tất cả".equals(categoryFilter) && !product.getCategory().equalsIgnoreCase(categoryFilter)) {
                continue;
            }
            // Lấy tên nhà cung cấp và lọc theo lựa chọn
            Supplier supplier = controller.getSupplierById(product.getSupplierId());
            String supplierName = (supplier != null) ? supplier.getName() : "";
            if (!"Tất cả".equals(supplierFilter) && !supplierName.equalsIgnoreCase(supplierFilter)) {
                continue;
            }
            // Tìm kiếm theo tên hoặc mã vạch nếu từ khóa không rỗng
            if (!keyword.isEmpty()) {
                String nameLower = product.getName().toLowerCase();
                String barcodeLower = product.getBarcode().toLowerCase();
                if (!nameLower.contains(keyword) && !barcodeLower.contains(keyword)) {
                    continue;
                }
            }
            int stock = 0;
            Inventory inventory = controller.getInventoryByProductId(product.getProductId());
            if (inventory != null) {
                stock = inventory.getStockQuantity();
            }
            Object[] rowData = {
                product.getProductId(),
                product.getName(),
                product.getCategory(),
                product.getPrice(),
                product.getUnit(),
                stock,
                product.getBarcode(),
                supplierName,
                product.getPurchasePrice()
            };
            tableModel.addRow(rowData);
        }
    }
    
    // Xử lý sự kiện sửa sản phẩm
    public void editProduct() {
        int selectedRow = panel.getProductTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(panel, "Vui lòng chọn sản phẩm cần sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        DefaultTableModel tableModel = panel.getTableModel();
        int productId = (int) tableModel.getValueAt(selectedRow, 0);
        Product product = controller.getAllProducts().stream()
                .filter(p -> p.getProductId() == productId)
                .findFirst().orElse(null);
        if (product != null) {
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(panel);
            new EditProductDialog(parentFrame, product, panel);
        }
    }
}
