package com.quanlykhov.api;

import com.quanlykhov.dao.NhaCCRepository;
import com.quanlykhov.model.NhaCC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

// Đánh dấu lớp này là nơi tiếp nhận các yêu cầu HTTP và trả kết quả dạng JSON
@RestController
// Đặt đường dẫn gốc cho các API nhà cung cấp là /api/nha-cung-cap
@RequestMapping("/api/nha-cung-cap")
// Cho phép giao diện từ nguồn khác gọi vào API này
@CrossOrigin(origins = "*")
public class NhaCCController {

    private final NhaCCRepository nhaCCRepository;

    // Hàm tạo để Spring tự động truyền NhaCCRepository vào sử dụng
    public NhaCCController(NhaCCRepository nhaCCRepository) {
        this.nhaCCRepository = nhaCCRepository;
    }

    // Xử lý yêu cầu GET /api/nha-cung-cap: trả về danh sách tất cả nhà cung cấp
    @GetMapping
    public List<NhaCC> getAllNhaCC() {
        return nhaCCRepository.findAll();
    }

    // Xử lý yêu cầu GET /api/nha-cung-cap/{maNCC}: trả về 1 nhà cung cấp theo mã, không có thì trả lỗi 404
    @GetMapping("/{maNCC}")
    public ResponseEntity<?> getNhaCCById(@PathVariable String maNCC) {
        Optional<NhaCC> ketQua = nhaCCRepository.findById(maNCC);
        if (ketQua.isPresent()) {
            return ResponseEntity.ok(ketQua.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy nhà cung cấp có mã: " + maNCC);
    }
}
