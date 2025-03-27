/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.controller;

import com.retail.dao.CustomerDAOImpl;
import com.retail.model.Customer;
import java.util.List;

/**
 *
 * @author Admin
 */
public class CustomerController {

    private CustomerDAOImpl customerDAO;

    public CustomerController() {
        customerDAO = new CustomerDAOImpl();
    }

    public List<Customer> getAllCustomers() {
        return customerDAO.getAllCustomers();
    }

    public List<Customer> searchCustomers(String keyword) {
        return customerDAO.searchCustomers(keyword);
    }

    public boolean addCustomer(Customer customer) {
        return customerDAO.addCustomer(customer);
    }

    public boolean updateCustomer(Customer customer) {
        return customerDAO.updateCustomer(customer);
    }

    public boolean deleteCustomer(int customerId) {
        return customerDAO.deleteCustomer(customerId);
    }
}
