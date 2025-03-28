/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.retail.view;

import com.retail.controller.EmployeeController;
import com.retail.controller.InventoryController;
import com.retail.controller.StockEntryController;
import com.retail.controller.StockEntryDetailController;
import com.retail.controller.SupplierController;
import com.retail.dao.DatabaseConnection;
import com.retail.model.ComboBoxItem;
import com.retail.model.CurrentUser;
import com.retail.model.Employee;
import com.retail.model.Inventory;
import com.retail.model.Product;
import com.retail.model.StockEntry;
import com.retail.model.StockEntryDetail;
import com.retail.model.StockEntryDetailChange;
import com.retail.model.Supplier;
import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;

/**
 *
 * @author Admin
 */
public class StockEntryPanel extends javax.swing.JPanel {

    private int supplierId; // Thêm biến supplierId
    private StockEntryController stockEntryController;
    private StockEntryDetailController stockEntryDetailController;
    private SupplierController supplierController;
    private EmployeeController employeeController;
    private InventoryController inventoryController;
    private List<ComboBoxItem> productItems;
    private JList<ComboBoxItem> suggestionList;
    private JList<ComboBoxItem> supplierSuggestionList;

    private List<StockEntryDetail> tempStockEntryDetails; // Danh sách tạm thời các chi tiết nhập hàng
    private StockEntry currentStockEntry; // Hóa đơn nhập hàng hiện tại
    private List<StockEntryDetailChange> pendingChanges = new ArrayList<>();
    private DefaultTableModel stockEntryDetailTableModel;
    private int stockEntryId;
    private StockEntry stockEntry;
    private List<ComboBoxItem> supplierItems;
    private int selectedRow = -1; // Mặc định là -1 (không có hàng nào được chọn)

