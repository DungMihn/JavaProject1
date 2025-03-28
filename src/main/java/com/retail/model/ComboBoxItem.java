/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.model;

/**
 *
 * @author Admin
 */
public class ComboBoxItem {
    private Integer id;
    private String name;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ComboBoxItem(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }
    

    @Override
    public String toString() {
        return name; // Hiển thị tên trong JComboBox
    }
    
    
}
