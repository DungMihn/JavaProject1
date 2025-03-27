package com.retail.dao;

import com.retail.model.Supplier;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierDAO {

    private static final String SELECT_SUPPLIER_BY_ID = "SELECT * FROM Supplier WHERE supplier_id = ?";
    private static final String SELECT_ALL_SUPPLIERS = "SELECT * FROM Supplier";

    // Lấy thông tin nhà cung cấp theo ID
    public Supplier getSupplierById(int supplierId) {
        Supplier supplier = null;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_SUPPLIER_BY_ID)) {
            pstmt.setInt(1, supplierId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                supplier = new Supplier(
                        rs.getInt("supplier_id"),
                        rs.getString("name"),
                        rs.getString("contact_name"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("address")
                );
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi lấy nhà cung cấp: " + e.getMessage());
        }
        return supplier;
    }
    
    // Lấy danh sách tất cả nhà cung cấp
    public List<Supplier> getAllSuppliers() {
        List<Supplier> suppliers = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_SUPPLIERS)) {
            while (rs.next()) {
                Supplier supplier = new Supplier(
                        rs.getInt("supplier_id"),
                        rs.getString("name"),
                        rs.getString("contact_name"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("address")
                );
                suppliers.add(supplier);
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi lấy danh sách nhà cung cấp: " + e.getMessage());
        }
        return suppliers;
    }
}
