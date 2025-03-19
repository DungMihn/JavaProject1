/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.service;

import com.retail.dao.StockEntryDetailDAOImpl;
import com.retail.model.StockEntryDetail;
import java.util.List;

/**
 *
 * @author Admin
 */
public class StockEntryDetailService {
    private final StockEntryDetailDAOImpl stockEntryDetailDAO = new StockEntryDetailDAOImpl();

    // Thêm chi tiết nhập kho mới
    public boolean addStockEntryDetail(StockEntryDetail stockEntryDetail) {
        return stockEntryDetailDAO.addStockEntryDetail(stockEntryDetail);
    }

    // Lấy danh sách tất cả chi tiết nhập kho
    public List<StockEntryDetail> getAllStockEntryDetails() {
        return stockEntryDetailDAO.getAllStockEntryDetails();
    }

    // Lấy chi tiết nhập kho theo ID
    public StockEntryDetail getStockEntryDetailById(int stockEntryDetailId) {
        return stockEntryDetailDAO.getStockEntryDetailById(stockEntryDetailId);
    }

    // Cập nhật thông tin chi tiết nhập kho
    public boolean updateStockEntryDetail(StockEntryDetail stockEntryDetail) {
        return stockEntryDetailDAO.updateStockEntryDetail(stockEntryDetail);
    }

    // Xóa chi tiết nhập kho theo ID
    public void deleteStockEntryDetail(int stockEntryDetailId) {
        stockEntryDetailDAO.deleteStockEntryDetail(stockEntryDetailId);
        System.out.println("✅ Chi tiết nhập kho đã được xóa thành công!");
    }

    // Lấy danh sách chi tiết nhập kho theo ID của StockEntry
    public List<StockEntryDetail> getStockEntryDetailsByStockEntryId(int stockEntryId) {
        return stockEntryDetailDAO.getStockEntryDetailsByStockEntryId(stockEntryId);
    }
    
    //
    public StockEntryDetail getStockEntryDetailByStockEntryIdAndProductId(int stockEntryId, int productId){
        return stockEntryDetailDAO.getStockEntryDetailByStockEntryIdAndProductId(stockEntryId, productId);
    }
    
    //
    public boolean deleteStockEntryDetailByProductId(int stockEntryId, int productId){
        return stockEntryDetailDAO.deleteStockEntryDetailByProductId(stockEntryId, productId);
    }
}
