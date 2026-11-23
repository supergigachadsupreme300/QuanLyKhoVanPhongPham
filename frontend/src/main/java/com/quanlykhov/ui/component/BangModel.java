package com.quanlykhov.ui.component;

import javax.swing.table.DefaultTableModel;

/**
 * TableModel chi de doc (khong cho phep sua truc tiep tren bang).
 */
public class BangModel extends DefaultTableModel {

    public BangModel(String[] cot, int soDong) {
        super(cot, soDong);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    public void xoaHet() {
        setRowCount(0);
    }
}
