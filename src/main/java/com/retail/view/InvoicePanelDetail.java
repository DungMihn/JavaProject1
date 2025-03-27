package com.retail.view;

import com.retail.dao.InvoiceDetailDAO;
import com.retail.model.InvoiceDetail;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class InvoicePanelDetail extends JDialog {

    public InvoicePanelDetail(Frame parent, int invoiceId) {
        super(parent, "Chi tiết hóa đơn", true);
        setSize(500, 400);
        setLocationRelativeTo(parent);
        initUI(invoiceId);
    }
    
    private void initUI(int invoiceId) {
        // Lấy danh sách chi tiết hóa đơn từ DAO
        InvoiceDetailDAO detailDAO = new InvoiceDetailDAO();
        List<InvoiceDetail> details = detailDAO.getInvoiceDetailsByInvoiceId(invoiceId);
        
        // Xây dựng nội dung hiển thị
        StringBuilder sb = new StringBuilder();
        sb.append("Chi tiết hóa đơn (Invoice ID: ").append(invoiceId).append("):\n\n");
        for (InvoiceDetail d : details) {
            sb.append("InvoiceDetail ID: ").append(d.getInvoiceDetailId())
              .append(", Product ID: ").append(d.getProductId())
              .append(", Quantity: ").append(d.getQuantity())
              .append(", Price: ").append(d.getPrice())
              .append(", Subtotal: ").append(d.getSubtotal())
              .append("\n");
        }
        
        // Sử dụng JTextArea để hiển thị thông tin
        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(textArea);
        
        add(scrollPane, BorderLayout.CENTER);
        
        // Nút Đóng
        JPanel buttonPanel = new JPanel();
        JButton btnClose = new JButton("Đóng");
        btnClose.addActionListener(e -> dispose());
        buttonPanel.add(btnClose);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    // Phương thức tĩnh để hiển thị dialog
    public static void showInvoiceDetail(Frame parent, int invoiceId) {
        InvoicePanelDetail dialog = new InvoicePanelDetail(parent, invoiceId);
        dialog.setVisible(true);
    }
}
