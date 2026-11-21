package com.quanlykhov.dao;

import com.quanlykhov.model.KeHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Giao diện truy xuất dữ liệu bảng KeHang bằng các hàm có sẵn của JpaRepository
@Repository
public interface KeHangRepository extends JpaRepository<KeHang, String> {
}
