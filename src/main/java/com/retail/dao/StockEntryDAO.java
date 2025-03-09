/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.retail.dao;

import com.retail.model.StockEntry;
import java.util.List;
/**
 *
 * @author Admin
 */
public interface StockEntryDAO {
    // Thêm một bản ghi nhập kho mới
    void addStockEntry(StockEntry stockEntry);

    // Lấy thông tin nhập kho theo ID
    StockEntry getStockEntryById(int stockEntryId);

    // Lấy tất cả các bản ghi nhập kho
    List<StockEntry> getAllStockEntries();

    // Cập nhật thông tin nhập kho
    void updateStockEntry(StockEntry stockEntry);

    // Xóa một bản ghi nhập kho
    void deleteStockEntry(int stockEntryId);
}
