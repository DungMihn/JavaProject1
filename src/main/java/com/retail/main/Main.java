/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.main;

import com.retail.controller.ProductController;
import java.io.UnsupportedEncodingException;
import java.util.List;
import com.retail.model.Product;
/**
 *
 * @author Admin
 */
public class Main {
    public static void main(String[] args) {
         ProductController productController = new ProductController();
        try {
            System.setOut(new java.io.PrintStream(System.out, true, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            System.out.println("Lỗi khi đặt encoding UTF-8!");
        }
        
        System.out.println("\n=== Danh sách sản phẩm ===");
        List<Product> products = productController.getAllProducts();
        for (Product product : products) {
            System.out.println(product.getName());
        }
        }
}
