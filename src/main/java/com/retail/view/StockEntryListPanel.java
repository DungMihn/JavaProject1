/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.retail.view;

import com.retail.controller.StockEntryController;
import com.retail.controller.SupplierController;
import com.retail.dao.DatabaseConnection;
import com.retail.model.ComboBoxItem;
import com.retail.model.StockEntry;
import com.retail.model.Supplier;
import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;

/**
 *
 * @author Admin
 */
public class StockEntryListPanel extends javax.swing.JPanel {

    private StockEntryController stockEntryController;
    private SupplierController supplierController;
    // Lấy Window cha của StockEntryPanel
    Window parentWindow = SwingUtilities.getWindowAncestor(this);

    /**
     * Creates new form StockEntryListPanel
     */
    public StockEntryListPanel() {
        initComponents();

        supplierController = new SupplierController();
        stockEntryController = new StockEntryController();

        DefaultTableModel defaulTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

        };

        stockEntryTable.setModel(defaulTableModel);

        defaulTableModel.addColumn("Mã phiếu nhập");
        defaulTableModel.addColumn("Nhà cung cấp");
        defaulTableModel.addColumn("Nhân viên tạo");
        defaulTableModel.addColumn("Thời gian tạo");
        defaulTableModel.addColumn("Tổng tiền");

