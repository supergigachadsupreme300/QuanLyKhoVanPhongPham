package com.quanlykhov.api;

import com.quanlykhov.dao.ChiTietPhieuXuatRepository;
import com.quanlykhov.dao.PhieuXuatKhoRepository;
import com.quanlykhov.model.ChiTietPhieuXuat;
import com.quanlykhov.model.PhieuXuatKho;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// Đánh dấu lớp này tiếp nhận các yêu cầu HTTP và trả về dữ liệu JSON
@RestController
// Đặt đường dẫn gốc cho các API phiếu xuất là /api/phieu-xuat
@RequestMapping("/api/phieu-xuat")
// Cho phép giao diện từ nguồn khác gọi vào API này
@CrossOrigin(origins = "*")
public class PhieuXuatController {

    private final PhieuXuatKhoRepository phieuXuatKhoRepository;
    private final ChiTietPhieuXuatRepository chiTietPhieuXuatRepository;

    // Hàm tạo để Spring truyền hai Repository phiếu xuất và chi tiết phiếu xuất vào sử dụng
    public PhieuXuatController(PhieuXuatKhoRepository phieuXuatKhoRepository,
                               ChiTietPhieuXuatRepository chiTietPhieuXuatRepository) {
        this.phieuXuatKhoRepository = phieuXuatKhoRepository;
        this.chiTietPhieuXuatRepository = chiTietPhieuXuatRepository;
    }

    // Xử lý yêu cầu GET /api/phieu-xuat: trả về danh sách tất cả phiếu xuất mới nhất lên đầu
    @GetMapping
    public List<PhieuXuatKho> getAllPhieuXuat() {
        return phieuXuatKhoRepository.findAllByOrderByNgayXuatDesc();
    }

    // Xử lý yêu cầu GET /api/phieu-xuat/{maPX}: trả về 1 phiếu xuất kèm danh sách chi tiết của phiếu đó
    @GetMapping("/{maPX}")
    public ResponseEntity<?> getPhieuXuatById(@PathVariable String maPX) {
        Optional<PhieuXuatKho> ketQua = phieuXuatKhoRepository.findById(maPX);
        if (!ketQua.isPresent()) {
            return ResponseEntity.status(404).body("Không tìm thấy phiếu xuất có mã: " + maPX);
        }

        List<ChiTietPhieuXuat> danhSachChiTiet = chiTietPhieuXuatRepository.findByMaPX(maPX);

        // LinkedHashMap là bảng ghép cặp khóa-giá trị giữ đúng thứ tự để đóng gói JSON gồm phieuXuat và chiTiet
        Map<String, Object> phanHoi = new LinkedHashMap<>();
        phanHoi.put("phieuXuat", ketQua.get());
        phanHoi.put("chiTiet", danhSachChiTiet);

        return ResponseEntity.ok(phanHoi);
    }
}
