/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Admin
 */
public class Inventory {

    private int inventoryId;
    private int productId;
    private String productName;
    private int stockQuantity;
    private LocalDateTime lastUpdated;

    // Constructor
    public Inventory() {
    }

    public Inventory(int productId, int stockQuantity) {
        this.productId = productId;
        this.stockQuantity = stockQuantity;
    }

    public Inventory(int inventoryId, int productId, int stockQuantity, LocalDateTime lastUpdated) {
        this.inventoryId = inventoryId;
        this.productId = productId;
        this.stockQuantity = stockQuantity;
        this.lastUpdated = lastUpdated;
    }

    public Inventory(int inventoryId, int productId, String productName, int stockQuantity, LocalDateTime lastUpdated) {
        this.inventoryId = inventoryId;
        this.productId = productId;
        this.productName = productName;
        this.stockQuantity = stockQuantity;
        this.lastUpdated = lastUpdated;
    }

    public int getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(int inventoryId) {
        this.inventoryId = inventoryId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getLastUpdatedString() {
        if (lastUpdated == null) {
            return "N/A"; // Trả về "N/A" nếu lastUpdated là null
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return lastUpdated.format(formatter);
    }
    
    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

}
