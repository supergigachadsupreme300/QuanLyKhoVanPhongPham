package com.quanlykhov.api;

import com.quanlykhov.dao.PhieuNhapBaoCaoRepository;
import com.quanlykhov.dao.PhieuNhapBaoCaoRepository.ChiTietPhieuNhapInProjection;
import com.quanlykhov.dao.PhieuNhapBaoCaoRepository.ThongTinPhieuNhapInProjection;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

// Đánh dấu lớp này là nơi tiếp nhận yêu cầu in và lưu trữ phiếu nhập kho
@RestController
// Đặt đường dẫn gốc là /api/phieu-nhap
@RequestMapping("/api/phieu-nhap")
// Cho phép giao diện từ nguồn khác gọi vào API này
@CrossOrigin(origins = "*")
public class PhieuNhapInController {

    private final PhieuNhapBaoCaoRepository phieuNhapBaoCaoRepository;

    // Hàm tạo để Spring tự động truyền PhieuNhapBaoCaoRepository vào sử dụng
    public PhieuNhapInController(PhieuNhapBaoCaoRepository phieuNhapBaoCaoRepository) {
        this.phieuNhapBaoCaoRepository = phieuNhapBaoCaoRepository;
    }

    // Hàm thay thế các ký tự đặc biệt (& < > " ') thành mã an toàn của HTML
    // để tránh lỗi vỡ giao diện hoặc chèn mã độc khi dữ liệu từ database có chứa các ký tự này
    private String escapeHtml(String chuoi) {
        if (chuoi == null) {
            return "";
        }
        String ketQua = chuoi;
        ketQua = ketQua.replace("&", "&amp;");
        ketQua = ketQua.replace("<", "&lt;");
        ketQua = ketQua.replace(">", "&gt;");
        ketQua = ketQua.replace("\"", "&quot;");
        ketQua = ketQua.replace("'", "&#39;");
        return ketQua;
    }

    // Hàm định dạng số tiền theo kiểu Việt Nam, dùng dấu chấm ngăn cách hàng nghìn (ví dụ: 1.325.000)
    private String dinhDangTien(BigDecimal soTien) {
        if (soTien == null) {
            return "0";
        }
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat nf = NumberFormat.getNumberInstance(localeVN);
        nf.setMaximumFractionDigits(0);
        return nf.format(soTien);
    }

    // Hàm kiểm tra mã phiếu nhập chỉ gồm chữ cái, chữ số, gạch ngang (-) hoặc gạch dưới (_) để tránh tên file nguy hiểm
    private boolean kiemTraMaPNAnToan(String maPN) {
        if (maPN == null || maPN.isEmpty()) {
            return false;
        }
        for (int i = 0; i < maPN.length(); i++) {
            char c = maPN.charAt(i);
            boolean laChuHoa = (c >= 'A' && c <= 'Z');
            boolean laChuThuong = (c >= 'a' && c <= 'z');
            boolean laSo = (c >= '0' && c <= '9');
            boolean laGach = (c == '-' || c == '_');
            if (!laChuHoa && !laChuThuong && !laSo && !laGach) {
                return false;
            }
        }
        return true;
    }

    // Hàm lưu nội dung HTML ra file phieu-nhap-luu-tru/{maPN}.html với bảng mã UTF-8 (ghi đè nếu file đã tồn tại)
    private void luuFileHtml(String maPN, String noiDungHtml) {
        // Nếu mã phiếu có ký tự lạ không an toàn thì bỏ qua việc ghi file
        if (!kiemTraMaPNAnToan(maPN)) {
            return;
        }

        try {
            File thuMuc = new File("phieu-nhap-luu-tru");
            if (!thuMuc.exists()) {
                thuMuc.mkdirs();
            }

            File fileLuu = new File(thuMuc, maPN + ".html");
            FileOutputStream fos = new FileOutputStream(fileLuu, false);
            OutputStreamWriter writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
            writer.write(noiDungHtml);
            writer.flush();
            writer.close();
            fos.close();
        } catch (IOException e) {
            // Nếu ghi file gặp lỗi thì chỉ in thông báo tiếng Việt ra console, vẫn trả trang HTML cho người dùng
            System.out.println("Lỗi khi lưu file phiếu nhập " + maPN + ": " + e.getMessage());
        }
    }

    // Xử lý yêu cầu GET /api/phieu-nhap/{maPN}/in: xuất trang HTML phiếu nhập kho để in khổ A4 và lưu 1 bản file HTML
    @GetMapping(value = "/{maPN}/in", produces = "text/html;charset=UTF-8")
    public ResponseEntity<String> inPhieuNhap(@PathVariable("maPN") String maPN) {
        // Bước 1: Lấy thông tin đầu phiếu từ database, nếu không có thì trả mã 404 kèm thông báo tiếng Việt
        ThongTinPhieuNhapInProjection phieu = phieuNhapBaoCaoRepository.layThongTinPhieuNhapDeIn(maPN);
        if (phieu == null) {
            HttpHeaders headersLoi = new HttpHeaders();
            headersLoi.setContentType(new MediaType("text", "plain", StandardCharsets.UTF_8));
            return new ResponseEntity<>("Không tìm thấy phiếu nhập có mã: " + maPN, headersLoi, HttpStatus.NOT_FOUND);
        }

        // Bước 2: Lấy danh sách các dòng chi tiết của phiếu nhập
        List<ChiTietPhieuNhapInProjection> danhSachChiTiet = phieuNhapBaoCaoRepository.layChiTietPhieuNhapDeIn(maPN);

        // Định dạng ngày nhập thành dd/MM/yyyy HH:mm
        String ngayNhapStr = "";
        LocalDateTime ngayNhap = phieu.getNgayNhap();
        if (ngayNhap != null) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            ngayNhapStr = ngayNhap.format(dtf);
        }

