package com.quanlykhov.api;

import com.quanlykhov.dao.DanhMucRepository;
import com.quanlykhov.model.DanhMuc;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

// Đánh dấu lớp này tiếp nhận các yêu cầu HTTP và trả về dữ liệu JSON
@RestController
// Đường dẫn gốc cho API quản lý danh mục
@RequestMapping("/api/danh-muc")
// Cho phép giao diện từ nguồn khác gọi vào API này
@CrossOrigin(origins = "*")
public class DanhMucController {

    private final DanhMucRepository danhMucRepository;

    // Khởi tạo controller và nhận DanhMucRepository từ Spring
    public DanhMucController(DanhMucRepository danhMucRepository) {
        this.danhMucRepository = danhMucRepository;
    }

    // Lấy danh sách tất cả danh mục trong database (GET /api/danh-muc)
    @GetMapping
    public List<DanhMuc> getAllDanhMuc() {
        return danhMucRepository.findAll();
    }

    // Lấy một danh mục theo mã, không tìm thấy thì trả về lỗi 404 (GET /api/danh-muc/{maDM})
    @GetMapping("/{maDM}")
    public ResponseEntity<?> getDanhMucById(@PathVariable String maDM) {
        Optional<DanhMuc> ketQua = danhMucRepository.findById(maDM);
        if (ketQua.isPresent()) {
            return ResponseEntity.ok(ketQua.get());
        }
        return ResponseEntity.status(404).body("Không tìm thấy danh mục có mã: " + maDM);
    }

    // Thêm danh mục mới sau khi kiểm tra mã và tên hợp lệ (POST /api/danh-muc)
    @PostMapping
    public ResponseEntity<?> createDanhMuc(@RequestBody DanhMuc danhMuc) {
        if (danhMuc.getMaDM() == null || danhMuc.getMaDM().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Mã danh mục không được để trống!");
        }
        String maDM = danhMuc.getMaDM().trim();
        if (danhMucRepository.existsById(maDM)) {
            return ResponseEntity.badRequest().body("Mã danh mục đã tồn tại!");
        }
        String loi = kiemTraTenDanhMuc(danhMuc);
        if (loi != null) {
            return ResponseEntity.badRequest().body(loi);
        }
        danhMuc.setMaDM(maDM);
        danhMuc.setTenDM(danhMuc.getTenDM().trim());
        DanhMuc daLuu = danhMucRepository.save(danhMuc);
        return ResponseEntity.ok(daLuu);
    }

    // Sửa tên danh mục theo mã, không cho đổi mã cũ (PUT /api/danh-muc/{maDM})
    @PutMapping("/{maDM}")
    public ResponseEntity<?> updateDanhMuc(@PathVariable String maDM, @RequestBody DanhMuc thongTinMoi) {
        Optional<DanhMuc> ketQua = danhMucRepository.findById(maDM);
        if (!ketQua.isPresent()) {
            return ResponseEntity.status(404).body("Không tìm thấy danh mục có mã: " + maDM);
        }
        String loi = kiemTraTenDanhMuc(thongTinMoi);
        if (loi != null) {
            return ResponseEntity.badRequest().body(loi);
        }
        DanhMuc danhMucHienTai = ketQua.get();
        danhMucHienTai.setTenDM(thongTinMoi.getTenDM().trim());
        DanhMuc daCapNhat = danhMucRepository.save(danhMucHienTai);
        return ResponseEntity.ok(daCapNhat);
    }

    // Kiểm tra tên danh mục có bị trống không, trả về thông báo lỗi nếu sai
    private String kiemTraTenDanhMuc(DanhMuc danhMuc) {
        if (danhMuc.getTenDM() == null || danhMuc.getTenDM().trim().isEmpty()) {
            return "Tên danh mục không được để trống!";
        }
        return null;
    }
}
