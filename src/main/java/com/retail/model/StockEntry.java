/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.model;

import java.time.LocalDateTime;

/**
 *
 * @author Admin
 */
public class StockEntry {
     private int stockEntryId;       
    private int productId;          
    private int supplierId;        
    private int quantity;           
    private double purchasePrice;   
    private LocalDateTime entryDate; 

    // Constructor
    public StockEntry() {
    }

    public StockEntry(int stockEntryId, int productId, int supplierId, int quantity, double purchasePrice, LocalDateTime entryDate) {
        this.stockEntryId = stockEntryId;
        this.productId = productId;
        this.supplierId = supplierId;
        this.quantity = quantity;
        this.purchasePrice = purchasePrice;
        this.entryDate = entryDate;
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

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
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

    public LocalDateTime getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(LocalDateTime entryDate) {
        this.entryDate = entryDate;
    }
    
    
}
