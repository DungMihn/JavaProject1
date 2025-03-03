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
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ProductPanel extends JPanel {
    private JTable productTable;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JButton btnAdd, btnEdit, btnDelete, btnSearch;

    private ProductDAO productDAO;

    public ProductPanel() {
        setLayout(new BorderLayout());
        productDAO = new ProductDAO();

        // Tiêu đề
        JLabel lblTitle = new JLabel("📦 Quản lý Sản phẩm", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        add(lblTitle, BorderLayout.NORTH);

        // Bảng hiển thị sản phẩm
        String[] columnNames = {"ID", "Tên sản phẩm", "Loại", "Giá", "Đơn vị", "Số lượng", "Mã vạch"};
        tableModel = new DefaultTableModel(columnNames, 0);
        productTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(productTable);
        add(scrollPane, BorderLayout.CENTER);

        // Panel chức năng
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());

        txtSearch = new JTextField(20);
        btnSearch = new JButton("🔍 Tìm kiếm");
        btnAdd = new JButton("➕ Thêm");
        btnEdit = new JButton("✏️ Sửa");
        btnDelete = new JButton("🗑️ Xóa");

        controlPanel.add(new JLabel("Tìm sản phẩm:"));
        controlPanel.add(txtSearch);
        controlPanel.add(btnSearch);
        controlPanel.add(btnAdd);
        controlPanel.add(btnEdit);
        controlPanel.add(btnDelete);

        add(controlPanel, BorderLayout.SOUTH);

        // Load dữ liệu sản phẩm vào bảng
        loadProductData();

        // Xử lý sự kiện
        btnSearch.addActionListener(e -> searchProduct());
        btnAdd.addActionListener(e -> new AddProductDialog((JFrame) SwingUtilities.getWindowAncestor(this), this));
        btnEdit.addActionListener(e -> editProduct());
        btnDelete.addActionListener(e -> deleteProduct());

        // Cho phép chỉnh sửa trực tiếp trên bảng và tự động lưu vào database
        tableModel.addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                int row = e.getFirstRow();
                updateProductFromTable(row);
            }
        });
    }

    public void loadProductData() {
        tableModel.setRowCount(0);
        List<Product> productList = productDAO.getAllProducts();
        for (Product product : productList) {
            Object[] rowData = {
                product.getProductId(),
                product.getName(),
                product.getCategory(),
                product.getPrice(),
                product.getUnit(),
                product.getStockQuantity(),
                product.getBarcode()
            };
            tableModel.addRow(rowData);
        }
    }

    private void searchProduct() {
        String keyword = txtSearch.getText().trim();
        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập từ khóa tìm kiếm!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        tableModel.setRowCount(0);
        List<Product> productList = productDAO.searchProducts(keyword);
        for (Product product : productList) {
            Object[] rowData = {
                product.getProductId(),
                product.getName(),
                product.getCategory(),
                product.getPrice(),
                product.getUnit(),
                product.getStockQuantity(),
                product.getBarcode()
            };
            tableModel.addRow(rowData);
        }
    }

    private void editProduct() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int productId = (int) tableModel.getValueAt(selectedRow, 0);
        String name = (String) tableModel.getValueAt(selectedRow, 1);
        String category = (String) tableModel.getValueAt(selectedRow, 2);
        double price = (double) tableModel.getValueAt(selectedRow, 3);
        String unit = (String) tableModel.getValueAt(selectedRow, 4);
        int stockQuantity = (int) tableModel.getValueAt(selectedRow, 5);
        String barcode = (String) tableModel.getValueAt(selectedRow, 6);

        Product product = new Product(productId, name, category, price, unit, stockQuantity, barcode);
        new EditProductDialog((JFrame) SwingUtilities.getWindowAncestor(this), product, this);
    }

    private void deleteProduct() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa sản phẩm này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int productId = (int) tableModel.getValueAt(selectedRow, 0);
            productDAO.deleteProduct(productId);
            loadProductData(); // Cập nhật lại bảng sau khi xóa
        }
    }

    // ✅ Cập nhật sản phẩm trong database khi người dùng chỉnh sửa trên bảng JTable
    private void updateProductFromTable(int row) {
        try {
            int productId = (int) tableModel.getValueAt(row, 0);
            String name = (String) tableModel.getValueAt(row, 1);
            String category = (String) tableModel.getValueAt(row, 2);
            double price = Double.parseDouble(tableModel.getValueAt(row, 3).toString());
            String unit = (String) tableModel.getValueAt(row, 4);
            int stockQuantity = Integer.parseInt(tableModel.getValueAt(row, 5).toString());
            String barcode = (String) tableModel.getValueAt(row, 6);

            Product updatedProduct = new Product(productId, name, category, price, unit, stockQuantity, barcode);
            productDAO.updateProduct(updatedProduct);
            JOptionPane.showMessageDialog(this, "✅ Cập nhật sản phẩm thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Lỗi cập nhật sản phẩm: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
