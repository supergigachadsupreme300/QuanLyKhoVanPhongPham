package com.quanlykhov.ui.component;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JButton;

/**
 * Nut menu tren thanh dieu huong ben trai.
 */
public class SidebarButton extends JButton {

    private boolean active;
    private final String bieuTuong;

    public SidebarButton(String bieuTuong, String nhan) {
        super("  " + bieuTuong + "   " + nhan);
        this.bieuTuong = bieuTuong;
        setHorizontalAlignment(LEFT);
        setFont(UiTheme.font(java.awt.Font.PLAIN, 14));
        setForeground(UiTheme.SIDEBAR_TEXT);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 20, 0, 16));
        setPreferredSize(new Dimension(230, 46));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
    }

    public String getBieuTuong() {
        return bieuTuong;
    }

    public void setActive(boolean active) {
        this.active = active;
        setForeground(active ? Color.WHITE : UiTheme.SIDEBAR_TEXT);
        setFont(UiTheme.font(active ? java.awt.Font.BOLD : java.awt.Font.PLAIN, 14));
        repaint();
    }

    public boolean isActive() {
        return active;
    }

    @Override
    protected void paintComponent(Graphics g) {
        UiTheme.batRenderingDep(g);
        Graphics2D g2 = (Graphics2D) g.create();
        if (active) {
            g2.setColor(UiTheme.SIDEBAR_ACTIVE);
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.setColor(Color.WHITE);
            g2.fillRect(0, 0, 4, getHeight());
        } else if (getModel().isRollover()) {
            g2.setColor(UiTheme.SIDEBAR_HOVER);
            g2.fillRect(0, 0, getWidth(), getHeight());
        }
        g2.dispose();
        super.paintComponent(g);
    }
}
