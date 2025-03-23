/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.controller;

import com.retail.model.Product;
import com.retail.model.StockEntry;
import com.retail.service.StockEntryService;
import java.time.LocalDate;
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

    public int getNextStockEntryId() {
        return stockEntryService.getNextStockEntryId();
    }

    public boolean isStockEntryExist(int stockEntryId) {
        return stockEntryService.isStockEntryExist(stockEntryId);
    }

    //
    public List<StockEntry> getFilteredStockEntries(Integer supplierId, LocalDate fromDate, LocalDate toDate) {
        return stockEntryService.getFilteredStockEntries(supplierId, fromDate, toDate);
    }

    //
    public Product getProductById(int productId) {
        return stockEntryService.getProductById(productId);
    }
    //
    public int addProductWithStockEntry(String productName, int supplierId, String unit, String category, String barcode, double purchasePrice, double price){
        return stockEntryService.addProductWithStockEntry(productName, supplierId, unit, category, barcode, purchasePrice, price);
    }
}
