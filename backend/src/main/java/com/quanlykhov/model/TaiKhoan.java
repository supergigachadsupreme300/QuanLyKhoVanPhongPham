package com.quanlykhov.model;

import jakarta.persistence.*;

@Entity
@Table(name = "TaiKhoan")
public class TaiKhoan {

    @Id
    @Column(name = "MaTK", length = 20)
    private String maTK;

    @Column(name = "TenDangNhap", length = 50, nullable = false, unique = true)
    private String tenDangNhap;

    @Column(name = "MatKhau", length = 255, nullable = false)
    private String matKhau;

    @Column(name = "TrangThai", length = 50)
    private String trangThai;

    @Column(name = "MaVT", length = 20, nullable = false)
    private String maVT;

    @Column(name = "MaNV", length = 20, nullable = false, unique = true)
    private String maNV;

    public TaiKhoan() {
    }

    public TaiKhoan(String maTK, String tenDangNhap, String matKhau, String trangThai, String maVT, String maNV) {
        this.maTK = maTK;
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.trangThai = trangThai;
        this.maVT = maVT;
        this.maNV = maNV;
    }

    public String getMaTK() {
        return maTK;
    }

    public void setMaTK(String maTK) {
        this.maTK = maTK;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getMaVT() {
        return maVT;
    }

    public void setMaVT(String maVT) {
        this.maVT = maVT;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }
}