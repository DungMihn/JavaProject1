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

public class AddInvoiceDialog extends JDialog {
    private JTextField txtCustomerId, txtEmployeeId, txtTotalAmount, txtPaymentMethod;
    private JButton btnSave, btnCancel;
    private InvoiceDAO invoiceDAO;
    private InvoicePanel invoicePanel;

    public AddInvoiceDialog(JFrame parent, InvoicePanel invoicePanel) {
        super(parent, "➕ Thêm Hóa đơn", true);
        this.invoiceDAO = new InvoiceDAO();
        this.invoicePanel = invoicePanel;

        setLayout(new GridLayout(5, 2, 10, 10));
        setSize(400, 250);
        setLocationRelativeTo(parent);

        add(new JLabel("Mã khách hàng:"));
        txtCustomerId = new JTextField();
        add(txtCustomerId);

        add(new JLabel("Mã nhân viên:"));
        txtEmployeeId = new JTextField();
        add(txtEmployeeId);

        add(new JLabel("Tổng tiền:"));
        txtTotalAmount = new JTextField();
        add(txtTotalAmount);

        add(new JLabel("Phương thức thanh toán:"));
        txtPaymentMethod = new JTextField();
        add(txtPaymentMethod);

        btnSave = new JButton("💾 Lưu");
        btnCancel = new JButton("❌ Hủy");

        JPanel panelButtons = new JPanel();
        panelButtons.add(btnSave);
        panelButtons.add(btnCancel);
        add(panelButtons);

        btnSave.addActionListener(e -> saveInvoice());
        btnCancel.addActionListener(e -> dispose());

        setVisible(true);
    }

    private void saveInvoice() {
        try {
            int customerId = Integer.parseInt(txtCustomerId.getText());
            int employeeId = Integer.parseInt(txtEmployeeId.getText());
            double totalAmount = Double.parseDouble(txtTotalAmount.getText());
            String paymentMethod = txtPaymentMethod.getText();

            Invoice newInvoice = new Invoice(0, customerId, employeeId, totalAmount, paymentMethod, null);
            invoiceDAO.addInvoice(newInvoice);

            JOptionPane.showMessageDialog(this, "✅ Thêm hóa đơn thành công!");
            invoicePanel.loadInvoiceData();
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "❌ Vui lòng nhập đúng định dạng số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}

