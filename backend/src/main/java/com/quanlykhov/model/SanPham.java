package com.quanlykhov.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

// Đánh dấu lớp này là một thực thể ánh xạ với bảng trong cơ sở dữ liệu
@Entity
// Chỉ định tên bảng trong MySQL là SanPham
@Table(name = "SanPham")
public class SanPham {

    // Đánh dấu MaSP là khóa chính của bảng
    @Id
    @Column(name = "MaSP", length = 20)
    private String maSP;

    @Column(name = "TenSP", length = 100)
    private String tenSP;

    @Column(name = "DonViTinh", length = 20)
    private String donViTinh;

    @Column(name = "SLTonKho")
    private Integer slTonKho;

    @Column(name = "SLToiThieu")
    private Integer slToiThieu;

    @Column(name = "GiaBan", precision = 18, scale = 2)
    private BigDecimal giaBan;

    @Column(name = "TrangThai", length = 50)
    private String trangThai;

    @Column(name = "MaDM", length = 20, nullable = false)
    private String maDM;

    @Column(name = "MaNCC", length = 20)
    private String maNCC;

    @Column(name = "MaKe", length = 20)
    private String maKe;

    // Hàm tạo không tham số (bắt buộc cho JPA)
    public SanPham() {
    }

    // Hàm tạo đầy đủ tham số để khởi tạo đối tượng sản phẩm
    public SanPham(String maSP, String tenSP, String donViTinh, Integer slTonKho,
                   Integer slToiThieu, BigDecimal giaBan, String trangThai,
                   String maDM, String maNCC, String maKe) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.donViTinh = donViTinh;
        this.slTonKho = slTonKho;
        this.slToiThieu = slToiThieu;
        this.giaBan = giaBan;
        this.trangThai = trangThai;
        this.maDM = maDM;
        this.maNCC = maNCC;
        this.maKe = maKe;
    }

    // Lấy mã sản phẩm
    public String getMaSP() {
        return maSP;
    }

    // Gán mã sản phẩm
    public void setMaSP(String maSP) {
        this.maSP = maSP;
    }

    // Lấy tên sản phẩm
    public String getTenSP() {
        return tenSP;
    }

    // Gán tên sản phẩm
    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    // Lấy đơn vị tính
    public String getDonViTinh() {
        return donViTinh;
    }

    // Gán đơn vị tính
    public void setDonViTinh(String donViTinh) {
        this.donViTinh = donViTinh;
    }

    // Lấy số lượng tồn kho
    public Integer getSlTonKho() {
        return slTonKho;
    }

    // Gán số lượng tồn kho
    public void setSlTonKho(Integer slTonKho) {
        this.slTonKho = slTonKho;
    }

    // Lấy số lượng tối thiểu
    public Integer getSlToiThieu() {
        return slToiThieu;
    }

    // Gán số lượng tối thiểu
    public void setSlToiThieu(Integer slToiThieu) {
        this.slToiThieu = slToiThieu;
    }

    // Lấy giá bán
    public BigDecimal getGiaBan() {
        return giaBan;
    }

    // Gán giá bán
    public void setGiaBan(BigDecimal giaBan) {
        this.giaBan = giaBan;
    }

    // Lấy trạng thái kinh doanh
    public String getTrangThai() {
        return trangThai;
    }

    // Gán trạng thái kinh doanh
    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    // Lấy mã danh mục
    public String getMaDM() {
        return maDM;
    }

    // Gán mã danh mục
    public void setMaDM(String maDM) {
        this.maDM = maDM;
    }

    // Lấy mã nhà cung cấp
    public String getMaNCC() {
        return maNCC;
    }

    // Gán mã nhà cung cấp
    public void setMaNCC(String maNCC) {
        this.maNCC = maNCC;
    }

    // Lấy mã kệ hàng
    public String getMaKe() {
        return maKe;
    }

    // Gán mã kệ hàng
    public void setMaKe(String maKe) {
        this.maKe = maKe;
    }
}
