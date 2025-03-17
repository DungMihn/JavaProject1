/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.controller;

/**
 *
 * @author ADMIN
 */
import com.retail.dao.EmployeeDAO;
import com.retail.model.Employee;
import java.util.List;

public class EmployeeController {
    private final EmployeeDAO employeeDAO = new EmployeeDAO();

    public void displayAllEmployees() {
        List<Employee> employees = employeeDAO.getAllEmployees();
        employees.forEach(System.out::println);
    }

    public void addEmployee( String name, String phone, String role) {
        Employee employee = new Employee();
        employee.setName(name);
        employee.setPhone(phone);
        employee.setRole(role);
        if (employeeDAO.insertEmployee(employee)) {
            System.out.println("Them nhan vien thanh cong!");
        }
    }

    public void updateEmployee(Employee employee) {
        if (employeeDAO.updateEmployee(employee)) {
            System.out.println("Cap nhat thanh cong!");
        }
    }

    public void deleteEmployee(String id) {
        if (employeeDAO.deleteEmployee(id)) {
            System.out.println("Xoa thanh cong!");
        }
    }
  
}