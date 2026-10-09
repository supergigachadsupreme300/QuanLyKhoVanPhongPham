package com.quanlykhov.ui;

import com.quanlykhov.common.constants.AppConstants;
import com.quanlykhov.ui.session.PhienDangNhap;
import com.quanlykhov.ui.component.RoundedButton;
import com.quanlykhov.ui.component.SidebarButton;
import com.quanlykhov.ui.component.ThongBao;
import com.quanlykhov.ui.component.UiTheme;
import com.quanlykhov.ui.DangNhapFrame;
import com.quanlykhov.ui.panel.NhanVienPanel;
import com.quanlykhov.ui.panel.TaiKhoanPanel;
import com.quanlykhov.ui.panel.TongQuanPanel;
import com.quanlykhov.ui.panel.VaiTroPanel;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Cua so chinh cua phan he quan tri, chua thanh dieu huong va vung noi dung.
 */
public class MainFrame extends JFrame {

    private static final String[][] MENU = {
        {"tongquan", "▦", "Tổng quan"},
        {"taikhoan", "◉", "Quản lý tài khoản"},
        {"nhanvien", "❖", "Quản lý nhân viên"},
        {"vaitro", "✦", "Vai trò & Phân quyền"}
    };

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel vungNoiDung = new JPanel(cardLayout);
    private final JLabel lblTieuDe = new JLabel();
    private final JLabel lblMoTa = new JLabel();
    private final Map<String, SidebarButton> nutMenu = new LinkedHashMap<String, SidebarButton>();
    private final Map<String, JLabel> tieuDeTrang = new LinkedHashMap<String, JLabel>();

