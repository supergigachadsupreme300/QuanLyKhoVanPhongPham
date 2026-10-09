package com.quanlykhov.ui.component;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import javax.swing.JPanel;

/**
 * JPanel co nen bo goc va tuy chon do bong nhe.
 */
public class RoundedPanel extends JPanel {

    private Color mauNen;
    private int banKinh;
    private boolean doBong;
    private Color mauVien;

    public RoundedPanel() {
        this(UiTheme.CARD, 14, false);
    }

    public RoundedPanel(Color mauNen, int banKinh, boolean doBong) {
        this.mauNen = mauNen;
        this.banKinh = banKinh;
        this.doBong = doBong;
        setOpaque(false);
    }

    public RoundedPanel(LayoutManager layout, Color mauNen, int banKinh, boolean doBong) {
        super(layout);
        this.mauNen = mauNen;
        this.banKinh = banKinh;
        this.doBong = doBong;
        setOpaque(false);
    }

    public void setMauNen(Color mauNen) {
        this.mauNen = mauNen;
        repaint();
    }

    public void setMauVien(Color mauVien) {
        this.mauVien = mauVien;
        repaint();
    }

    public void setBanKinh(int banKinh) {
        this.banKinh = banKinh;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        UiTheme.batRenderingDep(g);
        Graphics2D g2 = (Graphics2D) g.create();
        int w = getWidth();
        int h = getHeight();
        int pad = doBong ? 4 : 0;

        if (doBong) {
            g2.setColor(new Color(0, 0, 0, 18));
            g2.fillRoundRect(pad, pad + 3, w - pad * 2, h - pad * 2, banKinh, banKinh);
        }
        g2.setColor(mauNen);
        g2.fillRoundRect(0, 0, w - pad * 2 + pad, h - pad * 2 + pad, banKinh, banKinh);
        if (mauVien != null) {
            g2.setColor(mauVien);
            g2.drawRoundRect(0, 0, w - 1, h - 1, banKinh, banKinh);
        }
        g2.dispose();
        super.paintComponent(g);
    }
}
