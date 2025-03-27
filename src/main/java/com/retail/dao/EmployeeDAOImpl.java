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
public class EmployeeDAOImpl implements EmployeeDAO {
     private static final String SEARCH_EMPLOYEE_BY_NAME = "SELECT * FROM Employee WHERE name LIKE ?";
     private static final String SELECT_EMPLOYEE_BY_ID = "SELECT * FROM Employee WHERE employee_id = ?";

    @Override
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String query = "SELECT * FROM Employee";

        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                employees.add(new Employee(
                        rs.getInt("employee_id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("role"),
                        rs.getString("userName"),
                        rs.getString("password"),
                        rs.getObject("created_at", LocalDateTime.class)
                      
                ));
            }
        } catch (SQLException e) {
            System.out.println("Loi: " + e.getMessage());
        }
        return employees;
    }

    @Override
    public boolean insertEmployee(Employee employee) {
        String query = "INSERT INTO Employee (Name, Phone, Role, UserName, Password) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, employee.getName());
            pstmt.setString(2, employee.getPhone());
            pstmt.setString(3, employee.getRole());
            pstmt.setString(4, employee.getUserName());
            pstmt.setString(5, employee.getPassword());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Loi: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateEmployee(Employee employee) {
        String query = "UPDATE Employee SET name = ?, phone = ?, role = ?, username = ?, password = ? WHERE employee_id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, employee.getName());
            pstmt.setString(2, employee.getPhone());
            pstmt.setString(3, employee.getRole());
            pstmt.setString(4, employee.getUserName());
            pstmt.setString(5, employee.getPassword());
            pstmt.setInt(6, employee.getEmployeeId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Loi: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteEmployee(String employeeId) {
        if (employeeId == null || employeeId.isEmpty()) {
            System.out.println("Loi: employeeID khong duoc de trong!");
            return false;
        }

        String query = "DELETE FROM Employee WHERE Employee_ID = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, employeeId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Loi khi xoa nhan vien: " + e.getMessage());
            return false;
        }
    }
   
    @Override
    public int getNextSupplierId() {
        String GET_NEXT_SUPPLIER_ID = "SELECT MAX(Employee_id) + 1 AS NextEmployeeId FROM Employee";

        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pstmt = con.prepareStatement(GET_NEXT_SUPPLIER_ID); ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("NextEmployeeId"); // Truy cập cột NextSupplierId
            }
        } catch (SQLException e) {
            System.out.println("❌ Loi khi lay ID tiep theo cua Employee: " + e.getMessage());
        }
        return -1; // Trả về -1 nếu có lỗi
    }

    @Override
  public List<Employee> searchEmployeeByName(String keyword) {
        List<Employee> employees = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SEARCH_EMPLOYEE_BY_NAME)) {
            pstmt.setString(1, "%" + keyword + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    employees.add(new Employee(
                            rs.getInt("employee_id"),
                            rs.getString("name"),
                            rs.getString("phone"),
                            rs.getString("role"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getObject("created_at", LocalDateTime.class)
                    ));
                }
            }
        } catch (SQLException e) {
            System.out.println("Lỗi tìm kiếm nhân viên: " + e.getMessage());
        }
        return employees;
    }

    
    @Override
    public Employee getEmployeeById(int employeeId) {
        Employee employee = null;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_EMPLOYEE_BY_ID)) {
            stmt.setInt(1, employeeId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    employee = new Employee(
                            rs.getInt("employee_id"),
                            rs.getString("name"),
                            rs.getString("phone"),
                            rs.getString("role"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getObject("created_at", LocalDateTime.class)
                    );
                }
            }
        } catch (SQLException e) {
           System.out.println("❌ Lỗi tìm kiếm nhân  theo id: " + e.getMessage());
        }
        return employee;
    }

}
