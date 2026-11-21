package com.quanlykhov.dao;

import com.quanlykhov.model.ChiTietPhieuXuat;
import com.quanlykhov.model.ChiTietPhieuXuatId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// Giao diện truy xuất dữ liệu bảng ChiTietPhieuXuat (khóa chính ghép kiểu ChiTietPhieuXuatId)
@Repository
public interface ChiTietPhieuXuatRepository extends JpaRepository<ChiTietPhieuXuat, ChiTietPhieuXuatId> {

    // Lấy danh sách tất cả dòng chi tiết thuộc một mã phiếu xuất
    List<ChiTietPhieuXuat> findByMaPX(String maPX);
}
