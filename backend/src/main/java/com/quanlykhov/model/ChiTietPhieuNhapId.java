package com.quanlykhov.model;

import java.io.Serializable;
import java.util.Objects;

// Lớp này chỉ để đại diện cho khóa chính gồm 2 cột (MaPN và MaSP) của bảng ChiTietPhieuNhap
public class ChiTietPhieuNhapId implements Serializable {

    private String maPN;
    private String maSP;

    // Hàm tạo không tham số (bắt buộc cho JPA)
    public ChiTietPhieuNhapId() {
    }

    // Hàm tạo đầy đủ tham số để khởi tạo khóa chính 2 cột
    public ChiTietPhieuNhapId(String maPN, String maSP) {
        this.maPN = maPN;
        this.maSP = maSP;
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

    // So sánh hai đối tượng khóa chính có trùng mã phiếu nhập và mã sản phẩm hay không (bắt buộc cho khóa chính 2 cột)
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChiTietPhieuNhapId that = (ChiTietPhieuNhapId) o;
        return Objects.equals(maPN, that.maPN) && Objects.equals(maSP, that.maSP);
    }

    // Tạo mã băm từ mã phiếu nhập và mã sản phẩm (bắt buộc đi kèm với hàm equals cho khóa chính 2 cột)
    @Override
    public int hashCode() {
        return Objects.hash(maPN, maSP);
    }
}
