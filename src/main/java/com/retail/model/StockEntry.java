/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Admin
 */

 public class StockEntry {
    private int stockEntryId;
    private int supplierId;
    private String supplierName;
    private int employeeId;
    private String employeeName;
    private LocalDateTime entryDate;

    // Constructors
    public StockEntry() {
    }

    public StockEntry(int stockEntryId, int supplierId, int employeeId, LocalDateTime entryDate) {
        this.stockEntryId = stockEntryId;
        this.supplierId = supplierId;
        this.employeeId = employeeId;
        this.entryDate = entryDate;
    }

    // Getters and Setters
    public int getStockEntryId() {
        return stockEntryId;
    }

    public void setStockEntryId(int stockEntryId) {
        this.stockEntryId = stockEntryId;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getEntryDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return entryDate.format(formatter);
    }

    public void setEntryDate(LocalDateTime entryDate) {
        this.entryDate = entryDate;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }
    
    
}
