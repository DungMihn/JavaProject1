/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.controller;
import com.retail.model.Product;
import com.retail.service.ProductService;
import java.util.List;

/**
 *
 * @author Admin
 */
public class ProductController {
    private final ProductService productService = new ProductService();

    // Thêm sản phẩm mới
    public void addProduct(String name, String category, double price, String unit, int stockQuantity, String barcode, int supplierId) {
        Product product = new Product();
        product.setName(name);
        product.setCategory(category);
        product.setPrice(price);
        product.setUnit(unit);
        product.setStockQuantity(stockQuantity);
        product.setBarcode(barcode);
        product.setSupplierId(supplierId);
        productService.addProduct(product);
    }

    // Lấy danh sách sản phẩm
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    // Lấy thông tin sản phẩm theo ID
    public Product getProductById(int productId) {
        return productService.getProductById(productId);
    }

    // Xóa sản phẩm
    public void deleteProduct(int productId) {
        productService.deleteProduct(productId);
    }
}
