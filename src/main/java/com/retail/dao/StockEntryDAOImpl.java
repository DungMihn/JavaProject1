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
/**
 *
 * @author Admin
 */
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StockEntryDAOImpl implements StockEntryDAO {

    private static final String INSERT_STOCK_ENTRY = "{CALL sp_AddStockEntry(?, ?, ?)}";
    private static final String SELECT_ALL_STOCK_ENTRIES = "EXEC GetAllStockEntryDetails";
    private static final String GET_STOCK_ENTRY_DETAILS = "{CALL sp_GetStockEntryDetails(?)}";
    private static final String DELETE_STOCK_ENTRY = "DELETE FROM StockEntry WHERE stock_entry_id = ?";
    private static final String GET_STOCK_ENTRY_BY_ID = "{CALL GetStockEntryById(?)}";
    private static final String UPDATE_STOCK_ENTRY = "UPDATE StockEntry SET supplier_id = ?, employee_id = ?, entry_date = ? WHERE stock_entry_id = ?";
    private static final String GET_NEXT_STOCK_ENTRY_ID = "SELECT IDENT_CURRENT('StockEntry') + IDENT_INCR('StockEntry') AS NextStockEntryId;";

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
                        rs.getString("supplier_name"),
                        rs.getInt("employee_id"),
                        rs.getString("employee_name"),
                        rs.getObject("entry_date", LocalDateTime.class),
                        rs.getDouble("total_price")
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
            // Lấy ngày từ String (dd/MM/yyyy)
            String entryDateString = stockEntry.getEntryDate(); // Ví dụ: "16/03/2025"
            LocalDateTime entryDateTime = null;

            if (entryDateString != null && !entryDateString.isEmpty()) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate entryDate = LocalDate.parse(entryDateString, formatter);

                // Lấy giờ-phút-giây hiện tại
                LocalTime currentTime = LocalTime.now();

                // Kết hợp ngày từ `entryDateString` với giờ hiện tại
                entryDateTime = LocalDateTime.of(entryDate, currentTime);
            }

            // Chuyển thành Timestamp để lưu vào DB
            pstmt.setTimestamp(3, entryDateTime != null ? Timestamp.valueOf(entryDateTime) : null);
            pstmt.setInt(4, stockEntry.getStockEntryId()); // Điều kiện WHERE

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

    //
    @Override
    public int getNextStockEntryId() {
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pstmt = con.prepareStatement(GET_NEXT_STOCK_ENTRY_ID); ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                // Truy cập cột "NextStockEntryId" bằng tên cột
                return rs.getInt("NextStockEntryId");
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi khi lấy ID tiếp theo của StockEntry: " + e.getMessage());
        }
        return -1; // Trả về -1 nếu có lỗi
    }

    @Override
    public boolean isStockEntryExist(int stockEntryId) {
        String query = "SELECT COUNT(*) FROM StockEntry WHERE stock_entry_id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, stockEntryId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi khi kiểm tra ID: " + e.getMessage());
        }
        return false;
    }

    @Override
    public List<StockEntry> getFilteredStockEntries(Integer supplierId, LocalDate fromDate, LocalDate toDate) {
        List<StockEntry> filteredEntries = new ArrayList<>();
        System.out.println("FROM date: " + fromDate); 
        System.out.println("TO date: " + toDate); 

        try (Connection connection = DatabaseConnection.getConnection(); CallableStatement cstmt = connection.prepareCall("{call GetFilteredStockEntryDetails(?, ?, ?)}")) {

            // Thiết lập tham số cho nhà cung cấp
            if (supplierId != null && supplierId != -1) {
                cstmt.setInt(1, supplierId); // Truyền supplier_id nếu không phải "Tất cả"
            } else {
                cstmt.setNull(1, Types.INTEGER); // Truyền NULL nếu chọn "Tất cả"
            }

            // Thiết lập tham số cho ngày bắt đầu
            if (fromDate != null) {
                cstmt.setDate(2, Date.valueOf(fromDate));
            } else {
                cstmt.setNull(2, Types.DATE); // Truyền NULL nếu không chọn ngày bắt đầu
            }

            // Thiết lập tham số cho ngày kết thúc
            if (toDate != null) {
                cstmt.setDate(3, Date.valueOf(toDate));
            } else {
                cstmt.setNull(3, Types.DATE); // Truyền NULL nếu không chọn ngày kết thúc
            }

            // Thực thi stored procedure
            ResultSet rs = cstmt.executeQuery();

            // Đọc kết quả và thêm vào danh sách
            while (rs.next()) {
                StockEntry stockEntry = new StockEntry();
                stockEntry.setStockEntryId(rs.getInt("stock_entry_id"));
                stockEntry.setSupplierName(rs.getString("supplier_name"));
                stockEntry.setEmployeeName(rs.getString("employee_name"));
                stockEntry.setEntryDate(rs.getTimestamp("entry_date").toLocalDateTime());
                stockEntry.setTotalPrice(rs.getDouble("total_price"));
                filteredEntries.add(stockEntry);
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi khi lọc stock entry: " + e.getMessage());
        }

        return filteredEntries;
    }
}
