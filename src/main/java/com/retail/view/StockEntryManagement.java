/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.retail.view;

import com.retail.controller.StockEntryController;
import com.retail.model.StockEntry;
import java.io.UnsupportedEncodingException;
import java.util.List;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author Admin
 */
public final class StockEntryManagement extends javax.swing.JFrame {
        StockEntryController stockEntryController;

    /**
     * Creates new form StockEntryManagement
     */
    public StockEntryManagement() {
        initComponents();
        
        stockEntryController = new StockEntryController();
        
        DefaultTableModel defaulTableModel = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
        };
        
        stockEntryTable.setModel(defaulTableModel);
        
        defaulTableModel.addColumn("Inventory Id");
        defaulTableModel.addColumn("Product Name");
        defaulTableModel.addColumn("Quantity");
        defaulTableModel.addColumn("Purchase Price");
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
            stockEntry.getProductName(),
            stockEntry.getQuantity(),
            (int) stockEntry.getPurchasePrice(),
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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        addProductBtn = new javax.swing.JButton();
        editProductBtn = new javax.swing.JButton();
        deleteProductBtn = new javax.swing.JButton();
        goHomeBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        stockEntryTable = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 153, 51));
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("STOCK ENTRY MANAGEMENT");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(289, 289, 289))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel1)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        addProductBtn.setBackground(new java.awt.Color(0, 153, 51));
        addProductBtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        addProductBtn.setForeground(new java.awt.Color(255, 255, 255));
        addProductBtn.setText("Add");
        addProductBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addProductBtnActionPerformed(evt);
            }
        });

        editProductBtn.setBackground(new java.awt.Color(0, 153, 51));
        editProductBtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        editProductBtn.setForeground(new java.awt.Color(255, 255, 255));
        editProductBtn.setText("Edit");
        editProductBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editProductBtnActionPerformed(evt);
            }
        });

        deleteProductBtn.setBackground(new java.awt.Color(0, 153, 51));
        deleteProductBtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        deleteProductBtn.setForeground(new java.awt.Color(255, 255, 255));
        deleteProductBtn.setText("Delete");
        deleteProductBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteProductBtnActionPerformed(evt);
            }
        });

        goHomeBtn.setBackground(new java.awt.Color(0, 153, 51));
        goHomeBtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        goHomeBtn.setForeground(new java.awt.Color(255, 255, 255));
        goHomeBtn.setText("Home");
        goHomeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                goHomeBtnActionPerformed(evt);
            }
        });

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
        jScrollPane1.setViewportView(stockEntryTable);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 153, 51));
        jLabel5.setText(" BẢNG GHI NHẬP KHO");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(23, 23, 23)
                                .addComponent(addProductBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(editProductBtn)
                                .addGap(7, 7, 7)
                                .addComponent(deleteProductBtn)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(goHomeBtn))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(418, 418, 418)
                                .addComponent(jLabel5)))
                        .addGap(0, 428, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 998, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addProductBtn)
                    .addComponent(editProductBtn)
                    .addComponent(deleteProductBtn)
                    .addComponent(goHomeBtn))
                .addGap(35, 35, 35)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(52, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void goHomeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_goHomeBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_goHomeBtnActionPerformed

    private void deleteProductBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteProductBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_deleteProductBtnActionPerformed

    private void editProductBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editProductBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_editProductBtnActionPerformed

    private void addProductBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addProductBtnActionPerformed
           AddStockEntryFrame addStockFrame = new AddStockEntryFrame(this);
            addStockFrame.setVisible(true);
    }//GEN-LAST:event_addProductBtnActionPerformed

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
    private javax.swing.JButton deleteProductBtn;
    private javax.swing.JButton editProductBtn;
    private javax.swing.JButton goHomeBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable stockEntryTable;
    // End of variables declaration//GEN-END:variables
}
