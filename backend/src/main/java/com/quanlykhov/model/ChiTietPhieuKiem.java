package com.quanlykhov.model;

import jakarta.persistence.*;

// Đánh dấu lớp này là thực thể ánh xạ với bảng trong cơ sở dữ liệu
@Entity
// Chỉ định tên bảng trong MySQL là ChiTietPhieuKiem
@Table(name = "ChiTietPhieuKiem")
// @IdClass khai báo lớp phụ giữ khóa chính ghép gồm 2 cột (MaPK và MaSP)
@IdClass(ChiTietPhieuKiemId.class)
public class ChiTietPhieuKiem {

    // Phần 1 của khóa chính ghép: mã phiếu kiểm kê
    @Id
    @Column(name = "MaPK", length = 20)
    private String maPK;

    // Phần 2 của khóa chính ghép: mã sản phẩm
    @Id
    @Column(name = "MaSP", length = 20)
    private String maSP;

    // Số lượng tồn trên sổ sách tại thời điểm kiểm kê (không được để trống)
    @Column(name = "SLSoSach", nullable = false)
    private Integer slSoSach;

    // Số lượng đếm thực tế trong kho (có thể là null nếu chưa đếm)
    @Column(name = "SLThucTe")
    private Integer slThucTe;

    // Lý do giải thích nếu có chênh lệch
    @Column(name = "LyDo", length = 255)
    private String lyDo;

    // Hàm tạo không tham số (bắt buộc cho JPA)
    public ChiTietPhieuKiem() {
    }

    // Hàm tạo đầy đủ tham số để khởi tạo một dòng chi tiết phiếu kiểm
    public ChiTietPhieuKiem(String maPK, String maSP, Integer slSoSach, Integer slThucTe, String lyDo) {
        this.maPK = maPK;
        this.maSP = maSP;
        this.slSoSach = slSoSach;
        this.slThucTe = slThucTe;
        this.lyDo = lyDo;
    }

    // Lấy mã phiếu kiểm kê
    public String getMaPK() {
        return maPK;
    }

    // Gán mã phiếu kiểm kê
    public void setMaPK(String maPK) {
        this.maPK = maPK;
    }

    // Lấy mã sản phẩm
    public String getMaSP() {
        return maSP;
    }

    // Gán mã sản phẩm
    public void setMaSP(String maSP) {
        this.maSP = maSP;
    }

    // Lấy số lượng trên sổ sách
    public Integer getSlSoSach() {
        return slSoSach;
    }

    // Gán số lượng trên sổ sách
    public void setSlSoSach(Integer slSoSach) {
        this.slSoSach = slSoSach;
    }

    // Lấy số lượng đếm thực tế
    public Integer getSlThucTe() {
        return slThucTe;
    }

    // Gán số lượng đếm thực tế
    public void setSlThucTe(Integer slThucTe) {
        this.slThucTe = slThucTe;
    }

    // Lấy lý do chênh lệch
    public String getLyDo() {
        return lyDo;
    }

    // Gán lý do chênh lệch
    public void setLyDo(String lyDo) {
        this.lyDo = lyDo;
    }
}
