/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.service;
import com.retail.model.Invoice;
import com.retail.dao.InvoiceDAO;
import java.util.List;

/**
 *
 * @author Admin
 */
public class InvoiceService {
    private final InvoiceDAO invoiceDAO;

    // Constructor: Inject InvoiceDAO vào Service
    public InvoiceService(InvoiceDAO invoiceDAO) {
        this.invoiceDAO = invoiceDAO;
    }

    // Thêm hóa đơn
    public void addInvoice(Invoice invoice) {
        invoiceDAO.addInvoice(invoice);
    }

    // Lấy tất cả hóa đơn
    public List<Invoice> getAllInvoices() {
        return invoiceDAO.getAllInvoices();
    }

    // Lấy hóa đơn theo ID
    public Invoice getInvoiceById(int invoiceId) {
        return invoiceDAO.getInvoiceById(invoiceId);
    }

    // Xóa hóa đơn
    public void deleteInvoice(int invoiceId) {
        invoiceDAO.deleteInvoice(invoiceId);
    }
}
