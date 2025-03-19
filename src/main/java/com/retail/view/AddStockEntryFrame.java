/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.retail.view;

import com.retail.controller.ProductController;
import com.retail.controller.StockEntryController;
import com.retail.controller.StockEntryDetailController;
import com.retail.controller.SupplierController;
import com.retail.model.ComboBoxItem;
import com.retail.model.Product;
import com.retail.model.StockEntry;
import com.retail.model.StockEntryDetail;
import com.retail.model.Supplier;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.UnsupportedEncodingException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import net.sf.jasperreports.engine.JRException;

/**
 *
 * @author Admin
 */
public class AddStockEntryFrame extends javax.swing.JFrame {
    
     private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=GroceryStoreDB;encrypt=true;trustServerCertificate=true";
    private static final String USER = "bookoff";
    private static final String PASSWORD = "123456789";

    private StockEntryManagement parentFrame; // Tham chiếu đến StockEntryManagement

    private ProductController productController;
    private int supplierId; // Thêm biến supplierId
    StockEntryController stockEntryController;
    StockEntryDetailController stockEntryDetailController;
    private SupplierController supplierController;
    private List<ComboBoxItem> productItems;
    private JList<ComboBoxItem> suggestionList;

    private List<StockEntryDetail> tempStockEntryDetails; // Danh sách tạm thời các chi tiết nhập hàng
    private StockEntry currentStockEntry; // Hóa đơn nhập hàng hiện tại

    /**
     * Creates new form AddStockEntryFrame
     *
     * @param parentFrame
     * @param supplierId
     */
    public AddStockEntryFrame(StockEntryManagement parentFrame, int supplierId) {
        
        this.parentFrame = parentFrame;
        this.supplierId = supplierId;
        // Đặt frame vào giữa màn hình
        // Đặt kích thước cố định nếu chưa có
        setSize(800, 600); // Hoặc bất kỳ kích thước phù hợp

        // Căn giữa chính xác màn hình
        setLocation(
                (Toolkit.getDefaultToolkit().getScreenSize().width - getWidth()) / 2,
                (Toolkit.getDefaultToolkit().getScreenSize().height - getHeight()) / 2
        );
        initComponents();
        productController = new ProductController();
        supplierController = new SupplierController();
        stockEntryController = new StockEntryController();
        stockEntryDetailController = new StockEntryDetailController();

        // Khởi tạo suggestionList
        suggestionList = new JList<>();
        suggestionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Khởi tạo danh sách tạm thời
        tempStockEntryDetails = new ArrayList<>();
        currentStockEntry = null;

        loadProductsIntoAutoComplete();
        setupAutoCompleteListener();

    }

    private void loadProductsIntoAutoComplete() {
        List<Product> products = productController.getProductsBySupplierId(supplierId);
        productItems = new ArrayList<>();

        for (Product product : products) {
            productItems.add(new ComboBoxItem(product.getProductId(), product.getName()));
        }

        System.out.println("✅ Danh sách sản phẩm: " + productItems);

    }

    private void setupAutoCompleteListener() {
        productNameTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String text = productNameTextField.getText().trim();

                // Nếu người dùng xóa tên sản phẩm
                if (text.isEmpty()) {
                    resetProductFields(); // Đặt lại các trường về giá trị mặc định
                    jPopupMenu1.setVisible(false);
                    return;
                }

                // Nếu người dùng nhấn Enter và chọn sản phẩm từ danh sách gợi ý
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    ComboBoxItem selectedValue = suggestionList.getSelectedValue();
                    if (selectedValue != null) {
                        productNameTextField.setText(selectedValue.getName());
                        jPopupMenu1.setVisible(false);
                        autoFillProductFields(selectedValue.getId()); // Điền thông tin sản phẩm
                    }
                }

