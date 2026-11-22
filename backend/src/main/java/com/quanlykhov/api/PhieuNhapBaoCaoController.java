package com.quanlykhov.api;

import com.quanlykhov.dao.PhieuNhapBaoCaoRepository;
import com.quanlykhov.dao.PhieuNhapBaoCaoRepository.LichSuPhieuNhapProjection;
import com.quanlykhov.dao.PhieuNhapBaoCaoRepository.ThongKeTheoNCCProjection;
import com.quanlykhov.dao.PhieuNhapBaoCaoRepository.ThongKeTheoSanPhamProjection;
import com.quanlykhov.dao.PhieuNhapBaoCaoRepository.ThongKeTheoThangProjection;
import com.quanlykhov.dao.PhieuNhapBaoCaoRepository.ThongKeTongChungProjection;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// Đánh dấu lớp này là nơi tiếp nhận các yêu cầu HTTP báo cáo phiếu nhập và trả kết quả dạng JSON
@RestController
// Đặt đường dẫn gốc là /api/phieu-nhap
@RequestMapping("/api/phieu-nhap")
// Cho phép giao diện từ nguồn khác gọi vào API này
@CrossOrigin(origins = "*")
public class PhieuNhapBaoCaoController {

    private final PhieuNhapBaoCaoRepository phieuNhapBaoCaoRepository;

    // Hàm tạo để Spring tự động truyền PhieuNhapBaoCaoRepository vào sử dụng
    public PhieuNhapBaoCaoController(PhieuNhapBaoCaoRepository phieuNhapBaoCaoRepository) {
        this.phieuNhapBaoCaoRepository = phieuNhapBaoCaoRepository;
    }

    // Hàm kiểm tra tham số nam và thang: nếu có lỗi thì trả về câu thông báo lỗi tiếng Việt, nếu hợp lệ thì trả về null
    private String kiemTraNamThang(String namStr, String thangStr) {
        if (namStr == null || namStr.trim().isEmpty()) {
            return "Vui lòng nhập tham số năm (nam)!";
        }

        int nam = 0;
        try {
            nam = Integer.parseInt(namStr.trim());
        } catch (NumberFormatException e) {
            return "Tham số năm (nam) phải là một con số hợp lệ!";
        }

        if (nam < 2000 || nam > 2100) {
            return "Năm (nam) phải nằm trong khoảng từ 2000 đến 2100!";
        }

        if (thangStr != null && !thangStr.trim().isEmpty()) {
            int thang = 0;
            try {
                thang = Integer.parseInt(thangStr.trim());
            } catch (NumberFormatException e) {
                return "Tham số tháng (thang) phải là một con số hợp lệ!";
            }

            if (thang < 1 || thang > 12) {
                return "Tháng (thang) phải nằm trong khoảng từ 1 đến 12!";
            }
        }

        return null;
    }

    // Hàm tính mốc thời gian bắt đầu (tuNgay) dựa vào năm và tháng (nếu không chọn tháng thì tính từ ngày 1/1 của năm)
    private LocalDateTime tinhTuNgay(int nam, Integer thang) {
        if (thang == null) {
            return LocalDateTime.of(nam, 1, 1, 0, 0, 0);
        }
        return LocalDateTime.of(nam, thang, 1, 0, 0, 0);
    }

    // Hàm tính mốc thời gian kết thúc (denNgay) dựa vào năm và tháng (không chọn tháng thì đến 1/1 năm sau; có chọn tháng thì đến ngày 1 tháng kế tiếp)
    private LocalDateTime tinhDenNgay(int nam, Integer thang) {
        if (thang == null) {
            return LocalDateTime.of(nam + 1, 1, 1, 0, 0, 0);
        }
        if (thang == 12) {
            return LocalDateTime.of(nam + 1, 1, 1, 0, 0, 0);
        }
        return LocalDateTime.of(nam, thang + 1, 1, 0, 0, 0);
    }

    // Xử lý yêu cầu GET /api/phieu-nhap/lich-su: xem lịch sử phiếu nhập kho theo năm và tháng (tùy chọn)
    @GetMapping("/lich-su")
    public ResponseEntity<?> xemLichSuPhieuNhap(
            @RequestParam(value = "nam", required = false) String namStr,
            @RequestParam(value = "thang", required = false) String thangStr) {

        // Bước 1: Kiểm tra hợp lệ cho tham số nam và thang
        String loi = kiemTraNamThang(namStr, thangStr);
        if (loi != null) {
            return ResponseEntity.badRequest().body(loi);
        }

        int nam = Integer.parseInt(namStr.trim());
        Integer thang = null;
        if (thangStr != null && !thangStr.trim().isEmpty()) {
            thang = Integer.parseInt(thangStr.trim());
        }

        // Bước 2: Tính khoảng thời gian [tuNgay, denNgay) để truyền vào câu truy vấn SQL
        LocalDateTime tuNgay = tinhTuNgay(nam, thang);
        LocalDateTime denNgay = tinhDenNgay(nam, thang);

        // Bước 3: Gọi Repository thực hiện truy vấn và trả kết quả về
        List<LichSuPhieuNhapProjection> danhSach = phieuNhapBaoCaoRepository.layLichSuPhieuNhap(tuNgay, denNgay);
        return ResponseEntity.ok(danhSach);
    }

