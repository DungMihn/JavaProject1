/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.retail.view;

import com.retail.controller.SupplierController;
import com.retail.model.Supplier;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.UnsupportedEncodingException;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Admin
 */
public class SupplierPanel extends javax.swing.JPanel {

    private final SupplierController supplierController;
    private final DefaultTableModel supplierTableModel;
    private int nextSupplierId;
    /**
     * Creates new form SupplierPanel
     */
    public SupplierPanel() {
        initComponents();

        supplierController = new SupplierController();
        supplierTableModel = (DefaultTableModel) supplierTable.getModel();

        supplierTable.setModel(supplierTableModel);

        // Thêm các cột vào bảng
        supplierTableModel.addColumn("Mã");
        supplierTableModel.addColumn("Tên NCC");
        supplierTableModel.addColumn("Người liên lệ");
        supplierTableModel.addColumn("SĐT");
        supplierTableModel.addColumn("Email");
        supplierTableModel.addColumn("Địa chỉ");
        // Tải dữ liệu nhà cung cấp vào bảng
        loadSuppliersIntoTable();
        
        // Khởi tạo dữ liệu ban đầu
        initializeData();


        // Thêm DocumentListener để lắng nghe sự kiện nhập liệu
        searchSupplierNameTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterSuppliersByName(); // Gọi khi có ký tự được thêm vào
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterSuppliersByName();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // Không cần xử lý trong trường hợp này
            }
        });

        // Thêm sự kiện click chuột vào bảng
        supplierTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                supplierTableMouseClicked(evt);
            }
        });
    }
    
     private void initializeData() {
        // Lấy ID tiếp theo của Supplier
        nextSupplierId = supplierController.getNextSupplierId();
        if (nextSupplierId == -1) {
            JOptionPane.showMessageDialog(this, "Không thể lấy ID tiếp theo của Supplier!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Hiển thị ID tiếp theo lên giao diện
        supplierIdTextField.setText(String.valueOf(nextSupplierId));

        // Đặt lại các trường nhập liệu về giá trị mặc định
        clearFields();
    }

    private void supplierTableMouseClicked(MouseEvent evt) {
        int selectedRow = supplierTable.getSelectedRow();
        if (selectedRow >= 0) { // Đảm bảo có hàng được chọn
            // Lấy dữ liệu từ hàng được chọn
            int supplierId = (int) supplierTable.getValueAt(selectedRow, 0);
            String name = (String) supplierTable.getValueAt(selectedRow, 1);
            String contactName = (String) supplierTable.getValueAt(selectedRow, 2);
            String phone = (String) supplierTable.getValueAt(selectedRow, 3);
            String email = (String) supplierTable.getValueAt(selectedRow, 4);
            String address = (String) supplierTable.getValueAt(selectedRow, 5);

            // Điền dữ liệu vào các trường nhập liệu
            supplierIdTextField.setText(String.valueOf(supplierId));
            supplierNameTextField.setText(name);
            contactNameTextField.setText(contactName);
            phoneTextField.setText(phone);
            emailTextField.setText(email);
            addressTextField.setText(address);
        }
    }

    private void loadSuppliersIntoTable() {
        supplierTableModel.setRowCount(0); // Xóa dữ liệu cũ

        List<Supplier> suppliers = supplierController.getAllSuppliers();
        for (Supplier supplier : suppliers) {
            supplierTableModel.addRow(new Object[]{
                supplier.getSupplierId(),
                supplier.getName(),
                supplier.getContactName(),
                supplier.getPhone(),
                supplier.getEmail(),
                supplier.getAddress()
            });
        }
    }

    private void clearFields() {
        supplierNameTextField.setText("");
        contactNameTextField.setText("");
        phoneTextField.setText("");
        emailTextField.setText("");
        addressTextField.setText("");
    }

    private void filterSuppliersByName() {
        String keyword = searchSupplierNameTextField.getText().trim();
        supplierTableModel.setRowCount(0); // Xóa dữ liệu cũ

        List<Supplier> suppliers = supplierController.searchSuppliersByName(keyword);
        for (Supplier supplier : suppliers) {
            supplierTableModel.addRow(new Object[]{
                supplier.getSupplierId(),
                supplier.getName(),
                supplier.getContactName(),
                supplier.getPhone(),
                supplier.getEmail(),
                supplier.getAddress()
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

        boxProducts = new javax.swing.JPanel();
        ManageProducts = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        TableProducts = new javax.swing.JScrollPane();
        supplierTable = new javax.swing.JTable();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel2 = new javax.swing.JPanel();
        deleteSupplierBtn = new javax.swing.JButton();
        showInforSupplierBtn = new javax.swing.JButton();
        editSupplierBtn = new javax.swing.JButton();
        addSupplierBtn = new javax.swing.JButton();
        cancelBtn = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        Name = new javax.swing.JLabel();
        searchSupplierNameTextField = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        ProdID4 = new javax.swing.JLabel();
        ProdID5 = new javax.swing.JLabel();
        ProdID6 = new javax.swing.JLabel();
        supplierIdTextField = new javax.swing.JTextField();
        supplierNameTextField = new javax.swing.JTextField();
        contactNameTextField = new javax.swing.JTextField();
        ProdID7 = new javax.swing.JLabel();
        phoneTextField = new javax.swing.JTextField();
        ProdID8 = new javax.swing.JLabel();
        emailTextField = new javax.swing.JTextField();
        ProdID9 = new javax.swing.JLabel();
        addressTextField = new javax.swing.JTextField();

        boxProducts.setBackground(new java.awt.Color(255, 255, 255));
        boxProducts.setRequestFocusEnabled(false);

        ManageProducts.setFont(new java.awt.Font("Candara", 1, 18)); // NOI18N
        ManageProducts.setForeground(new java.awt.Color(255, 102, 51));
        ManageProducts.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ManageProducts.setText("QUẢN LÝ NHÀ CUNG CẤP");

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Nhà cung cấp", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 18), new java.awt.Color(255, 102, 0))); // NOI18N
        jPanel3.setForeground(new java.awt.Color(255, 102, 0));

        TableProducts.setBackground(new java.awt.Color(255, 255, 255));

        supplierTable.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        supplierTable.setRowHeight(25);
        supplierTable.setSelectionForeground(new java.awt.Color(255, 102, 0));
        TableProducts.setViewportView(supplierTable);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(TableProducts)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(10, Short.MAX_VALUE)
                .addComponent(TableProducts, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Chức năng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(255, 102, 0))); // NOI18N
        jPanel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        deleteSupplierBtn.setFont(new java.awt.Font("Candara", 1, 16)); // NOI18N
        deleteSupplierBtn.setForeground(new java.awt.Color(255, 102, 0));
        deleteSupplierBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/deleteIcon.png"))); // NOI18N
        deleteSupplierBtn.setText("Xóa");
        deleteSupplierBtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 102, 0)));
        deleteSupplierBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteSupplierBtnActionPerformed(evt);
            }
        });

        showInforSupplierBtn.setFont(new java.awt.Font("Candara", 1, 16)); // NOI18N
        showInforSupplierBtn.setForeground(new java.awt.Color(255, 102, 0));
        showInforSupplierBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/viewIcon.png"))); // NOI18N
        showInforSupplierBtn.setText("Xem chi tiết");
        showInforSupplierBtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 102, 0)));
        showInforSupplierBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showInforSupplierBtnActionPerformed(evt);
            }
        });

        editSupplierBtn.setFont(new java.awt.Font("Candara", 1, 16)); // NOI18N
        editSupplierBtn.setForeground(new java.awt.Color(255, 102, 0));
        editSupplierBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/editIcon.png"))); // NOI18N
        editSupplierBtn.setText("Chỉnh sửa");
        editSupplierBtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 102, 0)));
        editSupplierBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editSupplierBtnActionPerformed(evt);
            }
        });

        addSupplierBtn.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        addSupplierBtn.setForeground(new java.awt.Color(255, 102, 0));
        addSupplierBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/addIco.png"))); // NOI18N
        addSupplierBtn.setText("Thêm NCC");
        addSupplierBtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 102, 0)));
        addSupplierBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addSupplierBtnActionPerformed(evt);
            }
        });

        cancelBtn.setFont(new java.awt.Font("Candara", 1, 16)); // NOI18N
        cancelBtn.setForeground(new java.awt.Color(255, 102, 0));
        cancelBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cancelIcon.png"))); // NOI18N
        cancelBtn.setText("Hủy");
        cancelBtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 102, 0)));
        cancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(addSupplierBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(deleteSupplierBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(editSupplierBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(showInforSupplierBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(addSupplierBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
                        .addGap(4, 4, 4))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(deleteSupplierBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(showInforSupplierBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(editSupplierBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tìm kiếm ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(255, 102, 0))); // NOI18N
        jPanel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        Name.setBackground(new java.awt.Color(255, 102, 0));
        Name.setFont(new java.awt.Font("Candara", 1, 16)); // NOI18N
        Name.setForeground(new java.awt.Color(255, 102, 51));
        Name.setText("Tên NCC");

        searchSupplierNameTextField.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        searchSupplierNameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchSupplierNameTextFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(Name)
                .addGap(18, 18, 18)
                .addComponent(searchSupplierNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 431, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(86, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(searchSupplierNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Name))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        ProdID4.setBackground(new java.awt.Color(255, 102, 0));
        ProdID4.setFont(new java.awt.Font("Candara", 1, 16)); // NOI18N
        ProdID4.setForeground(new java.awt.Color(255, 102, 51));
        ProdID4.setText("Mã NCC");

        ProdID5.setBackground(new java.awt.Color(255, 102, 0));
        ProdID5.setFont(new java.awt.Font("Candara", 1, 16)); // NOI18N
        ProdID5.setForeground(new java.awt.Color(255, 102, 51));
        ProdID5.setText("Tên NCC");

        ProdID6.setBackground(new java.awt.Color(255, 102, 0));
        ProdID6.setFont(new java.awt.Font("Candara", 1, 16)); // NOI18N
        ProdID6.setForeground(new java.awt.Color(255, 102, 51));
        ProdID6.setText("SĐT");

        supplierIdTextField.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        supplierIdTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supplierIdTextFieldActionPerformed(evt);
            }
        });

        supplierNameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supplierNameTextFieldActionPerformed(evt);
            }
        });

        ProdID7.setBackground(new java.awt.Color(255, 102, 0));
        ProdID7.setFont(new java.awt.Font("Candara", 1, 16)); // NOI18N
        ProdID7.setForeground(new java.awt.Color(255, 102, 51));
        ProdID7.setText("Email");

        ProdID8.setBackground(new java.awt.Color(255, 102, 0));
        ProdID8.setFont(new java.awt.Font("Candara", 1, 16)); // NOI18N
        ProdID8.setForeground(new java.awt.Color(255, 102, 51));
        ProdID8.setText("Tên người liên hệ");

        ProdID9.setBackground(new java.awt.Color(255, 102, 0));
        ProdID9.setFont(new java.awt.Font("Candara", 1, 16)); // NOI18N
        ProdID9.setForeground(new java.awt.Color(255, 102, 51));
        ProdID9.setText("Địa chỉ");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                        .addComponent(ProdID7, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(emailTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ProdID8, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ProdID4, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(supplierIdTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE)
                            .addComponent(contactNameTextField))))
                .addGap(69, 69, 69)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(ProdID5, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(supplierNameTextField))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(ProdID6, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(phoneTextField))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(ProdID9, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addressTextField)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(supplierIdTextField)
                    .addComponent(ProdID4, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ProdID5, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(supplierNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ProdID6, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(contactNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(phoneTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ProdID8, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(ProdID7, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(68, 68, 68))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(emailTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ProdID9, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(addressTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(74, 74, 74))))
        );

        javax.swing.GroupLayout boxProductsLayout = new javax.swing.GroupLayout(boxProducts);
        boxProducts.setLayout(boxProductsLayout);
        boxProductsLayout.setHorizontalGroup(
            boxProductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, boxProductsLayout.createSequentialGroup()
                .addGroup(boxProductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(boxProductsLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(boxProductsLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(ManageProducts, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, boxProductsLayout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 1135, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, boxProductsLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(43, 43, 43)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        boxProductsLayout.setVerticalGroup(
            boxProductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(boxProductsLayout.createSequentialGroup()
                .addComponent(ManageProducts, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(boxProductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(145, 145, 145))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1461, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(boxProducts, javax.swing.GroupLayout.DEFAULT_SIZE, 1449, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 787, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(boxProducts, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void deleteSupplierBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteSupplierBtnActionPerformed
        int selectedRow = supplierTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một hàng để xóa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int supplierId = (int) supplierTable.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Bạn có chắc chắn muốn xóa nhà cung cấp này không?",
            "Xác nhận xóa",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            boolean isDeleted = supplierController.deleteSupplier(supplierId);
            if (isDeleted) {
                JOptionPane.showMessageDialog(this, "Xóa nhà cung cấp thành công!");
                loadSuppliersIntoTable(); // Tải lại dữ liệu
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Không thể xóa nhà cung cấp vì có dữ liệu liên quan trong các bảng khác!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_deleteSupplierBtnActionPerformed

    private void showInforSupplierBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showInforSupplierBtnActionPerformed
        int selectedRow = supplierTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một hàng để xem thông tin!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int supplierId = (int) supplierTable.getValueAt(selectedRow, 0);
        Supplier supplier = supplierController.getSupplierById(supplierId);

        if (supplier != null) {
            String message = "Thông tin chi tiết:\n"
            + "Mã NCC: " + supplier.getSupplierId() + "\n"
            + "Tên NCC: " + supplier.getName() + "\n"
            + "Tên người liên hệ: " + supplier.getContactName() + "\n"
            + "SĐT: " + supplier.getPhone() + "\n"
            + "Email: " + supplier.getEmail() + "\n"
            + "Địa chỉ: " + supplier.getAddress();
            JOptionPane.showMessageDialog(this, message, "Thông tin nhà cung cấp", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin nhà cung cấp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_showInforSupplierBtnActionPerformed

    private void editSupplierBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editSupplierBtnActionPerformed
        int selectedRow = supplierTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một hàng để chỉnh sửa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int supplierId = (int) supplierTable.getValueAt(selectedRow, 0);
        String name = supplierNameTextField.getText().trim();
        String contactName = contactNameTextField.getText().trim();
        String phone = phoneTextField.getText().trim();
        String email = emailTextField.getText().trim();
        String address = addressTextField.getText().trim();

        if (name.isEmpty() || contactName.isEmpty() || phone.isEmpty() || email.isEmpty() || address.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Supplier updatedSupplier = new Supplier();
        updatedSupplier.setSupplierId(supplierId);
        updatedSupplier.setName(name);
        updatedSupplier.setContactName(contactName);
        updatedSupplier.setPhone(phone);
        updatedSupplier.setEmail(email);
        updatedSupplier.setAddress(address);

        boolean isUpdated = supplierController.updateSupplier(updatedSupplier);
        if (isUpdated) {
            JOptionPane.showMessageDialog(this, "Cập nhật thông tin nhà cung cấp thành công!");
            loadSuppliersIntoTable(); // Tải lại dữ liệu
            clearFields(); // Xóa các trường nhập liệu
        } else {
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật thông tin nhà cung cấp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_editSupplierBtnActionPerformed

    private void addSupplierBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addSupplierBtnActionPerformed
        String name = supplierNameTextField.getText().trim();
        String contactName = contactNameTextField.getText().trim();
        String phone = phoneTextField.getText().trim();
        String email = emailTextField.getText().trim();
        String address = addressTextField.getText().trim();

        if (name.isEmpty() || contactName.isEmpty() || phone.isEmpty() || email.isEmpty() || address.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Supplier newSupplier = new Supplier();
        newSupplier.setName(name);
        newSupplier.setContactName(contactName);
        newSupplier.setPhone(phone);
        newSupplier.setEmail(email);
        newSupplier.setAddress(address);

        boolean success = supplierController.addSupplier(newSupplier);
        if (success) {
            JOptionPane.showMessageDialog(this, "Thêm nhà cung cấp thành công!");
            loadSuppliersIntoTable(); // Tải lại dữ liệu
            clearFields(); // Xóa các trường nhập liệu
        } else {
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm nhà cung cấp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_addSupplierBtnActionPerformed

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
        clearFields();
    }//GEN-LAST:event_cancelBtnActionPerformed

    private void searchSupplierNameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchSupplierNameTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchSupplierNameTextFieldActionPerformed

    private void supplierIdTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supplierIdTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_supplierIdTextFieldActionPerformed

    private void supplierNameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supplierNameTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_supplierNameTextFieldActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel ManageProducts;
    private javax.swing.JLabel Name;
    private javax.swing.JLabel ProdID4;
    private javax.swing.JLabel ProdID5;
    private javax.swing.JLabel ProdID6;
    private javax.swing.JLabel ProdID7;
    private javax.swing.JLabel ProdID8;
    private javax.swing.JLabel ProdID9;
    private javax.swing.JScrollPane TableProducts;
    private javax.swing.JButton addSupplierBtn;
    private javax.swing.JTextField addressTextField;
    private javax.swing.JPanel boxProducts;
    private javax.swing.JButton cancelBtn;
    private javax.swing.JTextField contactNameTextField;
    private javax.swing.JButton deleteSupplierBtn;
    private javax.swing.JButton editSupplierBtn;
    private javax.swing.JTextField emailTextField;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField phoneTextField;
    private javax.swing.JTextField searchSupplierNameTextField;
    private javax.swing.JButton showInforSupplierBtn;
    private javax.swing.JTextField supplierIdTextField;
    private javax.swing.JTextField supplierNameTextField;
    private javax.swing.JTable supplierTable;
    // End of variables declaration//GEN-END:variables
}
