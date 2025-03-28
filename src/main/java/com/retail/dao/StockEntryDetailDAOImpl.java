/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.retail.model.StockEntryDetail;

/**
 *
 * @author Admin
 */
public class StockEntryDetailDAOImpl implements StockEntryDetailDAO {

    private static final String INSERT_STOCK_ENTRY_DETAIL = "INSERT INTO Stock_Entry_Detail (stock_entry_id, product_id, quantity, purchase_price) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_STOCK_ENTRY_DETAIL = "UPDATE StockEntryDetail SET stock_entry_id = ?, product_id = ?, quantity = ?, purchase_price = ? WHERE stock_entry_detail_id = ?";
    private static final String DELETE_STOCK_ENTRY_DETAIL = "DELETE FROM Stock_Entry_Detail WHERE stock_entry_detail_id = ?";
    private static final String GET_STOCK_ENTRY_DETAIL_BY_ID = "SELECT * FROM Stock_Entry_Detail WHERE stock_entry_detail_id = ?";
    private static final String GET_ALL_STOCK_ENTRY_DETAILS = "SELECT * FROM Stock_Entry_Detail";
    private static final String DELETE_STOCK_ENTRY_DETAIL_BY_PRODUCT_ID = "DELETE FROM StockEntryDetail WHERE stock_entry_id = ? AND product_id = ?";

    @Override
    public boolean addStockEntryDetail(StockEntryDetail stockEntryDetail) {
        try (Connection conn = DatabaseConnection.getConnection(); CallableStatement cstmt = conn.prepareCall("{CALL sp_AddStockEntryDetail(?, ?, ?, ?)}")) {

            cstmt.setInt(1, stockEntryDetail.getStockEntryId());
            cstmt.setInt(2, stockEntryDetail.getProductId());
            cstmt.setInt(3, stockEntryDetail.getQuantity());
            cstmt.setDouble(4, stockEntryDetail.getPurchasePrice());
            cstmt.executeUpdate();

            return true;

        } catch (SQLException e) {
            System.out.println("❌ Lỗi thêm chi tiết nhập kho: " + e.getMessage());
        }
        return true;
    }

    @Override
    public boolean updateStockEntryDetail(StockEntryDetail stockEntryDetail) {
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(UPDATE_STOCK_ENTRY_DETAIL)) {

            pstmt.setInt(1, stockEntryDetail.getStockEntryId());
            pstmt.setInt(2, stockEntryDetail.getProductId());
            pstmt.setInt(3, stockEntryDetail.getQuantity());
            pstmt.setDouble(4, stockEntryDetail.getPurchasePrice());
            pstmt.setInt(5, stockEntryDetail.getStockEntryDetailId());
            int affectedRows = pstmt.executeUpdate();

            pstmt.executeUpdate();
            System.out.println("✅ Cập nhật chi tiết nhập kho thành công!");

            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("❌ Lỗi khi cập nhật chi tiết nhập kho: " + e.getMessage());
        }
        return false;
    }

    @Override
    public void deleteStockEntryDetail(int stockEntryDetailId) {
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(DELETE_STOCK_ENTRY_DETAIL)) {

            pstmt.setInt(1, stockEntryDetailId);
            pstmt.executeUpdate();
            System.out.println("✅ Xóa chi tiết nhập kho thành công!");

        } catch (SQLException e) {
            System.out.println("❌ Lỗi khi xóa chi tiết nhập kho: " + e.getMessage());
        }
    }

    @Override
    public StockEntryDetail getStockEntryDetailById(int stockEntryDetailId) {
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(GET_STOCK_ENTRY_DETAIL_BY_ID)) {

            pstmt.setInt(1, stockEntryDetailId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new StockEntryDetail(
                        rs.getInt("stock_entry_detail_id"),
                        rs.getInt("stock_entry_id"),
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getInt("quantity"),
                        rs.getDouble("purchase_price")
                );
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi khi lấy chi tiết nhập kho theo ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<StockEntryDetail> getAllStockEntryDetails() {
        List<StockEntryDetail> stockEntryDetails = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(GET_ALL_STOCK_ENTRY_DETAILS)) {

            while (rs.next()) {
                StockEntryDetail stockEntryDetail = new StockEntryDetail(
                        rs.getInt("stock_entry_detail_id"),
                        rs.getInt("stock_entry_id"),
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getInt("quantity"),
                        rs.getDouble("purchase_price")
                );
                stockEntryDetails.add(stockEntryDetail);
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi khi lấy danh sách chi tiết nhập kho: " + e.getMessage());
        }
        return stockEntryDetails;
    }

    // Lấy danh sách chi tiết nhập kho theo ID của StockEntry
    @Override
    public List<StockEntryDetail> getStockEntryDetailsByStockEntryId(int stockEntryId) {
        List<StockEntryDetail> details = new ArrayList<>();
        String query = "{CALL GetStockEntryDetails(?)}";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, stockEntryId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    StockEntryDetail detail = new StockEntryDetail();
                    detail.setStockEntryDetailId(rs.getInt("stock_entry_detail_id"));
                    detail.setStockEntryId(rs.getInt("stock_entry_id"));
                    detail.setProductId(rs.getInt("product_id"));
                    detail.setProductName(rs.getString("product_name"));
                    detail.setCategory(rs.getString("category"));
                    detail.setUnit(rs.getString("unit"));
                    detail.setBarcode(rs.getString("barcode"));
                    detail.setQuantity(rs.getInt("quantity"));
                    detail.setPurchasePrice(rs.getDouble("purchase_price"));
                    details.add(detail);
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi khi lấy danh sách chi tiết nhập kho: " + e.getMessage());
        }
        return details;
    }

    //
    @Override
    public StockEntryDetail getStockEntryDetailByStockEntryIdAndProductId(int stockEntryId, int productId) {
        StockEntryDetail detail = null;
        String query = "SELECT * FROM StockEntryDetail WHERE stock_entry_id = ? AND product_id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, stockEntryId);
            pstmt.setInt(2, productId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    detail = new StockEntryDetail();
                    detail.setStockEntryDetailId(rs.getInt("stock_entry_detail_id"));
                    detail.setStockEntryId(rs.getInt("stock_entry_id"));
                    detail.setProductId(rs.getInt("product_id"));
                    detail.setQuantity(rs.getInt("quantity"));
                    detail.setPurchasePrice(rs.getDouble("purchase_price"));
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi khi lấy chi tiết nhập kho: " + e.getMessage());
        }

        return detail;
    }

    @Override
    public boolean deleteStockEntryDetailByProductId(int stockEntryId, int productId) {
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(DELETE_STOCK_ENTRY_DETAIL_BY_PRODUCT_ID)) {

            pstmt.setInt(1, stockEntryId);
            pstmt.setInt(2, productId);
            int affectedRows = pstmt.executeUpdate();
            pstmt.executeUpdate();

            pstmt.executeUpdate();

            System.out.println("✅ Xóa chi tiết nhập kho thành công!");

            return affectedRows > 0;

        } catch (SQLException e) {
            System.out.println("❌ Lỗi khi xóa chi tiết nhập kho: " + e.getMessage());
        }
        return false;
    }
}
