/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.view;

/**
 *
 * @author Admin
 */
import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SimpleAutoComplete {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Test AutoComplete");
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JTextField textField = new JTextField();
        frame.add(textField);
        
        List<String> suggestions = Arrays.asList("Sữa tươi Vinamilk", "Bánh ChocoPie", "Mì Hảo Hảo", "Nước suối Lavie", "Dầu ăn Neptune", "Bột giặt Omo");
        JPopupMenu popupMenu = new JPopupMenu();
        JList<String> suggestionList = new JList<>();
        
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String text = textField.getText();
                System.out.println("🔍 Kiểm tra input: " + text);
                if (text.isEmpty()) {
                    popupMenu.setVisible(false);
                    return;
                }

                List<String> filtered = suggestions.stream()
                        .filter(s -> s.toLowerCase().contains(text.toLowerCase()))
                        .collect(Collectors.toList());

                if (filtered.isEmpty()) {
                    popupMenu.setVisible(false);
                    return;
                }

                suggestionList.setListData(filtered.toArray(new String[0]));
                popupMenu.removeAll();
                popupMenu.add(new JScrollPane(suggestionList));
                popupMenu.show(textField, 0, textField.getHeight());

                suggestionList.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent e) {
                        textField.setText(suggestionList.getSelectedValue());
                        popupMenu.setVisible(false);
                    }
                });
            }
        });

        frame.setVisible(true);
    }
}