                // Lọc danh sách gợi ý
                List<ComboBoxItem> filtered = productItems.stream()
                        .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase()))
                        .collect(Collectors.toList());

                if (filtered.isEmpty()) {
                    resetProductFields(); // Đặt lại các trường về giá trị mặc định
                    jPopupMenu1.setVisible(false);
                    return;
                }

                showSuggestionPopup(filtered);
            }
        });

        suggestionList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ComboBoxItem selectedValue = suggestionList.getSelectedValue();
                if (selectedValue != null) {
                    productNameTextField.setText(selectedValue.getName());
                    System.out.println("🛒 Sản phẩm đã chọn: ID = " + selectedValue.getId() + ", Name = " + selectedValue.getName());
                    // Tự động điền thông tin sản phẩm khi chọn từ danh sách gợi ý
                    autoFillProductFields(selectedValue.getId());
                }
                jPopupMenu1.setVisible(false);
                SwingUtilities.invokeLater(() -> suggestionList.requestFocusInWindow());
            }
        });

        suggestionList.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    ComboBoxItem selectedValue = suggestionList.getSelectedValue();
                    if (selectedValue != null) {
                        productNameTextField.setText(selectedValue.getName());
                        jPopupMenu1.setVisible(false);
                        // Tự động điền thông tin sản phẩm khi chọn từ danh sách gợi ý
                        autoFillProductFields(selectedValue.getId());
                    }
                }
            }
        });
    }

    private void autoFillProductFields(int productId) {
        Product product = productController.getProductById(productId);
        if (product != null) {
            // Điền thông tin sản phẩm vào các trường
            barcodeTextField.setText(product.getBarcode());
            unitComboBox.setSelectedItem(product.getUnit());
            categoryComboBox.setSelectedItem(product.getCategory());
            priceTextField.setText(String.valueOf(product.getPurchasePrice()));
        } else {
            // Nếu không tìm thấy sản phẩm, xóa các trường
            barcodeTextField.setText("");
            unitComboBox.setSelectedIndex(0);
            categoryComboBox.setSelectedIndex(0);
            priceTextField.setText("");
        }
    }

    private void resetProductFields() {
        barcodeTextField.setText(""); // Đặt lại mã vạch
        unitComboBox.setSelectedIndex(0); // Đặt lại đơn vị về giá trị đầu tiên trong combobox
        categoryComboBox.setSelectedIndex(0); // Đặt lại danh mục về giá trị đầu tiên trong combobox
        priceTextField.setText(""); // Đặt lại giá nhập
    }

    private void showSuggestionPopup(List<ComboBoxItem> suggestions) {
        jPopupMenu1.removeAll();

        if (suggestions.isEmpty()) {
            jPopupMenu1.setVisible(false);
            return;
        }

        // Cập nhật danh sách hiển thị
        suggestionList.setListData(suggestions.toArray(new ComboBoxItem[0]));
        suggestionList.setSelectedIndex(0);

        // Tạo ScrollPane chứa danh sách gợi ý
        JScrollPane scrollPane = new JScrollPane(suggestionList);

        // Điều chỉnh kích thước PopupMenu
        int rowHeight = suggestionList.getFixedCellHeight() > 0 ? suggestionList.getFixedCellHeight() : 20; // Chiều cao mỗi dòng (mặc định 20 nếu chưa có)
        int maxVisibleRows = 6; // Giới hạn số dòng tối đa hiển thị
        int popupHeight = Math.min(suggestions.size(), maxVisibleRows) * rowHeight + 10; // +10 để tránh cắt mép

        scrollPane.setPreferredSize(new Dimension(productNameTextField.getWidth(), popupHeight));

        jPopupMenu1.add(scrollPane);
        jPopupMenu1.show(productNameTextField, 0, productNameTextField.getHeight() + 5);
        // Trả lại focus ngay lập tức để tiếp tục nhập mà không cần click lại
//            SwingUtilities.invokeLater(() -> productNameTextField.requestFocusInWindow());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel5 = new javax.swing.JLabel();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        productNameTextField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        priceTextField = new javax.swing.JTextField();
        addStockEntryBtn = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cancelBtn = new javax.swing.JButton();
        quantityTextField = new javax.swing.JTextField();
        saveStockEntryBtn = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        categoryComboBox = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        unitComboBox = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        barcodeTextField = new javax.swing.JTextField();

        jLabel5.setText("jLabel5");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 153, 51));
        jLabel2.setText("Sản phẩm");

        productNameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                productNameTextFieldActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 153, 51));
        jLabel4.setText("Số lượng");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 153, 51));
        jLabel6.setText("Giá");

        priceTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                priceTextFieldActionPerformed(evt);
            }
        });

        addStockEntryBtn.setBackground(new java.awt.Color(0, 153, 0));
        addStockEntryBtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        addStockEntryBtn.setForeground(new java.awt.Color(255, 255, 255));
        addStockEntryBtn.setText("+ Thêm");
        addStockEntryBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addStockEntryBtnActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(0, 153, 51));
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Thông tin nhập kho");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(150, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(111, 111, 111))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel1)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        cancelBtn.setBackground(new java.awt.Color(204, 204, 204));
        cancelBtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cancelBtn.setText("Thoát");
        cancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBtnActionPerformed(evt);
            }
        });

        saveStockEntryBtn.setBackground(new java.awt.Color(204, 0, 51));
        saveStockEntryBtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        saveStockEntryBtn.setForeground(new java.awt.Color(255, 255, 255));
        saveStockEntryBtn.setText("Hoàn tất");
        saveStockEntryBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveStockEntryBtnActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 153, 51));
        jLabel7.setText("Loại");

        categoryComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Đồ Uống", "Bánh Kẹo", "Chăm Sóc Nhà Cửa", "Chăm Sóc Cá Nhân", "Gia Vị", "Thực Phẩm Đóng Hộp", "Thực Phẩm", "Gạo", "Mì Ăn Liền" }));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 153, 51));
        jLabel8.setText("Đơn vị");

        unitComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Kg", "Gói", "Chai", "Hộp", "Lon", "Tuýp" }));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 153, 51));
        jLabel10.setText("Mã vạch");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(cancelBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(addStockEntryBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39)
                .addComponent(saveStockEntryBtn)
                .addGap(27, 27, 27))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(89, 89, 89)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(27, 27, 27)
                        .addComponent(barcodeTextField)
                        .addGap(71, 71, 71))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel2))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(priceTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 338, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(quantityTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(unitComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(categoryComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(productNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 338, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addContainerGap(65, Short.MAX_VALUE))))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(112, 112, 112)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(productNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(categoryComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(unitComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(quantityTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(barcodeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(priceTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(39, 39, 39)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addStockEntryBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(saveStockEntryBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(59, 59, 59))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(467, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addStockEntryBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addStockEntryBtnActionPerformed

        String productName = productNameTextField.getText().trim();
        String barcode = barcodeTextField.getText().trim();
        String unit = (String) unitComboBox.getSelectedItem();
        String category = (String) categoryComboBox.getSelectedItem();
        int quantity;
        double purchasePrice;

        try {
            quantity = Integer.parseInt(quantityTextField.getText());
            purchasePrice = Double.parseDouble(priceTextField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số lượng hoặc giá nhập không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Tìm sản phẩm trong danh sách gợi ý
        ComboBoxItem selectedProduct = productItems.stream()
                .filter(item -> item.getName().equalsIgnoreCase(productName))
                .findFirst()
                .orElse(null);

        int productId;
        if (selectedProduct == null) {
            // Nếu sản phẩm chưa tồn tại, thêm sản phẩm mới vào bảng Product
            double price = purchasePrice * 1.2; // Tính giá bán với lợi nhuận 20%
            productId = productController.addProductWithStockEntry(productName, supplierId, unit, category, barcode, purchasePrice, price);
            if (productId == -1) {
                JOptionPane.showMessageDialog(this, "Lỗi khi thêm sản phẩm mới!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Thêm sản phẩm mới vào danh sách productItems
            ComboBoxItem newProduct = new ComboBoxItem(productId, productName);
            productItems.add(newProduct);

            // Cập nhật danh sách gợi ý
            loadProductsIntoAutoComplete();
        } else {
            // Nếu sản phẩm đã tồn tại, lấy productId
            productId = selectedProduct.getId();
        }

        // Tạo một StockEntryDetail mới
        StockEntryDetail detail = new StockEntryDetail();
        detail.setProductId(productId);
        detail.setQuantity(quantity);
        detail.setPurchasePrice(purchasePrice);

        // Thêm vào danh sách tạm thời
        tempStockEntryDetails.add(detail);

        // Xóa các trường đã điền để nhập tiếp
        productNameTextField.setText("");
        barcodeTextField.setText("");
        quantityTextField.setText("");
        priceTextField.setText("");

        JOptionPane.showMessageDialog(this, "Đã thêm sản phẩm vào danh sách nhập hàng!");

    }//GEN-LAST:event_addStockEntryBtnActionPerformed

    private void productNameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_productNameTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_productNameTextFieldActionPerformed

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_cancelBtnActionPerformed

    private void saveStockEntryBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveStockEntryBtnActionPerformed

        if (tempStockEntryDetails.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Danh sách nhập hàng trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int employeeId = 1; // Giả sử nhân viên có ID = 1

        StockEntry stockEntry = new StockEntry();
        stockEntry.setSupplierId(supplierId); // Sử dụng supplierId đã truyền
        stockEntry.setEmployeeId(employeeId);

        try {
            // Thêm StockEntry và lấy stock_entry_id vừa tạo
            int stockEntryId = stockEntryController.addStockEntry(stockEntry);

            // Thêm các chi tiết nhập hàng (StockEntryDetail)
            for (StockEntryDetail detail : tempStockEntryDetails) {
                detail.setStockEntryId(stockEntryId);
                stockEntryDetailController.addStockEntryDetail(detail);
            }

            

            JOptionPane.showMessageDialog(this, "Nhập hàng thành công!");
            if (parentFrame != null) {
                parentFrame.loadStockEntryData(); // Cập nhật lại dữ liệu trên giao diện chính
            }
            this.dispose(); // Đóng cửa sổ hiện tại
            
            // Xuất báo cáo PDF sau khi thêm thành công
            generateAndOpenStockEntryReport(stockEntryId);
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi nhập hàng: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }


    }//GEN-LAST:event_saveStockEntryBtnActionPerformed

    private void priceTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_priceTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_priceTextFieldActionPerformed

    private void generateAndOpenStockEntryReport(int stockEntryId) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            // Gọi stored procedure để lấy thông tin chi tiết
            CallableStatement cstmt = connection.prepareCall("{call sp_GetStockEntryDetails(?)}");
            cstmt.setInt(1, stockEntryId);

            // Thực thi stored procedure
            boolean hasResults = cstmt.execute();

            // Xử lý ResultSet đầu tiên (thông tin chung của StockEntry)
            if (hasResults) {
                ResultSet rsGeneral = cstmt.getResultSet();
                String entryDate = null;
                String supplierName = null;
                String employeeName = null;
                String totalPrice = null;

                if (rsGeneral.next()) {
                    entryDate = rsGeneral.getString("entry_date");
                    supplierName = rsGeneral.getString("supplier_name");
                    employeeName = rsGeneral.getString("employee_name");
                    double totalPriceDouble = rsGeneral.getDouble("total_price");
                    int totalPriceInt = (int) totalPriceDouble; // Bỏ phần thập phân
                    totalPrice = String.valueOf(totalPriceInt);
                }

                // Xử lý ResultSet thứ hai (thông tin chi tiết của StockEntryDetail)
                if (cstmt.getMoreResults()) {
                    ResultSet rsDetails = cstmt.getResultSet();

                    // Tạo báo cáo PDF từ cả hai ResultSet
                    StockEntryReportGenerator reportGenerator = new StockEntryReportGenerator();
                    reportGenerator.generateReport(entryDate, supplierName, employeeName, totalPrice, rsDetails, stockEntryId);

                }
            }
        } catch (SQLException | JRException e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi tạo báo cáo: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
    }

    public static void main(String[] args) {
        try {
            System.setOut(new java.io.PrintStream(System.out, true, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            System.out.println("Lỗi khi đặt encoding UTF-8!");
        }

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addStockEntryBtn;
    private javax.swing.JTextField barcodeTextField;
    private javax.swing.JButton cancelBtn;
    private javax.swing.JComboBox<String> categoryComboBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JTextField priceTextField;
    private javax.swing.JTextField productNameTextField;
    private javax.swing.JTextField quantityTextField;
    private javax.swing.JButton saveStockEntryBtn;
    private javax.swing.JComboBox<String> unitComboBox;
    // End of variables declaration//GEN-END:variables
}
