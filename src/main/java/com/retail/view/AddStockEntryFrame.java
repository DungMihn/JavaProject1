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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

/**
 *
 * @author Admin
 */
public class AddStockEntryFrame extends javax.swing.JFrame {

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
        jPopupMenu1 = new javax.swing.JPopupMenu();

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
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    ComboBoxItem selectedValue = suggestionList.getSelectedValue();
                    if (selectedValue != null) {
                        productNameTextField.setText(selectedValue.getName());
                        jPopupMenu1.setVisible(false);
                    }
                }
                System.out.println("Key released: " + productNameTextField.getText());
                String text = productNameTextField.getText().trim();
                if (text.isEmpty()) {
                    jPopupMenu1.setVisible(false);
                    return;
                }

                // Lọc danh sách gợi ý
                List<ComboBoxItem> filtered = productItems.stream()
                        .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase()))
                        .collect(Collectors.toList());

                if (filtered.isEmpty()) {
                    jPopupMenu1.setVisible(false);
                    return;
                }

                showSuggestionPopup(filtered);
            }
        });

//        productNameTextField.addFocusListener(new FocusAdapter() {
//            @Override
//            public void focusLost(FocusEvent e) {
//                jPopupMenu1.setVisible(false);
//            }
//        });
        suggestionList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ComboBoxItem selectedValue = suggestionList.getSelectedValue();
                if (selectedValue != null) {
                    productNameTextField.setText(selectedValue.getName());
                    System.out.println("🛒 Sản phẩm đã chọn: ID = " + selectedValue.getId() + ", Name = " + selectedValue.getName());
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
                    }
                }
            }
        });
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(89, 89, 89)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel6)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(productNameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 338, Short.MAX_VALUE)
                    .addComponent(priceTextField)
                    .addComponent(quantityTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(65, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(cancelBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(addStockEntryBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39)
                .addComponent(saveStockEntryBtn)
                .addGap(27, 27, 27))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(112, 112, 112)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(productNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(81, 81, 81)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(quantityTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(89, 89, 89)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(priceTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 81, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addStockEntryBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(saveStockEntryBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(59, 59, 59))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(459, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void priceTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_priceTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_priceTextFieldActionPerformed

    private void addStockEntryBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addStockEntryBtnActionPerformed

        String productName = productNameTextField.getText().trim();
        int quantity;
        double purchasePrice;

        try {
            quantity = Integer.parseInt(quantityTextField.getText());
            purchasePrice = Double.parseDouble(priceTextField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số lượng hoặc giá nhập không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ComboBoxItem selectedProduct = productItems.stream()
                .filter(item -> item.getName().equalsIgnoreCase(productName))
                .findFirst()
                .orElse(null);
        int productId;
        if (selectedProduct == null) {
            // Nếu sản phẩm chưa tồn tại, thêm sản phẩm mới vào bảng Product
            productId = productController.addProductWithStockEntry(productName, supplierId);
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
            int stockEntryId = stockEntryController.addStockEntry(stockEntry);

            for (StockEntryDetail detail : tempStockEntryDetails) {
                detail.setStockEntryId(stockEntryId);
                stockEntryDetailController.addStockEntryDetail(detail);
            }

            JOptionPane.showMessageDialog(this, "Nhập hàng thành công!");
            if (parentFrame != null) {
                parentFrame.loadStockEntryData();
            }
            this.dispose();
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi nhập hàng: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }


    }//GEN-LAST:event_saveStockEntryBtnActionPerformed

    public static void main(String[] args) {
        try {
            System.setOut(new java.io.PrintStream(System.out, true, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            System.out.println("Lỗi khi đặt encoding UTF-8!");
        }

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addStockEntryBtn;
    private javax.swing.JButton cancelBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JTextField priceTextField;
    private javax.swing.JTextField productNameTextField;
    private javax.swing.JTextField quantityTextField;
    private javax.swing.JButton saveStockEntryBtn;
    // End of variables declaration//GEN-END:variables
}
