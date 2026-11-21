package com.quanlykhov.dao;

import com.quanlykhov.model.DanhMuc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Giao diện truy xuất dữ liệu bảng DanhMuc bằng các hàm có sẵn của JpaRepository
@Repository
public interface DanhMucRepository extends JpaRepository<DanhMuc, String> {
}
