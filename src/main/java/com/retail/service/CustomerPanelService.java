package com.retail.service;

import com.retail.controller.CustomerController;
import com.retail.model.Customer;
import com.retail.view.CustomerDialog;
import com.retail.view.CustomerPanel;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class CustomerPanelService {
    private CustomerPanel panel;
    private CustomerController controller;
    
    public CustomerPanelService(CustomerPanel panel) {
        this.panel = panel;
        this.controller = new CustomerController();
        initEventHandlers();
        loadCustomerData();
    }
    
    // Gán các sự kiện cho các thành phần UI từ panel
    private void initEventHandlers() {
        panel.getBtnSearch().addActionListener(e -> searchCustomer());
        panel.getBtnAdd().addActionListener(e -> addCustomer());
        panel.getBtnEdit().addActionListener(e -> editCustomer());
        panel.getBtnDelete().addActionListener(e -> deleteCustomer());
        
        // Gán DocumentListener cho ô tìm kiếm
        panel.getTxtSearch().getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                searchCustomer();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                if (panel.getTxtSearch().getText().trim().isEmpty()) {
                    loadCustomerData();
                } else {
                    searchCustomer();
                }
            }
            @Override
            public void changedUpdate(DocumentEvent e) { }
        });
        
        // Sự kiện double-click vào row trong bảng
        panel.getCustomerTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 2) {
                    editCustomer();
                }
            }
        });
    }
    
    // Phương thức tải dữ liệu khách hàng từ controller và cập nhật lên bảng
    public void loadCustomerData() {
        DefaultTableModel tableModel = panel.getTableModel();
        tableModel.setRowCount(0);
        List<Customer> customers = controller.getAllCustomers();
        for (Customer customer : customers) {
            Object[] rowData = {
                customer.getCustomerId(),
                customer.getName(),
                customer.getPhone(),
                customer.getEmail(),
                customer.getAddress(),
                customer.getCreatedAt()
            };
            tableModel.addRow(rowData);
        }
    }
    
    public void searchCustomer() {
        String keyword = panel.getTxtSearch().getText().trim();
        DefaultTableModel tableModel = panel.getTableModel();
        if (keyword.isEmpty()) {
            loadCustomerData();
        } else {
            List<Customer> customers = controller.searchCustomers(keyword);
            tableModel.setRowCount(0);
            for (Customer customer : customers) {
                Object[] rowData = {
                    customer.getCustomerId(),
                    customer.getName(),
                    customer.getPhone(),
                    customer.getEmail(),
                    customer.getAddress(),
                    customer.getCreatedAt()
                };
                tableModel.addRow(rowData);
            }
        }
    }
    
    public void addCustomer() {
        Customer newCustomer = CustomerDialog.showDialog(null);
        if (newCustomer != null) {
            boolean result = controller.addCustomer(newCustomer);
            if (result) {
                JOptionPane.showMessageDialog(panel, "Thêm khách hàng thành công!");
                loadCustomerData();
            } else {
                JOptionPane.showMessageDialog(panel, "Thêm khách hàng thất bại!");
            }
        }
    }
    
    public void editCustomer() {
        JTable table = panel.getCustomerTable();
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(panel, "Vui lòng chọn khách hàng cần sửa!");
            return;
        }
        DefaultTableModel tableModel = panel.getTableModel();
        int customerId = (int) tableModel.getValueAt(selectedRow, 0);
        String name = (String) tableModel.getValueAt(selectedRow, 1);
        String phone = (String) tableModel.getValueAt(selectedRow, 2);
        String email = (String) tableModel.getValueAt(selectedRow, 3);
        String address = (String) tableModel.getValueAt(selectedRow, 4);
        Customer customer = new Customer(customerId, name, phone, email, address, null);
        Customer updatedCustomer = CustomerDialog.showDialog(panel, customer);
        if (updatedCustomer != null) {
            boolean result = controller.updateCustomer(updatedCustomer);
            if (result) {
                JOptionPane.showMessageDialog(panel, "Cập nhật khách hàng thành công!");
                loadCustomerData();
            } else {
                JOptionPane.showMessageDialog(panel, "Cập nhật khách hàng thất bại!");
            }
        }
    }
    
    public void deleteCustomer() {
        JTable table = panel.getCustomerTable();
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(panel, "Vui lòng chọn khách hàng cần xoá!");
            return;
        }
        DefaultTableModel tableModel = panel.getTableModel();
        int customerId = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(panel, "Bạn có chắc muốn xoá khách hàng này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean result = controller.deleteCustomer(customerId);
            if (result) {
                JOptionPane.showMessageDialog(panel, "Xoá khách hàng thành công!");
                loadCustomerData();
            } else {
                JOptionPane.showMessageDialog(panel, "Xoá khách hàng thất bại!");
            }
        }
    }
}
