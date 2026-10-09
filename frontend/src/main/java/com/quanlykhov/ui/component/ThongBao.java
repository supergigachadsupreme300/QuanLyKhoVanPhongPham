package com.quanlykhov.ui.component;

import java.awt.Component;
import javax.swing.JOptionPane;

/**
 * Tien ich hien thi hop thoai thong bao.
 */
public final class ThongBao {

    private ThongBao() {
    }

    public static void thongTin(Component parent, String noiDung) {
        JOptionPane.showMessageDialog(parent, noiDung, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void loi(Component parent, String noiDung) {
        JOptionPane.showMessageDialog(parent, noiDung, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    public static void canhBao(Component parent, String noiDung) {
        JOptionPane.showMessageDialog(parent, noiDung, "Cảnh báo", JOptionPane.WARNING_MESSAGE);
    }

    public static boolean xacNhan(Component parent, String noiDung) {
        int traLoi = JOptionPane.showConfirmDialog(parent, noiDung, "Xác nhận",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return traLoi == JOptionPane.YES_OPTION;
    }
}
