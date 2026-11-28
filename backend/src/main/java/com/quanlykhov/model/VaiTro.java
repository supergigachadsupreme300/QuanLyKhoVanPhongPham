package com.quanlykhov.model;

import jakarta.persistence.*;

@Entity
@Table(name = "VaiTro")
public class VaiTro {

    @Id
    @Column(name = "MaVT", length = 20)
    private String maVT;

    @Column(name = "TenVT", length = 100, nullable = false, unique = true)
    private String tenVT;

    @Column(name = "MoTa", length = 255)
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