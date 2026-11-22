package com.quanlykhov.api;

import java.util.List;

// Lớp này dùng để nhận toàn bộ dữ liệu JSON khi người dùng lập một phiếu nhập kho mới
public class PhieuNhapRequest {

    private String maNV;
    private String maNCC;
    private String ghiChu;
    private List<ChiTietNhapRequest> chiTiet;

    // Hàm tạo không tham số
    public PhieuNhapRequest() {
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

    // Lấy ghi chú phiếu nhập
    public String getGhiChu() {
        return ghiChu;
    }

    // Gán ghi chú phiếu nhập
    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    // Lấy danh sách các dòng chi tiết sản phẩm nhập
    public List<ChiTietNhapRequest> getChiTiet() {
        return chiTiet;
    }

    // Gán danh sách các dòng chi tiết sản phẩm nhập
    public void setChiTiet(List<ChiTietNhapRequest> chiTiet) {
        this.chiTiet = chiTiet;
    }
}
