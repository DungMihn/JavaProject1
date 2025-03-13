/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.retail.view;

import com.retail.controller.StockEntryController;
import com.retail.model.StockEntry;
import java.awt.HeadlessException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JFrame;
import net.sf.jasperreports.engine.JRException;

/**
 *
 * @author Admin
 */
public final class StockEntryManagement extends javax.swing.JFrame {

    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=GroceryStoreDB;encrypt=true;trustServerCertificate=true";
    private static final String USER = "bookoff";
    private static final String PASSWORD = "123456789";
    StockEntryController stockEntryController;

    /**
     * Creates new form StockEntryManagement
     */
    
    
    public StockEntryManagement() {
        initComponents();

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        stockEntryController = new StockEntryController();

        DefaultTableModel defaulTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

        };

        stockEntryTable.setModel(defaulTableModel);

        defaulTableModel.addColumn("Inventory Id");
        defaulTableModel.addColumn("Entry Date");

        loadStockEntryData();
    }

    void loadStockEntryData() {
        DefaultTableModel model = (DefaultTableModel) stockEntryTable.getModel();
        model.setRowCount(0); // Xóa dữ liệu cũ

        List<StockEntry> stockEntries = stockEntryController.getAllStockEntries();
        for (StockEntry stockEntry : stockEntries) {
            model.addRow(new Object[]{
                stockEntry.getStockEntryId(),
                stockEntry.getEntryDate()
            });
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
        addProductBtn = new javax.swing.JButton();
        editProductBtn = new javax.swing.JButton();
        deleteStockEntryBtn = new javax.swing.JButton();
        goHomeBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        stockEntryTable = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        viewStockEntryBtn = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        addProductBtn.setBackground(new java.awt.Color(0, 153, 51));
        addProductBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        addProductBtn.setForeground(new java.awt.Color(255, 255, 255));
        addProductBtn.setText("Thêm");
        addProductBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addProductBtnActionPerformed(evt);
            }
        });

        editProductBtn.setBackground(new java.awt.Color(0, 153, 51));
        editProductBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        editProductBtn.setForeground(new java.awt.Color(255, 255, 255));
        editProductBtn.setText("Chỉnh sửa");
        editProductBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editProductBtnActionPerformed(evt);
            }
        });

        deleteStockEntryBtn.setBackground(new java.awt.Color(0, 153, 51));
        deleteStockEntryBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        deleteStockEntryBtn.setForeground(new java.awt.Color(255, 255, 255));
        deleteStockEntryBtn.setText("Xóa");
        deleteStockEntryBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteStockEntryBtnActionPerformed(evt);
            }
        });

        goHomeBtn.setBackground(new java.awt.Color(0, 153, 51));
        goHomeBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        goHomeBtn.setForeground(new java.awt.Color(255, 255, 255));
        goHomeBtn.setText("Home");
        goHomeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                goHomeBtnActionPerformed(evt);
            }
        });

        stockEntryTable.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        stockEntryTable.setModel(new javax.swing.table.DefaultTableModel(
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
        stockEntryTable.setMaximumSize(new java.awt.Dimension(2147483647, 200));
        stockEntryTable.setPreferredSize(new java.awt.Dimension(500, 150));
        stockEntryTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(stockEntryTable);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 153, 51));
        jLabel5.setText(" BẢNG GHI NHẬP HÀNG");

        viewStockEntryBtn.setBackground(new java.awt.Color(0, 153, 51));
        viewStockEntryBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        viewStockEntryBtn.setForeground(new java.awt.Color(255, 255, 255));
        viewStockEntryBtn.setText("Xem hóa đơn");
        viewStockEntryBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewStockEntryBtnActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(0, 153, 51));
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("HÓA ĐƠN NHẬP HÀNG");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 415, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel1)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(addProductBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(editProductBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(deleteStockEntryBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 202, Short.MAX_VALUE)
                .addComponent(viewStockEntryBtn)
                .addGap(67, 67, 67)
                .addComponent(goHomeBtn)
                .addGap(155, 155, 155))
            .addComponent(jSeparator1)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane1)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                    .addComponent(addProductBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editProductBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(deleteStockEntryBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(viewStockEntryBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(goHomeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 28, Short.MAX_VALUE))
        );

        jMenuBar1.setBorder(javax.swing.BorderFactory.createCompoundBorder(null, javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0))));
        jMenuBar1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jMenu1.setText("Quản lý kho");
        jMenu1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu1MouseClicked(evt);
            }
        });
        jMenu1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu1ActionPerformed(evt);
            }
        });
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Hóa đơn nhập hàng");
        jMenu2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu2ActionPerformed(evt);
            }
        });
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

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

    private void goHomeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_goHomeBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_goHomeBtnActionPerformed

    private void deleteStockEntryBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteStockEntryBtnActionPerformed
        int row = stockEntryTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(StockEntryManagement.this, "Vui lòng chọn bản ghi nhập kho trước", "Lỗi", JOptionPane.ERROR_MESSAGE);

        } else {
            int stockEntryId = Integer.parseInt(String.valueOf(stockEntryTable.getValueAt(row, 0)));
            int confirm = JOptionPane.showConfirmDialog(StockEntryManagement.this, "Bạn chắc chắc muốn xóa không?");
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    stockEntryController.deleteStockEntry(stockEntryId);
                    JOptionPane.showMessageDialog(this, "Xóa bản ghi thành công!");

                    loadStockEntryData();

                } catch (HeadlessException e) {
                    JOptionPane.showMessageDialog(this, "Xóa bản ghi thất bại!");
                }
            }

        }

    }//GEN-LAST:event_deleteStockEntryBtnActionPerformed

    private void editProductBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editProductBtnActionPerformed
        int row = stockEntryTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(StockEntryManagement.this, "Vui lòng chọn bản ghi nhập kho trước", "Lỗi", JOptionPane.ERROR_MESSAGE);

        } else {
            int stockEntryId = Integer.parseInt(String.valueOf(stockEntryTable.getValueAt(row, 0)));
            new EditStockEntryFrame(this, stockEntryId).setVisible(true);
        }

    }//GEN-LAST:event_editProductBtnActionPerformed

    private void addProductBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addProductBtnActionPerformed
        SelectSupplierFrame selectSupplierFrame = new SelectSupplierFrame(this);
        selectSupplierFrame.setVisible(true);
    }//GEN-LAST:event_addProductBtnActionPerformed

    private void viewStockEntryBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewStockEntryBtnActionPerformed

        int row = stockEntryTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn bản ghi nhập kho trước", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } else {
            int stockEntryId = Integer.parseInt(String.valueOf(stockEntryTable.getValueAt(row, 0)));

            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
                // Gọi stored procedure
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

    }//GEN-LAST:event_viewStockEntryBtnActionPerformed

    private void jMenu2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenu2ActionPerformed

    private void jMenu1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu1ActionPerformed
        
    }//GEN-LAST:event_jMenu1ActionPerformed

    private void jMenu1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu1MouseClicked
        InventoryManagement inventoryManagement = new InventoryManagement(); // Truyền this (frame cha) vào
        inventoryManagement.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jMenu1MouseClicked


    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            System.setOut(new java.io.PrintStream(System.out, true, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            System.out.println("Lỗi khi đặt encoding UTF-8!");
        }
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
            java.util.logging.Logger.getLogger(StockEntryManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(StockEntryManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(StockEntryManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(StockEntryManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new StockEntryManagement().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addProductBtn;
    private javax.swing.JButton deleteStockEntryBtn;
    private javax.swing.JButton editProductBtn;
    private javax.swing.JButton goHomeBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable stockEntryTable;
    private javax.swing.JButton viewStockEntryBtn;
    // End of variables declaration//GEN-END:variables
}
