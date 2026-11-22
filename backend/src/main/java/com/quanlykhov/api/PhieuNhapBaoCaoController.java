package com.quanlykhov.api;

import com.quanlykhov.dao.PhieuNhapBaoCaoRepository;
import com.quanlykhov.dao.PhieuNhapBaoCaoRepository.LichSuPhieuNhapProjection;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

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

    // Xử lý yêu cầu GET /api/phieu-nhap/lich-su: xem lịch sử phiếu nhập kho theo năm và tháng (tùy chọn)
    @GetMapping("/lich-su")
    public ResponseEntity<?> xemLichSuPhieuNhap(
            @RequestParam(value = "nam", required = false) String namStr,
            @RequestParam(value = "thang", required = false) String thangStr) {

        // Bước 1: Kiểm tra tham số nam (bắt buộc phải gửi, phải là số từ 2000 đến 2100)
        if (namStr == null || namStr.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Vui lòng nhập tham số năm (nam)!");
        }

        int nam = 0;
        try {
            nam = Integer.parseInt(namStr.trim());
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Tham số năm (nam) phải là một con số hợp lệ!");
        }

        if (nam < 2000 || nam > 2100) {
            return ResponseEntity.badRequest().body("Năm (nam) phải nằm trong khoảng từ 2000 đến 2100!");
        }

        // Bước 2: Kiểm tra tham số thang nếu người dùng có gửi (phải là số từ 1 đến 12)
        Integer thang = null;
        if (thangStr != null && !thangStr.trim().isEmpty()) {
            try {
                thang = Integer.parseInt(thangStr.trim());
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().body("Tham số tháng (thang) phải là một con số hợp lệ!");
            }

            if (thang < 1 || thang > 12) {
                return ResponseEntity.badRequest().body("Tháng (thang) phải nằm trong khoảng từ 1 đến 12!");
            }
        }

        // Bước 3: Tính khoảng thời gian [tuNgay, denNgay) để truyền vào câu truy vấn SQL
        LocalDateTime tuNgay = null;
        LocalDateTime denNgay = null;

        if (thang == null) {
            // Nếu chỉ chọn cả năm: tính từ 00:00:00 ngày 1/1 năm đó đến 00:00:00 ngày 1/1 năm sau
            tuNgay = LocalDateTime.of(nam, 1, 1, 0, 0, 0);
            denNgay = LocalDateTime.of(nam + 1, 1, 1, 0, 0, 0);
        } else {
            // Nếu chọn thêm tháng: tính từ 00:00:00 ngày 1 của tháng đó đến 00:00:00 ngày 1 của tháng kế tiếp
            tuNgay = LocalDateTime.of(nam, thang, 1, 0, 0, 0);
            if (thang == 12) {
                // Tháng kế tiếp của tháng 12 là tháng 1 của năm sau
                denNgay = LocalDateTime.of(nam + 1, 1, 1, 0, 0, 0);
            } else {
                denNgay = LocalDateTime.of(nam, thang + 1, 1, 0, 0, 0);
            }
        }

        // Bước 4: Gọi Repository thực hiện truy vấn và trả kết quả về
        List<LichSuPhieuNhapProjection> danhSach = phieuNhapBaoCaoRepository.layLichSuPhieuNhap(tuNgay, denNgay);
        return ResponseEntity.ok(danhSach);
    }
}
