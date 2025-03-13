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
    private static final String SELECT_ALL_PRODUCTS = "SELECT * FROM Product";
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
                    rs.getString("name"),
                    rs.getString("category"),
                    rs.getDouble("price"),
                    rs.getString("unit"),
                    rs.getInt("supplier_id"),
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
   public List<Product> searchProductInInventory(String keyword) {
    List<Product> productList = new ArrayList<>();
    // Câu truy vấn SQL để tìm kiếm sản phẩm có trong Inventory
    String SEARCH_PRODUCT_IN_INVENTORY = "SELECT p.product_id, p.name, p.category, p.price, p.unit, p.supplier_id, p.barcode, i.stock_quantity " +
                                         "FROM Product p " +
                                         "INNER JOIN Inventory i ON p.product_id = i.product_id " +
                                         "WHERE (p.name LIKE ? OR p.barcode LIKE ?)";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(SEARCH_PRODUCT_IN_INVENTORY)) {
        pstmt.setString(1, "%" + keyword + "%"); // Tìm kiếm theo tên sản phẩm
        pstmt.setString(2, "%" + keyword + "%"); // Tìm kiếm theo barcode
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            productList.add(new Product(
                rs.getInt("product_id"),
                rs.getString("name"),
                rs.getString("category"),
                rs.getDouble("price"),
                rs.getString("unit"),
                rs.getInt("supplier_id"),
                rs.getInt("stock_quantity"), // Lấy stock_quantity từ Inventory
                rs.getString("barcode")
            ));
        }
    } catch (SQLException e) {
        System.out.println("❌ Lỗi tìm kiếm sản phẩm: " + e.getMessage());
    }
    return productList;
}

    @Override
    public List<Product> getProductsBySupplierId(int supplierId) {
        List<Product> products = new ArrayList<>();
        // Giả sử bạn có một phương thức trong DAO để lấy sản phẩm theo supplierId
        String query = "SELECT * FROM Product WHERE supplier_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, supplierId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getInt("product_id"));
                product.setName(rs.getString("name"));
                product.setSupplierId(rs.getInt("supplier_id"));
                products.add(product);
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi khi truy vấn sản phẩm theo supplier_id: " + e.getMessage());
        }
        return products;

    }
    
    @Override
    public int addProductWithStockEntry(String productName, int supplierId) {
        String query = "INSERT INTO Product (name, supplier_id) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, productName);
            stmt.setInt(2, supplierId);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1); // Trả về product_id mới tạo
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi khi thêm sản phẩm: " + e.getMessage());
        }
        return -1; // Trả về -1 nếu thêm thất bại
    }

    @Override
    public Product getProductByName(String productName) {
        String query = "SELECT * FROM Product WHERE name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, productName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getInt("product_id"));
                product.setName(rs.getString("name"));
                product.setSupplierId(rs.getInt("supplier_id"));
                return product;
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi khi lấy id sản phẩm: " + e.getMessage());
        }
        return null; // Trả về null nếu không tìm thấy sản phẩm
    }
    
    @Override
    public boolean updateStockQuantity(int productId, int quantityChange) {
        String query = "UPDATE Product SET stock_quantity = stock_quantity + ? WHERE product_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, quantityChange);
            pstmt.setInt(2, productId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("❌ Lỗi khi update số lượng sản phẩm: " + e.getMessage());
        }
        return false;
    }
}
