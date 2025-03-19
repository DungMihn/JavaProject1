/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.retail.dao;

import com.retail.model.StockEntryDetail;
import java.util.List;

/**
 *
 * @author Admin
 */
public interface StockEntryDetailDAO {
    // Thêm một bản ghi nhập kho mới
    boolean addStockEntryDetail(StockEntryDetail stockEntry);

    // Lấy thông tin nhập kho theo ID
    StockEntryDetail getStockEntryDetailById(int stockEntryId);

    // Lấy tất cả các bản ghi nhập kho
    List<StockEntryDetail> getAllStockEntryDetails();

    // Cập nhật thông tin nhập kho
    boolean updateStockEntryDetail(StockEntryDetail stockEntryDetail);

    // Xóa một bản ghi nhập kho
    void deleteStockEntryDetail(int stockEntryDetailId);
    
    //
    List<StockEntryDetail> getStockEntryDetailsByStockEntryId(int stockEntryId);
    
    //
    public StockEntryDetail getStockEntryDetailByStockEntryIdAndProductId(int stockEntryId, int productId);
    
    //
    boolean deleteStockEntryDetailByProductId(int stockEntryId , int productId);
}
