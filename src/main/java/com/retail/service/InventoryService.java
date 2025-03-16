/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.service;

import com.retail.dao.InventoryDAOImpl;
import com.retail.model.Inventory;
import com.retail.model.Product;
import java.util.List;
/**
 *
 * @author Admin
 */
public class InventoryService {
    private final InventoryDAOImpl inventoryDAO = new InventoryDAOImpl();

    // Thêm tồn kho
    public void addInventory(Inventory inventory) {
        inventoryDAO.addInventory(inventory);
        System.out.println("✅ Tồn kho đã được thêm thành công!");
    }

    // Lấy tất cả tồn kho
    public List<Inventory> getAllInventories() {
        return inventoryDAO.getAllInventories();
    }

    // Lấy tồn kho theo ID
    public Inventory getInventoryById(int inventoryId) {
        return inventoryDAO.getInventoryById(inventoryId);
    }

    // Lấy tồn kho theo productId
    public Inventory getInventoryByProductId(int productId) {
        return inventoryDAO.getInventoryByProductId(productId);
    }

    // Cập nhật tồn kho
    public void updateInventory(Inventory inventory) {
        inventoryDAO.updateInventory(inventory);
        System.out.println("✅ Tồn kho đã được cập nhật thành công!");
    }

    // Xóa tồn kho
    public void deleteInventory(int inventoryId) {
        inventoryDAO.deleteInventory(inventoryId);
        System.out.println("✅ Tồn kho đã được xóa thành công!");
    }

    // Cập nhật số lượng tồn kho
    public boolean updateStockQuantity(int productId, int quantityChange) {
        return inventoryDAO.updateStockQuantity(productId, quantityChange);
    }
    
    //
    public List<Inventory> searchInventoryByProductName(String productName){
        return inventoryDAO.searchInventoryByProductName(productName);
    }
    
    //
    public List<Product> getLowStockProducts(){
        return inventoryDAO.getLowStockProducts();
    }
    
    //
    public void showLowStockWarning(){
        inventoryDAO.showLowStockWarning();
    }
    //
    public List<Inventory> getInventoryReport(String startDate, String endDate){
        return inventoryDAO.getInventoryReport(startDate, endDate);
    }
}
