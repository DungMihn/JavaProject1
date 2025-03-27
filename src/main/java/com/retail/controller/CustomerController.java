package com.retail.controller;

import com.retail.dao.CustomerDAO;
import com.retail.model.Customer;
import java.util.List;

public class CustomerController {
    private CustomerDAO customerDAO;

    public CustomerController() {
        customerDAO = new CustomerDAO();
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
