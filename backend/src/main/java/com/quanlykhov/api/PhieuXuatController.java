package com.quanlykhov.api;

import com.quanlykhov.dao.ChiTietPhieuXuatRepository;
import com.quanlykhov.dao.EmployeeRepository;
import com.quanlykhov.dao.KhachHangRepository;
import com.quanlykhov.dao.PhieuXuatKhoRepository;
import com.quanlykhov.dao.SanPhamRepository;
import com.quanlykhov.model.ChiTietPhieuXuat;
import com.quanlykhov.model.PhieuXuatKho;
import com.quanlykhov.model.SanPham;
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
// Đặt đường dẫn gốc cho các API phiếu xuất là /api/phieu-xuat
@RequestMapping("/api/phieu-xuat")
// Cho phép giao diện từ nguồn khác gọi vào API này
@CrossOrigin(origins = "*")
public class PhieuXuatController {

    private final PhieuXuatKhoRepository phieuXuatKhoRepository;
    private final ChiTietPhieuXuatRepository chiTietPhieuXuatRepository;
    private final SanPhamRepository sanPhamRepository;
    private final KhachHangRepository khachHangRepository;
    private final EmployeeRepository employeeRepository;

    // Hàm tạo để Spring truyền các Repository cần thiết vào sử dụng
    public PhieuXuatController(PhieuXuatKhoRepository phieuXuatKhoRepository,
                               ChiTietPhieuXuatRepository chiTietPhieuXuatRepository,
                               SanPhamRepository sanPhamRepository,
                               KhachHangRepository khachHangRepository,
                               EmployeeRepository employeeRepository) {
        this.phieuXuatKhoRepository = phieuXuatKhoRepository;
        this.chiTietPhieuXuatRepository = chiTietPhieuXuatRepository;
        this.sanPhamRepository = sanPhamRepository;
        this.khachHangRepository = khachHangRepository;
        this.employeeRepository = employeeRepository;
    }

    // Xử lý yêu cầu GET /api/phieu-xuat: trả về danh sách tất cả phiếu xuất mới nhất lên đầu
    @GetMapping
    public List<PhieuXuatKho> getAllPhieuXuat() {
        return phieuXuatKhoRepository.findAllByOrderByNgayXuatDesc();
    }

    // Xử lý yêu cầu GET /api/phieu-xuat/{maPX}: trả về 1 phiếu xuất kèm danh sách chi tiết của phiếu đó
    @GetMapping("/{maPX}")
    public ResponseEntity<?> getPhieuXuatById(@PathVariable String maPX) {
        Optional<PhieuXuatKho> ketQua = phieuXuatKhoRepository.findById(maPX);
        if (!ketQua.isPresent()) {
            return ResponseEntity.status(404).body("Không tìm thấy phiếu xuất có mã: " + maPX);
        }

        List<ChiTietPhieuXuat> danhSachChiTiet = chiTietPhieuXuatRepository.findByMaPX(maPX);

        // LinkedHashMap là bảng ghép cặp khóa-giá trị giữ đúng thứ tự để đóng gói JSON gồm phieuXuat và chiTiet
        Map<String, Object> phanHoi = new LinkedHashMap<>();
        phanHoi.put("phieuXuat", ketQua.get());
        phanHoi.put("chiTiet", danhSachChiTiet);

        return ResponseEntity.ok(phanHoi);
    }

