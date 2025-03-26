/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.retail.view;

/**
 *
 * @author ADMIN
 */
public class SystemPanel extends javax.swing.JPanel {

    private final Menu mainMenu; // Tham chiếu đến Menu

    public SystemPanel(Menu mainMenu) {
        this.mainMenu = mainMenu; // Lưu tham chiếu Menu
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        SystemBox = new javax.swing.JPanel();
        BackupPage = new javax.swing.JPanel();
        BackupIcon = new javax.swing.JLabel();
        Backup = new javax.swing.JLabel();
        RecoverPage = new javax.swing.JPanel();
        RecoverIcon = new javax.swing.JLabel();
        Recover = new javax.swing.JLabel();
        RepairPage = new javax.swing.JPanel();
        RepairIcon = new javax.swing.JLabel();
        Repair = new javax.swing.JLabel();
        TransferPage = new javax.swing.JPanel();
        TransferIcon = new javax.swing.JLabel();
        Transfer = new javax.swing.JLabel();
        ChangePassPage = new javax.swing.JPanel();
        ChangePass = new javax.swing.JLabel();
        ChangePassIcon = new javax.swing.JLabel();
        DiaryPage = new javax.swing.JPanel();
        DiaryIcon = new javax.swing.JLabel();
        Diary = new javax.swing.JLabel();
        InformaitionPage = new javax.swing.JPanel();
        InforIcon = new javax.swing.JLabel();
        Infomation = new javax.swing.JLabel();
        DecentralizationPage = new javax.swing.JPanel();
        DecentralizationIcon = new javax.swing.JLabel();
        Decentralization = new javax.swing.JLabel();

        SystemBox.setBackground(new java.awt.Color(255, 193, 153));
        SystemBox.setPreferredSize(new java.awt.Dimension(1227, 900));

        BackupPage.setBackground(new java.awt.Color(255, 255, 255));
        BackupPage.setPreferredSize(new java.awt.Dimension(136, 136));

        BackupIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        Backup.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        Backup.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Backup.setText("Sao Lưu");

        javax.swing.GroupLayout BackupPageLayout = new javax.swing.GroupLayout(BackupPage);
        BackupPage.setLayout(BackupPageLayout);
        BackupPageLayout.setHorizontalGroup(
            BackupPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Backup, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
            .addComponent(BackupIcon, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        BackupPageLayout.setVerticalGroup(
            BackupPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BackupPageLayout.createSequentialGroup()
                .addComponent(BackupIcon, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Backup))
        );

        RecoverPage.setBackground(new java.awt.Color(255, 255, 255));
        RecoverPage.setPreferredSize(new java.awt.Dimension(136, 136));

        RecoverIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        Recover.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        Recover.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Recover.setText("Phục Hồi");

        javax.swing.GroupLayout RecoverPageLayout = new javax.swing.GroupLayout(RecoverPage);
        RecoverPage.setLayout(RecoverPageLayout);
        RecoverPageLayout.setHorizontalGroup(
            RecoverPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Recover, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
            .addComponent(RecoverIcon, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        RecoverPageLayout.setVerticalGroup(
            RecoverPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RecoverPageLayout.createSequentialGroup()
                .addComponent(RecoverIcon, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Recover))
        );

        RepairPage.setBackground(new java.awt.Color(255, 255, 255));
        RepairPage.setPreferredSize(new java.awt.Dimension(136, 136));

        RepairIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        Repair.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        Repair.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Repair.setText("Sửa Chửa");

        javax.swing.GroupLayout RepairPageLayout = new javax.swing.GroupLayout(RepairPage);
        RepairPage.setLayout(RepairPageLayout);
        RepairPageLayout.setHorizontalGroup(
            RepairPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Repair, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
            .addComponent(RepairIcon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        RepairPageLayout.setVerticalGroup(
            RepairPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RepairPageLayout.createSequentialGroup()
                .addComponent(RepairIcon, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Repair, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        TransferPage.setBackground(new java.awt.Color(255, 255, 255));
        TransferPage.setPreferredSize(new java.awt.Dimension(136, 136));

        TransferIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        Transfer.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        Transfer.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Transfer.setText("Kết Chuyển");
        Transfer.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        javax.swing.GroupLayout TransferPageLayout = new javax.swing.GroupLayout(TransferPage);
        TransferPage.setLayout(TransferPageLayout);
        TransferPageLayout.setHorizontalGroup(
            TransferPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Transfer, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
            .addComponent(TransferIcon, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        TransferPageLayout.setVerticalGroup(
            TransferPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TransferPageLayout.createSequentialGroup()
                .addComponent(TransferIcon, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Transfer, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        ChangePassPage.setBackground(new java.awt.Color(255, 255, 255));
        ChangePassPage.setPreferredSize(new java.awt.Dimension(136, 136));
        ChangePassPage.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ChangePassPageMouseClicked(evt);
            }
        });

        ChangePass.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        ChangePass.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ChangePass.setText("Đổi Mật Khẩu");

        ChangePassIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ChangePassIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/reset-password.png"))); // NOI18N

        javax.swing.GroupLayout ChangePassPageLayout = new javax.swing.GroupLayout(ChangePassPage);
        ChangePassPage.setLayout(ChangePassPageLayout);
        ChangePassPageLayout.setHorizontalGroup(
            ChangePassPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ChangePass, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
            .addComponent(ChangePassIcon, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        ChangePassPageLayout.setVerticalGroup(
            ChangePassPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ChangePassPageLayout.createSequentialGroup()
                .addComponent(ChangePassIcon, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ChangePass, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        DiaryPage.setBackground(new java.awt.Color(255, 255, 255));

        DiaryIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        DiaryIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/noteIcon.png"))); // NOI18N

        Diary.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        Diary.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Diary.setText("Nhật ký");
        Diary.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        javax.swing.GroupLayout DiaryPageLayout = new javax.swing.GroupLayout(DiaryPage);
        DiaryPage.setLayout(DiaryPageLayout);
        DiaryPageLayout.setHorizontalGroup(
            DiaryPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Diary, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
            .addComponent(DiaryIcon, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        DiaryPageLayout.setVerticalGroup(
            DiaryPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, DiaryPageLayout.createSequentialGroup()
                .addComponent(DiaryIcon, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Diary))
        );

        InformaitionPage.setBackground(new java.awt.Color(255, 255, 255));
        InformaitionPage.setPreferredSize(new java.awt.Dimension(136, 136));

        InforIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        InforIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/information.png"))); // NOI18N

        Infomation.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        Infomation.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Infomation.setText("Thông Tin");

        javax.swing.GroupLayout InformaitionPageLayout = new javax.swing.GroupLayout(InformaitionPage);
        InformaitionPage.setLayout(InformaitionPageLayout);
        InformaitionPageLayout.setHorizontalGroup(
            InformaitionPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(InforIcon, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(Infomation, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
        );
        InformaitionPageLayout.setVerticalGroup(
            InformaitionPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, InformaitionPageLayout.createSequentialGroup()
                .addComponent(InforIcon, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Infomation))
        );

        DecentralizationPage.setBackground(new java.awt.Color(255, 255, 255));
        DecentralizationPage.setPreferredSize(new java.awt.Dimension(136, 136));

        DecentralizationIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        DecentralizationIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/decentralization.png"))); // NOI18N

        Decentralization.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        Decentralization.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Decentralization.setText("Phân Quyền");

        javax.swing.GroupLayout DecentralizationPageLayout = new javax.swing.GroupLayout(DecentralizationPage);
        DecentralizationPage.setLayout(DecentralizationPageLayout);
        DecentralizationPageLayout.setHorizontalGroup(
            DecentralizationPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Decentralization, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
            .addComponent(DecentralizationIcon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        DecentralizationPageLayout.setVerticalGroup(
            DecentralizationPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, DecentralizationPageLayout.createSequentialGroup()
                .addComponent(DecentralizationIcon, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                .addGap(6, 6, 6)
                .addComponent(Decentralization, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout SystemBoxLayout = new javax.swing.GroupLayout(SystemBox);
        SystemBox.setLayout(SystemBoxLayout);
        SystemBoxLayout.setHorizontalGroup(
            SystemBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SystemBoxLayout.createSequentialGroup()
                .addGap(289, 289, 289)
                .addGroup(SystemBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(BackupPage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(InformaitionPage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addGroup(SystemBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(SystemBoxLayout.createSequentialGroup()
                        .addComponent(DecentralizationPage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(ChangePassPage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(DiaryPage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(SystemBoxLayout.createSequentialGroup()
                        .addComponent(RecoverPage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(RepairPage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(TransferPage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(285, 285, 285))
        );
        SystemBoxLayout.setVerticalGroup(
            SystemBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SystemBoxLayout.createSequentialGroup()
                .addGap(303, 303, 303)
                .addGroup(SystemBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(InformaitionPage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(DecentralizationPage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ChangePassPage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(DiaryPage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addGroup(SystemBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(RecoverPage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BackupPage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(RepairPage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TransferPage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(303, 303, 303))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(SystemBox, javax.swing.GroupLayout.DEFAULT_SIZE, 1241, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(SystemBox, javax.swing.GroupLayout.DEFAULT_SIZE, 918, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void ChangePassPageMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ChangePassPageMouseClicked
        if (mainMenu != null) {
            mainMenu.showChangePasswordPanel(); // Gọi phương thức trong Menu
        }
    }//GEN-LAST:event_ChangePassPageMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Backup;
    private javax.swing.JLabel BackupIcon;
    private javax.swing.JPanel BackupPage;
    private javax.swing.JLabel ChangePass;
    private javax.swing.JLabel ChangePassIcon;
    private javax.swing.JPanel ChangePassPage;
    private javax.swing.JLabel Decentralization;
    private javax.swing.JLabel DecentralizationIcon;
    private javax.swing.JPanel DecentralizationPage;
    private javax.swing.JLabel Diary;
    private javax.swing.JLabel DiaryIcon;
    private javax.swing.JPanel DiaryPage;
    private javax.swing.JLabel Infomation;
    private javax.swing.JLabel InforIcon;
    private javax.swing.JPanel InformaitionPage;
    private javax.swing.JLabel Recover;
    private javax.swing.JLabel RecoverIcon;
    private javax.swing.JPanel RecoverPage;
    private javax.swing.JLabel Repair;
    private javax.swing.JLabel RepairIcon;
    private javax.swing.JPanel RepairPage;
    private javax.swing.JPanel SystemBox;
    private javax.swing.JLabel Transfer;
    private javax.swing.JLabel TransferIcon;
    private javax.swing.JPanel TransferPage;
    // End of variables declaration//GEN-END:variables
}
