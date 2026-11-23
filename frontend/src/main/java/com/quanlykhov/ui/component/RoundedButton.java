package com.quanlykhov.ui.component;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JButton;

/**
 * Nut bam bo goc voi cac phan loai mau (chinh, phu, nguy hiem...).
 */
public class RoundedButton extends JButton {

    public enum Kieu {
        CHINH, PHU, NGUY_HIEM, THANH_CONG, CANH_BAO, TRONG_SUOT
    }

    private Color mauNen;
    private Color mauChu;
    private Color mauVien;
    private int banKinh = 10;

    public RoundedButton(String text) {
        this(text, Kieu.CHINH);
    }

    public RoundedButton(String text, Kieu kieu) {
        super(text);
        apDungKieu(kieu);
        setFont(UiTheme.FONT_BUTTON);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(9, 18, 9, 18));
        setPreferredSize(new Dimension(getPreferredSize().width, 38));
    }

    private void apDungKieu(Kieu kieu) {
        switch (kieu) {
            case PHU:
                mauNen = Color.WHITE;
                mauChu = UiTheme.PRIMARY;
                mauVien = UiTheme.BORDER;
                break;
            case NGUY_HIEM:
                mauNen = UiTheme.DANGER;
                mauChu = Color.WHITE;
                break;
            case THANH_CONG:
                mauNen = UiTheme.SUCCESS;
                mauChu = Color.WHITE;
                break;
            case CANH_BAO:
                mauNen = UiTheme.WARNING;
                mauChu = Color.WHITE;
                break;
            case TRONG_SUOT:
                mauNen = UiTheme.PRIMARY_LIGHT;
                mauChu = UiTheme.PRIMARY;
                break;
            case CHINH:
            default:
                mauNen = UiTheme.PRIMARY;
                mauChu = Color.WHITE;
                break;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        UiTheme.batRenderingDep(g);
        Graphics2D g2 = (Graphics2D) g.create();
        Color nen = mauNen;
        if (!isEnabled()) {
            nen = new Color(0xC5, 0xCC, 0xD6);
        } else if (getModel().isPressed()) {
            nen = nen.darker();
        } else if (getModel().isRollover()) {
            nen = new Color(
                    Math.max(0, nen.getRed() - 18),
                    Math.max(0, nen.getGreen() - 18),
                    Math.max(0, nen.getBlue() - 18));
        }
        g2.setColor(nen);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), banKinh, banKinh);
        if (mauVien != null && isEnabled()) {
            g2.setColor(mauVien);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, banKinh, banKinh);
        }
        g2.dispose();
        setForeground(isEnabled() ? mauChu : new Color(0xEE, 0xF1, 0xF5));
        super.paintComponent(g);
    }
}
