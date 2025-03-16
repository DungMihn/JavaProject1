/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.dao;

import com.retail.model.Inventory;
import com.retail.model.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Admin
 */
public class InventoryDAOImpl implements InventoryDAO {

    private static final String INSERT_INVENTORY = "INSERT INTO Inventory (product_id, stock_quantity) VALUES (?, ?)";
    private static final String SELECT_ALL_INVENTORIES = "EXEC sp_GetInventoryWithProduct;";
    private static final String SELECT_INVENTORY_BY_ID = "SELECT * FROM Inventory WHERE inventory_id = ?";
    private static final String SELECT_INVENTORY_BY_PRODUCT_ID = "SELECT * FROM Inventory WHERE product_id = ?";
    private static final String UPDATE_INVENTORY = "UPDATE Inventory SET product_id = ?, stock_quantity = ?, last_updated = ? WHERE inventory_id = ?";
    private static final String DELETE_INVENTORY = "DELETE FROM Inventory WHERE inventory_id = ?";
    private static final String UPDATE_STOCK_QUANTITY = "UPDATE Inventory SET stock_quantity = ?, last_updated = ? WHERE product_id = ?";
    private static final String SEARCH_INVENTORY_BY_PRODUCT_NAME
            = "SELECT i.inventory_id, i.product_id, p.name AS product_name, i.stock_quantity, i.last_updated "
            + "FROM Inventory i "
            + "INNER JOIN Product p ON i.product_id = p.product_id "
            + "WHERE p.name LIKE ?";
    private static final int WARNING_THRESHOLD = 10;

