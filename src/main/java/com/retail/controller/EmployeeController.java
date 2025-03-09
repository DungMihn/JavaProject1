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

    // Thêm nhân viên mới
    public void addEmployee(String name, String phone, String role) {
        Employee employee = new Employee();
        employee.setName(name);
        employee.setPhone(phone);
        employee.setRole(role);
        employeeService.addEmployee(employee);
    }

    // Lấy danh sách nhân viên
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    // Lấy thông tin nhân viên theo ID
    public Employee getEmployeeById(int employeeId) {
        return employeeService.getEmployeeById(employeeId);
    }

    // Xóa nhân viên
    public void deleteEmployee(int employeeId) {
        employeeService.deleteEmployee(employeeId);
    }
}
