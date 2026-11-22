package com.quanlykhov.dao;

import com.quanlykhov.model.ChiTietPhieuNhap;
import com.quanlykhov.model.ChiTietPhieuNhapId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// Đánh dấu giao diện này là nơi truy xuất dữ liệu từ bảng ChiTietPhieuNhap
@Repository
public interface ChiTietPhieuNhapRepository extends JpaRepository<ChiTietPhieuNhap, ChiTietPhieuNhapId> {

    // Lấy danh sách tất cả dòng chi tiết của một phiếu nhập theo mã phiếu nhập
    List<ChiTietPhieuNhap> findByMaPN(String maPN);
}
