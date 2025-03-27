/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.view;

/**
 *
 * @author Admin
 */
import com.retail.model.InvoiceDetail;
import java.awt.Color;
import java.math.BigDecimal;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.*;
import net.sf.jasperreports.engine.type.HorizontalTextAlignEnum;
import net.sf.jasperreports.view.JasperViewer;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.type.LineStyleEnum;

public class InvoiceReportGenerator {

    public void generateReport(String invoiceDate, String customerName, String employeeName,
            String totalAmount, String discount, String finalAmount, String paymentMethod,
            List<InvoiceDetail> details, int invoiceId) throws JRException {

        // Tạo parameters map để truyền các thông tin bổ sung
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("customerName", customerName);
        parameters.put("employeeName", employeeName);
        parameters.put("totalAmount", totalAmount);
        parameters.put("discount", discount);
        parameters.put("finalAmount", finalAmount);
        parameters.put("paymentMethod", paymentMethod);
        parameters.put("invoiceDate", formatInvoiceDate(invoiceDate));
        parameters.put("invoiceId", invoiceId);

        // Tạo data source từ List
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(details);

        // Tạo JasperDesign với các thông tin bổ sung
        JasperDesign jasperDesign = createJasperDesign(parameters);

        // Tạo và xuất báo cáo
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        String filePath = "InvoiceReport_" + invoiceId + ".pdf";
        JasperExportManager.exportReportToPdfFile(jasperPrint, filePath);
        JasperViewer.viewReport(jasperPrint, false);
    }

    private JasperDesign createJasperDesign(Map<String, Object> parameters) throws JRException {
        JasperDesign jasperDesign = new JasperDesign();
        jasperDesign.setName("InvoiceReport");
        jasperDesign.setPageWidth(595);
        jasperDesign.setPageHeight(842);
        jasperDesign.setColumnWidth(515);
        jasperDesign.setLeftMargin(40);
        jasperDesign.setRightMargin(40);
        jasperDesign.setTopMargin(50);
        jasperDesign.setBottomMargin(50);

        // Thêm các trường
        addFields(jasperDesign);

        // Tạo các band với đầy đủ thông tin
        jasperDesign.setTitle(createTitleBand(parameters));
        jasperDesign.setColumnHeader(createColumnHeaderBand());
        ((JRDesignSection) jasperDesign.getDetailSection()).addBand(createDetailBand());
        jasperDesign.setSummary(createSummaryBand(parameters));

        return jasperDesign;
    }

    private void addFields(JasperDesign jasperDesign) throws JRException {
        // Thêm trường "product_name"
        JRDesignField productNameField = new JRDesignField();
        productNameField.setName("productName");
        productNameField.setValueClass(String.class);
        jasperDesign.addField(productNameField);

        // Thêm trường "quantity"
        JRDesignField quantityField = new JRDesignField();
        quantityField.setName("quantity");
        quantityField.setValueClass(Integer.class);
        jasperDesign.addField(quantityField);

        JRDesignField priceField = new JRDesignField();
        priceField.setName("price");  // Khớp với tên getter getPrice()
        priceField.setValueClass(BigDecimal.class);  // Kiểu dữ liệu BigDecimal
        jasperDesign.addField(priceField);

        // Thêm trường "subtotal"
        JRDesignField subtotalField = new JRDesignField();
        subtotalField.setName("subtotal");
        subtotalField.setValueClass(BigDecimal.class);
        jasperDesign.addField(subtotalField);
    }

