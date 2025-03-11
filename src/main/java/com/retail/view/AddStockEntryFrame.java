/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.retail.view;


import com.retail.controller.ProductController;
import com.retail.controller.StockEntryController;
import com.retail.controller.SupplierController;
import com.retail.model.ComboBoxItem;
import com.retail.model.Product;
import com.retail.model.StockEntry;
import com.retail.model.Supplier;
import java.awt.Dimension;
import java.awt.HeadlessException;
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
    StockEntryController stockEntryController;
    private SupplierController supplierController;
    private List<ComboBoxItem> productItems;
    private JList<ComboBoxItem> suggestionList;


    /**
     * Creates new form AddStockEntryFrame
     * @param parentFrame
     */
    public AddStockEntryFrame(StockEntryManagement parentFrame) {
        this.parentFrame = parentFrame;
        initComponents();
        productController = new ProductController();
        supplierController = new SupplierController();
        stockEntryController = new StockEntryController();

        // Khởi tạo suggestionList
        suggestionList = new JList<>();
        suggestionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jPopupMenu1 = new javax.swing.JPopupMenu();

        loadSuppliersIntoComboBox();
        loadProductsIntoAutoComplete();
        setupAutoCompleteListener();
        
    }

    private void loadProductsIntoAutoComplete() {
        List<Product> products = productController.getAllProducts();
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

        productNameTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                jPopupMenu1.setVisible(true);
            }
        });

        suggestionList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ComboBoxItem selectedValue = suggestionList.getSelectedValue();
                if (selectedValue != null) {
                    productNameTextField.setText(selectedValue.getName());
                    System.out.println("🛒 Sản phẩm đã chọn: ID = " + selectedValue.getId() + ", Name = " + selectedValue.getName());
                }
                jPopupMenu1.setVisible(false);
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
            SwingUtilities.invokeLater(() -> productNameTextField.requestFocusInWindow());
    }

    private void loadSuppliersIntoComboBox() {
        List<Supplier> suppliers = supplierController.getAllSuppliers();
        supplierComboBox.removeAllItems();

        for (Supplier supplier : suppliers) {
            supplierComboBox.addItem(new ComboBoxItem(supplier.getSupplierId(), supplier.getName()));
        }
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
        jLabel2 = new javax.swing.JLabel();
        productNameTextField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        priceTextField = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        supplierComboBox = new javax.swing.JComboBox<>();
        addStockEntryBtn = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cancelBtn = new javax.swing.JButton();
        quantityTextField = new javax.swing.JTextField();

        jLabel5.setText("jLabel5");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 153, 51));
        jLabel2.setText("Tên sản phẩm");

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

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 153, 51));
        jLabel9.setText("Nhà cung cấp");

        supplierComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supplierComboBoxActionPerformed(evt);
            }
        });

        addStockEntryBtn.setBackground(new java.awt.Color(0, 153, 51));
        addStockEntryBtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        addStockEntryBtn.setForeground(new java.awt.Color(255, 255, 255));
        addStockEntryBtn.setText("Thêm");
        addStockEntryBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addStockEntryBtnActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(0, 153, 51));
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Thêm bản ghi nhập kho");

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(cancelBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(addStockEntryBtn)
                .addGap(34, 34, 34))
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(106, 106, 106)
                        .addComponent(priceTextField))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(67, 67, 67)
                        .addComponent(quantityTextField))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(34, 34, 34)
                        .addComponent(supplierComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                        .addComponent(productNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 392, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(28, 28, 28))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(productNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(47, 47, 47)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(supplierComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(54, 54, 54)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(quantityTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(51, 51, 51)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(priceTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 61, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addStockEntryBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(44, 44, 44))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void priceTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_priceTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_priceTextFieldActionPerformed

    private void supplierComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supplierComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_supplierComboBoxActionPerformed

    private void addStockEntryBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addStockEntryBtnActionPerformed
        
        // Lấy tên sản phẩm từ text field
            String productName = productNameTextField.getText().trim();

            // Tìm productId tương ứng trong danh sách productItems
            ComboBoxItem selectedProduct = productItems.stream()
                    .filter(item -> item.getName().equalsIgnoreCase(productName))
                    .findFirst()
                    .orElse(null);

        int productId = selectedProduct.getId();
            
        ComboBoxItem selectedSupplier = (ComboBoxItem) supplierComboBox.getSelectedItem();
        int supplierId = selectedSupplier.getId();
        int quantity = Integer.parseInt(quantityTextField.getText());
        double purchasePrice = Double.parseDouble(priceTextField.getText());
        try {
            stockEntryController.addStockEntry(productId, supplierId, quantity , purchasePrice);
            JOptionPane.showMessageDialog(this, "Added inventory successfully!");
            if (parentFrame != null) {
            parentFrame.loadStockEntryData();
    }

    this.dispose(); // Đóng cửa sổ sau khi thêm xong
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(this, "Failed to add inventory!");
        }
        
        

    }//GEN-LAST:event_addStockEntryBtnActionPerformed

    private void productNameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_productNameTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_productNameTextFieldActionPerformed

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_cancelBtnActionPerformed

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
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JTextField priceTextField;
    private javax.swing.JTextField productNameTextField;
    private javax.swing.JTextField quantityTextField;
    private javax.swing.JComboBox<ComboBoxItem> supplierComboBox;
    // End of variables declaration//GEN-END:variables
}
