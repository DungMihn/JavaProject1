/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.dao;

import com.retail.model.StockEntry;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Admin
 */
public class StockEntryDAOImpl implements StockEntryDAO{
 private static final String INSERT_STOCK_ENTRY = "INSERT INTO Stock_Entry (product_id, supplier_id, quantity, purchase_price, entry_date) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_ALL_STOCK_ENTRIES = "EXEC GetStockEntryDetails;";
    private static final String UPDATE_STOCK_ENTRY = "UPDATE Stock_Entry SET product_id = ?, supplier_id = ?, quantity = ?, purchase_price = ?, entry_date = ? WHERE stock_entry_id = ?";
    private static final String DELETE_STOCK_ENTRY = "DELETE FROM Stock_Entry WHERE stock_entry_id = ?";
    private static final String GET_STOCK_ENTRY_BY_ID = "SELECT * FROM Stock_Entry WHERE stock_entry_id = ?";

    @Override
    public void addStockEntry(StockEntry stockEntry) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_STOCK_ENTRY)) {
            pstmt.setInt(1, stockEntry.getProductId());
            pstmt.setInt(2, stockEntry.getSupplierId());
            pstmt.setInt(3, stockEntry.getQuantity());
            pstmt.setDouble(4, stockEntry.getPurchasePrice());
            pstmt.setObject(5, stockEntry.getEntryDate());
            pstmt.executeUpdate();
            System.out.println("✅ Thêm nhập kho thành công!");
        } catch (SQLException e) {
            System.out.println("❌ Lỗi thêm nhập kho: " + e.getMessage());
        }
    }

    @Override
    public void updateStockEntry(StockEntry stockEntry) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_STOCK_ENTRY)) {
            pstmt.setInt(1, stockEntry.getProductId());
            pstmt.setInt(2, stockEntry.getSupplierId());
            pstmt.setInt(3, stockEntry.getQuantity());
            pstmt.setDouble(4, stockEntry.getPurchasePrice());
            pstmt.setObject(5, stockEntry.getEntryDate());
            pstmt.setInt(6, stockEntry.getStockEntryId());
            pstmt.executeUpdate();
            System.out.println("✅ Cập nhật nhập kho thành công!");
        } catch (SQLException e) {
            System.out.println("❌ Lỗi cập nhật nhập kho: " + e.getMessage());
        }
    }

    @Override
    public void deleteStockEntry(int stockEntryId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELETE_STOCK_ENTRY)) {
            pstmt.setInt(1, stockEntryId);
            pstmt.executeUpdate();
            System.out.println("✅ Xóa nhập kho thành công!");
        } catch (SQLException e) {
            System.out.println("❌ Lỗi xóa nhập kho: " + e.getMessage());
        }
    }

    @Override
    public StockEntry getStockEntryById(int stockEntryId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(GET_STOCK_ENTRY_BY_ID)) {
            pstmt.setInt(1, stockEntryId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new StockEntry(
                        rs.getInt("stock_entry_id"),
                        rs.getInt("product_id"),
                        rs.getInt("supplier_id"),
                        rs.getInt("quantity"),
                        rs.getDouble("purchase_price"),
                        rs.getObject("entry_date", LocalDateTime.class)
                );
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi khi lấy thông tin nhập kho: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<StockEntry> getAllStockEntries() {
        List<StockEntry> stockEntries = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_STOCK_ENTRIES)) {

            while (rs.next()) {
                StockEntry stockEntry = new StockEntry(
                        rs.getInt("stock_entry_id"),
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getInt("supplier_id"),
                        rs.getInt("quantity"),
                        rs.getDouble("purchase_price"),
                        rs.getObject("entry_date", LocalDateTime.class)
                );
                stockEntries.add(stockEntry);
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi truy vấn nhập kho: " + e.getMessage());
        }
        return stockEntries;
    }
}
