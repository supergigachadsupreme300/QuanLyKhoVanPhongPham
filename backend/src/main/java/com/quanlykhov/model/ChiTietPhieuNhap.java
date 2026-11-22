package com.quanlykhov.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

// Đánh dấu lớp này là một thực thể ánh xạ với bảng trong cơ sở dữ liệu
@Entity
// Chỉ định tên bảng trong MySQL là ChiTietPhieuNhap
@Table(name = "ChiTietPhieuNhap")
// Chỉ định lớp ChiTietPhieuNhapId dùng để đại diện cho khóa chính gồm 2 cột (MaPN và MaSP)
@IdClass(ChiTietPhieuNhapId.class)
public class ChiTietPhieuNhap {

    // Đánh dấu MaPN là cột thứ nhất trong khóa chính
    @Id
    @Column(name = "MaPN", length = 20, nullable = false)
    private String maPN;

    // Đánh dấu MaSP là cột thứ hai trong khóa chính
    @Id
    @Column(name = "MaSP", length = 20, nullable = false)
    private String maSP;

    @Column(name = "SLTheoChungTu")
    private Integer slTheoChungTu;

    @Column(name = "SLThucNhap")
    private Integer slThucNhap;

    @Column(name = "GiaNhap", precision = 18, scale = 2)
    private BigDecimal giaNhap;

    // Hàm tạo không tham số (bắt buộc cho JPA)
    public ChiTietPhieuNhap() {
    }

    // Hàm tạo đầy đủ tham số để khởi tạo đối tượng chi tiết phiếu nhập
    public ChiTietPhieuNhap(String maPN, String maSP, Integer slTheoChungTu,
                            Integer slThucNhap, BigDecimal giaNhap) {
        this.maPN = maPN;
        this.maSP = maSP;
        this.slTheoChungTu = slTheoChungTu;
        this.slThucNhap = slThucNhap;
        this.giaNhap = giaNhap;
    }

    // Lấy mã phiếu nhập
    public String getMaPN() {
        return maPN;
    }

    // Gán mã phiếu nhập
    public void setMaPN(String maPN) {
        this.maPN = maPN;
    }

    // Lấy mã sản phẩm
    public String getMaSP() {
        return maSP;
    }

    // Gán mã sản phẩm
    public void setMaSP(String maSP) {
        this.maSP = maSP;
    }

    // Lấy số lượng ghi trên chứng từ của nhà cung cấp
    public Integer getSlTheoChungTu() {
        return slTheoChungTu;
    }

    // Gán số lượng ghi trên chứng từ của nhà cung cấp
    public void setSlTheoChungTu(Integer slTheoChungTu) {
        this.slTheoChungTu = slTheoChungTu;
    }

    // Lấy số lượng đếm thực tế khi nhập kho
    public Integer getSlThucNhap() {
        return slThucNhap;
    }

    // Gán số lượng đếm thực tế khi nhập kho
    public void setSlThucNhap(Integer slThucNhap) {
        this.slThucNhap = slThucNhap;
    }

    // Lấy đơn giá nhập
    public BigDecimal getGiaNhap() {
        return giaNhap;
    }

    // Gán đơn giá nhập
    public void setGiaNhap(BigDecimal giaNhap) {
        this.giaNhap = giaNhap;
    }
}
