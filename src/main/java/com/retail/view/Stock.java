/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.retail.view;

import com.retail.controller.EmployeeController;
import com.retail.controller.InventoryController;
import com.retail.controller.ProductController;
import com.retail.controller.StockEntryController;
import com.retail.controller.StockEntryDetailController;
import com.retail.controller.SupplierController;
import com.retail.model.ComboBoxItem;
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
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;

/**
 *
 * @author ADMIN
 */
public class Stock extends javax.swing.JFrame {

    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=GroceryStoreDB;encrypt=true;trustServerCertificate=true";
    private static final String USER = "bookoff";
    private static final String PASSWORD = "123456789";

    private ProductController productController;
    private int supplierId; // Thêm biến supplierId
    StockEntryController stockEntryController;
    StockEntryDetailController stockEntryDetailController;
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
     * Creates new form Stock
     */
    public Stock() {

        initComponents();
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        productController = new ProductController();
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
        stockEntryDetailTableModel.addColumn("Product Id");
        stockEntryDetailTableModel.addColumn("Product Name");
        stockEntryDetailTableModel.addColumn("Quantity");
        stockEntryDetailTableModel.addColumn("Purchase Price");

        // Khởi tạo autocomplete cho supplierNameTextField
        setupSupplierAutoComplete();
        loadSuppliersIntoAutoComplete();

        initializeData();

        // Thiết lập sự kiện click vào bảng
        setupTableClickListener();

        // Thiết lập tính toán totalPrice tự động
        setupTotalPriceCalculation();

    }

    private void loadEmployeesIntoComboBox() {
        List<Employee> employees = employeeController.getAllEmployees();
        employeeComboBox.removeAllItems();

        for (Employee employee : employees) {
            employeeComboBox.addItem(new ComboBoxItem(employee.getEmployeeId(), employee.getName()));
        }
    }

