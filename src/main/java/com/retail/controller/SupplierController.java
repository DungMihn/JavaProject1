/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.controller;

import com.retail.model.Supplier;
import com.retail.service.SupplierService;
import java.util.List;

/**
 *
 * @author Admin
 */
public class SupplierController {

    private final SupplierService supplierService = new SupplierService();

    // Thêm nhà cung cấp mới
    public boolean addSupplier(Supplier supplier) {
        return supplierService.addSupplier(supplier);
    }

    // Lấy danh sách nhà cung cấp
    public List<Supplier> getAllSuppliers() {
        return supplierService.getAllSuppliers();
    }

    // Lấy thông tin nhà cung cấp theo ID
    public Supplier getSupplierById(int supplierId) {
        return supplierService.getSupplierById(supplierId);
    }

    // Cập nhật thông tin nhà cung cấp
    public boolean updateSupplier(Supplier supplier) {

        return supplierService.updateSupplier(supplier);
    }

    // Xóa nhà cung cấp
    public boolean deleteSupplier(int supplierId) {
        return supplierService.deleteSupplier(supplierId);
    }

    public List<Supplier> searchSuppliersByName(String keyword) {
        return supplierService.searchSuppliersByName(keyword);
    }

    public int getNextSupplierId() {
        return supplierService.getNextSupplierId();
    }
}
