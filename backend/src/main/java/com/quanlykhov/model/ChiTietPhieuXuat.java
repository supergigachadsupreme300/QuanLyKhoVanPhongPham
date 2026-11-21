package com.quanlykhov.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

// Đánh dấu lớp này là thực thể ánh xạ với bảng trong cơ sở dữ liệu
@Entity
// Chỉ định tên bảng trong MySQL là ChiTietPhieuXuat
@Table(name = "ChiTietPhieuXuat")
// @IdClass khai báo lớp phụ giữ khóa chính ghép gồm 2 cột (MaPX và MaSP)
@IdClass(ChiTietPhieuXuatId.class)
public class ChiTietPhieuXuat {

    // Phần 1 của khóa chính ghép: mã phiếu xuất
    @Id
    @Column(name = "MaPX", length = 20)
    private String maPX;

    // Phần 2 của khóa chính ghép: mã sản phẩm
    @Id
    @Column(name = "MaSP", length = 20)
    private String maSP;

    // Số lượng sản phẩm xuất kho
    @Column(name = "SLXuat", nullable = false)
    private Integer slXuat;

    // Giá bán tại thời điểm xuất kho
    @Column(name = "GiaBan", precision = 18, scale = 2)
    private BigDecimal giaBan;

    // Hàm tạo không tham số (bắt buộc cho JPA)
    public ChiTietPhieuXuat() {
    }

    // Hàm tạo đầy đủ tham số để khởi tạo một dòng chi tiết phiếu xuất
    public ChiTietPhieuXuat(String maPX, String maSP, Integer slXuat, BigDecimal giaBan) {
        this.maPX = maPX;
        this.maSP = maSP;
        this.slXuat = slXuat;
        this.giaBan = giaBan;
    }

    // Lấy mã phiếu xuất
    public String getMaPX() {
        return maPX;
    }

    // Gán mã phiếu xuất
    public void setMaPX(String maPX) {
        this.maPX = maPX;
    }

    // Lấy mã sản phẩm
    public String getMaSP() {
        return maSP;
    }

    // Gán mã sản phẩm
    public void setMaSP(String maSP) {
        this.maSP = maSP;
    }

    // Lấy số lượng xuất
    public Integer getSlXuat() {
        return slXuat;
    }

    // Gán số lượng xuất
    public void setSlXuat(Integer slXuat) {
        this.slXuat = slXuat;
    }

    // Lấy giá bán lúc xuất
    public BigDecimal getGiaBan() {
        return giaBan;
    }

    // Gán giá bán lúc xuất
    public void setGiaBan(BigDecimal giaBan) {
        this.giaBan = giaBan;
    }
}
