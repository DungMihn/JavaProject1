/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Market;

/**
 *
 * @author ADMIN
 */
public class Login extends javax.swing.JFrame {

    /**
     * Creates new form Login
     */
    public Login() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        BoxLogo = new javax.swing.JPanel();
        Logo = new javax.swing.JLabel();
        FamilyPoint = new javax.swing.JLabel();
        EveryProduct = new javax.swing.JLabel();
        NiceService = new javax.swing.JLabel();
        BoxForm = new javax.swing.JPanel();
        FormPassword = new javax.swing.JTextField();
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
        jLabel3 = new javax.swing.JLabel();

        jLabel1.setText("jLabel1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        BoxLogo.setBackground(new java.awt.Color(255, 255, 255));

        Logo.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        Logo.setForeground(new java.awt.Color(255, 255, 255));
        Logo.setIcon(new javax.swing.ImageIcon("C:\\Users\\ADMIN\\Downloads\\market.png")); // NOI18N

        FamilyPoint.setBackground(new java.awt.Color(255, 102, 0));
        FamilyPoint.setFont(new java.awt.Font("Century Gothic", 1, 18)); // NOI18N
        FamilyPoint.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        FamilyPoint.setText("FamilyPoint");

        EveryProduct.setBackground(new java.awt.Color(255, 102, 0));
        EveryProduct.setFont(new java.awt.Font("Century Gothic", 1, 18)); // NOI18N
        EveryProduct.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        EveryProduct.setText("Every Product");

        NiceService.setBackground(new java.awt.Color(255, 102, 0));
        NiceService.setFont(new java.awt.Font("Century Gothic", 1, 18)); // NOI18N
        NiceService.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        NiceService.setText("Nice Service");

        javax.swing.GroupLayout BoxLogoLayout = new javax.swing.GroupLayout(BoxLogo);
        BoxLogo.setLayout(BoxLogoLayout);
        BoxLogoLayout.setHorizontalGroup(
            BoxLogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BoxLogoLayout.createSequentialGroup()
                .addContainerGap(70, Short.MAX_VALUE)
                .addComponent(Logo)
                .addGap(70, 70, 70))
            .addGroup(BoxLogoLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(BoxLogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(EveryProduct, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(FamilyPoint, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(NiceService, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        BoxLogoLayout.setVerticalGroup(
            BoxLogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BoxLogoLayout.createSequentialGroup()
                .addGap(66, 66, 66)
                .addComponent(Logo)
                .addGap(18, 18, 18)
                .addComponent(FamilyPoint)
                .addGap(16, 16, 16)
                .addComponent(EveryProduct)
                .addGap(16, 16, 16)
                .addComponent(NiceService)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        Logo.getAccessibleContext().setAccessibleName("");

        BoxForm.setBackground(new java.awt.Color(102, 102, 102));

        FormPassword.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        FormPassword.setForeground(new java.awt.Color(255, 102, 0));
        FormPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FormPasswordActionPerformed(evt);
            }
        });

        FormRole.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        FormRole.setForeground(new java.awt.Color(51, 51, 51));
        FormRole.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Admin", "Seller", " " }));
        FormRole.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FormRoleActionPerformed(evt);
            }
        });

        UID.setBackground(new java.awt.Color(255, 255, 255));
        UID.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        UID.setForeground(new java.awt.Color(255, 255, 255));
        UID.setText("User");

        Password.setBackground(new java.awt.Color(255, 102, 0));
        Password.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        Password.setForeground(new java.awt.Color(255, 255, 255));
        Password.setText("Mật khẩu");

        SelectRole.setBackground(new java.awt.Color(255, 255, 255));
        SelectRole.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        SelectRole.setForeground(new java.awt.Color(255, 255, 255));
        SelectRole.setText("Vai trò");

        LoginButton.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        LoginButton.setText("Đăng nhập");
        LoginButton.setBorder(null);

        ClearButton.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        ClearButton.setText("Clear");
        ClearButton.setBorder(null);

        Text.setBackground(new java.awt.Color(255, 102, 0));
        Text.setFont(new java.awt.Font("Century Gothic", 2, 24)); // NOI18N
        Text.setForeground(new java.awt.Color(255, 255, 255));
        Text.setText("CodeSpace");

        LogoLogin.setBackground(new java.awt.Color(255, 255, 255));
        LogoLogin.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        LogoLogin.setForeground(new java.awt.Color(255, 255, 255));
        LogoLogin.setIcon(new javax.swing.ImageIcon("C:\\Users\\ADMIN\\Downloads\\user.png")); // NOI18N

        LoginText.setBackground(new java.awt.Color(255, 255, 255));
        LoginText.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        LoginText.setForeground(new java.awt.Color(255, 255, 255));
        LoginText.setText("LOGIN");

        FormUID.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        FormUID.setForeground(new java.awt.Color(255, 102, 0));

        jLabel3.setIcon(new javax.swing.ImageIcon("C:\\Users\\ADMIN\\Downloads\\close.png")); // NOI18N

        javax.swing.GroupLayout BoxFormLayout = new javax.swing.GroupLayout(BoxForm);
        BoxForm.setLayout(BoxFormLayout);
        BoxFormLayout.setHorizontalGroup(
            BoxFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BoxFormLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(Text)
                .addGap(163, 163, 163))
            .addGroup(BoxFormLayout.createSequentialGroup()
                .addGap(115, 115, 115)
                .addComponent(LoginButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(ClearButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BoxFormLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(BoxFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BoxFormLayout.createSequentialGroup()
                        .addComponent(LoginText)
                        .addGap(158, 158, 158)
                        .addComponent(jLabel3)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BoxFormLayout.createSequentialGroup()
                        .addComponent(LogoLogin)
                        .addGap(213, 213, 213))))
            .addGroup(BoxFormLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(BoxFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(UID)
                    .addComponent(Password)
                    .addComponent(SelectRole))
                .addGap(40, 40, 40)
                .addGroup(BoxFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(BoxFormLayout.createSequentialGroup()
                        .addComponent(FormRole, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(BoxFormLayout.createSequentialGroup()
                        .addGroup(BoxFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(FormPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(FormUID, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(32, Short.MAX_VALUE))))
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
                        .addComponent(jLabel3)))
                .addGap(7, 7, 7)
                .addComponent(LogoLogin)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
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
                .addGap(24, 24, 24)
                .addGroup(BoxFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ClearButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LoginButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
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
                .addComponent(BoxForm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BoxLogo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(BoxForm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void FormPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FormPasswordActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_FormPasswordActionPerformed

    private void FormRoleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FormRoleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_FormRoleActionPerformed

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
    private javax.swing.JTextField FormPassword;
    private javax.swing.JComboBox<String> FormRole;
    private javax.swing.JTextField FormUID;
    private javax.swing.JButton LoginButton;
    private javax.swing.JLabel LoginText;
    private javax.swing.JLabel Logo;
    private javax.swing.JLabel LogoLogin;
    private javax.swing.JLabel NiceService;
    private javax.swing.JLabel Password;
    private javax.swing.JLabel SelectRole;
    private javax.swing.JLabel Text;
    private javax.swing.JLabel UID;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    // End of variables declaration//GEN-END:variables
}
