/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.view;

/**
 *
 * @author Admin
 */
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignSection;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.HorizontalTextAlignEnum;
import net.sf.jasperreports.engine.type.VerticalTextAlignEnum;

public class InventoryReportDesignGenerator {

    public JasperDesign createInventoryReportDesign(String reportTitle, String timeRange) throws JRException {
        JasperDesign jasperDesign = new JasperDesign();
        jasperDesign.setName("InventoryReport");
        jasperDesign.setPageWidth(595); // A4 width in pixels
        jasperDesign.setPageHeight(842); // A4 height in pixels
        jasperDesign.setColumnWidth(515); // Column width
        jasperDesign.setLeftMargin(40);
        jasperDesign.setRightMargin(40);
        jasperDesign.setTopMargin(50);
        jasperDesign.setBottomMargin(50);

        // Thêm các trường (fields) vào JasperDesign
        addInventoryFields(jasperDesign);

        // Tạo title band
        JRDesignBand titleBand = createInventoryTitleBand(reportTitle);
        jasperDesign.setTitle(titleBand);

        // Tạo time range band
        JRDesignBand timeRangeBand = createTimeRangeBand(timeRange);
        jasperDesign.setTitle(timeRangeBand);

        // Tạo column header band
        JRDesignBand columnHeaderBand = createInventoryColumnHeaderBand();
        jasperDesign.setColumnHeader(columnHeaderBand);

        // Tạo detail band
        JRDesignBand detailBand = createInventoryDetailBand();
        ((JRDesignSection) jasperDesign.getDetailSection()).addBand(detailBand);

        return jasperDesign;
    }
    
    private JRDesignBand createTimeRangeBand(String timeRange) {
        JRDesignBand timeRangeBand = new JRDesignBand();
        timeRangeBand.setHeight(70);

        // Thêm thông tin khoảng thời gian báo cáo
        JRDesignStaticText timeRangeText = new JRDesignStaticText();
        timeRangeText.setText(timeRange); // Ví dụ: "Báo cáo ngày 2025-03-15" hoặc "Báo cáo từ ngày 2025-03-13 đến ngày 2025-03-15"
        timeRangeText.setX(10);
        timeRangeText.setY(5); // Điều chỉnh vị trí y để phù hợp với chiều cao của band
        timeRangeText.setWidth(500);
        timeRangeText.setFontName("Arial");
        timeRangeText.setBold(Boolean.TRUE);
        timeRangeText.setHeight(25); // Tăng chiều cao của tiêu đề
        timeRangeText.setFontSize(12f); // Cỡ chữ lớn
        timeRangeText.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
        timeRangeText.setVerticalTextAlign(VerticalTextAlignEnum.MIDDLE);
        timeRangeBand.addElement(timeRangeText);

        return timeRangeBand;
    }

    private void addInventoryFields(JasperDesign jasperDesign) throws JRException {
        // Thêm trường "product_name"
        JRDesignField productNameField = new JRDesignField();
        productNameField.setName("product_name");
        productNameField.setValueClass(String.class);
        jasperDesign.addField(productNameField);

        // Thêm trường "stock_quantity"
        JRDesignField stockQuantityField = new JRDesignField();
        stockQuantityField.setName("stock_quantity");
        stockQuantityField.setValueClass(Integer.class);
        jasperDesign.addField(stockQuantityField);

        // Thêm trường "last_updated"
        JRDesignField lastUpdatedField = new JRDesignField();
        lastUpdatedField.setName("last_updated");
        lastUpdatedField.setValueClass(String.class);
        jasperDesign.addField(lastUpdatedField);
    }

    private JRDesignBand createInventoryTitleBand(String reportTitle) {
        JRDesignBand titleBand = new JRDesignBand();
        titleBand.setHeight(50);

        // Thêm tiêu đề báo cáo
        JRDesignStaticText titleText = new JRDesignStaticText();
        titleText.setText(reportTitle);
        titleText.setX(10);
        titleText.setY(10);
        titleText.setWidth(500);
        titleText.setFontName("Arial");
        titleText.setBold(Boolean.TRUE);
        titleText.setFontSize(18f);
        titleText.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
        titleText.setVerticalTextAlign(VerticalTextAlignEnum.MIDDLE);
        titleBand.addElement(titleText);

        return titleBand;
    }

