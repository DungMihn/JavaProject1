/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.retail.view;

/**
 *
 * @author Admin
 */
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PDFViewerFrame extends JFrame {
    private JPanel pdfPanel;
    private String filePath;

    public PDFViewerFrame(String filePath) {
        this.filePath = filePath;
        setTitle("PDF Viewer");
        setSize(1000, 900);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        pdfPanel = new JPanel();
        pdfPanel.setLayout(new BoxLayout(pdfPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(pdfPanel);
        add(scrollPane, BorderLayout.CENTER);

        // Tự động điều chỉnh kích thước khi thay đổi kích thước cửa sổ
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                loadPDF(filePath);
            }
        });

        loadPDF(filePath);
    }

    private void loadPDF(String filePath) {
        pdfPanel.removeAll(); // Xóa các thành phần cũ

        try (PDDocument document = PDDocument.load(new File(filePath))) {
            PDFRenderer pdfRenderer = new PDFRenderer(document);

            // Lấy kích thước của JFrame
            int frameWidth = getWidth();

            // Hiển thị từng trang của PDF
            for (int page = 0; page < document.getNumberOfPages(); page++) {
                BufferedImage image = pdfRenderer.renderImageWithDPI(page, 150); // DPI 150

                // Tính toán kích thước mới để vừa khít với chiều rộng của JFrame
                int imageWidth = image.getWidth();
                int imageHeight = image.getHeight();
                double aspectRatio = (double) imageHeight / imageWidth;

                int newWidth = frameWidth - 20; // Trừ đi padding
                int newHeight = (int) (newWidth * aspectRatio);

                // Thay đổi kích thước hình ảnh
                BufferedImage resizedImage = resizeImage(image, newWidth, newHeight);

                // Hiển thị hình ảnh đã điều chỉnh kích thước
                JLabel label = new JLabel(new ImageIcon(resizedImage));
                pdfPanel.add(label);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Không thể tải file PDF: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

        pdfPanel.revalidate();
        pdfPanel.repaint();
    }

    // Phương thức thay đổi kích thước hình ảnh
    private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
    BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
    Graphics2D g2d = resizedImage.createGraphics();

    // Thiết lập chất lượng cao cho việc scale hình ảnh
    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    // Vẽ lại hình ảnh với kích thước mới
    g2d.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
    g2d.dispose();

    return resizedImage;
}

}
