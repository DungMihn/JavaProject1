/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.model;

/**
 *
 * @author Admin
 */
public class StockEntryDetail {
    private int stockEntryDetailId;
    private int stockEntryId;
    private int productId;
    private String productName;
    private int quantity;
    private double purchasePrice;

    // Constructors
    public StockEntryDetail() {
    }

    public StockEntryDetail(int stockEntryDetailId, int stockEntryId, int productId, String productName, int quantity, double purchasePrice) {
        this.stockEntryDetailId = stockEntryDetailId;
        this.stockEntryId = stockEntryId;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.purchasePrice = purchasePrice;
    }

    // Getters and Setters
    public int getStockEntryDetailId() {
        return stockEntryDetailId;
    }

    public void setStockEntryDetailId(int stockEntryDetailId) {
        this.stockEntryDetailId = stockEntryDetailId;
    }

    public int getStockEntryId() {
        return stockEntryId;
    }

    public void setStockEntryId(int stockEntryId) {
        this.stockEntryId = stockEntryId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }
}

