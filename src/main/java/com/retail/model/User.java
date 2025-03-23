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

public class User {
    private String user_id;
    private String name;
    private String phone;
    private String username;
    private String password;
    private String role;
    private LocalDateTime createdAt;

    public User(String user_id, String name, String phone, String username, String password, String role, LocalDateTime createdAt) {
        this.user_id = user_id;
        this.name = name;
        this.phone = phone;
        this.username = username;
        this.password = password;
        this.role = role;
        this.createdAt = createdAt;
    }

    public User() {}

    public String getUserID() { return user_id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setName(String name) { this.name = name; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(String role) { this.role = role; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "ID: " + user_id + ", Name: " + name + ", Phone: " + phone + ", Username: " + username + 
               ", Role: " + role + ", Created At: " + createdAt;
    }
}
