/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.controller;

import com.retail.dao.InventoryDAOImpl;
import com.retail.dao.ProductDAOImpl;
import com.retail.dao.SupplierDAOImpl;
import com.retail.model.Product;
import com.retail.model.Supplier;
import com.retail.model.Inventory;
import java.util.List;

/**
 *
 * @author Admin
 */
public class ProductController {

    private ProductDAOImpl productDAO;
    private SupplierDAOImpl supplierDAO;
    private InventoryDAOImpl inventoryDAO;

    public ProductController() {
        productDAO = new ProductDAOImpl();
        supplierDAO = new SupplierDAOImpl();
        inventoryDAO = new InventoryDAOImpl();
    }

    public List<Product> getAllProducts() {
        return productDAO.getAllProducts();
    }

    public List<String> getDistinctCategories() {
        return productDAO.getDistinctCategories();
    }

    public List<Product> searchProducts(String keyword) {
        return productDAO.searchProducts(keyword);
    }

    public Supplier getSupplierById(int supplierId) {
        return supplierDAO.getSupplierById(supplierId);
    }

    public Inventory getInventoryByProductId(int productId) {
        return inventoryDAO.getInventoryByProductId(productId);
    }

    public List<com.retail.model.Supplier> getAllSuppliers() {
        return supplierDAO.getAllSuppliers();
    }

    public void updateProduct(Product product) {
        productDAO.updateProduct(product);
    }
}
