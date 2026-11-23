package com.quanlykhov.model;

import java.io.Serializable;
import java.util.Objects;

// Lớp phụ chứa 2 cột khóa chính (MaPK, MaSP) cho bảng ChiTietPhieuKiem;
// Serializable là giao diện bắt buộc của JPA dành cho lớp khóa chính ghép
public class ChiTietPhieuKiemId implements Serializable {

    private String maPK;
    private String maSP;

    // Hàm tạo không tham số (bắt buộc cho JPA)
    public ChiTietPhieuKiemId() {
    }

    // Hàm tạo đầy đủ tham số để khởi tạo khóa ghép (MaPK, MaSP)
    public ChiTietPhieuKiemId(String maPK, String maSP) {
        this.maPK = maPK;
        this.maSP = maSP;
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

    // So sánh 2 khóa ghép có cùng MaPK và MaSP hay không (bắt buộc cho JPA)
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ChiTietPhieuKiemId other = (ChiTietPhieuKiemId) obj;
        return Objects.equals(maPK, other.maPK) && Objects.equals(maSP, other.maSP);
    }

    // Tính mã băm từ MaPK và MaSP để JPA phân biệt từng dòng chi tiết (bắt buộc cho JPA)
    @Override
    public int hashCode() {
        return Objects.hash(maPK, maSP);
    }
}
