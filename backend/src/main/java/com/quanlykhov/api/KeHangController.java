package com.quanlykhov.api;

import com.quanlykhov.dao.KeHangRepository;
import com.quanlykhov.model.KeHang;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

// Đánh dấu lớp này tiếp nhận các yêu cầu HTTP và trả về dữ liệu JSON
@RestController
// Đường dẫn gốc cho API quản lý kệ hàng
@RequestMapping("/api/ke-hang")
// Cho phép giao diện từ nguồn khác gọi vào API này
@CrossOrigin(origins = "*")
public class KeHangController {

    private final KeHangRepository keHangRepository;

    // Khởi tạo controller và nhận KeHangRepository từ Spring
    public KeHangController(KeHangRepository keHangRepository) {
        this.keHangRepository = keHangRepository;
    }

    // Lấy danh sách tất cả kệ hàng trong database (GET /api/ke-hang)
    @GetMapping
    public List<KeHang> getAllKeHang() {
        return keHangRepository.findAll();
    }

    // Lấy một kệ hàng theo mã, không tìm thấy thì trả về lỗi 404 (GET /api/ke-hang/{maKe})
    @GetMapping("/{maKe}")
    public ResponseEntity<?> getKeHangById(@PathVariable String maKe) {
        Optional<KeHang> ketQua = keHangRepository.findById(maKe);
        if (ketQua.isPresent()) {
            return ResponseEntity.ok(ketQua.get());
        }
        return ResponseEntity.status(404).body("Không tìm thấy kệ hàng có mã: " + maKe);
    }

    // Thêm kệ hàng mới sau khi kiểm tra mã, tên và sức chứa hợp lệ (POST /api/ke-hang)
    @PostMapping
    public ResponseEntity<?> createKeHang(@RequestBody KeHang keHang) {
        if (keHang.getMaKe() == null || keHang.getMaKe().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Mã kệ hàng không được để trống!");
        }
        String maKe = keHang.getMaKe().trim();
        if (keHangRepository.existsById(maKe)) {
            return ResponseEntity.badRequest().body("Mã kệ hàng đã tồn tại!");
        }
        String loi = kiemTraTenVaSucChua(keHang);
        if (loi != null) {
            return ResponseEntity.badRequest().body(loi);
        }
        keHang.setMaKe(maKe);
        keHang.setTenKe(keHang.getTenKe().trim());
        KeHang daLuu = keHangRepository.save(keHang);
        return ResponseEntity.ok(daLuu);
    }

    // Sửa tên và sức chứa của kệ hàng theo mã, không cho đổi mã cũ (PUT /api/ke-hang/{maKe})
    @PutMapping("/{maKe}")
    public ResponseEntity<?> updateKeHang(@PathVariable String maKe, @RequestBody KeHang thongTinMoi) {
        Optional<KeHang> ketQua = keHangRepository.findById(maKe);
        if (!ketQua.isPresent()) {
            return ResponseEntity.status(404).body("Không tìm thấy kệ hàng có mã: " + maKe);
        }
        String loi = kiemTraTenVaSucChua(thongTinMoi);
        if (loi != null) {
            return ResponseEntity.badRequest().body(loi);
        }
        KeHang keHangHienTai = ketQua.get();
        keHangHienTai.setTenKe(thongTinMoi.getTenKe().trim());
        keHangHienTai.setSucChuaToiDa(thongTinMoi.getSucChuaToiDa());
        KeHang daCapNhat = keHangRepository.save(keHangHienTai);
        return ResponseEntity.ok(daCapNhat);
    }

    // Kiểm tra tên kệ và sức chứa tối đa có hợp lệ không, trả về thông báo lỗi nếu sai
    private String kiemTraTenVaSucChua(KeHang keHang) {
        if (keHang.getTenKe() == null || keHang.getTenKe().trim().isEmpty()) {
            return "Tên kệ hàng không được để trống!";
        }
        if (keHang.getSucChuaToiDa() == null || keHang.getSucChuaToiDa() <= 0) {
            return "Sức chứa tối đa phải lớn hơn 0!";
        }
        return null;
    }
}
