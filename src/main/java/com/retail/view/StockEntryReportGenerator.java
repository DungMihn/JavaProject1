/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.view;

/**
 *
 * @author Admin
 */
import java.awt.Color;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.*;
import net.sf.jasperreports.engine.type.HorizontalTextAlignEnum;
import net.sf.jasperreports.engine.type.VerticalTextAlignEnum;
import net.sf.jasperreports.view.JasperViewer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class StockEntryReportGenerator {

    public void generateReport(String entryDate, String supplierName, String employeeName, String totalPrice, ResultSet rsDetails, int stockEntryId) throws JRException, SQLException {
        // Định dạng lại ngày nhập
        String formattedEntryDate = formatEntryDate(entryDate);

        // Tạo JasperDesign
        JasperDesign jasperDesign = createJasperDesign(formattedEntryDate, supplierName, employeeName, totalPrice, stockEntryId);

        // Tạo JasperPrint từ JasperReport và dữ liệu
        JRResultSetDataSource dataSource = new JRResultSetDataSource(rsDetails);
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(), dataSource);

        // Xuất file PDF
        String filePath = "StockEntryReport_" + stockEntryId + ".pdf";
        JasperExportManager.exportReportToPdfFile(jasperPrint, filePath);

        // Hiển thị báo cáo
        JasperViewer.viewReport(jasperPrint, false);
    }

    private JasperDesign createJasperDesign(String entryDate, String supplierName, String employeeName, String totalPrice, int stockEntryId) throws JRException {
        // Tạo JasperDesign
        JasperDesign jasperDesign = new JasperDesign();
        jasperDesign.setName("StockEntryReport");
        jasperDesign.setPageWidth(595); // A4 width in pixels
        jasperDesign.setPageHeight(842); // A4 height in pixels
        jasperDesign.setColumnWidth(515); // Column width
        jasperDesign.setLeftMargin(40);
        jasperDesign.setRightMargin(40);
        jasperDesign.setTopMargin(50);
        jasperDesign.setBottomMargin(50);
        jasperDesign.setSummaryNewPage(false);

        // Thêm các trường (fields) vào JasperDesign
        addFields(jasperDesign);

        // Tạo title band
        JRDesignBand titleBand = createTitleBand(stockEntryId, entryDate, supplierName, employeeName);
        jasperDesign.setTitle(titleBand);

        // Tạo column header band
        JRDesignBand columnHeaderBand = createColumnHeaderBand();
        jasperDesign.setColumnHeader(columnHeaderBand);

        // Tạo detail band
        JRDesignBand detailBand = createDetailBand();
        ((JRDesignSection) jasperDesign.getDetailSection()).addBand(detailBand);

        // Tạo summary band (tổng giá tiền)
        JRDesignBand summaryBand = createSummaryBand(totalPrice);
        jasperDesign.setSummary(summaryBand);

        return jasperDesign;
    }

    private void addFields(JasperDesign jasperDesign) throws JRException {
        // Thêm trường "product_name"
        JRDesignField productNameField = new JRDesignField();
        productNameField.setName("product_name");
        productNameField.setValueClass(String.class);
        jasperDesign.addField(productNameField);

        // Thêm trường "quantity"
        JRDesignField quantityField = new JRDesignField();
        quantityField.setName("quantity");
        quantityField.setValueClass(Integer.class);
        jasperDesign.addField(quantityField);

        // Thêm trường "purchase_price" (kiểu Integer)
        JRDesignField purchasePriceField = new JRDesignField();
        purchasePriceField.setName("purchase_price");
        purchasePriceField.setValueClass(Integer.class);
        jasperDesign.addField(purchasePriceField);

    }

    private JRDesignBand createTitleBand(int stockEntryId, String entryDate, String supplierName, String employeeName) {
        JRDesignBand titleBand = new JRDesignBand();
        titleBand.setHeight(70); // Tăng chiều cao của titleBand lên 70

        // Thêm tiêu đề "BẢNG GHI NHẬP HÀNG" (in đậm và to)
        JRDesignStaticText titleText = new JRDesignStaticText();
        titleText.setText("PHIẾU NHẬP KHO - ID: " + stockEntryId);
        titleText.setX(10);
        titleText.setY(5); // Điều chỉnh vị trí y để phù hợp với chiều cao của band
        titleText.setWidth(500);
        titleText.setFontName("Arial");
        titleText.setBold(Boolean.TRUE);
        titleText.setHeight(25); // Tăng chiều cao của tiêu đề
        titleText.setFontSize(18f); // Cỡ chữ lớn
        titleText.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
        titleText.setVerticalTextAlign(VerticalTextAlignEnum.MIDDLE);
        titleBand.addElement(titleText);

        // Thêm phụ đề vào title band
        JRDesignStaticText subtitleText = new JRDesignStaticText();
        subtitleText.setText("Ngày nhập: " + entryDate + " | Nhà cung cấp: " + supplierName + " | Nhân viên: " + employeeName);
        subtitleText.setX(10);
        subtitleText.setY(35); // Điều chỉnh vị trí y để phù hợp với chiều cao của band
        subtitleText.setWidth(500);
        subtitleText.setHeight(20); // Tăng chiều cao của phụ đề
        subtitleText.setFontName("Arial");
        subtitleText.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
        subtitleText.setVerticalTextAlign(VerticalTextAlignEnum.MIDDLE);
        titleBand.addElement(subtitleText);

        return titleBand;
    }

    private JRDesignBand createColumnHeaderBand() {
        JRDesignBand columnHeaderBand = new JRDesignBand();
        columnHeaderBand.setHeight(30); // Tăng chiều cao của columnHeaderBand lên 30

        // Thêm cột "Sản phẩm"
        JRDesignStaticText productHeader = new JRDesignStaticText();
        productHeader.setText("Sản phẩm");
        productHeader.setX(10);
        productHeader.setY(5); // Điều chỉnh vị trí y để tăng khoảng cách
        productHeader.setWidth(200);
        productHeader.setHeight(20);
        productHeader.setFontName("Arial");
        productHeader.setBold(Boolean.TRUE); // Làm đậm tiêu đề
        productHeader.setHorizontalTextAlign(HorizontalTextAlignEnum.LEFT);
        productHeader.setVerticalTextAlign(VerticalTextAlignEnum.MIDDLE);
        columnHeaderBand.addElement(productHeader);

        // Thêm cột "Số lượng"
        JRDesignStaticText quantityHeader = new JRDesignStaticText();
        quantityHeader.setText("Số lượng");
        quantityHeader.setX(210);
        quantityHeader.setY(5); // Điều chỉnh vị trí y để tăng khoảng cách
        quantityHeader.setWidth(100);
        quantityHeader.setHeight(20);
        quantityHeader.setFontName("Arial");
        quantityHeader.setBold(Boolean.TRUE); // Làm đậm tiêu đề
        quantityHeader.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
        quantityHeader.setVerticalTextAlign(VerticalTextAlignEnum.MIDDLE);
        columnHeaderBand.addElement(quantityHeader);

        // Thêm cột "Giá nhập"
        JRDesignStaticText priceHeader = new JRDesignStaticText();
        priceHeader.setText("Giá nhập");
        priceHeader.setX(310);
        priceHeader.setY(5); // Điều chỉnh vị trí y để tăng khoảng cách
        priceHeader.setWidth(200);
        priceHeader.setHeight(20);
        priceHeader.setFontName("Arial");
        priceHeader.setBold(Boolean.TRUE);
        priceHeader.setHorizontalTextAlign(HorizontalTextAlignEnum.RIGHT);
        priceHeader.setVerticalTextAlign(VerticalTextAlignEnum.MIDDLE);
        columnHeaderBand.addElement(priceHeader);

        return columnHeaderBand;
    }

    private JRDesignBand createDetailBand() {
        JRDesignBand detailBand = new JRDesignBand();
        detailBand.setHeight(30); // Tăng chiều cao của detailBand lên 30

        // Thêm trường "Sản phẩm"
        JRDesignTextField productField = new JRDesignTextField();
        productField.setX(10);
        productField.setY(5); // Điều chỉnh vị trí y để tăng khoảng cách
        productField.setWidth(200);
        productField.setHeight(20);
        productField.setExpression(new JRDesignExpression("$F{product_name}"));
        productField.setFontName("Arial");
        productField.setHorizontalTextAlign(HorizontalTextAlignEnum.LEFT);
        detailBand.addElement(productField);

        // Thêm trường "Số lượng"
        JRDesignTextField quantityField = new JRDesignTextField();
        quantityField.setX(210);
        quantityField.setY(5); // Điều chỉnh vị trí y để tăng khoảng cách
        quantityField.setWidth(100);
        quantityField.setHeight(20);
        quantityField.setExpression(new JRDesignExpression("$F{quantity}"));
        quantityField.setFontName("Arial");
        quantityField.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
        detailBand.addElement(quantityField);

        // Thêm trường "Giá nhập"
        JRDesignTextField priceField = new JRDesignTextField();
        priceField.setX(310);
        priceField.setY(5); // Điều chỉnh vị trí y để tăng khoảng cách
        priceField.setWidth(200);
        priceField.setHeight(20);
        priceField.setExpression(new JRDesignExpression("$F{purchase_price}"));
        priceField.setPattern("#,##0 'VND'"); // Định dạng số nguyên
        priceField.setFontName("Arial");
        priceField.setHorizontalTextAlign(HorizontalTextAlignEnum.RIGHT);
        detailBand.addElement(priceField);

        // Thêm đường kẻ ngăn cách giữa các dòng
        JRDesignLine line = new JRDesignLine();
        line.setX(10);
        line.setY(29); // Điều chỉnh vị trí y của đường kẻ
        line.setWidth(500);
        line.setHeight(1);
        detailBand.addElement(line);

        return detailBand;
    }

    private JRDesignBand createSummaryBand(String totalPrice) {

        JRDesignBand summaryBand = new JRDesignBand();
        summaryBand.setHeight(50); // Tăng chiều cao của summaryBand lên 40

        // Thêm dòng tổng giá tiền
        JRDesignStaticText totalLabel = new JRDesignStaticText();
        totalLabel.setText("Tổng giá tiền:");
        totalLabel.setX(10);
        totalLabel.setY(10); // Điều chỉnh vị trí y để phù hợp với chiều cao của band
        totalLabel.setWidth(200);
        totalLabel.setHeight(20);
        totalLabel.setFontName("Arial");
        totalLabel.setBold(Boolean.TRUE); // Làm đậm tiêu đề
        totalLabel.setHorizontalTextAlign(HorizontalTextAlignEnum.LEFT);
        summaryBand.addElement(totalLabel);

        // Thêm trường tổng giá tiền
        JRDesignTextField totalField = new JRDesignTextField();
        totalField.setX(310);
        totalField.setY(10); // Điều chỉnh vị trí y để phù hợp với chiều cao của band
        totalField.setWidth(200);
        totalField.setHeight(20);
        int total = (int) Double.parseDouble(totalPrice);
        totalField.setExpression(new JRDesignExpression("\"" + String.format("%,d", total) + " VND\""));

        totalField.setFontName("Arial");
        totalField.setBold(Boolean.TRUE); // Làm đậm giá trị
        totalField.setHorizontalTextAlign(HorizontalTextAlignEnum.RIGHT);
        summaryBand.addElement(totalField);

        return summaryBand;
    }

    private String formatEntryDate(String entryDate) {
        try {
            // Định dạng ngày tháng năm và giờ phút
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Date date = inputFormat.parse(entryDate);
            return outputFormat.format(date);
        } catch (ParseException e) {
            return entryDate; // Trả về nguyên bản nếu có lỗi
        }
    }
}
