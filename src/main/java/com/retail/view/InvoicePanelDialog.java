package com.retail.view;

import com.retail.dao.CustomerDAO;
import com.retail.dao.CustomerDAOImpl;
import com.retail.dao.DatabaseConnection;
import com.retail.dao.EmployeeDAO;
import com.retail.dao.EmployeeDAOImpl;
import com.retail.dao.InvoiceDAO;
import com.retail.dao.InvoiceDetailDAO;
import com.retail.dao.ProductDAO;
import com.retail.dao.InventoryDAO;
import com.retail.dao.InventoryDAOImpl;
import com.retail.dao.InvoiceDAOImpl;
import com.retail.dao.InvoiceDetailDAOImpl;
import com.retail.dao.ProductDAOImpl;
import com.retail.model.Customer;
import com.retail.model.Employee;
import com.retail.model.Invoice;
import com.retail.model.InvoiceDetail;
import com.retail.model.Product;
import com.retail.model.Inventory;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import com.retail.view.ProductPanel;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JRException;

public class InvoicePanelDialog {

    private InvoicePanel invoicePanel;
    private Customer selectedCustomer;
    private int currentInvoiceId = 0;

    public InvoicePanelDialog(InvoicePanel invoicePanel) {
        this.invoicePanel = invoicePanel;
        initLogic();
    }

    private void initLogic() {
        // Nạp danh sách nhân viên vào combo box
        loadEmployees();

        // Xử lý tra cứu khách hàng khi nhập SĐT bằng Enter
        invoicePanel.getTxtCustomerPhone().addActionListener(e -> {
            String phone = invoicePanel.getTxtCustomerPhone().getText().trim();
            if (!phone.isEmpty()) {
                CustomerDAO customerDAO = new CustomerDAOImpl();
                Customer cust = customerDAO.getCustomerByPhone(phone);
                if (cust != null) {
                    selectedCustomer = cust;
                    invoicePanel.getLblCustomerName().setText(cust.getName());
                    invoicePanel.getLblCustomerEmail().setText(cust.getEmail());
                    invoicePanel.getLblCustomerAddress().setText(cust.getAddress());
                } else {
                    JOptionPane.showMessageDialog(invoicePanel, "Không tìm thấy khách hàng với SĐT này!");
                    int option = JOptionPane.showConfirmDialog(invoicePanel,
                            "Khách hàng không tồn tại. Bạn có muốn thêm mới không?",
                            "Thông báo", JOptionPane.YES_NO_OPTION);
                    if (option == JOptionPane.YES_OPTION) {
                        Customer newCust = CustomerDialog.showDialogWithPhone(invoicePanel, phone);
                        if (newCust != null) {
                            CustomerDAO dao = new CustomerDAOImpl();
                            boolean added = dao.addCustomer(newCust);
                            if (added) {
                                selectedCustomer = newCust;
                                invoicePanel.getTxtCustomerPhone().setText(newCust.getPhone());
                                invoicePanel.getLblCustomerName().setText(newCust.getName());
                                invoicePanel.getLblCustomerEmail().setText(newCust.getEmail());
                                invoicePanel.getLblCustomerAddress().setText(newCust.getAddress());
                                JOptionPane.showMessageDialog(invoicePanel, "Thêm khách hàng thành công!");
                            } else {
                                JOptionPane.showMessageDialog(invoicePanel, "Có lỗi khi thêm khách hàng vào DB!");
                            }
                        }
                    } else {
                        selectedCustomer = null;
                        invoicePanel.getLblCustomerName().setText("");
                        invoicePanel.getLblCustomerEmail().setText("");
                        invoicePanel.getLblCustomerAddress().setText("");
                    }
                }
            }
        });

        // Xử lý focusLost cho ô SĐT để xóa thông tin khi trống
        invoicePanel.getTxtCustomerPhone().getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (invoicePanel.getTxtCustomerPhone().getText().trim().isEmpty()) {
                    selectedCustomer = null;
                    invoicePanel.getLblCustomerName().setText("");
                    invoicePanel.getLblCustomerEmail().setText("");
                    invoicePanel.getLblCustomerAddress().setText("");
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });

        // Xử lý mã vạch sản phẩm
        invoicePanel.getTxtBarcode().addActionListener(e -> invoicePanel.getBtnAddProduct().doClick());

