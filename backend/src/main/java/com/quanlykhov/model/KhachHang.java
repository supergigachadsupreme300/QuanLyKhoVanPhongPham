package com.quanlykhov.model;

import jakarta.persistence.*;

// Lớp KhachHang tương ứng với bảng KhachHang trong cơ sở dữ liệu
@Entity
@Table(name = "KhachHang")
public class KhachHang {

    // Khóa chính MaKH của bảng KhachHang
    @Id
    @Column(name = "MaKH", length = 20)
    private String maKH;

    // Tên khách hàng (cột TenKH)
    @Column(name = "TenKH", length = 100)
    private String tenKH;

    // Số điện thoại khách hàng (cột Sdt)
    @Column(name = "Sdt", length = 15)
    private String sdt;

    // Địa chỉ khách hàng (cột DiaChi)
    @Column(name = "DiaChi", length = 255)
    private String diaChi;

    // Hàm tạo không tham số bắt buộc cho JPA
    public KhachHang() {
    }

    // Hàm tạo có tham số để tạo nhanh đối tượng KhachHang
    public KhachHang(String maKH, String tenKH, String sdt, String diaChi) {
        this.maKH = maKH;
        this.tenKH = tenKH;
        this.sdt = sdt;
        this.diaChi = diaChi;
    }

    // Lấy mã khách hàng
    public String getMaKH() {
        return maKH;
    }

    // Gán giá trị mã khách hàng
    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    // Lấy tên khách hàng
    public String getTenKH() {
        return tenKH;
    }

    // Gán giá trị tên khách hàng
    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }

    // Lấy số điện thoại khách hàng
    public String getSdt() {
        return sdt;
    }

    // Gán giá trị số điện thoại khách hàng
    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    // Lấy địa chỉ khách hàng
    public String getDiaChi() {
        return diaChi;
    }

    // Gán giá trị địa chỉ khách hàng
    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }
}
