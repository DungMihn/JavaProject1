package com.retail.view;

import com.retail.service.CustomerPanelService;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CustomerPanel extends JPanel {
    private static CustomerPanel instance;

    private JTable customerTable;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JButton btnSearch, btnAdd, btnEdit, btnDelete;
    private CustomerPanelService service;

    public CustomerPanel() {
        instance = this;
        setLayout(new BorderLayout());

        // === Phần trên: filterPanel ===
        JPanel filterPanel = new JPanel(new GridBagLayout());
        filterPanel.setBackground(new Color(255, 163, 102)); // Màu cam dịu
        filterPanel.setBorder(new LineBorder(Color.BLACK));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);

        // Hàng 0: Tiêu đề "QUẢN LÝ KHÁCH HÀNG"
        JLabel lblHeader = new JLabel("QUẢN LÝ KHÁCH HÀNG");
        lblHeader.setFont(new Font("Arial", Font.BOLD, 24));
        lblHeader.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 5;
        gbc.anchor = GridBagConstraints.CENTER;
        filterPanel.add(lblHeader, gbc);

        // Hàng 1: Label tìm + ô nhập + nút tìm
        JLabel lblSearch = new JLabel("Tìm khách hàng:");
        lblSearch.setFont(new Font("Arial", Font.BOLD, 20));
        lblSearch.setForeground(Color.WHITE);
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        filterPanel.add(lblSearch, gbc);

        txtSearch = new JTextField(20);
        txtSearch.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        filterPanel.add(txtSearch, gbc);

        btnSearch = new JButton("Tìm kiếm");
        btnSearch.setFont(new Font("Arial", Font.BOLD, 14));
        btnSearch.setBackground(new Color(255, 132, 51));
        btnSearch.setForeground(Color.BLACK);
        btnSearch.setMargin(new Insets(10, 20, 10, 20));
        // btnSearch
        ImageIcon searchIcon = new ImageIcon(getClass().getResource("/images/search.png"));
        Image scaledSearchIcon = searchIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        btnSearch.setIcon(new ImageIcon(scaledSearchIcon));
        btnSearch.setHorizontalTextPosition(SwingConstants.RIGHT);
        btnSearch.setIconTextGap(10);
        gbc.gridx = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        filterPanel.add(btnSearch, gbc);

        // Hàng 2: Các nút Thêm - Sửa - Xoá (nằm giữa)
        btnAdd = new JButton("Thêm mới");
        btnAdd.setFont(new Font("Arial", Font.BOLD, 14));
        btnAdd.setBackground(new Color(255, 132, 51));
        btnAdd.setForeground(Color.BLACK);
        btnAdd.setMargin(new Insets(10, 20, 10, 20));
        // btnAdd
        ImageIcon addIcon = new ImageIcon(getClass().getResource("/images/adduser.png"));
        Image scaledAddIcon = addIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        btnAdd.setIcon(new ImageIcon(scaledAddIcon));
        btnAdd.setHorizontalTextPosition(SwingConstants.RIGHT);
        btnAdd.setIconTextGap(10);

        btnEdit = new JButton("Sửa");
        btnEdit.setFont(new Font("Arial", Font.BOLD, 14));
        btnEdit.setBackground(new Color(255, 132, 51));
        btnEdit.setForeground(Color.BLACK);
        btnEdit.setMargin(new Insets(10, 20, 10, 20));
        // btnEdit
        ImageIcon editIcon = new ImageIcon(getClass().getResource("/images/edit.png"));
        Image scaledEditIcon = editIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        btnEdit.setIcon(new ImageIcon(scaledEditIcon));
        btnEdit.setHorizontalTextPosition(SwingConstants.RIGHT);
        btnEdit.setIconTextGap(10);

        btnDelete = new JButton("Xoá");
        btnDelete.setFont(new Font("Arial", Font.BOLD, 14));
        btnDelete.setBackground(new Color(255, 132, 51));
        btnDelete.setForeground(Color.BLACK);
        btnDelete.setMargin(new Insets(10, 20, 10, 20));
        // btnDelete
        ImageIcon deleteIcon = new ImageIcon(getClass().getResource("/images/delete.png"));
        Image scaledDeleteIcon = deleteIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        btnDelete.setIcon(new ImageIcon(scaledDeleteIcon));
        btnDelete.setHorizontalTextPosition(SwingConstants.RIGHT);
        btnDelete.setIconTextGap(10);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        actionPanel.setBackground(new Color(255, 163, 102));
        actionPanel.add(btnAdd);
        actionPanel.add(btnEdit);
        actionPanel.add(btnDelete);

        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 5;
        gbc.anchor = GridBagConstraints.CENTER;
        filterPanel.add(actionPanel, gbc);

        // === Tiêu đề bảng (trong phần chính) ===
        JLabel lblTableTitle = new JLabel("DANH SÁCH KHÁCH HÀNG");
        lblTableTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTableTitle.setForeground(Color.WHITE);
        lblTableTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(new Color(255, 102, 0));
        topPanel.add(Box.createVerticalStrut(10));
        topPanel.add(filterPanel);
        topPanel.add(Box.createVerticalStrut(10));
        topPanel.add(lblTableTitle);
        topPanel.add(Box.createVerticalStrut(10));

        // === Bảng khách hàng ===
        String[] columnNames = {"ID", "Tên", "SĐT", "Email", "Địa chỉ", "Ngày tạo"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };

        customerTable = new JTable(tableModel);
        customerTable.setRowHeight(25);
        customerTable.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(customerTable);
        scrollPane.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Danh sách khách hàng"));

        // === Chia phần trên (30%) và bảng (70%) ===
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topPanel, scrollPane);
        splitPane.setResizeWeight(0.2);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerSize(8);

        add(splitPane, BorderLayout.CENTER);

        service = new CustomerPanelService(this);
    }

    public static CustomerPanel getInstance() {
        return instance;
    }

    public JTable getCustomerTable() { return customerTable; }
    public DefaultTableModel getTableModel() { return tableModel; }
    public JTextField getTxtSearch() { return txtSearch; }
    public JButton getBtnSearch() { return btnSearch; }
    public JButton getBtnAdd() { return btnAdd; }
    public JButton getBtnEdit() { return btnEdit; }
    public JButton getBtnDelete() { return btnDelete; }
    public CustomerPanelService getService() { return service; }
}
