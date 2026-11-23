package com.quanlykhov.ui.component;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JPanel;

/**
 * Bieu do cot don gian ve so luong tai khoan theo vai tro.
 */
public class BarChartPanel extends JPanel {

    private final LinkedHashMap<String, Integer> duLieu = new LinkedHashMap<String, Integer>();
    private final Color[] bangMau = {
        UiTheme.PRIMARY, UiTheme.SUCCESS, UiTheme.WARNING, UiTheme.INFO, UiTheme.DANGER,
        new Color(0x7C, 0x3A, 0xED), new Color(0x0D, 0x94, 0x88)
    };

    public BarChartPanel() {
        setOpaque(false);
    }

    public void setDuLieu(Map<String, Integer> data) {
        duLieu.clear();
        duLieu.putAll(data);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        UiTheme.batRenderingDep(g);
        Graphics2D g2 = (Graphics2D) g.create();

        int w = getWidth();
        int h = getHeight();
        int left = 20;
        int right = 20;
        int top = 20;
        int bottom = 46;
        int chartH = h - top - bottom;
        int chartW = w - left - right;

        if (duLieu.isEmpty()) {
            g2.setColor(UiTheme.TEXT_MUTED);
            g2.setFont(UiTheme.FONT_BODY);
            g2.drawString("Chưa có dữ liệu", left, h / 2);
            g2.dispose();
            return;
        }

        int max = 1;
        for (int v : duLieu.values()) {
            max = Math.max(max, v);
        }

        // truc ngang
        g2.setColor(UiTheme.BORDER);
        g2.drawLine(left, top + chartH, left + chartW, top + chartH);

        int n = duLieu.size();
        int khoang = chartW / n;
        int barW = Math.min(70, (int) (khoang * 0.5));
        int i = 0;
        FontMetrics fm = g2.getFontMetrics(UiTheme.FONT_SMALL);
        for (Map.Entry<String, Integer> e : duLieu.entrySet()) {
            int giaTri = e.getValue();
            int barH = (int) ((double) giaTri / max * (chartH - 20));
            int x = left + i * khoang + (khoang - barW) / 2;
            int y = top + chartH - barH;

            g2.setColor(bangMau[i % bangMau.length]);
            g2.fillRoundRect(x, y, barW, barH, 8, 8);

            // gia tri tren dinh cot
            g2.setColor(UiTheme.TEXT);
            g2.setFont(UiTheme.FONT_BOLD);
            String val = String.valueOf(giaTri);
            g2.drawString(val, x + (barW - g2.getFontMetrics().stringWidth(val)) / 2, y - 6);

            // nhan duoi cot
            g2.setColor(UiTheme.TEXT_MUTED);
            g2.setFont(UiTheme.FONT_SMALL);
            String nhan = e.getKey();
            int nhanW = fm.stringWidth(nhan);
            if (nhanW > khoang - 4) {
                nhan = nhan.substring(0, Math.max(1, nhan.length() - 2)) + "..";
                nhanW = fm.stringWidth(nhan);
            }
            g2.drawString(nhan, left + i * khoang + (khoang - nhanW) / 2, top + chartH + 20);
            i++;
        }
        g2.dispose();
    }
}