        String ghiChuHienThi = "Không có";
        if (phieu.getGhiChu() != null && !phieu.getGhiChu().trim().isEmpty()) {
            ghiChuHienThi = escapeHtml(phieu.getGhiChu());
        }

        // Bước 3: Ghép chuỗi tạo trang HTML hoàn chỉnh bằng StringBuilder
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n");
        html.append("<html lang=\"vi\">\n");
        html.append("<head>\n");
        html.append("    <meta charset=\"UTF-8\">\n");
        html.append("    <title>Phiếu nhập kho - ").append(escapeHtml(phieu.getMaPN())).append("</title>\n");
        html.append("    <style>\n");
        html.append("        body { font-family: Arial, sans-serif; color: #222; margin: 20px; }\n");
        html.append("        .khung-a4 { max-width: 800px; margin: 0 auto; padding: 20px; border: 1px solid #ccc; }\n");
        html.append("        .thanh-cong-cu { text-align: right; margin-bottom: 15px; }\n");
        html.append("        .nut-in { padding: 8px 18px; font-size: 14px; background-color: #1976d2; color: #fff; border: none; border-radius: 4px; cursor: pointer; }\n");
        html.append("        .tieu-de { text-align: center; margin-bottom: 5px; font-size: 24px; font-weight: bold; }\n");
        html.append("        .canh-bao-huy { text-align: center; color: #d32f2f; font-size: 22px; font-weight: bold; margin: 8px 0; }\n");
        html.append("        .thong-tin-chung { margin-top: 15px; margin-bottom: 20px; line-height: 1.7; font-size: 14px; }\n");
        html.append("        table { width: 100%; border-collapse: collapse; margin-bottom: 25px; font-size: 14px; }\n");
        html.append("        th, td { border: 1px solid #333; padding: 8px; }\n");
        html.append("        th { background-color: #f2f2f2; text-align: center; }\n");
        html.append("        .can-giua { text-align: center; }\n");
        html.append("        .can-phai { text-align: right; }\n");
        html.append("        .dong-tong { font-weight: bold; background-color: #fafafa; }\n");
        html.append("        .khu-vuc-ky-ten { display: flex; justify-content: space-around; margin-top: 30px; text-align: center; font-size: 14px; }\n");
        html.append("        .o-ky-ten { width: 45%; }\n");
        html.append("        .khoang-trong-ky { height: 80px; }\n");
        html.append("        @media print {\n");
        html.append("            .thanh-cong-cu { display: none; }\n");
        html.append("            .khung-a4 { border: none; padding: 0; max-width: 100%; }\n");
        html.append("            body { margin: 10mm; }\n");
        html.append("        }\n");
        html.append("    </style>\n");
        html.append("</head>\n");
        html.append("<body>\n");
        html.append("    <div class=\"khung-a4\">\n");
        html.append("        <div class=\"thanh-cong-cu\">\n");
        html.append("            <button class=\"nut-in\" onclick=\"window.print()\">In phiếu</button>\n");
        html.append("        </div>\n");
        html.append("        <div class=\"tieu-de\">PHIẾU NHẬP KHO</div>\n");

        // Nếu trạng thái phiếu là "Hủy" thì hiện dòng chữ lớn màu đỏ "PHIẾU ĐÃ HỦY" ngay dưới tiêu đề
        if ("Hủy".equals(phieu.getTrangThai())) {
            html.append("        <div class=\"canh-bao-huy\">PHIẾU ĐÃ HỦY</div>\n");
        }

        html.append("        <div class=\"thong-tin-chung\">\n");
        html.append("            <div><strong>Mã phiếu:</strong> ").append(escapeHtml(phieu.getMaPN())).append("</div>\n");
        html.append("            <div><strong>Ngày nhập:</strong> ").append(escapeHtml(ngayNhapStr)).append("</div>\n");
        html.append("            <div><strong>Nhà cung cấp:</strong> ").append(escapeHtml(phieu.getMaNCC())).append(" - ").append(escapeHtml(phieu.getTenNCC())).append("</div>\n");
        html.append("            <div><strong>Nhân viên lập phiếu:</strong> ").append(escapeHtml(phieu.getMaNV())).append(" - ").append(escapeHtml(phieu.getHoTenNV())).append("</div>\n");
        html.append("            <div><strong>Trạng thái:</strong> ").append(escapeHtml(phieu.getTrangThai())).append("</div>\n");
        html.append("            <div><strong>Ghi chú:</strong> ").append(ghiChuHienThi).append("</div>\n");
        html.append("        </div>\n");

