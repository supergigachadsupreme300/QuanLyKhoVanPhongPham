package com.quanlykhov.api;

import com.quanlykhov.dao.PhieuNhapKhoRepository;
import com.quanlykhov.model.PhieuNhapKho;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

// Đánh dấu lớp này là nơi tiếp nhận các yêu cầu HTTP và trả kết quả dạng JSON
@RestController
// Đặt đường dẫn gốc cho các API phiếu nhập kho là /api/phieu-nhap
@RequestMapping("/api/phieu-nhap")
// Cho phép giao diện từ nguồn khác gọi vào API này
@CrossOrigin(origins = "*")
public class PhieuNhapController {

    private final PhieuNhapKhoRepository phieuNhapKhoRepository;

    // Hàm tạo để Spring tự động truyền PhieuNhapKhoRepository vào sử dụng
    public PhieuNhapController(PhieuNhapKhoRepository phieuNhapKhoRepository) {
        this.phieuNhapKhoRepository = phieuNhapKhoRepository;
    }

    // Xử lý yêu cầu GET /api/phieu-nhap: trả về danh sách tất cả phiếu nhập (cả phiếu đã Hủy), phiếu mới nhất lên đầu
    @GetMapping
    public List<PhieuNhapKho> getAllPhieuNhap() {
        return phieuNhapKhoRepository.findAllByOrderByNgayNhapDesc();
    }

    // Xử lý yêu cầu GET /api/phieu-nhap/{maPN}: trả về 1 phiếu nhập theo mã, không có thì trả lỗi 404 kèm thông báo
    @GetMapping("/{maPN}")
    public ResponseEntity<?> getPhieuNhapById(@PathVariable("maPN") String maPN) {
        Optional<PhieuNhapKho> ketQua = phieuNhapKhoRepository.findById(maPN);
        if (ketQua.isPresent()) {
            return ResponseEntity.ok(ketQua.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy phiếu nhập có mã: " + maPN);
    }
}
