/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.model;

/**
 *
 * @author Admin
 */
public class CurrentUser {

    private static int employeeId;
    private static String username;
    private static String role;

    public static int getEmployeeId() {
        return employeeId;
    }

    public static void setEmployeeId(int employeeId) {
        CurrentUser.employeeId = employeeId;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        CurrentUser.username = username;
    }

    public static String getRole() {
        return role;
    }

    public static void setRole(String role) {
        CurrentUser.role = role;
    }
}
