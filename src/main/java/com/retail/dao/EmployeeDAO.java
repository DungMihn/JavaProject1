/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.retail.dao;

import com.retail.model.Employee;
import java.util.List;

/**
 *
 * @author Admin
 */
public interface EmployeeDAO {

    public boolean insertEmployee(Employee employee);

    boolean updateEmployee(Employee employee);

    public boolean deleteEmployee(String employeeId);

    public Employee getEmployeeById(int employeeId);

    List<Employee> getAllEmployees ();

    public List<Employee> searchEmployeeByName(String keyword);

    public int getNextSupplierId();
}
