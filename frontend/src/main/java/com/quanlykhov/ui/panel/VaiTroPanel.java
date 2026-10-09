package com.quanlykhov.ui.panel;

import com.quanlykhov.common.constants.AppConstants;
import com.quanlykhov.ui.mock.DuLieuMau;
import com.quanlykhov.ui.model.VaiTro;
import com.quanlykhov.ui.component.BangModel;
import com.quanlykhov.ui.component.FormDialog;
import com.quanlykhov.ui.component.PanelUtil;
import com.quanlykhov.ui.component.RoundedButton;
import com.quanlykhov.ui.component.ThongBao;
import com.quanlykhov.ui.component.UiTheme;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;

/**
 * Trang quan ly vai tro va phan quyen chuc nang theo vai tro.
 */
public class VaiTroPanel extends JPanel {

    private final DuLieuMau kho = DuLieuMau.getInstance();

    private final BangModel model = new BangModel(
            new String[]{"Mã VT", "Tên vai trò", "Mô tả", "Số tài khoản"}, 0);
    private final JTable bang = new JTable(model);

    private final Map<String, JCheckBox> oQuyen = new LinkedHashMap<String, JCheckBox>();
    private final JLabel lblVaiTroDangChon = new JLabel("Chưa chọn vai trò");
    private final RoundedButton btnLuuQuyen = new RoundedButton("Lưu phân quyền", RoundedButton.Kieu.THANH_CONG);

    public VaiTroPanel() {
        setLayout(new BorderLayout(0, 16));
        setBackground(UiTheme.BG);
        setBorder(BorderFactory.createEmptyBorder(20, 24, 22, 24));
        khoiTaoGiaoDien();
        taiLaiBang();
    }

