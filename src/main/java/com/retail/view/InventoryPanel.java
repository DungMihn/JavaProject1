/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.retail.view;

import com.retail.controller.InventoryController;
import com.retail.dao.DatabaseConnection;
import com.retail.model.Inventory;
import com.retail.model.Product;
import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Admin
 */
public class InventoryPanel extends javax.swing.JPanel {

    // Lấy Window cha của StockEntryPanel
    Window parentWindow = SwingUtilities.getWindowAncestor(this);

    private InventoryController inventoryController;

    /**
     * Creates new form InventoryPanel
     */
    public InventoryPanel() {
        initComponents();

        inventoryController = new InventoryController();

        // Thiết lập bảng không cho phép chỉnh sửa
        DefaultTableModel defaulTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        inventoryTable.setModel(defaulTableModel);

        // Thêm các cột vào bảng
        defaulTableModel.addColumn("Mã");
        defaulTableModel.addColumn("Tên sản phẩm");
        defaulTableModel.addColumn("Số lượng");
        defaulTableModel.addColumn("Ngày cập nhập");

        // Tải dữ liệu kho
        loadInventoryData();

        // Kiểm tra và thêm hiệu ứng nhấp nháy cho warningBtn
        checkLowStockProducts();

        // Thêm DocumentListener để lắng nghe sự kiện nhập liệu
        findProductNameTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterInventoryByProductName(); // Gọi khi có ký tự được thêm vào
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterInventoryByProductName(); // Gọi khi có ký tự bị xóa
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // Không cần xử lý trong trường hợp này
            }
        });
    }

    /**
     * Hiển thị popup danh sách sản phẩm gần hết hàng
     */
    private void showLowStockProductsPopup() {
        // Lấy danh sách sản phẩm gần hết hàng
        List<Product> lowStockProducts = inventoryController.getLowStockProducts();

        if (lowStockProducts.isEmpty()) {
            // Nếu không có sản phẩm nào gần hết hàng, hiển thị thông báo
            JOptionPane.showMessageDialog(null, "Hiện tại không có cảnh báo nào!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } else {
            // Hiển thị danh sách sản phẩm gần hết hàng trong một popup
            StringBuilder message = new StringBuilder("Các sản phẩm gần hết hàng:\n");
            for (Product product : lowStockProducts) {
                message.append("- ").append(product.getName()).append(" (Còn lại: ").append(product.getStockQuantity()).append(")\n");
            }
            JOptionPane.showMessageDialog(null, message.toString(), "Cảnh báo", JOptionPane.WARNING_MESSAGE);
        }
    }

    //Kiểm tra và thêm hiệu ứng nhấp nháy cho warningBtn nếu có sản phẩm gần hết hàng
    private void checkLowStockProducts() {
        List<Product> lowStockProducts = inventoryController.getLowStockProducts();
        if (!lowStockProducts.isEmpty()) {
            // Nếu có sản phẩm gần hết hàng, thêm hiệu ứng nhấp nháy
            Timer timer = new Timer(500, e -> {
                if (warningBtn.getBackground() == Color.RED) {
                    warningBtn.setBackground(null);
                } else {
                    warningBtn.setBackground(Color.RED);
                }
            });
            timer.start();
        } else {
            warningBtn.setBackground(null); // Đặt lại màu nền mặc định
        }
    }

    // Tải dữ liệu kho vào bảng
    private void loadInventoryData() {
        DefaultTableModel model = (DefaultTableModel) inventoryTable.getModel();
        model.setRowCount(0); // Xóa dữ liệu cũ

        List<Inventory> inventories = inventoryController.getAllInventories();
        boolean hasLowStock = false; // Biến kiểm tra xem có sản phẩm nào sắp hết hàng không

        for (Inventory inventory : inventories) {
            model.addRow(new Object[]{
                inventory.getInventoryId(),
                inventory.getProductName(),
                inventory.getStockQuantity(),
                inventory.getLastUpdatedString()
            });

            // Kiểm tra nếu stock_quantity dưới ngưỡng cảnh báo
            if (inventory.getStockQuantity() <= 10) { // Ngưỡng cảnh báo là 10
                hasLowStock = true;
            }
        }

        // Kiểm tra và thêm hiệu ứng nhấp nháy cho warningBtn
        checkLowStockProducts();
    }

    //Lọc dữ liệu kho dựa trên tên sản phẩm
    private void filterInventoryByProductName() {
        String keyword = findProductNameTextField.getText().trim();

        DefaultTableModel model = (DefaultTableModel) inventoryTable.getModel();
        model.setRowCount(0); // Xóa dữ liệu cũ

        if (keyword.isEmpty()) {
            // Nếu keyword rỗng, tải lại toàn bộ dữ liệu từ Inventory
            loadInventoryData();
        } else {
            // Nếu có keyword, tìm kiếm sản phẩm
            List<Inventory> inventories = inventoryController.searchInventoryByProductName(keyword);

            // Hiển thị kết quả trong bảng inventoryTable
            for (Inventory inventory : inventories) {
                model.addRow(new Object[]{
                    inventory.getInventoryId(),
                    inventory.getProductName(),
                    inventory.getStockQuantity(),
                    inventory.getLastUpdatedString()
                });
            }
        }
    }

    /**
     * Lọc dữ liệu kho dựa trên ngày bắt đầu và ngày kết thúc
     */
    private void filterInventories() {
        // Lấy giá trị từ fromEntryDateTextField và toEntryDateTextField
        String fromDateStr = fromEntryDateTextField.getText().trim();
        String toDateStr = toEntryDateTextField.getText().trim();

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
            JOptionPane.showMessageDialog(this, "Định dạng ngày không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Lọc dữ liệu từ cơ sở dữ liệu
        List<Inventory> filteredInventories = inventoryController.getFilteredInventories(fromDate, toDate);

        // Cập nhật dữ liệu vào bảng
        updateInventoryTable(filteredInventories);
    }

    //Cập nhật dữ liệu vào bảng
    private void updateInventoryTable(List<Inventory> inventories) {
        DefaultTableModel model = (DefaultTableModel) inventoryTable.getModel();
        model.setRowCount(0); // Xóa dữ liệu cũ

        for (Inventory inventory : inventories) {
            model.addRow(new Object[]{
                inventory.getInventoryId(),
                inventory.getProductName(),
                inventory.getStockQuantity(),
                inventory.getLastUpdatedString()
            });
        }
    }

    /**
     * Hiển thị hộp thoại chọn ngày
     */
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
                onDateSelected.run(); // Thực thi callback sau khi chọn ngày
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

        jPopupMenu1 = new javax.swing.JPopupMenu();
        boxProducts = new javax.swing.JPanel();
        ManageProducts = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel2 = new javax.swing.JPanel();
        warningBtn = new javax.swing.JButton();
        showInforInventoryBtn = new javax.swing.JButton();
        exportReportInventory = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        Name = new javax.swing.JLabel();
        Name1 = new javax.swing.JLabel();
        Name2 = new javax.swing.JLabel();
        fromEntryDateTextField = new javax.swing.JFormattedTextField();
        openCalendarButton = new javax.swing.JButton();
        toEntryDateTextField = new javax.swing.JFormattedTextField();
        openCalendarButton1 = new javax.swing.JButton();
        findProductNameTextField = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        inventoryTable = new javax.swing.JTable();
        jSeparator2 = new javax.swing.JSeparator();

        boxProducts.setBackground(new java.awt.Color(255, 255, 255));
        boxProducts.setRequestFocusEnabled(false);

        ManageProducts.setFont(new java.awt.Font("Candara", 1, 18)); // NOI18N
        ManageProducts.setForeground(new java.awt.Color(255, 102, 51));
        ManageProducts.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ManageProducts.setText("QUẢN LÝ KHO");

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Chức năng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(255, 102, 0))); // NOI18N
        jPanel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        warningBtn.setFont(new java.awt.Font("Candara", 1, 16)); // NOI18N
        warningBtn.setForeground(new java.awt.Color(255, 102, 0));
        warningBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/warnIcon.png"))); // NOI18N
        warningBtn.setText("Cảnh báo hết hàng");
        warningBtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 102, 0)));
        warningBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                warningBtnActionPerformed(evt);
            }
        });

        showInforInventoryBtn.setFont(new java.awt.Font("Candara", 1, 16)); // NOI18N
        showInforInventoryBtn.setForeground(new java.awt.Color(255, 102, 0));
        showInforInventoryBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/viewIcon.png"))); // NOI18N
        showInforInventoryBtn.setText("Xem chi tiết");
        showInforInventoryBtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 102, 0)));
        showInforInventoryBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showInforInventoryBtnActionPerformed(evt);
            }
        });

        exportReportInventory.setFont(new java.awt.Font("Candara", 1, 16)); // NOI18N
        exportReportInventory.setForeground(new java.awt.Color(255, 102, 0));
        exportReportInventory.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pdfIcon.png"))); // NOI18N
        exportReportInventory.setText("Báo cáo tồn kho");
        exportReportInventory.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 102, 0)));
        exportReportInventory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportReportInventoryActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(exportReportInventory, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(warningBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(showInforInventoryBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(warningBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(showInforInventoryBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(exportReportInventory, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tìm kiếm ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(255, 102, 0))); // NOI18N
        jPanel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        Name.setBackground(new java.awt.Color(255, 102, 0));
        Name.setFont(new java.awt.Font("Candara", 1, 16)); // NOI18N
        Name.setForeground(new java.awt.Color(255, 102, 51));
        Name.setText("Tên SP");

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

        findProductNameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                findProductNameTextFieldActionPerformed(evt);
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 85, Short.MAX_VALUE)
                        .addComponent(Name2)
                        .addGap(18, 18, 18)
                        .addComponent(toEntryDateTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(openCalendarButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(findProductNameTextField))
                .addGap(28, 28, 28))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(Name))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(findProductNameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                        .addGap(3, 3, 3)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
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
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Danh sách tồn kho", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(255, 102, 0))); // NOI18N

        inventoryTable.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        inventoryTable.setModel(new javax.swing.table.DefaultTableModel(
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
        inventoryTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        inventoryTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                inventoryTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(inventoryTable);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1230, Short.MAX_VALUE)
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
                .addContainerGap(12, Short.MAX_VALUE)
                .addGroup(boxProductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(boxProductsLayout.createSequentialGroup()
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 1228, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(46, 46, 46))
                    .addGroup(boxProductsLayout.createSequentialGroup()
                        .addGroup(boxProductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, boxProductsLayout.createSequentialGroup()
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(22, 22, 22))))
        );
        boxProductsLayout.setVerticalGroup(
            boxProductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(boxProductsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ManageProducts, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(boxProductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(547, 547, 547))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1292, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addComponent(boxProducts, javax.swing.GroupLayout.DEFAULT_SIZE, 1286, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 787, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(boxProducts, javax.swing.GroupLayout.PREFERRED_SIZE, 781, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 6, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void warningBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_warningBtnActionPerformed
        showLowStockProductsPopup();
    }//GEN-LAST:event_warningBtnActionPerformed

    private void showInforInventoryBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showInforInventoryBtnActionPerformed
        int selectedRow = inventoryTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn một hàng để xem thông tin!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int inventoryId = (int) inventoryTable.getValueAt(selectedRow, 0);
        Inventory inventory = inventoryController.getInventoryById(inventoryId);
        if (inventory != null) {
            String message = "Thông tin chi tiết:\n"
                    + "Mã kho: " + inventory.getInventoryId() + "\n"
                    + "Tên sản phẩm: " + inventory.getProductName() + "\n"
                    + "Số lượng: " + inventory.getStockQuantity() + "\n"
                    + "Ngày cập nhật: " + inventory.getLastUpdatedString();
            JOptionPane.showMessageDialog(null, message, "Thông tin kho", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Không tìm thấy thông tin kho!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_showInforInventoryBtnActionPerformed

    private void exportReportInventoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportReportInventoryActionPerformed
        // Tạo một mảng chứa các tùy chọn báo cáo
        String[] options = {"Theo ngày", "Theo tuần", "Theo tháng"};

        // Hiển thị hộp thoại lựa chọn
        int choice = JOptionPane.showOptionDialog(
                null,
                "Chọn loại báo cáo tồn kho:",
                "Báo cáo tồn kho",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        //Xử lý lựa chọn
        switch (choice) {
            case 0 ->
                generateDailyReport(); // Báo cáo theo ngày
            case 1 ->
                generateWeeklyReport(); // Báo cáo theo tuần
            case 2 ->
                generateMonthlyReport(); // Báo cáo theo tháng
            default -> {
            }
        }
    }//GEN-LAST:event_exportReportInventoryActionPerformed

    private void generateDailyReport() {
        // Lấy ngày hiện tại
        LocalDate today = LocalDate.now();

        // Định dạng ngày bắt đầu và ngày kết thúc
        String startDate = today.toString(); // Ngày bắt đầu là hôm nay (ví dụ: "2025-03-14")
        String endDate = today.plusDays(1).toString(); // Ngày kết thúc là ngày tiếp theo (ví dụ: "2025-03-15")

        // Thông tin khoảng thời gian báo cáo
        String timeRange = "BÁO CÁO TỒN KHO NGÀY " + today.toString();

        // Lấy dữ liệu tồn kho từ cơ sở dữ liệu
        List<Inventory> inventoryReport = inventoryController.getInventoryReport(startDate, endDate);

        // Tạo báo cáo PDF
        generateInventoryReportPDF(inventoryReport, "bÁO CÁO tỒN KHO THEO NGÀY", timeRange);
    }

    private void generateWeeklyReport() {
        // Lấy ngày đầu tuần và cuối tuần
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(DayOfWeek.MONDAY); // Ngày đầu tuần (thứ Hai)
        LocalDate endOfWeek = today.with(DayOfWeek.SUNDAY);   // Ngày cuối tuần (Chủ Nhật)

        // Định dạng ngày bắt đầu và ngày kết thúc
        String startDate = startOfWeek.toString(); // Ngày đầu tuần (ví dụ: "2025-03-10")
        String endDate = endOfWeek.plusDays(1).toString(); // Ngày cuối tuần + 1 ngày (ví dụ: "2025-03-16")

        // Thông tin khoảng thời gian báo cáo
        String timeRange = "BÁO CÁO TỒN KHO TỪ NGÀY " + startOfWeek.toString() + " ĐẾN NGÀY " + endOfWeek.toString();

        // Lấy dữ liệu tồn kho từ cơ sở dữ liệu
        List<Inventory> inventoryReport = inventoryController.getInventoryReport(startDate, endDate);

        // Tạo báo cáo PDF
        generateInventoryReportPDF(inventoryReport, "BÁO CÁO TỒN KHO THEO TUẦN", timeRange);
    }

    private void generateMonthlyReport() {
        // Lấy ngày đầu tháng và cuối tháng
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.withDayOfMonth(1); // Ngày đầu tháng
        LocalDate endOfMonth = today.withDayOfMonth(today.lengthOfMonth()); // Ngày cuối tháng

        // Định dạng ngày bắt đầu và ngày kết thúc
        String startDate = startOfMonth.toString(); // Ngày đầu tháng (ví dụ: "2025-03-01")
        String endDate = endOfMonth.plusDays(1).toString(); // Ngày cuối tháng + 1 ngày (ví dụ: "2025-04-01")

        // Thông tin khoảng thời gian báo cáo
        String timeRange = "BÁO CÁO TỒN KHO TỪ NGÀY " + startOfMonth.toString() + " ĐẾN NGÀY " + endOfMonth.toString();

        // Lấy dữ liệu tồn kho từ cơ sở dữ liệu
        List<Inventory> inventoryReport = inventoryController.getInventoryReport(startDate, endDate);

        // Tạo báo cáo PDF
        generateInventoryReportPDF(inventoryReport, "BÁO CÁO TỒN KHO THEO THÁNG", timeRange);
    }

    private void generateInventoryReportPDF(List<Inventory> inventoryReport, String reportTitle, String timeRange) {
        if (inventoryReport.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Không có dữ liệu để tạo báo cáo!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Chuyển đổi danh sách Inventory thành ResultSet
            ResultSet rs = convertInventoryListToResultSet(inventoryReport);

            // Kiểm tra xem ResultSet có dữ liệu không
            if (!rs.next()) {
                JOptionPane.showMessageDialog(null, "Không có dữ liệu để tạo báo cáo!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Đưa con trỏ về đầu ResultSet
            rs.beforeFirst();

            // Tạo JasperDesign bằng cách sử dụng lớp InventoryReportDesignGenerator
            InventoryReportDesignGenerator designGenerator = new InventoryReportDesignGenerator();
            JasperDesign jasperDesign = designGenerator.createInventoryReportDesign(reportTitle, timeRange);

            // Tạo JasperPrint từ JasperReport và dữ liệu
            JRResultSetDataSource dataSource = new JRResultSetDataSource(rs);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(), dataSource);

            // Kiểm tra xem JasperPrint có trang nào không
            if (jasperPrint.getPages().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Không có dữ liệu để hiển thị trong báo cáo!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Xuất file PDF
            String filePath = "InventoryReport_" + System.currentTimeMillis() + ".pdf";
            JasperExportManager.exportReportToPdfFile(jasperPrint, filePath);

            // Hiển thị báo cáo
            JasperViewer.viewReport(jasperPrint, false);
        } catch (JRException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi tạo báo cáo: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private ResultSet convertInventoryListToResultSet(List<Inventory> inventoryReport) throws SQLException {
        // Tạo một CachedRowSet
        CachedRowSet rowSet = RowSetProvider.newFactory().createCachedRowSet();

        // Thiết lập loại ResultSet (hỗ trợ cuộn và chỉ đọc)
        rowSet.setType(ResultSet.TYPE_SCROLL_INSENSITIVE);
        rowSet.setConcurrency(ResultSet.CONCUR_UPDATABLE);

        rowSet.setCommand(
                "SELECT i.inventory_id, p.name AS product_name, i.stock_quantity, i.last_updated "
                + "FROM inventory i "
                + "JOIN product p ON i.product_id = p.product_id "
                + "WHERE 1=0"
        );

        // Kết nối đến cơ sở dữ liệu để thiết lập cấu trúc của ResultSet
        try (Connection conn = DatabaseConnection.getConnection()) {
            rowSet.execute(conn);
        }

        // Định dạng ngày theo dd/MM/yyyy
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Thêm dữ liệu vào CachedRowSet
        for (Inventory inventory : inventoryReport) {
            rowSet.moveToInsertRow(); // Di chuyển con trỏ đến hàng chèn
            rowSet.updateInt("inventory_id", inventory.getInventoryId());
            rowSet.updateString("product_name", inventory.getProductName());
            rowSet.updateInt("stock_quantity", inventory.getStockQuantity());

            // Chuyển lastUpdated sang định dạng dd/MM/yyyy
            if (inventory.getLastUpdated() != null) {
                String formattedDate = inventory.getLastUpdated().format(formatter);
                rowSet.updateString("last_updated", formattedDate); // Lưu dưới dạng chuỗi
            } else {
                rowSet.updateNull("last_updated");
            }

            rowSet.insertRow(); // Chèn hàng vào CachedRowSet
            rowSet.moveToCurrentRow(); // Di chuyển con trỏ về hàng hiện tại
        }

        // Di chuyển con trỏ về đầu ResultSet
        rowSet.beforeFirst();
        return rowSet;
    }


    private void fromEntryDateTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fromEntryDateTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fromEntryDateTextFieldActionPerformed

    private void openCalendarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openCalendarButtonActionPerformed

        showDatePickerDialog(fromEntryDateTextField, () -> filterInventories());
    }//GEN-LAST:event_openCalendarButtonActionPerformed

    private void toEntryDateTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toEntryDateTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_toEntryDateTextFieldActionPerformed

    private void openCalendarButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openCalendarButton1ActionPerformed
        showDatePickerDialog(toEntryDateTextField, () -> filterInventories());
    }//GEN-LAST:event_openCalendarButton1ActionPerformed

    private void inventoryTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_inventoryTableMouseClicked
//        if (evt.getClickCount() == 2) { // Kiểm tra ấn đúp chuột
//            int row = inventoryTable.getSelectedRow();
//            if (row != -1) { // Đảm bảo có hàng được chọn
//                // Lấy stockEntryId từ cột đầu tiên của hàng được chọn
//                int stockEntryId = Integer.parseInt(inventoryTable.getValueAt(row, 0).toString());
//
//                // Mở StockEntryPanel để chỉnh sửa
//                openStockEntryPanelForEditing(stockEntryId);
//            }
//        }
    }//GEN-LAST:event_inventoryTableMouseClicked

    private void findProductNameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_findProductNameTextFieldActionPerformed
        filterInventoryByProductName();
    }//GEN-LAST:event_findProductNameTextFieldActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel ManageProducts;
    private javax.swing.JLabel Name;
    private javax.swing.JLabel Name1;
    private javax.swing.JLabel Name2;
    private javax.swing.JPanel boxProducts;
    private javax.swing.JButton exportReportInventory;
    private javax.swing.JTextField findProductNameTextField;
    private javax.swing.JFormattedTextField fromEntryDateTextField;
    private javax.swing.JTable inventoryTable;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JButton openCalendarButton;
    private javax.swing.JButton openCalendarButton1;
    private javax.swing.JButton showInforInventoryBtn;
    private javax.swing.JFormattedTextField toEntryDateTextField;
    private javax.swing.JButton warningBtn;
    // End of variables declaration//GEN-END:variables
}
