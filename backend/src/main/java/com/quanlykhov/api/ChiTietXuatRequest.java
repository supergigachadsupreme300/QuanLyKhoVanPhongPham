package com.quanlykhov.api;

// Lớp nhận dữ liệu từng dòng sản phẩm trong JSON gửi lên khi lập phiếu xuất kho
public class ChiTietXuatRequest {

    private String maSP;
    private Integer slXuat;

    // Hàm tạo không tham số để Spring đọc dữ liệu từ JSON
    public ChiTietXuatRequest() {
    }

    // Lấy mã sản phẩm cần xuất
    public String getMaSP() {
        return maSP;
    }

    // Gán mã sản phẩm cần xuất
    public void setMaSP(String maSP) {
        this.maSP = maSP;
    }

    // Lấy số lượng sản phẩm cần xuất
    public Integer getSlXuat() {
        return slXuat;
    }

    // Gán số lượng sản phẩm cần xuất
    public void setSlXuat(Integer slXuat) {
        this.slXuat = slXuat;
    }
}
