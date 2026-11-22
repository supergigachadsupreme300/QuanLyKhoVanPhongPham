package com.quanlykhov.dao;

import com.quanlykhov.model.ChiTietPhieuKiem;
import com.quanlykhov.model.ChiTietPhieuKiemId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// Giao diện truy xuất dữ liệu bảng ChiTietPhieuKiem (khóa chính ghép kiểu ChiTietPhieuKiemId)
@Repository
public interface ChiTietPhieuKiemRepository extends JpaRepository<ChiTietPhieuKiem, ChiTietPhieuKiemId> {

    // Lấy danh sách tất cả dòng chi tiết thuộc một mã phiếu kiểm kê
    List<ChiTietPhieuKiem> findByMaPK(String maPK);
}
