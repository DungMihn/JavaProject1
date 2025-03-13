/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.dao;

import com.retail.model.Inventory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class InventoryDAOImpl implements InventoryDAO {

    private static final String INSERT_INVENTORY = "INSERT INTO Inventory (product_id, stock_quantity) VALUES (?, ?)";
    private static final String SELECT_ALL_INVENTORIES = "EXEC sp_GetInventoryWithProduct;";
    private static final String SELECT_INVENTORY_BY_ID = "SELECT * FROM Inventory WHERE inventory_id = ?";
    private static final String SELECT_INVENTORY_BY_PRODUCT_ID = "SELECT * FROM Inventory WHERE product_id = ?";
    private static final String UPDATE_INVENTORY = "UPDATE Inventory SET product_id = ?, stock_quantity = ?, last_updated = ? WHERE inventory_id = ?";
    private static final String DELETE_INVENTORY = "DELETE FROM Inventory WHERE inventory_id = ?";
    private static final String UPDATE_STOCK_QUANTITY = "UPDATE Inventory SET stock_quantity = ?, last_updated = ? WHERE product_id = ?";

    @Override
    public void addInventory(Inventory inventory) {
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(INSERT_INVENTORY)) {
            pstmt.setInt(1, inventory.getProductId());
            pstmt.setInt(2, inventory.getStockQuantity());
            pstmt.executeUpdate();
            System.out.println("✅ Thêm tồn kho thành công!");
        } catch (SQLException e) {
            System.out.println("❌ Lỗi thêm tồn kho: " + e.getMessage());
        }
    }

    @Override
    public Inventory getInventoryById(int inventoryId) {
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(SELECT_INVENTORY_BY_ID)) {
            pstmt.setInt(1, inventoryId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Inventory(
                        rs.getInt("inventory_id"),
                        rs.getInt("product_id"),
                        rs.getInt("stock_quantity"),
                        rs.getObject("last_updated", LocalDateTime.class)
                );
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi khi lấy thông tin tồn kho: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Inventory getInventoryByProductId(int productId) {
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(SELECT_INVENTORY_BY_PRODUCT_ID)) {
            pstmt.setInt(1, productId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Inventory(
                        rs.getInt("inventory_id"),
                        rs.getInt("product_id"),
                        rs.getInt("stock_quantity"),
                        rs.getObject("last_updated", LocalDateTime.class)
                );
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi khi lấy thông tin tồn kho theo productId: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Inventory> getAllInventories() {
        List<Inventory> inventoryList = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(SELECT_ALL_INVENTORIES); ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Inventory inventory = new Inventory(
                        rs.getInt("inventory_id"),
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getInt("stock_quantity"),
                        rs.getObject("last_updated", LocalDateTime.class) // ❌ Xóa dấu phẩy ở đây
                );

                inventoryList.add(inventory);
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi khi lấy danh sách tồn kho: " + e.getMessage());
        }
        return inventoryList;
    }

    @Override
    public void updateInventory(Inventory inventory) {
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(UPDATE_INVENTORY)) {
            pstmt.setInt(1, inventory.getProductId());
            pstmt.setInt(2, inventory.getStockQuantity());
            pstmt.setObject(3, inventory.getLastUpdated());
            pstmt.setInt(4, inventory.getInventoryId());
            pstmt.executeUpdate();
            System.out.println("✅ Cập nhật tồn kho thành công!");
        } catch (SQLException e) {
            System.out.println("❌ Lỗi cập nhật tồn kho: " + e.getMessage());
        }
    }

    @Override
    public void deleteInventory(int inventoryId) {
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(DELETE_INVENTORY)) {
            pstmt.setInt(1, inventoryId);
            pstmt.executeUpdate();
            System.out.println("✅ Xóa tồn kho thành công!");
        } catch (SQLException e) {
            System.out.println("❌ Lỗi xóa tồn kho: " + e.getMessage());
        }
    }

    @Override
   public boolean updateStockQuantity(int productId, int quantityChange) {
        String updateQuery = "UPDATE Inventory SET stock_quantity = stock_quantity + ?, last_updated = SYSDATETIME() WHERE product_id = ?";
        String deleteQuery = "DELETE FROM Inventory WHERE product_id = ? AND stock_quantity <= 0";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
             PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery)) {

            // Cập nhật stock_quantity
            updateStmt.setInt(1, quantityChange);
            updateStmt.setInt(2, productId);
            int affectedRows = updateStmt.executeUpdate();

            // Xóa bản ghi nếu stock_quantity về 0
            deleteStmt.setInt(1, productId);
            deleteStmt.executeUpdate();

            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("❌ Lỗi xuất kho: " + e.getMessage());
        }
        return false;
    }
}
