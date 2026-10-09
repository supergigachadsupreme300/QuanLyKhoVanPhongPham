package com.quanlykhov.ui.session;

import com.quanlykhov.ui.mock.DuLieuMau;
import com.quanlykhov.ui.model.TaiKhoan;

/**
 * Luu thong tin phien dang nhap hien tai.
 */
public final class PhienDangNhap {

    private static PhienDangNhap instance;

    private TaiKhoan taiKhoan;

    private PhienDangNhap() {
    }

    public static synchronized PhienDangNhap getInstance() {
        if (instance == null) {
            instance = new PhienDangNhap();
        }
        return instance;
    }

    public void dangNhap(TaiKhoan taiKhoan) {
        this.taiKhoan = taiKhoan;
    }

    public void dangXuat() {
        this.taiKhoan = null;
    }

    public TaiKhoan getTaiKhoan() {
        return taiKhoan;
    }

    public boolean daDangNhap() {
        return taiKhoan != null;
    }

    public String getTenDangNhap() {
        return taiKhoan == null ? "" : taiKhoan.getTenDangNhap();
    }

    public String getHoTen() {
        if (taiKhoan == null) {
            return "";
        }
        return DuLieuMau.getInstance().tenNhanVien(taiKhoan.getMaNV());
    }

    public String getTenVaiTro() {
        if (taiKhoan == null) {
            return "";
        }
        return DuLieuMau.getInstance().tenVaiTro(taiKhoan.getMaVT());
    }
}
