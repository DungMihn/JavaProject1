/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.model;

/**
 *
 * @author Admin
 */
public class StockEntryDetailChange {
    private int productId;
    private int quantity;
    private String action; // "DELETE" hoặc "UPDATE"

    public StockEntryDetailChange(int productId, int quantity, String action) {
        this.productId = productId;
        this.quantity = quantity;
        this.action = action;
    }

    // Getter và Setter
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

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