        loadStockEntryData();
        loadSuppliersIntoComboBox();
        // Đăng ký sự kiện tìm kiếm
        addSearchListeners();
    }

    private void loadSuppliersIntoComboBox() {
        supplierComboBox1.removeAllItems();

        // Thêm tùy chọn "Tất cả"
        supplierComboBox1.addItem(new ComboBoxItem(-1, "Tất cả"));

        // Thêm các nhà cung cấp từ cơ sở dữ liệu
        List<Supplier> suppliers = supplierController.getAllSuppliers();
        for (Supplier supplier : suppliers) {
            supplierComboBox1.addItem(new ComboBoxItem(supplier.getSupplierId(), supplier.getName()));
        }
    }

    private void filterStockEntries() {
        // Lấy giá trị từ supplierComboBox1
        ComboBoxItem selectedSupplier = (ComboBoxItem) supplierComboBox1.getSelectedItem();
        Integer supplierId = selectedSupplier.getId(); // Lấy ID nhà cung cấp

        // Nếu chọn "Tất cả", truyền null
        if (supplierId == -1) {
            supplierId = null;
        }

        // Lấy giá trị từ fromEntryDateTextField và toEntryDateTextField
        String fromDateStr = fromEntryDateTextField.getText().trim();
        String toDateStr = toEntryDateTextField.getText().trim();

        System.out.println(fromDateStr + toDateStr);

        // Chuyển đổi ngày từ String sang LocalDate
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate fromDate = null;
        LocalDate toDate = null;

        try {
            if (!fromDateStr.isEmpty()) {
                fromDate = LocalDate.parse(fromDateStr, formatter);
            }
            if (!toDateStr.isEmpty()) {
                toDate = LocalDate.parse(toDateStr, formatter);
            }
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(null, "Định dạng ngày không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Lọc dữ liệu từ cơ sở dữ liệu
        List<StockEntry> filteredEntries = stockEntryController.getFilteredStockEntries(supplierId, fromDate, toDate);

        // Cập nhật dữ liệu vào bảng
        updateStockEntryTable(filteredEntries);

    }

    private void addSearchListeners() {
        // Sự kiện khi chọn nhà cung cấp
        supplierComboBox1.addActionListener((ActionEvent e) -> {
            filterStockEntries();
        });
    }

    private void updateStockEntryTable(List<StockEntry> stockEntries) {
        DefaultTableModel model = (DefaultTableModel) stockEntryTable.getModel();
        model.setRowCount(0); // Xóa dữ liệu cũ

        // Thêm dữ liệu mới vào bảng
        for (StockEntry stockEntry : stockEntries) {
            model.addRow(new Object[]{
                stockEntry.getStockEntryId(),
                stockEntry.getSupplierName(),
                stockEntry.getEmployeeName(),
                stockEntry.getEntryDate(),
                stockEntry.getTotalPrice()
            });
        }
    }

    public void refreshSupplierComboBox(Supplier newSupplier) {
        loadSuppliersIntoComboBox(); // Tải lại danh sách nhà cung cấp
        // Chọn nhà cung cấp mới thêm vào JComboBox
        for (int i = 0; i < supplierComboBox1.getItemCount(); i++) {
            ComboBoxItem item = (ComboBoxItem) supplierComboBox1.getItemAt(i);
            if (item.getId() == newSupplier.getSupplierId()) {
                supplierComboBox1.setSelectedIndex(i);
                break;
            }
        }
    }

    private void loadStockEntryData() {
        DefaultTableModel model = (DefaultTableModel) stockEntryTable.getModel();
        model.setRowCount(0); // Xóa dữ liệu cũ

        List<StockEntry> stockEntries = stockEntryController.getAllStockEntries();
        for (StockEntry stockEntry : stockEntries) {
            model.addRow(new Object[]{
                stockEntry.getStockEntryId(),
                stockEntry.getSupplierName(),
                stockEntry.getEmployeeName(),
                stockEntry.getEntryDate(),
                stockEntry.getTotalPrice()
            });
        }
    }

    private void showDatePickerDialog(JTextField targetTextField, Runnable onDateSelected) {
        // Tạo JDialog với parentWindow
        JDialog datePickerDialog = new JDialog((Frame) parentWindow, "Chọn ngày", true);
        datePickerDialog.setSize(300, 200);
        datePickerDialog.setLocationRelativeTo(parentWindow); // Hiển thị ở giữa cửa sổ cha

        // Tạo JDateChooser
        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("dd/MM/yyyy"); // Định dạng ngày

        // Nút "OK" để xác nhận chọn ngày
        JButton okButton = new JButton("OK");
        okButton.addActionListener((ActionEvent e) -> {
            Date selectedDate = dateChooser.getDate();
            if (selectedDate != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                targetTextField.setText(dateFormat.format(selectedDate)); // Cập nhật vào TextField được truyền vào
                onDateSelected.run(); // Execute the callback after setting the date
            }
            datePickerDialog.dispose(); // Đóng dialog
        });

        // Thêm JDateChooser và nút OK vào dialog
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(dateChooser, BorderLayout.CENTER);
        panel.add(okButton, BorderLayout.SOUTH);

        datePickerDialog.add(panel);
        datePickerDialog.setVisible(true); // Hiển thị dialog
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
        boxProducts = new javax.swing.JPanel();
        ManageProducts = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel2 = new javax.swing.JPanel();
        deleteStockEntryBtn = new javax.swing.JButton();
        editStockEntryBtn = new javax.swing.JButton();
        showInforStockEntryBtn = new javax.swing.JButton();
        showPDFStockEntryBtn = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        supplierComboBox1 = new javax.swing.JComboBox<>();
        Name = new javax.swing.JLabel();
        Name1 = new javax.swing.JLabel();
        Name2 = new javax.swing.JLabel();
        fromEntryDateTextField = new javax.swing.JFormattedTextField();
        openCalendarButton = new javax.swing.JButton();
        toEntryDateTextField = new javax.swing.JFormattedTextField();
        openCalendarButton1 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        stockEntryTable = new javax.swing.JTable();

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        boxProducts.setBackground(new java.awt.Color(255, 255, 255));
        boxProducts.setRequestFocusEnabled(false);

        ManageProducts.setFont(new java.awt.Font("Candara", 1, 18)); // NOI18N
        ManageProducts.setForeground(new java.awt.Color(255, 102, 51));
        ManageProducts.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ManageProducts.setText("DANH SÁCH NHẬP HÀNG");

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Chức năng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(255, 102, 0))); // NOI18N
        jPanel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        deleteStockEntryBtn.setFont(new java.awt.Font("Candara", 1, 16)); // NOI18N
        deleteStockEntryBtn.setForeground(new java.awt.Color(255, 102, 0));
        deleteStockEntryBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/deleteIcon.png"))); // NOI18N
        deleteStockEntryBtn.setText("Xóa");
        deleteStockEntryBtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 102, 0)));
        deleteStockEntryBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteStockEntryBtnActionPerformed(evt);
            }
        });

        editStockEntryBtn.setFont(new java.awt.Font("Candara", 1, 16)); // NOI18N
        editStockEntryBtn.setForeground(new java.awt.Color(255, 102, 0));
        editStockEntryBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/editIcon.png"))); // NOI18N
        editStockEntryBtn.setText("Chỉnh sửa");
        editStockEntryBtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 102, 0)));
        editStockEntryBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editStockEntryBtnActionPerformed(evt);
            }
        });

        showInforStockEntryBtn.setFont(new java.awt.Font("Candara", 1, 16)); // NOI18N
        showInforStockEntryBtn.setForeground(new java.awt.Color(255, 102, 0));
        showInforStockEntryBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/viewIcon.png"))); // NOI18N
        showInforStockEntryBtn.setText("Xem chi tiết");
        showInforStockEntryBtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 102, 0)));
        showInforStockEntryBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showInforStockEntryBtnActionPerformed(evt);
            }
        });

        showPDFStockEntryBtn.setFont(new java.awt.Font("Candara", 1, 16)); // NOI18N
        showPDFStockEntryBtn.setForeground(new java.awt.Color(255, 102, 0));
        showPDFStockEntryBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pdfIcon.png"))); // NOI18N
        showPDFStockEntryBtn.setText("In phiếu");
        showPDFStockEntryBtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 102, 0)));
        showPDFStockEntryBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showPDFStockEntryBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(showPDFStockEntryBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(deleteStockEntryBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(editStockEntryBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(showInforStockEntryBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE)))
                .addGap(18, 18, 18))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(deleteStockEntryBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editStockEntryBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(showInforStockEntryBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(showPDFStockEntryBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tìm kiếm ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(255, 102, 0))); // NOI18N
        jPanel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        Name.setBackground(new java.awt.Color(255, 102, 0));
        Name.setFont(new java.awt.Font("Candara", 1, 16)); // NOI18N
        Name.setForeground(new java.awt.Color(255, 102, 51));
        Name.setText("Tên NCC");

        Name1.setBackground(new java.awt.Color(255, 102, 0));
        Name1.setFont(new java.awt.Font("Candara", 1, 16)); // NOI18N
        Name1.setForeground(new java.awt.Color(255, 102, 51));
        Name1.setText("Từ ngày");

        Name2.setBackground(new java.awt.Color(255, 102, 0));
        Name2.setFont(new java.awt.Font("Candara", 1, 16)); // NOI18N
        Name2.setForeground(new java.awt.Color(255, 102, 51));
        Name2.setText("Đến ngày");

        fromEntryDateTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(java.text.DateFormat.getDateInstance(java.text.DateFormat.SHORT))));
        fromEntryDateTextField.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        fromEntryDateTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fromEntryDateTextFieldActionPerformed(evt);
            }
        });

        openCalendarButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/calenIcon.png"))); // NOI18N
        openCalendarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openCalendarButtonActionPerformed(evt);
            }
        });

        toEntryDateTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(java.text.DateFormat.getDateInstance(java.text.DateFormat.SHORT))));
        toEntryDateTextField.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        toEntryDateTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toEntryDateTextFieldActionPerformed(evt);
            }
        });

        openCalendarButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/calenIcon.png"))); // NOI18N
        openCalendarButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openCalendarButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Name)
                    .addComponent(Name1))
                .addGap(30, 30, 30)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(fromEntryDateTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(openCalendarButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 61, Short.MAX_VALUE)
                        .addComponent(Name2)
                        .addGap(18, 18, 18)
                        .addComponent(toEntryDateTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(openCalendarButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(supplierComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(28, 28, 28))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(supplierComboBox1, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                        .addGap(28, 28, 28))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(Name)
                        .addGap(26, 26, 26)))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(openCalendarButton, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(fromEntryDateTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(Name1)))
                    .addComponent(openCalendarButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(toEntryDateTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Name2))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Danh sách phiếu nhập", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(255, 102, 0))); // NOI18N

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
        stockEntryTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        stockEntryTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                stockEntryTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(stockEntryTable);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1218, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1206, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 450, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        javax.swing.GroupLayout boxProductsLayout = new javax.swing.GroupLayout(boxProducts);
        boxProducts.setLayout(boxProductsLayout);
        boxProductsLayout.setHorizontalGroup(
            boxProductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, boxProductsLayout.createSequentialGroup()
                .addComponent(ManageProducts, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(boxProductsLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 1135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, boxProductsLayout.createSequentialGroup()
                .addContainerGap(37, Short.MAX_VALUE)
                .addGroup(boxProductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(boxProductsLayout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(46, 46, 46))
        );
        boxProductsLayout.setVerticalGroup(
            boxProductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(boxProductsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ManageProducts, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(boxProductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(522, 522, 522))
        );

        jPanel3.getAccessibleContext().setAccessibleName("Tìm kiếm");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1323, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(boxProducts, javax.swing.GroupLayout.DEFAULT_SIZE, 1311, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 787, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(boxProducts, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1323, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 787, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void deleteStockEntryBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteStockEntryBtnActionPerformed
        int row = stockEntryTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog((Frame) parentWindow, "Vui lòng chọn bản ghi nhập kho trước", "Lỗi", JOptionPane.ERROR_MESSAGE);

        } else {
            int stockEntryId = Integer.parseInt(String.valueOf(stockEntryTable.getValueAt(row, 0)));
            int confirm = JOptionPane.showConfirmDialog((Frame) parentWindow, "Bạn chắc chắc muốn xóa không?");
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    stockEntryController.deleteStockEntry(stockEntryId);
                    JOptionPane.showMessageDialog(null, "Xóa bản ghi thành công!");

                    loadStockEntryData();

                } catch (HeadlessException e) {
                    JOptionPane.showMessageDialog(null, "Xóa bản ghi thất bại!");
                }
            }

        }
    }//GEN-LAST:event_deleteStockEntryBtnActionPerformed

    private void editStockEntryBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editStockEntryBtnActionPerformed
        // Lấy chỉ số hàng được chọn
        int selectedRow = stockEntryTable.getSelectedRow();

        // Kiểm tra xem có hàng nào được chọn không
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn một phiếu nhập để chỉnh sửa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Lấy stockEntryId từ cột đầu tiên của hàng được chọn
        int stockEntryId = (int) stockEntryTable.getValueAt(selectedRow, 0);

        // Gọi phương thức để chuyển sang StockEntryPanel với stockEntryId
        openStockEntryPanelForEditing(stockEntryId);
    }//GEN-LAST:event_editStockEntryBtnActionPerformed

    private void openStockEntryPanelForEditing(int stockEntryId) {
        // Tìm StockMenuPanel là tổ tiên của component hiện tại
        StockMenuPanel stockMenuPanel = (StockMenuPanel) SwingUtilities.getAncestorOfClass(StockMenuPanel.class, this);

        if (stockMenuPanel != null) {
            // Lấy StockEntryPanel từ StockMenuPanel
            StockEntryPanel stockEntryPanel = stockMenuPanel.getStockEntryPanel();

            // Thiết lập stockEntryId và tải dữ liệu
            stockEntryPanel.setStockEntryId(stockEntryId);
            stockEntryPanel.fetchStockEntryData();

            // Chuyển sang StockEntryPanel
            stockMenuPanel.showPanel("StockEntry");
        } else {
            System.err.println("StockMenuPanel không tìm thấy trong cây thành phần.");
        }
    }
    private void showInforStockEntryBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showInforStockEntryBtnActionPerformed
        // Lấy chỉ số hàng được chọn
        int selectedRow = stockEntryTable.getSelectedRow();

        // Kiểm tra xem có hàng nào được chọn không
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn một phiếu nhập để chỉnh sửa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Lấy stockEntryId từ cột đầu tiên của hàng được chọn
        int stockEntryId = (int) stockEntryTable.getValueAt(selectedRow, 0);

        // Gọi phương thức để chuyển sang StockEntryPanel với stockEntryId
        openStockEntryPanelForEditing(stockEntryId);
    }//GEN-LAST:event_showInforStockEntryBtnActionPerformed

    private void showPDFStockEntryBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showPDFStockEntryBtnActionPerformed
        int row = stockEntryTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn bản ghi nhập kho trước", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } else {
            int stockEntryId = Integer.parseInt(String.valueOf(stockEntryTable.getValueAt(row, 0)));

            try (Connection conn = DatabaseConnection.getConnection()) {
                // Gọi stored procedure
                CallableStatement cstmt = conn.prepareCall("{call sp_GetStockEntryDetails(?)}");
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
                JOptionPane.showMessageDialog(null, "Lỗi khi tạo báo cáo: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_showPDFStockEntryBtnActionPerformed

    private void fromEntryDateTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fromEntryDateTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fromEntryDateTextFieldActionPerformed

    private void openCalendarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openCalendarButtonActionPerformed

        showDatePickerDialog(fromEntryDateTextField, () -> filterStockEntries());
    }//GEN-LAST:event_openCalendarButtonActionPerformed

    private void toEntryDateTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toEntryDateTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_toEntryDateTextFieldActionPerformed

    private void openCalendarButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openCalendarButton1ActionPerformed
        showDatePickerDialog(toEntryDateTextField, () -> filterStockEntries());
    }//GEN-LAST:event_openCalendarButton1ActionPerformed

    private void stockEntryTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_stockEntryTableMouseClicked
        if (evt.getClickCount() == 2) { // Kiểm tra ấn đúp chuột
            int row = stockEntryTable.getSelectedRow();
            if (row != -1) { // Đảm bảo có hàng được chọn
                // Lấy stockEntryId từ cột đầu tiên của hàng được chọn
                int stockEntryId = Integer.parseInt(stockEntryTable.getValueAt(row, 0).toString());

                // Mở StockEntryPanel để chỉnh sửa
                openStockEntryPanelForEditing(stockEntryId);
            }
        }
    }//GEN-LAST:event_stockEntryTableMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel ManageProducts;
    private javax.swing.JLabel Name;
    private javax.swing.JLabel Name1;
    private javax.swing.JLabel Name2;
    private javax.swing.JPanel boxProducts;
    private javax.swing.JButton deleteStockEntryBtn;
    private javax.swing.JButton editStockEntryBtn;
    private javax.swing.JFormattedTextField fromEntryDateTextField;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton openCalendarButton;
    private javax.swing.JButton openCalendarButton1;
    private javax.swing.JButton showInforStockEntryBtn;
    private javax.swing.JButton showPDFStockEntryBtn;
    private javax.swing.JTable stockEntryTable;
    private javax.swing.JComboBox<ComboBoxItem> supplierComboBox1;
    private javax.swing.JFormattedTextField toEntryDateTextField;
    // End of variables declaration//GEN-END:variables
}
