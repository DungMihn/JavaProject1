/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.model;

/**
 *
 * @author Admin
 */
public class Product {
    private int productId;
    private String name;
    private String category;
    private double price;
    private String unit;
    private int supplierId;
    private String supplierName;
    private int stockQuantity;
    private String barcode;
    
    public Product() {
    }

    public Product(int productId, String name, String category, double price, String unit, int supplierId, String supplierName, int stockQuantity, String barcode) {
        this.productId = productId;
        this.name = name;
        this.category = category;
        this.price = price;
        this.unit = unit;
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.stockQuantity = stockQuantity;
        this.barcode = barcode;
    }

    public Product(int productId, String name, String category, double price, String unit, int supplierId, int stockQuantity, String barcode) {
        this.productId = productId;
        this.name = name;
        this.category = category;
        this.price = price;
        this.unit = unit;
        this.supplierId = supplierId;
        this.stockQuantity = stockQuantity;
        this.barcode = barcode;
    }

    

     
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    
}
