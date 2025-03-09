/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.dao;

import com.retail.model.Customer;
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
public class CustomerDAOImpl implements CustomerDAO{
    private static final String ADD_CUSTOMER = "INSERT INTO Customer (name, phone, email, address) VALUES (?, ?, ?, ?)";
    private static final String GET_ALL_CUSTOMERS = "SELECT * FROM Customer";
    private static final String DELETE_CUSTOMER = "DELETE FROM Customer WHERE customer_id = ?";
    private static final String GET_CUSTOMER_BY_ID = "SELECT * FROM Customer WHERE customer_id = ?";
    
    @Override
    public void addCustomer(Customer customer) {
        try (Connection con = DatabaseConnection.getConnection();
            PreparedStatement pstmt = con.prepareStatement(ADD_CUSTOMER)) {
            pstmt.setString(1, customer.getName());
            pstmt.setString(2, customer.getPhone());
            pstmt.setString(3, customer.getEmail());
            pstmt.setString(4, customer.getAddress());
            pstmt.execute();
            System.out.println("✅ Thêm khách hàng thành công!");
        } catch (SQLException e) {
            System.out.println("❌ Lỗi thêm khách hàng: " + e.getMessage());
        }
    }

    @Override
    public void updateCustomer(Customer customer) {
        
    }

    @Override
    public void deleteCustomer(int customerId) {
        try (Connection con = DatabaseConnection.getConnection();
            PreparedStatement pstmt = con.prepareStatement(DELETE_CUSTOMER)) {
            pstmt.setInt(1, customerId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("❌ Lỗi xóa khách hàng: " + e.getMessage());
        }
    }

    @Override
    public Customer getCustomerById(int customerId) {
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(GET_CUSTOMER_BY_ID)) {
            pstmt.setInt(1, customerId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Customer(
                        rs.getInt("customer_id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("address"),
                        rs.getObject("created_at", LocalDateTime.class)
                );
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi khi lấy thông tin hóa đơn: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Customer> getAllCustomers() {
     List<Customer> customers = new ArrayList<>();
        try (Connection con = DatabaseConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(GET_ALL_CUSTOMERS)) {
            while (rs.next()) {
                customers.add(new Customer(
                    rs.getInt("customer_id"),
                    rs.getString("name"),
                    rs.getString("phone"),
                    rs.getString("email"),
                    rs.getString("address"),
                    rs.getObject("created_at", LocalDateTime.class)
                ));
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi truy vấn khách hàng: " + e.getMessage());
        }
        return customers;
    }
    
}
