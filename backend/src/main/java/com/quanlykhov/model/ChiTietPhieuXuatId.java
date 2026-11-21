package com.quanlykhov.model;

import java.io.Serializable;
import java.util.Objects;

// Lớp phụ chứa 2 cột khóa chính (MaPX, MaSP) cho bảng ChiTietPhieuXuat;
// Serializable là giao diện bắt buộc của JPA dành cho lớp khóa chính ghép
public class ChiTietPhieuXuatId implements Serializable {

    private String maPX;
    private String maSP;

    // Hàm tạo không tham số (bắt buộc cho JPA)
    public ChiTietPhieuXuatId() {
    }

    // Hàm tạo đầy đủ tham số để khởi tạo khóa ghép (MaPX, MaSP)
    public ChiTietPhieuXuatId(String maPX, String maSP) {
        this.maPX = maPX;
        this.maSP = maSP;
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

    // So sánh 2 khóa ghép có cùng MaPX và MaSP hay không (bắt buộc cho JPA)
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ChiTietPhieuXuatId other = (ChiTietPhieuXuatId) obj;
        return Objects.equals(maPX, other.maPX) && Objects.equals(maSP, other.maSP);
    }

    // Tính mã băm từ MaPX và MaSP để JPA phân biệt từng dòng chi tiết (bắt buộc cho JPA)
    @Override
    public int hashCode() {
        return Objects.hash(maPX, maSP);
    }
}
