package com.retail.controller;

import com.retail.service.ReportPanelService;
import com.retail.service.ReportPanelService.ReportData;
import com.retail.view.ReportPanel;
import java.util.Arrays;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class ReportPanelController {
    private ReportPanel view;
    private ReportPanelService service;
    
    public ReportPanelController(ReportPanel view) {
        this.view = view;
        this.service = new ReportPanelService();
        initController();
    }
    
    private void initController() {
        view.getBtnGenerateReport().addActionListener(e -> generateReport());
    }
    
    private void generateReport() {
        String reportType = (String) view.getCmbReportType().getSelectedItem();
        ReportData data;
        if ("Doanh thu theo ngày".equals(reportType)) {
            data = service.generateDailyReport();
        } else { // "Doanh thu theo tháng"
            data = service.generateMonthlyReport();
        }
        updateRevenueTable(data.getRevenueRows());
        updateBestSellingTable(data.getBestSellingRows());
        view.getLblOverallRevenue().setText("" + data.getOverallRevenue());
    }
    
    private void updateRevenueTable(List<Object[]> rows) {
        DefaultTableModel model = view.getRevenueTableModel();
        model.setRowCount(0);
        for (Object[] row : rows) {
            model.addRow(row);
        }
    }
    
    private void updateBestSellingTable(List<Object[]> rows) {
        
        DefaultTableModel model = view.getBestSellingTableModel();
        model.setRowCount(0);
        for (Object[] row : rows) {
            model.addRow(row);
        }
    }
}
