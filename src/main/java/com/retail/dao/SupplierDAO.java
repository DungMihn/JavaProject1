/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.retail.dao;

import com.retail.model.Supplier;
import java.util.List;

/**
 *
 * @author Admin
 */
public interface SupplierDAO {
    void addSupplier(Supplier supplier);
    void updateSupplier(Supplier supplier);
    void deleteSupplier(int supplierId);
    Supplier getSupplierById(int supplierId);
    List<Supplier> getAllSuppliers();
}
