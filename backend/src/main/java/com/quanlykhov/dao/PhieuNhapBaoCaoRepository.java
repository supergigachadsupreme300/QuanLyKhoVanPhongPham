package com.quanlykhov.dao;

import com.quanlykhov.model.PhieuNhapKho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

// Đánh dấu giao diện này là nơi thực hiện các truy vấn báo cáo cho bảng PhieuNhapKho
@Repository
public interface PhieuNhapBaoCaoRepository extends JpaRepository<PhieuNhapKho, String> {

    // Interface projection: khuôn mẫu chỉ gồm các hàm get...() để Spring tự động hứng các cột kết quả từ câu lệnh SQL theo đúng tên bí danh (AS)
    public interface LichSuPhieuNhapProjection {
        String getMaPN();
        LocalDateTime getNgayNhap();
        String getMaNCC();
        String getTenNCC();
        String getMaNV();
        String getHoTenNV();
        String getTrangThai();
        String getGhiChu();
        BigDecimal getTongTien();
        Integer getTongSLTheoChungTu();
        Integer getTongSLThucNhap();
    }

    // Truy vấn lấy danh sách lịch sử phiếu nhập trong khoảng thời gian [tuNgay, denNgay),
    // nối với bảng NhaCC để lấy tên nhà cung cấp, nối với NhanVien để lấy họ tên nhân viên,
    // nối trái (LEFT JOIN) với ChiTietPhieuNhap để tính tổng tiền, tổng số lượng theo chứng từ và tổng số lượng thực nhập của từng phiếu
    @Query(value = "SELECT "
            + "pn.MaPN AS maPN, "
            + "pn.NgayNhap AS ngayNhap, "
            + "pn.MaNCC AS maNCC, "
            + "ncc.TenNCC AS tenNCC, "
            + "pn.MaNV AS maNV, "
            + "nv.HoTen AS hoTenNV, "
            + "pn.TrangThai AS trangThai, "
            + "pn.GhiChu AS ghiChu, "
            + "COALESCE(SUM(ct.SLThucNhap * ct.GiaNhap), 0) AS tongTien, "
            + "COALESCE(SUM(ct.SLTheoChungTu), 0) AS tongSLTheoChungTu, "
            + "COALESCE(SUM(ct.SLThucNhap), 0) AS tongSLThucNhap "
            + "FROM PhieuNhapKho pn "
            + "JOIN NhaCC ncc ON pn.MaNCC = ncc.MaNCC "
            + "JOIN NhanVien nv ON pn.MaNV = nv.MaNV "
            + "LEFT JOIN ChiTietPhieuNhap ct ON pn.MaPN = ct.MaPN "
            + "WHERE pn.NgayNhap >= :tuNgay AND pn.NgayNhap < :denNgay "
            + "GROUP BY pn.MaPN, pn.NgayNhap, pn.MaNCC, ncc.TenNCC, pn.MaNV, nv.HoTen, pn.TrangThai, pn.GhiChu "
            + "ORDER BY pn.NgayNhap DESC",
            nativeQuery = true)
    List<LichSuPhieuNhapProjection> layLichSuPhieuNhap(
            @Param("tuNgay") LocalDateTime tuNgay,
            @Param("denNgay") LocalDateTime denNgay
    );
}
