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
public class InvoiceDAOImpl implements InvoiceDAO {

    private static final String INSERT_INVOICE = "INSERT INTO Invoice (customer_id, employee_id, total_amount, discount, final_amount, payment_method) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SELECT_INVOICE_BY_ID = "SELECT * FROM Invoice WHERE invoice_id = ?";
    private static final String SELECT_ALL_INVOICES = "SELECT * FROM Invoice";

    // Tạo hóa đơn mới
    @Override
    public boolean createInvoice(Invoice invoice) {
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(INSERT_INVOICE, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, invoice.getCustomerId());
            stmt.setInt(2, invoice.getEmployeeId());
            stmt.setBigDecimal(3, invoice.getTotalAmount());
            stmt.setBigDecimal(4, invoice.getDiscount());
            stmt.setBigDecimal(5, invoice.getFinalAmount());
            stmt.setString(6, invoice.getPaymentMethod());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Tạo hóa đơn thất bại, không có dòng nào được thêm.");
            }
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    invoice.setInvoiceId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Tạo hóa đơn thất bại, không lấy được ID.");
                }
            }
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // Lấy hóa đơn theo invoice_id
    @Override
    public Invoice getInvoiceById(int invoiceId) {
        Invoice invoice = null;
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(SELECT_INVOICE_BY_ID)) {
            stmt.setInt(1, invoiceId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    invoice = new Invoice();
                    invoice.setInvoiceId(rs.getInt("invoice_id"));
                    invoice.setCustomerId(rs.getInt("customer_id"));
                    invoice.setEmployeeId(rs.getInt("employee_id"));
                    invoice.setTotalAmount(rs.getBigDecimal("total_amount"));
                    invoice.setDiscount(rs.getBigDecimal("discount"));
                    invoice.setFinalAmount(rs.getBigDecimal("final_amount"));
                    invoice.setPaymentMethod(rs.getString("payment_method"));
                    invoice.setCreatedAt(rs.getTimestamp("created_at"));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return invoice;
    }

    // Lấy tất cả hóa đơn
    @Override
    public List<Invoice> getAllInvoices() {
        List<Invoice> invoices = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(SELECT_ALL_INVOICES)) {
            while (rs.next()) {
                Invoice invoice = new Invoice();
                invoice.setInvoiceId(rs.getInt("invoice_id"));
                invoice.setCustomerId(rs.getInt("customer_id"));
                invoice.setEmployeeId(rs.getInt("employee_id"));
                invoice.setTotalAmount(rs.getBigDecimal("total_amount"));
                invoice.setDiscount(rs.getBigDecimal("discount"));
                invoice.setFinalAmount(rs.getBigDecimal("final_amount"));
                invoice.setPaymentMethod(rs.getString("payment_method"));
                invoice.setCreatedAt(rs.getTimestamp("created_at"));
                invoices.add(invoice);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return invoices;
    }

    // Cập nhật hóa đơn dựa trên invoice_id
    @Override
    public boolean updateInvoice(Invoice invoice) {
        String sql = "UPDATE Invoice SET customer_id = ?, employee_id = ?, total_amount = ?, discount = ?, final_amount = ?, payment_method = ? WHERE invoice_id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, invoice.getCustomerId());
            pstmt.setInt(2, invoice.getEmployeeId());
            pstmt.setBigDecimal(3, invoice.getTotalAmount());
            pstmt.setBigDecimal(4, invoice.getDiscount());
            pstmt.setBigDecimal(5, invoice.getFinalAmount());
            pstmt.setString(6, invoice.getPaymentMethod());
            pstmt.setInt(7, invoice.getInvoiceId());
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // Lấy hóa đơn theo customer_id (hỗ trợ tìm hóa đơn theo SĐT)
    @Override
    public List<Invoice> getInvoicesByCustomerId(int customerId) {
        List<Invoice> invoices = new ArrayList<>();
        String sql = "SELECT * FROM Invoice WHERE customer_id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, customerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Invoice invoice = new Invoice();
                    invoice.setInvoiceId(rs.getInt("invoice_id"));
                    invoice.setCustomerId(rs.getInt("customer_id"));
                    invoice.setEmployeeId(rs.getInt("employee_id"));
                    invoice.setTotalAmount(rs.getBigDecimal("total_amount"));
                    invoice.setDiscount(rs.getBigDecimal("discount"));
                    invoice.setFinalAmount(rs.getBigDecimal("final_amount"));
                    invoice.setPaymentMethod(rs.getString("payment_method"));
                    invoice.setCreatedAt(rs.getTimestamp("created_at"));
                    invoices.add(invoice);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return invoices;
    }

    // Lấy hóa đơn theo ngày hiện tại (trả về danh sách hóa đơn)
    @Override
    public List<Invoice> getInvoicesByDate() {
        List<Invoice> invoices = new ArrayList<>();
        String query = "SELECT * FROM Invoice WHERE CAST(created_at AS DATE) = CAST(GETDATE() AS DATE)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Invoice invoice = new Invoice();
                    invoice.setInvoiceId(rs.getInt("invoice_id"));
                    invoice.setCustomerId(rs.getInt("customer_id"));
                    invoice.setEmployeeId(rs.getInt("employee_id"));
                    invoice.setTotalAmount(rs.getBigDecimal("total_amount"));
                    invoice.setDiscount(rs.getBigDecimal("discount"));
                    invoice.setFinalAmount(rs.getBigDecimal("final_amount"));
                    invoice.setPaymentMethod(rs.getString("payment_method"));
                    invoice.setCreatedAt(rs.getTimestamp("created_at"));
                    invoices.add(invoice);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return invoices;
    }

    // Lấy hóa đơn theo tháng hiện tại (trả về danh sách hóa đơn)
    @Override
    public List<Invoice> getInvoicesByMonth() {
        List<Invoice> invoices = new ArrayList<>();
        String query = "SELECT * FROM Invoice WHERE YEAR(created_at) = YEAR(GETDATE()) AND MONTH(created_at) = MONTH(GETDATE())";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Invoice invoice = new Invoice();
                    invoice.setInvoiceId(rs.getInt("invoice_id"));
                    invoice.setCustomerId(rs.getInt("customer_id"));
                    invoice.setEmployeeId(rs.getInt("employee_id"));
                    invoice.setTotalAmount(rs.getBigDecimal("total_amount"));
                    invoice.setDiscount(rs.getBigDecimal("discount"));
                    invoice.setFinalAmount(rs.getBigDecimal("final_amount"));
                    invoice.setPaymentMethod(rs.getString("payment_method"));
                    invoice.setCreatedAt(rs.getTimestamp("created_at"));
                    invoices.add(invoice);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return invoices;
    }

}
