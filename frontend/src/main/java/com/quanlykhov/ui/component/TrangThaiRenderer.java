package com.quanlykhov.ui.component;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Renderer hien thi trang thai dang nhan mau (Hoạt động / Khoá / Ngừng hợp tác).
 */
public class TrangThaiRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {
        JLabel label = (JLabel) super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);
        String trangThai = value == null ? "" : value.toString();
        label.setFont(UiTheme.FONT_BOLD);
        label.setForeground(UiTheme.trangThaiMau(trangThai));
        if (!isSelected) {
            label.setBackground(row % 2 == 0 ? Color.WHITE : UiTheme.ROW_ALT);
        }
        return label;
    }
}
