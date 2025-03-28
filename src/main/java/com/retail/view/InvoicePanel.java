package com.retail.view;

import com.retail.controller.InvoiceController;
import com.retail.model.Invoice;
import com.retail.model.InvoiceDetail;
import com.retail.service.InvoiceService;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class InvoicePanel extends JPanel {
    private JTextField txtCustomerPhone;
    private JLabel lblCustomerName;
    private JLabel lblCustomerEmail;
    private JLabel lblCustomerAddress;

    private JComboBox cmbEmployee;
    private JComboBox<String> cmbPaymentMethod;
    private JTextField txtDiscount;

    private JTable detailTable;
    private DefaultTableModel detailTableModel;

    private JTextField txtBarcode;
    private JButton btnAddProduct;

    private JButton btnCreateInvoice;
    private JButton btnEditInvoice;
    private JButton btnSearchInvoiceByPhone;
    private JButton btnViewInvoiceDetails;

    private JTable invoiceListTable;
    private DefaultTableModel invoiceListTableModel;
    private JLabel lblTotalAmount;
    private JLabel lblFinalAmount;

    private InvoicePanelDialog invoicePanelDialog;

    private final Color SOFT_ORANGE = new Color(255, 163, 102);

    public InvoicePanel() {
        setLayout(new BorderLayout());

        JPanel titlePanel = new JPanel(new GridBagLayout());
        titlePanel.setBackground(new Color(255, 102, 0));
        titlePanel.setBorder(new LineBorder(Color.BLACK));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        JLabel lblTitle = new JLabel("QUẢN LÝ BÁN HÀNG");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        titlePanel.add(lblTitle, gbc);

        // Giao diện tạo hóa đơn và danh sách
        JPanel createInvoicePanel = createInvoiceCreationPanel();
        JPanel invoiceListPanel = createInvoiceListPanel();

        JPanel topContainer = new JPanel();
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.Y_AXIS));
        topContainer.setBackground(SOFT_ORANGE);
        topContainer.add(titlePanel);
        topContainer.add(Box.createVerticalStrut(5));
        topContainer.add(createInvoicePanel);

        // Tạo JSplitPane chia top (60%) và danh sách (40%)
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topContainer, invoiceListPanel);
        splitPane.setResizeWeight(0.6);
//        splitPane.setDividerLocation(0.6)
        splitPane.setDividerSize(8);
        splitPane.setOneTouchExpandable(true);
        add(splitPane, BorderLayout.CENTER);
        // ⚠️ Set lại divider sau khi layout xong
