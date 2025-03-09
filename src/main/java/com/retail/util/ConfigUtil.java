/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.util;

import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author Admin
 */
public class ConfigUtil {
    public static Properties loadConfig() {
        Properties properties = new Properties();
        try (InputStream input = ConfigUtil.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.out.println("❌ Không tìm thấy file config.properties");
                return null;
            }
            properties.load(input);
        } catch (Exception e) {
            System.out.println("❌ Lỗi khi đọc file config.properties: " + e.getMessage());
        }
        return properties;
    }
}
