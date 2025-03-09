/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.service;

import com.retail.dao.SupplierDAOImpl;
import com.retail.model.Supplier;
import java.util.List;
/**
 *
 * @author Admin
 */
public class SupplierService {
     private final SupplierDAOImpl supplierDAO = new SupplierDAOImpl();

    // Thêm nhà cung cấp mới
    public void addSupplier(Supplier supplier) {
        supplierDAO.addSupplier(supplier);
        System.out.println("✅ Nhà cung cấp đã được thêm thành công!");
    }

    // Lấy danh sách tất cả nhà cung cấp
    public List<Supplier> getAllSuppliers() {
        return supplierDAO.getAllSuppliers();
    }

    // Lấy nhà cung cấp theo ID
    public Supplier getSupplierById(int supplierId) {
        return supplierDAO.getSupplierById(supplierId);
    }

    // Cập nhật thông tin nhà cung cấp
    public void updateSupplier(Supplier supplier) {
        supplierDAO.updateSupplier(supplier);
        System.out.println("✅ Nhà cung cấp đã được cập nhật thành công!");
    }

    // Xóa nhà cung cấp theo ID
    public void deleteSupplier(int supplierId) {
        supplierDAO.deleteSupplier(supplierId);
        System.out.println("✅ Nhà cung cấp đã được xóa thành công!");
    }
}
