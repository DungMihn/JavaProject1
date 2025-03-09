/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.dao;

import com.retail.model.Invoice;
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
public class InvoiceDAOImpl implements InvoiceDAO{
    private static final String SELECT_ALL_INVOICES = "SELECT * FROM Invoice";
    private static final String INSERT_INVOICE = "INSERT INTO Invoice (customer_id, employee_id, total_amount, discount, final_amount, payment_method) " +
                       "VALUES (?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_INVOICE = "UPDATE Invoice SET customer_id=?, employee_id=?, total_amount=?, payment_method=? WHERE invoice_id=?";
    private static final String DELETE_INVOICE = "DELETE FROM Invoice WHERE invoice_id = ?";
    private static final String SEARCH_INVOICE = "SELECT * FROM Invoice WHERE customer_id LIKE ? OR employee_id LIKE ?";
    private static final String GET_INVOICE_BY_ID = "SELECT * FROM Invoice WHERE invoice_id = ?";

    @Override
    public void addInvoice(Invoice invoice) {
         try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(INSERT_INVOICE)) {
            pstmt.setInt(1, invoice.getCustomerId());
            pstmt.setInt(2, invoice.getEmployeeId());
            pstmt.setDouble(3, invoice.getTotalAmount());
            pstmt.setDouble(4, invoice.getDiscount());
            pstmt.setDouble(5, invoice.getFinalAmount());
            pstmt.setString(6, invoice.getPaymentMethod());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("❌ Lỗi thêm hóa đơn: " + e.getMessage());
        }
    }

    @Override
    public void updateInvoice(Invoice invoice) {
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(UPDATE_INVOICE)) {
            pstmt.setInt(1, invoice.getCustomerId());
            pstmt.setInt(2, invoice.getEmployeeId());
            pstmt.setDouble(3, invoice.getTotalAmount());
            pstmt.setDouble(4, invoice.getDiscount());
            pstmt.setDouble(5, invoice.getFinalAmount());
            pstmt.setString(6, invoice.getPaymentMethod());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("❌ Lỗi cập nhật hóa đơn: " + e.getMessage());
        }
    }

    @Override
    public void deleteInvoice(int invoiceId) {
       try (Connection con = DatabaseConnection.getConnection();
            PreparedStatement pstmt = con.prepareStatement(DELETE_INVOICE)) {
            pstmt.setInt(1, invoiceId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("❌ Lỗi xóa hóa đơn: " + e.getMessage());
        }
    }

    @Override
    public Invoice getInvoiceById(int invoiceId) {
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(GET_INVOICE_BY_ID)) {
            pstmt.setInt(1, invoiceId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Invoice(
                        rs.getInt("invoice_id"),
                        rs.getInt("customer_id"),
                        rs.getInt("employee_id"),
                        rs.getDouble("total_amount"),
                        rs.getDouble("discount"),
                        rs.getDouble("final_amount"),
                        rs.getString("payment_method"),
                        rs.getObject("created_at", LocalDateTime.class)
                );
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi khi lấy thông tin hóa đơn: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Invoice> getAllInvoices() {
        List<Invoice> invoices = new ArrayList<>();
        try (Connection con = DatabaseConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_INVOICES)) {

            while (rs.next()) {
                invoices.add(new Invoice(
                    rs.getInt("invoice_id"),
                        rs.getInt("customer_id"),
                        rs.getInt("employee_id"),
                        rs.getDouble("total_amount"),
                        rs.getDouble("discount"),
                        rs.getDouble("final_amount"),
                        rs.getString("payment_method"),
                        rs.getObject("created_at", LocalDateTime.class)
                ));
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi truy vấn hóa đơn: " + e.getMessage());
        }
        return invoices;
    }
    
}
