/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.dao;

/**
 *
 * @author macbookprom1
 */
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static final String CONFIG_FILE = "/Users/macbookprom1/NetBeansProjects/RetailManagementSystem/src/resources/config.properties";

    public static Connection getConnection() {
        Connection conn = null;
        Properties properties = new Properties();

        try {
            // Đọc file config.properties
            FileInputStream fis = new FileInputStream(CONFIG_FILE);
            properties.load(fis);

            // Lấy thông tin từ file
            String url = properties.getProperty("db.url");
            String user = properties.getProperty("db.user");
            String password = properties.getProperty("db.password");

            // Kết nối đến database
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("✅ Kết nối MySQL thành công!");
        } catch (IOException e) {
            System.out.println("❌ Lỗi đọc file cấu hình: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("❌ Lỗi kết nối MySQL: " + e.getMessage());
        }
        return conn;
    }

    public static void main(String[] args) {
        getConnection();
    }
}

