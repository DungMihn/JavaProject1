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
    public void addSupplier(String name, String contactName, String phone, String email, String address) {
        Supplier supplier = new Supplier();
        supplier.setName(name);
        supplier.setContactName(contactName);
        supplier.setPhone(phone);
        supplier.setEmail(email);
        supplier.setAddress(address);
        supplierService.addSupplier(supplier);
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
    public void updateSupplier(int supplierId, String name, String contactName, String phone, String email, String address) {
        Supplier supplier = new Supplier();
        supplier.setSupplierId(supplierId);
        supplier.setName(name);
        supplier.setContactName(contactName);
        supplier.setPhone(phone);
        supplier.setEmail(email);
        supplier.setAddress(address);
        supplierService.updateSupplier(supplier);
    }

    // Xóa nhà cung cấp
    public void deleteSupplier(int supplierId) {
        supplierService.deleteSupplier(supplierId);
    }
}
