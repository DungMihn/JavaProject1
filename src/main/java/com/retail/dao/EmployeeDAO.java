/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.dao;

import com.retail.model.Employee;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {
    private static final String SELECT_EMPLOYEE_BY_ID = "SELECT * FROM Employee WHERE employee_id = ?";
    private static final String SELECT_ALL_EMPLOYEES = "SELECT * FROM Employee";

    public Employee getEmployeeById(int employeeId) {
        Employee employee = null;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_EMPLOYEE_BY_ID)) {
            stmt.setInt(1, employeeId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    employee = new Employee();
                    employee.setEmployeeId(rs.getInt("employee_id"));
                    employee.setName(rs.getString("name"));
                    employee.setPhone(rs.getString("phone"));
                    employee.setRole(rs.getString("role"));
                    employee.setCreatedAt(rs.getTimestamp("created_at"));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return employee;
    }
    
    // Phương thức lấy toàn bộ nhân viên để sử dụng trong combo box
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_EMPLOYEES)) {
            while (rs.next()) {
                Employee employee = new Employee();
                employee.setEmployeeId(rs.getInt("employee_id"));
                employee.setName(rs.getString("name"));
                employee.setPhone(rs.getString("phone"));
                employee.setRole(rs.getString("role"));
                employee.setCreatedAt(rs.getTimestamp("created_at"));
                employees.add(employee);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return employees;
    }
}