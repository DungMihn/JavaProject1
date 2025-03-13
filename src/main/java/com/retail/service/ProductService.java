/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.service;

import com.retail.dao.ProductDAOImpl;
import com.retail.model.Product;
import java.util.List;

/**
 *
 * @author Admin
 */
public class ProductService {
    private final ProductDAOImpl productDAO = new ProductDAOImpl();


    // Thêm sản phẩm
    public void addProduct(Product product) {
        productDAO.addProduct(product);
        System.out.println("✅ Sản phẩm đã được thêm thành công!");
    }

    // Lấy tất cả sản phẩm
    public List<Product> getAllProducts() {
            return productDAO.getAllProducts(); 
    }

    // Lấy sản phẩm theo ID
    public Product getProductById(int productId) {
        return productDAO.getProductById(productId);
    }

    // Xóa sản phẩm
    public void deleteProduct(int productId) {
        productDAO.deleteProduct(productId);
        System.out.println("✅ Sản phẩm đã được xóa thành công!");
    }
    
    //
    public List<Product> getProductsBySupplierId(int supplierId){
        return productDAO.getProductsBySupplierId(supplierId); 
    }
    
   //
    public int addProductWithStockEntry(String productName, int supplierId) {
        return productDAO.addProductWithStockEntry(productName,supplierId);
    }
    //
    public Product getProductByName(String productName) {
        return productDAO.getProductByName(productName);
    }
    //
    public List<Product> searchProductInInventory(String keyword){
        return productDAO.searchProductInInventory(keyword);
    }
    //
    public boolean updateStockQuantity(int productId, int quantityChange) {
        return productDAO.updateStockQuantity(productId, quantityChange);
    }
}
