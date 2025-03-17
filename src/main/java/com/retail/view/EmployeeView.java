/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.view;

/**
 *
 * @author ADMIN
 */
import com.retail.controller.EmployeeController;
import java.util.Scanner;

public class EmployeeView {
    private static final Scanner scanner = new Scanner(System.in);
    private static final EmployeeController controller = new EmployeeController();

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n--- QUAN LY NHAN VIEN ---");
            System.out.println("1. Hien thi danh sach nhanh vien");
            System.out.println("2. Them nhan vien moi");
            System.out.println("3. Xoa nhanh vien");
            System.out.println("4. Thoat");
            System.out.print("Chon chuc nang: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Xóa bộ nhớ đệm

            switch (choice) {
                case 1 -> controller.displayAllEmployees();
                case 2 -> addNewEmployee();
                case 3 -> deleteEmployee();
                case 4 -> {
                    System.out.println("Thoat chuong trinh!");
                    System.exit(0);
                }
                default -> System.out.println("Lua chon khong hop le! Vui long chon lai.");
            }
        }
    }

    private static void addNewEmployee() {
        System.out.print("Nhap ten: ");
        String name = scanner.nextLine();
        System.out.print("Nhap so dien thoai: ");
        String phone = scanner.nextLine();
        System.out.print("Nhap role: ");
        String role = scanner.nextLine();

        controller.addEmployee(name, phone, role);
    }

    private static void deleteEmployee() {
        System.out.print("Nhap ID nhanh vien can xoa: ");
        String id = scanner.nextLine();
        controller.deleteEmployee(id);
    }
}
