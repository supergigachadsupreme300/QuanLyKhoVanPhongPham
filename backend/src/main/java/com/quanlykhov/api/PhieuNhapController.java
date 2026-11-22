package com.quanlykhov.api;

import com.quanlykhov.dao.ChiTietPhieuNhapRepository;
import com.quanlykhov.dao.NhaCCRepository;
import com.quanlykhov.dao.PhieuNhapKhoRepository;
import com.quanlykhov.dao.SanPhamRepository;
import com.quanlykhov.model.ChiTietPhieuNhap;
import com.quanlykhov.model.NhaCC;
import com.quanlykhov.model.PhieuNhapKho;
import com.quanlykhov.model.SanPham;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// Đánh dấu lớp này là nơi tiếp nhận các yêu cầu HTTP và trả kết quả dạng JSON
@RestController
// Đặt đường dẫn gốc cho các API phiếu nhập kho là /api/phieu-nhap
@RequestMapping("/api/phieu-nhap")
// Cho phép giao diện từ nguồn khác gọi vào API này
@CrossOrigin(origins = "*")
public class PhieuNhapController {

    private final PhieuNhapKhoRepository phieuNhapKhoRepository;
    private final ChiTietPhieuNhapRepository chiTietPhieuNhapRepository;
    private final NhaCCRepository nhaCCRepository;
    private final SanPhamRepository sanPhamRepository;
    private final PhieuNhapService phieuNhapService;

    // Hàm tạo để Spring tự động truyền các Repository và Service vào sử dụng
    public PhieuNhapController(PhieuNhapKhoRepository phieuNhapKhoRepository,
                               ChiTietPhieuNhapRepository chiTietPhieuNhapRepository,
                               NhaCCRepository nhaCCRepository,
                               SanPhamRepository sanPhamRepository,
                               PhieuNhapService phieuNhapService) {
        this.phieuNhapKhoRepository = phieuNhapKhoRepository;
        this.chiTietPhieuNhapRepository = chiTietPhieuNhapRepository;
        this.nhaCCRepository = nhaCCRepository;
        this.sanPhamRepository = sanPhamRepository;
        this.phieuNhapService = phieuNhapService;
    }

    // Xử lý yêu cầu GET /api/phieu-nhap: trả về danh sách tất cả phiếu nhập (cả phiếu đã Hủy), phiếu mới nhất lên đầu
    @GetMapping
    public List<PhieuNhapKho> getAllPhieuNhap() {
        return phieuNhapKhoRepository.findAllByOrderByNgayNhapDesc();
    }

