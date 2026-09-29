package com.quanlykhov.ui;

import javax.swing.*;

/**
 * Main application frame for Office Supplies Inventory Management System
 */
public class MainFrame extends JFrame {
    
    public MainFrame() {
        setTitle("Quản Lý Kho Văn Phòng Phẩm");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        // TODO: Initialize UI components
        JPanel mainPanel = new JPanel();
        mainPanel.add(new JLabel("Welcome to Inventory Management System"));
        
        add(mainPanel);
        setVisible(true);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame());
    }
}
