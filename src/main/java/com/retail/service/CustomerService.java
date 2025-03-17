/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.service;

/**
 *
 * @author ADMIN
 */

import com.retail.dao.CustomerDAO;
import com.retail.model.Customer;

import java.time.LocalDateTime;
import java.util.List;

public class CustomerService {
    private final CustomerDAO customerDAO = new CustomerDAO();

    public List<Customer> getCustomers() {
        return customerDAO.getAllCustomers();
    }

    public void addCustomer(String id, String name, String address, String phone, String email) {
        Customer customer = new Customer(id, name, address, phone, email, LocalDateTime.now());
        customerDAO.insertCustomer(customer);
    }

    public boolean updateCustomer(Customer customer) {
        return customerDAO.updateCustomer(customer);
    }

    public boolean deleteCustomer(String customerID) {
        return customerDAO.deleteCustomer(customerID);
    }
}

