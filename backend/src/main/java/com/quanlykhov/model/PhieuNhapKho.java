package com.quanlykhov.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// Đánh dấu lớp này là một thực thể ánh xạ với bảng trong cơ sở dữ liệu
@Entity
// Chỉ định tên bảng trong MySQL là PhieuNhapKho
@Table(name = "PhieuNhapKho")
public class PhieuNhapKho {

    // Đánh dấu MaPN là khóa chính của bảng
    @Id
    @Column(name = "MaPN", length = 20)
    private String maPN;

    @Column(name = "NgayNhap")
    private LocalDateTime ngayNhap;

    @Column(name = "TrangThai", length = 50)
    private String trangThai;

    @Column(name = "GhiChu", length = 255)
    private String ghiChu;

    @Column(name = "MaNV", length = 20, nullable = false)
    private String maNV;

    @Column(name = "MaNCC", length = 20, nullable = false)
    private String maNCC;

    // Hàm tạo không tham số (bắt buộc cho JPA)
    public PhieuNhapKho() {
    }

    // Hàm tạo đầy đủ tham số để khởi tạo đối tượng phiếu nhập kho
    public PhieuNhapKho(String maPN, LocalDateTime ngayNhap, String trangThai,
                        String ghiChu, String maNV, String maNCC) {
        this.maPN = maPN;
        this.ngayNhap = ngayNhap;
        this.trangThai = trangThai;
        this.ghiChu = ghiChu;
        this.maNV = maNV;
        this.maNCC = maNCC;
    }

    // Lấy mã phiếu nhập
    public String getMaPN() {
        return maPN;
    }

    // Gán mã phiếu nhập
    public void setMaPN(String maPN) {
        this.maPN = maPN;
    }

    // Lấy ngày giờ nhập kho
    public LocalDateTime getNgayNhap() {
        return ngayNhap;
    }

    // Gán ngày giờ nhập kho
    public void setNgayNhap(LocalDateTime ngayNhap) {
        this.ngayNhap = ngayNhap;
    }

    // Lấy trạng thái phiếu nhập
    public String getTrangThai() {
        return trangThai;
    }

    // Gán trạng thái phiếu nhập
    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    // Lấy ghi chú phiếu nhập
    public String getGhiChu() {
        return ghiChu;
    }

    // Gán ghi chú phiếu nhập
    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    // Lấy mã nhân viên lập phiếu
    public String getMaNV() {
        return maNV;
    }

    // Gán mã nhân viên lập phiếu
    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    // Lấy mã nhà cung cấp
    public String getMaNCC() {
        return maNCC;
    }

    // Gán mã nhà cung cấp
    public void setMaNCC(String maNCC) {
        this.maNCC = maNCC;
    }
}
