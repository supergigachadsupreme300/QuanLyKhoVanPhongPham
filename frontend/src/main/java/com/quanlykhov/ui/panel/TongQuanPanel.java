package com.quanlykhov.ui.panel;

import com.quanlykhov.common.constants.AppConstants;
import com.quanlykhov.ui.mock.DuLieuMau;
import com.quanlykhov.ui.model.TaiKhoan;
import com.quanlykhov.ui.model.VaiTro;
import com.quanlykhov.ui.session.PhienDangNhap;
import com.quanlykhov.ui.component.BangModel;
import com.quanlykhov.ui.component.BarChartPanel;
import com.quanlykhov.ui.component.PanelUtil;
import com.quanlykhov.ui.component.RoundedButton;
import com.quanlykhov.ui.component.RoundedPanel;
import com.quanlykhov.ui.component.StatCard;
import com.quanlykhov.ui.component.ThongBao;
import com.quanlykhov.ui.component.TrangThaiRenderer;
import com.quanlykhov.ui.component.UiTheme;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.LinkedHashMap;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 * Bang dieu khien tong quan (dashboard) cua phan he quan tri.
 */
public class TongQuanPanel extends JPanel {

    private final StatCard theTaiKhoan = new StatCard("◉", "Tổng tài khoản", UiTheme.PRIMARY, "0");
    private final StatCard theHoatDong = new StatCard("✔", "Đang hoạt động", UiTheme.SUCCESS, "0");
    private final StatCard theNhanVien = new StatCard("❖", "Nhân viên", UiTheme.WARNING, "0");
    private final StatCard theVaiTro = new StatCard("✦", "Vai trò", UiTheme.INFO, "0");

    private final BarChartPanel bieuDo = new BarChartPanel();
    private final BangModel modelGanDay = new BangModel(
            new String[]{"Mã TK", "Tên đăng nhập", "Vai trò", "Trạng thái"}, 0);
    private final JTable bangGanDay = new JTable(modelGanDay);

    private final JLabel lblXinChao = new JLabel();

    public TongQuanPanel() {
        setLayout(new BorderLayout());
        setBackground(UiTheme.BG);
        setBorder(BorderFactory.createEmptyBorder(20, 24, 22, 24));
        khoiTaoGiaoDien();
        taiDuLieu();
    }

    private void khoiTaoGiaoDien() {
        JPanel cot = new JPanel();
        cot.setOpaque(false);
        cot.setLayout(new BoxLayout(cot, BoxLayout.Y_AXIS));

        cot.add(taoTheChao());
        cot.add(Box.createVerticalStrut(16));

        JPanel hangThe = PanelUtil.luoiThe(4, 16, theTaiKhoan, theHoatDong, theNhanVien, theVaiTro);
        hangThe.setAlignmentX(Component.LEFT_ALIGNMENT);
        cot.add(hangThe);
        cot.add(Box.createVerticalStrut(16));

        JPanel hangDuoi = new JPanel(new GridLayout(1, 2, 16, 0));
        hangDuoi.setOpaque(false);
        hangDuoi.setAlignmentX(Component.LEFT_ALIGNMENT);
        hangDuoi.add(PanelUtil.the("Tài khoản theo vai trò", null, bieuDo));
        hangDuoi.add(PanelUtil.the("Tài khoản gần đây", null, PanelUtil.bocBang(bangGanDay)));
        cot.add(hangDuoi);

        add(cot, BorderLayout.CENTER);
    }

    private RoundedPanel taoTheChao() {
        RoundedPanel card = new RoundedPanel(new BorderLayout(16, 0), UiTheme.CARD, 14, true);
        card.setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 96));

        lblXinChao.setFont(UiTheme.font(Font.BOLD, 18));
        lblXinChao.setForeground(UiTheme.TEXT);

        JLabel phu = new JLabel("Chào mừng bạn trở lại hệ thống quản trị kho văn phòng phẩm.");
        phu.setFont(UiTheme.FONT_SUBTITLE);
        phu.setForeground(UiTheme.TEXT_MUTED);

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.add(lblXinChao);
        info.add(Box.createVerticalStrut(4));
        info.add(phu);

        RoundedButton btnLamMoi = new RoundedButton("Làm mới", RoundedButton.Kieu.PHU);
        btnLamMoi.addActionListener(e -> {
            taiDuLieu();
            ThongBao.thongTin(this, "Đã cập nhật dữ liệu tổng quan.");
        });
        JPanel phai = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        phai.setOpaque(false);
        phai.add(btnLamMoi);

        card.add(info, BorderLayout.CENTER);
        card.add(phai, BorderLayout.EAST);
        return card;
    }

    private void taiDuLieu() {
        DuLieuMau kho = DuLieuMau.getInstance();

        lblXinChao.setText("Xin chào, " + PhienDangNhap.getInstance().getHoTen() + "!");

        List<TaiKhoan> dsTaiKhoan = kho.getDanhSachTaiKhoan();
        int hoatDong = 0;
        for (TaiKhoan tk : dsTaiKhoan) {
            if (AppConstants.TRANG_THAI_HOAT_DONG.equals(tk.getTrangThai())) {
                hoatDong++;
            }
        }
        theTaiKhoan.setGiaTri(String.valueOf(dsTaiKhoan.size()));
        theHoatDong.setGiaTri(String.valueOf(hoatDong));
        theNhanVien.setGiaTri(String.valueOf(kho.getDanhSachNhanVien().size()));
        theVaiTro.setGiaTri(String.valueOf(kho.getDanhSachVaiTro().size()));

        LinkedHashMap<String, Integer> theoVaiTro = new LinkedHashMap<String, Integer>();
        for (VaiTro vt : kho.getDanhSachVaiTro()) {
            theoVaiTro.put(vt.getTenVT(), kho.demTaiKhoanTheoVaiTro(vt.getMaVT()));
        }
        bieuDo.setDuLieu(theoVaiTro);

        modelGanDay.xoaHet();
        int batDau = Math.max(0, dsTaiKhoan.size() - 6);
        for (int i = dsTaiKhoan.size() - 1; i >= batDau; i--) {
            TaiKhoan tk = dsTaiKhoan.get(i);
            modelGanDay.addRow(new Object[]{
                tk.getMaTK(),
                tk.getTenDangNhap(),
                kho.tenVaiTro(tk.getMaVT()),
                tk.getTrangThai()
            });
        }
        for (int c = 0; c < bangGanDay.getColumnCount(); c++) {
            if ("Trạng thái".equals(bangGanDay.getColumnName(c))) {
                bangGanDay.getColumnModel().getColumn(c).setCellRenderer(new TrangThaiRenderer());
            }
        }
    }
}
