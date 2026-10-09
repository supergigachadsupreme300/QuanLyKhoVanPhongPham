package com.quanlykhov.ui.panel;

import com.quanlykhov.common.util.StringUtil;
import com.quanlykhov.ui.mock.DuLieuMau;
import com.quanlykhov.ui.model.NhanVien;
import com.quanlykhov.ui.model.VaiTro;
import com.quanlykhov.ui.component.BangModel;
import com.quanlykhov.ui.component.FormDialog;
import com.quanlykhov.ui.component.PanelUtil;
import com.quanlykhov.ui.component.RoundedButton;
import com.quanlykhov.ui.component.ThongBao;
import com.quanlykhov.ui.component.UiTheme;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Trang quan ly ho so nhan vien.
 */
public class NhanVienPanel extends JPanel {

    private final DuLieuMau kho = DuLieuMau.getInstance();

    private final JTextField txtTimKiem = PanelUtil.oTimKiem(220);
    private final JComboBox<String> cbVaiTro = new JComboBox<String>();
    private final JComboBox<String> cbSapXep = new JComboBox<String>();

    private final BangModel model = new BangModel(
            new String[]{"Mã NV", "Họ tên", "Số điện thoại", "Vai trò"}, 0);
    private final JTable bang = new JTable(model);
    private final JLabel lblTong = new JLabel();

    public NhanVienPanel() {
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
        cbSapXep.addItem("Mã NV (tăng dần)");
        cbSapXep.addItem("Họ tên (A-Z)");
        cbSapXep.addItem("Vai trò");

        for (JComboBox<?> cb : new JComboBox<?>[]{cbVaiTro, cbSapXep}) {
            cb.setFont(UiTheme.FONT_BODY);
            cb.setBackground(java.awt.Color.WHITE);
            cb.setPreferredSize(new Dimension(180, 38));
        }

        bang.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bang.getColumnModel().getColumn(0).setPreferredWidth(80);
        bang.getColumnModel().getColumn(1).setPreferredWidth(200);
        bang.getColumnModel().getColumn(3).setPreferredWidth(160);
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
        cbSapXep.addActionListener(e -> taiLaiBang());
        return bar;
    }

    private JPanel taoThanhNut() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        bar.setOpaque(false);
        RoundedButton btnThem = new RoundedButton("+ Thêm nhân viên", RoundedButton.Kieu.CHINH);
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

