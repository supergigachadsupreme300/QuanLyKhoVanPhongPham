package com.quanlykhov.ui.panel;

import com.quanlykhov.common.constants.AppConstants;
import com.quanlykhov.ui.mock.DuLieuMau;
import com.quanlykhov.ui.model.NhanVien;
import com.quanlykhov.ui.model.TaiKhoan;
import com.quanlykhov.ui.model.VaiTro;
import com.quanlykhov.ui.session.PhienDangNhap;
import com.quanlykhov.ui.component.BangModel;
import com.quanlykhov.ui.component.FormDialog;
import com.quanlykhov.ui.component.PanelUtil;
import com.quanlykhov.ui.component.RoundedButton;
import com.quanlykhov.ui.component.ThongBao;
import com.quanlykhov.ui.component.TrangThaiRenderer;
import com.quanlykhov.ui.component.UiTheme;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Trang quan ly tai khoan nguoi dung: them, sua, xoa, doi mat khau, phan quyen
 * (gan vai tro) va khoa/mo khoa tai khoan.
 */
public class TaiKhoanPanel extends JPanel {

    private static final int COT_TRANG_THAI = 5;

    private final DuLieuMau kho = DuLieuMau.getInstance();

    private final JTextField txtTimKiem = PanelUtil.oTimKiem(220);
    private final JComboBox<String> cbVaiTro = new JComboBox<String>();
    private final JComboBox<String> cbTrangThai = new JComboBox<String>();
    private final JComboBox<String> cbSapXep = new JComboBox<String>();

    private final BangModel model = new BangModel(
            new String[]{"Mã TK", "Tên đăng nhập", "Mật khẩu", "Vai trò", "Nhân viên", "Trạng thái"}, 0);
    private final JTable bang = new JTable(model);
    private final JLabel lblTong = new JLabel();

    public TaiKhoanPanel() {
        setLayout(new BorderLayout());
        setBackground(UiTheme.BG);
        setBorder(BorderFactory.createEmptyBorder(20, 24, 22, 24));
        khoiTaoGiaoDien();
        taiLaiBang();
    }

