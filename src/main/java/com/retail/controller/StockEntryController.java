/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.controller;

import com.retail.model.StockEntry;
import com.retail.service.StockEntryService;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author Admin
 */
public class StockEntryController {
    private final StockEntryService stockEntryService = new StockEntryService();


    // Thêm nhập kho mới
    public void addStockEntry(int productId, int supplierId, int quantity, double purchasePrice) {
        StockEntry stockEntry = new StockEntry(0, productId, supplierId, quantity, purchasePrice, LocalDateTime.now());
        stockEntryService.addStockEntry(stockEntry);
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
    public void updateStockEntry(int stockEntryId, int productId, int supplierId, int quantity, double purchasePrice) {
        StockEntry stockEntry = new StockEntry(stockEntryId, productId, supplierId, quantity, purchasePrice, LocalDateTime.now());
        stockEntryService.updateStockEntry(stockEntry);
    }

    // Xóa nhập kho
    public void deleteStockEntry(int stockEntryId) {
        stockEntryService.deleteStockEntry(stockEntryId);
    }
}
