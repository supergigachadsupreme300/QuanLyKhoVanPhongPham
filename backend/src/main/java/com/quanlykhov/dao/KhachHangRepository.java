package com.quanlykhov.dao;

import com.quanlykhov.model.KhachHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// Giao diện truy xuất dữ liệu bảng KhachHang bằng các hàm có sẵn của JpaRepository
@Repository
public interface KhachHangRepository extends JpaRepository<KhachHang, String> {

    // Tìm khách hàng có tên hoặc số điện thoại chứa từ khóa, không phân biệt hoa thường
    List<KhachHang> findByTenKHContainingIgnoreCaseOrSdtContainingIgnoreCase(String tenKH, String sdt);
}
