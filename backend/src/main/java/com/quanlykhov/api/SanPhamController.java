package com.quanlykhov.api;

import com.quanlykhov.dao.SanPhamRepository;
import com.quanlykhov.model.SanPham;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

// Đánh dấu lớp này là nơi tiếp nhận các yêu cầu HTTP và trả kết quả dạng JSON
@RestController
// Đặt đường dẫn gốc cho các API sản phẩm là /api/san-pham
@RequestMapping("/api/san-pham")
// Cho phép giao diện từ nguồn khác gọi vào API này
@CrossOrigin(origins = "*")
public class SanPhamController {

    private final SanPhamRepository sanPhamRepository;

    // Hàm tạo để Spring tự động truyền SanPhamRepository vào sử dụng
    public SanPhamController(SanPhamRepository sanPhamRepository) {
        this.sanPhamRepository = sanPhamRepository;
    }

    // Xử lý yêu cầu GET /api/san-pham: trả về danh sách tất cả sản phẩm
    @GetMapping
    public List<SanPham> getAllSanPham() {
        return sanPhamRepository.findAll();
    }

    // Xử lý yêu cầu GET /api/san-pham/{maSP}: trả về 1 sản phẩm theo mã, không có thì trả lỗi 404
    @GetMapping("/{maSP}")
    public ResponseEntity<SanPham> getSanPhamById(@PathVariable String maSP) {
        Optional<SanPham> ketQua = sanPhamRepository.findById(maSP);
        if (ketQua.isPresent()) {
            return ResponseEntity.ok(ketQua.get());
        }
        return ResponseEntity.notFound().build();
    }
}
