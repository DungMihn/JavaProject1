package com.retail.service;

import com.retail.dao.InvoiceDAO;
import com.retail.dao.InvoiceDetailDAO;
import com.retail.dao.CustomerDAO;
import com.retail.dao.EmployeeDAO;
import com.retail.dao.ProductDAO;
import com.retail.model.Invoice;
import com.retail.model.InvoiceDetail;
import com.retail.model.Customer;
import com.retail.model.Employee;
import com.retail.model.Product;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ReportPanelService {
    private InvoiceDAO invoiceDAO = new InvoiceDAO();
    private InvoiceDetailDAO invoiceDetailDAO = new InvoiceDetailDAO();
    private CustomerDAO customerDAO = new CustomerDAO();
    private EmployeeDAO employeeDAO = new EmployeeDAO();
    private ProductDAO productDAO = new ProductDAO();
    
    public ReportData generateDailyReport() {
        List<Invoice> invoices = invoiceDAO.getInvoicesByDate();
        BigDecimal overallRevenue = BigDecimal.ZERO;
        List<Object[]> revenueRows = new ArrayList<>();
        for (Invoice inv : invoices) {
            overallRevenue = overallRevenue.add(inv.getFinalAmount());
            
            // Lấy tên khách hàng thay vì id
            String customerName = "";
            Customer cust = customerDAO.getCustomerById(inv.getCustomerId());
            if (cust != null) {
                customerName = cust.getName();
            }
            
            // Lấy tên nhân viên thay vì id
            String employeeName = "";
            Employee emp = employeeDAO.getEmployeeById(inv.getEmployeeId());
            if (emp != null) {
                employeeName = emp.getName();
            }
            
            Object[] row = {
                inv.getInvoiceId(),
                customerName,
                employeeName,
                inv.getTotalAmount(),
                inv.getDiscount(),
                inv.getFinalAmount(),
                inv.getCreatedAt()
            };
            revenueRows.add(row);
        }
        List<Object[]> bestSellingRows = generateBestSellingProductsReportByDate();
        return new ReportData(overallRevenue, revenueRows, bestSellingRows);
    }
    
    public ReportData generateMonthlyReport() {
        List<Invoice> invoices = invoiceDAO.getInvoicesByMonth();
        BigDecimal overallRevenue = BigDecimal.ZERO;
        List<Object[]> revenueRows = new ArrayList<>();
        for (Invoice inv : invoices) {
            overallRevenue = overallRevenue.add(inv.getFinalAmount());
            
            String customerName = "";
            Customer cust = customerDAO.getCustomerById(inv.getCustomerId());
            if (cust != null) {
                customerName = cust.getName();
            }
            
            String employeeName = "";
            Employee emp = employeeDAO.getEmployeeById(inv.getEmployeeId());
            if (emp != null) {
                employeeName = emp.getName();
            }
            
            Object[] row = {
                inv.getInvoiceId(),
                customerName,
                employeeName,
                inv.getTotalAmount(),
                inv.getDiscount(),
                inv.getFinalAmount(),
                inv.getCreatedAt()
            };
            revenueRows.add(row);
        }
        List<Object[]> bestSellingRows = generateBestSellingProductsReportByMonth();
        return new ReportData(overallRevenue, revenueRows, bestSellingRows);
    }
    
    private List<Object[]> generateBestSellingProductsReportByDate() {
        List<InvoiceDetail> details = invoiceDetailDAO.getBestSellingProductsByDate();
        List<Object[]> bestSellingRows = new ArrayList<>();
        for (InvoiceDetail detail : details) {
            Product product = productDAO.getProductById(detail.getProductId());
            BigDecimal revenue = detail.getPrice().multiply(new BigDecimal(detail.getQuantity()));
            Object[] row = { product.getName(), detail.getQuantity(), revenue };
            bestSellingRows.add(row);
        }
        return bestSellingRows;
    }
    
    private List<Object[]> generateBestSellingProductsReportByMonth() {
        List<InvoiceDetail> details = invoiceDetailDAO.getBestSellingProductsByMonth();
        List<Object[]> bestSellingRows = new ArrayList<>();
        for (InvoiceDetail detail : details) {
            Product product = productDAO.getProductById(detail.getProductId());
            BigDecimal revenue = detail.getPrice().multiply(new BigDecimal(detail.getQuantity()));
            Object[] row = { product.getName(), detail.getQuantity(), revenue };
            bestSellingRows.add(row);
        }
        return bestSellingRows;
    }
    
    // Lớp chứa dữ liệu báo cáo
    public static class ReportData {
        private BigDecimal overallRevenue;
        private List<Object[]> revenueRows;
        private List<Object[]> bestSellingRows;
        
        public ReportData(BigDecimal overallRevenue, List<Object[]> revenueRows, List<Object[]> bestSellingRows) {
            this.overallRevenue = overallRevenue;
            this.revenueRows = revenueRows;
            this.bestSellingRows = bestSellingRows;
        }
        
        public BigDecimal getOverallRevenue() {
            return overallRevenue;
        }
        
        public List<Object[]> getRevenueRows() {
            return revenueRows;
        }
        
        public List<Object[]> getBestSellingRows() {
            return bestSellingRows;
        }
    }
}
