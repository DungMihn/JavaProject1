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
public class ProductDAOImpl implements ProductDAO{
    private static final String INSERT_PRODUCT = "INSERT INTO Product (name, category, price, unit, barcode, supplier_id) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SELECT_ALL_PRODUCTS = "EXEC GetProductInventory;";
    private static final String SEARCH_PRODUCT = "SELECT * FROM Product WHERE name LIKE ? OR category LIKE ?";
    private static final String UPDATE_PRODUCT = "UPDATE Product SET name=?, category=?, price=?, unit=?, stock_quantity=?, barcode=?, supplier_id=? WHERE product_id=?";
    private static final String DELETE_PRODUCT = "DELETE FROM Product WHERE product_id=?";
    private static final String GET_PRODUCT_BY_ID = "EXEC GetProductInventoryById @product_id = ?";
    
    private final InventoryDAOImpl inventoryDAO = new InventoryDAOImpl();
    
    @Override
    public void addProduct(Product product) {
        
    String generatedColumns[] = { "product_id" }; // Để lấy ID vừa insert

    try (Connection con = DatabaseConnection.getConnection();
         PreparedStatement pstmt = con.prepareStatement(INSERT_PRODUCT, generatedColumns)) {

        // Thêm sản phẩm vào bảng Product
        pstmt.setString(1, product.getName());
        pstmt.setString(2, product.getCategory());
        pstmt.setDouble(3, product.getPrice());
        pstmt.setString(4, product.getUnit());
        pstmt.setString(5, product.getBarcode());
        pstmt.setInt(6, product.getSupplierId());
        pstmt.executeUpdate();

        // Lấy product_id vừa tạo
        try (ResultSet rs = pstmt.getGeneratedKeys()) {
            if (rs.next()) {
                int productId = rs.getInt(1);
                System.out.println("✅ Thêm sản phẩm thành công với ID: " + productId);

                // Tạo tồn kho với product_id vừa lấy được
                inventoryDAO.addInventory(new Inventory(productId, product.getStockQuantity()));
            }
        }

    } catch (SQLException e) {
        System.out.println("❌ Lỗi thêm sản phẩm: " + e.getMessage());
    }
}


    @Override
    public void updateProduct(Product product) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_PRODUCT)) {
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
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELETE_PRODUCT)) {
            pstmt.setInt(1, productId);
            pstmt.executeUpdate();
            System.out.println("✅ Xóa sản phẩm thành công!");
        } catch (SQLException e) {
            System.out.println("❌ Lỗi xóa sản phẩm: " + e.getMessage());
        }
    }

    /**
     *
     * @param productId
     * @return
     */
    @Override
    public Product getProductById(int productId) {
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(GET_PRODUCT_BY_ID)) {
            pstmt.setInt(1, productId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                    rs.getInt("product_id");
                    rs.getString("product_name");
                    rs.getString("category");
                    rs.getDouble("price");
                    rs.getString("unit");
                    rs.getInt("supplier_id");
                    rs.getInt("stock_quantity");
                    rs.getString("barcode");
                    rs.getString("supplier_name");
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi khi lấy thông tin hóa đơn: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
         try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_PRODUCTS)) {

            while (rs.next()) {
                Product product = new Product(
                    rs.getInt("product_id"),
                    rs.getString("product_name"),
                    rs.getString("category"),
                    rs.getDouble("price"),
                    rs.getString("unit"),
                    rs.getInt("supplier_id"),   
                    rs.getString("supplier_name"),
                    rs.getInt("stock_quantity"),
                    rs.getString("barcode")
                    
                );
                productList.add(product);
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi truy vấn sản phẩm: " + e.getMessage());
        }
        return productList;
    }
    
    
    // Tìm kiếm sản phẩm
    @Override
    public List<Product> searchProducts(String keyword) {
        List<Product> productList = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SEARCH_PRODUCT)) {
            pstmt.setString(1, "%" + keyword + "%");
            pstmt.setString(2, "%" + keyword + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                productList.add(new Product(
                    rs.getInt("product_id"),
                    rs.getString("name"),
                    rs.getString("category"),
                    rs.getDouble("price"),
                    rs.getString("unit"),
                    rs.getInt("supplierId"),
                    rs.getString("supplierName"),
                    rs.getInt("stock_quantity"),
                    rs.getString("barcode")
                ));
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi tìm kiếm sản phẩm: " + e.getMessage());
        }
        return productList;
    }
}
