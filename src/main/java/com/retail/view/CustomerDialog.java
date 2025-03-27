package com.retail.view;

import com.retail.dao.CustomerDAO;
import com.retail.dao.CustomerDAOImpl;
import com.retail.model.Customer;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CustomerDialog extends JDialog {
    private JTextField txtName;
    private JTextField txtPhone;
    private JTextField txtEmail;
    private JTextField txtAddress;
    private JLabel lblPhoneError;
    private JLabel lblEmailError;
    private JButton btnSave, btnCancel;
    private Customer customer;

    public CustomerDialog(Frame parent, Customer cust) {
        super(parent, true);
        this.customer = cust;

        setTitle(cust == null ? "Thêm khách hàng" : "Sửa khách hàng");
        setSize(450, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // Đặt chế độ đóng cửa sổ để tự xử lý
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Khi bấm dấu X, gán customer = null và đóng cửa sổ
                customer = null;
                dispose();
            }
        });

        // === Main Panel ===
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(255, 163, 102));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Font fieldFont = new Font("Arial", Font.PLAIN, 14);

        int row = 0;

        // Tên
        gbc.gridx = 0; gbc.gridy = row;
        JLabel lblName = new JLabel("Tên:");
        lblName.setFont(labelFont);
        mainPanel.add(lblName, gbc);

        txtName = new JTextField(20);
        txtName.setFont(fieldFont);
        gbc.gridx = 1;
        mainPanel.add(txtName, gbc);

        // SĐT
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        JLabel lblPhone = new JLabel("SĐT:");
        lblPhone.setFont(labelFont);
        mainPanel.add(lblPhone, gbc);

        txtPhone = new JTextField(20);
        txtPhone.setFont(fieldFont);
        gbc.gridx = 1;
        mainPanel.add(txtPhone, gbc);

        row++;
        gbc.gridx = 1; gbc.gridy = row;
        lblPhoneError = new JLabel(" ");
        lblPhoneError.setForeground(Color.RED);
        mainPanel.add(lblPhoneError, gbc);

        // Email
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(labelFont);
        mainPanel.add(lblEmail, gbc);

        txtEmail = new JTextField(20);
        txtEmail.setFont(fieldFont);
        gbc.gridx = 1;
        mainPanel.add(txtEmail, gbc);

        row++;
        gbc.gridx = 1; gbc.gridy = row;
        lblEmailError = new JLabel(" ");
        lblEmailError.setForeground(Color.RED);
        mainPanel.add(lblEmailError, gbc);

        // Địa chỉ
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        JLabel lblAddress = new JLabel("Địa chỉ:");
        lblAddress.setFont(labelFont);
        mainPanel.add(lblAddress, gbc);

        txtAddress = new JTextField(20);
        txtAddress.setFont(fieldFont);
        gbc.gridx = 1;
        mainPanel.add(txtAddress, gbc);

        // === Button Panel ===
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(255, 163, 102));

        btnSave = new JButton("Cập Nhật");
        btnSave.setFont(new Font("Arial", Font.BOLD, 14));
        btnSave.setBackground(new Color(255, 132, 51));
        btnSave.setForeground(Color.BLACK);
        btnSave.setIcon(new ImageIcon(
                new ImageIcon(getClass().getResource("/images/edit.png"))
                .getImage()
                .getScaledInstance(20, 20, Image.SCALE_SMOOTH)
         ));
        
        btnCancel = new JButton("Hủy");
        btnCancel.setFont(new Font("Arial", Font.BOLD, 14));
        btnCancel.setBackground(new Color(255, 132, 51));
        btnCancel.setForeground(Color.BLACK);
        btnCancel.setIcon(new ImageIcon(
                new ImageIcon(getClass().getResource("/images/cancelIcon.png"))
                .getImage()
                .getScaledInstance(20, 20, Image.SCALE_SMOOTH)
        ));

        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // === Load nếu là sửa ===
        if (cust != null) {
            txtName.setText(cust.getName());
            txtPhone.setText(cust.getPhone());
            txtEmail.setText(cust.getEmail());
            txtAddress.setText(cust.getAddress());
        }

        // === Validation ===
        txtPhone.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { validatePhone(); }
            public void removeUpdate(DocumentEvent e) { validatePhone(); }
            public void changedUpdate(DocumentEvent e) { validatePhone(); }
        });

        txtEmail.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { validateEmail(); }
            public void removeUpdate(DocumentEvent e) { validateEmail(); }
            public void changedUpdate(DocumentEvent e) { validateEmail(); }
        });

        btnSave.addActionListener(e -> {
            if (validateAll()) {
                if (CustomerDialog.this.customer == null) {
                    CustomerDialog.this.customer = new Customer();
                }
                customer.setName(txtName.getText().trim());
                customer.setPhone(txtPhone.getText().trim());
                customer.setEmail(txtEmail.getText().trim());
                customer.setAddress(txtAddress.getText().trim());
                dispose();
            }
        });

        btnCancel.addActionListener(e -> {
            customer = null;
            dispose();
        });
    }

    private void validatePhone() {
        String phone = txtPhone.getText().trim();
        lblPhoneError.setText(" ");
        txtPhone.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border"));

        if (phone.isEmpty()) return;

        if (!phone.matches("^\\d{10,11}$")) {
            txtPhone.setBorder(BorderFactory.createLineBorder(Color.RED));
            lblPhoneError.setText("SĐT không hợp lệ (10-11 số).");
            return;
        }

        CustomerDAO dao = new CustomerDAOImpl();
        Customer existing = dao.getCustomerByPhone(phone);
        if (existing != null && (customer == null || !phone.equals(customer.getPhone()))) {
            txtPhone.setBorder(BorderFactory.createLineBorder(Color.RED));
            lblPhoneError.setText("SĐT đã tồn tại!");
        }
    }

    private void validateEmail() {
        String email = txtEmail.getText().trim();
        lblEmailError.setText(" ");
        txtEmail.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border"));

        if (email.isEmpty()) return;

        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            txtEmail.setBorder(BorderFactory.createLineBorder(Color.RED));
            lblEmailError.setText("Email không hợp lệ!");
            return;
        }

        CustomerDAO dao = new CustomerDAOImpl();
        Customer existing = dao.getCustomerByEmail(email);
        if (existing != null && (customer == null || !email.equals(customer.getEmail()))) {
            txtEmail.setBorder(BorderFactory.createLineBorder(Color.RED));
            lblEmailError.setText("Email đã tồn tại!");
        }
    }

    private boolean validateAll() {
        if (txtName.getText().trim().isEmpty() || txtPhone.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên và SĐT không được để trống!");
            return false;
        }
        return lblPhoneError.getText().trim().isEmpty() && lblEmailError.getText().trim().isEmpty();
    }

    public static Customer showDialog(Component parent, Customer customer) {
        Frame frame = JOptionPane.getFrameForComponent(parent);
        CustomerDialog dialog = new CustomerDialog(frame, customer);
        dialog.setVisible(true);
        return dialog.customer;
    }

    public static Customer showDialog(Component parent) {
        return showDialog(parent, null);
    }

    public static Customer showDialogWithPhone(Component parent, String phone) {
        Customer cust = new Customer();
        cust.setPhone(phone);
        return showDialog(parent, cust);
    }
}
