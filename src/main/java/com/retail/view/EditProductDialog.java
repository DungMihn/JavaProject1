package com.retail.view;

import com.retail.dao.InventoryDAO;
import com.retail.dao.ProductDAO;
import com.retail.dao.SupplierDAO;
import com.retail.model.Inventory;
import com.retail.model.Product;
import com.retail.model.Supplier;

import javax.swing.*;
import java.awt.*;

public class EditProductDialog extends JDialog {
    private JTextField txtName, txtCategory, txtPrice, txtUnit, txtStockQuantity, txtBarcode, txtSupplierName, txtPurchasePrice;
    private JButton btnSave, btnCancel;
    private ProductDAO productDAO;
    private Product product;
    private ProductPanel productPanel;

    public EditProductDialog(JFrame parent, Product product, ProductPanel productPanel) {
        super(parent, "CẬP NHẬT GIÁ BÁN SẢN PHẨM", true);
        this.productDAO = new ProductDAO();
        this.product = product;
        this.productPanel = productPanel;

        InventoryDAO inventoryDAO = new InventoryDAO();
        int stockQuantity = 0;
        Inventory inventory = inventoryDAO.getInventoryByProductId(product.getProductId());
        if (inventory != null) {
            stockQuantity = inventory.getStockQuantity();
        }

        setSize(500, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // === Panel thông tin chính ===
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(255, 163, 102));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Font fieldFont = new Font("Arial", Font.PLAIN, 14);

        int row = 0;

        // Helper to add a label and field
        JLabel label;
        gbc.gridx = 0; gbc.gridy = row;
        label = new JLabel("Tên sản phẩm:");
        label.setFont(labelFont);
        formPanel.add(label, gbc);
        txtName = new JTextField(product.getName(), 20);
        txtName.setFont(fieldFont);
        txtName.setEditable(false); txtName.setFocusable(false);
        gbc.gridx = 1;
        formPanel.add(txtName, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        label = new JLabel("Loại hàng:");
        label.setFont(labelFont);
        formPanel.add(label, gbc);
        txtCategory = new JTextField(product.getCategory(), 20);
        txtCategory.setFont(fieldFont);
        txtCategory.setEditable(false); txtCategory.setFocusable(false);
        gbc.gridx = 1;
        formPanel.add(txtCategory, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        label = new JLabel("Giá bán:");
        label.setFont(labelFont);
        formPanel.add(label, gbc);
        txtPrice = new JTextField(String.valueOf(product.getPrice()), 20);
        txtPrice.setFont(fieldFont);
        gbc.gridx = 1;
        formPanel.add(txtPrice, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        label = new JLabel("Đơn vị:");
        label.setFont(labelFont);
        formPanel.add(label, gbc);
        txtUnit = new JTextField(product.getUnit(), 20);
        txtUnit.setFont(fieldFont);
        txtUnit.setEditable(false); txtUnit.setFocusable(false);
        gbc.gridx = 1;
        formPanel.add(txtUnit, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        label = new JLabel("Tồn kho:");
        label.setFont(labelFont);
        formPanel.add(label, gbc);
        txtStockQuantity = new JTextField(String.valueOf(stockQuantity), 20);
        txtStockQuantity.setFont(fieldFont);
        txtStockQuantity.setEditable(false); txtStockQuantity.setFocusable(false);
        gbc.gridx = 1;
        formPanel.add(txtStockQuantity, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        label = new JLabel("Mã vạch:");
        label.setFont(labelFont);
        formPanel.add(label, gbc);
        txtBarcode = new JTextField(product.getBarcode(), 20);
        txtBarcode.setFont(fieldFont);
        txtBarcode.setEditable(false); txtBarcode.setFocusable(false);
        gbc.gridx = 1;
        formPanel.add(txtBarcode, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        label = new JLabel("Nhà cung cấp:");
        label.setFont(labelFont);
        formPanel.add(label, gbc);
        SupplierDAO supplierDAO = new SupplierDAO();
        Supplier supplier = supplierDAO.getSupplierById(product.getSupplierId());
        txtSupplierName = new JTextField(supplier != null ? supplier.getName() : "N/A", 20);
        txtSupplierName.setFont(fieldFont);
        txtSupplierName.setEditable(false); txtSupplierName.setFocusable(false);
        gbc.gridx = 1;
        formPanel.add(txtSupplierName, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        label = new JLabel("Giá nhập:");
        label.setFont(labelFont);
        formPanel.add(label, gbc);
        txtPurchasePrice = new JTextField(String.valueOf(product.getPurchasePrice()), 20);
        txtPurchasePrice.setFont(fieldFont);
        txtPurchasePrice.setEditable(false); txtPurchasePrice.setFocusable(false);
        gbc.gridx = 1;
        formPanel.add(txtPurchasePrice, gbc);

        // === Nút hành động ===
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(255, 163, 102));

        btnSave = new JButton("Cập nhật");
        btnSave.setFont(new Font("Arial", Font.BOLD, 14));
        btnSave.setBackground(new Color(255, 132, 51));
        btnSave.setForeground(Color.BLACK);
        ImageIcon saveIcon = new ImageIcon(getClass().getResource("/images/edit.png"));
        Image scaledSaveImage = saveIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH); // set kích thước 20x20 (hoặc theo ý bạn)
        btnSave.setIcon(new ImageIcon(scaledSaveImage));


        btnCancel = new JButton("Hủy");
        btnCancel.setFont(new Font("Arial", Font.BOLD, 14));
        btnCancel.setBackground(new Color(255, 132, 51));
        btnCancel.setForeground(Color.BLACK);
        ImageIcon cancelIcon = new ImageIcon(getClass().getResource("/images/cancelIcon.png"));
        Image scaledCancelImage = cancelIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        btnCancel.setIcon(new ImageIcon(scaledCancelImage));

        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Sự kiện
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
            int supplierId = product.getSupplierId();
            double purchasePrice = Double.parseDouble(txtPurchasePrice.getText());

            Product updatedProduct = new Product(
                product.getProductId(), name, category, price,
                unit, stockQuantity, barcode, supplierId, purchasePrice
            );

            productDAO.updateProduct(updatedProduct);
            JOptionPane.showMessageDialog(this, "✅ Cập nhật sản phẩm thành công!");

            productPanel.getService().loadProductData();
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "❌ Vui lòng nhập đúng định dạng số cho giá bán!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