SwingUtilities.invokeLater(() -> {
    splitPane.setDividerLocation(0.6); 
});
//// Gói splitPane trong một JPanel để đảm bảo layout chính xác khi scroll
//JPanel wrapperPanel = new JPanel(new BorderLayout());
//wrapperPanel.add(splitPane, BorderLayout.CENTER);
//wrapperPanel.setPreferredSize(new Dimension(1100, 800)); // 👈 chỉnh kích thước bạn mong muốn
//
//// Đặt wrapperPanel vào JScrollPane
//JScrollPane scrollPane = new JScrollPane(wrapperPanel);
//scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
//scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
//
//// Thêm scrollPane vào chính InvoicePanel
//add(scrollPane, BorderLayout.CENTER);


        initLogic();
        new InvoiceController(new InvoiceService(), this);
        invoicePanelDialog.loadInvoiceList();
    }

    private void initLogic() {
        invoicePanelDialog = new InvoicePanelDialog(this);
    }

    private JPanel createInvoiceCreationPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // --- Thông tin hóa đơn ---
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createTitledBorder("Thông tin hóa đơn"));
        topPanel.setBackground(SOFT_ORANGE);

        // 1. phonePanel: hàng đầu chứa SĐT và nút "Lấy thông tin"
        JPanel phonePanel = new JPanel(new GridBagLayout());
        phonePanel.setBackground(SOFT_ORANGE);
        GridBagConstraints pg = new GridBagConstraints();
        pg.insets = new Insets(5, 5, 5, 5);
        pg.fill = GridBagConstraints.HORIZONTAL;
        pg.anchor = GridBagConstraints.CENTER;
        pg.gridx = 0;
        pg.gridy = 0;
        phonePanel.add(new JLabel("<html><b><span style='font-size:12px; color:white;'>SĐT Khách Hàng:</span></b></html>"), pg);
        pg.gridx = 1;
        pg.weightx = 1.0;
        txtCustomerPhone = new JTextField();
        phonePanel.add(txtCustomerPhone, pg);
        pg.gridx = 2;
        pg.weightx = 0;
        JButton btnFetchCustomer = new JButton("Lấy thông tin");
        btnFetchCustomer.setFont(new Font("Arial", Font.BOLD, 14));
        btnFetchCustomer.setBackground(new Color(255, 132, 51));
        btnFetchCustomer.setForeground(Color.BLACK);
        btnFetchCustomer.setMargin(new Insets(10, 20, 10, 20));
        ImageIcon searchIcon = new ImageIcon(getClass().getResource("/images/search.png"));
        Image scaledSearchImage = searchIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        btnFetchCustomer.setIcon(new ImageIcon(scaledSearchImage));
        btnFetchCustomer.setHorizontalTextPosition(SwingConstants.RIGHT);
        btnFetchCustomer.setIconTextGap(10);
        phonePanel.add(btnFetchCustomer, pg);
        btnFetchCustomer.addActionListener(e -> txtCustomerPhone.postActionEvent());
        topPanel.add(phonePanel, BorderLayout.NORTH);

        // 2. infoContainer: chứa 2 khối thông tin bên cạnh nhau (Thông tin KH & Thông tin khác)
        JPanel infoContainer = new JPanel();
        infoContainer.setLayout(new BoxLayout(infoContainer, BoxLayout.X_AXIS));
        infoContainer.setBackground(SOFT_ORANGE);
        
        // Left block: Thông tin KH (Tên, Email, Địa chỉ)
        JPanel customerInfoPanel = new JPanel(new GridBagLayout());
        customerInfoPanel.setBackground(SOFT_ORANGE);
        GridBagConstraints c1 = new GridBagConstraints();
        c1.insets = new Insets(5, 5, 5, 5);
        c1.fill = GridBagConstraints.HORIZONTAL;
        c1.anchor = GridBagConstraints.WEST;
        
        c1.gridx = 0;
        c1.gridy = 0;
        customerInfoPanel.add(new JLabel("<html><b><font color='white'>Tên:</font></b></html>"), c1);
        c1.gridx = 1;
        c1.weightx = 1;
        lblCustomerName = new JLabel();
        customerInfoPanel.add(lblCustomerName, c1);
        
        c1.gridx = 0;
        c1.gridy = 1;
        c1.weightx = 0;
        customerInfoPanel.add(new JLabel("<html><b><font color='white'>Email:</font></b></html>"), c1);
        c1.gridx = 1;
        c1.weightx = 1;
        lblCustomerEmail = new JLabel();
        customerInfoPanel.add(lblCustomerEmail, c1);
        
        c1.gridx = 0;
        c1.gridy = 2;
        c1.weightx = 0;
        customerInfoPanel.add(new JLabel("<html><b><font color='white'>Địa chỉ:</font></b></html>"), c1);
        c1.gridx = 1;
        c1.weightx = 1;
        lblCustomerAddress = new JLabel();
        customerInfoPanel.add(lblCustomerAddress, c1);
        
        // Right block: Thông tin khác (Nhân viên, Thanh toán, Discount)
        JPanel otherInfoPanel = new JPanel(new GridBagLayout());
        otherInfoPanel.setBackground(SOFT_ORANGE);
        GridBagConstraints c2 = new GridBagConstraints();
        c2.insets = new Insets(5, 5, 5, 5);
        c2.fill = GridBagConstraints.HORIZONTAL;
        c2.anchor = GridBagConstraints.WEST;
        
        c2.gridx = 0;
        c2.gridy = 0;
        otherInfoPanel.add(new JLabel("<html><b><font color='white'>Nhân viên:</font></b></html>"), c2);
        c2.gridx = 1;
        c2.weightx = 1;
        cmbEmployee = new JComboBox<>();
        otherInfoPanel.add(cmbEmployee, c2);
        
        c2.gridx = 0;
        c2.gridy = 1;
        c2.weightx = 0;
        otherInfoPanel.add(new JLabel("<html><b><font color='white'>Thanh toán:</font></b></html>"), c2);
        c2.gridx = 1;
        c2.weightx = 1;
        cmbPaymentMethod = new JComboBox<>(new String[]{"cash", "credit_card", "e_wallet"});
        otherInfoPanel.add(cmbPaymentMethod, c2);
        
        c2.gridx = 0;
        c2.gridy = 2;
        c2.weightx = 0;
        otherInfoPanel.add(new JLabel("<html><b><font color='white'>Discount (%):</font></b></html>"), c2);
        c2.gridx = 1;
        c2.weightx = 1;
        txtDiscount = new JTextField("0");
        otherInfoPanel.add(txtDiscount, c2);
        
        // Thêm 2 khối vào infoContainer, phân cách bởi 1 separator dọc và khoảng cách
        infoContainer.add(customerInfoPanel);
        infoContainer.add(Box.createHorizontalStrut(10));
        JSeparator verticalSep = new JSeparator(SwingConstants.VERTICAL);
        verticalSep.setPreferredSize(new Dimension(2, customerInfoPanel.getPreferredSize().height));
        infoContainer.add(verticalSep);
        infoContainer.add(Box.createHorizontalStrut(10));
        infoContainer.add(otherInfoPanel);
        
        topPanel.add(infoContainer, BorderLayout.CENTER);
        // Tạo một wrapper panel để chứa topPanel với margin 2 bên
        JPanel topPanelWrapper = new JPanel(new BorderLayout());
        topPanelWrapper.setBackground(SOFT_ORANGE);
        topPanelWrapper.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40)); // top, left, bottom, right
        topPanelWrapper.add(topPanel, BorderLayout.CENTER);

        panel.add(topPanelWrapper);

        // --- Barcode & bảng sản phẩm ---
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        JPanel barcodePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        barcodePanel.setBorder(BorderFactory.createTitledBorder("Nhập Mã Vạch SP"));
        barcodePanel.setBackground(SOFT_ORANGE);
        txtBarcode = new JTextField(20);
        btnAddProduct = new JButton("Thêm SP");
        btnAddProduct.setFont(new Font("Arial", Font.BOLD, 14));
        btnAddProduct.setBackground(new Color(255, 132, 51));
        btnAddProduct.setForeground(Color.BLACK);
        btnAddProduct.setMargin(new Insets(5, 10, 5, 10));
        ImageIcon addIcon = new ImageIcon(getClass().getResource("/images/plus.png"));
        Image scaledAddImage = addIcon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        btnAddProduct.setIcon(new ImageIcon(scaledAddImage));
        barcodePanel.add(new JLabel("<html><b><span style='font-size:12px; color:white;'>Mã vạch Sản:</span></b></html>"));
        barcodePanel.add(txtBarcode);
        barcodePanel.add(btnAddProduct);
        JPanel barcodeWrapper = new JPanel(new BorderLayout());
        barcodeWrapper.setBackground(SOFT_ORANGE);
        barcodeWrapper.setBorder(BorderFactory.createEmptyBorder(5, 20, 0, 10));
        barcodeWrapper.add(barcodePanel, BorderLayout.CENTER);

        centerPanel.add(barcodeWrapper);

        detailTableModel = new DefaultTableModel(new Object[]{"Product ID", "Tên SP", "Tồn kho", "Số lượng", "Giá", "Tổng tiền", "Delete"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }
        };
        detailTable = new JTable(detailTableModel);
        JScrollPane detailScrollPane = new JScrollPane(detailTable);
        detailScrollPane.setBorder(BorderFactory.createTitledBorder("Chi tiết hóa đơn"));
        centerPanel.add(detailScrollPane);
        panel.add(centerPanel);

        // --- Tổng tiền + nút hành động ---
        JPanel southPanel = new JPanel(new BorderLayout());
        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        lblTotalAmount = new JLabel("TỔNG TIỀN: 0");
        lblTotalAmount.setFont(new Font("Arial", Font.BOLD, 16));
        lblFinalAmount = new JLabel("THÀNH TIỀN: 0");
        lblFinalAmount.setFont(new Font("Arial", Font.BOLD, 16));

        summaryPanel.add(lblTotalAmount);
        summaryPanel.add(Box.createHorizontalStrut(20));
        summaryPanel.add(lblFinalAmount);
        southPanel.add(summaryPanel, BorderLayout.NORTH);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(SOFT_ORANGE); 

        btnCreateInvoice = new JButton("Tạo hóa đơn");
        btnCreateInvoice.setFont(new Font("Arial", Font.BOLD, 14));
        btnCreateInvoice.setBackground(new Color(255, 132, 51));
        btnCreateInvoice.setForeground(Color.BLACK);
        btnCreateInvoice.setMargin(new Insets(10, 20, 10, 20));
        ImageIcon createIcon = new ImageIcon(getClass().getResource("/images/add.png"));
        Image scaledCreateIcon = createIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        btnCreateInvoice.setIcon(new ImageIcon(scaledCreateIcon));
        btnCreateInvoice.setHorizontalTextPosition(SwingConstants.RIGHT);
        btnCreateInvoice.setIconTextGap(10);
        buttonPanel.add(btnCreateInvoice);

        btnEditInvoice = new JButton("Sửa hóa đơn");
        btnEditInvoice.setFont(new Font("Arial", Font.BOLD, 14));
        btnEditInvoice.setBackground(new Color(255, 132, 51));
        btnEditInvoice.setForeground(Color.BLACK);
        btnEditInvoice.setMargin(new Insets(10, 20, 10, 20));
        ImageIcon editInvoiceIcon = new ImageIcon(getClass().getResource("/images/edit.png"));
        Image scaledEditInvoiceIcon = editInvoiceIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        btnEditInvoice.setIcon(new ImageIcon(scaledEditInvoiceIcon));
        btnEditInvoice.setHorizontalTextPosition(SwingConstants.RIGHT);
        btnEditInvoice.setIconTextGap(10);
        buttonPanel.add(btnEditInvoice);

        btnSearchInvoiceByPhone = new JButton("Tìm hóa đơn theo SĐT");
        btnSearchInvoiceByPhone.setFont(new Font("Arial", Font.BOLD, 14));
        btnSearchInvoiceByPhone.setBackground(new Color(255, 132, 51));
        btnSearchInvoiceByPhone.setForeground(Color.BLACK);
        btnSearchInvoiceByPhone.setMargin(new Insets(10, 20, 10, 20));
        ImageIcon searchPhoneIcon = new ImageIcon(getClass().getResource("/images/search.png"));
        Image scaledSearchPhoneIcon = searchPhoneIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        btnSearchInvoiceByPhone.setIcon(new ImageIcon(scaledSearchPhoneIcon));
        btnSearchInvoiceByPhone.setHorizontalTextPosition(SwingConstants.RIGHT);
        btnSearchInvoiceByPhone.setIconTextGap(10);
        buttonPanel.add(btnSearchInvoiceByPhone);

        btnViewInvoiceDetails = new JButton("Xem chi tiết hóa đơn");
        btnViewInvoiceDetails.setFont(new Font("Arial", Font.BOLD, 14));
        btnViewInvoiceDetails.setBackground(new Color(255, 132, 51));
        btnViewInvoiceDetails.setForeground(Color.BLACK);
        btnViewInvoiceDetails.setMargin(new Insets(10, 20, 10, 10));
        ImageIcon viewDetailsIcon = new ImageIcon(getClass().getResource("/images/visual.png"));
        Image scaledViewDetailsIcon = viewDetailsIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        btnViewInvoiceDetails.setIcon(new ImageIcon(scaledViewDetailsIcon));
        btnViewInvoiceDetails.setHorizontalTextPosition(SwingConstants.RIGHT);
        btnViewInvoiceDetails.setIconTextGap(10);
        buttonPanel.add(btnViewInvoiceDetails);

        southPanel.add(buttonPanel, BorderLayout.SOUTH);
        panel.add(southPanel, BorderLayout.PAGE_END);

        return panel;
    }

    private JPanel createInvoiceListPanel() {
        String[] columnNames = {"Invoice ID", "Khách ", "Nhân viên", "Tổng tiền", "Discount", "Thành tiền", "Phương thức", "Ngày tạo"};
        invoiceListTableModel = new DefaultTableModel(columnNames, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        invoiceListTable = new JTable(invoiceListTableModel);
        JScrollPane scrollPane = new JScrollPane(invoiceListTable);
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Danh sách hóa đơn"));
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    // --- Getter ---
    public JButton getSubmitButton() { return btnCreateInvoice; }
    public Invoice getInvoiceData() { return invoicePanelDialog.getInvoiceData(); }
    public List<InvoiceDetail> getInvoiceDetails() { return invoicePanelDialog.getInvoiceDetails(); }
    public void showMessage(String message) { invoicePanelDialog.showMessage(message); }
    public void resetForm() { invoicePanelDialog.resetForm(); }
    public void refreshInvoiceList() { invoicePanelDialog.loadInvoiceList(); }

    public JTextField getTxtCustomerPhone() { return txtCustomerPhone; }
    public JLabel getLblCustomerName() { return lblCustomerName; }
    public JLabel getLblCustomerEmail() { return lblCustomerEmail; }
    public JLabel getLblCustomerAddress() { return lblCustomerAddress; }
    public JComboBox getCmbEmployee() { return cmbEmployee; }
    public JComboBox<String> getCmbPaymentMethod() { return cmbPaymentMethod; }
    public JTextField getTxtDiscount() { return txtDiscount; }
    public JTable getDetailTable() { return detailTable; }
    public DefaultTableModel getDetailTableModel() { return detailTableModel; }
    public JTextField getTxtBarcode() { return txtBarcode; }
    public JButton getBtnAddProduct() { return btnAddProduct; }
    public JButton getBtnCreateInvoice() { return btnCreateInvoice; }
    public JButton getBtnEditInvoice() { return btnEditInvoice; }
    public JButton getBtnSearchInvoiceByPhone() { return btnSearchInvoiceByPhone; }
    public JButton getBtnViewInvoiceDetails() { return btnViewInvoiceDetails; }
    public JTable getInvoiceListTable() { return invoiceListTable; }
    public DefaultTableModel getInvoiceListTableModel() { return invoiceListTableModel; }
    public JLabel getLblTotalAmount() { return lblTotalAmount; }
    public JLabel getLblFinalAmount() { return lblFinalAmount; }
}
