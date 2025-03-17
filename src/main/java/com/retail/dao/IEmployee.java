/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.dao;

/**
 *
 * @author ADMIN
 */
import com.retail.model.Employee;
import java.util.List;

public interface IEmployee {
    List<Employee> getAllEmployee();
    void insertEmployee(Employee employees);
}
