package com.quanlykhov.dao;

import com.quanlykhov.model.PhieuNhapKho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// Đánh dấu giao diện này là nơi truy xuất dữ liệu từ bảng PhieuNhapKho
@Repository
public interface PhieuNhapKhoRepository extends JpaRepository<PhieuNhapKho, String> {

    // Lấy danh sách tất cả phiếu nhập kho sắp xếp theo ngày nhập giảm dần (mới nhất lên đầu)
    List<PhieuNhapKho> findAllByOrderByNgayNhapDesc();
}
