/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.model;

/**
 *
 * @author Admin
 */
public class SessionManager {
    private static SessionManager instance;
    private int employeeId; // Lưu employee_id của nhân viên đăng nhập

    private SessionManager() {
        // Private constructor để đảm bảo Singleton pattern
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public int getEmployeeId() {
        return employeeId;
    }
}