    /**
     * Creates new form StockEntryPanel
     */
    public StockEntryPanel() {
        initComponents();

        supplierController = new SupplierController();
        stockEntryController = new StockEntryController();
        stockEntryDetailController = new StockEntryDetailController();
        inventoryController = new InventoryController();
        employeeController = new EmployeeController();

        // Khởi tạo suggestionList
        suggestionList = new JList<>();
        suggestionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Khởi tạo danh sách tạm thời
        tempStockEntryDetails = new ArrayList<>();
        currentStockEntry = null;

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
        stockEntryDetailTableModel.addColumn("Mã SP");
        stockEntryDetailTableModel.addColumn("Tên SP");
        stockEntryDetailTableModel.addColumn("Loại");
        stockEntryDetailTableModel.addColumn("Đơn vị");
        stockEntryDetailTableModel.addColumn("Mã vạch");
        stockEntryDetailTableModel.addColumn("Số lượng");
        stockEntryDetailTableModel.addColumn("Giá nhập");

        // Khởi tạo autocomplete cho supplierNameTextField
        setupSupplierAutoComplete();
        loadSuppliersIntoAutoComplete();

        initializeData();

        // Thiết lập sự kiện click vào bảng
        setupTableClickListener();

        // Thiết lập tính toán totalPrice tự động
        setupTotalPriceCalculation();

        // Thêm sự kiện lắng nghe cho stockEntryIdTextField
        setupStockEntryIdTextFieldListener();

        setupSupplierIdTextFieldListener();

        stockEntryDetailTableModel.addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                int row = e.getFirstRow();
                int productId = (int) stockEntryDetailTableModel.getValueAt(row, 0);
                int quantity = (int) stockEntryDetailTableModel.getValueAt(row, 5);
                double price = (double) stockEntryDetailTableModel.getValueAt(row, 6);

                // Cập nhật temp list
                tempStockEntryDetails.stream()
                        .filter(d -> d.getProductId() == productId)
                        .findFirst()
                        .ifPresent(d -> {
                            d.setQuantity(quantity);
                            d.setPurchasePrice(price);
                        });
            }
        });

    }

    private void setupSupplierIdTextFieldListener() {
        supplierIdTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateSupplierNameFromId();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateSupplierNameFromId();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateSupplierNameFromId();
            }
        });
    }

    private void updateSupplierNameFromId() {
        String supplierIdText = supplierIdTextField.getText().trim();

        if (supplierIdText.isEmpty()) {
            supplierNameTextField.setText("");
            return;
        }

        try {
            supplierId = Integer.parseInt(supplierIdText);

            // Tìm nhà cung cấp trong danh sách đã load
            ComboBoxItem foundSupplier = supplierItems.stream()
                    .filter(item -> item.getId() == supplierId)
                    .findFirst()
                    .orElse(null);

            if (foundSupplier != null) {
                // Nếu tìm thấy, cập nhật tên nhà cung cấp
                supplierNameTextField.setText(foundSupplier.getName());
                this.supplierId = supplierId; // Cập nhật supplierId

                // Load danh sách sản phẩm của nhà cung cấp này
                loadProductsIntoAutoComplete();
                setupAutoCompleteListener();
            } else {
                // Nếu không tìm thấy, kiểm tra trong database
                Supplier supplier = supplierController.getSupplierById(supplierId);
                if (supplier != null) {
                    supplierNameTextField.setText(supplier.getName());
                    this.supplierId = supplierId;

                    // Thêm vào danh sách supplierItems nếu chưa có
                    if (supplierItems.stream().noneMatch(item -> item.getId() == supplierId)) {
                        supplierItems.add(new ComboBoxItem(supplierId, supplier.getName()));
                    }

                    // Load danh sách sản phẩm
                    loadProductsIntoAutoComplete();
                    setupAutoCompleteListener();
                } else {
                    supplierNameTextField.setText("");
                }
            }
        } catch (NumberFormatException e) {
            // Nếu nhập không phải số
            supplierNameTextField.setText("");
        }
    }

    private void setupStockEntryIdTextFieldListener() {
        stockEntryIdTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                fetchStockEntryData();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                fetchStockEntryData();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                fetchStockEntryData();
            }
        });
    }

    public void fetchStockEntryData() {
        String stockEntryIdText = stockEntryIdTextField.getText().trim();

        if (stockEntryIdText.isEmpty()) {
            // Nếu trường ID trống, đặt lại các trường về giá trị mặc định
            resetFields();
            return;
        }

        try {
            stockEntryId = Integer.parseInt(stockEntryIdText);

            // Lấy thông tin StockEntry từ cơ sở dữ liệu
            stockEntry = stockEntryController.getStockEntryById(stockEntryId);

            if (stockEntry == null) {
                resetFields();
                return;
            }

            // Đổ dữ liệu vào các trường
            supplierNameTextField.setText(stockEntry.getSupplierName());
            supplierIdTextField.setText(String.valueOf(stockEntry.getSupplierId()));

            this.supplierId = Integer.parseInt(supplierIdTextField.getText()); // Thiết lập supplier_id;

            // Sau khi có supplier_id, tải danh sách sản phẩm
            loadProductsIntoAutoComplete();
            setupAutoCompleteListener();

            // Chọn đúng nhân viên trong combobox
            for (int i = 0; i < employeeComboBox.getItemCount(); i++) {
                ComboBoxItem item = (ComboBoxItem) employeeComboBox.getItemAt(i);
                if (item.getId() == stockEntry.getEmployeeId()) {
                    employeeComboBox.setSelectedIndex(i);
                    break;
                }
            }
            // Hiển thị ngày nhập từ stockEntry vào entryDateTextField
            String entryDateString = stockEntry.getEntryDate(); // Lấy ngày dưới dạng String
            if (entryDateString != null && !entryDateString.isEmpty()) {
                entryDateTextField.setText(entryDateString); // Hiển thị ngày trong entryDateTextField
            }
//            entryDateTextField.setText(new SimpleDateFormat("dd/MM/yyyy").format(stockEntry.getEntryDate()));

            // Load chi tiết StockEntry vào bảng
            loadStockEntryDetailsIntoTable(stockEntryId);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID nhập vào không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            resetFields();
        }
    }

    private void resetFields() {
        supplierNameTextField.setText("");
        supplierIdTextField.setText("");
        stockEntryDetailTableModel.setRowCount(0); // Xóa dữ liệu cũ trong bảng
    }

    public void setStockEntryId(int stockEntryId) {
        this.stockEntryId = stockEntryId;
        stockEntryIdTextField.setText(String.valueOf(stockEntryId)); // Hiển thị stockEntryId lên giao diện
    }

    public void loadStockEntryDetailsIntoTable(int stockEntryId) {
        // Xóa dữ liệu cũ trong bảng và danh sách tạm thời
        stockEntryDetailTableModel.setRowCount(0);
        tempStockEntryDetails.clear();

        // Lấy danh sách chi tiết từ cơ sở dữ liệu
        List<StockEntryDetail> details = stockEntryDetailController.getStockEntryDetailsByStockEntryId(stockEntryId);

        // Thêm dữ liệu vào danh sách tạm thời
        tempStockEntryDetails.addAll(details);

        // Thêm dữ liệu từ danh sách tạm thời vào bảng
        for (StockEntryDetail detail : tempStockEntryDetails) {
            stockEntryDetailTableModel.addRow(new Object[]{
                detail.getProductId(),
                detail.getProductName(),
                detail.getCategory(),
                detail.getUnit(),
                detail.getBarcode(),
                detail.getQuantity(),
                detail.getPurchasePrice()
            });
        }

        // Cập nhật giao diện
        stockEntryDetailTable.revalidate();
        stockEntryDetailTable.repaint();
    }

    public void loadEmployeesIntoComboBox() {
        try {
            List<Employee> employees = employeeController.getAllEmployees();
            employeeComboBox.removeAllItems();

            // Thêm tất cả nhân viên vào combobox
            for (Employee employee : employees) {
                employeeComboBox.addItem(new ComboBoxItem(employee.getEmployeeId(), employee.getName()));
            }

            // Chọn nhân viên đang login
            int currentEmployeeId = CurrentUser.getEmployeeId();
            for (int i = 0; i < employeeComboBox.getItemCount(); i++) {
                ComboBoxItem item = (ComboBoxItem) employeeComboBox.getItemAt(i);
                if (item.getId() == currentEmployeeId) {
                    employeeComboBox.setSelectedIndex(i); // Sửa thành setSelectedIndex thay vì setSelectedItem
                    break;
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải danh sách nhân viên: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadProductsIntoAutoComplete() {
        List<Product> products = supplierController.getProductsBySupplierId(supplierId);
        productItems = new ArrayList<>();

        for (Product product : products) {
            productItems.add(new ComboBoxItem(product.getProductId(), product.getName()));
        }
    }

    private void showDatePickerDialog() {
        // Lấy Window cha của StockEntryPanel
        Window parentWindow = SwingUtilities.getWindowAncestor(this);

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
                entryDateTextField.setText(dateFormat.format(selectedDate)); // Cập nhật ngày vào entryDateTextField
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

    private void setupAutoCompleteListener() {
        productNameTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String text = productNameTextField.getText().trim();

                // Nếu người dùng xóa tên sản phẩm
                if (text.isEmpty()) {
                    resetProductFields(); // Đặt lại các trường về giá trị mặc định
                    supplierPopupMenu.setVisible(false);
                    return;
                }

                // Nếu người dùng nhấn Enter
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    ComboBoxItem selectedValue = suggestionList.getSelectedValue();
                    if (selectedValue != null) {
                        // Nếu người dùng chọn từ danh sách gợi ý
                        productNameTextField.setText(selectedValue.getName());
                        supplierPopupMenu.setVisible(false);
                        autoFillProductFields(selectedValue.getId()); // Điền thông tin sản phẩm
                    } else {
                        // Nếu người dùng nhập tên sản phẩm mới
                        int confirm = JOptionPane.showConfirmDialog(
                                null,
                                "Bạn có muốn thêm sản phẩm mới với tên '" + text + "' không?",
                                "Xác nhận",
                                JOptionPane.YES_NO_OPTION
                        );

                        if (confirm == JOptionPane.YES_OPTION) {
                            // Xử lý thêm sản phẩm mới
                            resetProductFields(); // Đặt lại các trường về giá trị mặc định
                            productNameTextField.setText(text); // Giữ lại tên sản phẩm mới
                        } else {
                            productNameTextField.setText(""); // Xóa trường nhập liệu
                            resetProductFields(); // Đặt lại các trường về giá trị mặc định
                        }
                    }
                }

                // Lọc danh sách gợi ý
                List<ComboBoxItem> filtered = productItems.stream()
                        .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase()))
                        .collect(Collectors.toList());

                showSuggestionPopup(filtered);
            }
        });

        suggestionList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ComboBoxItem selectedValue = suggestionList.getSelectedValue();
                if (selectedValue != null) {
                    productNameTextField.setText(selectedValue.getName());
                    supplierPopupMenu.setVisible(false);
                    autoFillProductFields(selectedValue.getId()); // Điền thông tin sản phẩm
                }
            }
        });

        suggestionList.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    ComboBoxItem selectedValue = suggestionList.getSelectedValue();
                    if (selectedValue != null) {
                        productNameTextField.setText(selectedValue.getName());
                        supplierPopupMenu.setVisible(false);
                        autoFillProductFields(selectedValue.getId()); // Điền thông tin sản phẩm
                    }
                }
            }
        });
    }

    private void autoFillProductFields(int productId) {
        Product product = stockEntryController.getProductById(productId);
        if (product != null) {
            productIdTextField.setText(String.valueOf(product.getProductId()));
            barcodeTextField.setText(product.getBarcode());
            unitComboBox.setSelectedItem(product.getUnit());
            categoryComboBox.setSelectedItem(product.getCategory());
            priceTextField.setText(String.valueOf(product.getPurchasePrice()));
        } else {
            barcodeTextField.setText("");
            unitComboBox.setSelectedIndex(0);
            categoryComboBox.setSelectedIndex(0);
            priceTextField.setText("");
        }
    }

    private void resetProductFields() {
        barcodeTextField.setText("");
        unitComboBox.setSelectedIndex(0);
        categoryComboBox.setSelectedIndex(0);
        priceTextField.setText("");
        productNameTextField.setText("");
        quantityTextField.setText("");
        productIdTextField.setText("");

    }

    private void showSuggestionPopup(List<ComboBoxItem> suggestions) {
        supplierPopupMenu.removeAll();

        if (suggestions.isEmpty()) {
            supplierPopupMenu.setVisible(false);
            return;
        }

        suggestionList.setListData(suggestions.toArray(new ComboBoxItem[0]));
        suggestionList.setSelectedIndex(0);

        JScrollPane scrollPane = new JScrollPane(suggestionList);

        int rowHeight = suggestionList.getFixedCellHeight() > 0 ? suggestionList.getFixedCellHeight() : 20;
        int maxVisibleRows = 6;
        int popupHeight = Math.min(suggestions.size(), maxVisibleRows) * rowHeight + 10;

        scrollPane.setPreferredSize(new Dimension(productNameTextField.getWidth(), popupHeight));

        supplierPopupMenu.add(scrollPane);
        supplierPopupMenu.show(productNameTextField, 0, productNameTextField.getHeight() + 5);
    }

    //supplier
    private void loadSuppliersIntoAutoComplete() {
        List<Supplier> suppliers = supplierController.getAllSuppliers();
        supplierItems = new ArrayList<>();

        for (Supplier supplier : suppliers) {
            supplierItems.add(new ComboBoxItem(supplier.getSupplierId(), supplier.getName()));
        }

        System.out.println("✅ Danh sách nhà cung cấp: " + supplierItems);
    }

    private void setupSupplierAutoComplete() {
        supplierSuggestionList = new JList<>();
        supplierSuggestionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        supplierPopupMenu.add(new JScrollPane(supplierSuggestionList));

        supplierNameTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String text = supplierNameTextField.getText().trim();

                if (text.isEmpty()) {
                    supplierPopupMenu.setVisible(false);
                    return;
                }

                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // Ép kiểu Object sang ComboBoxItem
                    ComboBoxItem selectedValue = (ComboBoxItem) supplierSuggestionList.getSelectedValue();
                    if (selectedValue != null) {
                        supplierNameTextField.setText(selectedValue.getName()); // Gọi phương thức getName()
                        supplierPopupMenu.setVisible(false);
                        autoFillSupplierFields(selectedValue.getId());
                    }
                }

                // Lọc danh sách gợi ý
                List<ComboBoxItem> filtered = supplierItems.stream()
                        .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase())) // Gọi phương thức getName()
                        .collect(Collectors.toList());

                if (filtered.isEmpty()) {
                    supplierPopupMenu.setVisible(false);
                    return;
                }

                showSupplierSuggestionPopup(filtered);
            }
        });

        supplierSuggestionList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Ép kiểu Object sang ComboBoxItem
                ComboBoxItem selectedValue = (ComboBoxItem) supplierSuggestionList.getSelectedValue();
                if (selectedValue != null) {
                    supplierNameTextField.setText(selectedValue.getName()); // Gọi phương thức getName()
                    supplierPopupMenu.setVisible(false);
                    autoFillSupplierFields(selectedValue.getId());
                }
            }
        });

        supplierSuggestionList.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // Ép kiểu Object sang ComboBoxItem
                    ComboBoxItem selectedValue = (ComboBoxItem) supplierSuggestionList.getSelectedValue();
                    if (selectedValue != null) {
                        supplierNameTextField.setText(selectedValue.getName()); // Gọi phương thức getName()
                        supplierPopupMenu.setVisible(false);
                        autoFillSupplierFields(selectedValue.getId());
                    }
                }
            }
        });
    }

    private void showSupplierSuggestionPopup(List<ComboBoxItem> suggestions) {
        supplierPopupMenu.removeAll();

        if (suggestions.isEmpty()) {
            supplierPopupMenu.setVisible(false);
            return;
        }

        supplierSuggestionList.setListData(suggestions.toArray(new ComboBoxItem[0]));
        supplierSuggestionList.setSelectedIndex(0);

        JScrollPane scrollPane = new JScrollPane(supplierSuggestionList);

        int rowHeight = supplierSuggestionList.getFixedCellHeight() > 0 ? supplierSuggestionList.getFixedCellHeight() : 20;
        int maxVisibleRows = 6;
        int popupHeight = Math.min(suggestions.size(), maxVisibleRows) * rowHeight + 10;

        scrollPane.setPreferredSize(new Dimension(supplierNameTextField.getWidth(), popupHeight));

        supplierPopupMenu.add(scrollPane);
        supplierPopupMenu.show(supplierNameTextField, 0, supplierNameTextField.getHeight() + 5);
    }

    private void autoFillSupplierFields(int supplierId) {
        this.supplierId = supplierId; // Thiết lập supplier_id

        // Cập nhật cả 2 trường ID và Name
        supplierIdTextField.setText(String.valueOf(supplierId));

        // Tìm tên nhà cung cấp tương ứng
        supplierItems.stream()
                .filter(item -> item.getId() == supplierId)
                .findFirst()
                .ifPresent(item -> supplierNameTextField.setText(item.getName()));

        // Sau khi có supplier_id, tải danh sách sản phẩm
        loadProductsIntoAutoComplete();
        setupAutoCompleteListener();
    }

    private boolean isStockQuantitySufficient(int productId, int quantityChange) {
        Inventory inventory = inventoryController.getInventoryByProductId(productId);
        int currentStockQuantity = inventory.getStockQuantity();
        return (currentStockQuantity + quantityChange) >= 0;
    }

    private void generateAndOpenStockEntryReport() throws JRException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            CallableStatement cstmt = conn.prepareCall("{call sp_GetStockEntryDetails(?)}");

            cstmt.setInt(1, stockEntryId);

            boolean hasResults = cstmt.execute();

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
                    totalPrice = rsGeneral.getString("total_price");
