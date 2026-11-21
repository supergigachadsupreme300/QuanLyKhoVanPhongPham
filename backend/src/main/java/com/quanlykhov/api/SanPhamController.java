package com.quanlykhov.api;

import com.quanlykhov.dao.SanPhamRepository;
import com.quanlykhov.model.SanPham;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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

    // Xử lý yêu cầu POST /api/san-pham: thêm sản phẩm mới
    @PostMapping
    public ResponseEntity<?> createSanPham(@RequestBody SanPham sanPhamMoi) {
        // Kiểm tra mã sản phẩm bắt buộc
        if (sanPhamMoi.getMaSP() == null || sanPhamMoi.getMaSP().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Mã sản phẩm không được để trống!");
        }

        // Kiểm tra dữ liệu đầu vào chung (tên, danh mục, giá bán, số lượng tối thiểu)
        String loiDuLieu = kiemTraDuLieuSanPham(sanPhamMoi);
        if (loiDuLieu != null) {
            return ResponseEntity.badRequest().body(loiDuLieu);
        }

        // Kiểm tra trùng mã sản phẩm (trả lỗi 409)
        if (sanPhamRepository.existsById(sanPhamMoi.getMaSP())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Mã sản phẩm đã tồn tại!");
        }

        // Nếu không gửi SLToiThieu thì mặc định là 10
        if (sanPhamMoi.getSlToiThieu() == null) {
            sanPhamMoi.setSlToiThieu(10);
        }

        // Sản phẩm mới luôn có tồn kho = 0 và trạng thái "Đang kinh doanh"
        sanPhamMoi.setSlTonKho(0);
        sanPhamMoi.setTrangThai("Đang kinh doanh");

        // Lưu vào cơ sở dữ liệu, bắt lỗi nếu mã danh mục/nhà cung cấp/kệ không tồn tại
        try {
            SanPham daLuu = sanPhamRepository.saveAndFlush(sanPhamMoi);
            return ResponseEntity.status(HttpStatus.CREATED).body(daLuu);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body("Mã danh mục, nhà cung cấp hoặc kệ không tồn tại!");
        }
    }

    // Xử lý yêu cầu PUT /api/san-pham/{maSP}: sửa thông tin sản phẩm
    @PutMapping("/{maSP}")
    public ResponseEntity<?> updateSanPham(@PathVariable String maSP, @RequestBody SanPham thongTinMoi) {
        // Tìm sản phẩm cần sửa theo mã
        Optional<SanPham> ketQua = sanPhamRepository.findById(maSP);
        if (!ketQua.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy sản phẩm có mã: " + maSP);
        }

        // Kiểm tra dữ liệu đầu vào chung (tên, danh mục, giá bán, số lượng tối thiểu)
        String loiDuLieu = kiemTraDuLieuSanPham(thongTinMoi);
        if (loiDuLieu != null) {
            return ResponseEntity.badRequest().body(loiDuLieu);
        }

        SanPham sanPhamHienTai = ketQua.get();

        // Chỉ cập nhật các trường được phép sửa (bỏ qua MaSP, SLTonKho, TrangThai)
        sanPhamHienTai.setTenSP(thongTinMoi.getTenSP());
        sanPhamHienTai.setDonViTinh(thongTinMoi.getDonViTinh());
        if (thongTinMoi.getSlToiThieu() == null) {
            sanPhamHienTai.setSlToiThieu(10);
        } else {
            sanPhamHienTai.setSlToiThieu(thongTinMoi.getSlToiThieu());
        }
        sanPhamHienTai.setGiaBan(thongTinMoi.getGiaBan());
        sanPhamHienTai.setMaDM(thongTinMoi.getMaDM());
        sanPhamHienTai.setMaNCC(thongTinMoi.getMaNCC());
        sanPhamHienTai.setMaKe(thongTinMoi.getMaKe());

        // Lưu thay đổi vào cơ sở dữ liệu, bắt lỗi nếu mã danh mục/nhà cung cấp/kệ không tồn tại
        try {
            SanPham daCapNhat = sanPhamRepository.saveAndFlush(sanPhamHienTai);
            return ResponseEntity.ok(daCapNhat);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body("Mã danh mục, nhà cung cấp hoặc kệ không tồn tại!");
        }
    }

    // Hàm phụ kiểm tra các quy tắc dữ liệu chung khi thêm và sửa sản phẩm
    private String kiemTraDuLieuSanPham(SanPham sp) {
        if (sp.getTenSP() == null || sp.getTenSP().trim().isEmpty()) {
            return "Tên sản phẩm không được để trống!";
        }
        if (sp.getMaDM() == null || sp.getMaDM().trim().isEmpty()) {
            return "Mã danh mục không được để trống!";
        }
        if (sp.getGiaBan() == null || sp.getGiaBan().compareTo(BigDecimal.ZERO) <= 0) {
            return "Giá bán phải lớn hơn 0!";
        }
        if (sp.getSlToiThieu() != null && sp.getSlToiThieu() < 0) {
            return "Số lượng tối thiểu không được âm!";
        }
        return null;
    }
}