        // Bảng danh sách các dòng hàng của phiếu nhập
        html.append("        <table>\n");
        html.append("            <thead>\n");
        html.append("                <tr>\n");
        html.append("                    <th>STT</th>\n");
        html.append("                    <th>Mã SP</th>\n");
        html.append("                    <th>Tên sản phẩm</th>\n");
        html.append("                    <th>Đơn vị tính</th>\n");
        html.append("                    <th>SL theo chứng từ</th>\n");
        html.append("                    <th>SL thực nhập</th>\n");
        html.append("                    <th>Giá nhập</th>\n");
        html.append("                    <th>Thành tiền</th>\n");
        html.append("                </tr>\n");
        html.append("            </thead>\n");
        html.append("            <tbody>\n");

        BigDecimal tongCong = BigDecimal.ZERO;

        if (danhSachChiTiet == null || danhSachChiTiet.isEmpty()) {
            html.append("                <tr>\n");
            html.append("                    <td colspan=\"8\" class=\"can-giua\">Phiếu chưa có dòng hàng</td>\n");
            html.append("                </tr>\n");
        } else {
            // Duyệt qua từng dòng chi tiết để tính Thành tiền = SL thực nhập x Giá nhập và cộng dồn Tổng cộng
            for (int i = 0; i < danhSachChiTiet.size(); i++) {
                ChiTietPhieuNhapInProjection dong = danhSachChiTiet.get(i);
                int slChungTu = 0;
                if (dong.getSlTheoChungTu() != null) {
                    slChungTu = dong.getSlTheoChungTu();
                }

                int slThucNhap = 0;
                if (dong.getSlThucNhap() != null) {
                    slThucNhap = dong.getSlThucNhap();
                }

                BigDecimal giaNhap = BigDecimal.ZERO;
                if (dong.getGiaNhap() != null) {
                    giaNhap = dong.getGiaNhap();
                }

                // Thành tiền = SL thực nhập x Giá nhập (tuyệt đối không dùng SL theo chứng từ)
                BigDecimal thanhTien = giaNhap.multiply(BigDecimal.valueOf(slThucNhap));
                tongCong = tongCong.add(thanhTien);

                html.append("                <tr>\n");
                html.append("                    <td class=\"can-giua\">").append(i + 1).append("</td>\n");
                html.append("                    <td class=\"can-giua\">").append(escapeHtml(dong.getMaSP())).append("</td>\n");
                html.append("                    <td>").append(escapeHtml(dong.getTenSP())).append("</td>\n");
                html.append("                    <td class=\"can-giua\">").append(escapeHtml(dong.getDonViTinh())).append("</td>\n");
                html.append("                    <td class=\"can-phai\">").append(slChungTu).append("</td>\n");
                html.append("                    <td class=\"can-phai\">").append(slThucNhap).append("</td>\n");
                html.append("                    <td class=\"can-phai\">").append(dinhDangTien(giaNhap)).append("</td>\n");
                html.append("                    <td class=\"can-phai\">").append(dinhDangTien(thanhTien)).append("</td>\n");
                html.append("                </tr>\n");
            }
        }

        html.append("                <tr class=\"dong-tong\">\n");
        html.append("                    <td colspan=\"7\" class=\"can-phai\">Tổng cộng:</td>\n");
        html.append("                    <td class=\"can-phai\">").append(dinhDangTien(tongCong)).append("</td>\n");
        html.append("                </tr>\n");
        html.append("            </tbody>\n");
        html.append("        </table>\n");

        // Hai ô ký tên ở cuối trang
        html.append("        <div class=\"khu-vuc-ky-ten\">\n");
        html.append("            <div class=\"o-ky-ten\">\n");
        html.append("                <div><strong>Người lập phiếu</strong></div>\n");
        html.append("                <div><em>(Ký, ghi rõ họ tên)</em></div>\n");
        html.append("                <div class=\"khoang-trong-ky\"></div>\n");
        html.append("            </div>\n");
        html.append("            <div class=\"o-ky-ten\">\n");
        html.append("                <div><strong>Người giao hàng</strong></div>\n");
        html.append("                <div><em>(Ký, ghi rõ họ tên)</em></div>\n");
        html.append("                <div class=\"khoang-trong-ky\"></div>\n");
        html.append("            </div>\n");
        html.append("        </div>\n");
        html.append("    </div>\n");
        html.append("</body>\n");
        html.append("</html>\n");

        String chuoiHtml = html.toString();

        // Bước 4: Lưu trữ 1 bản ra file phieu-nhap-luu-tru/{maPN}.html
        luuFileHtml(phieu.getMaPN(), chuoiHtml);

        // Bước 5: Trả trang HTML về với Content-Type text/html;charset=UTF-8
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("text", "html", StandardCharsets.UTF_8));
        return new ResponseEntity<>(chuoiHtml, headers, HttpStatus.OK);
    }
}
