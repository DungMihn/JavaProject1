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
import java.awt.*;

public class EditInvoiceDialog extends JDialog {
    private JTextField txtCustomerId, txtEmployeeId, txtTotalAmount, txtPaymentMethod;
    private JButton btnSave, btnCancel;
    private InvoiceDAO invoiceDAO;
    private Invoice invoice;
    private InvoicePanel invoicePanel;

    public EditInvoiceDialog(JFrame parent, Invoice invoice, InvoicePanel invoicePanel) {
        super(parent, "✏️ Chỉnh sửa Hóa đơn", true);
        this.invoiceDAO = new InvoiceDAO();
        this.invoice = invoice;
        this.invoicePanel = invoicePanel;

        setLayout(new GridLayout(5, 2, 10, 10));
        setSize(400, 250);
        setLocationRelativeTo(parent);

        add(new JLabel("Mã khách hàng:"));
        txtCustomerId = new JTextField(String.valueOf(invoice.getCustomerId()));
        add(txtCustomerId);

        add(new JLabel("Mã nhân viên:"));
        txtEmployeeId = new JTextField(String.valueOf(invoice.getEmployeeId()));
        add(txtEmployeeId);

        add(new JLabel("Tổng tiền:"));
        txtTotalAmount = new JTextField(String.valueOf(invoice.getTotalAmount()));
        add(txtTotalAmount);

        add(new JLabel("Phương thức thanh toán:"));
        txtPaymentMethod = new JTextField(invoice.getPaymentMethod());
        add(txtPaymentMethod);

        btnSave = new JButton("💾 Cập nhật");
        btnCancel = new JButton("❌ Hủy");

        btnSave.addActionListener(e -> updateInvoice());
        btnCancel.addActionListener(e -> dispose());

        setVisible(true);
    }

    private void updateInvoice() {
        invoice.setCustomerId(Integer.parseInt(txtCustomerId.getText()));
        invoice.setEmployeeId(Integer.parseInt(txtEmployeeId.getText()));
        invoice.setTotalAmount(Double.parseDouble(txtTotalAmount.getText()));
        invoice.setPaymentMethod(txtPaymentMethod.getText());

        invoiceDAO.updateInvoice(invoice);
        invoicePanel.loadInvoiceData();
        dispose();
    }
}

