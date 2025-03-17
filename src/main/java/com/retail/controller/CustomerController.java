/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.controller;

/**
 *
 * @author ADMIN
 */


import com.retail.dao.CustomerDAO;
import com.retail.model.Customer;
import java.util.List;

public class CustomerController {
    private final CustomerDAO customerDAO = new CustomerDAO();

    public void displayAllCustomers() {
        List<Customer> customers = customerDAO.getAllCustomers();
        customers.forEach(System.out::println);
    }

    public void addCustomer( String name, String address, String phone, String email) {
        Customer customer = new Customer();
        customer.setName(name);
        customer.setAddress(address);
        customer.setPhone(phone);
        customer.setEmail(email);
        if (customerDAO.insertCustomer(customer)) {
            System.out.println("Them khach hang thanh cong!");
        }
    }

    public void updateCustomer(Customer customer) {
        if (customerDAO.updateCustomer(customer)) {
            System.out.println("Cap nhat thanh cong!");
        }
    }

    public void deleteCustomer(String id) {
        if (customerDAO.deleteCustomer(id)) {
            System.out.println("Xoa thanh cong!");
        }
    }
}




