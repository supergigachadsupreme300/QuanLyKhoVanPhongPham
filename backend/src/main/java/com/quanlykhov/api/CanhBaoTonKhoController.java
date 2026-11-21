package com.quanlykhov.api;

import com.quanlykhov.dao.SanPhamRepository;
import com.quanlykhov.model.SanPham;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

// Đánh dấu lớp này tiếp nhận các yêu cầu HTTP và trả về dữ liệu JSON
@RestController
// Đặt đường dẫn gốc cho API cảnh báo tồn kho
@RequestMapping("/api/san-pham")
// Cho phép giao diện từ nguồn khác gọi vào API này
@CrossOrigin(origins = "*")
public class CanhBaoTonKhoController {

    private final SanPhamRepository sanPhamRepository;

    // Khởi tạo controller và nhận SanPhamRepository từ Spring
    public CanhBaoTonKhoController(SanPhamRepository sanPhamRepository) {
        this.sanPhamRepository = sanPhamRepository;
    }

    // Xử lý yêu cầu GET /api/san-pham/sap-het: trả về danh sách sản phẩm sắp hết hàng
    @GetMapping("/sap-het")
    public List<SanPham> getSanPhamSapHet() {
        List<SanPham> tatCaSanPham = sanPhamRepository.findAll();
        List<SanPham> danhSachSapHet = locSanPhamSapHet(tatCaSanPham);
        sapXepTheoThieuHutGiamDan(danhSachSapHet);
        return danhSachSapHet;
    }

    // Lọc danh sách để chỉ giữ sản phẩm đang kinh doanh và có tồn kho nhỏ hơn mức tối thiểu
    private List<SanPham> locSanPhamSapHet(List<SanPham> tatCaSanPham) {
        List<SanPham> ketQua = new ArrayList<>();
        for (int i = 0; i < tatCaSanPham.size(); i++) {
            SanPham sp = tatCaSanPham.get(i);

            // Chỉ tính sản phẩm có trạng thái Đang kinh doanh
            if (!"Đang kinh doanh".equals(sp.getTrangThai())) {
                continue;
            }
            if (sp.getSlTonKho() == null || sp.getSlToiThieu() == null) {
                continue;
            }

            // Nếu tồn kho thấp hơn mức tối thiểu thì đưa vào danh sách cảnh báo
            if (sp.getSlTonKho() < sp.getSlToiThieu()) {
                ketQua.add(sp);
            }
        }
        return ketQua;
    }

    // Sắp xếp danh sách sản phẩm theo mức thiếu hụt (SLToiThieu - SLTonKho) giảm dần
    private void sapXepTheoThieuHutGiamDan(List<SanPham> danhSach) {
        int soLuong = danhSach.size();
        for (int i = 0; i < soLuong - 1; i++) {
            for (int j = i + 1; j < soLuong; j++) {
                SanPham spI = danhSach.get(i);
                SanPham spJ = danhSach.get(j);

                int thieuI = spI.getSlToiThieu() - spI.getSlTonKho();
                int thieuJ = spJ.getSlToiThieu() - spJ.getSlTonKho();

                // Đưa sản phẩm có mức thiếu hụt lớn hơn lên trước
                if (thieuJ > thieuI) {
                    danhSach.set(i, spJ);
                    danhSach.set(j, spI);
                }
            }
        }
    }
}
