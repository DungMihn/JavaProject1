/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Market;

import java.awt.CardLayout;
import java.awt.Color;
import java.io.UnsupportedEncodingException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 *
 * @author ADMIN
 */
public class Menu extends javax.swing.JFrame {

    
    private CardLayout cardLayout;
    private BackgroundPanel backgroundpanel;
    private EmployeePanel employeepanel;
    private CustomerPanel customerpanel;
    private InvoicePanel invoicepanel;
    private ProductPanel productpanel;
    private ReportPanel reportpanel;
    private SystemPanel systempanel;
    private String userRole;
    private ChangepassPanel changepassPanel;
     private String loggedInUser; // Lưu username của user đang đăng nhập
     

    
    
    /**
     * Creates new form Menu
     */
    public Menu(String role, String username) {
        this.loggedInUser = username;
        this.userRole = role;
        initComponents();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        // Khởi tạo các panel
        backgroundpanel = new BackgroundPanel();
        employeepanel = new EmployeePanel();
        customerpanel = new CustomerPanel();
        invoicepanel = new InvoicePanel();
        productpanel = new ProductPanel();
        reportpanel = new ReportPanel();
        systempanel = new SystemPanel(this);
        changepassPanel = new ChangepassPanel(loggedInUser);
 


        // Thiết lập CardLayout
        cardLayout = new CardLayout();
        ContentPanel.setLayout(cardLayout);

        // Thêm các panel vào contentPanel
        ContentPanel.add(backgroundpanel,"Background");
        ContentPanel.add(customerpanel,"Customer");
        ContentPanel.add(employeepanel, "Employee");
        ContentPanel.add(invoicepanel, "Invoice");
        ContentPanel.add(productpanel, "Product");
        ContentPanel.add(reportpanel, "Report");
        ContentPanel.add(systempanel, "System");
        ContentPanel.add(changepassPanel, "Changepass");



        // Hiển thị màn hình mặc định
        cardLayout.show(ContentPanel, "Background");

        // Thêm sự kiện và hiệu ứng

    }

