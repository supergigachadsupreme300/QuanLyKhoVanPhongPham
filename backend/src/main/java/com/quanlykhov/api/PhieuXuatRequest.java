package com.quanlykhov.api;

import java.util.List;

// Lớp nhận dữ liệu JSON gửi lên khi lập phiếu xuất kho mới
public class PhieuXuatRequest {

    private String maNV;
    private String maKH;
    private String ghiChu;
    private List<ChiTietXuatRequest> chiTiet;

    // Hàm tạo không tham số để Spring đọc dữ liệu từ JSON
    public PhieuXuatRequest() {
    }

    // Lấy mã nhân viên lập phiếu
    public String getMaNV() {
        return maNV;
    }

    // Gán mã nhân viên lập phiếu
    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    // Lấy mã khách hàng nhận hàng
    public String getMaKH() {
        return maKH;
    }

    // Gán mã khách hàng nhận hàng
    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    // Lấy ghi chú của phiếu xuất
    public String getGhiChu() {
        return ghiChu;
    }

    // Gán ghi chú của phiếu xuất
    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    // Lấy danh sách các dòng sản phẩm cần xuất
    public List<ChiTietXuatRequest> getChiTiet() {
        return chiTiet;
    }

    // Gán danh sách các dòng sản phẩm cần xuất
    public void setChiTiet(List<ChiTietXuatRequest> chiTiet) {
        this.chiTiet = chiTiet;
    }
}