    private JRDesignBand createTitleBand(Map<String, Object> parameters) {
        JRDesignBand titleBand = new JRDesignBand();
        titleBand.setHeight(150); // Tăng chiều cao để chứa thêm thông tin

        // Tiêu đề hóa đơn
        JRDesignStaticText titleText = new JRDesignStaticText();
        titleText.setText("HÓA ĐƠN BÁN HÀNG - ID: " + parameters.get("invoiceId"));
        titleText.setX(10);
        titleText.setY(10);
        titleText.setWidth(500);
        titleText.setHeight(30);
        titleText.setFontName("Arial");
        titleText.setBold(Boolean.TRUE);
        titleText.setFontSize(18f);
        titleText.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
        titleBand.addElement(titleText);

        // Thông tin khách hàng và nhân viên
        addLabelValuePair(titleBand, "Khách hàng:", parameters.get("customerName").toString(), 10, 45, 300);
        addLabelValuePair(titleBand, "Nhân viên:", parameters.get("employeeName").toString(), 310, 45, 200);

        // Ngày hóa đơn và phương thức thanh toán
        addLabelValuePair(titleBand, "Ngày:", parameters.get("invoiceDate").toString(), 10, 70, 300);
        addLabelValuePair(titleBand, "PT thanh toán:", parameters.get("paymentMethod").toString(), 310, 70, 200);

        // Tổng tiền và chiết khấu
        addLabelValuePair(titleBand, "Tổng tiền:", formatCurrency(parameters.get("totalAmount").toString()), 10, 95, 300);
        addLabelValuePair(titleBand, "Chiết khấu:", formatCurrency(parameters.get("discount").toString()), 310, 95, 200);

        // Thành tiền
        addLabelValuePair(titleBand, "Thành tiền:", formatCurrency(parameters.get("finalAmount").toString()), 10, 120, 300);

        return titleBand;
    }

    private void addLabelValuePair(JRDesignBand band, String label, String value, int x, int y, int width) {
        JRDesignStaticText labelText = new JRDesignStaticText();
        labelText.setText(label);
        labelText.setX(x);
        labelText.setY(y);
        labelText.setWidth(width / 2);
        labelText.setHeight(20);
        labelText.setFontName("Arial");
        band.addElement(labelText);

        JRDesignStaticText valueText = new JRDesignStaticText();
        valueText.setText(value);
        valueText.setX(x + width / 2);
        valueText.setY(y);
        valueText.setWidth(width / 2);
        valueText.setHeight(20);
        valueText.setFontName("Arial");
        valueText.setBold(Boolean.TRUE);
        band.addElement(valueText);
    }

    private JRDesignBand createColumnHeaderBand() {
        JRDesignBand columnHeaderBand = new JRDesignBand();
        columnHeaderBand.setHeight(40); // Tăng chiều cao

        // Thêm đường kẻ dưới tiêu đề cột
        JRDesignLine headerLine = new JRDesignLine();
        headerLine.setX(10);
        headerLine.setY(35);
        headerLine.setWidth(500);
        headerLine.setHeight(1);
        headerLine.setForecolor(Color.DARK_GRAY);
        columnHeaderBand.addElement(headerLine);

        // Các tiêu đề cột (điều chỉnh vị trí y)
        // Tên sản phẩm
        JRDesignStaticText productHeader = new JRDesignStaticText();
        productHeader.setText("Sản phẩm");
        productHeader.setX(10);
        productHeader.setY(10); // Điều chỉnh vị trí
        // ... (các thuộc tính khác giữ nguyên)
        columnHeaderBand.addElement(productHeader);

        // Các tiêu đề cột khác tương tự...
        return columnHeaderBand;
    }

    private JRDesignBand createDetailBand() {
        JRDesignBand detailBand = new JRDesignBand();
        detailBand.setHeight(30); // Tăng chiều cao để chứa đường kẻ

        // Thêm đường kẻ ngang trên cùng
        JRDesignLine topLine = new JRDesignLine();
        topLine.setX(10);
        topLine.setY(0);
        topLine.setWidth(500);
        topLine.setHeight(1);
        topLine.setForecolor(Color.LIGHT_GRAY);
        detailBand.addElement(topLine);

        // Các trường dữ liệu (điều chỉnh vị trí y)
        // Tên sản phẩm
        JRDesignTextField productField = new JRDesignTextField();
        productField.setX(10);
        productField.setY(5); // Tăng y lên để tránh đường kẻ
        productField.setWidth(200);
        productField.setHeight(20);
        productField.setExpression(new JRDesignExpression("$F{productName}"));
        productField.setFontName("Arial");
        detailBand.addElement(productField);

        // Số lượng
        JRDesignTextField quantityField = new JRDesignTextField();
        quantityField.setX(210);
        quantityField.setY(5);
        quantityField.setWidth(50);
        quantityField.setHeight(20);
        quantityField.setExpression(new JRDesignExpression("$F{quantity}"));
        quantityField.setFontName("Arial");
        quantityField.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
        detailBand.addElement(quantityField);

        // Đơn giá
        JRDesignTextField priceField = new JRDesignTextField();
        priceField.setX(260);
        priceField.setY(5);
        priceField.setWidth(100);
        priceField.setHeight(20);
        priceField.setExpression(new JRDesignExpression("$F{price}"));
        priceField.setPattern("#,##0.00 'VND'");
        priceField.setFontName("Arial");
        priceField.setHorizontalTextAlign(HorizontalTextAlignEnum.RIGHT);
        detailBand.addElement(priceField);

        // Thành tiền
        JRDesignTextField subtotalField = new JRDesignTextField();
        subtotalField.setX(360);
        subtotalField.setY(5);
        subtotalField.setWidth(150);
        subtotalField.setHeight(20);
        subtotalField.setExpression(new JRDesignExpression("$F{subtotal}"));
        subtotalField.setPattern("#,##0.00 'VND'");
        subtotalField.setFontName("Arial");
        subtotalField.setHorizontalTextAlign(HorizontalTextAlignEnum.RIGHT);
        detailBand.addElement(subtotalField);

        // Thêm đường kẻ ngang dưới cùng
        JRDesignLine bottomLine = new JRDesignLine();
        bottomLine.setX(10);
        bottomLine.setY(28); // Vị trí ngay trên bottom của band
        bottomLine.setWidth(500);
        bottomLine.setHeight(1);
        bottomLine.setForecolor(Color.LIGHT_GRAY);
        detailBand.addElement(bottomLine);

        return detailBand;
    }

