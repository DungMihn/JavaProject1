/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.controller;

import com.retail.model.InvoiceDetail;
import com.retail.service.InvoiceDetailService;
import java.util.List;
/**
 *
 * @author Admin
 */
public class InvoiceDetailController {
    private final InvoiceDetailService invoiceDetailService = new InvoiceDetailService();

    // Thêm chi tiết hóa đơn mới
    public void addInvoiceDetail(int invoiceId, int productId, int quantity, double price) {
        InvoiceDetail invoiceDetail = new InvoiceDetail();
        invoiceDetail.setInvoiceId(invoiceId);
        invoiceDetail.setProductId(productId);
        invoiceDetail.setQuantity(quantity);
        invoiceDetail.setPrice(price);
        invoiceDetail.setSubtotal(quantity * price);
        invoiceDetailService.addInvoiceDetail(invoiceDetail);
    }

    // Lấy danh sách chi tiết hóa đơn
    public List<InvoiceDetail> getAllInvoiceDetails() {
        return invoiceDetailService.getAllInvoiceDetails();
    }

    // Lấy thông tin chi tiết hóa đơn theo ID
    public InvoiceDetail getInvoiceDetailById(int id) {
        return invoiceDetailService.getInvoiceDetailById(id);
    }

    // Xóa chi tiết hóa đơn
    public void deleteInvoiceDetail(int id) {
        invoiceDetailService.deleteInvoiceDetail(id);
    }
}
