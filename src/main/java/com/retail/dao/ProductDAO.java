/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.retail.dao;
import com.retail.model.Product;
import java.util.List;

/**
 *
 * @author Admin
 */
public interface ProductDAO {
    void addProduct(Product product);
    void updateProduct(Product product);
    void deleteProduct(int productId);
    Product getProductById(int productId);
    List<Product> getAllProducts();
    List<Product> searchProductInInventory(String keyword);
    List<Product> getProductsBySupplierId(int supplierId);
    public int addProductWithStockEntry(String productName, int supplierId, String unit, String category, String barcode, double purchasePrice, double price);
    public Product getProductByName(String productName);
    public boolean updateStockQuantity(int productId, int quantityChange);
}
