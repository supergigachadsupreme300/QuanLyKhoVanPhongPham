package com.quanlykhov.ui;

import com.quanlykhov.common.constants.AppConstants;
import com.quanlykhov.ui.mock.DuLieuMau;
import com.quanlykhov.ui.model.TaiKhoan;
import com.quanlykhov.ui.session.PhienDangNhap;
import com.quanlykhov.ui.component.RoundedButton;
import com.quanlykhov.ui.component.RoundedPanel;
import com.quanlykhov.ui.component.ThongBao;
import com.quanlykhov.ui.component.UiTheme;
import com.quanlykhov.ui.MainFrame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * Man hinh dang nhap cua phan he quan tri.
 */
public class DangNhapFrame extends JFrame {

    private final JTextField txtTenDangNhap = new JTextField();
    private final JPasswordField txtMatKhau = new JPasswordField();
    private final JLabel lblLoi = new JLabel(" ");

    public DangNhapFrame() {
        setTitle("Đăng nhập - " + AppConstants.APP_TITLE);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(940, 580);
        setMinimumSize(new Dimension(860, 540));
        setLocationRelativeTo(null);
        khoiTaoGiaoDien();
    }

    private void khoiTaoGiaoDien() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Color.WHITE);
        root.add(taoBangHieu(), BorderLayout.WEST);
        root.add(taoForm(), BorderLayout.CENTER);
        setContentPane(root);
        getRootPane().setDefaultButton(null);
    }

    private JPanel taoBangHieu() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                UiTheme.batRenderingDep(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setPaint(new GradientPaint(0, 0, UiTheme.PRIMARY_DARK,
                        0, getHeight(), UiTheme.PRIMARY));
                g2.fillRect(0, 0, getWidth(), getHeight());
                // vong tron trang tri
                g2.setColor(new Color(255, 255, 255, 18));
                g2.fillOval(-80, getHeight() - 260, 320, 320);
                g2.fillOval(getWidth() - 150, -90, 260, 260);
                g2.dispose();
            }
        };
        panel.setPreferredSize(new Dimension(420, 0));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(70, 48, 40, 48));

        JLabel icon = new JLabel("\uD83D\uDCE6");
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 56));
        icon.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(icon);
        panel.add(Box.createVerticalStrut(18));

        JLabel ten = new JLabel("<html>Quản lý Kho<br>Văn Phòng Phẩm</html>");
        ten.setFont(new Font("Segoe UI", Font.BOLD, 30));
        ten.setForeground(Color.WHITE);
        ten.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(ten);
        panel.add(Box.createVerticalStrut(14));

        JLabel phu = new JLabel("<html>Hệ thống quản lý kho hàng văn phòng phẩm<br>"
                + "dành cho doanh nghiệp thương mại.</html>");
        phu.setFont(UiTheme.FONT_BODY);
        phu.setForeground(new Color(0xD5, 0xE2, 0xF0));
        phu.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(phu);

        panel.add(Box.createVerticalGlue());

        JLabel pb = new JLabel("\u2022 Phân hệ Quản trị (Admin)");
        pb.setFont(UiTheme.FONT_BOLD);
        pb.setForeground(Color.WHITE);
        pb.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(pb);
        panel.add(Box.createVerticalStrut(6));
        JLabel pb2 = new JLabel("\u2022 Quản lý người dùng & phân quyền");
        pb2.setFont(UiTheme.FONT_SMALL);
        pb2.setForeground(new Color(0xBF, 0xD2, 0xE6));
        pb2.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(pb2);
        panel.add(Box.createVerticalStrut(28));

        JLabel footer = new JLabel(AppConstants.APP_FOOTER);
        footer.setFont(UiTheme.FONT_SMALL);
        footer.setForeground(new Color(0x9F, 0xB8, 0xD2));
        footer.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(footer);

        return panel;
    }

    private JPanel taoForm() {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(Color.WHITE);

        RoundedPanel card = new RoundedPanel(new BorderLayout(), Color.WHITE, 16, false);
        card.setBorder(BorderFactory.createEmptyBorder(38, 44, 38, 44));
        card.setPreferredSize(new Dimension(400, 430));

        JPanel noiDung = new JPanel();
        noiDung.setOpaque(false);
        noiDung.setLayout(new BoxLayout(noiDung, BoxLayout.Y_AXIS));

        JLabel tieuDe = new JLabel("Đăng nhập hệ thống");
        tieuDe.setFont(UiTheme.font(Font.BOLD, 24));
        tieuDe.setForeground(UiTheme.TEXT);
        tieuDe.setAlignmentX(Component.LEFT_ALIGNMENT);
        noiDung.add(tieuDe);
        noiDung.add(Box.createVerticalStrut(6));

        JLabel phuDe = new JLabel("Vui lòng đăng nhập để tiếp tục quản trị.");
        phuDe.setFont(UiTheme.FONT_SUBTITLE);
        phuDe.setForeground(UiTheme.TEXT_MUTED);
        phuDe.setAlignmentX(Component.LEFT_ALIGNMENT);
        noiDung.add(phuDe);
        noiDung.add(Box.createVerticalStrut(26));

        noiDung.add(taoTruong("Tên đăng nhập", txtTenDangNhap));
        noiDung.add(Box.createVerticalStrut(16));
        noiDung.add(taoTruong("Mật khẩu", txtMatKhau));

        lblLoi.setFont(UiTheme.FONT_SMALL);
        lblLoi.setForeground(UiTheme.DANGER);
        lblLoi.setAlignmentX(Component.LEFT_ALIGNMENT);
        noiDung.add(Box.createVerticalStrut(10));
        noiDung.add(lblLoi);
        noiDung.add(Box.createVerticalStrut(14));

        RoundedButton btnDangNhap = new RoundedButton("Đăng nhập");
        btnDangNhap.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnDangNhap.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btnDangNhap.addActionListener(e -> xuLyDangNhap());
        noiDung.add(btnDangNhap);

        noiDung.add(Box.createVerticalStrut(22));
        JLabel goiY = new JLabel("<html><i>Tài khoản demo: <b>admin</b> / <b>admin123</b></i></html>");
        goiY.setFont(UiTheme.FONT_SMALL);
        goiY.setForeground(UiTheme.TEXT_MUTED);
        goiY.setAlignmentX(Component.LEFT_ALIGNMENT);
        noiDung.add(goiY);

        card.add(noiDung, BorderLayout.CENTER);

        wrapper.add(card);
        getRootPane().setDefaultButton(btnDangNhap);
        return wrapper;
    }

    private JPanel taoTruong(String nhan, JTextField o) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lbl = new JLabel(nhan);
        lbl.setFont(UiTheme.FONT_BOLD);
        lbl.setForeground(UiTheme.TEXT);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(lbl);
        p.add(Box.createVerticalStrut(6));

        o.setFont(UiTheme.FONT_BODY);
        o.setPreferredSize(new Dimension(300, 40));
        o.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        o.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiTheme.BORDER),
                BorderFactory.createEmptyBorder(4, 12, 4, 12)));
        o.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(o);
        return p;
    }

    private void xuLyDangNhap() {
        String tenDangNhap = txtTenDangNhap.getText().trim();
        String matKhau = new String(txtMatKhau.getPassword());
        if (tenDangNhap.isEmpty() || matKhau.isEmpty()) {
            lblLoi.setText("Vui lòng nhập tên đăng nhập và mật khẩu.");
            return;
        }
        DuLieuMau kho = DuLieuMau.getInstance();
        TaiKhoan tk = kho.dangNhap(tenDangNhap, matKhau);
        if (tk == null) {
            lblLoi.setText("Tên đăng nhập hoặc mật khẩu không đúng.");
            return;
        }
        if (tk.getTrangThai() != null
                && !tk.getTrangThai().equals(AppConstants.TRANG_THAI_HOAT_DONG)) {
            lblLoi.setText("Tài khoản đang bị khoá. Vui lòng liên hệ quản trị viên.");
            return;
        }
        if (!"VT01".equals(tk.getMaVT())) {
            ThongBao.canhBao(this, "Tài khoản này không có quyền truy cập phân hệ Quản trị (Admin).\n"
                    + "Vui lòng dùng tài khoản quản trị viên.");
            return;
        }
        PhienDangNhap.getInstance().dangNhap(tk);
        MainFrame mainFrame = new MainFrame();
        mainFrame.setVisible(true);
        dispose();
    }

    /** Tien ich dong bo title. */
    public static void hienThi() {
        SwingUtilities.invokeLater(() -> new DangNhapFrame().setVisible(true));
    }

    private static Window windowOf(Component c) {
        return SwingUtilities.getWindowAncestor(c);
    }
}
