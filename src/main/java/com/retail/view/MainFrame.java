/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.view;

/**
 *
 * @author macbookprom1
 */
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private ProductPanel productPanel;
    private InvoicePanel invoicePanel;

    public MainFrame() {
        setTitle("Phần mềm quản lý bán hàng - Cửa hàng tạp hóa");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Tạo menu
        JMenuBar menuBar = new JMenuBar();

        JMenu menuProduct = new JMenu("Sản phẩm");
        JMenuItem menuItemManageProduct = new JMenuItem("Quản lý sản phẩm");

        JMenu menuInvoice = new JMenu("Hóa đơn");
        JMenuItem menuItemManageInvoice = new JMenuItem("Quản lý hóa đơn");

        JMenu menuCustomer = new JMenu("Khách hàng");
        JMenuItem menuItemManageCustomer = new JMenuItem("Quản lý khách hàng");

        JMenu menuReport = new JMenu("Báo cáo");
        JMenuItem menuItemViewReport = new JMenuItem("Xem báo cáo");

        JMenu menuLogout = new JMenu("Hệ thống");
        JMenuItem menuItemExit = new JMenuItem("Thoát");

        // Thêm menuItem vào menu
        menuProduct.add(menuItemManageProduct);
        menuInvoice.add(menuItemManageInvoice);
        menuCustomer.add(menuItemManageCustomer);
        menuReport.add(menuItemViewReport);
        menuLogout.add(menuItemExit);

        // Thêm menu vào menuBar
        menuBar.add(menuProduct);
        menuBar.add(menuInvoice);
        menuBar.add(menuCustomer);
        menuBar.add(menuReport);
        menuBar.add(menuLogout);

        setJMenuBar(menuBar);

        // Tạo panel chính dùng CardLayout để chứa các màn hình
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Khởi tạo các panel
        JPanel homePanel = new JPanel();
        homePanel.add(new JLabel("🏠 Chào mừng bạn đến với phần mềm quản lý cửa hàng tạp hóa!"));

        productPanel = new ProductPanel();
        invoicePanel = new InvoicePanel();
        JPanel customerPanel = new JPanel();
        JPanel reportPanel = new JPanel();

        // Thêm các panel vào CardLayout
        mainPanel.add(homePanel, "Home");
        mainPanel.add(productPanel, "Product");
        mainPanel.add(invoicePanel, "Invoice");
        mainPanel.add(customerPanel, "Customer");
        mainPanel.add(reportPanel, "Report");

        add(mainPanel);

        // Xử lý sự kiện menu
        menuItemManageProduct.addActionListener(e -> cardLayout.show(mainPanel, "Product"));
        menuItemManageInvoice.addActionListener(e -> {
            invoicePanel.loadInvoiceData(); // Cập nhật dữ liệu mỗi khi mở tab
            cardLayout.show(mainPanel, "Invoice");
        });
        menuItemManageCustomer.addActionListener(e -> cardLayout.show(mainPanel, "Customer"));
        menuItemViewReport.addActionListener(e -> cardLayout.show(mainPanel, "Report"));

        menuItemExit.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(null, "Bạn có chắc muốn thoát?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        // Hiển thị giao diện
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}
