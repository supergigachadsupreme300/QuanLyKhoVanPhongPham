package com.quanlykhov.api;

import com.quanlykhov.dao.ChiTietPhieuKiemRepository;
import com.quanlykhov.dao.PhieuKiemKeRepository;
import com.quanlykhov.dao.SanPhamRepository;
import com.quanlykhov.dao.ThamGiaKiemKeRepository;
import com.quanlykhov.model.ChiTietPhieuKiem;
import com.quanlykhov.model.PhieuKiemKe;
import com.quanlykhov.model.SanPham;
import com.quanlykhov.model.ThamGiaKiemKe;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
    private final SanPhamRepository sanPhamRepository;

    // Hàm tạo để Spring truyền các Repository cần thiết vào sử dụng
    public PhieuKiemKeController(PhieuKiemKeRepository phieuKiemKeRepository,
                                 ChiTietPhieuKiemRepository chiTietPhieuKiemRepository,
                                 ThamGiaKiemKeRepository thamGiaKiemKeRepository,
                                 SanPhamRepository sanPhamRepository) {
        this.phieuKiemKeRepository = phieuKiemKeRepository;
        this.chiTietPhieuKiemRepository = chiTietPhieuKiemRepository;
        this.thamGiaKiemKeRepository = thamGiaKiemKeRepository;
        this.sanPhamRepository = sanPhamRepository;
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

        Map<String, Object> phanHoi = taoPhanHoiPhieuKiemKe(ketQua.get(), danhSachChiTiet, danhSachThamGia);
        return ResponseEntity.ok(phanHoi);
    }

    // Xử lý yêu cầu POST /api/phieu-kiem-ke: lập phiếu kiểm kê mới (chỉ ghi nhận chênh lệch, không sửa tồn kho)
    // @Transactional đảm bảo việc lưu phiếu kiểm kê và các dòng chi tiết xảy ra cùng lúc (hoặc cùng hủy bỏ nếu có lỗi)
    @Transactional
    @PostMapping
    public ResponseEntity<?> createPhieuKiemKe(@RequestBody(required = false) PhieuKiemKeRequest request) {
        // Bước 1: Kiểm tra thông tin chung của phiếu (ghi chú, danh sách chi tiết không rỗng)
        String loiPhieu = kiemTraThongTinPhieu(request);
        if (loiPhieu != null) {
            return ResponseEntity.badRequest().body(loiPhieu);
        }

        // Bước 2: Kiểm tra toàn bộ các dòng sản phẩm và lấy sẵn thông tin tồn kho từ bảng SanPham
        List<SanPham> danhSachSanPham = new ArrayList<>();
        String loiChiTiet = kiemTraVaLayDanhSachSanPham(request.getChiTiet(), danhSachSanPham);
        if (loiChiTiet != null) {
            return ResponseEntity.badRequest().body(loiChiTiet);
        }

        // Bước 3: Tự sinh mã phiếu kiểm kê mới (ví dụ PK02, PK03...)
        String maPKMoi = taoMaPhieuKiemKeMoi();

        // Bước 4: Lưu thông tin chung của phiếu kiểm kê (gán rõ ngày hiện tại và trạng thái Hoàn thành)
        PhieuKiemKe phieuMoi = new PhieuKiemKe();
        phieuMoi.setMaPK(maPKMoi);
        phieuMoi.setNgayKiem(LocalDateTime.now());
        phieuMoi.setTrangThai("Hoàn thành");
        phieuMoi.setGhiChu(chuanHoaChuoi(request.getGhiChu()));
        PhieuKiemKe phieuDaLuu = phieuKiemKeRepository.save(phieuMoi);

        // Bước 5: Lưu từng dòng chi tiết kiểm kê với SLSoSach lấy từ tồn kho hiện tại (không đổi SLTonKho)
        List<ChiTietPhieuKiem> danhSachChiTietDaLuu = luuDanhSachChiTiet(
                maPKMoi,
                request.getChiTiet(),
                danhSachSanPham
        );

        // Bước 6: Đóng gói kết quả trả về cùng cấu trúc với API xem chi tiết và mã trạng thái 201 Created
        List<ThamGiaKiemKe> danhSachThamGiaRong = new ArrayList<>();
        Map<String, Object> phanHoi = taoPhanHoiPhieuKiemKe(phieuDaLuu, danhSachChiTietDaLuu, danhSachThamGiaRong);
        return ResponseEntity.status(HttpStatus.CREATED).body(phanHoi);
    }

    // Kiểm tra thông tin chung của phiếu kiểm kê; trả về câu thông báo lỗi nếu sai, null nếu hợp lệ
    private String kiemTraThongTinPhieu(PhieuKiemKeRequest request) {
        if (request == null) {
            return "Dữ liệu phiếu kiểm kê không được để trống!";
        }
        String ghiChu = chuanHoaChuoi(request.getGhiChu());
        if (ghiChu != null && ghiChu.length() > 255) {
            return "Ghi chú tối đa 255 ký tự!";
        }
        if (request.getChiTiet() == null || request.getChiTiet().isEmpty()) {
            return "Phiếu kiểm kê phải có ít nhất 1 sản phẩm!";
        }
        return null;
    }

    // Kiểm tra từng dòng sản phẩm (trùng mã, số lượng thực tế, lý do, sản phẩm tồn tại) và gom SanPham vào ketQuaSanPham
    private String kiemTraVaLayDanhSachSanPham(List<ChiTietKiemRequest> danhSachChiTiet,
                                               List<SanPham> ketQuaSanPham) {
        List<String> dsMaSPDaGap = new ArrayList<>();

        for (int i = 0; i < danhSachChiTiet.size(); i++) {
            ChiTietKiemRequest dong = danhSachChiTiet.get(i);
            if (dong == null) {
                return "Thông tin dòng sản phẩm thứ " + (i + 1) + " không hợp lệ!";
            }
            if (dong.getMaSP() == null || dong.getMaSP().trim().isEmpty()) {
                return "Mã sản phẩm ở dòng thứ " + (i + 1) + " không được để trống!";
            }

            String maSP = dong.getMaSP().trim();
            if (dsMaSPDaGap.contains(maSP)) {
                return "Sản phẩm có mã " + maSP + " bị trùng lặp trong cùng một phiếu kiểm kê!";
            }
            dsMaSPDaGap.add(maSP);

            if (dong.getSlThucTe() == null || dong.getSlThucTe() < 0) {
                return "Số lượng thực tế của sản phẩm " + maSP + " phải lớn hơn hoặc bằng 0!";
            }

            String lyDo = chuanHoaChuoi(dong.getLyDo());
            if (lyDo != null && lyDo.length() > 255) {
                return "Lý do của sản phẩm " + maSP + " tối đa 255 ký tự!";
            }

            Optional<SanPham> spOpt = sanPhamRepository.findById(maSP);
            if (!spOpt.isPresent()) {
                return "Không tìm thấy sản phẩm có mã: " + maSP;
            }

            ketQuaSanPham.add(spOpt.get());
        }

        return null;
    }

    // Tự sinh mã phiếu kiểm kê mới dạng PK02, PK03... dựa trên mã lớn nhất hiện có
    private String taoMaPhieuKiemKeMoi() {
        List<PhieuKiemKe> tatCaPhieu = phieuKiemKeRepository.findAll();
        int soLonNhat = 0;

        for (int i = 0; i < tatCaPhieu.size(); i++) {
            String ma = tatCaPhieu.get(i).getMaPK();
            if (ma != null && ma.startsWith("PK") && ma.length() > 2) {
                String phanSo = ma.substring(2);
                try {
                    int so = Integer.parseInt(phanSo);
                    if (so > soLonNhat) {
                        soLonNhat = so;
                    }
                } catch (NumberFormatException e) {
                    // Bỏ qua các mã không đúng dạng PK + chữ số
                }
            }
        }

        int soMoi = soLonNhat + 1;
        if (soMoi < 10) {
            return "PK0" + soMoi;
        }
        return "PK" + soMoi;
    }

    // Lưu từng dòng chi tiết kiểm kê với SLSoSach lấy từ tồn kho hiện tại, tuyệt đối không sửa SLTonKho
    private List<ChiTietPhieuKiem> luuDanhSachChiTiet(String maPK,
                                                      List<ChiTietKiemRequest> danhSachChiTiet,
                                                      List<SanPham> danhSachSanPham) {
        List<ChiTietPhieuKiem> ketQua = new ArrayList<>();

        for (int i = 0; i < danhSachChiTiet.size(); i++) {
            ChiTietKiemRequest dong = danhSachChiTiet.get(i);
            SanPham sp = danhSachSanPham.get(i);

            int tonSoSach = 0;
            if (sp.getSlTonKho() != null) {
                tonSoSach = sp.getSlTonKho();
            }

            ChiTietPhieuKiem chiTietMoi = new ChiTietPhieuKiem();
            chiTietMoi.setMaPK(maPK);
            chiTietMoi.setMaSP(sp.getMaSP());
            chiTietMoi.setSlSoSach(tonSoSach);
            chiTietMoi.setSlThucTe(dong.getSlThucTe());
            chiTietMoi.setLyDo(chuanHoaChuoi(dong.getLyDo()));

            ChiTietPhieuKiem daLuu = chiTietPhieuKiemRepository.save(chiTietMoi);
            ketQua.add(daLuu);
        }

        return ketQua;
    }

    // Đóng gói JSON trả về gồm thông tin phiếu, danh sách chi tiết (có chênh lệch) và danh sách tham gia
    private Map<String, Object> taoPhanHoiPhieuKiemKe(PhieuKiemKe phieuKiemKe,
                                                      List<ChiTietPhieuKiem> danhSachChiTiet,
                                                      List<ThamGiaKiemKe> danhSachThamGia) {
        List<Map<String, Object>> chiTietTraVe = taoDanhSachChiTietTraVe(danhSachChiTiet);
        List<Map<String, Object>> thamGiaTraVe = taoDanhSachThamGiaTraVe(danhSachThamGia);

        // LinkedHashMap là bảng ghép cặp khóa-giá trị giữ đúng thứ tự để đóng gói JSON gồm phieuKiemKe, chiTiet và thamGia
        Map<String, Object> phanHoi = new LinkedHashMap<>();
        phanHoi.put("phieuKiemKe", phieuKiemKe);
        phanHoi.put("chiTiet", chiTietTraVe);
        phanHoi.put("thamGia", thamGiaTraVe);

        return phanHoi;
    }

    // Cắt khoảng trắng hai đầu chuỗi, trả về null nếu chuỗi rỗng
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
