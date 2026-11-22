package com.quanlykhov.api;

import com.quanlykhov.dao.ChiTietPhieuNhapRepository;
import com.quanlykhov.dao.PhieuNhapKhoRepository;
import com.quanlykhov.dao.SanPhamRepository;
import com.quanlykhov.model.ChiTietPhieuNhap;
import com.quanlykhov.model.PhieuNhapKho;
import com.quanlykhov.model.SanPham;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

// Đánh dấu lớp này là nơi xử lý nghiệp vụ lập phiếu nhập kho
@Service
public class PhieuNhapService {

    private final PhieuNhapKhoRepository phieuNhapKhoRepository;
    private final ChiTietPhieuNhapRepository chiTietPhieuNhapRepository;
    private final SanPhamRepository sanPhamRepository;

    // Hàm tạo để Spring tự động truyền các Repository vào sử dụng
    public PhieuNhapService(PhieuNhapKhoRepository phieuNhapKhoRepository,
                            ChiTietPhieuNhapRepository chiTietPhieuNhapRepository,
                            SanPhamRepository sanPhamRepository) {
        this.phieuNhapKhoRepository = phieuNhapKhoRepository;
        this.chiTietPhieuNhapRepository = chiTietPhieuNhapRepository;
        this.sanPhamRepository = sanPhamRepository;
    }

    // Hàm tự động tạo mã phiếu nhập mới (ví dụ: PN04, PN10) từ các mã PN hiện có
    private String taoMaPhieuNhapMoi() {
        List<PhieuNhapKho> danhSachPhieu = phieuNhapKhoRepository.findAll();
        int soLonNhat = 0;

        // Duyệt qua từng phiếu nhập để tìm số lớn nhất sau chữ "PN"
        for (int i = 0; i < danhSachPhieu.size(); i++) {
            String ma = danhSachPhieu.get(i).getMaPN();
            if (ma != null && ma.startsWith("PN") && ma.length() > 2) {
                String phanSo = ma.substring(2);
                try {
                    int so = Integer.parseInt(phanSo);
                    if (so > soLonNhat) {
                        soLonNhat = so;
                    }
                } catch (NumberFormatException e) {
                    // Bỏ qua các mã không đúng định dạng PN + số
                }
            }
        }

        // Cộng 1 và định dạng "PN" + số có tối thiểu 2 chữ số
        int soMoi = soLonNhat + 1;
        return String.format("PN%02d", soMoi);
    }

    // @Transactional: đảm bảo lưu phiếu và tăng tồn kho cùng thành công hoặc cùng thất bại
    @Transactional
    public PhieuNhapKho lapPhieuNhap(PhieuNhapRequest yeuCau) {
        // Bước 1: Tạo mã phiếu nhập tự động
        String maPNMoi = taoMaPhieuNhapMoi();

        // Bước 2: Tạo và lưu phiếu nhập kho với thời điểm hiện tại và trạng thái "Hoàn thành"
        PhieuNhapKho phieuMoi = new PhieuNhapKho();
        phieuMoi.setMaPN(maPNMoi);
        phieuMoi.setNgayNhap(LocalDateTime.now());
        phieuMoi.setTrangThai("Hoàn thành");
        phieuMoi.setGhiChu(yeuCau.getGhiChu());
        phieuMoi.setMaNV(yeuCau.getMaNV().trim());
        phieuMoi.setMaNCC(yeuCau.getMaNCC().trim());

        // Dùng saveAndFlush để đẩy xuống database ngay, nếu mã nhân viên sai sẽ báo lỗi khóa ngoại lập tức
        PhieuNhapKho phieuDaLuu = phieuNhapKhoRepository.saveAndFlush(phieuMoi);

        // Bước 3: Duyệt từng dòng chi tiết để lưu chi tiết phiếu nhập và cộng tồn kho sản phẩm
        List<ChiTietNhapRequest> danhSachChiTiet = yeuCau.getChiTiet();
        for (int i = 0; i < danhSachChiTiet.size(); i++) {
            ChiTietNhapRequest dong = danhSachChiTiet.get(i);
            String maSP = dong.getMaSP().trim();
            Integer slThucNhap = dong.getSlThucNhap();

            // Nếu không gửi slTheoChungTu thì lấy bằng slThucNhap
            Integer slTheoChungTu = dong.getSlTheoChungTu();
            if (slTheoChungTu == null) {
                slTheoChungTu = slThucNhap;
            }

            // Lưu dòng chi tiết phiếu nhập vào bảng ChiTietPhieuNhap
            ChiTietPhieuNhap chiTiet = new ChiTietPhieuNhap(
                    maPNMoi,
                    maSP,
                    slTheoChungTu,
                    slThucNhap,
                    dong.getGiaNhap()
            );
            chiTietPhieuNhapRepository.save(chiTiet);

            // Tăng số lượng tồn kho của sản phẩm theo đúng SLThucNhap (tuyệt đối không dùng SLTheoChungTu)
            SanPham sanPham = sanPhamRepository.findById(maSP).get();
            int tonHienTai = 0;
            if (sanPham.getSlTonKho() != null) {
                tonHienTai = sanPham.getSlTonKho();
            }
            sanPham.setSlTonKho(tonHienTai + slThucNhap);
            sanPhamRepository.save(sanPham);
        }

        return phieuDaLuu;
    }
}
