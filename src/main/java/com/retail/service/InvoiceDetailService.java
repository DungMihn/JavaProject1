/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.service;

import com.retail.dao.InvoiceDetailDAOImpl;
import com.retail.model.InvoiceDetail;
import java.util.List;
/**
 *
 * @author Admin
 */
public class InvoiceDetailService {
    private final InvoiceDetailDAOImpl invoiceDetailDAO = new InvoiceDetailDAOImpl();

    // Thêm chi tiết hóa đơn
    public void addInvoiceDetail(InvoiceDetail invoiceDetail) {
        invoiceDetailDAO.addInvoiceDetail(invoiceDetail);
        System.out.println("✅ Chi tiết hóa đơn đã được thêm thành công!");
    }

    // Lấy danh sách chi tiết hóa đơn
    public List<InvoiceDetail> getAllInvoiceDetails() {
        return invoiceDetailDAO.getAllInvoiceDetails();
    }

    // Lấy chi tiết hóa đơn theo ID
    public InvoiceDetail getInvoiceDetailById(int id) {
        return invoiceDetailDAO.getInvoiceDetailById(id);
    }

    // Xóa chi tiết hóa đơn
    public void deleteInvoiceDetail(int id) {
        invoiceDetailDAO.deleteInvoiceDetail(id);
        System.out.println("✅ Chi tiết hóa đơn đã được xóa thành công!");
    }
}