    public MainFrame() {
        setTitle(AppConstants.APP_TITLE + " - " + AppConstants.APP_SUBTITLE);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1280, 760);
        setMinimumSize(new Dimension(1100, 680));
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UiTheme.BG);
        root.add(taoThanhDieuHuong(), BorderLayout.WEST);
        root.add(taoVungChinh(), BorderLayout.CENTER);
        setContentPane(root);

        chonMenu("tongquan");
    }

    // ------------------------------------------------------------------
    // Thanh dieu huong ben trai
    // ------------------------------------------------------------------

    private JPanel taoThanhDieuHuong() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(UiTheme.SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(240, 0));

        sidebar.add(taoDauTrang());
        sidebar.add(Box.createVerticalStrut(8));

        for (String[] item : MENU) {
            SidebarButton nut = new SidebarButton(item[1], item[2]);
            nut.addActionListener(e -> chonMenu(item[0]));
            nutMenu.put(item[0], nut);
            sidebar.add(nut);
        }

        sidebar.add(Box.createVerticalGlue());
        sidebar.add(taoChanTrang());
        return sidebar;
    }

    private JPanel taoDauTrang() {
        JPanel top = new JPanel();
        top.setOpaque(false);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.setBorder(BorderFactory.createEmptyBorder(22, 20, 18, 20));
        top.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));

        JPanel dong = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        dong.setOpaque(false);
        JLabel logo = new JLabel("VPP");
        logo.setFont(UiTheme.font(java.awt.Font.BOLD, 16));
        logo.setForeground(Color.WHITE);
        logo.setOpaque(true);
        logo.setBackground(UiTheme.PRIMARY);
        logo.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        dong.add(logo);

        JLabel ten = new JLabel("Quản lý kho");
        ten.setFont(UiTheme.font(java.awt.Font.BOLD, 17));
        ten.setForeground(Color.WHITE);
        dong.add(ten);
        top.add(dong);

        top.add(Box.createVerticalStrut(8));
        JLabel phan = new JLabel("PHÂN HỆ QUẢN TRỊ");
        phan.setFont(UiTheme.font(java.awt.Font.BOLD, 11));
        phan.setForeground(new Color(0x8F, 0xA6, 0xBF));
        top.add(phan);
        return top;
    }

    private JPanel taoChanTrang() {
        JPanel bottom = new JPanel();
        bottom.setOpaque(false);
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
        bottom.setBorder(BorderFactory.createEmptyBorder(12, 16, 18, 16));
        bottom.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        PhienDangNhap phien = PhienDangNhap.getInstance();

        JPanel user = new JPanel(new BorderLayout(8, 0));
        user.setOpaque(false);
        JLabel avatar = new JLabel(phien.getTenDangNhap().substring(0, 1).toUpperCase());
        avatar.setFont(UiTheme.font(java.awt.Font.BOLD, 16));
        avatar.setForeground(Color.WHITE);
        avatar.setOpaque(true);
        avatar.setBackground(UiTheme.PRIMARY);
        avatar.setHorizontalAlignment(JLabel.CENTER);
        avatar.setPreferredSize(new Dimension(38, 38));
        avatar.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        JLabel tenNguoiDung = new JLabel(phien.getHoTen().isEmpty() ? phien.getTenDangNhap() : phien.getHoTen());
        tenNguoiDung.setFont(UiTheme.font(java.awt.Font.BOLD, 13));
        tenNguoiDung.setForeground(Color.WHITE);
        tenNguoiDung.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel vaiTro = new JLabel(phien.getTenVaiTro());
        vaiTro.setFont(UiTheme.FONT_SMALL);
        vaiTro.setForeground(new Color(0x9F, 0xB8, 0xD2));
        vaiTro.setAlignmentX(Component.LEFT_ALIGNMENT);
        info.add(tenNguoiDung);
        info.add(Box.createVerticalStrut(2));
        info.add(vaiTro);

        user.add(avatar, BorderLayout.WEST);
        user.add(info, BorderLayout.CENTER);
        bottom.add(user);
        bottom.add(Box.createVerticalStrut(12));

        RoundedButton btnDangXuat = new RoundedButton("Đăng xuất", RoundedButton.Kieu.NGUY_HIEM);
        btnDangXuat.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        btnDangXuat.addActionListener(e -> xuLyDangXuat());
        bottom.add(btnDangXuat);
        return bottom;
    }

    // ------------------------------------------------------------------
    // Vung noi dung
    // ------------------------------------------------------------------

    private JPanel taoVungChinh() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(UiTheme.BG);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, UiTheme.BORDER),
                BorderFactory.createEmptyBorder(14, 26, 14, 26)));

        JPanel tieuDe = new JPanel();
        tieuDe.setOpaque(false);
        tieuDe.setLayout(new BoxLayout(tieuDe, BoxLayout.Y_AXIS));
        lblTieuDe.setFont(UiTheme.FONT_TITLE);
        lblTieuDe.setForeground(UiTheme.TEXT);
        lblTieuDe.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblMoTa.setFont(UiTheme.FONT_SUBTITLE);
        lblMoTa.setForeground(UiTheme.TEXT_MUTED);
        lblMoTa.setAlignmentX(Component.LEFT_ALIGNMENT);
        tieuDe.add(lblTieuDe);
        tieuDe.add(Box.createVerticalStrut(3));
        tieuDe.add(lblMoTa);
        header.add(tieuDe, BorderLayout.WEST);

        JLabel phien = new JLabel("Xin chào, " + PhienDangNhap.getInstance().getTenDangNhap());
        phien.setFont(UiTheme.FONT_BOLD);
        phien.setForeground(UiTheme.PRIMARY);
        header.add(phien, BorderLayout.EAST);

        vungNoiDung.setBackground(UiTheme.BG);
        vungNoiDung.add(new TongQuanPanel(), "tongquan");
        vungNoiDung.add(new TaiKhoanPanel(), "taikhoan");
        vungNoiDung.add(new NhanVienPanel(), "nhanvien");
        vungNoiDung.add(new VaiTroPanel(), "vaitro");


        main.add(header, BorderLayout.NORTH);
        main.add(vungNoiDung, BorderLayout.CENTER);
        return main;
    }

    private void chonMenu(String key) {
        cardLayout.show(vungNoiDung, key);
        for (Map.Entry<String, SidebarButton> e : nutMenu.entrySet()) {
            e.getValue().setActive(e.getKey().equals(key));
        }
        for (String[] item : MENU) {
            if (item[0].equals(key)) {
                lblTieuDe.setText(item[2]);
                lblMoTa.setText(moTaTrang(key));
            }
        }
    }

    private String moTaTrang(String key) {
        switch (key) {
            case "taikhoan":
                return "Thêm, sửa, xoá tài khoản và gán vai trò (phân quyền) cho người dùng.";
            case "nhanvien":
                return "Quản lý hồ sơ nhân viên trong hệ thống.";
            case "vaitro":
                return "Quản lý vai trò và thiết lập quyền truy cập theo từng nhóm chức năng.";
            case "tongquan":
            default:
                return "Tổng quan tình hình tài khoản và nhân sự của hệ thống.";
        }
    }

    private void xuLyDangXuat() {
        if (!ThongBao.xacNhan(this, "Bạn có chắc chắn muốn đăng xuất khỏi hệ thống?")) {
            return;
        }
        PhienDangNhap.getInstance().dangXuat();
        new DangNhapFrame().setVisible(true);
        dispose();
    }

    // ------------------------------------------------------------------
    // Diem khoi chay phan he Quan tri (Admin)
    // ------------------------------------------------------------------

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            cauHinhGiaoDien();
            new DangNhapFrame().setVisible(true);
        });
    }

    private static void cauHinhGiaoDien() {
        try {
            javax.swing.UIManager.setLookAndFeel(
                    javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // Giu nguyen Look and Feel mac dinh neu khong thiet lap duoc.
        }
        javax.swing.UIManager.put("OptionPane.messageFont", UiTheme.FONT_BODY);
        javax.swing.UIManager.put("OptionPane.buttonFont", UiTheme.FONT_BUTTON);
        javax.swing.UIManager.put("Table.font", UiTheme.FONT_BODY);
        javax.swing.UIManager.put("Label.font", UiTheme.FONT_BODY);
        javax.swing.UIManager.put("Button.font", UiTheme.FONT_BUTTON);
        javax.swing.UIManager.put("ComboBox.font", UiTheme.FONT_BODY);
        javax.swing.UIManager.put("TextField.font", UiTheme.FONT_BODY);
        javax.swing.UIManager.put("PasswordField.font", UiTheme.FONT_BODY);
        javax.swing.UIManager.put("TitledBorder.font", UiTheme.FONT_BOLD);
    }
}
