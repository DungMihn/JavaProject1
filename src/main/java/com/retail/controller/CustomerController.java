/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.controller;

import com.retail.service.CustomerService;
import com.retail.model.Customer;
import java.util.List;

/**
 *
 * @author Admin
 */
public class CustomerController {
    private final CustomerService customerService = new CustomerService();


    // Thêm khách hàng mới
    public void addCustomer(String name, String phone, String email, String address) {
        Customer customer = new Customer();
        customer.setName(name);
        customer.setPhone(phone);
        customer.setEmail(email);
        customer.setAddress(address);
        customerService.addCustomer(customer);
    }

    // Lấy danh sách khách hàng
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    // Lấy thông tin khách hàng theo ID
    public Customer getCustomerById(int customerId) {
        return customerService.getCustomerById(customerId);
    }

    // Xóa khách hàng
    public void deleteCustomer(int customerId) {
        customerService.deleteCustomer(customerId);
    }
}
