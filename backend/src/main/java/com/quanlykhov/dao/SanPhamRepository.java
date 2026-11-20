package com.quanlykhov.dao;

import com.quanlykhov.model.SanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Đánh dấu giao diện này là nơi truy xuất dữ liệu từ bảng SanPham
@Repository
public interface SanPhamRepository extends JpaRepository<SanPham, String> {
}