    // Xử lý yêu cầu GET /api/phieu-nhap/{maPN}: trả về 1 phiếu nhập theo mã, không có thì trả lỗi 404 kèm thông báo
    @GetMapping("/{maPN}")
    public ResponseEntity<?> getPhieuNhapById(@PathVariable("maPN") String maPN) {
        Optional<PhieuNhapKho> ketQua = phieuNhapKhoRepository.findById(maPN);
        if (ketQua.isPresent()) {
            return ResponseEntity.ok(ketQua.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy phiếu nhập có mã: " + maPN);
    }

    // Xử lý yêu cầu GET /api/phieu-nhap/{maPN}/chi-tiet: trả về danh sách các dòng chi tiết của 1 phiếu nhập
    @GetMapping("/{maPN}/chi-tiet")
    public ResponseEntity<?> getChiTietPhieuNhap(@PathVariable("maPN") String maPN) {
        // Kiểm tra phiếu nhập có tồn tại không, nếu không có thì trả lỗi 404
        if (!phieuNhapKhoRepository.existsById(maPN)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy phiếu nhập có mã: " + maPN);
        }

        // Lấy danh sách các dòng chi tiết của phiếu nhập này (nếu chưa có dòng nào thì trả danh sách rỗng)
        List<ChiTietPhieuNhap> danhSachChiTiet = chiTietPhieuNhapRepository.findByMaPN(maPN);
        return ResponseEntity.ok(danhSachChiTiet);
    }

    // Xử lý yêu cầu POST /api/phieu-nhap: lập phiếu nhập kho mới và tăng tồn kho sản phẩm
    @PostMapping
    public ResponseEntity<?> createPhieuNhap(@RequestBody PhieuNhapRequest yeuCau) {
        // Kiểm tra 1: maNV, maNCC không được rỗng và chiTiet phải có ít nhất 1 dòng
        if (yeuCau == null) {
            return ResponseEntity.badRequest().body("Dữ liệu phiếu nhập không được để trống!");
        }
        if (yeuCau.getMaNV() == null || yeuCau.getMaNV().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Mã nhân viên (maNV) không được để trống!");
        }
        if (yeuCau.getMaNCC() == null || yeuCau.getMaNCC().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Mã nhà cung cấp (maNCC) không được để trống!");
        }
        if (yeuCau.getChiTiet() == null || yeuCau.getChiTiet().isEmpty()) {
            return ResponseEntity.badRequest().body("Danh sách chi tiết phiếu nhập phải có ít nhất 1 dòng!");
        }

        // Kiểm tra 2: maNCC phải có trong database và không ở trạng thái "Ngừng hợp tác"
        String maNCC = yeuCau.getMaNCC().trim();
        Optional<NhaCC> ketQuaNCC = nhaCCRepository.findById(maNCC);
        if (!ketQuaNCC.isPresent()) {
            return ResponseEntity.badRequest().body("Mã nhà cung cấp không tồn tại: " + maNCC);
        }
        NhaCC nhaCC = ketQuaNCC.get();
        if ("Ngừng hợp tác".equals(nhaCC.getTrangThai())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Nhà cung cấp có mã " + maNCC + " đã ngừng hợp tác, không được lập phiếu nhập!");
        }

        // Kiểm tra 3: Kiểm tra toàn bộ các dòng chi tiết trước khi lưu
        List<String> danhSachMaSPDaCo = new ArrayList<>();
        List<ChiTietNhapRequest> danhSachChiTiet = yeuCau.getChiTiet();

        for (int i = 0; i < danhSachChiTiet.size(); i++) {
            ChiTietNhapRequest dong = danhSachChiTiet.get(i);
            if (dong == null || dong.getMaSP() == null || dong.getMaSP().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Mã sản phẩm (maSP) ở dòng chi tiết thứ " + (i + 1) + " không được để trống!");
            }

            String maSP = dong.getMaSP().trim();

            // Kiểm tra một maSP không được xuất hiện 2 lần trong cùng 1 phiếu
            for (int j = 0; j < danhSachMaSPDaCo.size(); j++) {
                if (danhSachMaSPDaCo.get(j).equals(maSP)) {
                    return ResponseEntity.badRequest().body("Mã sản phẩm " + maSP + " bị trùng lặp trong cùng một phiếu nhập!");
                }
            }
            danhSachMaSPDaCo.add(maSP);

            // Kiểm tra maSP phải có trong database
            Optional<SanPham> ketQuaSP = sanPhamRepository.findById(maSP);
            if (!ketQuaSP.isPresent()) {
                return ResponseEntity.badRequest().body("Mã sản phẩm không tồn tại: " + maSP);
            }

            // Kiểm tra sản phẩm không ở trạng thái "Ngừng kinh doanh"
            SanPham sanPham = ketQuaSP.get();
            if ("Ngừng kinh doanh".equals(sanPham.getTrangThai())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Sản phẩm có mã " + maSP + " đã ngừng kinh doanh, không được nhập kho!");
            }

            // Kiểm tra slThucNhap phải lớn hơn 0
            if (dong.getSlThucNhap() == null || dong.getSlThucNhap() <= 0) {
                return ResponseEntity.badRequest().body("Số lượng thực nhập (slThucNhap) của sản phẩm " + maSP + " phải lớn hơn 0!");
            }

            // Kiểm tra slTheoChungTu: nếu có gửi thì không được âm
            if (dong.getSlTheoChungTu() != null && dong.getSlTheoChungTu() < 0) {
                return ResponseEntity.badRequest().body("Số lượng theo chứng từ (slTheoChungTu) của sản phẩm " + maSP + " không được âm!");
            }

            // Kiểm tra giaNhap phải có và không âm
            if (dong.getGiaNhap() == null || dong.getGiaNhap().compareTo(BigDecimal.ZERO) < 0) {
                return ResponseEntity.badRequest().body("Giá nhập (giaNhap) của sản phẩm " + maSP + " phải có và không được âm!");
            }
        }

        // Sau khi kiểm tra hợp lệ toàn bộ, gọi Service để lưu phiếu và cập nhật tồn kho
        try {
            PhieuNhapKho phieuMoi = phieuNhapService.lapPhieuNhap(yeuCau);
            return ResponseEntity.status(HttpStatus.CREATED).body(phieuMoi);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body("Mã nhân viên không tồn tại");
        }
    }

    // Xử lý yêu cầu PUT /api/phieu-nhap/{maPN}/huy: hủy phiếu nhập kho và trừ lại tồn kho sản phẩm
    @PutMapping("/{maPN}/huy")
    public ResponseEntity<?> huyPhieuNhap(@PathVariable("maPN") String maPN) {
        // Kiểm tra 1: Tìm phiếu nhập theo mã, nếu không thấy thì trả lỗi 404
        Optional<PhieuNhapKho> ketQuaPhieu = phieuNhapKhoRepository.findById(maPN);
        if (!ketQuaPhieu.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy phiếu nhập có mã: " + maPN);
        }

        PhieuNhapKho phieuNhap = ketQuaPhieu.get();

        // Kiểm tra 2: Nếu phiếu đã có trạng thái "Hủy" thì trả lỗi 409, không hủy lần nữa
        if ("Hủy".equals(phieuNhap.getTrangThai())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Phiếu nhập có mã " + maPN + " đã được hủy trước đó!");
        }

        // Kiểm tra 3: Với từng dòng chi tiết, tồn kho hiện tại phải lớn hơn hoặc bằng SLThucNhap
        List<ChiTietPhieuNhap> danhSachChiTiet = chiTietPhieuNhapRepository.findByMaPN(maPN);
        for (int i = 0; i < danhSachChiTiet.size(); i++) {
            ChiTietPhieuNhap dong = danhSachChiTiet.get(i);
            String maSP = dong.getMaSP();
            int slCanTru = 0;
            if (dong.getSlThucNhap() != null) {
                slCanTru = dong.getSlThucNhap();
            }

            Optional<SanPham> ketQuaSP = sanPhamRepository.findById(maSP);
            if (!ketQuaSP.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy sản phẩm có mã: " + maSP);
            }

            SanPham sanPham = ketQuaSP.get();
            int tonHienTai = 0;
            if (sanPham.getSlTonKho() != null) {
                tonHienTai = sanPham.getSlTonKho();
            }

            // Nếu tồn kho hiện tại nhỏ hơn số lượng thực nhập cần trừ lại thì báo lỗi 409 và dừng ngay
            if (tonHienTai < slCanTru) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Không thể hủy phiếu: sản phẩm " + maSP + " chỉ còn tồn " + tonHienTai
                                + " nhưng cần trừ lại " + slCanTru + " (hàng đã được xuất bớt)");
            }
        }

        // Sau khi kiểm tra đủ tất cả các dòng, gọi Service để đổi trạng thái phiếu và trừ lại tồn kho
        PhieuNhapKho phieuDaHuy = phieuNhapService.huyPhieuNhap(phieuNhap, danhSachChiTiet);
        return ResponseEntity.ok(phieuDaHuy);
    }
}
