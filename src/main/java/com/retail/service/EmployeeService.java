/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.service;

import com.retail.dao.EmployeeDAOImpl;
import com.retail.model.Employee;
import java.util.List;


/**
 *
 * @author Admin
 */
public class EmployeeService {
    private final EmployeeDAOImpl employeeDAO = new EmployeeDAOImpl();

    // Thêm nhân viên
    public void addEmployee(Employee employee) {
        employeeDAO.addEmployee(employee);
        System.out.println("✅ Nhân viên đã được thêm thành công!");
    }

    // Lấy tất cả nhân viên
    public List<Employee> getAllEmployees() {
        return employeeDAO.getAllEmployees(); 
    }

    // Lấy nhân viên theo ID
    public Employee getEmployeeById(int employeeId) {
        return employeeDAO.getEmployeeById(employeeId);
    }

    // Xóa nhân viên
    public void deleteEmployee(int employeeId) {
        employeeDAO.deleteEmployee(employeeId);
        System.out.println("✅ Nhân viên đã được xóa thành công!");
    }
}
