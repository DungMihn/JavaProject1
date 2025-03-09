/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.retail.dao;

import com.retail.model.InvoiceDetail;
import java.util.List;

/**
 *
 * @author Admin
 */
public interface InvoiceDetailDAO {
    void addInvoiceDetail(InvoiceDetail invoiceDetail);
    void updateInvoiceDetail(InvoiceDetail invoiceDetail);
    void deleteInvoiceDetail(int invoiceDetailId);
    InvoiceDetail getInvoiceDetailById(int invoiceDetailId);
    List<InvoiceDetail> getAllInvoiceDetails();
}
