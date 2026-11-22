package com.quanlykhov.api;

import java.math.BigDecimal;

// Lớp này dùng để nhận dữ liệu JSON của một dòng chi tiết phiếu nhập từ người dùng gửi lên
public class ChiTietNhapRequest {

    private String maSP;
    private Integer slTheoChungTu;
    private Integer slThucNhap;
    private BigDecimal giaNhap;

    // Hàm tạo không tham số
    public ChiTietNhapRequest() {
    }

    // Lấy mã sản phẩm
    public String getMaSP() {
        return maSP;
    }

    // Gán mã sản phẩm
    public void setMaSP(String maSP) {
        this.maSP = maSP;
    }

    // Lấy số lượng ghi trên chứng từ
    public Integer getSlTheoChungTu() {
        return slTheoChungTu;
    }

    // Gán số lượng ghi trên chứng từ
    public void setSlTheoChungTu(Integer slTheoChungTu) {
        this.slTheoChungTu = slTheoChungTu;
    }

    // Lấy số lượng đếm thực tế khi nhập kho
    public Integer getSlThucNhap() {
        return slThucNhap;
    }

    // Gán số lượng đếm thực tế khi nhập kho
    public void setSlThucNhap(Integer slThucNhap) {
        this.slThucNhap = slThucNhap;
    }

    // Lấy đơn giá nhập
    public BigDecimal getGiaNhap() {
        return giaNhap;
    }

    // Gán đơn giá nhập
    public void setGiaNhap(BigDecimal giaNhap) {
        this.giaNhap = giaNhap;
    }
}
