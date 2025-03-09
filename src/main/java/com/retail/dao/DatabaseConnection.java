/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.dao;

import com.retail.util.ConfigUtil;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 *
 * @author Admin
 */
public class DatabaseConnection {
    private static String URL;
    private static String USER;
    private static String PASSWORD;

    static {
        // Đọc cấu hình từ file config.properties
        Properties config = ConfigUtil.loadConfig();
        if (config != null) {
            URL = config.getProperty("db.url");
            USER = config.getProperty("db.user");
            PASSWORD = config.getProperty("db.password");
        } else {
            System.out.println("❌ Không thể đọc cấu hình từ file config.properties");
        }
    }

    public static Connection getConnection() {
        Connection con = null;
        try {
            con = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Kết nối SQL Server thành công!");
        } catch (SQLException e) {
            System.out.println("❌ Lỗi kết nối SQL Server: " + e.getMessage());
        }
        return con;
    }
}
