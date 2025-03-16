/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.dao;

import com.retail.model.StockEntry;
import java.sql.CallableStatement;
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
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StockEntryDAOImpl implements StockEntryDAO {

    private static final String INSERT_STOCK_ENTRY = "{CALL sp_AddStockEntry(?, ?, ?)}";
    private static final String SELECT_ALL_STOCK_ENTRIES = "SELECT * FROM StockEntry";
    private static final String GET_STOCK_ENTRY_DETAILS = "{CALL sp_GetStockEntryDetails(?)}";
    private static final String DELETE_STOCK_ENTRY = "DELETE FROM StockEntry WHERE stock_entry_id = ?";
    private static final String GET_STOCK_ENTRY_BY_ID = "{CALL GetStockEntryById(?)}";
    private static final String UPDATE_STOCK_ENTRY = "UPDATE StockEntry SET supplier_id = ?, employee_id = ? WHERE stock_entry_id = ?";

    @Override
    public int addStockEntry(StockEntry stockEntry) {
        try (Connection conn = DatabaseConnection.getConnection(); CallableStatement cstmt = conn.prepareCall(INSERT_STOCK_ENTRY)) {

            cstmt.setInt(1, stockEntry.getSupplierId());
            cstmt.setInt(2, stockEntry.getEmployeeId());

            // Đăng ký tham số OUTPUT
            cstmt.registerOutParameter(3, Types.INTEGER);

            cstmt.execute();

            // Lấy giá trị của tham số OUTPUT
            int stockEntryId = cstmt.getInt(3);
            System.out.println("✅ Thêm nhập kho thành công! ID: " + stockEntryId);
            return stockEntryId;
        } catch (SQLException e) {
            System.out.println("❌ Lỗi thêm nhập kho: " + e.getMessage());
        }
        return -1; // Trả về -1 nếu có lỗi
    }

    @Override
    public void deleteStockEntry(int stockEntryId) {
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(DELETE_STOCK_ENTRY)) {
            pstmt.setInt(1, stockEntryId);
            pstmt.executeUpdate();
            System.out.println("✅ Xóa nhập kho thành công!");
        } catch (SQLException e) {
            System.out.println("❌ Lỗi xóa nhập kho: " + e.getMessage());
        }
    }

    @Override
    public StockEntry getStockEntryById(int stockEntryId) {
        StockEntry stockEntry = null;
        String query = "{CALL GetStockEntryById(?)}"; // Giả sử có stored procedure GetStockEntryById

        try (Connection conn = DatabaseConnection.getConnection(); CallableStatement cstmt = conn.prepareCall(query)) {

            cstmt.setInt(1, stockEntryId);

            // Thực thi stored procedure
            try (ResultSet rs = cstmt.executeQuery()) {
                if (rs.next()) {
                    stockEntry = new StockEntry();
                    stockEntry.setStockEntryId(rs.getInt("stock_entry_id"));
                    stockEntry.setEmployeeId(rs.getInt("employee_id"));
                    stockEntry.setSupplierId(rs.getInt("supplier_id"));
                    stockEntry.setEntryDate(rs.getTimestamp("entry_date").toLocalDateTime());
                    stockEntry.setEmployeeName(rs.getString("employee_name"));
                    stockEntry.setSupplierName(rs.getString("supplier_name"));
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi khi lấy thông tin nhập hàng: " + e.getMessage());
        }

        return stockEntry;
    }

    @Override
    public List<StockEntry> getAllStockEntries() {
        List<StockEntry> stockEntries = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(SELECT_ALL_STOCK_ENTRIES)) {

            while (rs.next()) {
                StockEntry stockEntry = new StockEntry(
                        rs.getInt("stock_entry_id"),
                        rs.getInt("supplier_id"),
                        rs.getInt("employee_id"),
                        rs.getObject("entry_date", LocalDateTime.class)
                );
                stockEntries.add(stockEntry);
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi truy vấn nhập kho: " + e.getMessage());
        }
        return stockEntries;
    }

    @Override
    public boolean updateStockEntry(StockEntry stockEntry) {
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(UPDATE_STOCK_ENTRY)) {

            pstmt.setInt(1, stockEntry.getSupplierId()); // Cập nhật supplier_id
            pstmt.setInt(2, stockEntry.getEmployeeId()); // Cập nhật employee_id
            pstmt.setInt(3, stockEntry.getStockEntryId()); // Điều kiện WHERE

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.out.println("❌ Lỗi cập nhật nhập kho: " + e.getMessage());
        }
        return false;
    }

    @Override
    public Map<String, Object> getStockEntryDetails(int stockEntryId) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> details = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection(); CallableStatement cstmt = conn.prepareCall(GET_STOCK_ENTRY_DETAILS)) {

            cstmt.setInt(1, stockEntryId);

            // Thực thi stored procedure
            boolean hasResults = cstmt.execute();

            // Lấy thông tin chung của StockEntry
            if (hasResults) {
                try (ResultSet rs = cstmt.getResultSet()) {
                    if (rs.next()) {
                        result.put("stock_entry_id", rs.getInt("stock_entry_id"));
                        result.put("entry_date", rs.getTimestamp("entry_date"));
                        result.put("supplier_name", rs.getString("supplier_name"));
                        result.put("employee_name", rs.getString("employee_name"));
                    }
                }
            }

            // Lấy thông tin chi tiết của StockEntryDetail
            if (cstmt.getMoreResults()) {
                try (ResultSet rs = cstmt.getResultSet()) {
                    while (rs.next()) {
                        Map<String, Object> detail = new HashMap<>();
                        detail.put("stock_entry_detail_id", rs.getInt("stock_entry_detail_id"));
                        detail.put("product_name", rs.getString("product_name"));
                        detail.put("quantity", rs.getInt("quantity"));
                        detail.put("purchase_price", rs.getDouble("purchase_price"));
                        details.add(detail);
                    }
                }
            }

            result.put("details", details); // Thêm danh sách chi tiết vào kết quả
        } catch (SQLException e) {
            System.out.println("❌ Lỗi lấy thông tin nhập hàng: " + e.getMessage());
        }

        return result;
    }
}
