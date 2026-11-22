package com.quanlykhov.model;

import java.io.Serializable;
import java.util.Objects;

// Lớp phụ chứa 2 cột khóa chính (MaNV, MaPK) cho bảng ThamGiaKiemKe;
// Serializable là giao diện bắt buộc của JPA dành cho lớp khóa chính ghép
public class ThamGiaKiemKeId implements Serializable {

    private String maNV;
    private String maPK;

    // Hàm tạo không tham số (bắt buộc cho JPA)
    public ThamGiaKiemKeId() {
    }

    // Hàm tạo đầy đủ tham số để khởi tạo khóa ghép (MaNV, MaPK)
    public ThamGiaKiemKeId(String maNV, String maPK) {
        this.maNV = maNV;
        this.maPK = maPK;
    }

    // Lấy mã nhân viên tham gia kiểm kê
    public String getMaNV() {
        return maNV;
    }

    // Gán mã nhân viên tham gia kiểm kê
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

    // So sánh 2 khóa ghép có cùng MaNV và MaPK hay không (bắt buộc cho JPA)
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ThamGiaKiemKeId other = (ThamGiaKiemKeId) obj;
        return Objects.equals(maNV, other.maNV) && Objects.equals(maPK, other.maPK);
    }

    // Tính mã băm từ MaNV và MaPK để JPA phân biệt từng dòng tham gia (bắt buộc cho JPA)
    @Override
    public int hashCode() {
        return Objects.hash(maNV, maPK);
    }
}
