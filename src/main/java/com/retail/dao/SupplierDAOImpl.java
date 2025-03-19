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
public class SupplierDAOImpl implements SupplierDAO {

    private static final String ADD_SUPPLIER = "INSERT INTO Supplier (name, contact_name, phone, email, address) VALUES (?, ?, ?, ?, ?)";
    private static final String GET_ALL_SUPPLIERS = "SELECT * FROM Supplier";
    private static final String GET_SUPPLIER_BY_ID = "SELECT * FROM Supplier WHERE supplier_id = ?";
    private static final String DELETE_SUPPLIER = "DELETE FROM Supplier WHERE supplier_id = ?";
    private static final String UPDATE_SUPPLIER = "UPDATE Supplier SET name = ?, contact_name = ?, phone = ?, email = ?, address = ? WHERE supplier_id = ?";
    private static final String SEARCH_SUPPLIER_BY_NAME = "SELECT * FROM Supplier WHERE name LIKE ?";

    @Override
    public boolean addSupplier(Supplier supplier) {
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(ADD_SUPPLIER, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, supplier.getName());
            stmt.setString(2, supplier.getContactName());
            stmt.setString(3, supplier.getPhone());
            stmt.setString(4, supplier.getEmail());
            stmt.setString(5, supplier.getAddress());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        supplier.setSupplierId(rs.getInt(1)); // Lấy ID tự sinh
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi khi thêm supplier: " + e.getMessage());
        }
        return false;
    }

    @Override
    public List<Supplier> getAllSuppliers() {
        List<Supplier> suppliers = new ArrayList<>();
        try (Connection con = DatabaseConnection.getConnection(); Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(GET_ALL_SUPPLIERS)) {
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
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pstmt = con.prepareStatement(GET_SUPPLIER_BY_ID)) {
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
    public boolean deleteSupplier(int id) {
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pstmt = con.prepareStatement(DELETE_SUPPLIER)) {

            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();

            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("❌ Lỗi xóa supplier: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateSupplier(Supplier supplier) {
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pstmt = con.prepareStatement(UPDATE_SUPPLIER)) {

            pstmt.setString(1, supplier.getName());
            pstmt.setString(2, supplier.getContactName());
            pstmt.setString(3, supplier.getPhone());
            pstmt.setString(4, supplier.getEmail());
            pstmt.setString(5, supplier.getAddress());
            pstmt.setInt(6, supplier.getSupplierId());

            int affectedRows = pstmt.executeUpdate();

            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("❌ Lỗi cập nhật supplier: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Supplier> searchSuppliersByName(String keyword) {
        List<Supplier> suppliers = new ArrayList<>();
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pstmt = con.prepareStatement(SEARCH_SUPPLIER_BY_NAME)) {

            pstmt.setString(1, "%" + keyword + "%"); // Tìm kiếm với LIKE '%keyword%'
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Supplier supplier = new Supplier();
                supplier.setSupplierId(rs.getInt("supplier_id"));
                supplier.setName(rs.getString("name"));
                supplier.setContactName(rs.getString("contact_name"));
                supplier.setPhone(rs.getString("phone"));
                supplier.setEmail(rs.getString("email"));
                supplier.setAddress(rs.getString("address"));

                suppliers.add(supplier);
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi tìm kiếm supplier: " + e.getMessage());
        }
        return suppliers;
    }

    @Override
    public int getNextSupplierId() {
        String GET_NEXT_SUPPLIER_ID = "SELECT MAX(supplier_id) + 1 AS NextSupplierId FROM supplier";

        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pstmt = con.prepareStatement(GET_NEXT_SUPPLIER_ID); ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("NextSupplierId"); // Truy cập cột NextSupplierId
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi khi lấy ID tiếp theo của Supplier: " + e.getMessage());
        }
        return -1; // Trả về -1 nếu có lỗi
    }
}
