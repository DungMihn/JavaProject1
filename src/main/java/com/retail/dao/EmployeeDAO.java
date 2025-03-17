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
                        rs.getString("Employee_ID"),
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
            pstmt.setString(4, employee.getEmployeeID());
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
 
}
