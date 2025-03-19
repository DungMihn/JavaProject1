/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.retail.view;

import com.retail.controller.EmployeeController;
import com.retail.controller.InventoryController;
import com.retail.controller.StockEntryController;
import com.retail.controller.StockEntryDetailController;
import com.retail.controller.SupplierController;
import com.retail.model.ComboBoxItem;
import com.retail.model.StockEntry;
import com.retail.model.Inventory;
import com.retail.model.StockEntryDetail;
import com.retail.model.Supplier;
import com.retail.model.Employee;
import com.retail.model.StockEntryDetailChange;
import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.UnsupportedEncodingException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UnsupportedLookAndFeelException;
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
    private EmployeeController employeeController;
    private int stockEntryId;
    private DefaultTableModel stockEntryDetailTableModel;
    private List<StockEntryDetailChange> pendingChanges = new ArrayList<>();
    private JDateChooser dateChooser;
    private int supplierId;

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
        employeeController = new EmployeeController();

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

            loadSuppliersIntoComboBox();
            setSupplierComboBox(stockEntry);
            loadEmployeesIntoComboBox();
            setEmployeeComboBox(stockEntry);

            // Hiển thị ngày nhập từ stockEntry vào entryDateTextField
            String entryDateString = stockEntry.getEntryDate(); // Lấy ngày dưới dạng String
            if (entryDateString != null && !entryDateString.isEmpty()) {
                entryDateTextField.setText(entryDateString); // Hiển thị ngày trong entryDateTextField
            }
            loadStockEntryDetailsIntoTable();
        }

        // Khởi tạo JDateChooser
        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("dd/MM/yyyy"); // Định dạng ngày

        entryDateTextField.setFocusable(false);

    }

    private void showDatePickerDialog() {
        // Tạo JDialog để chứa JDateChooser
        JDialog datePickerDialog = new JDialog(this, "Chọn ngày", true);
        datePickerDialog.setSize(300, 200);
        datePickerDialog.setLocationRelativeTo(this); // Hiển thị ở giữa cửa sổ cha

        // Tạo JDateChooser
        JDateChooser dateChooserr = new JDateChooser();
        dateChooserr.setDateFormatString("dd/MM/yyyy"); // Định dạng ngày

        // Nút "OK" để xác nhận chọn ngày
        JButton okButton = new JButton("OK");
        okButton.addActionListener((ActionEvent e) -> {
            Date selectedDate = dateChooserr.getDate();
            if (selectedDate != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                entryDateTextField.setText(dateFormat.format(selectedDate)); // Cập nhật ngày vào entryDateTextField
            }
            datePickerDialog.dispose(); // Đóng dialog
        });

        // Thêm JDateChooser và nút OK vào dialog
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(dateChooserr, BorderLayout.CENTER);
        panel.add(okButton, BorderLayout.SOUTH);

        datePickerDialog.add(panel);
        datePickerDialog.setVisible(true); // Hiển thị dialog
    }

    private void setSupplierComboBox(StockEntry stockEntry) {
        for (int i = 0; i < supplierComboBox.getItemCount(); i++) {
            ComboBoxItem item = (ComboBoxItem) supplierComboBox.getItemAt(i);
            if (item.getId() == stockEntry.getSupplierId()) {
                supplierComboBox.setSelectedIndex(i);
                break;
            }
        }
    }

    private void setEmployeeComboBox(StockEntry stockEntry) {
        for (int i = 0; i < employeeComboBox.getItemCount(); i++) {
            ComboBoxItem item = (ComboBoxItem) employeeComboBox.getItemAt(i);
            if (item.getId() == stockEntry.getEmployeeId()) {
                employeeComboBox.setSelectedIndex(i);
                break;
            }
        }
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

    private void loadEmployeesIntoComboBox() {
        List<Employee> employees = employeeController.getAllEmployees();
        employeeComboBox.removeAllItems();

        for (Employee employee : employees) {
            employeeComboBox.addItem(new ComboBoxItem(employee.getEmployeeId(), employee.getName()));
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

        jDialog1 = new javax.swing.JDialog();
        jPanel2 = new javax.swing.JPanel();
        cancelBtn = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        supplierComboBox = new javax.swing.JComboBox<>();
        editStockEntryBtn = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        stockEntryDetailTable = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        deleteStockEntryDetail = new javax.swing.JButton();
        employeeComboBox = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        entryDateTextField = new javax.swing.JFormattedTextField();
        openCalendarButton = new javax.swing.JButton();
        addStockEntryDetail = new javax.swing.JButton();

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

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
                .addContainerGap(196, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(182, 182, 182))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel1)
                .addContainerGap(21, Short.MAX_VALUE))
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

        deleteStockEntryDetail.setBackground(new java.awt.Color(255, 0, 0));
        deleteStockEntryDetail.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        deleteStockEntryDetail.setForeground(new java.awt.Color(255, 255, 255));
        deleteStockEntryDetail.setText("Xóa sản phẩm");
        deleteStockEntryDetail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteStockEntryDetailActionPerformed(evt);
            }
        });

        employeeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employeeComboBoxActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 153, 51));
        jLabel11.setText("Ngày nhập");

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        entryDateTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(java.text.DateFormat.getDateInstance(java.text.DateFormat.SHORT))));
        entryDateTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                entryDateTextFieldActionPerformed(evt);
            }
        });

        openCalendarButton.setText("...");
        openCalendarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openCalendarButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 194, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGap(18, 18, 18)
                    .addComponent(entryDateTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(1, 1, 1)
                    .addComponent(openCalendarButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(19, Short.MAX_VALUE)))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGap(13, 13, 13)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(entryDateTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(openCalendarButton))
                    .addContainerGap(14, Short.MAX_VALUE)))
        );

        addStockEntryDetail.setBackground(new java.awt.Color(153, 51, 255));
        addStockEntryDetail.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        addStockEntryDetail.setForeground(new java.awt.Color(255, 255, 255));
        addStockEntryDetail.setText("Thêm sản phẩm");
        addStockEntryDetail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addStockEntryDetailActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(cancelBtn)
                        .addGap(134, 134, 134)
                        .addComponent(deleteStockEntryDetail)
                        .addGap(31, 31, 31)
                        .addComponent(addStockEntryDetail)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(editStockEntryBtn)
                        .addGap(41, 41, 41))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 631, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(264, 264, 264)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(224, 224, 224))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 56, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(supplierComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(38, 38, 38)
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(employeeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(26, 26, 26))))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(93, 93, 93)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(supplierComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(82, 82, 82)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(9, 9, 9)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(employeeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editStockEntryBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(deleteStockEntryDetail)
                    .addComponent(addStockEntryDetail))
                .addGap(97, 97, 97))
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
        ComboBoxItem selectedEmployee = (ComboBoxItem) employeeComboBox.getSelectedItem();
        supplierId = selectedSupplier.getId();
        int employeeId = selectedEmployee.getId();
        String entryDateString = entryDateTextField.getText();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Chuyển đổi String thành LocalDate
        LocalDate localDate = LocalDate.parse(entryDateString, formatter);

        // Chuyển LocalDate thành LocalDateTime (mặc định giờ là 00:00:00)
        LocalDateTime entryDate = localDate.atStartOfDay();

        // Cập nhật thông tin StockEntry
        stockEntry.setSupplierId(supplierId);
        stockEntry.setEmployeeId(employeeId);
        stockEntry.setEntryDate(entryDate);
        boolean isStockEntryUpdated = stockEntryController.updateStockEntry(stockEntry);

        if (isStockEntryUpdated) {
            // Xử lý các thay đổi tạm thời
            for (StockEntryDetailChange change : pendingChanges) {
                if (change.getAction().equals("DELETE")) {
                    // Xóa bản ghi khỏi cơ sở dữ liệu (đặt quantity = 0)
                    boolean isDeleted = stockEntryDetailController.deleteStockEntryDetailByProductId(stockEntryId, change.getProductId());

                    if (isDeleted) {
                        // Cập nhật số lượng tồn kho (giảm đi số lượng đã xóa)
                        inventoryController.updateStockQuantity(change.getProductId(), change.getQuantity());
                    } else {
                        System.out.println("❌ Lỗi khi xóa chi tiết nhập kho cho Product ID: " + change.getProductId());
                    }
                }
            }

            // Xóa danh sách thay đổi tạm thời sau khi xử lý
            pendingChanges.clear();

            // Cập nhật thông tin chi tiết nhập kho (nếu có thay đổi)
            boolean isDetailsUpdated = updateStockEntryDetails();

            if (isDetailsUpdated) {
                JOptionPane.showMessageDialog(this, "Cập nhật thông tin nhập kho thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);

                // Xuất lại file PDF và hiển thị cho người dùng
                generateAndOpenStockEntryReport();
            } 
//            else {
//                JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật chi tiết nhập kho!", "Lỗi", JOptionPane.ERROR_MESSAGE);
//            }
        } else {
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật thông tin nhập kho!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_editStockEntryBtnActionPerformed

    private void deleteStockEntryDetailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteStockEntryDetailActionPerformed
        // Lấy hàng được chọn trong bảng
        int row = stockEntryDetailTable.getSelectedRow();

        // Kiểm tra xem có hàng nào được chọn không
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng để xóa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Lấy thông tin từ bảng
        int productId = Integer.parseInt(stockEntryDetailTable.getValueAt(row, 0).toString());
        int quantity = Integer.parseInt(stockEntryDetailTable.getValueAt(row, 2).toString());

        // Kiểm tra số lượng tồn kho trước khi xóa
        if (!isStockQuantitySufficient(productId, -quantity)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Không đủ số lượng tồn kho để xóa sản phẩm có ID: " + productId,
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
            return; // Không thực hiện xóa nếu không đủ số lượng
        }

        // Xác nhận xóa
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn xóa dòng này không?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            // Thêm vào danh sách thay đổi tạm thời
            pendingChanges.add(new StockEntryDetailChange(productId, quantity, "DELETE"));

            // Xóa dòng khỏi bảng (tạm thời)
            ((DefaultTableModel) stockEntryDetailTable.getModel()).removeRow(row);

            JOptionPane.showMessageDialog(this, "Đã xóa dòng dữ liệu tạm thời!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_deleteStockEntryDetailActionPerformed

    private void employeeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employeeComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_employeeComboBoxActionPerformed

    private void entryDateTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_entryDateTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_entryDateTextFieldActionPerformed

    private void openCalendarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openCalendarButtonActionPerformed

        showDatePickerDialog();
    }//GEN-LAST:event_openCalendarButtonActionPerformed

    private void addStockEntryDetailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addStockEntryDetailActionPerformed
        ComboBoxItem selectedSupplier = (ComboBoxItem) supplierComboBox.getSelectedItem();
        supplierId = selectedSupplier.getId();
        // Tạo và hiển thị AddStockEntryDetailFrame
        AddStockEntryDetailFrame addStockEntryDetailFrame = new AddStockEntryDetailFrame(this, supplierId);
        addStockEntryDetailFrame.setVisible(true);

        // Lắng nghe sự kiện khi người dùng thêm sản phẩm
        addStockEntryDetailFrame.setAddStockEntryDetailListener((StockEntryDetail detail) -> {
            // Thêm dữ liệu vào bảng tạm thời
            DefaultTableModel model = (DefaultTableModel) stockEntryDetailTable.getModel();
            model.addRow(new Object[]{
                detail.getProductId(),
                detail.getProductName(),
                detail.getQuantity(),
                detail.getPurchasePrice()
            });
        });
    }//GEN-LAST:event_addStockEntryDetailActionPerformed

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

        // Danh sách các sản phẩm cần xóa
        List<Integer> productsToDelete = new ArrayList<>();

        int rowCount = stockEntryDetailTableModel.getRowCount();

        for (int i = 0; i < rowCount; i++) {
            try {
                Object productIdObj = stockEntryDetailTableModel.getValueAt(i, 0);
                Object productNameObj = stockEntryDetailTableModel.getValueAt(i, 1);
                Object quantityObj = stockEntryDetailTableModel.getValueAt(i, 2);
                Object priceObj = stockEntryDetailTableModel.getValueAt(i, 3);

                if (productIdObj == null || quantityObj == null || priceObj == null) {
                    System.out.println("❌ Giá trị tại hàng " + i + " bị thiếu. Hủy bỏ cập nhật.");
                    return false; // ⛔ Hủy bỏ nếu có lỗi
                }

                String productName = productNameObj.toString();
                int productId = Integer.parseInt(productIdObj.toString());
                int newQuantity = Integer.parseInt(quantityObj.toString());
                double newPrice = Double.parseDouble(priceObj.toString());

                if (productsToDelete.contains(productId)) {
                    if (!isStockQuantitySufficient(productId, -newQuantity)) {
                        JOptionPane.showMessageDialog(this, "Không đủ số lượng tồn kho để xóa sản phẩm: " + productName, "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return false; // ⛔ Hủy bỏ nếu không đủ số lượng
                    }

                    boolean isDeleted = stockEntryDetailController.deleteStockEntryDetailByProductId(stockEntryId, productId);
                    if (!isDeleted) {
                        System.out.println("❌ Lỗi khi xóa chi tiết nhập kho cho Product ID: " + productId);
                        return false; // ⛔ Hủy bỏ nếu xóa thất bại
                    }

                    inventoryController.updateStockQuantity(productId, -newQuantity);
                    System.out.println("✅ Đã xóa chi tiết nhập kho cho Product ID: " + productId);
                    continue;
                }

                StockEntryDetail oldDetail = stockEntryDetailController.getStockEntryDetailByStockEntryIdAndProductId(stockEntryId, productId);
                if (oldDetail == null) {
                    System.out.println("❌ Không tìm thấy chi tiết nhập kho cho Product ID: " + productId);
                    return false; // ⛔ Hủy bỏ nếu không tìm thấy dữ liệu
                }

                int quantityChange = newQuantity - oldDetail.getQuantity();

                if (quantityChange < 0 && !isStockQuantitySufficient(productId, quantityChange)) {
                    JOptionPane.showMessageDialog(this, "Không đủ số lượng tồn kho để cập nhật cho sản phẩm: " + productName, "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return false; // ⛔ Hủy bỏ nếu không đủ hàng tồn kho
                }

                oldDetail.setQuantity(newQuantity);
                oldDetail.setPurchasePrice(newPrice);
                boolean isDetailUpdated = stockEntryDetailController.updateStockEntryDetail(oldDetail);

                if (!isDetailUpdated) {
                    System.out.println("❌ Lỗi khi cập nhật chi tiết nhập kho cho Product ID: " + productId);
                    return false; // ⛔ Hủy bỏ nếu cập nhật thất bại
                }

                boolean isInventoryUpdated = inventoryController.updateStockQuantity(productId, quantityChange);
                if (!isInventoryUpdated) {
                    System.out.println("❌ Lỗi khi cập nhật số lượng tồn kho cho Product ID: " + productId);
                    return false; // ⛔ Hủy bỏ nếu cập nhật tồn kho thất bại
                }

            } catch (NumberFormatException e) {
                System.out.println("❌ Lỗi chuyển đổi số tại hàng " + i + ": " + e.getMessage());
                return false; // ⛔ Hủy bỏ nếu lỗi định dạng số
            } catch (HeadlessException e) {
                System.out.println("❌ Lỗi không xác định tại hàng " + i + ": " + e.getMessage());
                return false; // ⛔ Hủy bỏ nếu có lỗi hệ thống
            }
        }

        return true; // ✅ Chỉ return `true` nếu tất cả các hàng đều thành công
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
     * @throws javax.swing.UnsupportedLookAndFeelException
     */
    public static void main(String args[]) throws UnsupportedLookAndFeelException {
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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EditStockEntryFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        //</editor-fold>

        /* Create and display the form */
        try {
            System.setOut(new java.io.PrintStream(System.out, true, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            System.out.println("Lỗi khi đặt encoding UTF-8!");
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addStockEntryDetail;
    private javax.swing.JButton cancelBtn;
    private javax.swing.JButton deleteStockEntryDetail;
    private javax.swing.JButton editStockEntryBtn;
    private javax.swing.JComboBox<ComboBoxItem> employeeComboBox;
    private javax.swing.JFormattedTextField entryDateTextField;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton openCalendarButton;
    private javax.swing.JTable stockEntryDetailTable;
    private javax.swing.JComboBox<ComboBoxItem> supplierComboBox;
    // End of variables declaration//GEN-END:variables
}
