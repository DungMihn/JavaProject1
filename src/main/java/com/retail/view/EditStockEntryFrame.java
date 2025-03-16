/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.retail.view;

import com.retail.controller.InventoryController;
import com.retail.controller.StockEntryController;
import com.retail.controller.StockEntryDetailController;
import com.retail.controller.SupplierController;
import com.retail.model.ComboBoxItem;
import com.retail.model.StockEntry;
import com.retail.model.Inventory;
import com.retail.model.StockEntryDetail;
import com.retail.model.Supplier;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.io.UnsupportedEncodingException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;

/**
 *
 * @author Admin
 */
public class EditStockEntryFrame extends javax.swing.JFrame {

    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=GroceryStoreDB;encrypt=true;trustServerCertificate=true";
    private static final String USER = "bookoff";
    private static final String PASSWORD = "123456789";

    private StockEntryManagement parentFrame;
    StockEntryController stockEntryController;
    StockEntryDetailController stockEntryDetailController;
    InventoryController inventoryController;
    private StockEntry stockEntry;
    private SupplierController supplierController;
    private int stockEntryId;
    private DefaultTableModel stockEntryDetailTableModel;

    /**
     * Creates new form EditStockEntryFrame
     *
     * @param parentFrame
     * @param stockEntryId
     */
    public EditStockEntryFrame(StockEntryManagement parentFrame, int stockEntryId) {
        this.parentFrame = parentFrame;
        this.stockEntryId = stockEntryId;
        initComponents();

        // Đặt vị trí và kích thước
        setSize(800, 600);
        setLocation(
                (Toolkit.getDefaultToolkit().getScreenSize().width - getWidth()) / 2,
                (Toolkit.getDefaultToolkit().getScreenSize().height - getHeight()) / 2
        );

        stockEntryController = new StockEntryController();
        stockEntryDetailController = new StockEntryDetailController();
        supplierController = new SupplierController();
        inventoryController = new InventoryController();

        // Khởi tạo DefaultTableModel
        stockEntryDetailTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true; // Không cho phép chỉnh sửa ô trong bảng
            }
        };
        


        // Gán DefaultTableModel cho bảng
        stockEntryDetailTable.setModel(stockEntryDetailTableModel);

        // Thêm các cột vào bảng
        stockEntryDetailTableModel.addColumn("Product Id");
        stockEntryDetailTableModel.addColumn("Product Name");
        stockEntryDetailTableModel.addColumn("Quantity");
        stockEntryDetailTableModel.addColumn("Purchase Price");

        // Lấy thông tin StockEntry và hiển thị
        stockEntry = stockEntryController.getStockEntryById(stockEntryId);
        if (stockEntry != null) {
            employeeTextField.setText(String.valueOf(stockEntry.getEmployeeId()));

            loadSuppliersIntoComboBox();
            setSupplierComboBox(stockEntry);
            loadStockEntryDetailsIntoTable();
        }
    }

    private void setSupplierComboBox(StockEntry stockEntry) {
//        loadSuppliersIntoComboBox(); // Tải lại danh sách nhà cung cấp
//        // Chọn nhà cung cấp mới thêm vào JComboBox
        for (int i = 0; i < supplierComboBox.getItemCount(); i++) {
            ComboBoxItem item = (ComboBoxItem) supplierComboBox.getItemAt(i);
            if (item.getId() == stockEntry.getSupplierId()) {
                supplierComboBox.setSelectedIndex(i);
                break;
            }
        }

        System.out.println(stockEntry.getSupplierId());
    }

    private void loadStockEntryDetailsIntoTable() {
        stockEntryDetailTableModel.setRowCount(0); // Xóa dữ liệu cũ

        // Lấy danh sách chi tiết từ cơ sở dữ liệu
        List<StockEntryDetail> details = stockEntryDetailController.getStockEntryDetailsByStockEntryId(stockEntryId);

        // Thêm dữ liệu vào bảng
        for (StockEntryDetail detail : details) {
            stockEntryDetailTableModel.addRow(new Object[]{
                detail.getProductId(),
                detail.getProductName(),
                detail.getQuantity(),
                detail.getPurchasePrice()
            });
        }

        // Cập nhật giao diện
        stockEntryDetailTable.revalidate();
        stockEntryDetailTable.repaint();
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

        jPanel2 = new javax.swing.JPanel();
        cancelBtn = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        supplierComboBox = new javax.swing.JComboBox<>();
        editStockEntryBtn = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        employeeTextField = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        stockEntryDetailTable = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        cancelBtn.setBackground(new java.awt.Color(204, 204, 204));
        cancelBtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cancelBtn.setText("Thoát");
        cancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBtnActionPerformed(evt);
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

        editStockEntryBtn.setBackground(new java.awt.Color(0, 153, 51));
        editStockEntryBtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        editStockEntryBtn.setForeground(new java.awt.Color(255, 255, 255));
        editStockEntryBtn.setText("Lưu");
        editStockEntryBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editStockEntryBtnActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(0, 153, 51));
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Chỉnh sửa bản ghi nhập kho");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(247, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(131, 131, 131))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel1)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 153, 51));
        jLabel10.setText("Nhân viên");

        stockEntryDetailTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(stockEntryDetailTable);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Danh sách sản phẩm");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 631, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(30, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(cancelBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(editStockEntryBtn)
                        .addGap(41, 41, 41))))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(jLabel10))
                        .addGap(34, 34, 34)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(supplierComboBox, 0, 191, Short.MAX_VALUE)
                            .addComponent(employeeTextField)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(285, 285, 285)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(93, 93, 93)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(supplierComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(employeeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editStockEntryBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(68, 68, 68))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(437, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_cancelBtnActionPerformed

    private void supplierComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supplierComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_supplierComboBoxActionPerformed

    private void editStockEntryBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editStockEntryBtnActionPerformed
        // Lấy thông tin từ giao diện
        ComboBoxItem selectedSupplier = (ComboBoxItem) supplierComboBox.getSelectedItem();
        int supplierId = selectedSupplier.getId();
        int employeeId = Integer.parseInt(employeeTextField.getText());

        // Cập nhật thông tin StockEntry
        stockEntry.setSupplierId(supplierId);
        stockEntry.setEmployeeId(employeeId);
        boolean isStockEntryUpdated = stockEntryController.updateStockEntry(stockEntry);

        if (isStockEntryUpdated) {
            // Cập nhật thông tin StockEntryDetail và điều chỉnh stock_quantity
            boolean isDetailsUpdated = updateStockEntryDetails();

            if (isDetailsUpdated) {
                JOptionPane.showMessageDialog(this, "Cập nhật thông tin nhập kho thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);

                // Xuất lại file PDF và hiển thị cho người dùng
                generateAndOpenStockEntryReport();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật thông tin nhập kho!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_editStockEntryBtnActionPerformed

    private boolean isStockQuantitySufficient(int productId, int quantityChange) {
        Inventory inventory = inventoryController.getInventoryByProductId(productId);

        int currentStockQuantity = inventory.getStockQuantity();
        return (currentStockQuantity + quantityChange) >= 0;
    }

    private boolean updateStockEntryDetails() {
        if (stockEntryDetailTableModel == null) {
            System.out.println("❌ stockEntryDetailTableModel chưa được khởi tạo!");
            return false;
        }

        for (int i = 0; i < stockEntryDetailTableModel.getRowCount(); i++) {
            try {
                // Lấy giá trị từ bảng
                Object productIdObj = stockEntryDetailTableModel.getValueAt(i, 0); // Cột Product Id
                Object productNameObj = stockEntryDetailTableModel.getValueAt(i, 1); // Cột Product Id
                Object quantityObj = stockEntryDetailTableModel.getValueAt(i, 2);  // Cột Quantity
                Object priceObj = stockEntryDetailTableModel.getValueAt(i, 3);     // Cột Purchase Price

                // Kiểm tra giá trị null
                if (productIdObj == null || quantityObj == null || priceObj == null) {
                    System.out.println("❌ Giá trị tại hàng " + i + " bị thiếu. Bỏ qua hàng này.");
                    return false; // Dừng ngay lập tức nếu có lỗi
                }

                // Chuyển đổi giá trị
                String productName = productNameObj.toString();
                int productId = Integer.parseInt(productIdObj.toString());
                int newQuantity = Integer.parseInt(quantityObj.toString());
                double newPrice = Double.parseDouble(priceObj.toString());

                // Lấy thông tin chi tiết cũ từ cơ sở dữ liệu
                StockEntryDetail oldDetail = stockEntryDetailController.getStockEntryDetailByStockEntryIdAndProductId(stockEntryId, productId);

                if (oldDetail == null) {
                    System.out.println("❌ Không tìm thấy chi tiết nhập kho cho Product ID: " + productId);
                    return false; // Dừng ngay lập tức nếu có lỗi
                }

                // Tính toán sự thay đổi số lượng
                int quantityChange = newQuantity - oldDetail.getQuantity();

                // Kiểm tra số lượng tồn kho
                if (quantityChange < 0 && !isStockQuantitySufficient(productId, quantityChange)) {
                    System.out.println("❌ Không đủ số lượng tồn kho để cập nhật cho Product ID: " + productId);
                    JOptionPane.showMessageDialog(this, "Không đủ số lượng tồn kho để cập nhật cho sản phẩm: " + productName, "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return false; // Dừng ngay lập tức nếu có lỗi
                }

                // Cập nhật StockEntryDetail
                oldDetail.setQuantity(newQuantity);
                oldDetail.setPurchasePrice(newPrice);
                boolean isDetailUpdated = stockEntryDetailController.updateStockEntryDetail(oldDetail);

                if (!isDetailUpdated) {
                    System.out.println("❌ Lỗi khi cập nhật chi tiết nhập kho cho Product ID: " + productId);
                    return false; // Dừng ngay lập tức nếu có lỗi
                }

                // Cập nhật Inventory
                boolean isInventoryUpdated = inventoryController.updateStockQuantity(productId, quantityChange);

                if (!isInventoryUpdated) {
                    System.out.println("❌ Lỗi khi cập nhật số lượng tồn kho cho Product ID: " + productId);
                    return false; // Dừng ngay lập tức nếu có lỗi
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Lỗi chuyển đổi số tại hàng " + i + ": " + e.getMessage());
                return false; // Dừng ngay lập tức nếu có lỗi
            } catch (HeadlessException e) {
                System.out.println("❌ Lỗi không xác định tại hàng " + i + ": " + e.getMessage());
                return false; // Dừng ngay lập tức nếu có lỗi
            }
        }

        return true; // Trả về true nếu tất cả đều thành công
    }

    private void generateAndOpenStockEntryReport() {
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

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(EditStockEntryFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EditStockEntryFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EditStockEntryFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EditStockEntryFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        try {
            System.setOut(new java.io.PrintStream(System.out, true, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            System.out.println("Lỗi khi đặt encoding UTF-8!");
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelBtn;
    private javax.swing.JButton editStockEntryBtn;
    private javax.swing.JTextField employeeTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable stockEntryDetailTable;
    private javax.swing.JComboBox<ComboBoxItem> supplierComboBox;
    // End of variables declaration//GEN-END:variables
}
