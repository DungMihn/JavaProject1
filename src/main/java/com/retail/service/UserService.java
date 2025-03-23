/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.service;

/**
 *
 * @author ADMIN
 */
import com.retail.dao.UserDAO;
import com.retail.model.User;
import java.time.LocalDateTime;
import java.util.List;

public class UserService {
    private final UserDAO userDAO = new UserDAO();

    public List<User> getUsers() {
        return userDAO.getAllUsers();
    }

    public void addUser(String id, String name, String phone, String username, String password, String role) {
        User user = new User(id, name, phone, username, password, role, LocalDateTime.now());
        userDAO.insertUser(user);
    }

    public boolean updateUser(User user) {
        return userDAO.updateUser(user);
    }

    public boolean deleteUser(String userID) {
        return userDAO.deleteUser(userID);
    }
    
}

