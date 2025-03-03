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

public class EditProductDialog extends JDialog {
    private JTextField txtName, txtCategory, txtPrice, txtUnit, txtStockQuantity, txtBarcode;
    private JButton btnSave, btnCancel;
    private ProductDAO productDAO;
    private Product product;
    private ProductPanel productPanel;

    public EditProductDialog(JFrame parent, Product product, ProductPanel productPanel) {
        super(parent, "✏️ Chỉnh sửa Sản phẩm", true);
        this.productDAO = new ProductDAO();
        this.product = product;
        this.productPanel = productPanel;

        setLayout(new GridLayout(7, 2, 10, 10));
        setSize(400, 300);
        setLocationRelativeTo(parent);

        add(new JLabel("Tên sản phẩm:"));
        txtName = new JTextField(product.getName());
        add(txtName);

        add(new JLabel("Loại:"));
        txtCategory = new JTextField(product.getCategory());
        add(txtCategory);

        add(new JLabel("Giá:"));
        txtPrice = new JTextField(String.valueOf(product.getPrice()));
        add(txtPrice);

        add(new JLabel("Đơn vị:"));
        txtUnit = new JTextField(product.getUnit());
        add(txtUnit);

        add(new JLabel("Số lượng:"));
        txtStockQuantity = new JTextField(String.valueOf(product.getStockQuantity()));
        add(txtStockQuantity);

        add(new JLabel("Mã vạch:"));
        txtBarcode = new JTextField(product.getBarcode());
        add(txtBarcode);

        btnSave = new JButton("💾 Cập nhật");
        btnCancel = new JButton("❌ Hủy");

        JPanel panelButtons = new JPanel();
        panelButtons.add(btnSave);
        panelButtons.add(btnCancel);
        add(panelButtons);

        btnSave.addActionListener(e -> updateProduct());
        btnCancel.addActionListener(e -> dispose());

        setVisible(true);
    }

    private void updateProduct() {
        try {
            String name = txtName.getText();
            String category = txtCategory.getText();
            double price = Double.parseDouble(txtPrice.getText());
            String unit = txtUnit.getText();
            int stockQuantity = Integer.parseInt(txtStockQuantity.getText());
            String barcode = txtBarcode.getText();

            Product updatedProduct = new Product(product.getProductId(), name, category, price, unit, stockQuantity, barcode);
            productDAO.updateProduct(updatedProduct);

            JOptionPane.showMessageDialog(this, "✅ Cập nhật sản phẩm thành công!");
            productPanel.loadProductData(); // Cập nhật bảng sản phẩm
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "❌ Vui lòng nhập đúng định dạng số cho giá và số lượng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
