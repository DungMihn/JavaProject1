/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.retail.dao;

import com.retail.model.Product;
import com.retail.model.Supplier;
import java.util.List;

/**
 *
 * @author Admin
 */
public interface SupplierDAO {
    boolean addSupplier(Supplier supplier);
    boolean updateSupplier(Supplier supplier);
    boolean deleteSupplier(int supplierId);
    Supplier getSupplierById(int supplierId);
    List<Supplier> getAllSuppliers();
    public List<Supplier> searchSuppliersByName(String keyword);
    public int getNextSupplierId();
    public List<Product> getProductsBySupplierId(int supplierId);
}
