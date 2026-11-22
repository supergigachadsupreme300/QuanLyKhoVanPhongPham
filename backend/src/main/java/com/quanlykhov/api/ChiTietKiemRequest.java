package com.quanlykhov.api;

// Lớp nhận dữ liệu từng dòng sản phẩm trong JSON gửi lên khi lập phiếu kiểm kê
public class ChiTietKiemRequest {

    private String maSP;
    private Integer slThucTe;
    private String lyDo;

    // Hàm tạo không tham số để Spring đọc dữ liệu từ JSON
    public ChiTietKiemRequest() {
    }

    // Lấy mã sản phẩm được kiểm kê
    public String getMaSP() {
        return maSP;
    }

    // Gán mã sản phẩm được kiểm kê
    public void setMaSP(String maSP) {
        this.maSP = maSP;
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
