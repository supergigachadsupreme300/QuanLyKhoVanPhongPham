package com.quanlykhov.api;

import com.quanlykhov.dao.TaiKhoanRepository;
import com.quanlykhov.dao.VaiTroRepository;
import com.quanlykhov.model.TaiKhoan;
import com.quanlykhov.model.VaiTro;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tai-khoan")
@CrossOrigin(origins = "*")
public class TaiKhoanController {

    private final TaiKhoanRepository taiKhoanRepository;
    private final VaiTroRepository vaiTroRepository;

    public TaiKhoanController(TaiKhoanRepository taiKhoanRepository, VaiTroRepository vaiTroRepository) {
        this.taiKhoanRepository = taiKhoanRepository;
        this.vaiTroRepository = vaiTroRepository;
    }

    @GetMapping
    public List<TaiKhoan> getAllTaiKhoan() {
        return taiKhoanRepository.findAll();
    }

    @GetMapping("/{maTK}")
    public ResponseEntity<?> getTaiKhoanById(@PathVariable String maTK) {
        Optional<TaiKhoan> ketQua = taiKhoanRepository.findById(maTK);
        if (ketQua.isPresent()) {
            return ResponseEntity.ok(ketQua.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy tài khoản có mã: " + maTK);
    }

    @PostMapping
    public ResponseEntity<?> createTaiKhoan(@RequestBody TaiKhoan taiKhoan) {
        if (taiKhoan.getMaTK() == null || taiKhoan.getMaTK().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Mã tài khoản không được để trống!");
        }
        String maTK = taiKhoan.getMaTK().trim();
        if (taiKhoanRepository.existsById(maTK)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Mã tài khoản đã tồn tại!");
        }
        String loi = kiemTraDuLieu(taiKhoan, null);
        if (loi != null) {
            return ResponseEntity.badRequest().body(loi);
        }
        taiKhoan.setMaTK(maTK);
        taiKhoan.setTenDangNhap(taiKhoan.getTenDangNhap().trim());
        if (taiKhoan.getTrangThai() == null || taiKhoan.getTrangThai().trim().isEmpty()) {
            taiKhoan.setTrangThai("Hoạt động");
        }
        TaiKhoan daLuu = taiKhoanRepository.save(taiKhoan);
        return ResponseEntity.status(HttpStatus.CREATED).body(daLuu);
    }

    @PutMapping("/{maTK}")
    public ResponseEntity<?> updateTaiKhoan(@PathVariable String maTK, @RequestBody TaiKhoan thongTinMoi) {
        Optional<TaiKhoan> ketQua = taiKhoanRepository.findById(maTK);
        if (!ketQua.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy tài khoản có mã: " + maTK);
        }
        String loi = kiemTraDuLieu(thongTinMoi, maTK);
        if (loi != null) {
            return ResponseEntity.badRequest().body(loi);
        }
        TaiKhoan hienTai = ketQua.get();
        hienTai.setTenDangNhap(thongTinMoi.getTenDangNhap().trim());
        if (thongTinMoi.getMatKhau() != null && !thongTinMoi.getMatKhau().trim().isEmpty()) {
            hienTai.setMatKhau(thongTinMoi.getMatKhau());
        }
        if (thongTinMoi.getTrangThai() != null && !thongTinMoi.getTrangThai().trim().isEmpty()) {
            hienTai.setTrangThai(thongTinMoi.getTrangThai().trim());
        }
        hienTai.setMaVT(thongTinMoi.getMaVT().trim());
        hienTai.setMaNV(thongTinMoi.getMaNV().trim());
        TaiKhoan daCapNhat = taiKhoanRepository.save(hienTai);
        return ResponseEntity.ok(daCapNhat);
    }

    @DeleteMapping("/{maTK}")
    public ResponseEntity<?> deleteTaiKhoan(@PathVariable String maTK) {
        Optional<TaiKhoan> ketQua = taiKhoanRepository.findById(maTK);
        if (!ketQua.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy tài khoản có mã: " + maTK);
        }
        taiKhoanRepository.deleteById(maTK);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{maTK}/khoa")
    public ResponseEntity<?> khoaTaiKhoan(@PathVariable String maTK) {
        Optional<TaiKhoan> ketQua = taiKhoanRepository.findById(maTK);
        if (!ketQua.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy tài khoản có mã: " + maTK);
        }
        TaiKhoan tk = ketQua.get();
        tk.setTrangThai("Khoá");
        return ResponseEntity.ok(taiKhoanRepository.save(tk));
    }

    @PutMapping("/{maTK}/mo-khoa")
    public ResponseEntity<?> moKhoaTaiKhoan(@PathVariable String maTK) {
        Optional<TaiKhoan> ketQua = taiKhoanRepository.findById(maTK);
        if (!ketQua.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy tài khoản có mã: " + maTK);
        }
        TaiKhoan tk = ketQua.get();
        tk.setTrangThai("Hoạt động");
        return ResponseEntity.ok(taiKhoanRepository.save(tk));
    }

    private String kiemTraDuLieu(TaiKhoan tk, String maTKHienTai) {
        if (tk.getTenDangNhap() == null || tk.getTenDangNhap().trim().isEmpty()) {
            return "Tên đăng nhập không được để trống!";
        }
        boolean trungTen = taiKhoanRepository.findAll().stream()
                .anyMatch(t -> (maTKHienTai == null || !t.getMaTK().equals(maTKHienTai))
                        && t.getTenDangNhap().equalsIgnoreCase(tk.getTenDangNhap().trim()));
        if (trungTen) {
            return "Tên đăng nhập đã tồn tại!";
        }
        if (tk.getMatKhau() != null && !tk.getMatKhau().trim().isEmpty() && tk.getMatKhau().trim().length() < 6) {
            return "Mật khẩu phải có ít nhất 6 ký tự!";
        }
        if (maTKHienTai == null && (tk.getMatKhau() == null || tk.getMatKhau().trim().isEmpty())) {
            return "Mật khẩu không được để trống!";
        }
        if (tk.getMaVT() == null || tk.getMaVT().trim().isEmpty()) {
            return "Vai trò không được để trống!";
        }
        if (!vaiTroRepository.existsById(tk.getMaVT().trim())) {
            return "Vai trò không tồn tại!";
        }
        if (tk.getMaNV() == null || tk.getMaNV().trim().isEmpty()) {
            return "Nhân viên không được để trống!";
        }
        boolean trungNV = taiKhoanRepository.findAll().stream()
                .anyMatch(t -> (maTKHienTai == null || !t.getMaTK().equals(maTKHienTai))
                        && t.getMaNV().equalsIgnoreCase(tk.getMaNV().trim()));
        if (trungNV) {
            return "Nhân viên này đã có tài khoản!";
        }
        return null;
    }
}