    private JPanel taoChanTrang() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        lblTong.setFont(UiTheme.FONT_SMALL);
        lblTong.setForeground(UiTheme.TEXT_MUTED);
        p.add(lblTong, BorderLayout.WEST);
        return p;
    }

    private void taiLaiBang() {
        String tuKhoa = txtTimKiem.getText();
        String maVTLoc = maVaiTroDangChon();
        List<NhanVien> ds = new ArrayList<NhanVien>(kho.getDanhSachNhanVien());
        int kieu = cbSapXep.getSelectedIndex();
        ds.sort((a, b) -> {
            if (kieu == 1) {
                return a.getHoTen().compareToIgnoreCase(b.getHoTen());
            }
            if (kieu == 2) {
                return kho.tenVaiTro(a.getMaVT()).compareToIgnoreCase(kho.tenVaiTro(b.getMaVT()));
            }
            return a.getMaNV().compareTo(b.getMaNV());
        });

        model.xoaHet();
        int dem = 0;
        for (NhanVien nv : ds) {
            if (maVTLoc != null && !maVTLoc.equals(nv.getMaVT())) {
                continue;
            }
            boolean khop = StringUtil.chua(nv.getHoTen(), tuKhoa)
                    || StringUtil.chua(nv.getSdt(), tuKhoa);
            if (!khop) {
                continue;
            }
            model.addRow(new Object[]{
                nv.getMaNV(), nv.getHoTen(), nv.getSdt(), kho.tenVaiTro(nv.getMaVT())
            });
            dem++;
        }
        lblTong.setText("Hiển thị " + dem + " / " + kho.getDanhSachNhanVien().size() + " nhân viên");
    }

    private String maVaiTroDangChon() {
        int idx = cbVaiTro.getSelectedIndex();
        if (idx <= 0) {
            return null;
        }
        String item = (String) cbVaiTro.getSelectedItem();
        return item.substring(0, item.indexOf(" - "));
    }

    private NhanVien nhanVienDangChon() {
        int row = bang.getSelectedRow();
        if (row < 0) {
            return null;
        }
        return kho.timNhanVien((String) model.getValueAt(row, 0));
    }

    private void xuLyThem() {
        Map<String, String> ketQua = FormDialog.hien(this,
                "Thêm nhân viên", "Nhập thông tin hồ sơ nhân viên mới.",
                FormDialog.ds(
                        FormDialog.readonly("maNV", "Mã nhân viên", kho.sinhMaNhanVien()),
                        FormDialog.text("hoTen", "Họ tên", "", true),
                        FormDialog.text("sdt", "Số điện thoại", "", true).sdt(),
                        FormDialog.combo("maVT", "Vai trò", tuyChonVaiTro(), "", true)));
        if (ketQua == null) {
            return;
        }
        NhanVien nv = new NhanVien(ketQua.get("maNV"), ketQua.get("hoTen"),
                ketQua.get("sdt"), ketQua.get("maVT"));
        String loi = kho.themNhanVien(nv);
        if (loi != null) {
            ThongBao.loi(this, loi);
            return;
        }
        taiLaiBang();
        ThongBao.thongTin(this, "Đã thêm nhân viên \"" + nv.getHoTen() + "\".");
    }

    private void xuLySua() {
        NhanVien nv = nhanVienDangChon();
        if (nv == null) {
            ThongBao.canhBao(this, "Vui lòng chọn một nhân viên để sửa.");
            return;
        }
        Map<String, String> ketQua = FormDialog.hien(this,
                "Cập nhật nhân viên", "Chỉnh sửa hồ sơ nhân viên.",
                FormDialog.ds(
                        FormDialog.readonly("maNV", "Mã nhân viên", nv.getMaNV()),
                        FormDialog.text("hoTen", "Họ tên", nv.getHoTen(), true),
                        FormDialog.text("sdt", "Số điện thoại", nv.getSdt(), true).sdt(),
                        FormDialog.combo("maVT", "Vai trò", tuyChonVaiTro(), nv.getMaVT(), true)));
        if (ketQua == null) {
            return;
        }
        NhanVien moi = new NhanVien(nv.getMaNV(), ketQua.get("hoTen"),
                ketQua.get("sdt"), ketQua.get("maVT"));
        String loi = kho.suaNhanVien(moi);
        if (loi != null) {
            ThongBao.loi(this, loi);
            return;
        }
        taiLaiBang();
        ThongBao.thongTin(this, "Đã cập nhật nhân viên.");
    }

    private void xuLyXoa() {
        NhanVien nv = nhanVienDangChon();
        if (nv == null) {
            ThongBao.canhBao(this, "Vui lòng chọn một nhân viên để xoá.");
            return;
        }
        if (!ThongBao.xacNhan(this, "Bạn có chắc chắn muốn xoá nhân viên \"" + nv.getHoTen() + "\"?")) {
            return;
        }
        String loi = kho.xoaNhanVien(nv.getMaNV());
        if (loi != null) {
            ThongBao.loi(this, loi);
            return;
        }
        taiLaiBang();
        ThongBao.thongTin(this, "Đã xoá nhân viên.");
    }

    private LinkedHashMap<String, String> tuyChonVaiTro() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        for (VaiTro vt : kho.getDanhSachVaiTro()) {
            map.put(vt.getMaVT(), vt.getMaVT() + " - " + vt.getTenVT());
        }
        return map;
    }
}
