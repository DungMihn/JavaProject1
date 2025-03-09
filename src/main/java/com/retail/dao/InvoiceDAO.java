/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.retail.dao;
import com.retail.model.Invoice;
import java.util.List;
/**
 *
 * @author Admin
 */
public interface InvoiceDAO {
    void addInvoice(Invoice invoice);
    void updateInvoice(Invoice invoice);
    void deleteInvoice(int invoiceId);
    Invoice getInvoiceById(int invoiceId);
    List<Invoice> getAllInvoices();
}