    // Xử lý yêu cầu POST /api/phieu-xuat: lập phiếu xuất kho mới và trừ tồn kho sản phẩm
    // @Transactional đảm bảo việc lưu phiếu, lưu chi tiết và giảm tồn kho xảy ra cùng lúc (hoặc cùng hủy bỏ nếu có lỗi)
    @Transactional
    @PostMapping
    public ResponseEntity<?> createPhieuXuat(@RequestBody(required = false) PhieuXuatRequest request) {
        // Bước 1: Kiểm tra thông tin chung của phiếu (nhân viên, khách hàng, ghi chú, danh sách chi tiết)
        String loiPhieu = kiemTraThongTinPhieu(request);
        if (loiPhieu != null) {
            return ResponseEntity.badRequest().body(loiPhieu);
        }

        // Bước 2: Kiểm tra toàn bộ các dòng sản phẩm trước khi lưu bất kỳ dữ liệu nào
        List<SanPham> danhSachSanPham = new ArrayList<>();
        String loiChiTiet = kiemTraVaLayDanhSachSanPham(request.getChiTiet(), danhSachSanPham);
        if (loiChiTiet != null) {
            return ResponseEntity.badRequest().body(loiChiTiet);
        }

        // Bước 3: Tự sinh mã phiếu xuất mới (ví dụ PX04, PX05...)
        String maPXMoi = taoMaPhieuXuatMoi();

        // Bước 4: Lưu thông tin chung của phiếu xuất kho
        PhieuXuatKho phieuMoi = new PhieuXuatKho();
        phieuMoi.setMaPX(maPXMoi);
        phieuMoi.setNgayXuat(LocalDateTime.now());
        phieuMoi.setTrangThai("Hoàn thành");
        phieuMoi.setGhiChu(chuanHoaChuoi(request.getGhiChu()));
        phieuMoi.setMaNV(request.getMaNV().trim());
        phieuMoi.setMaKH(request.getMaKH().trim());
        PhieuXuatKho phieuDaLuu = phieuXuatKhoRepository.save(phieuMoi);

        // Bước 5: Lưu từng dòng chi tiết và trừ tồn kho cho từng sản phẩm
        List<ChiTietPhieuXuat> danhSachChiTietDaLuu = luuChiTietVaGiamTonKho(
                maPXMoi,
                request.getChiTiet(),
                danhSachSanPham
        );

        // Bước 6: Đóng gói kết quả trả về cùng mã trạng thái 201 Created
        Map<String, Object> phanHoi = new LinkedHashMap<>();
        phanHoi.put("phieuXuat", phieuDaLuu);
        phanHoi.put("chiTiet", danhSachChiTietDaLuu);
        return ResponseEntity.status(HttpStatus.CREATED).body(phanHoi);
    }

    // Kiểm tra thông tin chung của phiếu xuất; trả về câu thông báo lỗi nếu sai, null nếu hợp lệ
    private String kiemTraThongTinPhieu(PhieuXuatRequest request) {
        if (request == null) {
            return "Dữ liệu phiếu xuất không được để trống!";
        }
        if (request.getMaNV() == null || request.getMaNV().trim().isEmpty()) {
            return "Mã nhân viên không được để trống!";
        }
        if (request.getMaKH() == null || request.getMaKH().trim().isEmpty()) {
            return "Mã khách hàng không được để trống!";
        }
        if (request.getChiTiet() == null || request.getChiTiet().isEmpty()) {
            return "Phiếu xuất phải có ít nhất 1 sản phẩm!";
        }

        String ghiChu = chuanHoaChuoi(request.getGhiChu());
        if (ghiChu != null && ghiChu.length() > 255) {
            return "Ghi chú tối đa 255 ký tự!";
        }

        String maNV = request.getMaNV().trim();
        if (!employeeRepository.existsById(maNV)) {
            return "Không tìm thấy nhân viên có mã: " + maNV;
        }

        String maKH = request.getMaKH().trim();
        if (!khachHangRepository.existsById(maKH)) {
            return "Không tìm thấy khách hàng có mã: " + maKH;
        }

        return null;
    }

