/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.view;

/**
 *
 * @author macbookprom1
 */
import com.retail.dao.ProductDAO;
import com.retail.model.Product;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddProductDialog extends JDialog {
    private JTextField txtName, txtCategory, txtPrice, txtUnit, txtStockQuantity, txtBarcode;
    private JButton btnSave, btnCancel;
    private ProductDAO productDAO;
    private ProductPanel productPanel;

    public AddProductDialog(JFrame parent, ProductPanel productPanel) {
        super(parent, "➕ Thêm Sản phẩm", true);
        this.productDAO = new ProductDAO();
        this.productPanel = productPanel;

        setLayout(new GridLayout(7, 2, 10, 10));
        setSize(400, 300);
        setLocationRelativeTo(parent);

        add(new JLabel("Tên sản phẩm:"));
        txtName = new JTextField();
        add(txtName);

        add(new JLabel("Loại:"));
        txtCategory = new JTextField();
        add(txtCategory);

        add(new JLabel("Giá:"));
        txtPrice = new JTextField();
        add(txtPrice);

        add(new JLabel("Đơn vị:"));
        txtUnit = new JTextField();
        add(txtUnit);

        add(new JLabel("Số lượng:"));
        txtStockQuantity = new JTextField();
        add(txtStockQuantity);

        add(new JLabel("Mã vạch:"));
        txtBarcode = new JTextField();
        add(txtBarcode);

        btnSave = new JButton("💾 Lưu");
        btnCancel = new JButton("❌ Hủy");

        JPanel panelButtons = new JPanel();
        panelButtons.add(btnSave);
        panelButtons.add(btnCancel);
        add(panelButtons);

        btnSave.addActionListener(e -> saveProduct());
        btnCancel.addActionListener(e -> dispose());

        setVisible(true);
    }

    private void saveProduct() {
        try {
            String name = txtName.getText();
            String category = txtCategory.getText();
            double price = Double.parseDouble(txtPrice.getText());
            String unit = txtUnit.getText();
            int stockQuantity = Integer.parseInt(txtStockQuantity.getText());
            String barcode = txtBarcode.getText();

            Product newProduct = new Product(0, name, category, price, unit, stockQuantity, barcode);
            productDAO.addProduct(newProduct);

            JOptionPane.showMessageDialog(this, "✅ Thêm sản phẩm thành công!");
            productPanel.loadProductData(); // Cập nhật bảng sản phẩm
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "❌ Vui lòng nhập đúng định dạng số cho giá và số lượng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}

