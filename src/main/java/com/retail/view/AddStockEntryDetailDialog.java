/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.view;

/**
 *
 * @author Admin
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddStockEntryDetailDialog extends JDialog {

    private JTextField productIdField;
    private JTextField quantityField;
    private JTextField purchasePriceField;
    private JButton saveButton;
    private JButton cancelButton;

    private boolean saved = false;

    public AddStockEntryDetailDialog(JFrame parent) {
        super(parent, "Thêm chi tiết nhập kho", true);
        setSize(400, 200);
        setLocationRelativeTo(parent);

        // Tạo các trường nhập liệu
        productIdField = new JTextField(20);
        quantityField = new JTextField(20);
        purchasePriceField = new JTextField(20);

        // Tạo các nút bấm
        saveButton = new JButton("Lưu");
        cancelButton = new JButton("Hủy");

        // Bố cục giao diện
        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(new JLabel("Tên sản phẩm:"));
        panel.add(productIdField);
        panel.add(new JLabel("Số lượng:"));
        panel.add(quantityField);
        panel.add(new JLabel("Giá nhập:"));
        panel.add(purchasePriceField);
        panel.add(saveButton);
        panel.add(cancelButton);

        add(panel);

        // Xử lý sự kiện nút Lưu
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saved = true;
                dispose(); // Đóng dialog
            }
        });

        // Xử lý sự kiện nút Hủy
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Đóng dialog
            }
        });
    }

    // Lấy thông tin từ các trường nhập liệu
    public String getProductId() {
        return productIdField.getText();
    }

    public int getQuantity() {
        return Integer.parseInt(quantityField.getText());
    }

    public double getPurchasePrice() {
        return Double.parseDouble(purchasePriceField.getText());
    }

    // Kiểm tra xem người dùng đã lưu hay chưa
    public boolean isSaved() {
        return saved;
    }
}
