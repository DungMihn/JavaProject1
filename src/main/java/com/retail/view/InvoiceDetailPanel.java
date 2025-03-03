/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.view;

/**
 *
 * @author macbookprom1
 */
import com.retail.dao.InvoiceDetailDAO;
import com.retail.model.InvoiceDetail;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class InvoiceDetailPanel extends JDialog {
    private JTable invoiceDetailTable;
    private DefaultTableModel tableModel;
    private InvoiceDetailDAO invoiceDetailDAO;
    private int invoiceId;

    public InvoiceDetailPanel(JFrame parent, int invoiceId) {
        super(parent, "Chi tiết Hóa đơn #" + invoiceId, true);
        this.invoiceDetailDAO = new InvoiceDetailDAO();
        this.invoiceId = invoiceId;

        setLayout(new BorderLayout());
        setSize(600, 400);
        setLocationRelativeTo(parent);

        JLabel lblTitle = new JLabel("🧾 Chi tiết Hóa đơn #" + invoiceId, JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        add(lblTitle, BorderLayout.NORTH);

        String[] columnNames = {"Mã SP", "Số lượng", "Giá", "Tổng"};
        tableModel = new DefaultTableModel(columnNames, 0);
        invoiceDetailTable = new JTable(tableModel);
        add(new JScrollPane(invoiceDetailTable), BorderLayout.CENTER);

        loadInvoiceDetails();

        JButton btnClose = new JButton("Đóng");
        btnClose.addActionListener(e -> dispose());
        JPanel panel = new JPanel();
        panel.add(btnClose);
        add(panel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void loadInvoiceDetails() {
        tableModel.setRowCount(0);
        List<InvoiceDetail> details = invoiceDetailDAO.getInvoiceDetails(invoiceId);
        for (InvoiceDetail detail : details) {
            tableModel.addRow(new Object[]{
                detail.getProductId(), detail.getQuantity(), detail.getPrice(), detail.getSubtotal()
            });
        }
    }
}


