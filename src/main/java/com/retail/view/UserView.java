/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.view;

/**
 *
 * @author ADMIN
 */
import com.retail.controller.UserController;
import java.util.Scanner;

public class UserView {
    private static final Scanner scanner = new Scanner(System.in);
    private static final UserController controller = new UserController();

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n--- QUAN LY NGUOI DUNG ---");
            System.out.println("1. Hien thi danh sach nguoi dung");
            System.out.println("2. Them nguoi dung moi");
            System.out.println("3. Xoa nguoi dung");
            System.out.println("4. Thoat");
            System.out.print("Chon chuc nang: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Xóa bộ nhớ đệm

            switch (choice) {
                case 1 -> controller.displayAllUsers();
                case 2 -> addNewUser();
                case 3 -> deleteUser();
                case 4 -> {
                    System.out.println("Thoat chuong trinh!");
                    System.exit(0);
                }
                default -> System.out.println("Lua chon khong hop le! Vui long chon lai.");
            }
        }
    }

    private static void addNewUser() {
        System.out.print("Nhap ten: ");
        String name = scanner.nextLine();
        System.out.print("Nhap so dien thoai: ");
        String phone = scanner.nextLine();
        System.out.print("Nhap username: ");
        String username = scanner.nextLine();
        System.out.print("Nhap password: ");
        String password = scanner.nextLine();
        System.out.print("Nhap role: ");
        String role = scanner.nextLine();

        controller.addUser(name, phone, username, password, role);
    }

    private static void deleteUser() {
        System.out.print("Nhap ID nguoi dung can xoa: ");
        String id = scanner.nextLine();
        controller.deleteUser(id);
    }
}

