package com.quanlykhov.model;

import jakarta.persistence.*;

// Đánh dấu lớp này là một thực thể ánh xạ với bảng trong cơ sở dữ liệu
@Entity
// Chỉ định tên bảng trong MySQL là NhaCC
@Table(name = "NhaCC")
public class NhaCC {

    // Đánh dấu MaNCC là khóa chính của bảng
    @Id
    @Column(name = "MaNCC", length = 20)
    private String maNCC;

    @Column(name = "TenNCC", length = 100)
    private String tenNCC;

    @Column(name = "DiaChi", length = 255)
    private String diaChi;

    @Column(name = "Sdt", length = 15)
    private String sdt;

    @Column(name = "TrangThai", length = 50)
    private String trangThai;

    // Hàm tạo không tham số (bắt buộc cho JPA)
    public NhaCC() {
    }

    // Hàm tạo đầy đủ tham số để khởi tạo đối tượng nhà cung cấp
    public NhaCC(String maNCC, String tenNCC, String diaChi, String sdt, String trangThai) {
        this.maNCC = maNCC;
        this.tenNCC = tenNCC;
        this.diaChi = diaChi;
        this.sdt = sdt;
        this.trangThai = trangThai;
    }

    // Lấy mã nhà cung cấp
    public String getMaNCC() {
        return maNCC;
    }

    // Gán mã nhà cung cấp
    public void setMaNCC(String maNCC) {
        this.maNCC = maNCC;
    }

    // Lấy tên nhà cung cấp
    public String getTenNCC() {
        return tenNCC;
    }

    // Gán tên nhà cung cấp
    public void setTenNCC(String tenNCC) {
        this.tenNCC = tenNCC;
    }

    // Lấy địa chỉ nhà cung cấp
    public String getDiaChi() {
        return diaChi;
    }

    // Gán địa chỉ nhà cung cấp
    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    // Lấy số điện thoại nhà cung cấp
    public String getSdt() {
        return sdt;
    }

    // Gán số điện thoại nhà cung cấp
    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    // Lấy trạng thái hợp tác
    public String getTrangThai() {
        return trangThai;
    }

    // Gán trạng thái hợp tác
    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}
