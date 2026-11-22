package com.quanlykhov.dao;

import com.quanlykhov.model.PhieuKiemKe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// Giao diện truy xuất dữ liệu bảng PhieuKiemKe bằng JpaRepository
@Repository
public interface PhieuKiemKeRepository extends JpaRepository<PhieuKiemKe, String> {

    // Lấy danh sách tất cả phiếu kiểm kê, sắp xếp theo ngày kiểm mới nhất lên đầu
    List<PhieuKiemKe> findAllByOrderByNgayKiemDesc();
}
