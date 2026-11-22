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

    // Interface projection hứng kết quả thống kê tổng chung (tổng số phiếu, tổng tiền, tổng số lượng thực nhập, tổng số lượng theo chứng từ, chênh lệch)
    public interface ThongKeTongChungProjection {
        Integer getTongSoPhieu();
        BigDecimal getTongTien();
        Integer getTongSLThucNhap();
        Integer getTongSLTheoChungTu();
        Integer getChenhLech();
    }

    // Interface projection hứng kết quả thống kê theo từng nhà cung cấp
    public interface ThongKeTheoNCCProjection {
        String getMaNCC();
        String getTenNCC();
        Integer getSoPhieu();
        BigDecimal getTongTien();
    }

    // Interface projection hứng kết quả thống kê theo từng sản phẩm
    public interface ThongKeTheoSanPhamProjection {
        String getMaSP();
        String getTenSP();
        Integer getTongSLThucNhap();
        BigDecimal getTongTien();
    }

    // Interface projection hứng kết quả thống kê theo từng tháng trong năm
    public interface ThongKeTheoThangProjection {
        Integer getThang();
        Integer getSoPhieu();
        BigDecimal getTongTien();
    }

    // Interface projection hứng thông tin đầu phiếu của 1 phiếu nhập để in phiếu
    public interface ThongTinPhieuNhapInProjection {
        String getMaPN();
        LocalDateTime getNgayNhap();
        String getTrangThai();
        String getGhiChu();
        String getMaNCC();
        String getTenNCC();
        String getMaNV();
        String getHoTenNV();
    }

    // Interface projection hứng danh sách các dòng chi tiết sản phẩm của 1 phiếu nhập để in phiếu
    public interface ChiTietPhieuNhapInProjection {
        String getMaSP();
        String getTenSP();
        String getDonViTinh();
        Integer getSlTheoChungTu();
        Integer getSlThucNhap();
        BigDecimal getGiaNhap();
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

    // Truy vấn tính các con số thống kê tổng chung (tổng số phiếu, tổng tiền, tổng SL thực nhập, tổng SL chứng từ, chênh lệch) của các phiếu 'Hoàn thành'
    @Query(value = "SELECT "
            + "COUNT(DISTINCT pn.MaPN) AS tongSoPhieu, "
            + "COALESCE(SUM(ct.SLThucNhap * ct.GiaNhap), 0) AS tongTien, "
            + "COALESCE(SUM(ct.SLThucNhap), 0) AS tongSLThucNhap, "
            + "COALESCE(SUM(ct.SLTheoChungTu), 0) AS tongSLTheoChungTu, "
            + "COALESCE(SUM(ct.SLTheoChungTu) - SUM(ct.SLThucNhap), 0) AS chenhLech "
            + "FROM PhieuNhapKho pn "
            + "LEFT JOIN ChiTietPhieuNhap ct ON pn.MaPN = ct.MaPN "
            + "WHERE pn.TrangThai = 'Hoàn thành' "
            + "AND pn.NgayNhap >= :tuNgay AND pn.NgayNhap < :denNgay",
            nativeQuery = true)
    ThongKeTongChungProjection thongKeTongChung(
            @Param("tuNgay") LocalDateTime tuNgay,
            @Param("denNgay") LocalDateTime denNgay
    );

    // Truy vấn thống kê số phiếu và tổng tiền nhập theo từng nhà cung cấp của các phiếu 'Hoàn thành', sắp xếp tổng tiền giảm dần
    @Query(value = "SELECT "
            + "ncc.MaNCC AS maNCC, "
            + "ncc.TenNCC AS tenNCC, "
            + "COUNT(DISTINCT pn.MaPN) AS soPhieu, "
            + "COALESCE(SUM(ct.SLThucNhap * ct.GiaNhap), 0) AS tongTien "
            + "FROM PhieuNhapKho pn "
            + "JOIN NhaCC ncc ON pn.MaNCC = ncc.MaNCC "
            + "LEFT JOIN ChiTietPhieuNhap ct ON pn.MaPN = ct.MaPN "
            + "WHERE pn.TrangThai = 'Hoàn thành' "
            + "AND pn.NgayNhap >= :tuNgay AND pn.NgayNhap < :denNgay "
            + "GROUP BY ncc.MaNCC, ncc.TenNCC "
            + "ORDER BY tongTien DESC",
            nativeQuery = true)
    List<ThongKeTheoNCCProjection> thongKeTheoNhaCungCap(
            @Param("tuNgay") LocalDateTime tuNgay,
            @Param("denNgay") LocalDateTime denNgay
    );

    // Truy vấn thống kê tổng số lượng thực nhập và tổng tiền theo từng sản phẩm của các phiếu 'Hoàn thành', sắp xếp tổng tiền giảm dần
    @Query(value = "SELECT "
            + "sp.MaSP AS maSP, "
            + "sp.TenSP AS tenSP, "
            + "COALESCE(SUM(ct.SLThucNhap), 0) AS tongSLThucNhap, "
            + "COALESCE(SUM(ct.SLThucNhap * ct.GiaNhap), 0) AS tongTien "
            + "FROM PhieuNhapKho pn "
            + "JOIN ChiTietPhieuNhap ct ON pn.MaPN = ct.MaPN "
            + "JOIN SanPham sp ON ct.MaSP = sp.MaSP "
            + "WHERE pn.TrangThai = 'Hoàn thành' "
            + "AND pn.NgayNhap >= :tuNgay AND pn.NgayNhap < :denNgay "
            + "GROUP BY sp.MaSP, sp.TenSP "
            + "ORDER BY tongTien DESC",
            nativeQuery = true)
    List<ThongKeTheoSanPhamProjection> thongKeTheoSanPham(
            @Param("tuNgay") LocalDateTime tuNgay,
            @Param("denNgay") LocalDateTime denNgay
    );

    // Truy vấn thống kê số phiếu và tổng tiền theo từng tháng của các phiếu 'Hoàn thành' trong khoảng thời gian [tuNgay, denNgay)
    @Query(value = "SELECT "
            + "MONTH(pn.NgayNhap) AS thang, "
            + "COUNT(DISTINCT pn.MaPN) AS soPhieu, "
            + "COALESCE(SUM(ct.SLThucNhap * ct.GiaNhap), 0) AS tongTien "
            + "FROM PhieuNhapKho pn "
            + "LEFT JOIN ChiTietPhieuNhap ct ON pn.MaPN = ct.MaPN "
            + "WHERE pn.TrangThai = 'Hoàn thành' "
            + "AND pn.NgayNhap >= :tuNgay AND pn.NgayNhap < :denNgay "
            + "GROUP BY MONTH(pn.NgayNhap) "
            + "ORDER BY thang ASC",
            nativeQuery = true)
    List<ThongKeTheoThangProjection> thongKeTheoThang(
            @Param("tuNgay") LocalDateTime tuNgay,
            @Param("denNgay") LocalDateTime denNgay
    );

    // Truy vấn lấy thông tin đầu phiếu của 1 phiếu nhập theo maPN (nối với bảng NhaCC để lấy tên nhà cung cấp và NhanVien để lấy họ tên nhân viên)
    @Query(value = "SELECT "
            + "pn.MaPN AS maPN, "
            + "pn.NgayNhap AS ngayNhap, "
            + "pn.TrangThai AS trangThai, "
            + "pn.GhiChu AS ghiChu, "
            + "pn.MaNCC AS maNCC, "
            + "ncc.TenNCC AS tenNCC, "
            + "pn.MaNV AS maNV, "
            + "nv.HoTen AS hoTenNV "
            + "FROM PhieuNhapKho pn "
            + "JOIN NhaCC ncc ON pn.MaNCC = ncc.MaNCC "
            + "JOIN NhanVien nv ON pn.MaNV = nv.MaNV "
            + "WHERE pn.MaPN = :maPN",
            nativeQuery = true)
    ThongTinPhieuNhapInProjection layThongTinPhieuNhapDeIn(@Param("maPN") String maPN);

    // Truy vấn lấy các dòng chi tiết của 1 phiếu nhập theo maPN (nối với bảng SanPham để lấy tên sản phẩm và đơn vị tính, sắp theo maSP tăng dần)
    @Query(value = "SELECT "
            + "ct.MaSP AS maSP, "
            + "sp.TenSP AS tenSP, "
            + "sp.DonViTinh AS donViTinh, "
            + "ct.SLTheoChungTu AS slTheoChungTu, "
            + "ct.SLThucNhap AS slThucNhap, "
            + "ct.GiaNhap AS giaNhap "
            + "FROM ChiTietPhieuNhap ct "
            + "JOIN SanPham sp ON ct.MaSP = sp.MaSP "
            + "WHERE ct.MaPN = :maPN "
            + "ORDER BY ct.MaSP ASC",
            nativeQuery = true)
    List<ChiTietPhieuNhapInProjection> layChiTietPhieuNhapDeIn(@Param("maPN") String maPN);
}
