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
import com.retail.model.Employee;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String query = "SELECT * FROM Employee";

        try (Connection conn = ConnectDB.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                employees.add(new Employee(
                        rs.getInt("Employee_ID"),
                        rs.getString("Name"),
                        rs.getString("Phone"),
                        rs.getString("Role"),
                        rs.getObject("created_at", LocalDateTime.class)
                ));
            }
        } catch (SQLException e) {
            System.out.println("Loi: " + e.getMessage());
        }
        return employees;
    }

    public boolean insertEmployee(Employee employee) {
    String query = "INSERT INTO Employee (Name, Phone, Role) VALUES (?, ?, ?)";
    try (Connection conn = ConnectDB.connect();
         PreparedStatement pstmt = conn.prepareStatement(query)) {
        pstmt.setString(1, employee.getName());
        pstmt.setString(2, employee.getPhone());
        pstmt.setString(3, employee.getRole());       
        return pstmt.executeUpdate() > 0;
    } catch (SQLException e) {
        System.out.println("Loi: " + e.getMessage());
        return false;
    }
}

    public boolean updateEmployee(Employee employee) {
        String query = "UPDATE Employee SET Name = ?, Phone = ?, Role = ? WHERE Employee_ID = ?";
        try (Connection conn = ConnectDB.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, employee.getName());
            pstmt.setString(2, employee.getPhone());
            pstmt.setString(3, employee.getRole());
            pstmt.setInt(4, employee.getEmployeeID());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Loi: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteEmployee(String employeeID) {
    if (employeeID == null || employeeID.isEmpty()) {
        System.out.println("Loi: employeeID khong duoc de trong!");
        return false;
    }

    String query = "DELETE FROM Employee WHERE Employee_ID = ?";
    try (Connection conn = ConnectDB.connect();
         PreparedStatement pstmt = conn.prepareStatement(query)) {

        pstmt.setString(1, employeeID);
        return pstmt.executeUpdate() > 0;
    } catch (SQLException e) {
        System.out.println("Loi khi xoa nhan vien: " + e.getMessage());
        return false;
    }
}
 public int getNextSupplierId() {
        String GET_NEXT_SUPPLIER_ID = "SELECT MAX(Employee_id) + 1 AS NextEmployeeId FROM Employee";

        try (Connection con = ConnectDB.connect(); PreparedStatement pstmt = con.prepareStatement(GET_NEXT_SUPPLIER_ID); ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("NextEmployeeId"); // Truy cập cột NextSupplierId
            }
        } catch (SQLException e) {
            System.out.println("❌ Loi khi lay ID tiep theo cua Employee: " + e.getMessage());
        }
        return -1; // Trả về -1 nếu có lỗi
    }
 public List<Employee> searchEmployeeByName(String keyword) {
        List<Employee> employees = new ArrayList<>();
        try (Connection con = ConnectDB.connect(); PreparedStatement pstmt = con.prepareStatement(SEARCH_EMPLOYEE_BY_NAME)) {

            pstmt.setString(1, "%" + keyword + "%"); // Tìm kiếm với LIKE '%keyword%'
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Employee employee = new Employee();
                employee.setEmployeeID(rs.getInt("employee_id"));
                employee.setName(rs.getString("name"));
                employee.setPhone(rs.getString("phone"));
                employee.setRole(rs.getString("role"));

                employees.add(employee);
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi tìm kiếm nhân viên: " + e.getMessage());
        }
        return employees;
    }
private static final String SEARCH_EMPLOYEE_BY_NAME = "SELECT * FROM Employee WHERE name LIKE ?";
}
