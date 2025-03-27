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


    // Lấy danh sách tất cả nhân viên
    public List<Employee> getAllEmployees() {
        return employeeDAO.getAllEmployees();
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
        
        Employee newEmployee = new Employee(name, phone, role, username, password);
        return employeeDAO.insertEmployee(newEmployee);
    }

    // Cập nhật thông tin nhân viên
    public boolean updateEmployee(Employee update) {       
        return employeeDAO.updateEmployee(update);
    }

    // Xóa nhân viên theo ID
     public boolean deleteEmployee(int id) {
        if (id <= 0) {
            System.out.println("❌ Loi: ID nhan vien khong hop le!");
            return false;
        }
        return employeeDAO.deleteEmployee(String.valueOf(id));
    }
    
    // Lấy ID nhân viên tiếp theo
    public int getNextEmployeeId() {
        return employeeDAO.getNextSupplierId();
    }
    
    // Tìm kiếm nhân viên theo tên
    public List<Employee> searchEmployeesByName(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            System.out.println("Loi: Tu khoa tim kiem khong hop le!");
            return List.of(); // Trả về danh sách rỗng nếu từ khóa không hợp lệ
        }
        return employeeDAO.searchEmployeeByName(keyword);
    }
}