        invoicePanel.getBtnAddProduct().addActionListener(e -> {
            String barcode = invoicePanel.getTxtBarcode().getText().trim();
            if (barcode.isEmpty()) {
                JOptionPane.showMessageDialog(invoicePanel, "Vui lòng nhập mã vạch!");
                return;
            }
            ProductDAO productDAO = new ProductDAOImpl();
            Product product = productDAO.getProductByBarcode(barcode);
            if (product == null) {
                JOptionPane.showMessageDialog(invoicePanel, "Không tìm thấy sản phẩm với mã vạch: " + barcode);
                return;
            }
            InventoryDAO inventoryDAO = new InventoryDAOImpl();
            int stock = 0;
            try {
                Inventory inv = inventoryDAO.getInventoryByProductId(product.getProductId());
                if (inv != null) {
                    stock = inv.getStockQuantity();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            DefaultTableModel detailTableModel = invoicePanel.getDetailTableModel();
            boolean found = false;
            for (int i = 0; i < detailTableModel.getRowCount(); i++) {
                int pid = Integer.parseInt(detailTableModel.getValueAt(i, 0).toString());
                if (pid == product.getProductId()) {
                    int currentQty = Integer.parseInt(detailTableModel.getValueAt(i, 3).toString());
                    if (currentQty + 1 > stock) {
                        JOptionPane.showMessageDialog(invoicePanel, "Không đủ hàng, tồn kho là " + stock);
                        found = true;
                        break;
                    } else {
                        int newQty = currentQty + 1;
                        detailTableModel.setValueAt(newQty, i, 3);
                        BigDecimal price = BigDecimal.valueOf(product.getPrice());
                        BigDecimal newSubtotal = price.multiply(new BigDecimal(newQty));
                        detailTableModel.setValueAt(newSubtotal, i, 5);
                        found = true;
                        break;
                    }
                }
            }
            if (!found) {
                if (1 > stock) {
                    JOptionPane.showMessageDialog(invoicePanel, "Không đủ hàng, tồn kho là " + stock);
                    return;
                }
                BigDecimal price = BigDecimal.valueOf(product.getPrice());
                BigDecimal subtotal = price;
                Object[] rowData = new Object[]{
                    product.getProductId(),
                    product.getName(),
                    stock,
                    1,
                    price,
                    subtotal,
                    "Xóa"
                };
                detailTableModel.addRow(rowData);
            }
            updateSummary();
            invoicePanel.getTxtBarcode().setText("");
        });

        // Cài đặt editor cho cột Quantity
        JTextField quantityField = new JTextField();
        quantityField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (invoicePanel.getDetailTable().isEditing()) {
                    invoicePanel.getDetailTable().getCellEditor().stopCellEditing();
                }
            }
        });
        quantityField.getDocument().addDocumentListener(new DocumentListener() {
            private void updateEditingRow() {
                if (invoicePanel.getDetailTable().isEditing() && invoicePanel.getDetailTable().getEditingColumn() == 3) {
                    int editingRow = invoicePanel.getDetailTable().getEditingRow();
                    String text = quantityField.getText();
                    try {
                        int quantity = Integer.parseInt(text);
                        int stock = Integer.parseInt(invoicePanel.getDetailTableModel().getValueAt(editingRow, 2).toString());
                        if (quantity > stock) {
                            quantity = stock;
                        }
                        BigDecimal price = new BigDecimal(invoicePanel.getDetailTableModel().getValueAt(editingRow, 4).toString());
                        BigDecimal newSubtotal = price.multiply(new BigDecimal(quantity));
                        invoicePanel.getDetailTable().setValueAt(newSubtotal, editingRow, 5);
                        updateSummary();
                    } catch (NumberFormatException ex) {
                    }
                }
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                updateEditingRow();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateEditingRow();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateEditingRow();
            }
        });
        DefaultCellEditor quantityEditor = new DefaultCellEditor(quantityField);
        invoicePanel.getDetailTable().getColumnModel().getColumn(3).setCellEditor(quantityEditor);

        // Xử lý nút "Delete" trong bảng
        invoicePanel.getDetailTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = invoicePanel.getDetailTable().rowAtPoint(e.getPoint());
                int col = invoicePanel.getDetailTable().columnAtPoint(e.getPoint());
                if (col == 6) {
                    int confirm = JOptionPane.showConfirmDialog(invoicePanel, "Bạn có chắc muốn xoá dòng này?", "Xác nhận xoá", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        invoicePanel.getDetailTableModel().removeRow(row);
                        updateSummary();
                    }
                }
            }
        });

        // Lắng nghe thay đổi số lượng trong bảng
        invoicePanel.getDetailTableModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    int col = e.getColumn();
                    if (col == 3) {
                        int row = e.getFirstRow();
                        updateRow(row);
                        updateSummary();
                    }
                }
            }
        });

        invoicePanel.getTxtDiscount().getDocument().addDocumentListener(new DocumentListener() {
            private void validateDiscount() {
                try {
                    BigDecimal discountValue = new BigDecimal(invoicePanel.getTxtDiscount().getText());
                    if (discountValue.compareTo(new BigDecimal("100")) > 0) {
                        JOptionPane.showMessageDialog(invoicePanel, "Discount không được vượt quá 100%", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        invoicePanel.getTxtDiscount().setText("100");
                    }
                } catch (NumberFormatException ex) {
                }
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                validateDiscount();
                updateSummary();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                validateDiscount();
                updateSummary();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                validateDiscount();
                updateSummary();
            }
        });

        invoicePanel.getBtnEditInvoice().addActionListener(e -> editSelectedInvoice());
        invoicePanel.getBtnSearchInvoiceByPhone().addActionListener(e -> searchInvoiceByPhone());
        invoicePanel.getBtnViewInvoiceDetails().addActionListener(e -> {
            try {
                viewInvoiceDetails();
            } catch (JRException ex) {
                Logger.getLogger(InvoicePanelDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        invoicePanel.getInvoiceListTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    try {
                        viewInvoiceDetails();
                    } catch (JRException ex) {
                        Logger.getLogger(InvoicePanelDialog.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
    }

    private void loadEmployees() {
        EmployeeDAOImpl employeeDAO = new EmployeeDAOImpl();
        List<Employee> employees = employeeDAO.getAllEmployees();
        JComboBox<Employee> cmbEmployee = invoicePanel.getCmbEmployee(); // Đảm bảo là JComboBox<Employee>

        cmbEmployee.removeAllItems();

        // Kiểm tra danh sách có rỗng không
        if (employees == null || employees.isEmpty()) {
            System.out.println("Không có nhân viên nào để hiển thị!");
            return;
        }

        for (Employee emp : employees) {
            cmbEmployee.addItem(emp);
            System.out.println(emp); // Debug xem Employee hiển thị đúng không
        }
    }

    private void updateRow(int row) {
        try {
            int productId = Integer.parseInt(invoicePanel.getDetailTableModel().getValueAt(row, 0).toString());
            int quantity = Integer.parseInt(invoicePanel.getDetailTableModel().getValueAt(row, 3).toString());
            int stock = Integer.parseInt(invoicePanel.getDetailTableModel().getValueAt(row, 2).toString());
            if (quantity > stock) {
                JOptionPane.showMessageDialog(invoicePanel, "Số lượng không được vượt quá tồn kho (" + stock + ") cho sản phẩm ID " + productId, "Lỗi", JOptionPane.ERROR_MESSAGE);
                invoicePanel.getDetailTableModel().setValueAt(stock, row, 3);
                quantity = stock;
            }
            ProductDAO productDAO = new ProductDAOImpl();
            Product product = productDAO.getProductById(productId);
            if (product == null) {
                JOptionPane.showMessageDialog(invoicePanel, "Không tìm thấy sản phẩm với ID " + productId, "Lỗi", JOptionPane.ERROR_MESSAGE);
                invoicePanel.getDetailTableModel().setValueAt(0, row, 4);
                invoicePanel.getDetailTableModel().setValueAt(0, row, 5);
                return;
            }
            BigDecimal price = BigDecimal.valueOf(product.getPrice());
            BigDecimal subtotal = price.multiply(new BigDecimal(quantity));
            invoicePanel.getDetailTableModel().setValueAt(price, row, 4);
            invoicePanel.getDetailTableModel().setValueAt(subtotal, row, 5);
        } catch (NumberFormatException ex) {
        }
    }

    private void updateSummary() {
        BigDecimal totalAmount = BigDecimal.ZERO;
        DefaultTableModel model = invoicePanel.getDetailTableModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            try {
                BigDecimal subtotal = new BigDecimal(model.getValueAt(i, 5).toString());
                totalAmount = totalAmount.add(subtotal);
            } catch (NumberFormatException ex) {
            }
        }
        BigDecimal discountPercentage;
        try {
            discountPercentage = new BigDecimal(invoicePanel.getTxtDiscount().getText());
        } catch (NumberFormatException ex) {
            discountPercentage = BigDecimal.ZERO;
        }
        BigDecimal discountAmount = totalAmount.multiply(discountPercentage).divide(new BigDecimal("100"));
        BigDecimal finalAmount = totalAmount.subtract(discountAmount);
        invoicePanel.getLblTotalAmount().setText("THÀNH TIỀN: " + totalAmount);
        invoicePanel.getLblFinalAmount().setText("TỔNG TIỀN: " + finalAmount);
    }

    public Invoice getInvoiceData() {
        if (selectedCustomer == null) {
            JOptionPane.showMessageDialog(invoicePanel, "Vui lòng nhập SĐT hợp lệ để tra cứu khách hàng!");
            return null;
        }
        String paymentMethod = invoicePanel.getCmbPaymentMethod().getSelectedItem().toString();
        BigDecimal discountPercentage;
        try {
            discountPercentage = new BigDecimal(invoicePanel.getTxtDiscount().getText());
        } catch (NumberFormatException ex) {
            discountPercentage = BigDecimal.ZERO;
        }
        BigDecimal totalAmount = BigDecimal.ZERO;
        DefaultTableModel model = invoicePanel.getDetailTableModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            try {
                BigDecimal subtotal = new BigDecimal(model.getValueAt(i, 5).toString());
                totalAmount = totalAmount.add(subtotal);
            } catch (NumberFormatException ex) {
            }
        }
        BigDecimal discountAmount = totalAmount.multiply(discountPercentage).divide(new BigDecimal("100"));
        BigDecimal finalAmount = totalAmount.subtract(discountAmount);
        Invoice invoice = new Invoice();
        invoice.setCustomerId(selectedCustomer.getCustomerId());
        Employee selectedEmp = (Employee) invoicePanel.getCmbEmployee().getSelectedItem();
        invoice.setEmployeeId(selectedEmp.getEmployeeId());
        invoice.setPaymentMethod(paymentMethod);
        invoice.setDiscount(discountPercentage);
        invoice.setTotalAmount(totalAmount);
        invoice.setFinalAmount(finalAmount);
        if (currentInvoiceId != 0) {
            invoice.setInvoiceId(currentInvoiceId);
        }
        return invoice;
    }

    public List<InvoiceDetail> getInvoiceDetails() {
        List<InvoiceDetail> details = new ArrayList<>();
        DefaultTableModel model = invoicePanel.getDetailTableModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            try {
                int productId = Integer.parseInt(model.getValueAt(i, 0).toString());
                int quantity = Integer.parseInt(model.getValueAt(i, 3).toString());
                BigDecimal price = new BigDecimal(model.getValueAt(i, 4).toString());
                BigDecimal subtotal = new BigDecimal(model.getValueAt(i, 5).toString());
                InvoiceDetail detail = new InvoiceDetail();
                detail.setProductId(productId);
                detail.setQuantity(quantity);
                detail.setPrice(price);
                detail.setSubtotal(subtotal);
                details.add(detail);
            } catch (NumberFormatException ex) {
            }
        }
        return details;
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(invoicePanel, message);
    }

    // Sau khi tạo hóa đơn thành công, gọi resetForm để làm mới form và cập nhật bảng sản phẩm
    public void resetForm() {
        invoicePanel.getTxtCustomerPhone().setText("");
        invoicePanel.getCmbEmployee().setSelectedIndex(0);
        invoicePanel.getCmbPaymentMethod().setSelectedIndex(0);
        invoicePanel.getTxtDiscount().setText("0");
        invoicePanel.getDetailTableModel().setRowCount(0);
        updateSummary();
        invoicePanel.getLblCustomerName().setText("");
        invoicePanel.getLblCustomerEmail().setText("");
        invoicePanel.getLblCustomerAddress().setText("");
        selectedCustomer = null;
        currentInvoiceId = 0;
        invoicePanel.getBtnCreateInvoice().setText("Tạo hóa đơn");

        // Cập nhật lại bảng sản phẩm trong ProductPanel sau khi tồn kho được cập nhật
        if (ProductPanel.getInstance() != null) {
            ProductPanel.getInstance().getService().loadProductData();
        }
    }

    public void loadInvoiceList() {
        String[] columnNames = {"Invoice ID", "Khách ", "Nhân viên", "Tổng tiền", "Discount", "Thành tiền", "Phương thức", "Ngày tạo"};
        DefaultTableModel model = invoicePanel.getInvoiceListTableModel();
        model.setDataVector(new Object[0][0], columnNames);
        InvoiceDAO invoiceDAO = new InvoiceDAOImpl();
        CustomerDAO customerDAO = new CustomerDAOImpl();
        EmployeeDAO employeeDAO = new EmployeeDAOImpl();
        List<Invoice> invoices = invoiceDAO.getAllInvoices();
        for (Invoice inv : invoices) {
            String custName = "";
            Customer cust = customerDAO.getCustomerById(inv.getCustomerId());
            if (cust != null) {
                custName = cust.getName();
            }
            String empName = "";
            Employee emp = employeeDAO.getEmployeeById(inv.getEmployeeId());
            if (emp != null) {
                empName = emp.getName();
            }
            model.addRow(new Object[]{
                inv.getInvoiceId(),
                custName,
                empName,
                inv.getTotalAmount(),
                inv.getDiscount(),
                inv.getFinalAmount(),
                inv.getPaymentMethod(),
                inv.getCreatedAt()
            });
        }
    }

    private void viewInvoiceDetails() throws JRException {
        int selectedRow = invoicePanel.getInvoiceListTable().getSelectedRow();
        if (selectedRow == -1) {
            showMessage("Vui lòng chọn một hóa đơn từ danh sách.");
            return;
        }

        int invoiceId = Integer.parseInt(invoicePanel.getInvoiceListTableModel()
                .getValueAt(selectedRow, 0).toString());

        try (Connection conn = DatabaseConnection.getConnection()) {
            // Gọi stored procedure lấy thông tin hóa đơn
            CallableStatement cstmt = conn.prepareCall("{call sp_GetInvoiceDetails(?)}");
            cstmt.setInt(1, invoiceId);

            // Thực thi stored procedure
            boolean hasResults = cstmt.execute();

            // Xử lý ResultSet đầu tiên (thông tin chung của hóa đơn)
            String customerName = null;
            String employeeName = null;
            String totalAmount = null;
            String discount = null;
            String finalAmount = null;
            String paymentMethod = null;
            String createdAt = null;

            if (hasResults) {
                ResultSet rsGeneral = cstmt.getResultSet();
                if (rsGeneral.next()) {
                    customerName = rsGeneral.getString("customer_name");
                    employeeName = rsGeneral.getString("employee_name");
                    totalAmount = rsGeneral.getString("total_amount");
                    discount = rsGeneral.getString("discount");
                    finalAmount = rsGeneral.getString("final_amount");
                    paymentMethod = rsGeneral.getString("payment_method");
                    createdAt = rsGeneral.getString("created_at");
                }
            }

            // Xử lý ResultSet thứ hai (thông tin chi tiết hóa đơn)
            List<InvoiceDetail> details = new ArrayList<>();
            if (cstmt.getMoreResults()) {
                ResultSet rsDetails = cstmt.getResultSet();
                while (rsDetails.next()) {
                    String productName = rsDetails.getString("product_name");
                    int quantity = rsDetails.getInt("quantity");
                    BigDecimal unitPrice = rsDetails.getBigDecimal("unit_price");
                    BigDecimal subtotal = rsDetails.getBigDecimal("subtotal");

                    details.add(new InvoiceDetail(productName, quantity, unitPrice, subtotal));
                }
            }

            // Tạo báo cáo PDF
            InvoiceReportGenerator reportGenerator = new InvoiceReportGenerator();

            reportGenerator.generateReport(createdAt, customerName, employeeName,
                    totalAmount, discount, finalAmount, paymentMethod,
                    details, invoiceId);

        } catch (SQLException | JRException e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi tạo hóa đơn: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void fetchCustomerInfoByPhone() {
        String phone = invoicePanel.getTxtCustomerPhone().getText().trim();
        if (!phone.isEmpty()) {
            CustomerDAO customerDAO = new CustomerDAOImpl();
            Customer cust = customerDAO.getCustomerByPhone(phone);
            if (cust != null) {
                selectedCustomer = cust;
                invoicePanel.getLblCustomerName().setText(cust.getName());
                invoicePanel.getLblCustomerEmail().setText(cust.getEmail());
                invoicePanel.getLblCustomerAddress().setText(cust.getAddress());
            } else {
                JOptionPane.showMessageDialog(invoicePanel, "Không tìm thấy khách hàng với SĐT này!");
                selectedCustomer = null;
            }
        }
    }

    private void editSelectedInvoice() {
        int selectedRow = invoicePanel.getInvoiceListTable().getSelectedRow();
        if (selectedRow == -1) {
            showMessage("Vui lòng chọn hóa đơn cần sửa!");
            return;
        }

        int invoiceId = Integer.parseInt(invoicePanel.getInvoiceListTableModel().getValueAt(selectedRow, 0).toString());
        InvoiceDAO invoiceDAO = new InvoiceDAOImpl();
        Invoice invoice = invoiceDAO.getInvoiceById(invoiceId);
        InvoiceDetailDAO detailDAO = new InvoiceDetailDAOImpl();
        List<InvoiceDetail> details = detailDAO.getInvoiceDetailsByInvoiceId(invoiceId);

        // Phục hồi tồn kho cho hóa đơn cũ
        InventoryDAO inventoryDAO = new InventoryDAOImpl();
        for (InvoiceDetail detail : details) {
            Inventory inv = inventoryDAO.getInventoryByProductId(detail.getProductId());
            if (inv != null) {
                int newStock = inv.getStockQuantity() + detail.getQuantity();
                inv.setStockQuantity(newStock);
                inventoryDAO.updateInventory(inv);
            }
        }

        if (invoice != null) {
            CustomerDAO customerDAO = new CustomerDAOImpl();
            Customer cust = customerDAO.getCustomerById(invoice.getCustomerId());
            if (cust != null) {
                invoicePanel.getTxtCustomerPhone().setText(cust.getPhone());

                // 👉 Gọi lại như bấm "Lấy thông tin"
                fetchCustomerInfoByPhone();
            }
        }

        EmployeeDAO empDAO = new EmployeeDAOImpl();
        Employee emp = empDAO.getEmployeeById(invoice.getEmployeeId());
        if (emp != null) {
            invoicePanel.getCmbEmployee().setSelectedItem(emp);
        }

        invoicePanel.getCmbPaymentMethod().setSelectedItem(invoice.getPaymentMethod());
        invoicePanel.getTxtDiscount().setText(invoice.getDiscount().toString());

        invoicePanel.getDetailTableModel().setRowCount(0);
        for (InvoiceDetail detail : details) {
            Product product = new ProductDAOImpl().getProductById(detail.getProductId());
            int stock = 0;
            Inventory inv = inventoryDAO.getInventoryByProductId(detail.getProductId());
            if (inv != null) {
                stock = inv.getStockQuantity();
            }
            BigDecimal price = BigDecimal.valueOf(product.getPrice());
            BigDecimal subtotal = price.multiply(new BigDecimal(detail.getQuantity()));
            Object[] rowData = new Object[]{
                product.getProductId(),
                product.getName(),
                stock,
                detail.getQuantity(),
                price,
                subtotal,
                "Xóa"
            };
            invoicePanel.getDetailTableModel().addRow(rowData);
        }

        updateSummary();
        currentInvoiceId = invoice.getInvoiceId();
        invoicePanel.getBtnCreateInvoice().setText("Cập nhật hóa đơn");
    }

    private void searchInvoiceByPhone() {
        String phone = invoicePanel.getTxtCustomerPhone().getText().trim();
        if (phone.isEmpty()) {
            JOptionPane.showMessageDialog(invoicePanel, "Vui lòng nhập SĐT cần tìm!");
            return;
        }
        CustomerDAO customerDAO = new CustomerDAOImpl();
        Customer cust = customerDAO.getCustomerByPhone(phone);
        if (cust == null) {
            JOptionPane.showMessageDialog(invoicePanel, "Không tìm thấy khách hàng với SĐT này!");
            return;
        }
        InvoiceDAO invoiceDAO = new InvoiceDAOImpl();
        List<Invoice> invoices = invoiceDAO.getInvoicesByCustomerId(cust.getCustomerId());
        DefaultTableModel model = invoicePanel.getInvoiceListTableModel();
        model.setRowCount(0);
        EmployeeDAO employeeDAO = new EmployeeDAOImpl();
        for (Invoice inv : invoices) {
            String custName = "";
            Customer c = customerDAO.getCustomerById(inv.getCustomerId());
            if (c != null) {
                custName = c.getName();
            }
            String empName = "";
            Employee emp = employeeDAO.getEmployeeById(inv.getEmployeeId());
            if (emp != null) {
                empName = emp.getName();
            }
            model.addRow(new Object[]{
                inv.getInvoiceId(),
                custName,
                empName,
                inv.getTotalAmount(),
                inv.getDiscount(),
                inv.getFinalAmount(),
                inv.getPaymentMethod(),
                inv.getCreatedAt()
            });
        }
    }
}
