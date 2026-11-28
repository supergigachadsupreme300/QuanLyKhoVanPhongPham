package com.quanlykhov.dao;

import com.quanlykhov.model.TaiKhoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaiKhoanRepository extends JpaRepository<TaiKhoan, String> {
    Optional<TaiKhoan> findByTenDangNhap(String tenDangNhap);
    List<TaiKhoan> findByMaVT(String maVT);
    List<TaiKhoan> findByMaNV(String maNV);
    boolean existsByTenDangNhapIgnoreCase(String tenDangNhap);
    boolean existsByMaNV(String maNV);
}