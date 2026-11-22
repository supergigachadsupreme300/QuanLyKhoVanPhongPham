package com.quanlykhov.dao;

import com.quanlykhov.model.NhaCC;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

// Đánh dấu giao diện này là nơi truy xuất dữ liệu từ bảng NhaCC
@Repository
public interface NhaCCRepository extends JpaRepository<NhaCC, String> {

    // @Query dùng để tự viết câu truy vấn tìm và lọc nhà cung cấp theo các điều kiện tùy chọn (tham số nào null thì bỏ qua)
    @Query("SELECT ncc FROM NhaCC ncc WHERE " +
           "(:ten IS NULL OR ncc.tenNCC LIKE CONCAT('%', :ten, '%')) AND " +
           "(:trangThai IS NULL OR ncc.trangThai = :trangThai)")
    List<NhaCC> timKiemNhaCC(
            @Param("ten") String ten,
            @Param("trangThai") String trangThai,
            Sort sort);
}
