/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.dao;

import com.retail.model.InvoiceDetail;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDetailDAO {
    private static final String INSERT_INVOICE_DETAIL = "INSERT INTO InvoiceDetail (invoice_id, product_id, quantity, price, subtotal) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_DETAILS_BY_INVOICE = "SELECT * FROM InvoiceDetail WHERE invoice_id = ?";

    public boolean addInvoiceDetail(InvoiceDetail detail) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_INVOICE_DETAIL, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, detail.getInvoiceId());
            stmt.setInt(2, detail.getProductId());
            stmt.setInt(3, detail.getQuantity());
            stmt.setBigDecimal(4, detail.getPrice());
            stmt.setBigDecimal(5, detail.getSubtotal());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Tạo chi tiết hóa đơn thất bại, không có dòng nào được thêm.");
            }
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    detail.setInvoiceDetailId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Tạo chi tiết hóa đơn thất bại, không lấy được ID.");
                }
            }
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public List<InvoiceDetail> getInvoiceDetailsByInvoiceId(int invoiceId) {
        List<InvoiceDetail> details = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_DETAILS_BY_INVOICE)) {
            stmt.setInt(1, invoiceId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    InvoiceDetail detail = new InvoiceDetail();
                    detail.setInvoiceDetailId(rs.getInt("invoice_detail_id"));
                    detail.setInvoiceId(rs.getInt("invoice_id"));
                    detail.setProductId(rs.getInt("product_id"));
                    detail.setQuantity(rs.getInt("quantity"));
                    detail.setPrice(rs.getBigDecimal("price"));
                    detail.setSubtotal(rs.getBigDecimal("subtotal"));
                    details.add(detail);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return details;
    }
    public boolean deleteInvoiceDetailsByInvoiceId(int invoiceId) {
    String sql = "DELETE FROM InvoiceDetail WHERE invoice_id = ?";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, invoiceId);
        int affectedRows = stmt.executeUpdate();
        return affectedRows > 0;
    } catch (SQLException ex) {
        ex.printStackTrace();
        return false;
    }
}
    
    // Lấy top sản phẩm bán chạy trong ngày (theo ngày hiện tại)
public List<InvoiceDetail> getBestSellingProductsByDate() {
    List<InvoiceDetail> invoiceDetails = new ArrayList<>();
    String query = "SELECT TOP 10 d.product_id, SUM(d.quantity) AS total_quantity, SUM(d.price * d.quantity) AS total_revenue " +
                   "FROM InvoiceDetail d " +
                   "JOIN Invoice i ON d.invoice_id = i.invoice_id " +
                   "WHERE CAST(i.created_at AS DATE) = CAST(GETDATE() AS DATE) " +
                   "GROUP BY d.product_id " +
                   "ORDER BY total_quantity DESC";
    
    try (Connection conn = DatabaseConnection.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {
        while (rs.next()) {
            InvoiceDetail detail = new InvoiceDetail();
            detail.setProductId(rs.getInt("product_id"));
            detail.setQuantity(rs.getInt("total_quantity"));
            // Ở đây, bạn có thể lưu doanh thu vào trường price (hoặc tạo một thuộc tính mới)
            detail.setPrice(rs.getBigDecimal("total_revenue"));
            invoiceDetails.add(detail);
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
    return invoiceDetails;
}

// Lấy top sản phẩm bán chạy trong tháng (theo tháng hiện tại)
public List<InvoiceDetail> getBestSellingProductsByMonth() {
    List<InvoiceDetail> invoiceDetails = new ArrayList<>();
    String query = "SELECT TOP 10 d.product_id, SUM(d.quantity) AS total_quantity, SUM(d.price * d.quantity) AS total_revenue " +
                   "FROM InvoiceDetail d " +
                   "JOIN Invoice i ON d.invoice_id = i.invoice_id " +
                   "WHERE YEAR(i.created_at) = YEAR(GETDATE()) AND MONTH(i.created_at) = MONTH(GETDATE()) " +
                   "GROUP BY d.product_id " +
                   "ORDER BY total_quantity DESC";
    try (Connection conn = DatabaseConnection.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {
        while (rs.next()) {
            InvoiceDetail detail = new InvoiceDetail();
            detail.setProductId(rs.getInt("product_id"));
            detail.setQuantity(rs.getInt("total_quantity"));
            detail.setPrice(rs.getBigDecimal("total_revenue"));
            invoiceDetails.add(detail);
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
    return invoiceDetails;
}

}