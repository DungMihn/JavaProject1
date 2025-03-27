/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.model;

import java.time.LocalDateTime;

/**
 *
 * @author Admin
 */
public class Employee {
    private int employeeId;
    private String name;
    private String phone;
    private String role;
    private LocalDateTime createAt;

    public Employee(int employeeId, String name, String phone, String role, LocalDateTime createAt) {
        this.employeeId = employeeId;
        this.name = name;
        this.phone = phone;
        this.role = role;
        this.createAt = createAt;
    }

    public Employee(){
        
    }

    @Override
    public String toString() {
        return name; // Trả về tên nhân viên thay vì hash code
    }
    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }
    
    
}
