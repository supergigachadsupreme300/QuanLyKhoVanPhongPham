package com.quanlykhov.model;

import jakarta.persistence.*;

// Lớp DanhMuc tương ứng với bảng DanhMuc trong cơ sở dữ liệu
@Entity
@Table(name = "DanhMuc")
public class DanhMuc {

    // Khóa chính MaDM của bảng DanhMuc
    @Id
    @Column(name = "MaDM", length = 20)
    private String maDM;

    // Tên danh mục sản phẩm (cột TenDM)
    @Column(name = "TenDM", length = 100)
    private String tenDM;

    // Hàm tạo không tham số bắt buộc cho JPA
    public DanhMuc() {
    }

    // Hàm tạo có tham số để tạo nhanh đối tượng DanhMuc
    public DanhMuc(String maDM, String tenDM) {
        this.maDM = maDM;
        this.tenDM = tenDM;
    }

    // Lấy mã danh mục
    public String getMaDM() {
        return maDM;
    }

    // Gán giá trị mã danh mục
    public void setMaDM(String maDM) {
        this.maDM = maDM;
    }

    // Lấy tên danh mục
    public String getTenDM() {
        return tenDM;
    }

    // Gán giá trị tên danh mục
    public void setTenDM(String tenDM) {
        this.tenDM = tenDM;
    }
}
