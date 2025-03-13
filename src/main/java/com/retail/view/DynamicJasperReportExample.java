/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.view;

/**
 *
 * @author Admin
 */
import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.builders.ColumnBuilderException;
import ar.com.fdvs.dj.domain.builders.FastReportBuilder;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

public class DynamicJasperReportExample extends JFrame {
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=GroceryStoreDB;encrypt=true;trustServerCertificate=true";
    private static final String USER = "bookoff";
    private static final String PASSWORD = "123456789";
    private JButton btnGenerateReport;

    public DynamicJasperReportExample() {
        setTitle("Dynamic Jasper Report in Java Swing");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());

        btnGenerateReport = new JButton("Generate Report");
        btnGenerateReport.addActionListener(e -> generateReport());

        add(btnGenerateReport);
    }

    private void generateReport() {
        try {
            // 2️⃣ Xây dựng báo cáo với Dynamic Jasper
            try ( // 1️⃣ Kết nối CSDL (Thay thế bằng thông tin của bạn)
                   Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
                // 2️⃣ Xây dựng báo cáo với Dynamic Jasper
                FastReportBuilder drb = new FastReportBuilder();
                drb.addColumn("ID", "id", Integer.class.getName(), 30)
                        .addColumn("Name", "name", String.class.getName(), 50)
                        .setTitle("Dynamic Jasper Report Example")
                        .setSubtitle("Generated using Dynamic Jasper in Java Swing")
                        .setUseFullPageWidth(true);
                DynamicReport dr = drb.build();
                // 3️⃣ Tạo JasperPrint từ dữ liệu
                JRDataSource ds = new JREmptyDataSource();
                JasperPrint jp = DynamicJasperHelper.generateJasperPrint(dr, new ClassicLayoutManager(), ds);
                // 4️⃣ Hiển thị báo cáo trong JasperViewer
                JasperViewer.viewReport(jp, false);
                // 5️⃣ Đóng kết nối
            }

        } catch (ColumnBuilderException | ClassNotFoundException | SQLException | JRException ex) {
            JOptionPane.showMessageDialog(this, "Error generating report: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DynamicJasperReportExample().setVisible(true));
    }
}