    private JRDesignBand createInventoryColumnHeaderBand() {
        JRDesignBand columnHeaderBand = new JRDesignBand();
        columnHeaderBand.setHeight(30);

        // Thêm cột "Sản phẩm"
        JRDesignStaticText productHeader = new JRDesignStaticText();
        productHeader.setText("Sản phẩm");
        productHeader.setX(10);
        productHeader.setY(5);
        productHeader.setWidth(200);
        productHeader.setHeight(20);
        productHeader.setFontName("Arial");
        productHeader.setBold(Boolean.TRUE);
        productHeader.setHorizontalTextAlign(HorizontalTextAlignEnum.LEFT);
        columnHeaderBand.addElement(productHeader);

        // Thêm cột "Số lượng tồn kho"
        JRDesignStaticText stockQuantityHeader = new JRDesignStaticText();
        stockQuantityHeader.setText("Số lượng tồn kho");
        stockQuantityHeader.setX(210);
        stockQuantityHeader.setY(5);
        stockQuantityHeader.setWidth(150);
        stockQuantityHeader.setHeight(20);
        stockQuantityHeader.setFontName("Arial");
        stockQuantityHeader.setBold(Boolean.TRUE);
        stockQuantityHeader.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
        columnHeaderBand.addElement(stockQuantityHeader);

        // Thêm cột "Ngày cập nhật"
        JRDesignStaticText lastUpdatedHeader = new JRDesignStaticText();
        lastUpdatedHeader.setText("Ngày cập nhật");
        lastUpdatedHeader.setX(360);
        lastUpdatedHeader.setY(5);
        lastUpdatedHeader.setWidth(150);
        lastUpdatedHeader.setHeight(20);
        lastUpdatedHeader.setFontName("Arial");
        lastUpdatedHeader.setBold(Boolean.TRUE);
        lastUpdatedHeader.setHorizontalTextAlign(HorizontalTextAlignEnum.RIGHT);
        columnHeaderBand.addElement(lastUpdatedHeader);

        return columnHeaderBand;
    }

    private JRDesignBand createInventoryDetailBand() {
        JRDesignBand detailBand = new JRDesignBand();
        detailBand.setHeight(20);

        // Thêm trường "Sản phẩm"
        JRDesignTextField productField = new JRDesignTextField();
        productField.setX(10);
        productField.setY(0);
        productField.setWidth(200);
        productField.setHeight(20);
        productField.setExpression(new JRDesignExpression("$F{product_name}"));
        productField.setFontName("Arial");
        productField.setHorizontalTextAlign(HorizontalTextAlignEnum.LEFT);
        detailBand.addElement(productField);

        // Thêm trường "Số lượng tồn kho"
        JRDesignTextField stockQuantityField = new JRDesignTextField();
        stockQuantityField.setX(210);
        stockQuantityField.setY(0);
        stockQuantityField.setWidth(150);
        stockQuantityField.setHeight(20);
        stockQuantityField.setExpression(new JRDesignExpression("$F{stock_quantity}"));
        stockQuantityField.setFontName("Arial");
        stockQuantityField.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
        detailBand.addElement(stockQuantityField);

        // Thêm trường "Ngày cập nhật"
        JRDesignTextField lastUpdatedField = new JRDesignTextField();
        lastUpdatedField.setX(360);
        lastUpdatedField.setY(0);
        lastUpdatedField.setWidth(150);
        lastUpdatedField.setHeight(20);
        lastUpdatedField.setExpression(new JRDesignExpression("$F{last_updated}"));
        lastUpdatedField.setFontName("Arial");
        lastUpdatedField.setHorizontalTextAlign(HorizontalTextAlignEnum.RIGHT);
        detailBand.addElement(lastUpdatedField);

        return detailBand;
    }
}
