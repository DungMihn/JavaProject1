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
import javax.swing.text.*;
import java.awt.event.*;
import java.util.List;
import java.util.stream.Collectors;

public class AutoCompleteTextField {
    private JTextField textField;
    private List<String> suggestions;
    private JPopupMenu popupMenu;
    private JList<String> suggestionList;

    public AutoCompleteTextField(JTextField textField, List<String> suggestions ) {
    this.textField = textField;
    this.suggestions = suggestions;
    this.popupMenu = new JPopupMenu();
    this.suggestionList = new JList<>();
    
    textField.addKeyListener(new KeyAdapter() {
        @Override
        public void keyReleased(KeyEvent e) {
            String text = textField.getText();
            System.out.println("🔍 Kiểm tra input: " + text);

            if (text.isEmpty()) {
                popupMenu.setVisible(false);
                return;
            }

            // Lọc danh sách gợi ý
            List<String> filtered = suggestions.stream()
                    .filter(s -> s.toLowerCase().startsWith(text.toLowerCase()))
                    .collect(Collectors.toList());

            System.out.println("📌 Gợi ý tìm được: " + filtered);

            if (filtered.isEmpty()) {
                popupMenu.setVisible(false);
                return;
            }

            suggestionList.setListData(filtered.toArray(new String[0]));
            suggestionList.setSelectedIndex(0);
            popupMenu.removeAll();
            popupMenu.add(new JScrollPane(suggestionList));

            // Hiển thị popup ngay dưới text field
            popupMenu.show(textField, 0, textField.getHeight() + 5);

            System.out.println("✅ Hiển thị gợi ý dưới TextField");

            suggestionList.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    textField.setText(suggestionList.getSelectedValue());
                    popupMenu.setVisible(false);
                    System.out.println("✅ Người dùng chọn: " + suggestionList.getSelectedValue());
                }
            });
        }
    });

    textField.addFocusListener(new FocusAdapter() {
        @Override
        public void focusLost(FocusEvent e) {
            popupMenu.setVisible(false);
        }
    });
}

}

