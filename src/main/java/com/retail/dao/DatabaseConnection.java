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
    private static final String url = "jdbc:sqlserver://localhost:1433;databaseName=sales_mangement;encrypt=true;trustServerCertificate=true";
    private static final String user = "sa"; 
    private static final String password = "Dung12345!";
    
    public static Connection getConnection() {
        Connection conn = null;
        try {
            // Không cần `Class.forName()` nếu dùng Maven
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("✅ Kết nối SQL Server thành công!");
        } catch (SQLException e) {
            System.out.println("❌ Lỗi kết nối SQL Server: " + e.getMessage());
        }
        return conn;
    }

    public static void main(String[] args) {
        getConnection();
    }
}

