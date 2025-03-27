package com.retail.view;

import com.retail.controller.ReportPanelController;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ReportPanel extends JPanel {
    private JComboBox<String> cmbReportType;
    private JButton btnGenerateReport;
    private JLabel lblOverallRevenue;
    private JTable revenueTable;
    private DefaultTableModel revenueTableModel;
    private JTable bestSellingTable;
    private DefaultTableModel bestSellingTableModel;

    public ReportPanel() {
        setLayout(new BorderLayout());

        // === Phần trên: ReportSearchBox ===
        JPanel filterPanel = new JPanel(new GridBagLayout());
        filterPanel.setBackground(new Color(255, 163, 102));
        filterPanel.setBorder(new LineBorder(Color.BLACK));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Hàng 1: Tiêu đề
        JLabel lblTitle = new JLabel("QUẢN LÝ BÁO CÁO");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 5;
        gbc.anchor = GridBagConstraints.CENTER;
        filterPanel.add(lblTitle, gbc);

        gbc.gridwidth = 1;

        // Hàng 2: Label + ComboBox
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel lblChoose = new JLabel("Chọn báo cáo");
        lblChoose.setFont(new Font("Arial", Font.BOLD, 20));
        lblChoose.setForeground(Color.WHITE);
        filterPanel.add(lblChoose, gbc);

        gbc.gridx = 1;
        cmbReportType = new JComboBox<>(new String[]{"Doanh thu theo ngày", "Doanh thu theo tháng"});
        cmbReportType.setFont(new Font("Arial", Font.BOLD, 14));
        cmbReportType.setPreferredSize(new Dimension(300, 60));
        filterPanel.add(cmbReportType, gbc);

        // Hàng 2: Nút tìm kiếm (icon giống ProductPanel)
        gbc.gridx = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        btnGenerateReport = new JButton("Xem Báo Cáo");
        btnGenerateReport.setFont(new Font("Arial", Font.BOLD, 14));
        btnGenerateReport.setBackground(new Color(255, 132, 51));
        btnGenerateReport.setForeground(Color.BLACK);
        btnGenerateReport.setMargin(new Insets(10, 20, 10, 20));
        // Resize icon for btnGenerateReport
        ImageIcon reportIcon = new ImageIcon(getClass().getResource("/images/search.png"));
        Image scaledReportImage = reportIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        btnGenerateReport.setIcon(new ImageIcon(scaledReportImage));
        btnGenerateReport.setHorizontalTextPosition(SwingConstants.RIGHT);
        btnGenerateReport.setIconTextGap(10);
        filterPanel.add(btnGenerateReport, gbc);

        // Hàng 3: Tổng doanh thu
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel lblTotal = new JLabel("Tổng doanh thu:");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 24));
        lblTotal.setForeground(Color.WHITE);
        filterPanel.add(lblTotal, gbc);

        gbc.gridx = 1;
        lblOverallRevenue = new JLabel("0");
        lblOverallRevenue.setFont(new Font("Arial",Font.BOLD, 26));
        lblOverallRevenue.setForeground(Color.BLACK);
        lblOverallRevenue.setPreferredSize(new Dimension(200, 30));
        filterPanel.add(lblOverallRevenue, gbc);

        // === Label dưới cùng "DANH SÁCH BÁO CÁO" ===
        JLabel lblReportList = new JLabel("DANH SÁCH BÁO CÁO");
        lblReportList.setFont(new Font("Arial", Font.BOLD, 24));
        lblReportList.setForeground(Color.WHITE);
        lblReportList.setAlignmentX(Component.CENTER_ALIGNMENT);

        // === Bảng doanh thu ===
        String[] revenueColumns = {"Invoice ID", "Khách ", "Nhân viên", "Tổng tiền", "Discount", "Thành tiền", "Ngày tạo"};
        revenueTableModel = new DefaultTableModel(revenueColumns, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        revenueTable = new JTable(revenueTableModel);
        revenueTable.setRowHeight(25);
        revenueTable.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane revenueScrollPane = new JScrollPane(revenueTable);
        revenueScrollPane.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Báo cáo doanh thu"));

        // === Bảng SP bán chạy ===
        JPanel sellProductBox = new JPanel(new BorderLayout());
        sellProductBox.setBackground(new Color(255, 193, 153));
        sellProductBox.setBorder(new LineBorder(Color.BLACK));

        JLabel lblTopSelling = new JLabel("Top Sản Phẩm Bán Chạy");
        lblTopSelling.setFont(new Font("Arial", Font.BOLD, 20));
        lblTopSelling.setForeground(Color.BLACK);
        lblTopSelling.setHorizontalAlignment(SwingConstants.CENTER); // Căn giữa nhãn
        sellProductBox.add(lblTopSelling, BorderLayout.NORTH);
        
        String[] bestSellingColumns = {"Tên SP", "Số lượng bán", "Doanh thu"};
        bestSellingTableModel = new DefaultTableModel(bestSellingColumns, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        bestSellingTable = new JTable(bestSellingTableModel);
        bestSellingTable.setRowHeight(25);
        bestSellingTable.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane bestSellingScrollPane = new JScrollPane(bestSellingTable);
        bestSellingScrollPane.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Top Sản Phẩm Bán Chạy"));
        sellProductBox.add(bestSellingScrollPane, BorderLayout.CENTER);

        // === SplitPane 3 phần: 20% - 50% - 30% ===
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(new Color(255, 102, 0));
        topPanel.add(Box.createVerticalStrut(10));
        topPanel.add(filterPanel);
        topPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        topPanel.add(lblReportList);
        topPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JSplitPane splitBottom = new JSplitPane(JSplitPane.VERTICAL_SPLIT, revenueScrollPane, sellProductBox);
        splitBottom.setResizeWeight(0.625);
        splitBottom.setDividerSize(8);

        JSplitPane splitTop = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topPanel, splitBottom);
        splitTop.setResizeWeight(0.15);
        splitTop.setDividerSize(8);

        add(splitTop, BorderLayout.CENTER);

        new ReportPanelController(this);
    }

    public JComboBox<String> getCmbReportType() { return cmbReportType; }
    public JButton getBtnGenerateReport() { return btnGenerateReport; }
    public JLabel getLblOverallRevenue() { return lblOverallRevenue; }
    public JTable getRevenueTable() { return revenueTable; }
    public JTable getBestSellingTable() { return bestSellingTable; }
    public DefaultTableModel getRevenueTableModel() { return revenueTableModel; }
    public DefaultTableModel getBestSellingTableModel() { return bestSellingTableModel; }
}