    // Kiểm tra từng dòng sản phẩm (trùng mã, số lượng, tồn tại, trạng thái, tồn kho) và gom SanPham vào ketQuaSanPham
    private String kiemTraVaLayDanhSachSanPham(List<ChiTietXuatRequest> danhSachChiTiet,
                                               List<SanPham> ketQuaSanPham) {
        List<String> dsMaSPDaGap = new ArrayList<>();

        for (int i = 0; i < danhSachChiTiet.size(); i++) {
            ChiTietXuatRequest dong = danhSachChiTiet.get(i);
            if (dong == null) {
                return "Thông tin dòng sản phẩm thứ " + (i + 1) + " không hợp lệ!";
            }
            if (dong.getMaSP() == null || dong.getMaSP().trim().isEmpty()) {
                return "Mã sản phẩm ở dòng thứ " + (i + 1) + " không được để trống!";
            }

            String maSP = dong.getMaSP().trim();
            if (dsMaSPDaGap.contains(maSP)) {
                return "Sản phẩm có mã " + maSP + " bị trùng lặp trong cùng một phiếu xuất!";
            }
            dsMaSPDaGap.add(maSP);

            if (dong.getSlXuat() == null || dong.getSlXuat() <= 0) {
                return "Số lượng xuất của sản phẩm " + maSP + " phải lớn hơn 0!";
            }

            Optional<SanPham> spOpt = sanPhamRepository.findById(maSP);
            if (!spOpt.isPresent()) {
                return "Không tìm thấy sản phẩm có mã: " + maSP;
            }

            SanPham sp = spOpt.get();
            if ("Ngừng kinh doanh".equals(sp.getTrangThai()) || !"Đang kinh doanh".equals(sp.getTrangThai())) {
                return "Sản phẩm " + sp.getTenSP() + " (" + maSP + ") đang ngừng kinh doanh, không thể xuất kho!";
            }

            int tonHienTai = 0;
            if (sp.getSlTonKho() != null) {
                tonHienTai = sp.getSlTonKho();
            }

            if (dong.getSlXuat() > tonHienTai) {
                return "Sản phẩm " + sp.getTenSP() + " (" + maSP + ") không đủ tồn kho! Hiện tồn: "
                        + tonHienTai + ", yêu cầu xuất: " + dong.getSlXuat() + ".";
            }

            ketQuaSanPham.add(sp);
        }

        return null;
    }

    // Tự sinh mã phiếu xuất mới dạng PX04, PX05... dựa trên mã lớn nhất hiện có
    private String taoMaPhieuXuatMoi() {
        List<PhieuXuatKho> tatCaPhieu = phieuXuatKhoRepository.findAll();
        int soLonNhat = 0;

        for (int i = 0; i < tatCaPhieu.size(); i++) {
            String ma = tatCaPhieu.get(i).getMaPX();
            if (ma != null && ma.startsWith("PX") && ma.length() > 2) {
                String phanSo = ma.substring(2);
                try {
                    int so = Integer.parseInt(phanSo);
                    if (so > soLonNhat) {
                        soLonNhat = so;
                    }
                } catch (NumberFormatException e) {
                    // Bỏ qua các mã không đúng dạng PX + chữ số
                }
            }
        }

        int soMoi = soLonNhat + 1;
        if (soMoi < 10) {
            return "PX0" + soMoi;
        }
        return "PX" + soMoi;
    }

    // Lưu từng dòng chi tiết phiếu xuất với giá bán hiện tại và giảm số lượng tồn kho của sản phẩm
    private List<ChiTietPhieuXuat> luuChiTietVaGiamTonKho(String maPX,
                                                           List<ChiTietXuatRequest> danhSachChiTiet,
                                                           List<SanPham> danhSachSanPham) {
        List<ChiTietPhieuXuat> ketQua = new ArrayList<>();

        for (int i = 0; i < danhSachChiTiet.size(); i++) {
            ChiTietXuatRequest dong = danhSachChiTiet.get(i);
            SanPham sp = danhSachSanPham.get(i);

            int tonHienTai = 0;
            if (sp.getSlTonKho() != null) {
                tonHienTai = sp.getSlTonKho();
            }
            int slXuat = dong.getSlXuat();

            // Giảm tồn kho của sản phẩm theo số lượng xuất
            sp.setSlTonKho(tonHienTai - slXuat);
            sanPhamRepository.save(sp);

            // Lưu dòng chi tiết với giá bán lấy từ SanPham.GiaBan tại thời điểm xuất
            ChiTietPhieuXuat chiTietMoi = new ChiTietPhieuXuat();
            chiTietMoi.setMaPX(maPX);
            chiTietMoi.setMaSP(sp.getMaSP());
            chiTietMoi.setSlXuat(slXuat);
            chiTietMoi.setGiaBan(sp.getGiaBan());

            ChiTietPhieuXuat daLuu = chiTietPhieuXuatRepository.save(chiTietMoi);
            ketQua.add(daLuu);
        }

        return ketQua;
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
}
