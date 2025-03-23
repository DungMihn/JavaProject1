/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Market;

/**
 *
 * @author ADMIN
 */
import com.retail.controller.EmployeeController;
import com.retail.model.Employee;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;


public class EmployeePanel extends javax.swing.JPanel {

    private final EmployeeController employeeController = new EmployeeController();
    
    
    public EmployeePanel() {
        initComponents();
        loadEmployeeTable();
        initializeData();
        setupTableClickListener();
        // Thêm DocumentListener để lắng nghe sự kiện nhập liệu
        SearchEmployeeForm.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterEmployeeByName(); // Gọi khi có ký tự được thêm vào
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterEmployeeByName();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // Không cần xử lý trong trường hợp này
            }
        });
    }
    
    private void filterEmployeeByName() {
    String keyword = SearchEmployeeForm.getText().trim();
    
    // Lấy mô hình bảng từ JTable
    DefaultTableModel model = (DefaultTableModel) EmployeeTable.getModel();
    model.setRowCount(0); // Xóa dữ liệu cũ

    List<Employee> employees;

    // Nếu ô tìm kiếm trống, lấy toàn bộ nhân viên
    if (keyword.isEmpty()) {
        employees = employeeController.getAllEmployees();
    } else {
        employees = employeeController.searchEmployeesByName(keyword);
    }

    // Thêm dữ liệu vào bảng
    for (Employee employee : employees) {
        model.addRow(new Object[]{
            employee.getEmployeeID(),
            employee.getName(),
            employee.getPhone(),
            employee.getRole()
        });
    }
}

    
    private void loadEmployeeTable() {
        List<com.retail.model.Employee> employees = employeeController.getAllEmployees();
        DefaultTableModel model = (DefaultTableModel) EmployeeTable.getModel();
        model.setRowCount(0); // Xóa dữ liệu cũ trước khi tải mới

        for (com.retail.model.Employee emp : employees) {
            Object[] row = {
                emp.getEmployeeID(), 
                emp.getName(), 
                emp.getPhone(), 
                emp.getRole(), 
                emp.getCreatedAt()
            };
            model.addRow(row);
        }
    }
    private void initializeData() {
        // Lấy ID tiếp theo của Supplier
        int nextEmployeeId = employeeController.getNextEmployeeId();
        if (nextEmployeeId == -1) {
            JOptionPane.showMessageDialog(this, "Không thể lấy ID tiếp theo của Employee!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Hiển thị ID tiếp theo lên giao diện
        EmployeeIDForm.setText(String.valueOf(nextEmployeeId));

        // Đặt lại các trường nhập liệu về giá trị mặc định
        clearFields();
    }
    
    
 
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        InformationBox = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        Search = new javax.swing.JLabel();
        SearchEmployeeForm = new javax.swing.JTextField();
        SearchButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        EmployeeIDForm = new javax.swing.JTextField();
        AddButton = new javax.swing.JButton();
        EditButton = new javax.swing.JButton();
        DeleteButton = new javax.swing.JButton();
        NameEmployeeForm = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        PhoneEmployeeForm = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        RoleEmployeeCombobox = new javax.swing.JComboBox<>();
        TableBox = new javax.swing.JPanel();
        Title = new javax.swing.JLabel();
        BoxTable = new javax.swing.JScrollPane();
        EmployeeTable = new javax.swing.JTable();

        setPreferredSize(new java.awt.Dimension(1232, 899));

        InformationBox.setBackground(new java.awt.Color(255, 102, 0));

        jPanel1.setBackground(new java.awt.Color(255, 163, 102));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        Search.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        Search.setForeground(new java.awt.Color(255, 255, 255));
        Search.setText("Tìm nhân viên");

        SearchEmployeeForm.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        SearchEmployeeForm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SearchEmployeeFormActionPerformed(evt);
            }
        });

        SearchButton.setBackground(new java.awt.Color(255, 132, 51));
        SearchButton.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        SearchButton.setForeground(new java.awt.Color(255, 255, 255));
        SearchButton.setIcon(new javax.swing.ImageIcon("C:\\Users\\ADMIN\\Downloads\\search.png")); // NOI18N
        SearchButton.setText("Tìm kiếm");
        SearchButton.setMargin(new java.awt.Insets(8, 14, 8, 14));
        SearchButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                SearchButtonMouseClicked(evt);
            }
        });
        SearchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SearchButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(255, 255, 255)
                .addComponent(Search)
                .addGap(40, 40, 40)
                .addComponent(SearchEmployeeForm, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(SearchButton)
                .addContainerGap(254, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Search)
                    .addComponent(SearchEmployeeForm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(SearchButton))
                .addGap(31, 31, 31))
        );

        jPanel2.setBackground(new java.awt.Color(255, 163, 102));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel1.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Mã Nhân viên");

        EmployeeIDForm.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N

        AddButton.setBackground(new java.awt.Color(255, 132, 51));
        AddButton.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        AddButton.setForeground(new java.awt.Color(255, 255, 255));
        AddButton.setIcon(new javax.swing.ImageIcon("C:\\Users\\ADMIN\\Downloads\\add-user.png")); // NOI18N
        AddButton.setText("Thêm mới");
        AddButton.setMargin(new java.awt.Insets(8, 14, 8, 14));
        AddButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                AddButtonMouseClicked(evt);
            }
        });

        EditButton.setBackground(new java.awt.Color(255, 132, 51));
        EditButton.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        EditButton.setForeground(new java.awt.Color(255, 255, 255));
        EditButton.setIcon(new javax.swing.ImageIcon("C:\\Users\\ADMIN\\Downloads\\edit.png")); // NOI18N
        EditButton.setText("Sửa");
        EditButton.setMargin(new java.awt.Insets(8, 14, 8, 14));
        EditButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                EditButtonMouseClicked(evt);
            }
        });
        EditButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditButtonActionPerformed(evt);
            }
        });

        DeleteButton.setBackground(new java.awt.Color(255, 132, 51));
        DeleteButton.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        DeleteButton.setForeground(new java.awt.Color(255, 255, 255));
        DeleteButton.setIcon(new javax.swing.ImageIcon("C:\\Users\\ADMIN\\Downloads\\delete.png")); // NOI18N
        DeleteButton.setText("Xoá");
        DeleteButton.setMargin(new java.awt.Insets(8, 14, 8, 14));
        DeleteButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                DeleteButtonMouseClicked(evt);
            }
        });
        DeleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeleteButtonActionPerformed(evt);
            }
        });

        NameEmployeeForm.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N

        jLabel2.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Tên");

        jLabel3.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Số điện thoại");

        PhoneEmployeeForm.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N

        jLabel4.setBackground(new java.awt.Color(255, 255, 255));
        jLabel4.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Vai trò");

        RoleEmployeeCombobox.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        RoleEmployeeCombobox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Manager", "Cashier", "Stock_keeper", " " }));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(227, 227, 227)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24)
                        .addComponent(PhoneEmployeeForm, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24)
                        .addComponent(EmployeeIDForm, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(80, 80, 80)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(RoleEmployeeCombobox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(NameEmployeeForm, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(AddButton, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(EditButton, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(DeleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(330, 330, 330))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(PhoneEmployeeForm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(NameEmployeeForm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(EmployeeIDForm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(RoleEmployeeCombobox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(38, 38, 38)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(AddButton)
                    .addComponent(EditButton)
                    .addComponent(DeleteButton))
                .addContainerGap(57, Short.MAX_VALUE))
        );

        TableBox.setBackground(new java.awt.Color(255, 255, 255));
        TableBox.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        Title.setBackground(new java.awt.Color(255, 255, 255));
        Title.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        Title.setText("DANH SÁCH NHÂN VIÊN");

        EmployeeTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "Tên", "SĐT", "Vai trò", "Ngày tạo"
            }
        ));
        BoxTable.setViewportView(EmployeeTable);

        javax.swing.GroupLayout TableBoxLayout = new javax.swing.GroupLayout(TableBox);
        TableBox.setLayout(TableBoxLayout);
        TableBoxLayout.setHorizontalGroup(
            TableBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, TableBoxLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Title)
                .addGap(475, 475, 475))
            .addComponent(BoxTable, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        TableBoxLayout.setVerticalGroup(
            TableBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TableBoxLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(Title)
                .addGap(24, 24, 24)
                .addComponent(BoxTable, javax.swing.GroupLayout.DEFAULT_SIZE, 411, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout InformationBoxLayout = new javax.swing.GroupLayout(InformationBox);
        InformationBox.setLayout(InformationBoxLayout);
        InformationBoxLayout.setHorizontalGroup(
            InformationBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(InformationBoxLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(InformationBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addComponent(TableBox, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        InformationBoxLayout.setVerticalGroup(
            InformationBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(InformationBoxLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TableBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(InformationBox, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(InformationBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void SearchEmployeeFormActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SearchEmployeeFormActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_SearchEmployeeFormActionPerformed

    private void SearchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SearchButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_SearchButtonActionPerformed

    private void EditButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_EditButtonActionPerformed

    private void DeleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeleteButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_DeleteButtonActionPerformed

private void setupTableClickListener() {
        EmployeeTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = EmployeeTable.getSelectedRow(); // Lưu vị trí hàng được chọn
                if (selectedRow >= 0) {
                    // Lấy thông tin từ hàng được chọn
                    int EmployeeID = (int) EmployeeTable.getValueAt(selectedRow, 0);
                    String Name = (String) EmployeeTable.getValueAt(selectedRow, 1);
                    String Phone = (String) EmployeeTable.getValueAt(selectedRow, 2);
                    String Role = (String) EmployeeTable.getValueAt(selectedRow, 3);

                    // Điền thông tin vào các trường nhập liệu
                    EmployeeIDForm.setText(String.valueOf(EmployeeID));
                    NameEmployeeForm.setText(Name);
                    PhoneEmployeeForm.setText(String.valueOf(Phone));
                    RoleEmployeeCombobox.setSelectedItem(Role);
                }
            }
        });
    }

    private void EditButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_EditButtonMouseClicked
        int selectedRow = EmployeeTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một hàng để chỉnh sửa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int EmployeeID = (int) EmployeeTable.getValueAt(selectedRow, 0);
        String name = NameEmployeeForm.getText().trim();
        String phone = PhoneEmployeeForm.getText().trim();
        String role = RoleEmployeeCombobox.getSelectedItem().toString();

        if (name.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Employee updatedEmployee = new Employee();
        updatedEmployee.setEmployeeID(EmployeeID);
        updatedEmployee.setName(name);
        updatedEmployee.setPhone(phone);
        updatedEmployee.setRole(role);

        
        boolean isUpdated = employeeController.updateEmployee(updatedEmployee);
        if (isUpdated) {
            JOptionPane.showMessageDialog(this, "Cập nhật thông tin nhân viên thành công!");
            loadEmployeeTable(); // Tải lại dữ liệu
            clearFields(); // Xóa các trường nhập liệu
        } else {
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật thông tin nhân viên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_EditButtonMouseClicked

    private void DeleteButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_DeleteButtonMouseClicked
        int selectedRow = EmployeeTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một hàng để xóa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int EmployeeID = (int) EmployeeTable.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn xóa nhân viên này không?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            boolean isDeleted = employeeController.deleteEmployee(EmployeeID);
            if (isDeleted) {
                JOptionPane.showMessageDialog(this, "Xóa nhân viên thành công!");
                loadEmployeeTable(); // Tải lại dữ liệu
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Không thể xóa nhân viên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
    }//GEN-LAST:event_DeleteButtonMouseClicked
 }
    private void AddButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_AddButtonMouseClicked
        String name = NameEmployeeForm.getText().trim();
        String phone = PhoneEmployeeForm.getText().trim();
        String role = RoleEmployeeCombobox.getSelectedItem().toString();

        if (name.isEmpty() || phone.isEmpty() || role.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui long nhap day du thong tin!", "Loi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        EmployeeController controller = new EmployeeController();
        boolean success = controller.addEmployee(name, phone, role);

        if (success) {
            JOptionPane.showMessageDialog(this, "Them nhan vien thanh cong!", "Thong bao", JOptionPane.INFORMATION_MESSAGE);
            clearFields(); // Xoa du lieu sau khi them thanh cong
            loadEmployeeTable();
        } else {
            JOptionPane.showMessageDialog(this, "Them nhan vien that bai!", "Loi", JOptionPane.ERROR_MESSAGE);
        }
        }

        // Ham de xoa du lieu tren TextField sau khi them thanh cong
        private void clearFields() {
            NameEmployeeForm.setText("");
            PhoneEmployeeForm.setText("");
            RoleEmployeeCombobox.setSelectedItem("");
    }//GEN-LAST:event_AddButtonMouseClicked

    private void SearchButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SearchButtonMouseClicked
       // Lấy từ khóa tìm kiếm từ trường nhập liệu
    String keyword = SearchEmployeeForm.getText().trim();

    // Kiểm tra nếu từ khóa rỗng
    if (keyword.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Vui lòng nhập tên nhân viên cần tìm!", "Thông báo", JOptionPane.WARNING_MESSAGE);
        return ;
    }

    // Gọi Controller để tìm kiếm nhân viên
    EmployeeController controller = new EmployeeController();
    List<Employee> employees = controller.searchEmployeesByName(keyword);

    // Kiểm tra nếu không có kết quả
    if (employees.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Không tìm thấy nhân viên!", "Kết quả", JOptionPane.INFORMATION_MESSAGE);
        return;
    }

    // Cập nhật bảng hiển thị kết quả
    DefaultTableModel model = (DefaultTableModel) EmployeeTable.getModel();
    model.setRowCount(0); // Xóa dữ liệu cũ

    for (Employee emp : employees) {
        model.addRow(new Object[]{
            emp.getEmployeeID(),
            emp.getName(),
            emp.getPhone(),
            emp.getRole()
        });
    }
    }//GEN-LAST:event_SearchButtonMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AddButton;
    private javax.swing.JScrollPane BoxTable;
    private javax.swing.JButton DeleteButton;
    private javax.swing.JButton EditButton;
    private javax.swing.JTextField EmployeeIDForm;
    private javax.swing.JTable EmployeeTable;
    private javax.swing.JPanel InformationBox;
    private javax.swing.JTextField NameEmployeeForm;
    private javax.swing.JTextField PhoneEmployeeForm;
    private javax.swing.JComboBox<String> RoleEmployeeCombobox;
    private javax.swing.JLabel Search;
    private javax.swing.JButton SearchButton;
    private javax.swing.JTextField SearchEmployeeForm;
    private javax.swing.JPanel TableBox;
    private javax.swing.JLabel Title;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables
}
