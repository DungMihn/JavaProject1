/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.controller;

import com.retail.model.StockEntry;
import com.retail.service.StockEntryService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Admin
 */
public class StockEntryController {
    private final StockEntryService stockEntryService = new StockEntryService();

    // Thêm nhập kho mới
    public int addStockEntry(StockEntry stockEntry) {
        return stockEntryService.addStockEntry(stockEntry);
    }

    // Lấy thông tin nhập kho theo ID
    public StockEntry getStockEntryById(int stockEntryId) {
        return stockEntryService.getStockEntryById(stockEntryId);
    }

    // Lấy tất cả nhập kho
    public List<StockEntry> getAllStockEntries() {
        return stockEntryService.getAllStockEntries();
    }

    // Cập nhật nhập kho
    public boolean updateStockEntry(StockEntry stockEntry) {
        return stockEntryService.updateStockEntry(stockEntry);
    }

    // Xóa nhập kho
    public void deleteStockEntry(int stockEntryId) {
        stockEntryService.deleteStockEntry(stockEntryId);
    }
    
    public Map<String, Object> getStockEntryDetails(int stockEntryId) {
        return stockEntryService.getStockEntryDetails(stockEntryId);
    }
}
