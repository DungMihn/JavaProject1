/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Market;

import com.retail.ConnectDB;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;



public class Login extends javax.swing.JFrame {

    /**
     * Creates new form Login
     */
    public Login() {
        initComponents();
    }
    
    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        BoxLogo = new javax.swing.JPanel();
        Logo = new javax.swing.JLabel();
        FamilyPoint = new javax.swing.JLabel();
        EveryProduct = new javax.swing.JLabel();
        NiceService = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        BoxForm = new javax.swing.JPanel();
        FormRole = new javax.swing.JComboBox<>();
        UID = new javax.swing.JLabel();
        Password = new javax.swing.JLabel();
        SelectRole = new javax.swing.JLabel();
        LoginButton = new javax.swing.JButton();
        ClearButton = new javax.swing.JButton();
        Text = new javax.swing.JLabel();
        LogoLogin = new javax.swing.JLabel();
        LoginText = new javax.swing.JLabel();
        FormUID = new javax.swing.JTextField();
        OffIcon = new javax.swing.JLabel();
        FormPassword = new javax.swing.JPasswordField();

        jLabel1.setText("jLabel1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        BoxLogo.setBackground(new java.awt.Color(255, 102, 0));

        Logo.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        Logo.setForeground(new java.awt.Color(255, 255, 255));

        FamilyPoint.setBackground(new java.awt.Color(255, 255, 255));
        FamilyPoint.setFont(new java.awt.Font("Century Gothic", 1, 18)); // NOI18N
        FamilyPoint.setForeground(new java.awt.Color(255, 255, 255));
        FamilyPoint.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        FamilyPoint.setText("FamilyPoint");

        EveryProduct.setBackground(new java.awt.Color(255, 255, 255));
        EveryProduct.setFont(new java.awt.Font("Century Gothic", 1, 18)); // NOI18N
        EveryProduct.setForeground(new java.awt.Color(255, 255, 255));
        EveryProduct.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        EveryProduct.setText("Every Product");

        NiceService.setBackground(new java.awt.Color(255, 255, 255));
        NiceService.setFont(new java.awt.Font("Century Gothic", 1, 18)); // NOI18N
        NiceService.setForeground(new java.awt.Color(255, 255, 255));
        NiceService.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        NiceService.setText("Nice Service");

        jLabel2.setIcon(new javax.swing.ImageIcon("D:\\LongNg(Aptec)\\JAVA_B189_196\\JavaProject1\\src\\main\\java\\Icon\\market.png")); // NOI18N

        javax.swing.GroupLayout BoxLogoLayout = new javax.swing.GroupLayout(BoxLogo);
        BoxLogo.setLayout(BoxLogoLayout);
        BoxLogoLayout.setHorizontalGroup(
            BoxLogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BoxLogoLayout.createSequentialGroup()
                .addContainerGap(16, Short.MAX_VALUE)
                .addGroup(BoxLogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BoxLogoLayout.createSequentialGroup()
                        .addGroup(BoxLogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(BoxLogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(EveryProduct, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(FamilyPoint, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(NiceService, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(BoxLogoLayout.createSequentialGroup()
                                .addGap(64, 64, 64)
                                .addComponent(Logo)))
                        .addGap(16, 16, 16))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BoxLogoLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(42, 42, 42))))
        );
        BoxLogoLayout.setVerticalGroup(
            BoxLogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BoxLogoLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Logo)
                .addGap(37, 37, 37)
                .addComponent(FamilyPoint)
                .addGap(16, 16, 16)
                .addComponent(EveryProduct)
                .addGap(16, 16, 16)
                .addComponent(NiceService)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        BoxForm.setBackground(new java.awt.Color(255, 255, 255));

        FormRole.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        FormRole.setForeground(new java.awt.Color(51, 51, 51));
        FormRole.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Manager", "Cashier", "Stock_keeper" }));
        FormRole.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FormRoleActionPerformed(evt);
            }
        });

        UID.setBackground(new java.awt.Color(255, 255, 255));
        UID.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        UID.setForeground(new java.awt.Color(255, 163, 102));
        UID.setText("Người dùng");

        Password.setBackground(new java.awt.Color(255, 102, 0));
        Password.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        Password.setForeground(new java.awt.Color(255, 163, 102));
        Password.setText("Mật khẩu");

        SelectRole.setBackground(new java.awt.Color(255, 255, 255));
        SelectRole.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        SelectRole.setForeground(new java.awt.Color(255, 163, 102));
        SelectRole.setText("Vai trò");

        LoginButton.setBackground(new java.awt.Color(255, 132, 51));
        LoginButton.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        LoginButton.setForeground(new java.awt.Color(255, 255, 255));
        LoginButton.setIcon(new javax.swing.ImageIcon("D:\\LongNg(Aptec)\\JAVA_B189_196\\JavaProject1\\src\\main\\java\\Icon\\login.png")); // NOI18N
        LoginButton.setText("Đăng nhập");
        LoginButton.setBorder(null);
        LoginButton.setMargin(new java.awt.Insets(8, 14, 3, 14));
        LoginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                LoginButtonMouseClicked(evt);
            }
        });
        LoginButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LoginButtonActionPerformed(evt);
            }
        });

        ClearButton.setBackground(new java.awt.Color(255, 132, 51));
        ClearButton.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        ClearButton.setForeground(new java.awt.Color(255, 255, 255));
        ClearButton.setIcon(new javax.swing.ImageIcon("D:\\LongNg(Aptec)\\JAVA_B189_196\\JavaProject1\\src\\main\\java\\Icon\\cleaning.png")); // NOI18N
        ClearButton.setText("Clear");
        ClearButton.setBorder(null);
        ClearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ClearButtonActionPerformed(evt);
            }
        });

        Text.setBackground(new java.awt.Color(255, 102, 0));
        Text.setFont(new java.awt.Font("Century Gothic", 2, 24)); // NOI18N
        Text.setForeground(new java.awt.Color(255, 255, 255));
        Text.setText("CodeSpace");

        LogoLogin.setBackground(new java.awt.Color(255, 255, 255));
        LogoLogin.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        LogoLogin.setForeground(new java.awt.Color(255, 255, 255));
        LogoLogin.setIcon(new javax.swing.ImageIcon("D:\\LongNg(Aptec)\\JAVA_B189_196\\JavaProject1\\src\\main\\java\\Icon\\Admin.png")); // NOI18N

        LoginText.setBackground(new java.awt.Color(255, 102, 0));
        LoginText.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        LoginText.setForeground(new java.awt.Color(255, 102, 0));
        LoginText.setText("LOGIN");

        FormUID.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        FormUID.setForeground(new java.awt.Color(255, 102, 0));
        FormUID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FormUIDActionPerformed(evt);
            }
        });

        OffIcon.setIcon(new javax.swing.ImageIcon("D:\\LongNg(Aptec)\\JAVA_B189_196\\JavaProject1\\src\\main\\java\\Icon\\close.png")); // NOI18N
        OffIcon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                OffIconMouseClicked(evt);
            }
        });

        FormPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                FormPasswordKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout BoxFormLayout = new javax.swing.GroupLayout(BoxForm);
        BoxForm.setLayout(BoxFormLayout);
        BoxFormLayout.setHorizontalGroup(
            BoxFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BoxFormLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(Text)
                .addGap(163, 163, 163))
            .addGroup(BoxFormLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(BoxFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(UID)
                    .addComponent(Password)
                    .addComponent(SelectRole))
                .addGap(24, 24, 24)
                .addGroup(BoxFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(FormUID, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(FormRole, javax.swing.GroupLayout.Alignment.LEADING, 0, 272, Short.MAX_VALUE)
                    .addComponent(FormPassword, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap(61, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BoxFormLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(BoxFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BoxFormLayout.createSequentialGroup()
                        .addGroup(BoxFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(BoxFormLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(LogoLogin))
                            .addGroup(BoxFormLayout.createSequentialGroup()
                                .addComponent(LoginText)
                                .addGap(158, 158, 158)
                                .addComponent(OffIcon)))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BoxFormLayout.createSequentialGroup()
                        .addComponent(LoginButton, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24)
                        .addComponent(ClearButton, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(119, 119, 119))))
        );
        BoxFormLayout.setVerticalGroup(
            BoxFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BoxFormLayout.createSequentialGroup()
                .addGroup(BoxFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(BoxFormLayout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(LoginText))
                    .addGroup(BoxFormLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(OffIcon)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LogoLogin)
                .addGap(13, 13, 13)
                .addGroup(BoxFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BoxFormLayout.createSequentialGroup()
                        .addComponent(SelectRole)
                        .addGap(20, 20, 20)
                        .addComponent(UID))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BoxFormLayout.createSequentialGroup()
                        .addComponent(FormRole, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(FormUID, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(BoxFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Password)
                    .addComponent(FormPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(BoxFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ClearButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LoginButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(Text)
                .addGap(24, 24, 24))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(BoxLogo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(BoxForm, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BoxLogo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(BoxForm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void FormRoleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FormRoleActionPerformed
       // TODO add your handling code here:
    }//GEN-LAST:event_FormRoleActionPerformed

    private void LoginButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LoginButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_LoginButtonActionPerformed

    private void ClearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ClearButtonActionPerformed
         FormUID.setText("");  // Xóa nội dung ô nhập User ID
    FormPassword.setText("");  // Xóa nội dung ô nhập Password
    FormRole.setSelectedIndex(0); // Đưa combo box về giá trị đầu tiên (Admin)
    }//GEN-LAST:event_ClearButtonActionPerformed

    private void FormUIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FormUIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_FormUIDActionPerformed

    private void LoginButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LoginButtonMouseClicked
         String role = FormRole.getSelectedItem().toString();
        String uid = FormUID.getText();
        String password = new String(FormPassword.getPassword());

        if (uid.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = ConnectDB.connect()) {
            if (conn != null) {
                String sql = "SELECT * FROM Employee WHERE username = ? AND password = ? AND role = ?";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, uid);
                pst.setString(2, password);
                pst.setString(3, role);

                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "Đăng nhập thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    this.dispose(); 

                    String username = rs.getString("username"); // Lấy username từ DB
                    Menu menuForm = new Menu(role, username); // Truyền role và username
                    menuForm.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Sai thông tin đăng nhập!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Không thể kết nối CSDL!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi hệ thống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    

                                        
    }//GEN-LAST:event_LoginButtonMouseClicked

    private void OffIconMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OffIconMouseClicked
     int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn thoát?", "Xác nhận", JOptionPane.YES_NO_OPTION);
    
    if (confirm == JOptionPane.YES_OPTION) {
        java.lang.System.exit(0); // Thoát chương trình
    }
    }//GEN-LAST:event_OffIconMouseClicked

    private void FormPasswordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_FormPasswordKeyPressed
            if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            LoginButtonMouseClicked(null); // Gọi sự kiện đăng nhập khi nhấn Enter
        }
    }//GEN-LAST:event_FormPasswordKeyPressed

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
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Login().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel BoxForm;
    private javax.swing.JPanel BoxLogo;
    private javax.swing.JButton ClearButton;
    private javax.swing.JLabel EveryProduct;
    private javax.swing.JLabel FamilyPoint;
    private javax.swing.JPasswordField FormPassword;
    private javax.swing.JComboBox<String> FormRole;
    private javax.swing.JTextField FormUID;
    private javax.swing.JButton LoginButton;
    private javax.swing.JLabel LoginText;
    private javax.swing.JLabel Logo;
    private javax.swing.JLabel LogoLogin;
    private javax.swing.JLabel NiceService;
    private javax.swing.JLabel OffIcon;
    private javax.swing.JLabel Password;
    private javax.swing.JLabel SelectRole;
    private javax.swing.JLabel Text;
    private javax.swing.JLabel UID;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    // End of variables declaration//GEN-END:variables
}
