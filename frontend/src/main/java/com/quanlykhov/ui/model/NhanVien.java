package com.quanlykhov.ui.model;

/**
 * Anh xa bang NhanVien (MaNV, HoTen, Sdt, MaVT).
 */
public class NhanVien {

    private String maNV;
    private String hoTen;
    private String sdt;
    private String maVT;

    public NhanVien() {
    }

    public NhanVien(String maNV, String hoTen, String sdt, String maVT) {
        this.maNV = maNV;
        this.hoTen = hoTen;
        this.sdt = sdt;
        this.maVT = maVT;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getMaVT() {
        return maVT;
    }

    public void setMaVT(String maVT) {
        this.maVT = maVT;
    }
}