    @Override
    public void addInventory(Inventory inventory) {
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(INSERT_INVENTORY)) {
            pstmt.setInt(1, inventory.getProductId());
            pstmt.setInt(2, inventory.getStockQuantity());
            pstmt.executeUpdate();
            System.out.println("✅ Thêm tồn kho thành công!");
        } catch (SQLException e) {
            System.out.println("❌ Lỗi thêm tồn kho: " + e.getMessage());
        }
    }

    @Override
    public Inventory getInventoryById(int inventoryId) {
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(SELECT_INVENTORY_BY_ID)) {
            pstmt.setInt(1, inventoryId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Inventory(
                        rs.getInt("inventory_id"),
                        rs.getInt("product_id"),
                        rs.getInt("stock_quantity"),
                        rs.getObject("last_updated", LocalDateTime.class)
                );
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi khi lấy thông tin tồn kho: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Inventory getInventoryByProductId(int productId) {
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(SELECT_INVENTORY_BY_PRODUCT_ID)) {
            pstmt.setInt(1, productId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Inventory(
                        rs.getInt("inventory_id"),
                        rs.getInt("product_id"),
                        rs.getInt("stock_quantity"),
                        rs.getObject("last_updated", LocalDateTime.class)
                );
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi khi lấy thông tin tồn kho theo productId: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Inventory> getAllInventories() {
        List<Inventory> inventoryList = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(SELECT_ALL_INVENTORIES); ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Inventory inventory = new Inventory(
                        rs.getInt("inventory_id"),
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getInt("stock_quantity"),
                        rs.getObject("last_updated", LocalDateTime.class) // ❌ Xóa dấu phẩy ở đây
                );

                inventoryList.add(inventory);
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi khi lấy danh sách tồn kho: " + e.getMessage());
        }
        return inventoryList;
    }

    @Override
    public void updateInventory(Inventory inventory) {
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(UPDATE_INVENTORY)) {
            pstmt.setInt(1, inventory.getProductId());
            pstmt.setInt(2, inventory.getStockQuantity());
            pstmt.setObject(3, inventory.getLastUpdated());
            pstmt.setInt(4, inventory.getInventoryId());
            pstmt.executeUpdate();
            System.out.println("✅ Cập nhật tồn kho thành công!");
        } catch (SQLException e) {
            System.out.println("❌ Lỗi cập nhật tồn kho: " + e.getMessage());
        }
    }

    @Override
    public void deleteInventory(int inventoryId) {
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(DELETE_INVENTORY)) {
            pstmt.setInt(1, inventoryId);
            pstmt.executeUpdate();
            System.out.println("✅ Xóa tồn kho thành công!");
        } catch (SQLException e) {
            System.out.println("❌ Lỗi xóa tồn kho: " + e.getMessage());
        }
    }

//    @Override
//    public boolean updateStockQuantity(int productId, int quantityChange) {
//        String updateQuery = "UPDATE Inventory SET stock_quantity = stock_quantity + ?, last_updated = SYSDATETIME() WHERE product_id = ?";
//        String deleteQuery = "DELETE FROM Inventory WHERE product_id = ? AND stock_quantity <= 0";
//
//        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement updateStmt = conn.prepareStatement(updateQuery); PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery)) {
//
//            // Cập nhật stock_quantity
//            updateStmt.setInt(1, quantityChange);
//            updateStmt.setInt(2, productId);
//            int affectedRows = updateStmt.executeUpdate();
//
//            // Xóa bản ghi nếu stock_quantity về 0
//            deleteStmt.setInt(1, productId);
//            deleteStmt.executeUpdate();
//
//            return affectedRows > 0;
//        } catch (SQLException e) {
//            System.out.println("❌ Lỗi xuất kho: " + e.getMessage());
//        }
//        return false;
//    }
    @Override
    public boolean updateStockQuantity(int productId, int quantityChange) {
        String queryCheck = "SELECT * FROM Inventory WHERE product_id = ?";
        String queryUpdate = "UPDATE Inventory SET stock_quantity = stock_quantity + ?, last_updated = SYSDATETIME() WHERE product_id = ?";
        String queryInsert = "INSERT INTO Inventory (product_id, stock_quantity) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmtCheck = conn.prepareStatement(queryCheck); PreparedStatement pstmtUpdate = conn.prepareStatement(queryUpdate); PreparedStatement pstmtInsert = conn.prepareStatement(queryInsert)) {

            // Kiểm tra xem sản phẩm đã tồn tại trong Inventory chưa
            pstmtCheck.setInt(1, productId);
            try (ResultSet rs = pstmtCheck.executeQuery()) {
                if (rs.next()) {
                    // Nếu tồn tại, cập nhật stock_quantity
                    pstmtUpdate.setInt(1, quantityChange);
                    pstmtUpdate.setInt(2, productId);
                    int rowsUpdated = pstmtUpdate.executeUpdate();
                    return rowsUpdated > 0; // Trả về true nếu cập nhật thành công
                } else {
                    // Nếu không tồn tại, thêm bản ghi mới
                    pstmtInsert.setInt(1, productId);
                    pstmtInsert.setInt(2, quantityChange);
                    int rowsInserted = pstmtInsert.executeUpdate();
                    return rowsInserted > 0; // Trả về true nếu thêm thành công
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi khi cập nhật số lượng tồn kho: " + e.getMessage());
            return false; // Trả về false nếu có lỗi
        }
    }

    @Override
    public List<Inventory> searchInventoryByProductName(String productName) {
        List<Inventory> inventoryList = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(SEARCH_INVENTORY_BY_PRODUCT_NAME)) {

            pstmt.setString(1, "%" + productName + "%"); // Tìm kiếm theo tên sản phẩm
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                inventoryList.add(new Inventory(
                        rs.getInt("inventory_id"),
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getInt("stock_quantity"),
                        rs.getTimestamp("last_updated").toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi tìm kiếm tồn kho theo tên sản phẩm: " + e.getMessage());
        }
        return inventoryList;
    }

    @Override
    public List<Product> getLowStockProducts() {
        List<Product> lowStockProducts = new ArrayList<>();
        String query = "SELECT p.product_id, p.name, p.category, p.price, p.unit, p.supplier_id, p.barcode, i.stock_quantity "
                + "FROM Product p "
                + "INNER JOIN Inventory i ON p.product_id = i.product_id "
                + "WHERE i.stock_quantity <= ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, WARNING_THRESHOLD);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                lowStockProducts.add(new Product(
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getDouble("price"),
                        rs.getString("unit"),
                        rs.getInt("supplier_id"),
                        rs.getInt("stock_quantity"),
                        rs.getString("barcode")
                ));
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi khi lấy danh sách sản phẩm sắp hết hàng: " + e.getMessage());
        }
        return lowStockProducts;
    }

    //      Hiển thị cảnh báo sản phẩm sắp hết hàng.
    @Override
    public void showLowStockWarning() {
        List<Product> lowStockProducts = getLowStockProducts();
        if (!lowStockProducts.isEmpty()) {
            StringBuilder warningMessage = new StringBuilder("Cảnh báo: Các sản phẩm sau sắp hết hàng:\n");
            for (Product product : lowStockProducts) {
                warningMessage.append("- ").append(product.getName())
                        .append(" (Số lượng còn: ").append(product.getStockQuantity()).append(")\n");
            }
            JOptionPane.showMessageDialog(null, warningMessage.toString(), "Cảnh báo", JOptionPane.WARNING_MESSAGE);
        }
    }

    @Override
    public List<Inventory> getInventoryReport(String startDate, String endDate) {
        List<Inventory> inventoryReport = new ArrayList<>();

        // Câu lệnh SQL để lấy dữ liệu tồn kho trong khoảng thời gian
        String sql = "SELECT i.inventory_id, i.product_id, p.name AS product_name, i.stock_quantity, i.last_updated "
                + "FROM inventory i JOIN product p ON i.product_id = p.product_id "
                + "WHERE CAST(i.last_updated AS DATE) >= ? AND CAST(i.last_updated AS DATE) < ?";

        try (Connection conn = DatabaseConnection.getConnection(); // Kết nối cơ sở dữ liệu
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Thiết lập tham số cho câu lệnh SQL
            pstmt.setString(1, startDate); // Ngày bắt đầu (ví dụ: "2025-03-13")
            pstmt.setString(2, endDate);   // Ngày kết thúc (ví dụ: "2025-03-16")

            // Log giá trị của startDate và endDate
            System.out.println("Start Date: " + startDate);
            System.out.println("End Date: " + endDate);

            // Thực thi truy vấn
            ResultSet rs = pstmt.executeQuery();

            // Duyệt qua kết quả và thêm vào danh sách
            while (rs.next()) {
                Inventory inventory = new Inventory();
                inventory.setInventoryId(rs.getInt("inventory_id"));
                inventory.setProductName(rs.getString("product_name"));
                inventory.setStockQuantity(rs.getInt("stock_quantity"));

                // Chuyển đổi Timestamp thành LocalDateTime
                Timestamp timestamp = rs.getTimestamp("last_updated");
                if (timestamp != null) {
                    inventory.setLastUpdated(timestamp.toLocalDateTime());
                } else {
                    inventory.setLastUpdated(null); // Xử lý trường hợp last_updated là null
                }

                inventoryReport.add(inventory);
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi khi lấy báo cáo: " + e.getMessage());
        }
        return inventoryReport;
    }

}
