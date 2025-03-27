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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class CustomerDAOImpl implements CustomerDAO {

    private static final String INSERT_CUSTOMER = "INSERT INTO Customer (name, phone, email, address) VALUES (?, ?, ?, ?)";
    private static final String SELECT_CUSTOMER_BY_PHONE = "SELECT * FROM Customer WHERE phone = ?";
    private static final String SELECT_ALL_CUSTOMERS = "SELECT * FROM Customer";
    private static final String UPDATE_CUSTOMER = "UPDATE Customer SET name = ?, phone = ?, email = ?, address = ? WHERE customer_id = ?";
    private static final String DELETE_CUSTOMER = "DELETE FROM Customer WHERE customer_id = ?";

    @Override
    public boolean addCustomer(Customer customer) {
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(INSERT_CUSTOMER, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getPhone());
            stmt.setString(3, customer.getEmail());
            stmt.setString(4, customer.getAddress());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Tạo khách hàng thất bại, không có dòng nào được thêm.");
            }
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    customer.setCustomerId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Tạo khách hàng thất bại, không lấy được ID.");
                }
            }
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public Customer getCustomerByPhone(String phone) {
        Customer customer = null;
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(SELECT_CUSTOMER_BY_PHONE)) {
            stmt.setString(1, phone);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    customer = new Customer();
                    customer.setCustomerId(rs.getInt("customer_id"));
                    customer.setName(rs.getString("name"));
                    customer.setPhone(rs.getString("phone"));
                    customer.setEmail(rs.getString("email"));
                    customer.setAddress(rs.getString("address"));
                    customer.setCreatedAt(rs.getTimestamp("created_at"));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return customer;
    }

    @Override
    public Customer getCustomerByEmail(String email) {
        Customer customer = null;
        String sql = "SELECT * FROM Customer WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    customer = new Customer();
                    customer.setCustomerId(rs.getInt("customer_id"));
                    customer.setName(rs.getString("name"));
                    customer.setPhone(rs.getString("phone"));
                    customer.setEmail(rs.getString("email"));
                    customer.setAddress(rs.getString("address"));
                    customer.setCreatedAt(rs.getTimestamp("created_at"));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return customer;
    }

    @Override
    public Customer getCustomerById(int customerId) {
        Customer customer = null;
        String sql = "SELECT * FROM Customer WHERE customer_id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, customerId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                customer = new Customer();
                customer.setCustomerId(rs.getInt("customer_id"));
                customer.setName(rs.getString("name"));
                customer.setPhone(rs.getString("phone"));
                customer.setEmail(rs.getString("email"));
                customer.setAddress(rs.getString("address"));
                customer.setCreatedAt(rs.getTimestamp("created_at"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return customer;
    }

    @Override
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(SELECT_ALL_CUSTOMERS)) {
            while (rs.next()) {
                Customer customer = new Customer();
                customer.setCustomerId(rs.getInt("customer_id"));
                customer.setName(rs.getString("name"));
                customer.setPhone(rs.getString("phone"));
                customer.setEmail(rs.getString("email"));
                customer.setAddress(rs.getString("address"));
                customer.setCreatedAt(rs.getTimestamp("created_at"));
                customers.add(customer);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return customers;
    }

    @Override
    public boolean updateCustomer(Customer customer) {
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(UPDATE_CUSTOMER)) {
            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getPhone());
            stmt.setString(3, customer.getEmail());
            stmt.setString(4, customer.getAddress());
            stmt.setInt(5, customer.getCustomerId());
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteCustomer(int customerId) {
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(DELETE_CUSTOMER)) {
            stmt.setInt(1, customerId);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Customer> searchCustomers(String keyword) {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM Customer WHERE name LIKE ? OR phone LIKE ? OR email LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            String pattern = "%" + keyword + "%";
            stmt.setString(1, pattern);
            stmt.setString(2, pattern);
            stmt.setString(3, pattern);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Customer customer = new Customer();
                    customer.setCustomerId(rs.getInt("customer_id"));
                    customer.setName(rs.getString("name"));
                    customer.setPhone(rs.getString("phone"));
                    customer.setEmail(rs.getString("email"));
                    customer.setAddress(rs.getString("address"));
                    customer.setCreatedAt(rs.getTimestamp("created_at"));
                    customers.add(customer);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return customers;
    }

    @Override
    public List<String> getAllCustomerPhones() {
        List<String> phones = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(SELECT_ALL_CUSTOMERS)) {
            while (rs.next()) {
                String phone = rs.getString("phone");
                if (phone != null && !phone.isEmpty()) {
                    phones.add(phone);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return phones;
    }

}
