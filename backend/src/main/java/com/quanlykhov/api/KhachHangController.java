package com.quanlykhov.api;

import com.quanlykhov.dao.KhachHangRepository;
import com.quanlykhov.model.KhachHang;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

// Đánh dấu lớp này tiếp nhận các yêu cầu HTTP và trả về dữ liệu JSON
@RestController
// Đường dẫn gốc cho API quản lý khách hàng
@RequestMapping("/api/khach-hang")
// Cho phép giao diện từ nguồn khác gọi vào API này
@CrossOrigin(origins = "*")
public class KhachHangController {

    private final KhachHangRepository khachHangRepository;

    // Khởi tạo controller và nhận KhachHangRepository từ Spring
    public KhachHangController(KhachHangRepository khachHangRepository) {
        this.khachHangRepository = khachHangRepository;
    }

    // Lấy danh sách tất cả khách hàng trong database (GET /api/khach-hang)
    @GetMapping
    public List<KhachHang> getAllKhachHang() {
        return khachHangRepository.findAll();
    }

    // Tìm khách hàng theo tên hoặc số điện thoại, từ khóa trống thì trả tất cả (GET /api/khach-hang/tim?tuKhoa=...)
    @GetMapping("/tim")
    public List<KhachHang> searchKhachHang(@RequestParam(required = false) String tuKhoa) {
        if (tuKhoa == null || tuKhoa.trim().isEmpty()) {
            return khachHangRepository.findAll();
        }
        String khoa = tuKhoa.trim();
        return khachHangRepository.findByTenKHContainingIgnoreCaseOrSdtContainingIgnoreCase(khoa, khoa);
    }

    // Lấy một khách hàng theo mã, không tìm thấy thì trả về lỗi 404 (GET /api/khach-hang/{maKH})
    @GetMapping("/{maKH}")
    public ResponseEntity<?> getKhachHangById(@PathVariable String maKH) {
        Optional<KhachHang> ketQua = khachHangRepository.findById(maKH);
        if (ketQua.isPresent()) {
            return ResponseEntity.ok(ketQua.get());
        }
        return ResponseEntity.status(404).body("Không tìm thấy khách hàng có mã: " + maKH);
    }

    // Thêm khách hàng mới sau khi kiểm tra mã, tên và số điện thoại hợp lệ (POST /api/khach-hang)
    @PostMapping
    public ResponseEntity<?> createKhachHang(@RequestBody KhachHang khachHang) {
        if (khachHang.getMaKH() == null || khachHang.getMaKH().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Mã khách hàng không được để trống!");
        }
        String maKH = khachHang.getMaKH().trim();
        if (khachHangRepository.existsById(maKH)) {
            return ResponseEntity.badRequest().body("Mã khách hàng đã tồn tại!");
        }
        String loi = kiemTraTenVaSdt(khachHang);
        if (loi != null) {
            return ResponseEntity.badRequest().body(loi);
        }
        khachHang.setMaKH(maKH);
        khachHang.setTenKH(khachHang.getTenKH().trim());
        khachHang.setSdt(chuanHoaChuoi(khachHang.getSdt()));
        khachHang.setDiaChi(chuanHoaChuoi(khachHang.getDiaChi()));
        KhachHang daLuu = khachHangRepository.save(khachHang);
        return ResponseEntity.ok(daLuu);
    }

    // Sửa tên, số điện thoại, địa chỉ của khách hàng theo mã, không cho đổi mã cũ (PUT /api/khach-hang/{maKH})
    @PutMapping("/{maKH}")
    public ResponseEntity<?> updateKhachHang(@PathVariable String maKH, @RequestBody KhachHang thongTinMoi) {
        Optional<KhachHang> ketQua = khachHangRepository.findById(maKH);
        if (!ketQua.isPresent()) {
            return ResponseEntity.status(404).body("Không tìm thấy khách hàng có mã: " + maKH);
        }
        String loi = kiemTraTenVaSdt(thongTinMoi);
        if (loi != null) {
            return ResponseEntity.badRequest().body(loi);
        }
        KhachHang khachHangHienTai = ketQua.get();
        khachHangHienTai.setTenKH(thongTinMoi.getTenKH().trim());
        khachHangHienTai.setSdt(chuanHoaChuoi(thongTinMoi.getSdt()));
        khachHangHienTai.setDiaChi(chuanHoaChuoi(thongTinMoi.getDiaChi()));
        KhachHang daCapNhat = khachHangRepository.save(khachHangHienTai);
        return ResponseEntity.ok(daCapNhat);
    }

    // Kiểm tra tên khách hàng không trống và số điện thoại hợp lệ, trả về câu lỗi nếu sai
    private String kiemTraTenVaSdt(KhachHang khachHang) {
        if (khachHang.getTenKH() == null || khachHang.getTenKH().trim().isEmpty()) {
            return "Tên khách hàng không được để trống!";
        }
        if (khachHang.getSdt() != null) {
            String sdt = khachHang.getSdt().trim();
            if (!sdt.isEmpty()) {
                if (sdt.length() > 15) {
                    return "Số điện thoại tối đa 15 ký tự!";
                }
                for (int i = 0; i < sdt.length(); i++) {
                    char kyTu = sdt.charAt(i);
                    if (kyTu < '0' || kyTu > '9') {
                        return "Số điện thoại chỉ được chứa chữ số!";
                    }
                }
            }
        }
        return null;
    }

    // Cắt khoảng trắng hai đầu chuỗi, trả về null nếu chuỗi bị trống
    private String chuanHoaChuoi(String giaTri) {
        if (giaTri == null) {
            return null;
        }
        String ketQua = giaTri.trim();
        if (ketQua.isEmpty()) {
            return null;
        }
        return ketQua;
    }
}
