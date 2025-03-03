/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.view;

/**
 *
 * @author macbookprom1
 */
import com.retail.dao.InvoiceDAO;
import com.retail.model.Invoice;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class InvoicePanel extends JPanel {
    private JTable invoiceTable;
    private DefaultTableModel tableModel;
    private JButton btnAdd, btnEdit, btnDelete, btnViewDetails;
    private InvoiceDAO invoiceDAO;

    public InvoicePanel() {
        setLayout(new BorderLayout());
        invoiceDAO = new InvoiceDAO();

        JLabel lblTitle = new JLabel("🧾 Quản lý Hóa đơn", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        add(lblTitle, BorderLayout.NORTH);

        String[] columnNames = {"ID", "Khách hàng", "Nhân viên", "Tổng tiền", "Thanh toán", "Ngày tạo"};
        tableModel = new DefaultTableModel(columnNames, 0);
        invoiceTable = new JTable(tableModel);
        add(new JScrollPane(invoiceTable), BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        btnAdd = new JButton("➕ Thêm hóa đơn");
        btnEdit = new JButton("✏️ Sửa hóa đơn");
        btnDelete = new JButton("🗑️ Xóa hóa đơn");
        btnViewDetails = new JButton("🔍 Xem chi tiết");

        controlPanel.add(btnAdd);
        controlPanel.add(btnEdit);
        controlPanel.add(btnDelete);
        controlPanel.add(btnViewDetails);
        add(controlPanel, BorderLayout.SOUTH);

        loadInvoiceData();

        btnAdd.addActionListener(e -> new AddInvoiceDialog((JFrame) SwingUtilities.getWindowAncestor(this), this));
        btnEdit.addActionListener(e -> editInvoice());
        btnDelete.addActionListener(e -> deleteInvoice());
        btnViewDetails.addActionListener(e -> viewInvoiceDetails());
    }

    public void loadInvoiceData() {
        tableModel.setRowCount(0);
        List<Invoice> invoices = invoiceDAO.getAllInvoices();
        for (Invoice invoice : invoices) {
            tableModel.addRow(new Object[]{
                invoice.getInvoiceId(), invoice.getCustomerId(), invoice.getEmployeeId(),
                invoice.getTotalAmount(), invoice.getPaymentMethod(), invoice.getCreatedAt()
            });
        }
    }

    private void editInvoice() {
        int selectedRow = invoiceTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn cần sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int invoiceId = (int) tableModel.getValueAt(selectedRow, 0);
        int customerId = (int) tableModel.getValueAt(selectedRow, 1);
        int employeeId = (int) tableModel.getValueAt(selectedRow, 2);
        double totalAmount = (double) tableModel.getValueAt(selectedRow, 3);
        String paymentMethod = (String) tableModel.getValueAt(selectedRow, 4);

        Invoice invoice = new Invoice(invoiceId, customerId, employeeId, totalAmount, paymentMethod, null);
        new EditInvoiceDialog((JFrame) SwingUtilities.getWindowAncestor(this), invoice, this);
    }

    private void deleteInvoice() {
        int selectedRow = invoiceTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn để xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int invoiceId = (int) tableModel.getValueAt(selectedRow, 0);
        invoiceDAO.deleteInvoice(invoiceId);
        loadInvoiceData();
    }

    private void viewInvoiceDetails() {
        int selectedRow = invoiceTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn để xem chi tiết!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int invoiceId = (int) tableModel.getValueAt(selectedRow, 0);
        new InvoiceDetailPanel((JFrame) SwingUtilities.getWindowAncestor(this), invoiceId);
    }
}
