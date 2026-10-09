package com.quanlykhov.common.constants;

/**
 * Hang so dung chung cho toan bo ung dung quan tri.
 */
public final class AppConstants {

    private AppConstants() {
    }

    public static final String APP_TITLE = "Hệ thống Quản lý Kho Văn Phòng Phẩm";
    public static final String APP_SUBTITLE = "Phân hệ Quản trị (Admin)";
    public static final String APP_VERSION = "1.0.0";
    public static final String APP_FOOTER = "Đồ án Phân tích & Thiết kế HTTT - Trường Đại học Sài Gòn";

    public static final String TRANG_THAI_HOAT_DONG = "Hoạt động";
    public static final String TRANG_THAI_KHOA = "Khoá";
    public static final String TRANG_THAI_NGUNG = "Ngừng hợp tác";

    public static final String[] TRANG_THAI_TAI_KHOAN = {
        TRANG_THAI_HOAT_DONG, TRANG_THAI_KHOA
    };

    public static final String[] TRANG_THAI_NHA_CC = {
        TRANG_THAI_HOAT_DONG, TRANG_THAI_NGUNG
    };

    /** Cac module dung cho ma tran phan quyen (chi mang tinh demo o tang giao dien). */
    public static final String[] MODULE_PHAN_QUYEN = {
        "Sản phẩm", "Danh mục", "Nhà cung cấp", "Nhập kho",
        "Xuất kho", "Kiểm kê", "Bán hàng", "Người dùng", "Báo cáo"
    };
}