    private void loadProductsIntoAutoComplete() {
        List<Product> products = productController.getProductsBySupplierId(supplierId);
        productItems = new ArrayList<>();

        for (Product product : products) {
            productItems.add(new ComboBoxItem(product.getProductId(), product.getName()));
        }

        System.out.println("✅ Danh sách sản phẩm: " + productItems);
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

    private void setupAutoCompleteListener() {
        productNameTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String text = productNameTextField.getText().trim();

                // Nếu người dùng xóa tên sản phẩm
                if (text.isEmpty()) {
                    resetProductFields(); // Đặt lại các trường về giá trị mặc định
                    jPopupMenu1.setVisible(false);
                    return;
                }

                // Nếu người dùng nhấn Enter
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    ComboBoxItem selectedValue = suggestionList.getSelectedValue();
                    if (selectedValue != null) {
                        // Nếu người dùng chọn từ danh sách gợi ý
                        productNameTextField.setText(selectedValue.getName());
                        jPopupMenu1.setVisible(false);
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
                    jPopupMenu1.setVisible(false);
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
                        jPopupMenu1.setVisible(false);
                        autoFillProductFields(selectedValue.getId()); // Điền thông tin sản phẩm
                    }
                }
            }
        });
    }

    private void autoFillProductFields(int productId) {
        Product product = productController.getProductById(productId);
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
        jPopupMenu1.removeAll();

        if (suggestions.isEmpty()) {
            jPopupMenu1.setVisible(false);
            return;
        }

        suggestionList.setListData(suggestions.toArray(new ComboBoxItem[0]));
        suggestionList.setSelectedIndex(0);

        JScrollPane scrollPane = new JScrollPane(suggestionList);

        int rowHeight = suggestionList.getFixedCellHeight() > 0 ? suggestionList.getFixedCellHeight() : 20;
        int maxVisibleRows = 6;
        int popupHeight = Math.min(suggestions.size(), maxVisibleRows) * rowHeight + 10;

        scrollPane.setPreferredSize(new Dimension(productNameTextField.getWidth(), popupHeight));

        jPopupMenu1.add(scrollPane);
        jPopupMenu1.show(productNameTextField, 0, productNameTextField.getHeight() + 5);
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
        Supplier supplier = supplierController.getSupplierById(supplierId);
        if (supplier != null) {
            supplierIdTextField.setText(String.valueOf(supplier.getSupplierId()));

            // Sau khi có supplier_id, tải danh sách sản phẩm
            loadProductsIntoAutoComplete();
            setupAutoCompleteListener();
        } else {
            supplierIdTextField.setText("");
        }
    }

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
                    return false;
                }

                String productName = productNameObj.toString();
                int productId = Integer.parseInt(productIdObj.toString());
                int newQuantity = Integer.parseInt(quantityObj.toString());
                double newPrice = Double.parseDouble(priceObj.toString());

                if (productsToDelete.contains(productId)) {
                    if (!isStockQuantitySufficient(productId, -newQuantity)) {
                        JOptionPane.showMessageDialog(this, "Không đủ số lượng tồn kho để xóa sản phẩm: " + productName, "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }

                    boolean isDeleted = stockEntryDetailController.deleteStockEntryDetailByProductId(stockEntryId, productId);
                    if (!isDeleted) {
                        System.out.println("❌ Lỗi khi xóa chi tiết nhập kho cho Product ID: " + productId);
                        return false;
                    }

                    inventoryController.updateStockQuantity(productId, -newQuantity);
                    System.out.println("✅ Đã xóa chi tiết nhập kho cho Product ID: " + productId);
                    continue;
                }

                StockEntryDetail oldDetail = stockEntryDetailController.getStockEntryDetailByStockEntryIdAndProductId(stockEntryId, productId);
                if (oldDetail == null) {
                    System.out.println("❌ Không tìm thấy chi tiết nhập kho cho Product ID: " + productId);
                    return false;
                }

                int quantityChange = newQuantity - oldDetail.getQuantity();

                if (quantityChange < 0 && !isStockQuantitySufficient(productId, quantityChange)) {
                    JOptionPane.showMessageDialog(this, "Không đủ số lượng tồn kho để cập nhật cho sản phẩm: " + productName, "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                oldDetail.setQuantity(newQuantity);
                oldDetail.setPurchasePrice(newPrice);
                boolean isDetailUpdated = stockEntryDetailController.updateStockEntryDetail(oldDetail);

                if (!isDetailUpdated) {
                    System.out.println("❌ Lỗi khi cập nhật chi tiết nhập kho cho Product ID: " + productId);
                    return false;
                }

                boolean isInventoryUpdated = inventoryController.updateStockQuantity(productId, quantityChange);
                if (!isInventoryUpdated) {
                    System.out.println("❌ Lỗi khi cập nhật số lượng tồn kho cho Product ID: " + productId);
                    return false;
                }

            } catch (NumberFormatException e) {
                System.out.println("❌ Lỗi chuyển đổi số tại hàng " + i + ": " + e.getMessage());
                return false;
            } catch (HeadlessException e) {
                System.out.println("❌ Lỗi không xác định tại hàng " + i + ": " + e.getMessage());
                return false;
            }
        }

        return true;
    }

    private void generateAndOpenStockEntryReport() throws JRException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            CallableStatement cstmt = connection.prepareCall("{call sp_GetStockEntryDetails(?)}");

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
                    double totalPriceDouble = rsGeneral.getDouble("total_price");
                    int totalPriceInt = (int) totalPriceDouble;
                    totalPrice = String.valueOf(totalPriceInt);
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

    private void setupTotalPriceCalculation() {
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

    private void calculateTotalPrice() {
        try {
            // Lấy giá trị từ quantityTextField và priceTextField
            int quantity = Integer.parseInt(quantityTextField.getText());
            double price = Double.parseDouble(priceTextField.getText());

            // Tính toán totalPrice
            double totalPrice = quantity * price;

            // Cập nhật giá trị vào totalPriceTextField
            totalPriceTextField.setText(String.valueOf(totalPrice));
        } catch (NumberFormatException e) {
            // Nếu có lỗi (ví dụ: người dùng nhập không phải số), đặt totalPrice về 0
            totalPriceTextField.setText("0");
        }
    }

    private void initializeData() {
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
                    int quantity = (int) stockEntryDetailTable.getValueAt(selectedRow, 2);
                    double purchasePrice = (double) stockEntryDetailTable.getValueAt(selectedRow, 3);

                    // Điền thông tin vào các trường nhập liệu
                    productIdTextField.setText(String.valueOf(productId));
                    productNameTextField.setText(productName);
                    quantityTextField.setText(String.valueOf(quantity));
                    priceTextField.setText(String.valueOf(purchasePrice));
                }
            }
        });
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
        supplierPopupMenu = new javax.swing.JPopupMenu();
        jPanel1 = new javax.swing.JPanel();
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
        jPanel5 = new javax.swing.JPanel();
        addSupplier1 = new javax.swing.JButton();
        ProdID4 = new javax.swing.JLabel();
        ProdID5 = new javax.swing.JLabel();
        ProdID6 = new javax.swing.JLabel();
        supplierIdTextField1 = new javax.swing.JTextField();
        supplierNameTextField1 = new javax.swing.JTextField();
        contactNameTextField = new javax.swing.JTextField();
        ProdID7 = new javax.swing.JLabel();
        phoneTextField = new javax.swing.JTextField();
        ProdID8 = new javax.swing.JLabel();
        emailTextField = new javax.swing.JTextField();
        ProdID9 = new javax.swing.JLabel();
        addressTextField = new javax.swing.JTextField();
        Close = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(240, 102, 0));

        boxProducts.setRequestFocusEnabled(false);

        ManageProducts.setFont(new java.awt.Font("Candara", 1, 24)); // NOI18N
        ManageProducts.setForeground(new java.awt.Color(255, 102, 51));
        ManageProducts.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ManageProducts.setText("PHIẾU NHẬP HÀNG");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel2.setPreferredSize(new java.awt.Dimension(1074, 142));

        addStockEntryButton.setBackground(new java.awt.Color(204, 204, 204));
        addStockEntryButton.setFont(new java.awt.Font("Candara", 1, 16)); // NOI18N
        addStockEntryButton.setForeground(new java.awt.Color(255, 102, 0));
        addStockEntryButton.setText("Thêm");
        addStockEntryButton.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 102, 0)));
        addStockEntryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addStockEntryButtonActionPerformed(evt);
            }
        });

        saveStockEntryBtn.setBackground(new java.awt.Color(204, 204, 204));
        saveStockEntryBtn.setFont(new java.awt.Font("Candara", 1, 16)); // NOI18N
        saveStockEntryBtn.setForeground(new java.awt.Color(255, 102, 0));
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
        unitComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Kg", "Gói", "Chai", "Hộp", "Lon", "Tuýp" }));

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

        EditStockEntryBtn.setBackground(new java.awt.Color(204, 204, 204));
        EditStockEntryBtn.setFont(new java.awt.Font("Candara", 1, 16)); // NOI18N
        EditStockEntryBtn.setForeground(new java.awt.Color(255, 102, 0));
        EditStockEntryBtn.setText("Sửa");
        EditStockEntryBtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 102, 0)));
        EditStockEntryBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditStockEntryBtnActionPerformed(evt);
            }
        });

        deleteStockEntryBtn.setBackground(new java.awt.Color(204, 204, 204));
        deleteStockEntryBtn.setFont(new java.awt.Font("Candara", 1, 16)); // NOI18N
        deleteStockEntryBtn.setForeground(new java.awt.Color(255, 102, 0));
        deleteStockEntryBtn.setText("Xóa");
        deleteStockEntryBtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 102, 0)));
        deleteStockEntryBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteStockEntryBtnActionPerformed(evt);
            }
        });

        cancelBtn.setBackground(new java.awt.Color(204, 204, 204));
        cancelBtn.setFont(new java.awt.Font("Candara", 1, 16)); // NOI18N
        cancelBtn.setForeground(new java.awt.Color(255, 102, 0));
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
        categoryComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Đồ Uống", "Bánh Kẹo", "Chăm Sóc Nhà Cửa", "Chăm Sóc Cá Nhân", "Gia Vị", "Thực Phẩm Đóng Hộp", "Thực Phẩm", "Gạo", "Mì Ăn Liền" }));

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
                        .addComponent(addStockEntryButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
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
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(EditStockEntryBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(deleteStockEntryBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel11)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(saveStockEntryBtn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(addStockEntryButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(allTotalPriceTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(ProdID3)
                .addGap(21, 21, 21)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(supplierNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 431, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(employeeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(46, 46, 46)
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
                            .addComponent(supplierIdTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE))))
                .addContainerGap())
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        addSupplier1.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        addSupplier1.setForeground(new java.awt.Color(255, 102, 0));
        addSupplier1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/addIco.png"))); // NOI18N
        addSupplier1.setText("Thêm NCC");
        addSupplier1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 102, 0)));
        addSupplier1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addSupplier1ActionPerformed(evt);
            }
        });

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

        supplierIdTextField1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        supplierIdTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supplierIdTextField1ActionPerformed(evt);
            }
        });

        supplierNameTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supplierNameTextField1ActionPerformed(evt);
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

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(0, 1110, Short.MAX_VALUE)
                        .addComponent(addSupplier1, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                                .addComponent(ProdID7, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(emailTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(ProdID8, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(ProdID4, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(12, 12, 12)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(supplierIdTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE)
                                    .addComponent(contactNameTextField))))
                        .addGap(69, 69, 69)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(ProdID5, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(supplierNameTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 588, Short.MAX_VALUE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(ProdID6, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(phoneTextField))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(ProdID9, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(addressTextField)))))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(supplierIdTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                    .addComponent(ProdID4, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ProdID5, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(supplierNameTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ProdID6, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(contactNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(phoneTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ProdID8, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(ProdID7, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(19, 19, 19))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(emailTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ProdID9, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(addressTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(25, 25, 25)))
                .addComponent(addSupplier1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout boxProductsLayout = new javax.swing.GroupLayout(boxProducts);
        boxProducts.setLayout(boxProductsLayout);
        boxProductsLayout.setHorizontalGroup(
            boxProductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(boxProductsLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 1135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(boxProductsLayout.createSequentialGroup()
                .addContainerGap(28, Short.MAX_VALUE)
                .addGroup(boxProductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 1299, Short.MAX_VALUE))
                .addContainerGap(27, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, boxProductsLayout.createSequentialGroup()
                .addComponent(ManageProducts, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(boxProductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(boxProductsLayout.createSequentialGroup()
                    .addGap(43, 43, 43)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGap(44, 44, 44)))
        );
        boxProductsLayout.setVerticalGroup(
            boxProductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(boxProductsLayout.createSequentialGroup()
                .addComponent(ManageProducts, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                .addGap(12, 12, 12)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(12, 12, 12)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40))
            .addGroup(boxProductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(boxProductsLayout.createSequentialGroup()
                    .addGap(280, 280, 280)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(281, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(Close))
                    .addComponent(boxProducts, javax.swing.GroupLayout.DEFAULT_SIZE, 1354, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Close)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(boxProducts, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(213, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void saveStockEntryBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveStockEntryBtnActionPerformed
        if (tempStockEntryDetails.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Danh sách nhập hàng trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int employeeId = 1; // Giả sử nhân viên có ID = 1
        String supplierName = supplierNameTextField.getText().trim();

        ComboBoxItem selectedSupplier = supplierItems.stream()
                .filter(item -> item.getName().equalsIgnoreCase(supplierName))
                .findFirst()
                .orElse(null);

        if (selectedSupplier == null) {
            JOptionPane.showMessageDialog(this, "Nhà cung cấp không tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        supplierId = selectedSupplier.getId();

        stockEntry = new StockEntry();
        stockEntry.setSupplierId(supplierId);
        stockEntry.setEmployeeId(employeeId);

        try {
            // Thêm StockEntry và lấy stock_entry_id vừa tạo
            stockEntryId = stockEntryController.addStockEntry(stockEntry);

            if (stockEntryId <= 0) {
                JOptionPane.showMessageDialog(this, "Lỗi khi lưu thông tin nhập kho!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Thêm các chi tiết nhập hàng (StockEntryDetail)
            for (StockEntryDetail detail : tempStockEntryDetails) {
                detail.setStockEntryId(stockEntryId);
                stockEntryDetailController.addStockEntryDetail(detail);
            }

            JOptionPane.showMessageDialog(this, "Nhập hàng thành công!");
            // Hỏi người dùng có muốn xuất hóa đơn không
            try {
                int option = JOptionPane.showConfirmDialog(
                        this,
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
                        this,
                        "Lỗi khi tạo hóa đơn: " + ex.getMessage(),
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE
                );
            }
            // Refresh lại dữ liệu
            initializeData();
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi nhập hàng: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_saveStockEntryBtnActionPerformed

    private void addStockEntryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addStockEntryButtonActionPerformed
        String productIdText = productIdTextField.getText().trim();
        String productName = productNameTextField.getText().trim();
        String quantityText = quantityTextField.getText().trim();
        String priceText = priceTextField.getText().trim();

        if (productIdText.isEmpty() || productName.isEmpty() || quantityText.isEmpty() || priceText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin sản phẩm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int productId = Integer.parseInt(productIdText);
            int quantity = Integer.parseInt(quantityText);
            double purchasePrice = Double.parseDouble(priceText);

            // Tạo đối tượng StockEntryDetail
            StockEntryDetail detail = new StockEntryDetail();
            detail.setProductId(productId);
            detail.setQuantity(quantity);
            detail.setPurchasePrice(purchasePrice);

            // Thêm vào danh sách tạm thời
            tempStockEntryDetails.add(detail);

            // Thêm sản phẩm vào bảng tạm thời
            stockEntryDetailTableModel.addRow(new Object[]{
                productId,
                productName,
                quantity,
                purchasePrice
            });

            // Tính toán và cập nhật totalPrice
            updateTotalPrice();

            // Đặt lại các trường nhập liệu
            resetProductFields();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số lượng hoặc giá nhập không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_addStockEntryButtonActionPerformed

    private void addSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addSupplierActionPerformed
        String supplierName = supplierNameTextField.getText().trim();
//        String contactName = contactNameTextField.getText().trim();
//        String phone = phoneTextField.getText().trim();
//        String email = emailTextField.getText().trim();
//        String address = addressTextField.getText().trim();

        if (supplierName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
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
            JOptionPane.showMessageDialog(this, "Nhà cung cấp đã tồn tại!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
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
            JOptionPane.showMessageDialog(this, "Thêm nhà cung cấp thành công!");

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
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm nhà cung cấp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_addSupplierActionPerformed

    private void addProductBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addProductBtnActionPerformed
        String productName = productNameTextField.getText().trim();
        String barcode = barcodeTextField.getText().trim();
        String unit = (String) unitComboBox.getSelectedItem();
        String category = (String) categoryComboBox.getSelectedItem();
        double purchasePrice;

        try {
            purchasePrice = Double.parseDouble(priceTextField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá nhập không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
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
            productId = productController.addProductWithStockEntry(productName, supplierId, unit, category, barcode, purchasePrice, price);

            if (productId == -1) {
                JOptionPane.showMessageDialog(this, "Lỗi khi thêm sản phẩm mới!", "Lỗi", JOptionPane.ERROR_MESSAGE);
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
        JOptionPane.showMessageDialog(this, "Sản phẩm đã được thêm thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_addProductBtnActionPerformed

    private void updateTotalPrice() {
        double total = 0;
        for (int i = 0; i < stockEntryDetailTableModel.getRowCount(); i++) {
            int quantity = (int) stockEntryDetailTableModel.getValueAt(i, 2);
            double price = (double) stockEntryDetailTableModel.getValueAt(i, 3);
            total += quantity * price;
        }
        allTotalPriceTextField.setText(String.valueOf(total));
    }
    private void priceTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_priceTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_priceTextFieldActionPerformed

    private void EditStockEntryBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditStockEntryBtnActionPerformed
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một hàng để chỉnh sửa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Lấy thông tin từ các trường nhập liệu
        String productIdText = productIdTextField.getText().trim();
        String productName = productNameTextField.getText().trim();
        String quantityText = quantityTextField.getText().trim();
        String priceText = priceTextField.getText().trim();

        if (productIdText.isEmpty() || productName.isEmpty() || quantityText.isEmpty() || priceText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin sản phẩm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int productId = Integer.parseInt(productIdText);
            int quantity = Integer.parseInt(quantityText);
            double purchasePrice = Double.parseDouble(priceText);

            // Cập nhật lại dữ liệu trong bảng tại đúng vị trí hàng đã chọn
            stockEntryDetailTableModel.setValueAt(productId, selectedRow, 0);
            stockEntryDetailTableModel.setValueAt(productName, selectedRow, 1);
            stockEntryDetailTableModel.setValueAt(quantity, selectedRow, 2);
            stockEntryDetailTableModel.setValueAt(purchasePrice, selectedRow, 3);

            // Tính toán và cập nhật totalPrice
            updateTotalPrice();

            // Đặt lại các trường nhập liệu
            resetProductFields();

            // Đặt lại selectedRow về -1 (không có hàng nào được chọn)
            selectedRow = -1;

            // Hiển thị thông báo thành công
            JOptionPane.showMessageDialog(this, "Cập nhật thông tin sản phẩm thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số lượng hoặc giá nhập không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_EditStockEntryBtnActionPerformed


    private void deleteStockEntryBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteStockEntryBtnActionPerformed
        int row = stockEntryDetailTable.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng để xóa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int productId = Integer.parseInt(stockEntryDetailTable.getValueAt(row, 0).toString());
        int quantity = Integer.parseInt(stockEntryDetailTable.getValueAt(row, 2).toString());

        if (!isStockQuantitySufficient(productId, -quantity)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Không đủ số lượng tồn kho để xóa sản phẩm có ID: " + productId,
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn xóa dòng này không?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            pendingChanges.add(new StockEntryDetailChange(productId, quantity, "DELETE"));
            ((DefaultTableModel) stockEntryDetailTable.getModel()).removeRow(row);
            JOptionPane.showMessageDialog(this, "Đã xóa dòng dữ liệu tạm thời!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_deleteStockEntryBtnActionPerformed

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
        initializeData();
    }//GEN-LAST:event_cancelBtnActionPerformed

    private void totalPriceTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_totalPriceTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_totalPriceTextFieldActionPerformed

    private void entryDateTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_entryDateTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_entryDateTextFieldActionPerformed

    private void openCalendarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openCalendarButtonActionPerformed

        showDatePickerDialog();
    }//GEN-LAST:event_openCalendarButtonActionPerformed

    private void supplierIdTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supplierIdTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_supplierIdTextFieldActionPerformed

    private void employeeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employeeComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_employeeComboBoxActionPerformed

    private void allTotalPriceTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_allTotalPriceTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_allTotalPriceTextFieldActionPerformed

    private void supplierNameTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supplierNameTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_supplierNameTextField1ActionPerformed

    private void supplierIdTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supplierIdTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_supplierIdTextField1ActionPerformed

    private void addSupplier1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addSupplier1ActionPerformed
        String supplierName = supplierNameTextField.getText().trim();
        //        String contactName = contactNameTextField.getText().trim();
        //        String phone = phoneTextField.getText().trim();
        //        String email = emailTextField.getText().trim();
        //        String address = addressTextField.getText().trim();

        if (supplierName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
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
            JOptionPane.showMessageDialog(this, "Nhà cung cấp đã tồn tại!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
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
            JOptionPane.showMessageDialog(this, "Thêm nhà cung cấp thành công!");

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
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm nhà cung cấp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_addSupplier1ActionPerformed

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
            java.util.logging.Logger.getLogger(Stock.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Stock.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Stock.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Stock.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Stock().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Close;
    private javax.swing.JButton EditStockEntryBtn;
    private javax.swing.JLabel ManageProducts;
    private javax.swing.JLabel Name;
    private javax.swing.JLabel ProdID;
    private javax.swing.JLabel ProdID1;
    private javax.swing.JLabel ProdID2;
    private javax.swing.JLabel ProdID3;
    private javax.swing.JLabel ProdID4;
    private javax.swing.JLabel ProdID5;
    private javax.swing.JLabel ProdID6;
    private javax.swing.JLabel ProdID7;
    private javax.swing.JLabel ProdID8;
    private javax.swing.JLabel ProdID9;
    private javax.swing.JScrollPane TableProducts;
    private javax.swing.JButton addProductBtn;
    private javax.swing.JButton addStockEntryButton;
    private javax.swing.JButton addSupplier;
    private javax.swing.JButton addSupplier1;
    private javax.swing.JTextField addressTextField;
    private javax.swing.JTextField allTotalPriceTextField;
    private javax.swing.JTextField barcodeTextField;
    private javax.swing.JPanel boxProducts;
    private javax.swing.JButton cancelBtn;
    private javax.swing.JComboBox<String> categoryComboBox;
    private javax.swing.JTextField contactNameTextField;
    private javax.swing.JButton deleteStockEntryBtn;
    private javax.swing.JTextField emailTextField;
    private javax.swing.JComboBox<ComboBoxItem> employeeComboBox;
    private javax.swing.JFormattedTextField entryDateTextField;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton openCalendarButton;
    private javax.swing.JTextField phoneTextField;
    private javax.swing.JTextField priceTextField;
    private javax.swing.JTextField productIdTextField;
    private javax.swing.JTextField productNameTextField;
    private javax.swing.JTextField quantityTextField;
    private javax.swing.JButton saveStockEntryBtn;
    private javax.swing.JTable stockEntryDetailTable;
    private javax.swing.JTextField stockEntryIdTextField;
    private javax.swing.JTextField supplierIdTextField;
    private javax.swing.JTextField supplierIdTextField1;
    private javax.swing.JTextField supplierNameTextField;
    private javax.swing.JTextField supplierNameTextField1;
    private javax.swing.JPopupMenu supplierPopupMenu;
    private javax.swing.JTextField totalPriceTextField;
    private javax.swing.JComboBox<String> unitComboBox;
    // End of variables declaration//GEN-END:variables
}
