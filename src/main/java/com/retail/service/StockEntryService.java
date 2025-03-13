/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.service;

import com.retail.dao.StockEntryDAOImpl;
import com.retail.model.StockEntry;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Admin
 */
public class StockEntryService {
    private final StockEntryDAOImpl stockEntryDAO = new StockEntryDAOImpl();

    // Thêm nhập kho mới
     public int addStockEntry(StockEntry stockEntry) {
        return stockEntryDAO.addStockEntry(stockEntry);
    }

    // Lấy danh sách tất cả nhập kho
    public List<StockEntry> getAllStockEntries() {
        return stockEntryDAO.getAllStockEntries();
    }

    // Lấy nhập kho theo ID
    public StockEntry getStockEntryById(int stockEntryId) {
        return stockEntryDAO.getStockEntryById(stockEntryId);
    }

    // Cập nhật thông tin nhập kho
    public void updateStockEntry(StockEntry stockEntry) {
        stockEntryDAO.updateStockEntry(stockEntry);
        System.out.println("✅ Nhập kho đã được cập nhật thành công!");
    }

    // Xóa nhập kho theo ID
    public void deleteStockEntry(int stockEntryId) {
        stockEntryDAO.deleteStockEntry(stockEntryId);
        System.out.println("✅ Nhập kho đã được xóa thành công!");
    }
    


    public Map<String, Object> getStockEntryDetails(int stockEntryId) {
        return stockEntryDAO.getStockEntryDetails(stockEntryId);
    }

}
