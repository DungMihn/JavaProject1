/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.dao;

/**
 *
 * @author ADMIN
 */




import com.retail.ConnectDB;
import com.retail.model.Customer;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String query = "SELECT * FROM Customer";

        try (Connection conn = ConnectDB.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                customers.add(new Customer(
                        rs.getString("Customer_ID"),
                        rs.getString("Name"),
                        rs.getString("Address"),
                        rs.getString("Phone"),
                        rs.getString("Email"),
                        rs.getObject("created_at", LocalDateTime.class)
                ));
            }
        } catch (SQLException e) {
            System.out.println("Loi: " + e.getMessage());
        }
        return customers;
    }

    public boolean insertCustomer(Customer customer) {
    String query = "INSERT INTO Customer (Name, Address, Phone, Email) VALUES (?, ?, ?, ?)";
    try (Connection conn = ConnectDB.connect();
         PreparedStatement pstmt = conn.prepareStatement(query)) {
        pstmt.setString(1, customer.getName());
        pstmt.setString(2, customer.getAddress());
        pstmt.setString(3, customer.getPhone());
        pstmt.setString(4, customer.getEmail());       
        return pstmt.executeUpdate() > 0;
    } catch (SQLException e) {
        System.out.println("Loi: " + e.getMessage());
        return false;
    }
}

    public boolean updateCustomer(Customer customer) {
        String query = "UPDATE Customer SET Name = ?, Address = ?, Phone = ?, Email = ? WHERE CustomerID = ?";
        try (Connection conn = ConnectDB.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, customer.getName());
            pstmt.setString(2, customer.getAddress());
            pstmt.setString(3, customer.getPhone());
            pstmt.setString(4, customer.getEmail());
            pstmt.setString(5, customer.getCustomerID());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Loi: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteCustomer(String customerID) {
        String query = "DELETE FROM Customer WHERE Customer_ID = ?";
        try (Connection conn = ConnectDB.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, customerID);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Loi: " + e.getMessage());
            return false;
        }
    }
}







