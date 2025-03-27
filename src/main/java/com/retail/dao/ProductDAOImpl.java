/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.dao;

import com.retail.model.Product;
import com.retail.model.Inventory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class ProductDAOImpl implements ProductDAO {

    private static final String SELECT_ALL_PRODUCTS = "SELECT * FROM Product";
    private static final String SEARCH_PRODUCT = "SELECT * FROM Product WHERE name LIKE ? OR category LIKE ? OR barcode LIKE ?";
    private static final String UPDATE_PRODUCT = "UPDATE Product SET name=?, category=?, price=?, unit=?, stock_quantity=?, barcode=?, supplier_id=? WHERE product_id=?";
    private static final String DELETE_PRODUCT = "DELETE FROM Product WHERE product_id=?";

    @Override
    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(SELECT_ALL_PRODUCTS)) {
            while (rs.next()) {
                Product product = new Product(
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getDouble("price"),
                        rs.getString("unit"),
                        rs.getInt("stock_quantity"),
                        rs.getString("barcode"),
                        rs.getInt("supplier_id"),
                        rs.getDouble("purchase_price")
                );
                productList.add(product);
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi truy vấn sản phẩm: " + e.getMessage());
        }
        return productList;
    }

    @Override
    public List<Product> searchProducts(String keyword) {
        List<Product> productList = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(SEARCH_PRODUCT)) {
            String param = "%" + keyword + "%";
            pstmt.setString(1, param);
            pstmt.setString(2, param);
            pstmt.setString(3, param);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                productList.add(new Product(
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getDouble("price"),
                        rs.getString("unit"),
                        rs.getInt("stock_quantity"),
                        rs.getString("barcode"),
                        rs.getInt("supplier_id"),
                        rs.getDouble("purchase_price")
                ));
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi tìm kiếm sản phẩm: " + e.getMessage());
        }
        return productList;
    }

    @Override
    public Product getProductById(int productId) {
        Product product = null;
        String sql = "SELECT * FROM Product WHERE product_id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, productId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                product = new Product(
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getDouble("price"),
                        rs.getString("unit"),
                        rs.getInt("stock_quantity"),
                        rs.getString("barcode"),
                        rs.getInt("supplier_id"),
                        rs.getDouble("purchase_price")
                );
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return product;
    }

    @Override
    public Product getProductByBarcode(String barcode) {
        Product product = null;
        String sql = "SELECT * FROM Product WHERE barcode = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, barcode);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    product = new Product(
                            rs.getInt("product_id"),
                            rs.getString("name"),
                            rs.getString("category"),
                            rs.getDouble("price"),
                            rs.getString("unit"),
                            rs.getInt("stock_quantity"),
                            rs.getString("barcode"),
                            rs.getInt("supplier_id"),
                            rs.getDouble("purchase_price")
                    );
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return product;
    }

    // Lấy danh sách các mã vạch sản phẩm
    @Override
    public List<String> getAllProductBarcodes() {
        List<String> barcodes = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(SELECT_ALL_PRODUCTS)) {
            while (rs.next()) {
                String barcode = rs.getString("barcode");
                if (barcode != null && !barcode.isEmpty()) {
                    barcodes.add(barcode);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return barcodes;
    }

    // Lấy danh sách các loại sản phẩm (category) duy nhất
    @Override
    public List<String> getDistinctCategories() {
        List<String> categories = new ArrayList<>();
        String sql = "SELECT DISTINCT category FROM Product";
        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String category = rs.getString("category");
                if (category != null && !category.isEmpty()) {
                    categories.add(category);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return categories;
    }

    @Override
    public void updateProduct(Product product) {
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(UPDATE_PRODUCT)) {
            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getCategory());
            pstmt.setDouble(3, product.getPrice());
            pstmt.setString(4, product.getUnit());
            pstmt.setInt(5, product.getStockQuantity());
            pstmt.setString(6, product.getBarcode());
            pstmt.setInt(7, product.getSupplierId());
            pstmt.setInt(8, product.getProductId());
            pstmt.executeUpdate();
            System.out.println("✅ Cập nhật sản phẩm thành công!");
        } catch (SQLException e) {
            System.out.println("❌ Lỗi cập nhật sản phẩm: " + e.getMessage());
        }
    }

    @Override
    public void deleteProduct(int productId) {
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(DELETE_PRODUCT)) {
            pstmt.setInt(1, productId);
            pstmt.executeUpdate();
            System.out.println("✅ Xóa sản phẩm thành công!");
        } catch (SQLException e) {
            System.out.println("❌ Lỗi xóa sản phẩm: " + e.getMessage());
        }
    }
}
