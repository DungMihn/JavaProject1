package com.retail.controller;

import com.retail.dao.ProductDAO;
import com.retail.dao.SupplierDAO;
import com.retail.dao.InventoryDAO;
import com.retail.model.Product;
import com.retail.model.Supplier;
import com.retail.model.Inventory;
import java.util.List;

public class ProductController {
    private ProductDAO productDAO;
    private SupplierDAO supplierDAO;
    private InventoryDAO inventoryDAO;

    public ProductController() {
        productDAO = new ProductDAO();
        supplierDAO = new SupplierDAO();
        inventoryDAO = new InventoryDAO();
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
