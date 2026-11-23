package com.quanlykhov.ui.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTable;

/**
 * Tien ich tao cac thanh phan giao dien dung chung cho cac trang quan ly.
 */
public final class PanelUtil {

    private PanelUtil() {
    }

    /** Khung noi dung trang co vien dem. */
    public static JPanel trang() {
        JPanel p = new JPanel(new BorderLayout(0, 16));
        p.setBackground(UiTheme.BG);
        p.setBorder(BorderFactory.createEmptyBorder(20, 24, 22, 24));
        return p;
    }

    /** The trang (card) co tieu de va phan noi dung o giua. */
    public static RoundedPanel the(String tieuDe, String moTa, Component giua) {
        RoundedPanel card = new RoundedPanel(new BorderLayout(0, 12), UiTheme.CARD, 14, true);
        card.setBorder(BorderFactory.createEmptyBorder(16, 18, 18, 18));

        if (tieuDe != null) {
            JPanel head = new JPanel();
            head.setOpaque(false);
            head.setLayout(new BoxLayout(head, BoxLayout.Y_AXIS));
            JLabel lbl = new JLabel(tieuDe);
            lbl.setFont(UiTheme.FONT_HEADING);
            lbl.setForeground(UiTheme.TEXT);
            lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
            head.add(lbl);
            if (moTa != null) {
                JLabel sub = new JLabel(moTa);
                sub.setFont(UiTheme.FONT_SMALL);
                sub.setForeground(UiTheme.TEXT_MUTED);
                sub.setAlignmentX(Component.LEFT_ALIGNMENT);
                head.add(Box.createVerticalStrut(3));
                head.add(sub);
            }
            card.add(head, BorderLayout.NORTH);
        }
        if (giua != null) {
            card.add(giua, BorderLayout.CENTER);
        }
        return card;
    }

    /** Boc bang trong scrollpane da trang tri. */
    public static JScrollPane bocBang(JTable table) {
        UiTheme.trangTriBang(table);
        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createLineBorder(UiTheme.BORDER));
        sp.getViewport().setBackground(Color.WHITE);
        sp.setBackground(Color.WHITE);
        return sp;
    }

    /** Truong tim kiem da trang tri. */
    public static JTextField oTimKiem(int rong) {
        JTextField tf = new JTextField();
        tf.setPreferredSize(new Dimension(rong, 38));
        tf.setFont(UiTheme.FONT_BODY);
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiTheme.BORDER),
                BorderFactory.createEmptyBorder(4, 12, 4, 12)));
        return tf;
    }

    /** Mot dong gom cac nut / truong can deu nhau. */
    public static JPanel thanhCongCu(Component... items) {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        bar.setOpaque(false);
        for (Component c : items) {
            bar.add(c);
        }
        return bar;
    }

    /** Khoang trong de day cac nut ve ben phai. */
    public static Component dayPhai() {
        return javax.swing.Box.createHorizontalGlue();
    }

    /** Luoi deu cho cac the thong ke. */
    public static JPanel luoiThe(int soCot, int khoang, Component... the) {
        JPanel p = new JPanel(new GridLayout(1, soCot, khoang, 0));
        p.setOpaque(false);
        for (Component c : the) {
            p.add(c);
        }
        return p;
    }

    public static JLabel nhan(String text, java.awt.Font font, Color color) {
        JLabel l = new JLabel(text);
        l.setFont(font);
        l.setForeground(color);
        return l;
    }
}
