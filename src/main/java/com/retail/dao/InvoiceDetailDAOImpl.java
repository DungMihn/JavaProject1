/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.dao;

import com.retail.model.InvoiceDetail;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class InvoiceDetailDAOImpl implements InvoiceDetailDAO{
    private static final String ADD_INVOICE_DETAIL = "INSERT INTO Invoice_Detail (invoice_id, product_id, quantity, price, subtotal) VALUES (?, ?, ?, ?, ?)";
    private static final String GET_ALL_INVOICE_DETAILS = "SELECT * FROM Invoice_Detail";
    private static final String GET_INVOICE_DETAIL_BY_ID = "SELECT * FROM Invoice_Detail WHERE invoice_detail_id = ?";
    private static final String DELETE_INVOICE_DETAIL = "DELETE FROM Invoice_Detail WHERE invoice_detail_id = ?";

    @Override
    public void addInvoiceDetail(InvoiceDetail invoiceDetail) {
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(ADD_INVOICE_DETAIL)) {
            pstmt.setInt(1, invoiceDetail.getInvoiceId());
            pstmt.setInt(2, invoiceDetail.getProductId());
            pstmt.setInt(3, invoiceDetail.getQuantity());
            pstmt.setDouble(4, invoiceDetail.getPrice());
            pstmt.setDouble(5, invoiceDetail.getSubtotal());
            pstmt.executeUpdate();
            System.out.println("✅ Thêm chi tiết hóa đơn thành công!");
        } catch (SQLException e) {
            System.out.println("❌ Lỗi thêm chi tiết hóa đơn: " + e.getMessage());
        }
    }

    @Override
    public List<InvoiceDetail> getAllInvoiceDetails() {
        List<InvoiceDetail> details = new ArrayList<>();
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(GET_ALL_INVOICE_DETAILS);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                details.add(new InvoiceDetail(
                    rs.getInt("invoice_detail_id"),
                    rs.getInt("invoice_id"),
                    rs.getInt("product_id"),
                    rs.getInt("quantity"),
                    rs.getDouble("price"),
                    rs.getDouble("subtotal")
                ));
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi truy vấn chi tiết hóa đơn: " + e.getMessage());
        }
        return details;
    }

    @Override
    public InvoiceDetail getInvoiceDetailById(int id) {
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(GET_INVOICE_DETAIL_BY_ID)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new InvoiceDetail(
                    rs.getInt("invoice_detail_id"),
                    rs.getInt("invoice_id"),
                    rs.getInt("product_id"),
                    rs.getInt("quantity"),
                    rs.getDouble("price"),
                    rs.getDouble("subtotal")
                );
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi lấy chi tiết hóa đơn: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void deleteInvoiceDetail(int id) {
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(DELETE_INVOICE_DETAIL)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("✅ Xóa chi tiết hóa đơn thành công!");
        } catch (SQLException e) {
            System.out.println("❌ Lỗi xóa chi tiết hóa đơn: " + e.getMessage());
        }
    }

    @Override
    public void updateInvoiceDetail(InvoiceDetail invoiceDetail) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
