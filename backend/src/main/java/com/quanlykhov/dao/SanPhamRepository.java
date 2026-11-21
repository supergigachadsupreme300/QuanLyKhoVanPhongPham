package com.quanlykhov.dao;

import com.quanlykhov.model.SanPham;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

// Đánh dấu giao diện này là nơi truy xuất dữ liệu từ bảng SanPham
@Repository
public interface SanPhamRepository extends JpaRepository<SanPham, String> {

    // @Query dùng để tự viết câu truy vấn tìm và lọc sản phẩm theo các điều kiện tùy chọn (tham số nào null thì bỏ qua)
    @Query("SELECT sp FROM SanPham sp WHERE " +
           "(:ten IS NULL OR sp.tenSP LIKE CONCAT('%', :ten, '%')) AND " +
           "(:giaTu IS NULL OR sp.giaBan >= :giaTu) AND " +
           "(:giaDen IS NULL OR sp.giaBan <= :giaDen) AND " +
           "(:maNCC IS NULL OR sp.maNCC = :maNCC) AND " +
           "(:trangThai IS NULL OR sp.trangThai = :trangThai)")
    List<SanPham> timKiemSanPham(
            @Param("ten") String ten,
            @Param("giaTu") BigDecimal giaTu,
            @Param("giaDen") BigDecimal giaDen,
            @Param("maNCC") String maNCC,
            @Param("trangThai") String trangThai,
            Sort sort);
}