    private JRDesignBand createSummaryBand(Map<String, Object> parameters) {
        JRDesignBand summaryBand = new JRDesignBand();
        summaryBand.setHeight(100); // Tăng chiều cao

        // Thêm đường kẻ phân cách trước phần tổng kết
        JRDesignLine separatorLine = new JRDesignLine();
        separatorLine.setX(10);
        separatorLine.setY(10);
        separatorLine.setWidth(500);
        separatorLine.setHeight(2);
        separatorLine.setForecolor(Color.DARK_GRAY);
        summaryBand.addElement(separatorLine);

        // Các phần tử tổng kết (điều chỉnh vị trí y)
        addSummaryRow(summaryBand, "Tổng tiền:", parameters.get("totalAmount").toString(), 20);
        addSummaryRow(summaryBand, "Chiết khấu:", parameters.get("discount").toString(), 45);
        addSummaryRow(summaryBand, "Thành tiền:", parameters.get("finalAmount").toString(), 70, true);

        // Thêm đường kẻ dưới cùng
        JRDesignLine bottomLine = new JRDesignLine();
        bottomLine.setX(10);
        bottomLine.setY(95);
        bottomLine.setWidth(500);
        bottomLine.setHeight(2);
        bottomLine.setForecolor(Color.DARK_GRAY);
        summaryBand.addElement(bottomLine);

        return summaryBand;
    }

    private void addSummaryRow(JRDesignBand band, String label, String value, int y) {
        addSummaryRow(band, label, value, y, false);
    }

    private void addSummaryRow(JRDesignBand band, String label, String value, int y, boolean isBold) {
        // Label
        JRDesignStaticText labelText = new JRDesignStaticText();
        labelText.setText(label);
        labelText.setX(260);
        labelText.setY(y);
        labelText.setWidth(100);
        labelText.setHeight(20);
        labelText.setFontName("Arial");
        labelText.setBold(Boolean.TRUE);
        labelText.setHorizontalTextAlign(HorizontalTextAlignEnum.RIGHT);
        band.addElement(labelText);

        // Value
        JRDesignTextField valueField = new JRDesignTextField();
        valueField.setX(360);
        valueField.setY(y);
        valueField.setWidth(150);
        valueField.setHeight(20);
        valueField.setExpression(new JRDesignExpression("\"" + formatCurrency(value) + "\""));
        valueField.setFontName("Arial");
        valueField.setBold(Boolean.TRUE);
        valueField.setHorizontalTextAlign(HorizontalTextAlignEnum.RIGHT);
        band.addElement(valueField);
    }

    private String formatInvoiceDate(String invoiceDate) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            java.util.Date date = inputFormat.parse(invoiceDate);
            return outputFormat.format(date);
        } catch (ParseException e) {
            return invoiceDate;
        }
    }

    private String formatCurrency(String amount) {
        try {
            double value = Double.parseDouble(amount);
            return String.format("%,.0f VND", value);
        } catch (NumberFormatException e) {
            return amount + " VND";
        }
    }
}
