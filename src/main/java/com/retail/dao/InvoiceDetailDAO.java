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
    boolean addInvoiceDetail(InvoiceDetail detail);
    List<InvoiceDetail> getInvoiceDetailsByInvoiceId(int invoiceId);
    boolean deleteInvoiceDetailsByInvoiceId(int invoiceId);
    List<InvoiceDetail> getBestSellingProductsByDate();
    List<InvoiceDetail> getBestSellingProductsByMonth();
}
