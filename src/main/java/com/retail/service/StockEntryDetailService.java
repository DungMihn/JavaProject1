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
    public void addStockEntryDetail(StockEntryDetail stockEntryDetail) {
        stockEntryDetailDAO.addStockEntryDetail(stockEntryDetail);
        System.out.println("✅ Chi tiết nhập kho đã được thêm thành công!");
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
    public void updateStockEntryDetail(StockEntryDetail stockEntryDetail) {
        stockEntryDetailDAO.updateStockEntryDetail(stockEntryDetail);
        System.out.println("✅ Chi tiết nhập kho đã được cập nhật thành công!");
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
}
