/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.view;

/**
 *
 * @author ADMIN
 */



import com.retail.controller.CustomerController;
import java.util.Scanner;

public class CustomerView {
    private static final Scanner scanner = new Scanner(System.in);
    private static final CustomerController controller = new CustomerController();

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n--- QUAN LY KHACH HANG ---");
            System.out.println("1. Hien thi danh sach khach hang");
            System.out.println("2. Them khach hang moi");
            System.out.println("3. Xoa khach hang");
            System.out.println("4. Thoat");
            System.out.print("Chon chuc nang: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Xóa bộ nhớ đệm

            switch (choice) {
                case 1 -> controller.displayAllCustomers();
                case 2 -> addNewCustomer();
                case 3 -> deleteCustomer();
                case 4 -> {
                    System.out.println("Thoat chuong trinh!");
                    System.exit(0);
                }
                default -> System.out.println("Lua chon khong hop le! Vui long chon lai.");
            }
        }
    }

    private static void addNewCustomer() {
        System.out.print("Nhap ten: ");
        String name = scanner.nextLine();
        System.out.print("Nhap dia chi: ");
        String address = scanner.nextLine();
        System.out.print("Nhap so dien thoai: ");
        String phone = scanner.nextLine();
        System.out.print("Nhap email: ");
        String email = scanner.nextLine();

        controller.addCustomer(name, address, phone, email);
    }

    private static void deleteCustomer() {
        System.out.print("Nhap ID khach hang can xoa: ");
        String id = scanner.nextLine();
        controller.deleteCustomer(id);
    }
}




