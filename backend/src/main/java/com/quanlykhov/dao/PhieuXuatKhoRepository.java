package com.quanlykhov.dao;

import com.quanlykhov.model.PhieuXuatKho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

// Giao diện truy xuất dữ liệu bảng PhieuXuatKho bằng JpaRepository
@Repository
public interface PhieuXuatKhoRepository extends JpaRepository<PhieuXuatKho, String> {

    // Lấy danh sách tất cả phiếu xuất, sắp xếp theo ngày xuất mới nhất lên đầu
    List<PhieuXuatKho> findAllByOrderByNgayXuatDesc();

    // Lấy danh sách phiếu xuất có ngày xuất trong khoảng [tuNgay, denNgay), sắp xếp mới nhất lên đầu
    List<PhieuXuatKho> findByNgayXuatGreaterThanEqualAndNgayXuatLessThanOrderByNgayXuatDesc(
            LocalDateTime tuNgay,
            LocalDateTime denNgay
    );
}
