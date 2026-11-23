package com.quanlykhov.model;

import jakarta.persistence.*;

// Đánh dấu lớp này là thực thể ánh xạ với bảng trong cơ sở dữ liệu
@Entity
// Chỉ định tên bảng trong MySQL là ThamGiaKiemKe
@Table(name = "ThamGiaKiemKe")
// @IdClass khai báo lớp phụ giữ khóa chính ghép gồm 2 cột (MaNV và MaPK)
@IdClass(ThamGiaKiemKeId.class)
public class ThamGiaKiemKe {

    // Phần 1 của khóa chính ghép: mã nhân viên tham gia kiểm kê
    @Id
    @Column(name = "MaNV", length = 20)
    private String maNV;

    // Phần 2 của khóa chính ghép: mã phiếu kiểm kê
    @Id
    @Column(name = "MaPK", length = 20)
    private String maPK;

    // Vai trò của nhân viên trong đợt kiểm kê
    @Column(name = "VaiTroKiem", length = 100)
    private String vaiTroKiem;

    // Hàm tạo không tham số (bắt buộc cho JPA)
    public ThamGiaKiemKe() {
    }

    // Hàm tạo đầy đủ tham số để khởi tạo một dòng nhân viên tham gia kiểm kê
    public ThamGiaKiemKe(String maNV, String maPK, String vaiTroKiem) {
        this.maNV = maNV;
        this.maPK = maPK;
        this.vaiTroKiem = vaiTroKiem;
    }

    // Lấy mã nhân viên
    public String getMaNV() {
        return maNV;
    }

    // Gán mã nhân viên
    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    // Lấy mã phiếu kiểm kê
    public String getMaPK() {
        return maPK;
    }

    // Gán mã phiếu kiểm kê
    public void setMaPK(String maPK) {
        this.maPK = maPK;
    }

    // Lấy vai trò kiểm kê
    public String getVaiTroKiem() {
        return vaiTroKiem;
    }

    // Gán vai trò kiểm kê
    public void setVaiTroKiem(String vaiTroKiem) {
        this.vaiTroKiem = vaiTroKiem;
    }
}
