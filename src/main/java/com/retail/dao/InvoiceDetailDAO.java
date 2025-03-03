/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.dao;

/**
 *
 * @author macbookprom1
 */
import com.retail.model.InvoiceDetail;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDetailDAO {
    private static final String SELECT_INVOICE_DETAILS = "SELECT * FROM Invoice_Detail WHERE invoice_id = ?";

    public List<InvoiceDetail> getInvoiceDetails(int invoiceId) {
        List<InvoiceDetail> details = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_INVOICE_DETAILS)) {
            pstmt.setInt(1, invoiceId);
            ResultSet rs = pstmt.executeQuery();

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
}




