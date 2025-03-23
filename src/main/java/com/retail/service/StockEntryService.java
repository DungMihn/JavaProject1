/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.service;

import com.retail.dao.StockEntryDAOImpl;
import com.retail.model.Product;
import com.retail.model.StockEntry;
import java.time.LocalDate;
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
    public boolean updateStockEntry(StockEntry stockEntry) {
        return stockEntryDAO.updateStockEntry(stockEntry);
    }

    // Xóa nhập kho theo ID
    public void deleteStockEntry(int stockEntryId) {
        stockEntryDAO.deleteStockEntry(stockEntryId);
        System.out.println("✅ Nhập kho đã được xóa thành công!");
    }
    


    public Map<String, Object> getStockEntryDetails(int stockEntryId) {
        return stockEntryDAO.getStockEntryDetails(stockEntryId);
    }
    
    public int getNextStockEntryId(){
        return stockEntryDAO.getNextStockEntryId();
    }
    //
    public boolean isStockEntryExist(int stockEntryId){
        return stockEntryDAO.isStockEntryExist(stockEntryId);
    }
    //
    public List<StockEntry> getFilteredStockEntries(Integer supplierId, LocalDate fromDate, LocalDate toDate) {
        return stockEntryDAO.getFilteredStockEntries(supplierId, fromDate, toDate);
    }
    //
    public Product getProductById(int productId){
        return stockEntryDAO.getProductById(productId);
    }
    //
    public int addProductWithStockEntry(String productName, int supplierId, String unit, String category, String barcode, double purchasePrice, double price){
        return stockEntryDAO.addProductWithStockEntry(productName, supplierId, unit, category, barcode, purchasePrice, price);
    }
}
