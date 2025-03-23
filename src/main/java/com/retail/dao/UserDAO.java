/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.dao;

/**
 *
 * @author ADMIN
 */
import com.retail.ConnectDB;
import com.retail.model.User;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM Users";

        try (Connection conn = ConnectDB.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                users.add(new User(
                        rs.getString("user_id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getObject("created_at", LocalDateTime.class)
                ));
            }
        } catch (SQLException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
        return users;
    }

    public boolean insertUser(User user) {
        String query = "INSERT INTO Users (name, phone, username, password, role) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConnectDB.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getPhone());
            pstmt.setString(3, user.getUsername());
            pstmt.setString(4, user.getPassword());
            pstmt.setString(5, user.getRole());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi: " + e.getMessage());
            return false;
        }
    }

    public boolean updateUser(User user) {
        String query = "UPDATE Users SET name = ?, phone = ?, username = ?, password = ?, role = ? WHERE user_id = ?";
        try (Connection conn = ConnectDB.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getPhone());
            pstmt.setString(3, user.getUsername());
            pstmt.setString(4, user.getPassword());
            pstmt.setString(5, user.getRole());
            pstmt.setString(6, user.getUserID());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Loi: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteUser(String userID) {
        if (userID == null || userID.isEmpty()) {
            System.out.println("Loi: userID khong duoc de trong!");
            return false;
        }

        String query = "DELETE FROM Users WHERE user_id = ?";
        try (Connection conn = ConnectDB.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, userID);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Loi khi xoa nguoi dung: " + e.getMessage());
            return false;
        }
    }
}

