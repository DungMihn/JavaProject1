/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.retail.dao;

import com.retail.model.Inventory;
import java.util.List;
/**
 *
 * @author Admin
 */
public interface InventoryDAO {
    void addInventory(Inventory inventory);

    // Lấy thông tin tồn kho theo ID
    Inventory getInventoryById(int inventoryId);

    // Lấy thông tin tồn kho theo productId
    Inventory getInventoryByProductId(int productId);

    // Lấy tất cả các bản ghi tồn kho
    List<Inventory> getAllInventories();

    // Cập nhật thông tin tồn kho
    void updateInventory(Inventory inventory);

    // Xóa một bản ghi tồn kho theo ID
    void deleteInventory(int inventoryId);

    // Cập nhật số lượng tồn kho cho một sản phẩm
    void updateStockQuantity(int productId, int newQuantity);
}
