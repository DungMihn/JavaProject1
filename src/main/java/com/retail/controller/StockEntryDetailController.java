/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.controller;

import com.retail.model.StockEntryDetail;
import com.retail.service.StockEntryDetailService;
import java.util.List;



/**
 *
 * @author Admin
 */
public class StockEntryDetailController {
    private final StockEntryDetailService stockEntryDetailService = new StockEntryDetailService();

    // Thêm chi tiết nhập kho mới
    public void addStockEntryDetail(StockEntryDetail stockEntryDetail) {
        stockEntryDetailService.addStockEntryDetail(stockEntryDetail);
    }

    // Lấy thông tin chi tiết nhập kho theo ID
    public StockEntryDetail getStockEntryDetailById(int stockEntryDetailId) {
        return stockEntryDetailService.getStockEntryDetailById(stockEntryDetailId);
    }

    // Lấy tất cả chi tiết nhập kho
    public List<StockEntryDetail> getAllStockEntryDetails() {
        return stockEntryDetailService.getAllStockEntryDetails();
    }

    // Lấy chi tiết nhập kho theo ID của StockEntry
    public List<StockEntryDetail> getStockEntryDetailsByStockEntryId(int stockEntryId) {
        return stockEntryDetailService.getStockEntryDetailsByStockEntryId(stockEntryId);
    }

    // Cập nhật chi tiết nhập kho
    public void updateStockEntryDetail(StockEntryDetail stockEntryDetail) {
        stockEntryDetailService.updateStockEntryDetail(stockEntryDetail);
    }

    // Xóa chi tiết nhập kho
    public void deleteStockEntryDetail(int stockEntryDetailId) {
        stockEntryDetailService.deleteStockEntryDetail(stockEntryDetailId);
    }
}