//                    double totalPriceDouble = rsGeneral.getDouble("total_price");
//                    int totalPriceInt = (int) totalPriceDouble;
//                    totalPrice = String.valueOf(totalPriceInt);
                }

                if (cstmt.getMoreResults()) {
                    ResultSet rsDetails = cstmt.getResultSet();

                    StockEntryReportGenerator reportGenerator = new StockEntryReportGenerator();
                    reportGenerator.generateReport(entryDate, supplierName, employeeName, totalPrice, rsDetails, stockEntryId);
                }
            }
        } catch (SQLException | JRException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tạo báo cáo: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setupTotalPriceCalculation() {
        // Lắng nghe sự thay đổi trong quantityTextField
        quantityTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                calculateTotalPrice();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                calculateTotalPrice();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                calculateTotalPrice();
            }
        });

        // Lắng nghe sự thay đổi trong priceTextField
        priceTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                calculateTotalPrice();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                calculateTotalPrice();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                calculateTotalPrice();
            }
        });
    }

    public void calculateTotalPrice() {
        try {
            // Lấy giá trị từ quantityTextField và priceTextField
            int quantity = Integer.parseInt(quantityTextField.getText());
            double price = Double.parseDouble(priceTextField.getText());

            // Tính toán totalPrice
            double totalPriceDouble = quantity * price;

// Ép kiểu thành int
            int totalPrice = (int) totalPriceDouble;

            // Cập nhật giá trị vào totalPriceTextField
            totalPriceTextField.setText(String.valueOf(totalPrice));
        } catch (NumberFormatException e) {
            // Nếu có lỗi (ví dụ: người dùng nhập không phải số), đặt totalPrice về 0
            totalPriceTextField.setText("0");
        }
    }

    public void initializeData() {
        // Lấy ngày hiện tại và điền vào entryDateTextField
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String currentDate = dateFormat.format(new Date());
        entryDateTextField.setText(currentDate);

        // Tải danh sách nhân viên vào employeeComboBox
        loadEmployeesIntoComboBox();

        // Lấy ID tiếp theo của StockEntry
        int nextStockEntryId = stockEntryController.getNextStockEntryId();
        if (nextStockEntryId == -1) {
            JOptionPane.showMessageDialog(this, "Không thể lấy ID tiếp theo của StockEntry!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Gán giá trị nextStockEntryId vào biến stockEntryId
        this.stockEntryId = nextStockEntryId;

        // Hiển thị ID tiếp theo lên giao diện
        stockEntryIdTextField.setText(String.valueOf(this.stockEntryId));

        // Đặt lại các trường nhập liệu khác về giá trị mặc định
        resetProductFields();
        supplierNameTextField.setText("");
        supplierIdTextField.setText("");
        totalPriceTextField.setText("0");

        // Xóa dữ liệu cũ trong bảng chi tiết nhập hàng
        tempStockEntryDetails.clear();
        stockEntryDetailTableModel.setRowCount(0);

        // Khởi tạo đối tượng StockEntry mới
        this.stockEntry = new StockEntry();
        this.stockEntry.setStockEntryId(this.stockEntryId);

        // Hiển thị thông báo hoặc log (nếu cần)
        System.out.println("✅ Đã khởi tạo một StockEntry mới với ID: " + this.stockEntryId);
    }

    private void setupTableClickListener() {
        stockEntryDetailTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectedRow = stockEntryDetailTable.getSelectedRow(); // Lưu vị trí hàng được chọn
                if (selectedRow >= 0) {
                    // Lấy thông tin từ hàng được chọn
                    int productId = (int) stockEntryDetailTable.getValueAt(selectedRow, 0);
                    String productName = (String) stockEntryDetailTable.getValueAt(selectedRow, 1);
                    String category = (String) stockEntryDetailTable.getValueAt(selectedRow, 2);
                    String unit = (String) stockEntryDetailTable.getValueAt(selectedRow, 3);
                    String barcode = (String) stockEntryDetailTable.getValueAt(selectedRow, 4);
                    int quantity = (int) stockEntryDetailTable.getValueAt(selectedRow, 5);
                    double purchasePrice = (double) stockEntryDetailTable.getValueAt(selectedRow, 6);

                    // Điền thông tin vào các trường nhập liệu
                    productIdTextField.setText(String.valueOf(productId));
                    productNameTextField.setText(productName);
                    categoryComboBox.setSelectedItem(category);
                    unitComboBox.setSelectedItem(unit);
                    barcodeTextField.setText(barcode);
                    quantityTextField.setText(String.valueOf(quantity));
                    priceTextField.setText(String.valueOf(purchasePrice));
                }
            }
        });
    }

    public void updateTotalPrice() {
        double totalDouble = 0;
        for (int i = 0; i < stockEntryDetailTableModel.getRowCount(); i++) {
            int quantity = (int) stockEntryDetailTableModel.getValueAt(i, 5);
            double price = (double) stockEntryDetailTableModel.getValueAt(i, 6);
            totalDouble += quantity * price;
        }
        int total = (int) totalDouble;

        allTotalPriceTextField.setText(String.valueOf(total));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        supplierPopupMenu = new javax.swing.JPopupMenu();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        boxProducts = new javax.swing.JPanel();
        ManageProducts = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        addStockEntryButton = new javax.swing.JButton();
        saveStockEntryBtn = new javax.swing.JButton();
        ProdID2 = new javax.swing.JLabel();
        productIdTextField = new javax.swing.JTextField();
        productNameTextField = new javax.swing.JTextField();
        addProductBtn = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        unitComboBox = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        quantityTextField = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        priceTextField = new javax.swing.JTextField();
        EditStockEntryBtn = new javax.swing.JButton();
        deleteStockEntryBtn = new javax.swing.JButton();
        cancelBtn = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        totalPriceTextField = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        categoryComboBox = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        barcodeTextField = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        allTotalPriceTextField = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        TableProducts = new javax.swing.JScrollPane();
        stockEntryDetailTable = new javax.swing.JTable();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel4 = new javax.swing.JPanel();
        employeeComboBox = new javax.swing.JComboBox<>();
        ProdID1 = new javax.swing.JLabel();
        stockEntryIdTextField = new javax.swing.JTextField();
        openCalendarButton = new javax.swing.JButton();
        ProdID = new javax.swing.JLabel();
        Name = new javax.swing.JLabel();
        supplierIdTextField = new javax.swing.JTextField();
        addSupplier = new javax.swing.JButton();
        supplierNameTextField = new javax.swing.JTextField();
        ProdID3 = new javax.swing.JLabel();
        entryDateTextField = new javax.swing.JFormattedTextField();

        setBackground(new java.awt.Color(255, 255, 255));

        boxProducts.setBackground(new java.awt.Color(255, 255, 255));
        boxProducts.setRequestFocusEnabled(false);

        ManageProducts.setFont(new java.awt.Font("Candara", 1, 18)); // NOI18N
        ManageProducts.setForeground(new java.awt.Color(255, 102, 51));
        ManageProducts.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ManageProducts.setText("PHIẾU NHẬP HÀNG");

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel2.setPreferredSize(new java.awt.Dimension(1074, 142));

        addStockEntryButton.setFont(new java.awt.Font("Candara", 1, 16)); // NOI18N
        addStockEntryButton.setForeground(new java.awt.Color(255, 102, 0));
        addStockEntryButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/addIco.png"))); // NOI18N
        addStockEntryButton.setText("Thêm ");
        addStockEntryButton.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 102, 0)));
        addStockEntryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addStockEntryButtonActionPerformed(evt);
            }
        });

        saveStockEntryBtn.setFont(new java.awt.Font("Candara", 1, 16)); // NOI18N
        saveStockEntryBtn.setForeground(new java.awt.Color(255, 102, 0));
        saveStockEntryBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/saveIcon.png"))); // NOI18N
        saveStockEntryBtn.setText("Lưu");
        saveStockEntryBtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 102, 0)));
        saveStockEntryBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveStockEntryBtnActionPerformed(evt);
            }
        });

        ProdID2.setBackground(new java.awt.Color(255, 102, 0));
        ProdID2.setFont(new java.awt.Font("Candara", 1, 20)); // NOI18N
        ProdID2.setForeground(new java.awt.Color(255, 102, 51));
        ProdID2.setText("Mã HH");

        productIdTextField.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        productNameTextField.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        addProductBtn.setBackground(new java.awt.Color(204, 204, 204));
        addProductBtn.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        addProductBtn.setForeground(new java.awt.Color(255, 102, 0));
        addProductBtn.setText("Thêm hàng");
        addProductBtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 102, 0)));
        addProductBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addProductBtnActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Candara", 1, 20)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 102, 0));
        jLabel8.setText("Số lượng");

        unitComboBox.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        unitComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Gói", "Hộp", "Chai", "Lon", "Lít", "Cái", "Kg", "Túi", "Thanh", "Viên" }));

        jLabel9.setFont(new java.awt.Font("Candara", 1, 20)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 102, 0));
        jLabel9.setText("Đơn vị");

        quantityTextField.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        jLabel10.setFont(new java.awt.Font("Candara", 1, 20)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 102, 0));
        jLabel10.setText("Đơn giá");

        priceTextField.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        priceTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                priceTextFieldActionPerformed(evt);
            }
        });

        EditStockEntryBtn.setFont(new java.awt.Font("Candara", 1, 16)); // NOI18N
        EditStockEntryBtn.setForeground(new java.awt.Color(255, 102, 0));
        EditStockEntryBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/editIcon.png"))); // NOI18N
        EditStockEntryBtn.setText("Sửa");
        EditStockEntryBtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 102, 0)));
        EditStockEntryBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditStockEntryBtnActionPerformed(evt);
            }
        });

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

        jLabel11.setFont(new java.awt.Font("Candara", 1, 20)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 102, 0));
        jLabel11.setText("Tổng tiền");

        totalPriceTextField.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        totalPriceTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                totalPriceTextFieldActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Candara", 1, 20)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 102, 0));
        jLabel12.setText("Loại");

        categoryComboBox.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        categoryComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Thực phẩm khô", "Thực phẩm đông lạnh", "Đồ uống", "Gia vị", "Sữa & sản phẩm từ sữa", "Đồ hộp", "Bánh kẹo", "Rau củ quả", "Hóa mỹ phẩm", "Đồ gia dụng" }));

        jLabel13.setFont(new java.awt.Font("Candara", 1, 20)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 102, 0));
        jLabel13.setText("Mã vạch");

        barcodeTextField.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        jLabel14.setFont(new java.awt.Font("Candara", 1, 20)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 102, 0));
        jLabel14.setText("Thành tiền");

        allTotalPriceTextField.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        allTotalPriceTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                allTotalPriceTextFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(addStockEntryButton, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(saveStockEntryBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ProdID2)
                            .addComponent(jLabel9)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(80, 80, 80)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(87, 87, 87)
                                .addComponent(EditStockEntryBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(deleteStockEntryBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(allTotalPriceTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(productIdTextField, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(unitComboBox, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(categoryComboBox, 0, 346, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 118, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(productNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 430, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(addProductBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                                .addComponent(jLabel8)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                                .addComponent(jLabel13)
                                                .addGap(22, 22, 22)))
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(quantityTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(jLabel10)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(priceTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(26, 26, 26)
                                                .addComponent(jLabel14)
                                                .addGap(18, 18, 18)
                                                .addComponent(totalPriceTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(barcodeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 579, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                        .addGap(19, 19, 19))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ProdID2, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addProductBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(productNameTextField)
                        .addComponent(productIdTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10)
                    .addComponent(priceTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(quantityTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(unitComboBox)
                    .addComponent(jLabel14)
                    .addComponent(totalPriceTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(categoryComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel12))
                    .addComponent(jLabel13)
                    .addComponent(barcodeTextField))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 12, Short.MAX_VALUE)
                        .addComponent(jLabel11)
                        .addGap(9, 9, 9))
                    .addComponent(addStockEntryButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(EditStockEntryBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(deleteStockEntryBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(allTotalPriceTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(saveStockEntryBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                            .addComponent(cancelBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Sản phẩm", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 18), new java.awt.Color(255, 102, 0))); // NOI18N
        jPanel3.setForeground(new java.awt.Color(255, 102, 0));

        TableProducts.setBackground(new java.awt.Color(255, 255, 255));

        stockEntryDetailTable.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        stockEntryDetailTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã", "Tên", "Loại", "Giá", "Đơn vị", "Mã vạch", "Số lượng"
            }
        ));
        stockEntryDetailTable.setRowHeight(25);
        stockEntryDetailTable.setSelectionForeground(new java.awt.Color(255, 102, 0));
        TableProducts.setViewportView(stockEntryDetailTable);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(TableProducts, javax.swing.GroupLayout.PREFERRED_SIZE, 1256, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(10, Short.MAX_VALUE)
                .addComponent(TableProducts, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        employeeComboBox.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        employeeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employeeComboBoxActionPerformed(evt);
            }
        });

        ProdID1.setBackground(new java.awt.Color(255, 102, 0));
        ProdID1.setFont(new java.awt.Font("Candara", 1, 20)); // NOI18N
        ProdID1.setForeground(new java.awt.Color(255, 102, 51));
        ProdID1.setText("Ngày nhập");

        stockEntryIdTextField.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        openCalendarButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/calenIcon.png"))); // NOI18N
        openCalendarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openCalendarButtonActionPerformed(evt);
            }
        });

        ProdID.setBackground(new java.awt.Color(255, 102, 0));
        ProdID.setFont(new java.awt.Font("Candara", 1, 20)); // NOI18N
        ProdID.setForeground(new java.awt.Color(255, 102, 51));
        ProdID.setText("Mã phiếu nhập");

        Name.setBackground(new java.awt.Color(255, 102, 0));
        Name.setFont(new java.awt.Font("Candara", 1, 20)); // NOI18N
        Name.setForeground(new java.awt.Color(255, 102, 51));
        Name.setText("Mã NCC");

        supplierIdTextField.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        supplierIdTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supplierIdTextFieldActionPerformed(evt);
            }
        });

        addSupplier.setBackground(new java.awt.Color(204, 204, 204));
        addSupplier.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        addSupplier.setForeground(new java.awt.Color(255, 102, 0));
        addSupplier.setText("Thêm NCC");
        addSupplier.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 102, 0)));
        addSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addSupplierActionPerformed(evt);
            }
        });

        supplierNameTextField.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        ProdID3.setBackground(new java.awt.Color(255, 102, 0));
        ProdID3.setFont(new java.awt.Font("Candara", 1, 20)); // NOI18N
        ProdID3.setForeground(new java.awt.Color(255, 102, 51));
        ProdID3.setText("Nhân viên");

        entryDateTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(java.text.DateFormat.getDateInstance(java.text.DateFormat.SHORT))));
        entryDateTextField.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        entryDateTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                entryDateTextFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ProdID)
                    .addComponent(Name))
                .addGap(10, 10, 10)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(supplierIdTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
                    .addComponent(stockEntryIdTextField))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 119, Short.MAX_VALUE)
                .addComponent(ProdID3)
                .addGap(21, 21, 21)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(supplierNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 431, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(employeeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(ProdID1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(entryDateTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(openCalendarButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(openCalendarButton, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(supplierNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(addSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(ProdID, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(employeeComboBox)
                                .addComponent(ProdID3)
                                .addComponent(ProdID1)
                                .addComponent(entryDateTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(stockEntryIdTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(Name))
                            .addComponent(supplierIdTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE))))
                .addContainerGap())
        );

        javax.swing.GroupLayout boxProductsLayout = new javax.swing.GroupLayout(boxProducts);
        boxProducts.setLayout(boxProductsLayout);
        boxProductsLayout.setHorizontalGroup(
            boxProductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(boxProductsLayout.createSequentialGroup()
                .addGroup(boxProductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ManageProducts, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(boxProductsLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(boxProductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(boxProductsLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(boxProductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 1299, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(boxProductsLayout.createSequentialGroup()
                                .addGap(32, 32, 32)
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 1135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        boxProductsLayout.setVerticalGroup(
            boxProductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(boxProductsLayout.createSequentialGroup()
                .addComponent(ManageProducts, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1323, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(boxProducts, javax.swing.GroupLayout.DEFAULT_SIZE, 1311, Short.MAX_VALUE)
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

    private void addStockEntryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addStockEntryButtonActionPerformed
        String productIdText = productIdTextField.getText().trim();
        String productName = productNameTextField.getText().trim();
        String category = (String) categoryComboBox.getSelectedItem();
        String unit = (String) unitComboBox.getSelectedItem();
        String barcode = barcodeTextField.getText().trim();
        String quantityText = quantityTextField.getText().trim();
        String priceText = priceTextField.getText().trim();

        // Kiểm tra các trường nhập liệu
        if (productIdText.isEmpty() || productName.isEmpty() || quantityText.isEmpty() || priceText.isEmpty() || barcode.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vui lòng điền đầy đủ thông tin sản phẩm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int productId = Integer.parseInt(productIdText);
            int quantity = Integer.parseInt(quantityText);
            double purchasePrice = Double.parseDouble(priceText);

            // Kiểm tra xem sản phẩm đã tồn tại trong danh sách tạm thời chưa
            boolean isProductExist = tempStockEntryDetails.stream()
                    .anyMatch(detail -> detail.getProductId() == productId);

            if (isProductExist) {
                JOptionPane.showMessageDialog(null, "Sản phẩm đã tồn tại trong danh sách nhập hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Tạo đối tượng StockEntryDetail
            StockEntryDetail detail = new StockEntryDetail();
            detail.setProductId(productId);
            detail.setProductName(productName);
            detail.setCategory(category);
            detail.setUnit(unit);
            detail.setBarcode(barcode);
            detail.setQuantity(quantity);
            detail.setPurchasePrice(purchasePrice);

            // Thêm vào danh sách tạm thời
            tempStockEntryDetails.add(detail);

            // Thêm sản phẩm vào bảng tạm thời
            stockEntryDetailTableModel.addRow(new Object[]{
                productId,
                productName,
                category,
                unit,
                barcode,
                quantity,
                purchasePrice
            });

            // Tính toán và cập nhật totalPrice
            updateTotalPrice();

            // Đặt lại các trường nhập liệu
            resetProductFields();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Số lượng hoặc giá nhập không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_addStockEntryButtonActionPerformed

    private void saveStockEntryBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveStockEntryBtnActionPerformed
        // Kiểm tra danh sách nhập hàng có trống không
        if (tempStockEntryDetails.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Danh sách nhập hàng trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Lấy thông tin nhân viên (giả sử nhân viên có ID = 1)
        int employeeId = CurrentUser.getEmployeeId();

        // Lấy tên nhà cung cấp từ giao diện
        String supplierName = supplierNameTextField.getText().trim();

        // Tìm nhà cung cấp trong danh sách
        ComboBoxItem selectedSupplier = supplierItems.stream()
                .filter(item -> item.getName().equalsIgnoreCase(supplierName))
                .findFirst()
                .orElse(null);

        // Kiểm tra nhà cung cấp có tồn tại không
        if (selectedSupplier == null) {
            JOptionPane.showMessageDialog(null, "Nhà cung cấp không tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Lấy ID nhà cung cấp
        supplierId = selectedSupplier.getId();

        // Kiểm tra ngày nhập hàng
        String entryDateString = entryDateTextField.getText().trim();
        if (entryDateString.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập ngày nhập hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            entryDateTextField.requestFocus(); // Đặt focus vào trường ngày nhập
            return;
        }

        try {
            // Chuyển đổi ngày nhập hàng từ String sang LocalDateTime
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate localDate = LocalDate.parse(entryDateString, formatter);
            LocalDateTime entryDate = localDate.atStartOfDay();

            // Kiểm tra ngày nhập không được trong tương lai
            if (localDate.isAfter(LocalDate.now())) {
                JOptionPane.showMessageDialog(null, "Ngày nhập hàng không được trong tương lai!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                entryDateTextField.requestFocus();
                return;
            }

            // Tạo đối tượng StockEntry
            stockEntry = new StockEntry();
            stockEntry.setSupplierId(supplierId);
            stockEntry.setEmployeeId(employeeId);
            stockEntry.setEntryDate(entryDate);

        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(null, "Định dạng ngày không hợp lệ! Vui lòng nhập theo định dạng dd/MM/yyyy", "Lỗi", JOptionPane.ERROR_MESSAGE);
            entryDateTextField.requestFocus();
            return;
        }

        try {
            // Kiểm tra xem stockEntryId đã tồn tại chưa
            boolean isExistingStockEntry = stockEntryController.isStockEntryExist(stockEntryId);

            if (isExistingStockEntry) {
                // Nếu đã tồn tại, cập nhật thông tin StockEntry
                stockEntry.setStockEntryId(stockEntryId);
                boolean isStockEntryUpdated = stockEntryController.updateStockEntry(stockEntry);

                if (!isStockEntryUpdated) {
                    JOptionPane.showMessageDialog(null, "Lỗi khi cập nhật thông tin nhập kho!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Xử lý các thay đổi tạm thời (xóa các chi tiết đã bị xóa)
                for (StockEntryDetailChange change : pendingChanges) {
                    if (change.getAction().equals("DELETE")) {
                        // Xóa bản ghi khỏi cơ sở dữ liệu
                        boolean isDeleted = stockEntryDetailController.deleteStockEntryDetailByProductId(stockEntryId, change.getProductId());

                        if (isDeleted) {
                            // Cập nhật số lượng tồn kho (giảm đi số lượng đã xóa)
                            inventoryController.updateStockQuantity(change.getProductId(), -change.getQuantity());
                        } else {
                            System.out.println("❌ Lỗi khi xóa chi tiết nhập kho cho Product ID: " + change.getProductId());
                        }
                    }
                }

                // Xóa danh sách thay đổi tạm thời sau khi xử lý
                pendingChanges.clear();

                // Cập nhật hoặc thêm mới các chi tiết nhập hàng
                for (StockEntryDetail detail : tempStockEntryDetails) {
                    detail.setStockEntryId(stockEntryId);
                    StockEntryDetail existingDetail = stockEntryDetailController.getStockEntryDetailByStockEntryIdAndProductId(stockEntryId, detail.getProductId());

                    if (existingDetail != null) {
                        // Lấy giá trị MỚI từ bảng (table model) thay vì từ tempStockEntryDetails
                        for (int i = 0; i < stockEntryDetailTableModel.getRowCount(); i++) {
                            int productId = (int) stockEntryDetailTableModel.getValueAt(i, 0); // Cột mã SP
                            if (productId == detail.getProductId()) {
                                // Lấy giá trị mới từ table
                                int newQuantity = (int) stockEntryDetailTableModel.getValueAt(i, 5); // Cột số lượng
                                double newPrice = (double) stockEntryDetailTableModel.getValueAt(i, 6); // Cột giá nhập

                                // Cập nhật cả temp detail và existing detail
                                detail.setQuantity(newQuantity);
                                detail.setPurchasePrice(newPrice);
                                existingDetail.setQuantity(newQuantity);
                                existingDetail.setPurchasePrice(newPrice);

                                System.out.println("Updating product ID " + productId
                                        + " with new quantity: " + newQuantity
                                        + " and price: " + newPrice);

                                break;
                            }
                        }

                        boolean isUpdated = stockEntryDetailController.updateStockEntryDetail(existingDetail);
                        if (!isUpdated) {
                            System.out.println("❌ Lỗi khi cập nhật chi tiết nhập kho cho Product ID: " + detail.getProductId());
                        }
                    } else {
                        // Nếu chi tiết chưa tồn tại, thêm mới
                        boolean isAdded = stockEntryDetailController.addStockEntryDetail(detail);
                        if (!isAdded) {
                            System.out.println("❌ Lỗi khi thêm chi tiết nhập kho cho Product ID: " + detail.getProductId());
                        }
                    }
                }

                JOptionPane.showMessageDialog(null, "Cập nhật nhập hàng thành công!");
            } else {
                // Nếu chưa tồn tại, thêm mới StockEntry và lấy stock_entry_id vừa tạo
                stockEntryId = stockEntryController.addStockEntry(stockEntry);

                if (stockEntryId <= 0) {
                    JOptionPane.showMessageDialog(null, "Lỗi khi lưu thông tin nhập kho!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Thêm các chi tiết nhập hàng (StockEntryDetail)
                for (StockEntryDetail detail : tempStockEntryDetails) {
                    detail.setStockEntryId(stockEntryId); // Gán stockEntryId mới
                    boolean isAdded = stockEntryDetailController.addStockEntryDetail(detail);

                    if (!isAdded) {
                        JOptionPane.showMessageDialog(null, "Lỗi khi thêm chi tiết nhập kho!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                JOptionPane.showMessageDialog(null, "Nhập hàng thành công!");
            }

            // Hỏi người dùng có muốn xuất hóa đơn không
            try {
                int option = JOptionPane.showConfirmDialog(
                        null,
                        "Bạn có muốn xuất hóa đơn không?",
                        "Xuất hóa đơn",
                        JOptionPane.YES_NO_OPTION
                );

                if (option == JOptionPane.YES_OPTION) {
                    generateAndOpenStockEntryReport(); // Xuất hóa đơn
                } else {
                    System.out.println("Người dùng đã chọn không xuất hóa đơn.");
                }
            } catch (JRException ex) {
                JOptionPane.showMessageDialog(
                        null,
                        "Lỗi khi tạo hóa đơn: " + ex.getMessage(),
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE
                );
            }

            // Refresh lại dữ liệu
            initializeData();
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi nhập hàng: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_saveStockEntryBtnActionPerformed

    private void addProductBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addProductBtnActionPerformed
        String productName = productNameTextField.getText().trim();
        String barcode = barcodeTextField.getText().trim();
        String unit = (String) unitComboBox.getSelectedItem();
        String category = (String) categoryComboBox.getSelectedItem();
        double purchasePrice;

        try {
            purchasePrice = Double.parseDouble(priceTextField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Giá nhập không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Kiểm tra xem sản phẩm đã tồn tại chưa
        ComboBoxItem selectedProduct = productItems.stream()
                .filter(item -> item.getName().equalsIgnoreCase(productName))
                .findFirst()
                .orElse(null);

        int productId;
        if (selectedProduct == null) {
            // Nếu sản phẩm chưa tồn tại, thêm sản phẩm mới vào cơ sở dữ liệu
            double price = purchasePrice * 1.2; // Tính giá bán với lợi nhuận 20%
            productId = stockEntryController.addProductWithStockEntry(productName, supplierId, unit, category, barcode, purchasePrice, price);

            if (productId == -1) {
                JOptionPane.showMessageDialog(null, "Lỗi khi thêm sản phẩm mới!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Thêm sản phẩm mới vào danh sách gợi ý
            ComboBoxItem newProduct = new ComboBoxItem(productId, productName);
            productItems.add(newProduct);

            // Cập nhật danh sách gợi ý
            loadProductsIntoAutoComplete();
        } else {
            // Nếu sản phẩm đã tồn tại, lấy productId
            productId = selectedProduct.getId();
        }

        // Điền productId vào productIdTextField
        productIdTextField.setText(String.valueOf(productId));

        // Hiển thị thông báo thành công
        JOptionPane.showMessageDialog(null, "Sản phẩm đã được thêm thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_addProductBtnActionPerformed

    private void priceTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_priceTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_priceTextFieldActionPerformed

    private void EditStockEntryBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditStockEntryBtnActionPerformed
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn một hàng để chỉnh sửa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Lấy thông tin từ các trường nhập liệu
        String productIdText = productIdTextField.getText().trim();
        String productName = productNameTextField.getText().trim();
        String category = (String) categoryComboBox.getSelectedItem();
        String unit = (String) unitComboBox.getSelectedItem();
        String barcode = barcodeTextField.getText().trim();
        String quantityText = quantityTextField.getText().trim();
        String priceText = priceTextField.getText().trim();

        if (productIdText.isEmpty() || productName.isEmpty() || quantityText.isEmpty() || priceText.isEmpty() || barcode.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vui lòng điền đầy đủ thông tin sản phẩm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int productId = Integer.parseInt(productIdText);
            int quantity = Integer.parseInt(quantityText);
            double purchasePrice = Double.parseDouble(priceText);

            // Cập nhật lại dữ liệu trong bảng tại đúng vị trí hàng đã chọn
            stockEntryDetailTableModel.setValueAt(productId, selectedRow, 0);
            stockEntryDetailTableModel.setValueAt(productName, selectedRow, 1);
            stockEntryDetailTableModel.setValueAt(category, selectedRow, 2);
            stockEntryDetailTableModel.setValueAt(unit, selectedRow, 3);
            stockEntryDetailTableModel.setValueAt(barcode, selectedRow, 4);
            stockEntryDetailTableModel.setValueAt(quantity, selectedRow, 5);
            stockEntryDetailTableModel.setValueAt(purchasePrice, selectedRow, 6);

            // Tính toán và cập nhật totalPrice
            updateTotalPrice();

            // Đặt lại các trường nhập liệu
            resetProductFields();

            // Đặt lại selectedRow về -1 (không có hàng nào được chọn)
            selectedRow = -1;

            // Hiển thị thông báo thành công
            JOptionPane.showMessageDialog(null, "Cập nhật thông tin sản phẩm thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Số lượng hoặc giá nhập không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_EditStockEntryBtnActionPerformed

    private void deleteStockEntryBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteStockEntryBtnActionPerformed
        int row = stockEntryDetailTable.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn một dòng để xóa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int productId = Integer.parseInt(stockEntryDetailTable.getValueAt(row, 0).toString());
        int quantity = Integer.parseInt(stockEntryDetailTable.getValueAt(row, 2).toString());

        if (!isStockQuantitySufficient(productId, -quantity)) {
            JOptionPane.showMessageDialog(
                    null,
                    "Không đủ số lượng tồn kho để xóa sản phẩm có ID: " + productId,
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                null,
                "Bạn có chắc chắn muốn xóa dòng này không?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            // Thêm vào danh sách thay đổi tạm thời (chỉ khi xóa)
            pendingChanges.add(new StockEntryDetailChange(productId, quantity, "DELETE"));

            // Xóa khỏi danh sách tạm thời
            tempStockEntryDetails.removeIf(detail -> detail.getProductId() == productId);

            // Xóa khỏi bảng hiển thị
            ((DefaultTableModel) stockEntryDetailTable.getModel()).removeRow(row);

            JOptionPane.showMessageDialog(null, "Đã xóa dòng dữ liệu tạm thời!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_deleteStockEntryBtnActionPerformed

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
        initializeData();
    }//GEN-LAST:event_cancelBtnActionPerformed

    private void totalPriceTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_totalPriceTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_totalPriceTextFieldActionPerformed

    private void allTotalPriceTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_allTotalPriceTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_allTotalPriceTextFieldActionPerformed

    private void employeeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employeeComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_employeeComboBoxActionPerformed

    private void openCalendarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openCalendarButtonActionPerformed

        showDatePickerDialog();
    }//GEN-LAST:event_openCalendarButtonActionPerformed

    private void supplierIdTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supplierIdTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_supplierIdTextFieldActionPerformed

    private void addSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addSupplierActionPerformed
        String supplierName = supplierNameTextField.getText().trim();
        //        String contactName = contactNameTextField.getText().trim();
        //        String phone = phoneTextField.getText().trim();
        //        String email = emailTextField.getText().trim();
        //        String address = addressTextField.getText().trim();

        if (supplierName.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vui lòng điền đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Kiểm tra xem nhà cung cấp đã tồn tại chưa
        ComboBoxItem selectedSupplier = supplierItems.stream()
                .filter(item -> item.getName().equalsIgnoreCase(supplierName))
                .findFirst()
                .orElse(null);

        if (selectedSupplier != null) {
            // Nếu nhà cung cấp đã tồn tại, điền supplierId vào trường supplierIdTextField
            supplierIdTextField.setText(String.valueOf(selectedSupplier.getId()));
            JOptionPane.showMessageDialog(null, "Nhà cung cấp đã tồn tại!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Nếu nhà cung cấp chưa tồn tại, thêm nhà cung cấp mới
        Supplier newSupplier = new Supplier();
        newSupplier.setName(supplierName);
        //        newSupplier.setContactName(contactName);
        //        newSupplier.setPhone(phone);
        //        newSupplier.setEmail(email);
        //        newSupplier.setAddress(address);

        boolean success = supplierController.addSupplier(newSupplier);
        if (success) {
            JOptionPane.showMessageDialog(null, "Thêm nhà cung cấp thành công!");

            // Lấy supplierId mới và điền vào supplierIdTextField
            int newSupplierId = newSupplier.getSupplierId();
            supplierIdTextField.setText(String.valueOf(newSupplierId));

            // Thêm nhà cung cấp mới vào danh sách gợi ý
            supplierItems.add(new ComboBoxItem(newSupplierId, supplierName));
            loadSuppliersIntoAutoComplete();

            // Thiết lập supplier_id và tải danh sách sản phẩm
            this.supplierId = newSupplierId;
            loadProductsIntoAutoComplete();
            setupAutoCompleteListener();
        } else {
            JOptionPane.showMessageDialog(null, "Lỗi khi thêm nhà cung cấp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_addSupplierActionPerformed

    private void entryDateTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_entryDateTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_entryDateTextFieldActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton EditStockEntryBtn;
    private javax.swing.JLabel ManageProducts;
    private javax.swing.JLabel Name;
    private javax.swing.JLabel ProdID;
    private javax.swing.JLabel ProdID1;
    private javax.swing.JLabel ProdID2;
    private javax.swing.JLabel ProdID3;
    private javax.swing.JScrollPane TableProducts;
    private javax.swing.JButton addProductBtn;
    private javax.swing.JButton addStockEntryButton;
    private javax.swing.JButton addSupplier;
    private javax.swing.JTextField allTotalPriceTextField;
    private javax.swing.JTextField barcodeTextField;
    private javax.swing.JPanel boxProducts;
    private javax.swing.JButton cancelBtn;
    private javax.swing.JComboBox<String> categoryComboBox;
    private javax.swing.JButton deleteStockEntryBtn;
    private javax.swing.JComboBox<ComboBoxItem> employeeComboBox;
    private javax.swing.JFormattedTextField entryDateTextField;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton openCalendarButton;
    private javax.swing.JTextField priceTextField;
    private javax.swing.JTextField productIdTextField;
    private javax.swing.JTextField productNameTextField;
    private javax.swing.JTextField quantityTextField;
    private javax.swing.JButton saveStockEntryBtn;
    private javax.swing.JTable stockEntryDetailTable;
    private javax.swing.JTextField stockEntryIdTextField;
    private javax.swing.JTextField supplierIdTextField;
    private javax.swing.JTextField supplierNameTextField;
    private javax.swing.JPopupMenu supplierPopupMenu;
    private javax.swing.JTextField totalPriceTextField;
    private javax.swing.JComboBox<String> unitComboBox;
    // End of variables declaration//GEN-END:variables
}
