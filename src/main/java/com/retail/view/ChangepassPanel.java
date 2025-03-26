/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.retail.view;


import com.retail.dao.DatabaseConnection;
import java.awt.Container;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

/**
 *
 * @author ADMIN
 */
public class ChangepassPanel extends javax.swing.JPanel {

  private String loggedInUser; // Lưu username của người dùng đang đăng nhập
    
    public ChangepassPanel(String username) {
        this.loggedInUser = username;
        initComponents();
    
    }
    
 
    
    private void reloadChangePassPanel() {
    // Xóa nội dung ô nhập mật khẩu
    OldPassForm.setText("");
    NewPassForm.setText("");
    ConfirmPassForm.setText("");

    // Thay thế panel cũ bằng panel mới
    Container parent = this.getParent();
    if (parent != null) {
        parent.remove(this);
        
        // Truyền tham số đúng vào constructor
        ChangepassPanel newPanel = new ChangepassPanel(loggedInUser); 
        
        parent.add(newPanel);
        parent.revalidate();
        parent.repaint();
    }
}


  
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ChangePass = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        OldPassForm = new javax.swing.JPasswordField();
        NewPassForm = new javax.swing.JPasswordField();
        ConfirmPassForm = new javax.swing.JPasswordField();
        IdentifyButton = new javax.swing.JButton();
        ExitButton = new javax.swing.JButton();
        ShowPassCheckbox = new javax.swing.JCheckBox();

        setBackground(new java.awt.Color(255, 163, 102));

        ChangePass.setFont(new java.awt.Font("Arial", 1, 32)); // NOI18N
        ChangePass.setForeground(new java.awt.Color(255, 255, 255));
        ChangePass.setText("ĐỔI MẬT KHẨU");

        jLabel2.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Mật khẩu cũ");

        jLabel3.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Mật khẩu mới");

        jLabel4.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Xác nhận mật khẩu");

        OldPassForm.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N

        NewPassForm.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N

        ConfirmPassForm.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N

        IdentifyButton.setBackground(new java.awt.Color(255, 132, 51));
        IdentifyButton.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        IdentifyButton.setForeground(new java.awt.Color(255, 255, 255));
        IdentifyButton.setText("Đổi mật khẩu");
        IdentifyButton.setMargin(new java.awt.Insets(8, 16, 8, 16));
        IdentifyButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                IdentifyButtonMouseClicked(evt);
            }
        });

        ExitButton.setBackground(new java.awt.Color(255, 132, 51));
        ExitButton.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        ExitButton.setForeground(new java.awt.Color(255, 255, 255));
        ExitButton.setText("Thoát");
        ExitButton.setMargin(new java.awt.Insets(8, 16, 8, 16));
        ExitButton.setPreferredSize(new java.awt.Dimension(105, 34));
        ExitButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ExitButtonMouseClicked(evt);
            }
        });

        ShowPassCheckbox.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        ShowPassCheckbox.setText("Hiển thị mật khẩu");
        ShowPassCheckbox.setToolTipText("");
        ShowPassCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ShowPassCheckboxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(195, 195, 195)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(24, 24, 24)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(ConfirmPassForm)
                            .addComponent(OldPassForm)
                            .addComponent(NewPassForm, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(259, 259, 259)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(71, 71, 71)
                                .addComponent(ShowPassCheckbox))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(IdentifyButton)
                                .addGap(40, 40, 40)
                                .addComponent(ExitButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(196, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(ChangePass)
                .addGap(279, 279, 279))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(ChangePass)
                .addGap(40, 40, 40)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(OldPassForm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(NewPassForm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ConfirmPassForm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addComponent(ShowPassCheckbox)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(IdentifyButton)
                    .addComponent(ExitButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(66, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void IdentifyButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_IdentifyButtonMouseClicked
    if (loggedInUser == null || loggedInUser.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin tài khoản!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String oldPass = new String(OldPassForm.getPassword());
        String newPass = new String(NewPassForm.getPassword());
        String confirmPass = new String(ConfirmPassForm.getPassword());

        if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!newPass.equals(confirmPass)) {
            JOptionPane.showMessageDialog(this, "Mật khẩu mới không trùng khớp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null) {
                // Kiểm tra mật khẩu cũ
                String sqlCheck = "SELECT password FROM Employee WHERE username = ?";
                PreparedStatement pstCheck = conn.prepareStatement(sqlCheck);
                pstCheck.setString(1, loggedInUser);
                ResultSet rs = pstCheck.executeQuery();

                if (rs.next()) {
                    String storedPassword = rs.getString("password");

                    if (!storedPassword.equals(oldPass)) {
                        JOptionPane.showMessageDialog(this, "Mật khẩu cũ không đúng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Cập nhật mật khẩu mới
                    String sqlUpdate = "UPDATE Employee SET password = ? WHERE username = ?";
                    PreparedStatement pstUpdate = conn.prepareStatement(sqlUpdate);
                    pstUpdate.setString(1, newPass);
                    pstUpdate.setString(2, loggedInUser);
                    pstUpdate.executeUpdate();

                    JOptionPane.showMessageDialog(this, "Mật khẩu đã được cập nhật thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    
                    // Reload lại ChangepassPanel
                reloadChangePassPanel();
                } else {
                    JOptionPane.showMessageDialog(this, "Tài khoản không tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi hệ thống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
   
                            
    }//GEN-LAST:event_IdentifyButtonMouseClicked

    private void ExitButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ExitButtonMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_ExitButtonMouseClicked

    private void ShowPassCheckboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ShowPassCheckboxActionPerformed
       char echoChar = ShowPassCheckbox.isSelected() ? (char) 0 : '•';
    OldPassForm.setEchoChar(echoChar);
    NewPassForm.setEchoChar(echoChar);
    ConfirmPassForm.setEchoChar(echoChar);
    }//GEN-LAST:event_ShowPassCheckboxActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel ChangePass;
    private javax.swing.JPasswordField ConfirmPassForm;
    private javax.swing.JButton ExitButton;
    private javax.swing.JButton IdentifyButton;
    private javax.swing.JPasswordField NewPassForm;
    private javax.swing.JPasswordField OldPassForm;
    private javax.swing.JCheckBox ShowPassCheckbox;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    // End of variables declaration//GEN-END:variables
}
