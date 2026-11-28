package com.quanlykhov.api;

import com.quanlykhov.dao.TaiKhoanRepository;
import com.quanlykhov.dao.VaiTroRepository;
import com.quanlykhov.model.VaiTro;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/vai-tro")
@CrossOrigin(origins = "*")
public class VaiTroController {

    private final VaiTroRepository vaiTroRepository;
    private final TaiKhoanRepository taiKhoanRepository;

    public VaiTroController(VaiTroRepository vaiTroRepository, TaiKhoanRepository taiKhoanRepository) {
        this.vaiTroRepository = vaiTroRepository;
        this.taiKhoanRepository = taiKhoanRepository;
    }

    @GetMapping
    public List<VaiTro> getAllVaiTro() {
        return vaiTroRepository.findAll();
    }

    @GetMapping("/{maVT}")
    public ResponseEntity<?> getVaiTroById(@PathVariable String maVT) {
        Optional<VaiTro> ketQua = vaiTroRepository.findById(maVT);
        if (ketQua.isPresent()) {
            return ResponseEntity.ok(ketQua.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy vai trò có mã: " + maVT);
    }

    @PostMapping
    public ResponseEntity<?> createVaiTro(@RequestBody VaiTro vaiTro) {
        if (vaiTro.getMaVT() == null || vaiTro.getMaVT().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Mã vai trò không được để trống!");
        }
        String maVT = vaiTro.getMaVT().trim();
        if (vaiTroRepository.existsById(maVT)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Mã vai trò đã tồn tại!");
        }
        if (vaiTro.getTenVT() == null || vaiTro.getTenVT().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Tên vai trò không được để trống!");
        }
        if (vaiTroRepository.existsByTenVTIgnoreCase(vaiTro.getTenVT().trim())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Tên vai trò đã tồn tại!");
        }
        vaiTro.setMaVT(maVT);
        vaiTro.setTenVT(vaiTro.getTenVT().trim());
        if (vaiTro.getMoTa() != null) {
            vaiTro.setMoTa(vaiTro.getMoTa().trim());
        }
        VaiTro daLuu = vaiTroRepository.save(vaiTro);
        return ResponseEntity.status(HttpStatus.CREATED).body(daLuu);
    }

    @PutMapping("/{maVT}")
    public ResponseEntity<?> updateVaiTro(@PathVariable String maVT, @RequestBody VaiTro thongTinMoi) {
        Optional<VaiTro> ketQua = vaiTroRepository.findById(maVT);
        if (!ketQua.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy vai trò có mã: " + maVT);
        }
        if (thongTinMoi.getTenVT() == null || thongTinMoi.getTenVT().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Tên vai trò không được để trống!");
        }
        boolean trungTen = vaiTroRepository.findAll().stream()
                .anyMatch(v -> !v.getMaVT().equals(maVT) && v.getTenVT().equalsIgnoreCase(thongTinMoi.getTenVT().trim()));
        if (trungTen) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Tên vai trò đã tồn tại!");
        }
        VaiTro vaiTroHienTai = ketQua.get();
        vaiTroHienTai.setTenVT(thongTinMoi.getTenVT().trim());
        vaiTroHienTai.setMoTa(thongTinMoi.getMoTa() == null ? null : thongTinMoi.getMoTa().trim());
        VaiTro daCapNhat = vaiTroRepository.save(vaiTroHienTai);
        return ResponseEntity.ok(daCapNhat);
    }

    @DeleteMapping("/{maVT}")
    public ResponseEntity<?> deleteVaiTro(@PathVariable String maVT) {
        Optional<VaiTro> ketQua = vaiTroRepository.findById(maVT);
        if (!ketQua.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy vai trò có mã: " + maVT);
        }
        if (!taiKhoanRepository.findByMaVT(maVT).isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Không thể xoá vai trò đang được sử dụng.");
        }
        vaiTroRepository.deleteById(maVT);
        return ResponseEntity.noContent().build();
    }
}