    private void khoiTaoGiaoDien() {
        bang.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bang.getColumnModel().getColumn(0).setPreferredWidth(70);
        bang.getColumnModel().getColumn(1).setPreferredWidth(150);
        bang.getColumnModel().getColumn(2).setPreferredWidth(420);
        bang.getColumnModel().getColumn(3).setPreferredWidth(90);
        bang.getSelectionModel().addListSelectionListener(this::khiChonDong);
        bang.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    xuLySua();
                }
            }
        });

        JPanel cardBang = new JPanel(new BorderLayout(0, 12));
        cardBang.setBackground(UiTheme.CARD);
        cardBang.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiTheme.BORDER),
                BorderFactory.createEmptyBorder(16, 18, 16, 18)));
        cardBang.add(taoThanhNut(), BorderLayout.NORTH);
        cardBang.add(PanelUtil.bocBang(bang), BorderLayout.CENTER);

        add(cardBang, BorderLayout.CENTER);
        add(taoThePhanQuyen(), BorderLayout.SOUTH);
    }

    private JPanel taoThanhNut() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        bar.setOpaque(false);
        RoundedButton btnThem = new RoundedButton("+ Thêm vai trò", RoundedButton.Kieu.CHINH);
        RoundedButton btnSua = new RoundedButton("Sửa", RoundedButton.Kieu.PHU);
        RoundedButton btnXoa = new RoundedButton("Xoá", RoundedButton.Kieu.NGUY_HIEM);
        btnThem.addActionListener(e -> xuLyThem());
        btnSua.addActionListener(e -> xuLySua());
        btnXoa.addActionListener(e -> xuLyXoa());
        bar.add(btnThem);
        bar.add(btnSua);
        bar.add(btnXoa);
        return bar;
    }

    private JPanel taoThePhanQuyen() {
        JPanel card = new JPanel(new BorderLayout(0, 10));
        card.setBackground(UiTheme.CARD);
        card.setPreferredSize(new Dimension(0, 236));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiTheme.BORDER),
                BorderFactory.createEmptyBorder(14, 18, 14, 18)));

        JPanel head = new JPanel(new BorderLayout());
        head.setOpaque(false);
        JLabel tieuDe = new JLabel("Phân quyền chức năng theo vai trò");
        tieuDe.setFont(UiTheme.FONT_HEADING);
        tieuDe.setForeground(UiTheme.TEXT);
        lblVaiTroDangChon.setFont(UiTheme.FONT_BOLD);
        lblVaiTroDangChon.setForeground(UiTheme.PRIMARY);
        head.add(tieuDe, BorderLayout.WEST);
        head.add(lblVaiTroDangChon, BorderLayout.EAST);

        JPanel luoiQuyen = new JPanel(new GridLayout(0, 3, 8, 8));
        luoiQuyen.setOpaque(false);
        for (String module : AppConstants.MODULE_PHAN_QUYEN) {
            JCheckBox cb = new JCheckBox(module);
            cb.setFont(UiTheme.FONT_BODY);
            cb.setOpaque(false);
            cb.setForeground(UiTheme.TEXT);
            cb.setEnabled(false);
            oQuyen.put(module, cb);
            luoiQuyen.add(cb);
        }

        JPanel giua = new JPanel(new BorderLayout());
        giua.setOpaque(false);
        giua.add(luoiQuyen, BorderLayout.NORTH);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        footer.setOpaque(false);
        JLabel ghiChu = new JLabel("Ghi chú: đây là ma trận phân quyền demo ở tầng giao diện.");
        ghiChu.setFont(UiTheme.FONT_SMALL);
        ghiChu.setForeground(UiTheme.TEXT_MUTED);
        btnLuuQuyen.setEnabled(false);
        btnLuuQuyen.addActionListener(e -> xuLyLuuQuyen());
        footer.add(ghiChu);
        footer.add(btnLuuQuyen);

        card.add(head, BorderLayout.NORTH);
        card.add(giua, BorderLayout.CENTER);
        card.add(footer, BorderLayout.SOUTH);
        return card;
    }

    private void taiLaiBang() {
        String maDangChon = vaiTroDangChon() == null ? null : vaiTroDangChon().getMaVT();
        model.xoaHet();
        for (VaiTro vt : kho.getDanhSachVaiTro()) {
            model.addRow(new Object[]{
                vt.getMaVT(), vt.getTenVT(), vt.getMoTa(), kho.demTaiKhoanTheoVaiTro(vt.getMaVT())
            });
        }
        if (maDangChon != null) {
            for (int i = 0; i < model.getRowCount(); i++) {
                if (model.getValueAt(i, 0).equals(maDangChon)) {
                    bang.setRowSelectionInterval(i, i);
                    break;
                }
            }
        }
    }

    private void khiChonDong(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            return;
        }
        VaiTro vt = vaiTroDangChon();
        boolean co = vt != null;
        btnLuuQuyen.setEnabled(co);
        for (JCheckBox cb : oQuyen.values()) {
            cb.setEnabled(co);
        }
        if (!co) {
            lblVaiTroDangChon.setText("Chưa chọn vai trò");
            for (JCheckBox cb : oQuyen.values()) {
                cb.setSelected(false);
            }
            return;
        }
        lblVaiTroDangChon.setText(vt.getMaVT() + " - " + vt.getTenVT());
        Set<String> quyen = kho.layQuyen(vt.getMaVT());
        for (Map.Entry<String, JCheckBox> entry : oQuyen.entrySet()) {
            entry.getValue().setSelected(quyen.contains(entry.getKey()));
        }
    }

    private VaiTro vaiTroDangChon() {
        int row = bang.getSelectedRow();
        if (row < 0) {
            return null;
        }
        return kho.timVaiTro((String) model.getValueAt(row, 0));
    }

    private void xuLyThem() {
        Map<String, String> ketQua = FormDialog.hien(this,
                "Thêm vai trò", "Tạo vai trò mới cho hệ thống.",
                FormDialog.ds(
                        FormDialog.readonly("maVT", "Mã vai trò", kho.sinhMaVaiTro()),
                        FormDialog.text("tenVT", "Tên vai trò", "", true),
                        FormDialog.area("moTa", "Mô tả", "")));
        if (ketQua == null) {
            return;
        }
        VaiTro vt = new VaiTro(ketQua.get("maVT"), ketQua.get("tenVT"), ketQua.get("moTa"));
        String loi = kho.themVaiTro(vt);
        if (loi != null) {
            ThongBao.loi(this, loi);
            return;
        }
        taiLaiBang();
        ThongBao.thongTin(this, "Đã thêm vai trò \"" + vt.getTenVT() + "\".");
    }

    private void xuLySua() {
        VaiTro vt = vaiTroDangChon();
        if (vt == null) {
            ThongBao.canhBao(this, "Vui lòng chọn một vai trò để sửa.");
            return;
        }
        Map<String, String> ketQua = FormDialog.hien(this,
                "Cập nhật vai trò", "Chỉnh sửa thông tin vai trò.",
                FormDialog.ds(
                        FormDialog.readonly("maVT", "Mã vai trò", vt.getMaVT()),
                        FormDialog.text("tenVT", "Tên vai trò", vt.getTenVT(), true),
                        FormDialog.area("moTa", "Mô tả", vt.getMoTa())));
        if (ketQua == null) {
            return;
        }
        VaiTro moi = new VaiTro(vt.getMaVT(), ketQua.get("tenVT"), ketQua.get("moTa"));
        String loi = kho.suaVaiTro(moi);
        if (loi != null) {
            ThongBao.loi(this, loi);
            return;
        }
        taiLaiBang();
        ThongBao.thongTin(this, "Đã cập nhật vai trò.");
    }

    private void xuLyXoa() {
        VaiTro vt = vaiTroDangChon();
        if (vt == null) {
            ThongBao.canhBao(this, "Vui lòng chọn một vai trò để xoá.");
            return;
        }
        if (!ThongBao.xacNhan(this, "Bạn có chắc chắn muốn xoá vai trò \"" + vt.getTenVT() + "\"?")) {
            return;
        }
        String loi = kho.xoaVaiTro(vt.getMaVT());
        if (loi != null) {
            ThongBao.loi(this, loi);
            return;
        }
        taiLaiBang();
        ThongBao.thongTin(this, "Đã xoá vai trò.");
    }

    private void xuLyLuuQuyen() {
        VaiTro vt = vaiTroDangChon();
        if (vt == null) {
            return;
        }
        Set<String> quyen = new LinkedHashSet<String>();
        for (Map.Entry<String, JCheckBox> entry : oQuyen.entrySet()) {
            if (entry.getValue().isSelected()) {
                quyen.add(entry.getKey());
            }
        }
        kho.luuQuyen(vt.getMaVT(), quyen);
        ThongBao.thongTin(this, "Đã lưu phân quyền cho vai trò \"" + vt.getTenVT() + "\".");
    }
}
