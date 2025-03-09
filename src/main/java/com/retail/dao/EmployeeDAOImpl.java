/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.dao;

import com.retail.model.Employee;
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
public class EmployeeDAOImpl implements EmployeeDAO{
    private static final String ADD_EMPLOYEE = "INSERT INTO Employee (name, phone, role) VALUES (?, ?, ?)";
    private static final String GET_ALL_EMPLOYEES = "SELECT * FROM Employee";
    private static final String DELETE_EMPLOYEE = "DELETE FROM Employee WHERE employee_id = ?";
    private static final String GET_EMPLOYEE_BY_ID = "SELECT * FROM Employee WHERE employee_id = ?";

    @Override
    public void addEmployee(Employee employee) {
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(ADD_EMPLOYEE)) {
            pstmt.setString(1, employee.getName());
            pstmt.setString(2, employee.getPhone());
            pstmt.setString(3, employee.getRole());
            pstmt.execute();
            System.out.println("✅ Thêm nhân viên thành công!");
        } catch (SQLException e) {
            System.out.println("❌ Lỗi thêm nhân viên: " + e.getMessage());
        }
    }

    @Override
    public void updateEmployee(Employee employee) {
        // Cập nhật nhân viên nếu cần
    }

    @Override
    public void deleteEmployee(int employeeId) {
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(DELETE_EMPLOYEE)) {
            pstmt.setInt(1, employeeId);
            pstmt.executeUpdate();
            System.out.println("✅ Xóa nhân viên thành công!");
        } catch (SQLException e) {
            System.out.println("❌ Lỗi xóa nhân viên: " + e.getMessage());
        }
    }

    @Override
    public Employee getEmployeeById(int employeeId) {
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(GET_EMPLOYEE_BY_ID)) {
            pstmt.setInt(1, employeeId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Employee(
                        rs.getInt("employee_id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("role"),
                        rs.getObject("created_at", LocalDateTime.class)
                );
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi khi lấy thông tin nhân viên: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        try (Connection con = DatabaseConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(GET_ALL_EMPLOYEES)) {
            while (rs.next()) {
                employees.add(new Employee(
                        rs.getInt("employee_id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("role"),
                        rs.getObject("created_at", LocalDateTime.class)
                ));
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi truy vấn nhân viên: " + e.getMessage());
        }
        return employees;
    }
}
