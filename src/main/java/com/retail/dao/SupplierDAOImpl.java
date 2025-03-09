/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.dao;

import com.retail.model.Supplier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Admin
 */
public class SupplierDAOImpl implements SupplierDAO{
    private static final String ADD_SUPPLIER = "INSERT INTO Supplier (name, contact_name, phone, email, address) VALUES (?, ?, ?, ?, ?)";
    private static final String GET_ALL_SUPPLIERS = "SELECT * FROM Supplier";
    private static final String GET_SUPPLIER_BY_ID = "SELECT * FROM Supplier WHERE supplier_id = ?";
    private static final String DELETE_SUPPLIER = "DELETE FROM Supplier WHERE supplier_id = ?";
    private static final String UPDATE_SUPPLIER = "UPDATE Supplier SET name = ?, contact_name = ?, phone = ?, email = ?, address = ? WHERE supplier_id = ?";

    @Override
    public void addSupplier(Supplier supplier) {
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(ADD_SUPPLIER)) {
            pstmt.setString(1, supplier.getName());
            pstmt.setString(2, supplier.getContactName());
            pstmt.setString(3, supplier.getPhone());
            pstmt.setString(4, supplier.getEmail());
            pstmt.setString(5, supplier.getAddress());
            pstmt.executeUpdate();
        } catch (SQLException e) {
             System.out.println("❌ Lỗi thêm nhà cung cấp: " + e.getMessage());
        }
    }

    @Override
    public List<Supplier> getAllSuppliers() {
        List<Supplier> suppliers = new ArrayList<>();
        try (Connection con = DatabaseConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(GET_ALL_SUPPLIERS)) {
            while (rs.next()) {
                suppliers.add(new Supplier(
                    rs.getInt("supplier_id"),
                    rs.getString("name"),
                    rs.getString("contact_name"),
                    rs.getString("phone"),
                    rs.getString("email"),
                    rs.getString("address")
                ));
            }
        } catch (SQLException e) {
             System.out.println("❌ Lỗi khi lấy chi tiết supplier: " + e.getMessage());
        }
        return suppliers;
    }

    @Override
    public Supplier getSupplierById(int id) {
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(GET_SUPPLIER_BY_ID)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Supplier(
                    rs.getInt("supplier_id"),
                    rs.getString("name"),
                    rs.getString("contact_name"),
                    rs.getString("phone"),
                    rs.getString("email"),
                    rs.getString("address")
                );
            }
        } catch (SQLException e) {
             System.out.println("❌ Lỗi thêm chi tiết theo supplier id: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void deleteSupplier(int id) {
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(DELETE_SUPPLIER)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
             System.out.println("❌ Lỗi xóa supplier: " + e.getMessage());
        }
    }

    @Override
    public void updateSupplier(Supplier supplier) {
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(UPDATE_SUPPLIER)) {
            pstmt.setString(1, supplier.getName());
            pstmt.setString(2, supplier.getContactName());
            pstmt.setString(3, supplier.getPhone());
            pstmt.setString(4, supplier.getEmail());
            pstmt.setString(5, supplier.getAddress());
            pstmt.setInt(6, supplier.getSupplierId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
             System.out.println("❌ Lỗi cập nhập supplier: " + e.getMessage());
        }
    }
}
