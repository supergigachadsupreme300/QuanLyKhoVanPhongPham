package com.quanlykhov.ui.mock;

import com.quanlykhov.common.constants.AppConstants;
import com.quanlykhov.ui.model.NhanVien;
import com.quanlykhov.ui.model.TaiKhoan;
import com.quanlykhov.ui.model.VaiTro;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class DuLieuMau {
    private static final DuLieuMau INSTANCE = new DuLieuMau();
    private final List<VaiTro> danhSachVaiTro = new ArrayList<VaiTro>();
    private final List<NhanVien> danhSachNhanVien = new ArrayList<NhanVien>();
    private final List<TaiKhoan> danhSachTaiKhoan = new ArrayList<TaiKhoan>();
    private final Map<String, String> quyenTheoVaiTro = new LinkedHashMap<String, String>();

    private DuLieuMau() {
        khoiTao();
    }

    public static DuLieuMau getInstance() {
        return INSTANCE;
    }

    private void khoiTao() {
        danhSachVaiTro.add(new VaiTro("VT01", "Quản trị viên", "Toàn quyền quản lý hệ thống"));
        danhSachVaiTro.add(new VaiTro("VT02", "Nhân viên kho", "Quản lý kho, nhập - xuất hàng"));
        danhSachVaiTro.add(new VaiTro("VT03", "Nhân viên bán hàng", "Xử lý đơn hàng, khách hàng"));

        danhSachNhanVien.add(new NhanVien("NV01", "Nguyễn Văn Admin", "0909123456", "VT01"));
        danhSachNhanVien.add(new NhanVien("NV02", "Trần Thị Hương", "0909234567", "VT02"));
        danhSachNhanVien.add(new NhanVien("NV03", "Lê Minh Tuấn", "0909345678", "VT03"));

        danhSachTaiKhoan.add(new TaiKhoan("TK01", "admin", "admin123", "NV01", "VT01", AppConstants.TRANG_THAI_HOAT_DONG));
        danhSachTaiKhoan.add(new TaiKhoan("TK02", "kho1", "123456", "NV02", "VT02", AppConstants.TRANG_THAI_HOAT_DONG));
        danhSachTaiKhoan.add(new TaiKhoan("TK03", "banhang1", "123456", "NV03", "VT03", AppConstants.TRANG_THAI_HOAT_DONG));

        quyenTheoVaiTro.put("VT01", String.join(",", Arrays.asList("tongquan", "taikhoan", "nhanvien", "vaitro")));
        quyenTheoVaiTro.put("VT02", String.join(",", Arrays.asList("tongquan")));
        quyenTheoVaiTro.put("VT03", String.join(",", Arrays.asList("tongquan")));
    }

    public List<VaiTro> getDanhSachVaiTro() { return danhSachVaiTro; }
    public List<NhanVien> getDanhSachNhanVien() { return danhSachNhanVien; }
    public List<TaiKhoan> getDanhSachTaiKhoan() { return danhSachTaiKhoan; }

    public String tenNhanVien(String maNV) {
        if (maNV == null) return "";
        for (NhanVien nv : danhSachNhanVien) if (maNV.equals(nv.getMaNV())) return nv.getHoTen();
        return maNV;
    }
    public String tenVaiTro(String maVT) {
        if (maVT == null) return "";
        for (VaiTro vt : danhSachVaiTro) if (maVT.equals(vt.getMaVT())) return vt.getTenVT();
        return maVT;
    }
    public TaiKhoan dangNhap(String tenDangNhap, String matKhau) {
        if (tenDangNhap == null || matKhau == null) return null;
        for (TaiKhoan tk : danhSachTaiKhoan) if (tenDangNhap.equals(tk.getTenDangNhap()) && matKhau.equals(tk.getMatKhau())) return tk;
        return null;
    }
    public String maTaiKhoanMoi() { return sinhMaMoi("TK", danhSachTaiKhoan, tk -> tk.getMaTK()); }
    public String maNhanVienMoi() { return sinhMaMoi("NV", danhSachNhanVien, nv -> nv.getMaNV()); }
    public String maVaiTroMoi() { return sinhMaMoi("VT", danhSachVaiTro, vt -> vt.getMaVT()); }
    private <T> String sinhMaMoi(String prefix, List<T> list, java.util.function.Function<T,String> getMa) {
        int max = 0;
        for (T o : list) {
            String m = getMa.apply(o);
            if (m != null && m.startsWith(prefix)) {
                try { int v = Integer.parseInt(m.substring(prefix.length())); if (v > max) max = v; } catch (Exception ignored) {}
            }
        }
        return String.format("%s%02d", prefix, max + 1);
    }
    public String themTaiKhoan(TaiKhoan tk) {
        if (tk == null || tk.getTenDangNhap() == null || tk.getTenDangNhap().trim().isEmpty()) return "Vui lòng nhập tên đăng nhập.";
        for (TaiKhoan t : danhSachTaiKhoan) if (t.getTenDangNhap().equalsIgnoreCase(tk.getTenDangNhap())) return "Tên đăng nhập đã tồn tại.";
        danhSachTaiKhoan.add(tk); return null;
    }
    public String suaTaiKhoan(TaiKhoan tk) {
        if (tk == null) return "Dữ liệu không hợp lệ.";
        for (TaiKhoan t : danhSachTaiKhoan) {
            if (t.getMaTK().equals(tk.getMaTK())) {
                t.setTenDangNhap(tk.getTenDangNhap());
                t.setMatKhau(tk.getMatKhau());
                t.setMaNV(tk.getMaNV());
                t.setMaVT(tk.getMaVT());
                t.setTrangThai(tk.getTrangThai());
                return null;
            }
        }
        return "Không tìm thấy tài khoản.";
    }
    public String suaTaiKhoan(TaiKhoan tk, String matKhauCu) { return suaTaiKhoan(tk); }
    public String xoaTaiKhoan(String maTK) {
        for (int i = 0; i < danhSachTaiKhoan.size(); i++) {
            if (danhSachTaiKhoan.get(i).getMaTK().equals(maTK)) { danhSachTaiKhoan.remove(i); return null; }
        }
        return "Không tìm thấy tài khoản.";
    }
    public NhanVien timNhanVien(String maNV) {
        if (maNV == null) return null;
        for (NhanVien nv : danhSachNhanVien) if (maNV.equals(nv.getMaNV())) return nv;
        return null;
    }
    public String themNhanVien(NhanVien nv) { if (nv == null) return "Dữ liệu không hợp lệ."; danhSachNhanVien.add(nv); return null; }
    public String suaNhanVien(NhanVien nv) {
        if (nv == null) return "Dữ liệu không hợp lệ.";
        for (NhanVien n : danhSachNhanVien) {
            if (n.getMaNV().equals(nv.getMaNV())) { n.setHoTen(nv.getHoTen()); n.setSdt(nv.getSdt()); return null; }
        }
        return "Không tìm thấy nhân viên.";
    }
    public String xoaNhanVien(String maNV) {
        for (int i = 0; i < danhSachNhanVien.size(); i++) {
            if (danhSachNhanVien.get(i).getMaNV().equals(maNV)) { danhSachNhanVien.remove(i); return null; }
        }
        return "Không tìm thấy nhân viên.";
    }
    public VaiTro timVaiTro(String maVT) {
        if (maVT == null) return null;
        for (VaiTro vt : danhSachVaiTro) if (maVT.equals(vt.getMaVT())) return vt;
        return null;
    }
    public String themVaiTro(VaiTro vt) { if (vt == null) return "Dữ liệu không hợp lệ."; danhSachVaiTro.add(vt); return null; }
    public String suaVaiTro(VaiTro vt) {
        if (vt == null) return "Dữ liệu không hợp lệ.";
        for (VaiTro v : danhSachVaiTro) {
            if (v.getMaVT().equals(vt.getMaVT())) { v.setTenVT(vt.getTenVT()); v.setMoTa(vt.getMoTa()); return null; }
        }
        return "Không tìm thấy vai trò.";
    }
    public String xoaVaiTro(String maVT) {
        for (int i = 0; i < danhSachVaiTro.size(); i++) {
            if (danhSachVaiTro.get(i).getMaVT().equals(maVT)) { danhSachVaiTro.remove(i); return null; }
        }
        return "Không tìm thấy vai trò.";
    }
    public int demTaiKhoanTheoVaiTro(String maVT) {
        int c = 0;
        for (TaiKhoan tk : danhSachTaiKhoan) if (maVT != null && maVT.equals(tk.getMaVT())) c++;
        return c;
    }
    public Set<String> layQuyen(String maVT) {
        Set<String> set = new HashSet<String>();
        String s = quyenTheoVaiTro.get(maVT);
        if (s != null && !s.isEmpty()) {
            for (String p : s.split(",")) { String t = p.trim(); if (!t.isEmpty()) set.add(t); }
        }
        return set;
    }
    public void luuQuyen(String maVT, Set<String> quyen) {
        if (maVT == null) return;
        if (quyen == null || quyen.isEmpty()) { quyenTheoVaiTro.put(maVT, ""); return; }
        StringBuilder sb = new StringBuilder();
        for (String p : quyen) {
            if (p != null && !p.trim().isEmpty()) { if (sb.length() > 0) sb.append(","); sb.append(p.trim()); }
        }
        quyenTheoVaiTro.put(maVT, sb.toString());
    }
    public TaiKhoan timTaiKhoan(String maTK) {
        if (maTK == null) return null;
        for (TaiKhoan tk : danhSachTaiKhoan) if (maTK.equals(tk.getMaTK())) return tk;
        return null;
    }
    public String sinhMaTaiKhoan() { return maTaiKhoanMoi(); }
    public String sinhMaNhanVien() { return maNhanVienMoi(); }
    public String sinhMaVaiTro() { return maVaiTroMoi(); }
}