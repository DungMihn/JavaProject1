/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.controller;

/**
 *
 * @author ADMIN
 */
import com.retail.dao.UserDAO;
import com.retail.model.User;
import java.util.List;

public class UserController {
    private final UserDAO userDAO = new UserDAO();

    public void displayAllUsers() {
        List<User> users = userDAO.getAllUsers();
        users.forEach(System.out::println);
    }

    public void addUser(String name, String phone, String username, String password, String role) {
        User user = new User();
        user.setName(name);
        user.setPhone(phone);
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(role);
        
        if (userDAO.insertUser(user)) {
            System.out.println("Them nguoi dung thanh cong!");
        }
    }

    public void updateUser(User user) {
        if (userDAO.updateUser(user)) {
            System.out.println("Cap nhat nguoi dung thanh cong!");
        }
    }

    public void deleteUser(String userID) {
        if (userDAO.deleteUser(userID)) {
            System.out.println("Xoa nguoi dung thanh cong!");
        }
    }
}

