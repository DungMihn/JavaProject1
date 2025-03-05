/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.main;

/**
 *
 * @author macbookprom1
 */
import com.retail.dao.CustomerDAO;
import com.retail.dao.InvoiceDAO;
import com.retail.model.Customer;
import com.retail.model.Invoice;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        CustomerDAO customerDAO = new CustomerDAO();
        InvoiceDAO invoiceDAO = new InvoiceDAO();

        // Thêm khách hàng mới
        Customer newCustomer = new Customer(6, "Nguyễn Văn B", "09dđ543d002ddđ1", "ddddddddđda@gmail.com", "Hà Nội");
        customerDAO.addCustomer(newCustomer);

        // Lấy danh sách khách hàng
        List<Customer> customers = customerDAO.getAllCustomers();
        System.out.println("📌 Danh sách khách hàng:");
        for (Customer c : customers) {
            System.out.println(c.getName() + " - " + c.getPhone());
        }

        // Lấy danh sách hóa đơn
        List<Invoice> invoices = invoiceDAO.getAllInvoices();
        System.out.println("📌 Danh sách hóa đơn:");
        for (Invoice i : invoices) {
            System.out.println("ID: " + i.getInvoiceId() + ", Tổng tiền: " + i.getTotalAmount());
        }
    }
}
