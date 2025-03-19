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
import com.retail.model.StockEntryDetail;
import com.retail.view.EditStockEntryFrame;
import com.retail.view.StockEntryManagement;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
public class AddStockEntryDetailFrame extends javax.swing.JFrame {

    private EditStockEntryFrame parentFrame; // Tham chiếu đến StockEntryManagement

    private ProductController productController;
    private int supplierId; // Thêm biến supplierId
    StockEntryController stockEntryController;
    StockEntryDetailController stockEntryDetailController;
    private SupplierController supplierController;
    private List<ComboBoxItem> productItems;
    private JList<ComboBoxItem> suggestionList;

    /**
     * Creates new form AddStockEntryDetailFrame
     */
    public interface AddStockEntryDetailListener {

        void onAddStockEntryDetail(StockEntryDetail detail);
    }
    private AddStockEntryDetailListener addStockEntryDetailListener;

    public void setAddStockEntryDetailListener(AddStockEntryDetailListener listener) {
        this.addStockEntryDetailListener = listener;
    }

    public AddStockEntryDetailFrame(EditStockEntryFrame parentFrame, int supplierId) {
        this.parentFrame = parentFrame;
        this.supplierId = supplierId;
        initComponents();

        // Đặt frame vào giữa màn hình
        setSize(800, 600);
        setLocation(
                (Toolkit.getDefaultToolkit().getScreenSize().width - getWidth()) / 2,
                (Toolkit.getDefaultToolkit().getScreenSize().height - getHeight()) / 2
        );

        productController = new ProductController();
        supplierController = new SupplierController();
        stockEntryController = new StockEntryController();
        stockEntryDetailController = new StockEntryDetailController();

        // Khởi tạo suggestionList
        suggestionList = new JList<>();
        suggestionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Tải danh sách sản phẩm và thiết lập auto-complete
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
            resetProductFields();
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
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        productNameTextField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        priceTextField = new javax.swing.JTextField();
        addStockEntryDetailBtn = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cancelBtn = new javax.swing.JButton();
        quantityTextField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        categoryComboBox = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        unitComboBox = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        barcodeTextField = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

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

        addStockEntryDetailBtn.setBackground(new java.awt.Color(0, 153, 0));
        addStockEntryDetailBtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        addStockEntryDetailBtn.setForeground(new java.awt.Color(255, 255, 255));
        addStockEntryDetailBtn.setText("Thêm");
        addStockEntryDetailBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addStockEntryDetailBtnActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(0, 153, 51));
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Nhập sản phẩm");

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
                .addComponent(addStockEntryDetailBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45))
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
                        .addContainerGap(117, Short.MAX_VALUE))))
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
                    .addComponent(addStockEntryDetailBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
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

    private void productNameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_productNameTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_productNameTextFieldActionPerformed

    private void priceTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_priceTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_priceTextFieldActionPerformed

    private void addStockEntryDetailBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addStockEntryDetailBtnActionPerformed
        // Lấy thông tin từ các trường nhập liệu
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
        detail.setProductName(productName);
        detail.setQuantity(quantity);
        detail.setPurchasePrice(purchasePrice);

        // Gọi listener để thêm dữ liệu vào bảng
        if (addStockEntryDetailListener != null) {
            addStockEntryDetailListener.onAddStockEntryDetail(detail);
        }


        JOptionPane.showMessageDialog(this, "Đã thêm sản phẩm vào danh sách nhập hàng!");
    }//GEN-LAST:event_addStockEntryDetailBtnActionPerformed

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_cancelBtnActionPerformed

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(AddStockEntryDetailFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(AddStockEntryDetailFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(AddStockEntryDetailFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(AddStockEntryDetailFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new AddStockEntryDetailFrame().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addStockEntryDetailBtn;
    private javax.swing.JTextField barcodeTextField;
    private javax.swing.JButton cancelBtn;
    private javax.swing.JComboBox<String> categoryComboBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JTextField priceTextField;
    private javax.swing.JTextField productNameTextField;
    private javax.swing.JTextField quantityTextField;
    private javax.swing.JComboBox<String> unitComboBox;
    // End of variables declaration//GEN-END:variables
}
