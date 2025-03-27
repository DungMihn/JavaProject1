package com.retail.model;

public class Product {
    private int productId;
    private String name;
    private String category;
    private double price;
    private String unit;
    private int supplierId;
    private int stockQuantity;
    private String barcode;
    private double purchasePrice; // Giá nhập kho D thêm mới 

    public Product(int productId, String name, String category, double price, String unit, int stockQuantity, String barcode, int supplierId, double purchasePrice) {
        this.productId = productId;
        this.name = name;
        this.category = category;
        this.price = price;
        this.unit = unit;
        this.stockQuantity = stockQuantity;
        this.barcode = barcode;
        this.supplierId = supplierId;
        this.purchasePrice = purchasePrice;
    }

    // Getters & Setters
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public int getSupplierId() { return supplierId; }
    public void setSupplierId(int supplierId) { this.supplierId = supplierId; }
    public int getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }
    public String getBarcode() { return barcode; }
    public void setBarcode(String barcode) { this.barcode = barcode; }
    public double getPurchasePrice() { return purchasePrice; }
    public void setPurchasePrice(double purchasePrice) { this.purchasePrice = purchasePrice; }
}
