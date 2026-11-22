package com.quanlykhov.api;

import java.util.List;

// Lớp nhận dữ liệu JSON gửi lên khi lập phiếu kiểm kê mới
public class PhieuKiemKeRequest {

    private String ghiChu;
    private List<ChiTietKiemRequest> chiTiet;

    // Hàm tạo không tham số để Spring đọc dữ liệu từ JSON
    public PhieuKiemKeRequest() {
    }

    // Lấy ghi chú của phiếu kiểm kê
    public String getGhiChu() {
        return ghiChu;
    }

    // Gán ghi chú của phiếu kiểm kê
    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    // Lấy danh sách các dòng sản phẩm cần kiểm kê
    public List<ChiTietKiemRequest> getChiTiet() {
        return chiTiet;
    }

    // Gán danh sách các dòng sản phẩm cần kiểm kê
    public void setChiTiet(List<ChiTietKiemRequest> chiTiet) {
        this.chiTiet = chiTiet;
    }
}
