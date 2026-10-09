package com.quanlykhov.common.util;

import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Tien ich dinh dang va so sanh chuoi tieng Viet.
 */
public final class StringUtil {

    private static final DateTimeFormatter NGAY_GIO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final SimpleDateFormat DTF = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    private StringUtil() {
    }

    public static String dinhDangNgayGio(LocalDateTime thoiDiem) {
        if (thoiDiem == null) {
            return "";
        }
        return NGAY_GIO.format(thoiDiem);
    }

    public static String dinhDangNgayGio(Date thoiDiem) {
        if (thoiDiem == null) {
            return "";
        }
        return DTF.format(thoiDiem);
    }

    /** Bo dau tieng Viet de so sanh / tim kiem khong phan biet dau. */
    public static String boDau(String text) {
        if (text == null) {
            return "";
        }
        String temp = Normalizer.normalize(text, Normalizer.Form.NFD);
        temp = temp.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        temp = temp.replace('đ', 'd').replace('Đ', 'D');
        return temp.toLowerCase();
    }

    public static boolean chua(String nguon, String tuKhoa) {
        if (tuKhoa == null || tuKhoa.trim().isEmpty()) {
            return true;
        }
        return boDau(nguon).contains(boDau(tuKhoa.trim()));
    }

    public static boolean rong(String s) {
        return s == null || s.trim().isEmpty();
    }

    public static String anToan(String s) {
        return s == null ? "" : s;
    }
}