  public void showChangePasswordPanel() {
    cardLayout.show(ContentPanel, "Changepass");
}

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        SideBarPanel = new javax.swing.JPanel();
        SupplierPage = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        SupplierButton = new javax.swing.JButton();
        ImportPage = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        ImportButton = new javax.swing.JButton();
        ProductPage = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        ProductButton = new javax.swing.JButton();
        CustomerPage = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        CustomerButton = new javax.swing.JButton();
        EmployeePage = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        EmployeeButton = new javax.swing.JButton();
        InvoicePage = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        InvoiceButton = new javax.swing.JButton();
        SystemPage = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        SystemButton = new javax.swing.JButton();
        ReportPage = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        ReportButton = new javax.swing.JButton();
        Logout = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        LogoutButton = new javax.swing.JButton();
        ContentPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1440, 900));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.setPreferredSize(new java.awt.Dimension(1440, 900));

        SideBarPanel.setBackground(new java.awt.Color(255, 102, 0));
        SideBarPanel.setPreferredSize(new java.awt.Dimension(200, 900));

        SupplierPage.setBackground(new java.awt.Color(255, 255, 255));
        SupplierPage.setPreferredSize(new java.awt.Dimension(120, 80));

        jLabel12.setBackground(new java.awt.Color(255, 255, 255));
        jLabel12.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("Nhà CC");

        SupplierButton.setBackground(new java.awt.Color(255, 193, 153));
        SupplierButton.setIcon(new javax.swing.ImageIcon("D:\\LongNg(Aptec)\\JAVA_B189_196\\JavaProject1\\src\\main\\java\\Icon\\supplier.png")); // NOI18N
        SupplierButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                SupplierButtonMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout SupplierPageLayout = new javax.swing.GroupLayout(SupplierPage);
        SupplierPage.setLayout(SupplierPageLayout);
        SupplierPageLayout.setHorizontalGroup(
            SupplierPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
            .addComponent(SupplierButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        SupplierPageLayout.setVerticalGroup(
            SupplierPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SupplierPageLayout.createSequentialGroup()
                .addComponent(SupplierButton, javax.swing.GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel12))
        );

        ImportPage.setBackground(new java.awt.Color(255, 255, 255));
        ImportPage.setPreferredSize(new java.awt.Dimension(120, 80));

        jLabel13.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("QL Nhập Kho");

        ImportButton.setBackground(new java.awt.Color(255, 193, 153));
        ImportButton.setIcon(new javax.swing.ImageIcon("D:\\LongNg(Aptec)\\JAVA_B189_196\\JavaProject1\\src\\main\\java\\Icon\\import.png")); // NOI18N
        ImportButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ImportButtonMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout ImportPageLayout = new javax.swing.GroupLayout(ImportPage);
        ImportPage.setLayout(ImportPageLayout);
        ImportPageLayout.setHorizontalGroup(
            ImportPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
            .addComponent(ImportButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        ImportPageLayout.setVerticalGroup(
            ImportPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ImportPageLayout.createSequentialGroup()
                .addComponent(ImportButton, javax.swing.GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel13))
        );

        ProductPage.setBackground(new java.awt.Color(255, 255, 255));
        ProductPage.setPreferredSize(new java.awt.Dimension(120, 80));

        jLabel14.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("Sản Phẩm");

        ProductButton.setBackground(new java.awt.Color(255, 193, 153));
        ProductButton.setIcon(new javax.swing.ImageIcon("D:\\LongNg(Aptec)\\JAVA_B189_196\\JavaProject1\\src\\main\\java\\Icon\\products.png")); // NOI18N
        ProductButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ProductButtonMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout ProductPageLayout = new javax.swing.GroupLayout(ProductPage);
        ProductPage.setLayout(ProductPageLayout);
        ProductPageLayout.setHorizontalGroup(
            ProductPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
            .addComponent(ProductButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        ProductPageLayout.setVerticalGroup(
            ProductPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ProductPageLayout.createSequentialGroup()
                .addComponent(ProductButton, javax.swing.GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14))
        );

        CustomerPage.setBackground(new java.awt.Color(255, 255, 255));
        CustomerPage.setPreferredSize(new java.awt.Dimension(120, 80));

        jLabel16.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("Khách Hàng");

        CustomerButton.setBackground(new java.awt.Color(255, 193, 153));
        CustomerButton.setIcon(new javax.swing.ImageIcon("D:\\LongNg(Aptec)\\JAVA_B189_196\\JavaProject1\\src\\main\\java\\Icon\\service.png")); // NOI18N
        CustomerButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CustomerButtonMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout CustomerPageLayout = new javax.swing.GroupLayout(CustomerPage);
        CustomerPage.setLayout(CustomerPageLayout);
        CustomerPageLayout.setHorizontalGroup(
            CustomerPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
            .addComponent(CustomerButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        CustomerPageLayout.setVerticalGroup(
            CustomerPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CustomerPageLayout.createSequentialGroup()
                .addComponent(CustomerButton, javax.swing.GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel16))
        );

        EmployeePage.setBackground(new java.awt.Color(255, 255, 255));
        EmployeePage.setPreferredSize(new java.awt.Dimension(120, 80));

        jLabel17.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("Nhân Viên");

        EmployeeButton.setBackground(new java.awt.Color(255, 193, 153));
        EmployeeButton.setIcon(new javax.swing.ImageIcon("D:\\LongNg(Aptec)\\JAVA_B189_196\\JavaProject1\\src\\main\\java\\Icon\\division.png")); // NOI18N
        EmployeeButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                EmployeeButtonMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout EmployeePageLayout = new javax.swing.GroupLayout(EmployeePage);
        EmployeePage.setLayout(EmployeePageLayout);
        EmployeePageLayout.setHorizontalGroup(
            EmployeePageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
            .addComponent(EmployeeButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        EmployeePageLayout.setVerticalGroup(
            EmployeePageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(EmployeePageLayout.createSequentialGroup()
                .addComponent(EmployeeButton, javax.swing.GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel17))
        );

        InvoicePage.setBackground(new java.awt.Color(255, 255, 255));
        InvoicePage.setPreferredSize(new java.awt.Dimension(120, 80));

        jLabel15.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("Hoá Đơn");

        InvoiceButton.setBackground(new java.awt.Color(255, 193, 153));
        InvoiceButton.setIcon(new javax.swing.ImageIcon("D:\\LongNg(Aptec)\\JAVA_B189_196\\JavaProject1\\src\\main\\java\\Icon\\invoice.png")); // NOI18N
        InvoiceButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                InvoiceButtonMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout InvoicePageLayout = new javax.swing.GroupLayout(InvoicePage);
        InvoicePage.setLayout(InvoicePageLayout);
        InvoicePageLayout.setHorizontalGroup(
            InvoicePageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
            .addComponent(InvoiceButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        InvoicePageLayout.setVerticalGroup(
            InvoicePageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, InvoicePageLayout.createSequentialGroup()
                .addComponent(InvoiceButton, javax.swing.GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        SystemPage.setBackground(new java.awt.Color(255, 255, 255));
        SystemPage.setForeground(new java.awt.Color(255, 255, 255));
        SystemPage.setPreferredSize(new java.awt.Dimension(120, 80));

        jLabel19.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("Hệ Thống");

        SystemButton.setBackground(new java.awt.Color(255, 193, 153));
        SystemButton.setIcon(new javax.swing.ImageIcon("D:\\LongNg(Aptec)\\JAVA_B189_196\\JavaProject1\\src\\main\\java\\Icon\\system-integration.png")); // NOI18N
        SystemButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                SystemButtonMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout SystemPageLayout = new javax.swing.GroupLayout(SystemPage);
        SystemPage.setLayout(SystemPageLayout);
        SystemPageLayout.setHorizontalGroup(
            SystemPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
            .addComponent(SystemButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        SystemPageLayout.setVerticalGroup(
            SystemPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, SystemPageLayout.createSequentialGroup()
                .addComponent(SystemButton, javax.swing.GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel19))
        );

        ReportPage.setBackground(new java.awt.Color(255, 255, 255));
        ReportPage.setPreferredSize(new java.awt.Dimension(120, 80));

        jLabel18.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("Báo Cáo");

        ReportButton.setBackground(new java.awt.Color(255, 193, 153));
        ReportButton.setIcon(new javax.swing.ImageIcon("D:\\LongNg(Aptec)\\JAVA_B189_196\\JavaProject1\\src\\main\\java\\Icon\\report.png")); // NOI18N
        ReportButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ReportButtonMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout ReportPageLayout = new javax.swing.GroupLayout(ReportPage);
        ReportPage.setLayout(ReportPageLayout);
        ReportPageLayout.setHorizontalGroup(
            ReportPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
            .addComponent(ReportButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        ReportPageLayout.setVerticalGroup(
            ReportPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ReportPageLayout.createSequentialGroup()
                .addComponent(ReportButton, javax.swing.GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel18))
        );

        Logout.setBackground(new java.awt.Color(255, 255, 255));
        Logout.setPreferredSize(new java.awt.Dimension(120, 80));

        jLabel20.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setText("Thoát");

        LogoutButton.setBackground(new java.awt.Color(255, 193, 153));
        LogoutButton.setIcon(new javax.swing.ImageIcon("D:\\LongNg(Aptec)\\JAVA_B189_196\\JavaProject1\\src\\main\\java\\Icon\\logout.png")); // NOI18N
        LogoutButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                LogoutButtonMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout LogoutLayout = new javax.swing.GroupLayout(Logout);
        Logout.setLayout(LogoutLayout);
        LogoutLayout.setHorizontalGroup(
            LogoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
            .addComponent(LogoutButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        LogoutLayout.setVerticalGroup(
            LogoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, LogoutLayout.createSequentialGroup()
                .addComponent(LogoutButton, javax.swing.GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel20))
        );

        javax.swing.GroupLayout SideBarPanelLayout = new javax.swing.GroupLayout(SideBarPanel);
        SideBarPanel.setLayout(SideBarPanelLayout);
        SideBarPanelLayout.setHorizontalGroup(
            SideBarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SideBarPanelLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(SideBarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(Logout, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(SystemPage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(InvoicePage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(EmployeePage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(CustomerPage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ProductPage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ImportPage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(SupplierPage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ReportPage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(40, Short.MAX_VALUE))
        );
        SideBarPanelLayout.setVerticalGroup(
            SideBarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SideBarPanelLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(SupplierPage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16)
                .addComponent(ImportPage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16)
                .addComponent(ProductPage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16)
                .addComponent(CustomerPage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16)
                .addComponent(EmployeePage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16)
                .addComponent(InvoicePage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16)
                .addComponent(SystemPage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(ReportPage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Logout, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        ContentPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        ContentPanel.setPreferredSize(new java.awt.Dimension(1232, 900));

        javax.swing.GroupLayout ContentPanelLayout = new javax.swing.GroupLayout(ContentPanel);
        ContentPanel.setLayout(ContentPanelLayout);
        ContentPanelLayout.setHorizontalGroup(
            ContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1230, Short.MAX_VALUE)
        );
        ContentPanelLayout.setVerticalGroup(
            ContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(SideBarPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ContentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(SideBarPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 962, Short.MAX_VALUE)
            .addComponent(ContentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 962, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 964, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void SupplierButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SupplierButtonMouseClicked
       
    }//GEN-LAST:event_SupplierButtonMouseClicked

    private void ImportButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ImportButtonMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_ImportButtonMouseClicked

    private void ProductButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ProductButtonMouseClicked
        cardLayout.show(ContentPanel, "Product");
    }//GEN-LAST:event_ProductButtonMouseClicked

    private void CustomerButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CustomerButtonMouseClicked
        cardLayout.show(ContentPanel, "Customer");
    }//GEN-LAST:event_CustomerButtonMouseClicked

    private void EmployeeButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_EmployeeButtonMouseClicked
         if ("manager".equalsIgnoreCase(userRole)) {
            cardLayout.show(ContentPanel, "Employee");
        } else {
            JOptionPane.showMessageDialog(this, "Bạn không có quyền truy cập vào Nhân Viên!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_EmployeeButtonMouseClicked

    private void InvoiceButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_InvoiceButtonMouseClicked
        if ("manager".equalsIgnoreCase(userRole) || "cashier".equalsIgnoreCase(userRole)) {
            cardLayout.show(ContentPanel, "Invoice");
        } else {
            JOptionPane.showMessageDialog(this, "Bạn không có quyền truy cập vào Hoá Đơn!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_InvoiceButtonMouseClicked

    private void SystemButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SystemButtonMouseClicked
        cardLayout.show(ContentPanel, "System");
    }//GEN-LAST:event_SystemButtonMouseClicked

    private void ReportButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ReportButtonMouseClicked
        if ("manager".equalsIgnoreCase(userRole) || "cashier".equalsIgnoreCase(userRole)) {
            cardLayout.show(ContentPanel, "Report");
        } else {
            JOptionPane.showMessageDialog(this, "Bạn không có quyền truy cập vào Báo Cáo!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_ReportButtonMouseClicked

    private void LogoutButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LogoutButtonMouseClicked
         int confirm = JOptionPane.showConfirmDialog(this, 
        "Bạn có chắc chắn muốn thoát không?", 
        "Xác nhận đăng xuất", 
        JOptionPane.YES_NO_OPTION);

    if (confirm == JOptionPane.YES_OPTION) {
        System.exit(0); // Thoát hoàn toàn chương trình
    }
    }//GEN-LAST:event_LogoutButtonMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Login().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel ContentPanel;
    private javax.swing.JButton CustomerButton;
    private javax.swing.JPanel CustomerPage;
    private javax.swing.JButton EmployeeButton;
    private javax.swing.JPanel EmployeePage;
    private javax.swing.JButton ImportButton;
    private javax.swing.JPanel ImportPage;
    private javax.swing.JButton InvoiceButton;
    private javax.swing.JPanel InvoicePage;
    private javax.swing.JPanel Logout;
    private javax.swing.JButton LogoutButton;
    private javax.swing.JButton ProductButton;
    private javax.swing.JPanel ProductPage;
    private javax.swing.JButton ReportButton;
    private javax.swing.JPanel ReportPage;
    private javax.swing.JPanel SideBarPanel;
    private javax.swing.JButton SupplierButton;
    private javax.swing.JPanel SupplierPage;
    private javax.swing.JButton SystemButton;
    private javax.swing.JPanel SystemPage;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables
}
