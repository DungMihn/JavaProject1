/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.controller;

import com.retail.model.Inventory;
import com.retail.model.Product;
import com.retail.service.InventoryService;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Admin
 */
public class InventoryController {

    private final InventoryService inventoryService = new InventoryService();

    // Thêm tồn kho mới
    public boolean addInventory(int productId, int stockQuantity) {
        try {
            Inventory inventory = new Inventory();
            inventory.setProductId(productId);
            inventory.setStockQuantity(stockQuantity);
            inventoryService.addInventory(inventory);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    // Lấy danh sách tồn kho
    public List<Inventory> getAllInventories() {
        return inventoryService.getAllInventories();
    }

    // Lấy thông tin tồn kho theo ID
    public Inventory getInventoryById(int inventoryId) {
        return inventoryService.getInventoryById(inventoryId);
    }

    // Lấy thông tin tồn kho theo productId
    public Inventory getInventoryByProductId(int productId) {
        return inventoryService.getInventoryByProductId(productId);
    }

    // Cập nhật tồn kho
    public void updateInventory(int inventoryId, int productId, int stockQuantity) {
        Inventory inventory = new Inventory();
        inventory.setInventoryId(inventoryId);
        inventory.setProductId(productId);
        inventory.setStockQuantity(stockQuantity);
        inventoryService.updateInventory(inventory);
    }

    // Xóa tồn kho
    public void deleteInventory(int inventoryId) {
        inventoryService.deleteInventory(inventoryId);
    }

    // Cập nhật số lượng tồn kho
    public boolean updateStockQuantity(int productId, int newQuantity) {
        return inventoryService.updateStockQuantity(productId, newQuantity);
    }

    //
    public List<Inventory> searchInventoryByProductName(String productName) {
        return inventoryService.searchInventoryByProductName(productName);
    }
    //

    public List<Product> getLowStockProducts() {
        return inventoryService.getLowStockProducts();
    }

    //
    public void showLowStockWarning() {
        inventoryService.showLowStockWarning();
    }

    //
    public List<Inventory> getInventoryReport(String startDate, String endDate) {
        return inventoryService.getInventoryReport(startDate, endDate);
    }

    //
    public List<Inventory> getFilteredInventories(LocalDate fromDate, LocalDate toDate) {
        return inventoryService.getFilteredInventories(fromDate, toDate);
    }
}
