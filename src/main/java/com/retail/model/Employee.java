/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.model;

/**
 *
 * @author ADMIN
 */
import java.time.LocalDateTime;

public class Employee {
    private int employeeID;
    private String name;
    private String phone;
    private String role;
    private LocalDateTime createdAt;

    public Employee(int employeeID, String name, String phone, String role, LocalDateTime createdAt) {
        this.employeeID = employeeID;
        this.name = name;
        this.phone = phone;
        this.role = role;
        this.createdAt = createdAt;
    }

    public Employee() {
        
    }

    public int getEmployeeID() { return employeeID; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getRole() { return role; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    
    public void setEmployeeID(int ID){
        this.employeeID = ID;
    }
    
    public void setName(String name){
        this.name = name;
    }
    public void setPhone(String phone){
        this.phone = phone;
    }
    public void setRole(String role){
        this.role = role;
    }
    public void setCreate_At(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    // Getters và Setters

    @Override
    public String toString() {
        return "ID: " + employeeID + ", Name: " + name + ", Phone: " + phone + ", Role: " + role + ", Created At: " + createdAt;
    }

}
