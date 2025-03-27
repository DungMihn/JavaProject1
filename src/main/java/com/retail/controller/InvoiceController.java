/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.controller;

import com.retail.model.Invoice;
import com.retail.model.InvoiceDetail;
import com.retail.service.InvoiceService;
import com.retail.view.InvoicePanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class InvoiceController {
    private InvoiceService invoiceService;
    private InvoicePanel invoicePanel;

    public InvoiceController(InvoiceService invoiceService, InvoicePanel invoicePanel) {
        this.invoiceService = invoiceService;
        this.invoicePanel = invoicePanel;
        initController();
    }

    private void initController() {
        invoicePanel.getSubmitButton().addActionListener(e -> processInvoice());
    }

    private void processInvoice() {
    Invoice invoice = invoicePanel.getInvoiceData();
    List<InvoiceDetail> details = invoicePanel.getInvoiceDetails();
    boolean result;
    boolean isNew = invoice.getInvoiceId() == 0;
    
    if (isNew) {
        // Tạo hóa đơn mới
        result = invoiceService.createInvoice(invoice, details);
    } else {
        // Cập nhật hóa đơn cũ
        result = invoiceService.updateInvoice(invoice, details);
    }
    
    if (result) {
        if (isNew) {
            invoicePanel.showMessage("Hóa đơn được tạo thành công!");
        } else {
            invoicePanel.showMessage("Hóa đơn được cập nhật thành công!");
        }
        invoicePanel.resetForm();
        invoicePanel.refreshInvoiceList();
    } else {
        if (isNew) {
            invoicePanel.showMessage("Tạo hóa đơn thất bại!");
        } else {
            invoicePanel.showMessage("Cập nhật hóa đơn thất bại!");
        }
    }
}

}