    // Xử lý yêu cầu GET /api/phieu-nhap/thong-ke: thống kê phiếu nhập kho (chỉ tính phiếu 'Hoàn thành') theo năm và tháng (tùy chọn)
    @GetMapping("/thong-ke")
    public ResponseEntity<?> thongKePhieuNhap(
            @RequestParam(value = "nam", required = false) String namStr,
            @RequestParam(value = "thang", required = false) String thangStr) {

        // Bước 1: Kiểm tra hợp lệ cho tham số nam và thang
        String loi = kiemTraNamThang(namStr, thangStr);
        if (loi != null) {
            return ResponseEntity.badRequest().body(loi);
        }

        int nam = Integer.parseInt(namStr.trim());
        Integer thang = null;
        if (thangStr != null && !thangStr.trim().isEmpty()) {
            thang = Integer.parseInt(thangStr.trim());
        }

        // Bước 2: Tính khoảng thời gian [tuNgay, denNgay)
        LocalDateTime tuNgay = tinhTuNgay(nam, thang);
        LocalDateTime denNgay = tinhDenNgay(nam, thang);

        // Bước 3: Thực hiện các truy vấn thống kê nhỏ
        ThongKeTongChungProjection tongChung = phieuNhapBaoCaoRepository.thongKeTongChung(tuNgay, denNgay);
        List<ThongKeTheoNCCProjection> theoNhaCungCap = phieuNhapBaoCaoRepository.thongKeTheoNhaCungCap(tuNgay, denNgay);
        List<ThongKeTheoSanPhamProjection> theoSanPham = phieuNhapBaoCaoRepository.thongKeTheoSanPham(tuNgay, denNgay);

        // Bước 4: Đưa kết quả vào LinkedHashMap để giữ đúng thứ tự các mục khi trả về JSON
        Map<String, Object> ketQua = new LinkedHashMap<>();
        ketQua.put("tongSoPhieu", tongChung.getTongSoPhieu());
        ketQua.put("tongTien", tongChung.getTongTien());
        ketQua.put("tongSLThucNhap", tongChung.getTongSLThucNhap());
        ketQua.put("tongSLTheoChungTu", tongChung.getTongSLTheoChungTu());
        ketQua.put("chenhLech", tongChung.getChenhLech());
        ketQua.put("theoNhaCungCap", theoNhaCungCap);
        ketQua.put("theoSanPham", theoSanPham);

        // Bước 5: Nếu người dùng KHÔNG gửi tham số thang (thống kê cả năm) thì thêm danh sách đủ 12 tháng
        if (thang == null) {
            List<ThongKeTheoThangProjection> duLieuThang = phieuNhapBaoCaoRepository.thongKeTheoThang(tuNgay, denNgay);
            List<Map<String, Object>> danhSach12Thang = new ArrayList<>();

            // Dùng vòng lặp for từ tháng 1 đến tháng 12 để đảm bảo tháng nào không có phiếu vẫn hiện số 0
            for (int m = 1; m <= 12; m++) {
                int soPhieuThang = 0;
                BigDecimal tongTienThang = BigDecimal.ZERO;

                // Tìm xem tháng m có trong kết quả truy vấn từ database hay không
                for (int i = 0; i < duLieuThang.size(); i++) {
                    ThongKeTheoThangProjection dong = duLieuThang.get(i);
                    if (dong.getThang() != null && dong.getThang() == m) {
                        soPhieuThang = dong.getSoPhieu();
                        tongTienThang = dong.getTongTien();
                        break;
                    }
                }

                Map<String, Object> mucThang = new LinkedHashMap<>();
                mucThang.put("thang", m);
                mucThang.put("soPhieu", soPhieuThang);
                mucThang.put("tongTien", tongTienThang);
                danhSach12Thang.add(mucThang);
            }

            ketQua.put("theoThang", danhSach12Thang);
        }

        return ResponseEntity.ok(ketQua);
    }
}
