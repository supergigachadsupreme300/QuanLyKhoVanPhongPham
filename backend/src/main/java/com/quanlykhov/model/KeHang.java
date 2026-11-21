package com.quanlykhov.model;

import jakarta.persistence.*;

// Lớp KeHang tương ứng với bảng KeHang trong cơ sở dữ liệu
@Entity
@Table(name = "KeHang")
public class KeHang {

    // Khóa chính MaKe của bảng KeHang
    @Id
    @Column(name = "MaKe", length = 20)
    private String maKe;

    // Tên kệ hàng (cột TenKe)
    @Column(name = "TenKe", length = 100)
    private String tenKe;

    // Sức chứa tối đa của kệ hàng (cột SucChuaToiDa)
    @Column(name = "SucChuaToiDa")
    private Integer sucChuaToiDa;

    // Hàm tạo không tham số bắt buộc cho JPA
    public KeHang() {
    }

    // Hàm tạo có tham số để tạo nhanh đối tượng KeHang
    public KeHang(String maKe, String tenKe, Integer sucChuaToiDa) {
        this.maKe = maKe;
        this.tenKe = tenKe;
        this.sucChuaToiDa = sucChuaToiDa;
    }

    // Lấy mã kệ hàng
    public String getMaKe() {
        return maKe;
    }

    // Gán giá trị mã kệ hàng
    public void setMaKe(String maKe) {
        this.maKe = maKe;
    }

    // Lấy tên kệ hàng
    public String getTenKe() {
        return tenKe;
    }

    // Gán giá trị tên kệ hàng
    public void setTenKe(String tenKe) {
        this.tenKe = tenKe;
    }

    // Lấy sức chứa tối đa của kệ
    public Integer getSucChuaToiDa() {
        return sucChuaToiDa;
    }

    // Gán giá trị sức chứa tối đa của kệ
    public void setSucChuaToiDa(Integer sucChuaToiDa) {
        this.sucChuaToiDa = sucChuaToiDa;
    }
}
