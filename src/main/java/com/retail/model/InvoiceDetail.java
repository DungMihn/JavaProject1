/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.model;

import java.math.BigDecimal;

public class InvoiceDetail {
    private int invoiceDetailId;
    private int invoiceId;
    private int productId;
    private int quantity;
    private BigDecimal price;
    private BigDecimal subtotal;

    public InvoiceDetail() { }

    public InvoiceDetail(int invoiceId, int productId, int quantity, BigDecimal price, BigDecimal subtotal) {
        this.invoiceId = invoiceId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        this.subtotal = subtotal;
    }

    // Getters và Setters

    public int getInvoiceDetailId() {
        return invoiceDetailId;
    }

    public void setInvoiceDetailId(int invoiceDetailId) {
        this.invoiceDetailId = invoiceDetailId;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public java.math.BigDecimal getPrice() {
        return price;
    }

    public void setPrice(java.math.BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
}





