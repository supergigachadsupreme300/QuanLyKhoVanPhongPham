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

    // Xử lý yêu cầu POST /api/nha-cung-cap: thêm nhà cung cấp mới
    @PostMapping
    public ResponseEntity<?> createNhaCC(@RequestBody NhaCC nhaCCMoi) {
        // Kiểm tra mã nhà cung cấp bắt buộc
        if (nhaCCMoi.getMaNCC() == null || nhaCCMoi.getMaNCC().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Mã nhà cung cấp không được để trống!");
        }

        // Kiểm tra dữ liệu đầu vào chung (tên nhà cung cấp, số điện thoại)
        String loiDuLieu = kiemTraDuLieuNhaCC(nhaCCMoi);
        if (loiDuLieu != null) {
            return ResponseEntity.badRequest().body(loiDuLieu);
        }

        // Kiểm tra trùng mã nhà cung cấp (trả lỗi 409)
        if (nhaCCRepository.existsById(nhaCCMoi.getMaNCC())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Mã nhà cung cấp đã tồn tại!");
        }

        // Nhà cung cấp mới luôn có trạng thái "Hoạt động"
        nhaCCMoi.setTrangThai("Hoạt động");

        NhaCC daLuu = nhaCCRepository.save(nhaCCMoi);
        return ResponseEntity.status(HttpStatus.CREATED).body(daLuu);
    }

    // Xử lý yêu cầu PUT /api/nha-cung-cap/{maNCC}: sửa thông tin nhà cung cấp
    @PutMapping("/{maNCC}")
    public ResponseEntity<?> updateNhaCC(@PathVariable String maNCC, @RequestBody NhaCC thongTinMoi) {
        // Tìm nhà cung cấp cần sửa theo mã
        Optional<NhaCC> ketQua = nhaCCRepository.findById(maNCC);
        if (!ketQua.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy nhà cung cấp có mã: " + maNCC);
        }

        // Kiểm tra dữ liệu đầu vào chung (tên nhà cung cấp, số điện thoại)
        String loiDuLieu = kiemTraDuLieuNhaCC(thongTinMoi);
        if (loiDuLieu != null) {
            return ResponseEntity.badRequest().body(loiDuLieu);
        }

        NhaCC nhaCCHienTai = ketQua.get();

        // Chỉ cập nhật các trường được phép sửa (bỏ qua MaNCC và TrangThai)
        nhaCCHienTai.setTenNCC(thongTinMoi.getTenNCC());
        nhaCCHienTai.setDiaChi(thongTinMoi.getDiaChi());
        nhaCCHienTai.setSdt(thongTinMoi.getSdt());

        NhaCC daCapNhat = nhaCCRepository.save(nhaCCHienTai);
        return ResponseEntity.ok(daCapNhat);
    }

    // Xử lý yêu cầu DELETE /api/nha-cung-cap/{maNCC}: xóa mềm nhà cung cấp (đổi trạng thái sang "Ngừng hợp tác")
    @DeleteMapping("/{maNCC}")
    public ResponseEntity<?> deleteNhaCC(@PathVariable String maNCC) {
        Optional<NhaCC> ketQua = nhaCCRepository.findById(maNCC);
        if (!ketQua.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy nhà cung cấp có mã: " + maNCC);
        }

        NhaCC nhaCCHienTai = ketQua.get();
        if ("Ngừng hợp tác".equals(nhaCCHienTai.getTrangThai())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Nhà cung cấp đã ở trạng thái Ngừng hợp tác!");
        }

        nhaCCHienTai.setTrangThai("Ngừng hợp tác");
        NhaCC daLuu = nhaCCRepository.save(nhaCCHienTai);
        return ResponseEntity.ok(daLuu);
    }

    // Xử lý yêu cầu PUT /api/nha-cung-cap/{maNCC}/khoi-phuc: khôi phục nhà cung cấp về trạng thái "Hoạt động"
    @PutMapping("/{maNCC}/khoi-phuc")
    public ResponseEntity<?> restoreNhaCC(@PathVariable String maNCC) {
        Optional<NhaCC> ketQua = nhaCCRepository.findById(maNCC);
        if (!ketQua.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy nhà cung cấp có mã: " + maNCC);
        }

        NhaCC nhaCCHienTai = ketQua.get();
        if ("Hoạt động".equals(nhaCCHienTai.getTrangThai())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Nhà cung cấp đang ở trạng thái Hoạt động!");
        }

        nhaCCHienTai.setTrangThai("Hoạt động");
        NhaCC daLuu = nhaCCRepository.save(nhaCCHienTai);
        return ResponseEntity.ok(daLuu);
    }

    // Hàm phụ kiểm tra các quy tắc dữ liệu chung khi thêm và sửa nhà cung cấp
    private String kiemTraDuLieuNhaCC(NhaCC ncc) {
        if (ncc.getTenNCC() == null || ncc.getTenNCC().trim().isEmpty()) {
            return "Tên nhà cung cấp không được để trống!";
        }
        String sdt = ncc.getSdt();
        if (sdt != null && !sdt.isEmpty()) {
            if (sdt.length() > 15) {
                return "Số điện thoại không được quá 15 ký tự!";
            }
            for (int i = 0; i < sdt.length(); i++) {
                char kyTu = sdt.charAt(i);
                if (kyTu < '0' || kyTu > '9') {
                    return "Số điện thoại chỉ được chứa chữ số!";
                }
            }
        }
        return null;
    }
}
