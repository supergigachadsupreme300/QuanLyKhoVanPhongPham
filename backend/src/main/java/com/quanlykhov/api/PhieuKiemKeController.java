package com.quanlykhov.api;

import com.quanlykhov.dao.ChiTietPhieuKiemRepository;
import com.quanlykhov.dao.PhieuKiemKeRepository;
import com.quanlykhov.dao.ThamGiaKiemKeRepository;
import com.quanlykhov.model.ChiTietPhieuKiem;
import com.quanlykhov.model.PhieuKiemKe;
import com.quanlykhov.model.ThamGiaKiemKe;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// Đánh dấu lớp này tiếp nhận các yêu cầu HTTP và trả về dữ liệu JSON
@RestController
// Đặt đường dẫn gốc cho các API phiếu kiểm kê là /api/phieu-kiem-ke
@RequestMapping("/api/phieu-kiem-ke")
// Cho phép giao diện từ nguồn khác gọi vào API này
@CrossOrigin(origins = "*")
public class PhieuKiemKeController {

    private final PhieuKiemKeRepository phieuKiemKeRepository;
    private final ChiTietPhieuKiemRepository chiTietPhieuKiemRepository;
    private final ThamGiaKiemKeRepository thamGiaKiemKeRepository;

    // Hàm tạo để Spring truyền các Repository cần thiết vào sử dụng
    public PhieuKiemKeController(PhieuKiemKeRepository phieuKiemKeRepository,
                                 ChiTietPhieuKiemRepository chiTietPhieuKiemRepository,
                                 ThamGiaKiemKeRepository thamGiaKiemKeRepository) {
        this.phieuKiemKeRepository = phieuKiemKeRepository;
        this.chiTietPhieuKiemRepository = chiTietPhieuKiemRepository;
        this.thamGiaKiemKeRepository = thamGiaKiemKeRepository;
    }

    // Xử lý yêu cầu GET /api/phieu-kiem-ke: trả về danh sách tất cả phiếu kiểm kê mới nhất lên đầu
    @GetMapping
    public List<PhieuKiemKe> getAllPhieuKiemKe() {
        return phieuKiemKeRepository.findAllByOrderByNgayKiemDesc();
    }

    // Xử lý yêu cầu GET /api/phieu-kiem-ke/{maPK}: trả về thông tin 1 phiếu kiểm kê kèm chi tiết và người tham gia
    @GetMapping("/{maPK}")
    public ResponseEntity<?> getPhieuKiemKeById(@PathVariable String maPK) {
        Optional<PhieuKiemKe> ketQua = phieuKiemKeRepository.findById(maPK);
        if (!ketQua.isPresent()) {
            return ResponseEntity.status(404).body("Không tìm thấy phiếu kiểm kê có mã: " + maPK);
        }

        List<ChiTietPhieuKiem> danhSachChiTiet = chiTietPhieuKiemRepository.findByMaPK(maPK);
        List<ThamGiaKiemKe> danhSachThamGia = thamGiaKiemKeRepository.findByMaPK(maPK);

        List<Map<String, Object>> chiTietTraVe = taoDanhSachChiTietTraVe(danhSachChiTiet);
        List<Map<String, Object>> thamGiaTraVe = taoDanhSachThamGiaTraVe(danhSachThamGia);

        // LinkedHashMap là bảng ghép cặp khóa-giá trị giữ đúng thứ tự để đóng gói JSON gồm phieuKiemKe, chiTiet và thamGia
        Map<String, Object> phanHoi = new LinkedHashMap<>();
        phanHoi.put("phieuKiemKe", ketQua.get());
        phanHoi.put("chiTiet", chiTietTraVe);
        phanHoi.put("thamGia", thamGiaTraVe);

        return ResponseEntity.ok(phanHoi);
    }

    // Chuyển từng dòng chi tiết sang Map gồm maSP, slSoSach, slThucTe, lyDo và chenhLech (chỉ tính lúc trả về)
    private List<Map<String, Object>> taoDanhSachChiTietTraVe(List<ChiTietPhieuKiem> danhSachChiTiet) {
        List<Map<String, Object>> ketQua = new ArrayList<>();

        for (int i = 0; i < danhSachChiTiet.size(); i++) {
            ChiTietPhieuKiem dong = danhSachChiTiet.get(i);

            Integer chenhLech = null;
            if (dong.getSlThucTe() != null && dong.getSlSoSach() != null) {
                chenhLech = dong.getSlThucTe() - dong.getSlSoSach();
            }

            Map<String, Object> dongTraVe = new LinkedHashMap<>();
            dongTraVe.put("maSP", dong.getMaSP());
            dongTraVe.put("slSoSach", dong.getSlSoSach());
            dongTraVe.put("slThucTe", dong.getSlThucTe());
            dongTraVe.put("lyDo", dong.getLyDo());
            dongTraVe.put("chenhLech", chenhLech);

            ketQua.add(dongTraVe);
        }

        return ketQua;
    }

    // Chuyển từng dòng tham gia kiểm kê sang Map chỉ gồm maNV và vaiTroKiem
    private List<Map<String, Object>> taoDanhSachThamGiaTraVe(List<ThamGiaKiemKe> danhSachThamGia) {
        List<Map<String, Object>> ketQua = new ArrayList<>();

        for (int i = 0; i < danhSachThamGia.size(); i++) {
            ThamGiaKiemKe dong = danhSachThamGia.get(i);

            Map<String, Object> dongTraVe = new LinkedHashMap<>();
            dongTraVe.put("maNV", dong.getMaNV());
            dongTraVe.put("vaiTroKiem", dong.getVaiTroKiem());

            ketQua.add(dongTraVe);
        }

        return ketQua;
    }
}
