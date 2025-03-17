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

public class Customer {
    private String customerID;
    private String name;
    private String address;
    private String phone;
    private String email;
    private LocalDateTime createdAt;

    public Customer(String customerID, String name, String address, String phone, String email, LocalDateTime createdAt) {
        this.customerID = customerID;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.createdAt = createdAt;
    }

    public Customer() {
        
    }

    public String getCustomerID() { return customerID; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setName(String name){
        this.name = name;
    }
    public void setAddress(String address){
        this.address = address;
    }
    public void setPhone(String phone){
        this.phone = phone;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public void setCreate_At(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    // Getters và Setters

    @Override
    public String toString() {
        return "ID: " + customerID + ", Name: " + name + ", Address: " + address +
                ", Phone: " + phone + ", Email: " + email + ", Created At: " + createdAt;
    }
}



