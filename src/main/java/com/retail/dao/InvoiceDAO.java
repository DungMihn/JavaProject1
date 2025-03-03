/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.dao;

/**
 *
 * @author macbookprom1
 */
import com.retail.model.Invoice;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDAO {
    private static final String SELECT_ALL_INVOICES = "SELECT * FROM Invoice";
    private static final String INSERT_INVOICE = "INSERT INTO Invoice (customer_id, employee_id, total_amount, payment_method) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_INVOICE = "UPDATE Invoice SET customer_id=?, employee_id=?, total_amount=?, payment_method=? WHERE invoice_id=?";
    private static final String DELETE_INVOICE = "DELETE FROM Invoice WHERE invoice_id = ?";
    private static final String SEARCH_INVOICE = "SELECT * FROM Invoice WHERE customer_id LIKE ? OR employee_id LIKE ?";

    public List<Invoice> getAllInvoices() {
        List<Invoice> invoices = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_INVOICES)) {

            while (rs.next()) {
                invoices.add(new Invoice(
                    rs.getInt("invoice_id"),
                    rs.getInt("customer_id"),
                    rs.getInt("employee_id"),
                    rs.getDouble("total_amount"),
                    rs.getString("payment_method"),
                    rs.getTimestamp("created_at")
                ));
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi truy vấn hóa đơn: " + e.getMessage());
        }
        return invoices;
    }

    public void addInvoice(Invoice invoice) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_INVOICE)) {
            pstmt.setInt(1, invoice.getCustomerId());
            pstmt.setInt(2, invoice.getEmployeeId());
            pstmt.setDouble(3, invoice.getTotalAmount());
            pstmt.setString(4, invoice.getPaymentMethod());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("❌ Lỗi thêm hóa đơn: " + e.getMessage());
        }
    }

    public void updateInvoice(Invoice invoice) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_INVOICE)) {
            pstmt.setInt(1, invoice.getCustomerId());
            pstmt.setInt(2, invoice.getEmployeeId());
            pstmt.setDouble(3, invoice.getTotalAmount());
            pstmt.setString(4, invoice.getPaymentMethod());
            pstmt.setInt(5, invoice.getInvoiceId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("❌ Lỗi cập nhật hóa đơn: " + e.getMessage());
        }
    }

    public void deleteInvoice(int invoiceId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELETE_INVOICE)) {
            pstmt.setInt(1, invoiceId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("❌ Lỗi xóa hóa đơn: " + e.getMessage());
        }
    }
}





