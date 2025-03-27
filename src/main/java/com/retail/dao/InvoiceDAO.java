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
    boolean createInvoice(Invoice invoice);
    Invoice getInvoiceById(int invoiceId);
    List<Invoice> getAllInvoices();
    boolean updateInvoice(Invoice invoice);
    List<Invoice> getInvoicesByCustomerId(int customerId);
    List<Invoice> getInvoicesByDate();
    List<Invoice> getInvoicesByMonth();
}
