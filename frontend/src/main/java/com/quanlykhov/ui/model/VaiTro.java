package com.quanlykhov.ui.model;

/**
 * Anh xa bang VaiTro (MaVT, TenVT, MoTa).
 */
public class VaiTro {

    private String maVT;
    private String tenVT;
    private String moTa;

    public VaiTro() {
    }

    public VaiTro(String maVT, String tenVT, String moTa) {
        this.maVT = maVT;
        this.tenVT = tenVT;
        this.moTa = moTa;
    }

    public String getMaVT() {
        return maVT;
    }

    public void setMaVT(String maVT) {
        this.maVT = maVT;
    }

    public String getTenVT() {
        return tenVT;
    }

    public void setTenVT(String tenVT) {
        this.tenVT = tenVT;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }
}
