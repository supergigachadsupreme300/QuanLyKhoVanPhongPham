package com.quanlykhov.ui.component;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;

/**
 * Bang mau va font dung thong nhat cho phan he quan tri.
 */
public final class UiTheme {

    private UiTheme() {
    }

    public static final Color PRIMARY = new Color(0x31, 0x68, 0x96);
    public static final Color PRIMARY_DARK = new Color(0x24, 0x4B, 0x6B);
    public static final Color PRIMARY_LIGHT = new Color(0xE8, 0xF0, 0xF8);

    public static final Color SIDEBAR_BG = new Color(0x1B, 0x2A, 0x41);
    public static final Color SIDEBAR_HOVER = new Color(0x2C, 0x42, 0x5E);
    public static final Color SIDEBAR_ACTIVE = new Color(0x31, 0x68, 0x96);
    public static final Color SIDEBAR_TEXT = new Color(0xC7, 0xD2, 0xE0);

    public static final Color BG = new Color(0xF4, 0xF6, 0xFA);
    public static final Color CARD = Color.WHITE;
    public static final Color BORDER = new Color(0xE2, 0xE8, 0xF0);

    public static final Color TEXT = new Color(0x1F, 0x29, 0x37);
    public static final Color TEXT_MUTED = new Color(0x6B, 0x72, 0x80);

    public static final Color SUCCESS = new Color(0x16, 0xA3, 0x4A);
    public static final Color DANGER = new Color(0xDC, 0x26, 0x26);
    public static final Color WARNING = new Color(0xD9, 0x77, 0x06);
    public static final Color INFO = new Color(0x0E, 0xA5, 0xE9);

    public static final Color ROW_ALT = new Color(0xF7, 0xFA, 0xFC);
    public static final Color SELECTION = new Color(0xD6, 0xE4, 0xF2);

    public static Font font(int style, int size) {
        return new Font("Segoe UI", style, size);
    }

    public static final Font FONT_TITLE = font(Font.BOLD, 22);
    public static final Font FONT_HEADING = font(Font.BOLD, 16);
    public static final Font FONT_SUBTITLE = font(Font.PLAIN, 13);
    public static final Font FONT_BODY = font(Font.PLAIN, 14);
    public static final Font FONT_SMALL = font(Font.PLAIN, 12);
    public static final Font FONT_BOLD = font(Font.BOLD, 14);
    public static final Font FONT_BUTTON = font(Font.BOLD, 13);

    /** Ap dung kieu dang cho JTable. */
    public static void trangTriBang(JTable table) {
        table.setFont(FONT_BODY);
        table.setRowHeight(34);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(BORDER);
        table.setBackground(Color.WHITE);
        table.setForeground(TEXT);
        table.setSelectionBackground(SELECTION);
        table.setSelectionForeground(TEXT);
        table.setFillsViewportHeight(true);
        table.setIntercellSpacing(new java.awt.Dimension(0, 1));

        if (table.getTableHeader() != null) {
            JTableHeader header = table.getTableHeader();
            header.setFont(FONT_BOLD);
            header.setBackground(PRIMARY);
            header.setForeground(Color.WHITE);
            header.setPreferredSize(new java.awt.Dimension(header.getPreferredSize().width, 36));
            header.setReorderingAllowed(false);
            header.setBorder(BorderFactory.createEmptyBorder());
        }
    }

    /** Bo goc cho khung nhin. */
    public static void batRenderingDep(Graphics g) {
        if (g instanceof Graphics2D) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        }
    }

    public static Color trangThaiMau(String trangThai) {
        if (trangThai == null) {
            return TEXT_MUTED;
        }
        if (trangThai.equalsIgnoreCase("Hoạt động")) {
            return SUCCESS;
        }
        if (trangThai.equalsIgnoreCase("Khoá")) {
            return WARNING;
        }
        if (trangThai.equalsIgnoreCase("Ngừng hợp tác")) {
            return DANGER;
        }
        return TEXT_MUTED;
    }
}
