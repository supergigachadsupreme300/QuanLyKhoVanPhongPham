package com.quanlykhov.ui.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * The thong ke tren bang dieu khien (dashboard).
 */
public class StatCard extends RoundedPanel {

    private final JLabel lblGiaTri = new JLabel("0");

    public StatCard(String bieuTuong, String nhan, Color mauNhan, String giaTri) {
        super(new BorderLayout(), UiTheme.CARD, 14, true);
        setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 18));
        setPreferredSize(new Dimension(220, 110));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        top.setOpaque(false);
        JLabel icon = new JLabel(bieuTuong);
        icon.setFont(UiTheme.font(java.awt.Font.PLAIN, 22));
        icon.setForeground(mauNhan);
        icon.setOpaque(true);
        icon.setBackground(new Color(mauNhan.getRed(), mauNhan.getGreen(), mauNhan.getBlue(), 30));
        icon.setHorizontalAlignment(SwingConstants.CENTER);
        icon.setPreferredSize(new Dimension(40, 40));
        icon.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        top.add(icon);

        JLabel lblNhan = new JLabel(nhan);
        lblNhan.setFont(UiTheme.FONT_SMALL);
        lblNhan.setForeground(UiTheme.TEXT_MUTED);
        top.add(lblNhan);

        lblGiaTri.setText(giaTri);
        lblGiaTri.setFont(UiTheme.font(java.awt.Font.BOLD, 30));
        lblGiaTri.setForeground(UiTheme.TEXT);

        add(top, BorderLayout.NORTH);
        add(lblGiaTri, BorderLayout.CENTER);
    }

    public void setGiaTri(String giaTri) {
        lblGiaTri.setText(giaTri);
    }
}
