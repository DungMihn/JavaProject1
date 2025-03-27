/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.controller;

import com.retail.service.EmployeeService;
import com.retail.model.Employee;
import java.util.List;

/**
 *
 * @author Admin
 */
public class EmployeeController {
    private final EmployeeService employeeService = new EmployeeService();
    

    // Lấy danh sách tất cả nhân viên
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    // Thêm nhân viên mới
   public boolean addEmployee(String name, String phone, String role, String username, String password) {
        if (name == null || name.trim().isEmpty() ||
            phone == null || phone.trim().isEmpty() ||
            role == null || role.trim().isEmpty() ||
            username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            System.out.println("❌ Lỗi: Thông tin nhân viên không được để trống!");
            return false;
        }
        return employeeService.addEmployee(name, phone, role, username, password);
    }

    // Cập nhật thông tin nhân viên
    public boolean updateEmployee(Employee update) {
        return employeeService.updateEmployee(update);
    }

    // Xóa nhân viên
    public boolean deleteEmployee(int employeeID) {
        if (employeeID <= 0) {
            System.out.println("ID nhân viên không hợp lệ!");
            return false;
        }
        return employeeService.deleteEmployee(employeeID);
    }

    // Lấy ID nhân viên tiếp theo (nếu cần)
    public int getNextEmployeeId() {
        return employeeService.getNextEmployeeId();
    }

    // Tìm kiếm nhân viên theo tên
    public List<Employee> searchEmployeesByName(String keyword) {
        return employeeService.searchEmployeesByName(keyword);
    }
}
