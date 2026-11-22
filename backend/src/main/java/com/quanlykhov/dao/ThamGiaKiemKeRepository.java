package com.quanlykhov.dao;

import com.quanlykhov.model.ThamGiaKiemKe;
import com.quanlykhov.model.ThamGiaKiemKeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// Giao diện truy xuất dữ liệu bảng ThamGiaKiemKe (khóa chính ghép kiểu ThamGiaKiemKeId)
@Repository
public interface ThamGiaKiemKeRepository extends JpaRepository<ThamGiaKiemKe, ThamGiaKiemKeId> {

    // Lấy danh sách nhân viên tham gia kiểm kê thuộc một mã phiếu kiểm kê
    List<ThamGiaKiemKe> findByMaPK(String maPK);
}
