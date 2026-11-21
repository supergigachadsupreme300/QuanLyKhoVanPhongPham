package com.quanlykhov.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// Đánh dấu lớp này là thực thể ánh xạ với bảng trong cơ sở dữ liệu
@Entity
// Chỉ định tên bảng trong MySQL là PhieuXuatKho
@Table(name = "PhieuXuatKho")
public class PhieuXuatKho {

    // Khóa chính MaPX của bảng PhieuXuatKho
    @Id
    @Column(name = "MaPX", length = 20)
    private String maPX;

    // Ngày giờ xuất kho (khớp với kiểu DATETIME trong MySQL)
    @Column(name = "NgayXuat")
    private LocalDateTime ngayXuat;

    // Trạng thái phiếu xuất ('Hoàn thành' hoặc 'Hủy')
    @Column(name = "TrangThai", length = 50)
    private String trangThai;

    // Ghi chú thêm của phiếu xuất
    @Column(name = "GhiChu", length = 255)
    private String ghiChu;

    // Mã nhân viên lập phiếu (khóa ngoại trỏ tới NhanVien)
    @Column(name = "MaNV", length = 20, nullable = false)
    private String maNV;

    // Mã khách hàng nhận hàng (khóa ngoại trỏ tới KhachHang)
    @Column(name = "MaKH", length = 20, nullable = false)
    private String maKH;

    // Hàm tạo không tham số (bắt buộc cho JPA)
    public PhieuXuatKho() {
    }

    // Hàm tạo đầy đủ tham số để khởi tạo đối tượng phiếu xuất kho
    public PhieuXuatKho(String maPX, LocalDateTime ngayXuat, String trangThai,
                        String ghiChu, String maNV, String maKH) {
        this.maPX = maPX;
        this.ngayXuat = ngayXuat;
        this.trangThai = trangThai;
        this.ghiChu = ghiChu;
        this.maNV = maNV;
        this.maKH = maKH;
    }

    // Lấy mã phiếu xuất
    public String getMaPX() {
        return maPX;
    }

    // Gán mã phiếu xuất
    public void setMaPX(String maPX) {
        this.maPX = maPX;
    }

    // Lấy ngày giờ xuất kho
    public LocalDateTime getNgayXuat() {
        return ngayXuat;
    }

    // Gán ngày giờ xuất kho
    public void setNgayXuat(LocalDateTime ngayXuat) {
        this.ngayXuat = ngayXuat;
    }

    // Lấy trạng thái phiếu xuất
    public String getTrangThai() {
        return trangThai;
    }

    // Gán trạng thái phiếu xuất
    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    // Lấy ghi chú phiếu xuất
    public String getGhiChu() {
        return ghiChu;
    }

    // Gán ghi chú phiếu xuất
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

    // Lấy mã khách hàng
    public String getMaKH() {
        return maKH;
    }

    // Gán mã khách hàng
    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }
}
