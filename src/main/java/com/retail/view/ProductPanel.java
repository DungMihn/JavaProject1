package com.retail.view;

import com.retail.service.ProductPanelService;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ProductPanel extends JPanel {
    private static ProductPanel instance;

    private JTable productTable;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JButton btnSearch, btnEdit;
    private JComboBox<String> cmbCategory;
    private JComboBox<String> cmbSupplier;
    private ProductPanelService service;

    private JPanel container;
    private JPanel topPanel;
    private JPanel filterPanel;
    private JPanel bottomPanel;
    private JLabel lblManageProducts;
    private JLabel lblProductList;

    public ProductPanel() {
    instance = this;
    setLayout(new BorderLayout());

    container = new JPanel(new BorderLayout());
    container.setBackground(new Color(255, 102, 0));
    container.setBorder(new LineBorder(Color.BLACK, 2));

    topPanel = new JPanel();
    topPanel.setBackground(new Color(255, 102, 0));
    topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

    filterPanel = new JPanel(new GridBagLayout());
    filterPanel.setBackground(new Color(255, 163, 102));
    filterPanel.setBorder(new LineBorder(Color.BLACK));
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);

    // Hàng 0: tiêu đề "QUẢN LÝ SẢN PHẨM"
    lblManageProducts = new JLabel("QUẢN LÝ SẢN PHẨM");
    lblManageProducts.setFont(new Font("Arial", Font.BOLD, 24));
    lblManageProducts.setForeground(Color.WHITE);
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 5;
    gbc.anchor = GridBagConstraints.CENTER;
    filterPanel.add(lblManageProducts, gbc);

    // Hàng 1: "Nhà CC" và "Loại"
    gbc.gridwidth = 1;
    gbc.gridy = 1;
    gbc.gridx = 0;
    gbc.anchor = GridBagConstraints.EAST;
    JLabel lblSupplier = new JLabel("Nhà CC");
    lblSupplier.setFont(new Font("Arial", Font.BOLD, 20));
    lblSupplier.setForeground(Color.WHITE);
    filterPanel.add(lblSupplier, gbc);

    gbc.gridx = 1;
    cmbSupplier = new JComboBox<>();
    cmbSupplier.addItem("Beverage");
    cmbSupplier.addItem("Vegetable");
    cmbSupplier.addItem("Meat");
    cmbSupplier.addItem("Clothes");
    cmbSupplier.addItem("Tất cả");
    cmbSupplier.setFont(new Font("Arial", Font.BOLD, 14));
    cmbSupplier.setPreferredSize(new Dimension(150, 40));
    filterPanel.add(cmbSupplier, gbc);

    gbc.gridx = 2;
    gbc.anchor = GridBagConstraints.EAST;
    JLabel lblCategory = new JLabel("Loại");
    lblCategory.setFont(new Font("Arial", Font.BOLD, 20));
    lblCategory.setForeground(Color.WHITE);
    filterPanel.add(lblCategory, gbc);

    gbc.gridx = 3;
    cmbCategory = new JComboBox<>();
    cmbCategory.addItem("Beverage");
    cmbCategory.addItem("Vegetable");
    cmbCategory.addItem("Meat");
    cmbCategory.addItem("Clothes");
    cmbCategory.addItem("Tất cả");
    cmbCategory.setFont(new Font("Arial", Font.BOLD, 14));
    cmbCategory.setPreferredSize(new Dimension(150, 40));
    filterPanel.add(cmbCategory, gbc);

    gbc.gridx = 4;
    JButton btnSync = new JButton("Refresh");
    btnSync.setFont(new Font("Arial", Font.BOLD, 14));
    btnSync.setBackground(new Color(255, 132, 51));
    btnSync.setForeground(Color.BLACK);
    btnSync.setMargin(new Insets(10, 20, 10, 20));
    ImageIcon syncIcon = new ImageIcon(getClass().getResource("/images/transfer.png"));
    Image scaledSyncImage = syncIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
    btnSync.setIcon(new ImageIcon(scaledSyncImage));
    btnSync.setHorizontalTextPosition(SwingConstants.RIGHT);
    btnSync.setIconTextGap(10);
    filterPanel.add(btnSync, gbc);
    // -----------------------------------------------------

    // Hàng 2: "Tìm SP/ Mã vạch", ô tìm kiếm, nút Tìm kiếm và nút Chỉnh sửa
    gbc.gridy = 2;
    gbc.gridx = 0;
    gbc.anchor = GridBagConstraints.EAST;
    JLabel lblSearch = new JLabel("Tìm SP/ Mã vạch");
    lblSearch.setFont(new Font("Arial", Font.BOLD, 20));
    lblSearch.setForeground(Color.WHITE);
    filterPanel.add(lblSearch, gbc);

    gbc.gridx = 1;
    gbc.gridwidth = 2;
    gbc.anchor = GridBagConstraints.WEST;
    txtSearch = new JTextField(20);
    txtSearch.setPreferredSize(new Dimension(350, 35));
    txtSearch.setFont(new Font("Arial", Font.PLAIN, 16));
    filterPanel.add(txtSearch, gbc);

    gbc.gridx = 3;
    gbc.gridwidth = 1;
    gbc.anchor = GridBagConstraints.CENTER;
    btnSearch = new JButton("Tìm kiếm");
    btnSearch.setFont(new Font("Arial", Font.BOLD, 14));
    btnSearch.setBackground(new Color(255, 132, 51));
    btnSearch.setMargin(new Insets(10, 20, 10, 20));
    btnSearch.setForeground(Color.BLACK);
    ImageIcon searchIcon = new ImageIcon(getClass().getResource("/images/search.png"));
    Image scaledSearchImage = searchIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
    btnSearch.setIcon(new ImageIcon(scaledSearchImage));
    btnSearch.setHorizontalTextPosition(SwingConstants.RIGHT);
    btnSearch.setIconTextGap(10);
    filterPanel.add(btnSearch, gbc);

    gbc.gridx = 4;
    btnEdit = new JButton("Chỉnh sửa");
    btnEdit.setFont(new Font("Arial", Font.BOLD, 14));
    btnEdit.setBackground(new Color(255, 132, 51));
    btnEdit.setForeground(Color.BLACK);
    btnEdit.setMargin(new Insets(10, 20, 10, 20));
    ImageIcon editIcon = new ImageIcon(getClass().getResource("/images/edit.png"));
    Image scaledEditImage = editIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
    btnEdit.setIcon(new ImageIcon(scaledEditImage));
    btnEdit.setHorizontalTextPosition(SwingConstants.RIGHT);
    btnEdit.setIconTextGap(10);
    filterPanel.add(btnEdit, gbc);

    topPanel.add(Box.createVerticalStrut(10));
    topPanel.add(filterPanel);
    topPanel.add(Box.createRigidArea(new Dimension(0, 10)));

    // Thêm label "DANH SÁCH SẢN PHẨM" vào topPanel
    lblProductList = new JLabel("DANH SÁCH SẢN PHẨM");
    lblProductList.setFont(new Font("Arial", Font.BOLD, 24));
    lblProductList.setForeground(Color.WHITE);
    lblProductList.setAlignmentX(Component.CENTER_ALIGNMENT);
    topPanel.add(lblProductList);
    topPanel.add(Box.createRigidArea(new Dimension(0, 10)));

    // bottomPanel chứa bảng sản phẩm
    bottomPanel = new JPanel(new BorderLayout());
    bottomPanel.setBackground(new Color(255, 102, 0));

    // Tạo bảng sản phẩm với DefaultTableModel
    String[] columnNames = {
        "ID", "Tên sản phẩm", "Loại", "Giá bán",
        "Đơn vị", "Tồn kho", "Mã vạch", "Nhà cung cấp", "Giá nhập"
    };
    tableModel = new DefaultTableModel(columnNames, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    productTable = new JTable(tableModel);
    productTable.setRowHeight(25);
    productTable.setFont(new Font("Arial", Font.PLAIN, 14));
    JScrollPane tableScrollPane = new JScrollPane(productTable);
    bottomPanel.add(tableScrollPane, BorderLayout.CENTER);

    // Sử dụng JSplitPane để chia topPanel (30%) và bottomPanel (70%)
    JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topPanel, bottomPanel);
    splitPane.setResizeWeight(0.2);
    splitPane.setOneTouchExpandable(true);
    splitPane.setDividerSize(8);

    // Thêm splitPane vào container và container vào ProductPanel
    container.add(splitPane, BorderLayout.CENTER);
    add(container, BorderLayout.CENTER);

    // Khởi tạo ProductPanelService và gán instance
    service = new ProductPanelService(this);

    // Gán sự kiện cho nút Sync: khi click sẽ gọi syncProject() để load lại toàn bộ dữ liệu
    btnSync.addActionListener(e -> service.syncProject());

    }

    // Các phương thức getter cho các thành phần UI
    public JTable getProductTable() {
        return productTable;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public JTextField getTxtSearch() {
        return txtSearch;
    }

    public JButton getBtnSearch() {
        return btnSearch;
    }

    public JButton getBtnEdit() {
        return btnEdit;
    }

    public JComboBox<String> getCmbCategory() {
        return cmbCategory;
    }

    public JComboBox<String> getCmbSupplier() {
        return cmbSupplier;
    }

    public ProductPanelService getService() {
        return service;
    }

    // Trả về instance của ProductPanel
    public static ProductPanel getInstance() {
        return instance;
    }
}