    private void khoiTaoGiaoDien() {
        cbVaiTro.addItem("Tất cả vai trò");
        for (VaiTro vt : kho.getDanhSachVaiTro()) {
            cbVaiTro.addItem(vt.getMaVT() + " - " + vt.getTenVT());
        }
        cbTrangThai.addItem("Tất cả trạng thái");
        for (String tt : AppConstants.TRANG_THAI_TAI_KHOAN) {
            cbTrangThai.addItem(tt);
        }
        cbSapXep.addItem("Mã TK (tăng dần)");
        cbSapXep.addItem("Tên đăng nhập (A-Z)");
        cbSapXep.addItem("Trạng thái");

        for (JComboBox<?> cb : new JComboBox<?>[]{cbVaiTro, cbTrangThai, cbSapXep}) {
            cb.setFont(UiTheme.FONT_BODY);
            cb.setBackground(java.awt.Color.WHITE);
            cb.setPreferredSize(new Dimension(cb == cbSapXep ? 180 : 170, 38));
        }

        bang.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bang.getColumnModel().getColumn(COT_TRANG_THAI).setCellRenderer(new TrangThaiRenderer());
        bang.getColumnModel().getColumn(0).setPreferredWidth(70);
        bang.getColumnModel().getColumn(3).setPreferredWidth(140);
        bang.getColumnModel().getColumn(4).setPreferredWidth(150);
        bang.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    xuLySua();
                }
            }
        });

        JPanel thanhTren = new JPanel(new BorderLayout(0, 12));
        thanhTren.setOpaque(false);
        thanhTren.add(taoThanhTimKiem(), BorderLayout.NORTH);
        thanhTren.add(taoThanhNut(), BorderLayout.SOUTH);

        JPanel card = new JPanel(new BorderLayout(0, 12));
        card.setBackground(UiTheme.CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiTheme.BORDER),
                BorderFactory.createEmptyBorder(16, 18, 16, 18)));
        card.add(thanhTren, BorderLayout.NORTH);
        card.add(PanelUtil.bocBang(bang), BorderLayout.CENTER);
        card.add(taoChanTrang(), BorderLayout.SOUTH);

        add(card, BorderLayout.CENTER);
    }

    private JPanel taoThanhTimKiem() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        bar.setOpaque(false);

        bar.add(new JLabel("Tìm kiếm:"));
        bar.add(txtTimKiem);
        bar.add(new JLabel("Vai trò:"));
        bar.add(cbVaiTro);
        bar.add(new JLabel("Trạng thái:"));
        bar.add(cbTrangThai);
        bar.add(new JLabel("Sắp xếp:"));
        bar.add(cbSapXep);

        txtTimKiem.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                taiLaiBang();
            }

            public void removeUpdate(DocumentEvent e) {
                taiLaiBang();
            }

            public void changedUpdate(DocumentEvent e) {
                taiLaiBang();
            }
        });
        cbVaiTro.addActionListener(e -> taiLaiBang());
        cbTrangThai.addActionListener(e -> taiLaiBang());
        cbSapXep.addActionListener(e -> taiLaiBang());
        return bar;
    }

    private JPanel taoThanhNut() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        bar.setOpaque(false);

        RoundedButton btnThem = new RoundedButton("+ Thêm tài khoản", RoundedButton.Kieu.CHINH);
        RoundedButton btnSua = new RoundedButton("Sửa", RoundedButton.Kieu.PHU);
        RoundedButton btnXoa = new RoundedButton("Xoá", RoundedButton.Kieu.NGUY_HIEM);
        RoundedButton btnDoiMk = new RoundedButton("Đổi mật khẩu", RoundedButton.Kieu.PHU);
        RoundedButton btnKhoa = new RoundedButton("Khoá / Mở khoá", RoundedButton.Kieu.CANH_BAO);

        btnThem.addActionListener(e -> xuLyThem());
        btnSua.addActionListener(e -> xuLySua());
        btnXoa.addActionListener(e -> xuLyXoa());
        btnDoiMk.addActionListener(e -> xuLyDoiMatKhau());
        btnKhoa.addActionListener(e -> xuLyKhoaMoKhoa());

        bar.add(btnThem);
        bar.add(btnSua);
        bar.add(btnXoa);
        bar.add(btnDoiMk);
        bar.add(btnKhoa);
        return bar;
    }

    private JPanel taoChanTrang() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        lblTong.setFont(UiTheme.FONT_SMALL);
        lblTong.setForeground(UiTheme.TEXT_MUTED);
        p.add(lblTong, BorderLayout.WEST);
        return p;
    }

    // ------------------------------------------------------------------
    // Du lieu
    // ------------------------------------------------------------------

    private void taiLaiBang() {
        String tuKhoa = txtTimKiem.getText();
        String maVTLoc = maVaiTroDangChon();
        String ttLoc = cbTrangThai.getSelectedIndex() <= 0 ? null : (String) cbTrangThai.getSelectedItem();

        List<TaiKhoan> ds = new ArrayList<TaiKhoan>(kho.getDanhSachTaiKhoan());
        ds.sort((a, b) -> sapXep(a, b));

        model.xoaHet();
        int dem = 0;
        for (TaiKhoan tk : ds) {
            if (maVTLoc != null && !maVTLoc.equals(tk.getMaVT())) {
                continue;
            }
            if (ttLoc != null && !ttLoc.equals(tk.getTrangThai())) {
                continue;
            }
            String tenNhanVien = kho.tenNhanVien(tk.getMaNV());
            boolean khop = com.quanlykhov.common.util.StringUtil.chua(tk.getTenDangNhap(), tuKhoa)
                    || com.quanlykhov.common.util.StringUtil.chua(tenNhanVien, tuKhoa);
            if (!khop) {
                continue;
            }
            model.addRow(new Object[]{
                tk.getMaTK(),
                tk.getTenDangNhap(),
                anMatKhau(tk.getMatKhau()),
                kho.tenVaiTro(tk.getMaVT()),
                tenNhanVien,
                tk.getTrangThai()
            });
            dem++;
        }
        lblTong.setText("Hiển thị " + dem + " / " + kho.getDanhSachTaiKhoan().size() + " tài khoản");
    }

    private int sapXep(TaiKhoan a, TaiKhoan b) {
        int kieu = cbSapXep.getSelectedIndex();
        if (kieu == 1) {
            return a.getTenDangNhap().compareToIgnoreCase(b.getTenDangNhap());
        }
        if (kieu == 2) {
            return a.getTrangThai().compareToIgnoreCase(b.getTrangThai());
        }
        return a.getMaTK().compareTo(b.getMaTK());
    }

    private String maVaiTroDangChon() {
        int idx = cbVaiTro.getSelectedIndex();
        if (idx <= 0) {
            return null;
        }
        String item = (String) cbVaiTro.getSelectedItem();
        return item.substring(0, item.indexOf(" - "));
    }

    private String anMatKhau(String mk) {
        if (mk == null || mk.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Math.min(mk.length(), 8); i++) {
            sb.append("•");
        }
        return sb.toString();
    }

    private TaiKhoan taiKhoanDangChon() {
        int row = bang.getSelectedRow();
        if (row < 0) {
            return null;
        }
        String maTK = (String) model.getValueAt(row, 0);
        return kho.timTaiKhoan(maTK);
    }

    // ------------------------------------------------------------------
    // Nghiep vu
    // ------------------------------------------------------------------

    private void xuLyThem() {
        LinkedHashMap<String, String> vaiTroOptions = tuyChonVaiTro();
        LinkedHashMap<String, String> nhanVienOptions = tuyChonNhanVien();
        LinkedHashMap<String, String> trangThaiOptions = FormDialog.options(
                AppConstants.TRANG_THAI_HOAT_DONG, AppConstants.TRANG_THAI_HOAT_DONG,
                AppConstants.TRANG_THAI_KHOA, AppConstants.TRANG_THAI_KHOA);

        Map<String, String> ketQua = FormDialog.hien(this,
                "Thêm tài khoản mới", "Tạo tài khoản đăng nhập và gán vai trò cho người dùng.",
                FormDialog.ds(
                        FormDialog.readonly("maTK", "Mã tài khoản", kho.sinhMaTaiKhoan()),
                        FormDialog.text("tenDangNhap", "Tên đăng nhập", "", true),
                        FormDialog.password("matKhau", "Mật khẩu", "", true),
                        FormDialog.combo("maVT", "Vai trò", vaiTroOptions, "", true),
                        FormDialog.combo("maNV", "Nhân viên", nhanVienOptions, "", false),
                        FormDialog.combo("trangThai", "Trạng thái", trangThaiOptions,
                                AppConstants.TRANG_THAI_HOAT_DONG, true)));

        if (ketQua == null) {
            return;
        }
        TaiKhoan tk = new TaiKhoan(ketQua.get("maTK"), ketQua.get("tenDangNhap"),
                ketQua.get("matKhau"), ketQua.get("trangThai"), ketQua.get("maVT"),
                ketQua.get("maNV"));
        String loi = kho.themTaiKhoan(tk);
        if (loi != null) {
            ThongBao.loi(this, loi);
            return;
        }
        taiLaiBang();
        ThongBao.thongTin(this, "Đã thêm tài khoản \"" + tk.getTenDangNhap() + "\".");
    }

    private void xuLySua() {
        TaiKhoan tk = taiKhoanDangChon();
        if (tk == null) {
            ThongBao.canhBao(this, "Vui lòng chọn một tài khoản để sửa.");
            return;
        }
        LinkedHashMap<String, String> vaiTroOptions = tuyChonVaiTro();
        LinkedHashMap<String, String> nhanVienOptions = tuyChonNhanVien();
        LinkedHashMap<String, String> trangThaiOptions = FormDialog.options(
                AppConstants.TRANG_THAI_HOAT_DONG, AppConstants.TRANG_THAI_HOAT_DONG,
                AppConstants.TRANG_THAI_KHOA, AppConstants.TRANG_THAI_KHOA);

        Map<String, String> ketQua = FormDialog.hien(this,
                "Cập nhật tài khoản", "Chỉnh sửa thông tin và phân quyền cho tài khoản.",
                FormDialog.ds(
                        FormDialog.readonly("maTK", "Mã tài khoản", tk.getMaTK()),
                        FormDialog.text("tenDangNhap", "Tên đăng nhập", tk.getTenDangNhap(), true),
                        FormDialog.password("matKhau", "Mật khẩu", tk.getMatKhau(), true),
                        FormDialog.combo("maVT", "Vai trò", vaiTroOptions, tk.getMaVT(), true),
                        FormDialog.combo("maNV", "Nhân viên", nhanVienOptions, tk.getMaNV(), false),
                        FormDialog.combo("trangThai", "Trạng thái", trangThaiOptions,
                                tk.getTrangThai(), true)));

        if (ketQua == null) {
            return;
        }
        TaiKhoan moi = new TaiKhoan(tk.getMaTK(), ketQua.get("tenDangNhap"),
                ketQua.get("matKhau"), ketQua.get("trangThai"), ketQua.get("maVT"),
                ketQua.get("maNV"));
        String loi = kho.suaTaiKhoan(moi, tk.getTenDangNhap());
        if (loi != null) {
            ThongBao.loi(this, loi);
            return;
        }
        taiLaiBang();
        ThongBao.thongTin(this, "Đã cập nhật tài khoản.");
    }

    private void xuLyXoa() {
        TaiKhoan tk = taiKhoanDangChon();
        if (tk == null) {
            ThongBao.canhBao(this, "Vui lòng chọn một tài khoản để xoá.");
            return;
        }
        if (tk.getTenDangNhap().equals(PhienDangNhap.getInstance().getTenDangNhap())) {
            ThongBao.canhBao(this, "Không thể xoá tài khoản đang đăng nhập.");
            return;
        }
        if (!ThongBao.xacNhan(this, "Bạn có chắc chắn muốn xoá tài khoản \"" + tk.getTenDangNhap() + "\"?")) {
            return;
        }
        String loi = kho.xoaTaiKhoan(tk.getMaTK());
        if (loi != null) {
            ThongBao.loi(this, loi);
            return;
        }
        taiLaiBang();
        ThongBao.thongTin(this, "Đã xoá tài khoản.");
    }

    private void xuLyDoiMatKhau() {
        TaiKhoan tk = taiKhoanDangChon();
        if (tk == null) {
            ThongBao.canhBao(this, "Vui lòng chọn một tài khoản để đổi mật khẩu.");
            return;
        }
        while (true) {
            Map<String, String> ketQua = FormDialog.hien(this,
                    "Đổi mật khẩu", "Đổi mật khẩu cho tài khoản \"" + tk.getTenDangNhap() + "\".",
                    FormDialog.ds(
                            FormDialog.password("mk1", "Mật khẩu mới", "", true),
                            FormDialog.password("mk2", "Nhập lại mật khẩu", "", true)));
            if (ketQua == null) {
                return;
            }
            if (!ketQua.get("mk1").equals(ketQua.get("mk2"))) {
                ThongBao.loi(this, "Mật khẩu nhập lại không khớp. Vui lòng thử lại.");
                continue;
            }
            tk.setMatKhau(ketQua.get("mk1"));
            taiLaiBang();
            ThongBao.thongTin(this, "Đã đổi mật khẩu thành công.");
            return;
        }
    }

    private void xuLyKhoaMoKhoa() {
        TaiKhoan tk = taiKhoanDangChon();
        if (tk == null) {
            ThongBao.canhBao(this, "Vui lòng chọn một tài khoản.");
            return;
        }
        if (tk.getTenDangNhap().equals(PhienDangNhap.getInstance().getTenDangNhap())) {
            ThongBao.canhBao(this, "Không thể tự khoá tài khoản đang đăng nhập.");
            return;
        }
        boolean dangHoatDong = AppConstants.TRANG_THAI_HOAT_DONG.equals(tk.getTrangThai());
        String hanhDong = dangHoatDong ? "khoá" : "mở khoá";
        if (!ThongBao.xacNhan(this, "Bạn có muốn " + hanhDong + " tài khoản \""
                + tk.getTenDangNhap() + "\"?")) {
            return;
        }
        tk.setTrangThai(dangHoatDong ? AppConstants.TRANG_THAI_KHOA : AppConstants.TRANG_THAI_HOAT_DONG);
        taiLaiBang();
        ThongBao.thongTin(this, "Đã " + hanhDong + " tài khoản.");
    }

    // ------------------------------------------------------------------
    // Tuy chon cho hop thoai
    // ------------------------------------------------------------------

    private LinkedHashMap<String, String> tuyChonVaiTro() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        for (VaiTro vt : kho.getDanhSachVaiTro()) {
            map.put(vt.getMaVT(), vt.getMaVT() + " - " + vt.getTenVT());
        }
        return map;
    }

    private LinkedHashMap<String, String> tuyChonNhanVien() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        map.put("", "(Chưa gán nhân viên)");
        for (NhanVien nv : kho.getDanhSachNhanVien()) {
            map.put(nv.getMaNV(), nv.getMaNV() + " - " + nv.getHoTen());
        }
        return map;
    }
}
