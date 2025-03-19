/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.retail.dao;

import com.retail.model.StockEntry;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
/**
 *
 * @author Admin
 */
public interface StockEntryDAO {
    // Thêm một bản ghi nhập kho mới
    int addStockEntry(StockEntry stockEntry);

    // Lấy thông tin nhập kho theo ID
    StockEntry getStockEntryById(int stockEntryId);

    // Lấy tất cả các bản ghi nhập kho
    List<StockEntry> getAllStockEntries();

    // Cập nhật thông tin nhập kho
    boolean updateStockEntry(StockEntry stockEntry);

    // Xóa một bản ghi nhập kho
    void deleteStockEntry(int stockEntryId);
    
    Map<String, Object> getStockEntryDetails(int stockEntryId);
    
    //
    public int getNextStockEntryId();
    
    //
    public boolean isStockEntryExist(int stockEntryId);
    
    //
    public List<StockEntry> getFilteredStockEntries(Integer supplierId, LocalDate fromDate, LocalDate toDate);
    
}
