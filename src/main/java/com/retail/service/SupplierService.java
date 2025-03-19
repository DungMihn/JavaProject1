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
    public boolean addSupplier(Supplier supplier) {
        return supplierDAO.addSupplier(supplier);
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
    public boolean updateSupplier(Supplier supplier) {
        return supplierDAO.updateSupplier(supplier);
    }

    // Xóa nhà cung cấp theo ID
    public boolean deleteSupplier(int supplierId) {
        return supplierDAO.deleteSupplier(supplierId);
    }
    //
     public List<Supplier> searchSuppliersByName(String keyword) {
        return supplierDAO.searchSuppliersByName(keyword);
    }
     //
     public int getNextSupplierId() {
        return supplierDAO.getNextSupplierId();
    }
}
