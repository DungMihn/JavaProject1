/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.controller;
import com.retail.model.Invoice;
import com.retail.service.InvoiceService;
import java.util.List;

/**
 *
 * @author Admin
 */
public class InvoiceController {
    private final InvoiceService invoiceService;

    // Constructor: Inject InvoiceService vào Controller
    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    // Phương thức tạo hóa đơn mới
    public void createInvoice(int customerId, int employeeId, double totalAmount, double discount, String paymentMethod) {
        // Tính toán finalAmount
        double finalAmount = totalAmount - discount;

        // Tạo đối tượng Invoice
        Invoice invoice = new Invoice();
        invoice.setCustomerId(customerId);
        invoice.setEmployeeId(employeeId);
        invoice.setTotalAmount(totalAmount);
        invoice.setDiscount(discount);
        invoice.setFinalAmount(finalAmount);
        invoice.setPaymentMethod(paymentMethod);

        // Gọi Service để lưu hóa đơn
        invoiceService.addInvoice(invoice);
        System.out.println("✅ Hóa đơn đã được tạo thành công!");
    }

    // Phương thức lấy danh sách hóa đơn
    public List<Invoice> getAllInvoices() {
        return invoiceService.getAllInvoices();
    }

    // Phương thức lấy hóa đơn theo ID
    public Invoice getInvoiceById(int invoiceId) {
        return invoiceService.getInvoiceById(invoiceId);
    }

    // Phương thức xóa hóa đơn
    public void deleteInvoice(int invoiceId) {
        invoiceService.deleteInvoice(invoiceId);
        System.out.println("✅ Hóa đơn đã được xóa thành công!");
    }
}
