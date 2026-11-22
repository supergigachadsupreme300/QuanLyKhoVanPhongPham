package com.quanlykhov.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// Đánh dấu lớp này là thực thể ánh xạ với bảng trong cơ sở dữ liệu
@Entity
// Chỉ định tên bảng trong MySQL là PhieuKiemKe
@Table(name = "PhieuKiemKe")
public class PhieuKiemKe {

    // Khóa chính MaPK của bảng PhieuKiemKe
    @Id
    @Column(name = "MaPK", length = 20)
    private String maPK;

    // Ngày giờ kiểm kê (khớp với kiểu DATETIME trong MySQL)
    @Column(name = "NgayKiem")
    private LocalDateTime ngayKiem;

    // Trạng thái phiếu kiểm kê (ví dụ: 'Hoàn thành')
    @Column(name = "TrangThai", length = 50)
    private String trangThai;

    // Ghi chú thêm của phiếu kiểm kê
    @Column(name = "GhiChu", length = 255)
    private String ghiChu;

    // Hàm tạo không tham số (bắt buộc cho JPA)
    public PhieuKiemKe() {
    }

    // Hàm tạo đầy đủ tham số để khởi tạo đối tượng phiếu kiểm kê
    public PhieuKiemKe(String maPK, LocalDateTime ngayKiem, String trangThai, String ghiChu) {
        this.maPK = maPK;
        this.ngayKiem = ngayKiem;
        this.trangThai = trangThai;
        this.ghiChu = ghiChu;
    }

    // Lấy mã phiếu kiểm kê
    public String getMaPK() {
        return maPK;
    }

    // Gán mã phiếu kiểm kê
    public void setMaPK(String maPK) {
        this.maPK = maPK;
    }

    // Lấy ngày giờ kiểm kê
    public LocalDateTime getNgayKiem() {
        return ngayKiem;
    }

    // Gán ngày giờ kiểm kê
    public void setNgayKiem(LocalDateTime ngayKiem) {
        this.ngayKiem = ngayKiem;
    }

    // Lấy trạng thái phiếu kiểm kê
    public String getTrangThai() {
        return trangThai;
    }

    // Gán trạng thái phiếu kiểm kê
    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    // Lấy ghi chú phiếu kiểm kê
    public String getGhiChu() {
        return ghiChu;
    }

    // Gán ghi chú phiếu kiểm kê
    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
}
