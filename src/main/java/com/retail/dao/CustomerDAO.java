/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.retail.dao;

import com.retail.model.Customer;
import java.util.List;

/**
 *
 * @author Admin
 */
public interface CustomerDAO {
    boolean addCustomer(Customer customer);
    Customer getCustomerByPhone(String phone);
    Customer getCustomerByEmail(String email);
    Customer getCustomerById(int customerId);
    List<Customer> getAllCustomers();
    boolean updateCustomer(Customer customer);
    boolean deleteCustomer(int customerId);
    List<Customer> searchCustomers(String keyword);
    List<String> getAllCustomerPhones();
}
