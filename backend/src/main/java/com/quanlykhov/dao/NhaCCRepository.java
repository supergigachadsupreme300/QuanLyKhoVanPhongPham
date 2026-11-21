package com.quanlykhov.dao;

import com.quanlykhov.model.NhaCC;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Đánh dấu giao diện này là nơi truy xuất dữ liệu từ bảng NhaCC
@Repository
public interface NhaCCRepository extends JpaRepository<NhaCC, String> {
}
