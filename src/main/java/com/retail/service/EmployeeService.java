/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.service;

/**
 *
 * @author ADMIN
 */
import com.retail.dao.EmployeeDAO;
import com.retail.model.Employee;

import java.time.LocalDateTime;
import java.util.List;

public class EmployeeService {
    private final EmployeeDAO employeeDAO = new EmployeeDAO();

    public List<Employee> getEmployees() {
        return employeeDAO.getAllEmployees();
    }

    public void addCustomer(String id, String name, String phone, String role) {
        Employee customer = new Employee(id, name, phone, role, LocalDateTime.now());
        employeeDAO.insertEmployee(customer);
    }

    public boolean updateCustomer(Employee customer) {
        return employeeDAO.updateEmployee(customer);
    }

    public boolean deleteCustomer(String employeeID) {
        return employeeDAO.deleteEmployee(employeeID);
    }
}
