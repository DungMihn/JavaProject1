/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.dao;

import com.retail.model.Inventory;
import java.sql.*;

public class InventoryDAO {
    private static final String SELECT_INVENTORY_BY_PRODUCT_ID = "SELECT * FROM Inventory WHERE product_id = ?";
    private static final String UPDATE_INVENTORY = "UPDATE Inventory SET stock_quantity = ?, last_updated = GETDATE() WHERE product_id = ?";

    public Inventory getInventoryByProductId(int productId) {
        Inventory inventory = null;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_INVENTORY_BY_PRODUCT_ID)) {
            stmt.setInt(1, productId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    inventory = new Inventory();
                    inventory.setInventoryId(rs.getInt("inventory_id"));
                    inventory.setProductId(rs.getInt("product_id"));
                    inventory.setStockQuantity(rs.getInt("stock_quantity"));
                    inventory.setLastUpdated(rs.getTimestamp("last_updated"));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return inventory;
    }

    public boolean updateInventory(Inventory inventory) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_INVENTORY)) {
            stmt.setInt(1, inventory.getStockQuantity());
            stmt.setInt(2, inventory.getProductId());
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
