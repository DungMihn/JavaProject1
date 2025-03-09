/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.service;

import com.retail.dao.CustomerDAOImpl;
import com.retail.model.Customer;
import java.util.List;

/**
 *
 * @author Admin
 */
public class CustomerService {
    private final CustomerDAOImpl customerDAO = new CustomerDAOImpl();



    // Thêm khách hàng
    public void addCustomer(Customer customer) {
        customerDAO.addCustomer(customer);
        System.out.println("✅ Khách hàng đã được thêm thành công!");
    }

    // Lấy tất cả khách hàng
    public List<Customer> getAllCustomers() {
        return customerDAO.getAllCustomers(); 
    }

    // Lấy khách hàng theo ID
    public Customer getCustomerById(int customerId) {
        return customerDAO.getCustomerById(customerId);
    }

    // Xóa khách hàng
    public void deleteCustomer(int customerId) {
        customerDAO.deleteCustomer(customerId);
        System.out.println("✅ Khách hàng đã được xóa thành công!");
    }
}
