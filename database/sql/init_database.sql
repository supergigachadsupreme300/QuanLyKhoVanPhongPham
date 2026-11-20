DROP DATABASE IF EXISTS quan_ly_kho;
CREATE DATABASE quan_ly_kho CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE quan_ly_kho;

-- PHAN 1: TAO BANG

CREATE TABLE DanhMuc (
    MaDM  VARCHAR(20)  PRIMARY KEY,
    TenDM VARCHAR(100)
);

CREATE TABLE KeHang (
    MaKe         VARCHAR(20)  PRIMARY KEY,
    TenKe        VARCHAR(100),
    SucChuaToiDa INT
);

CREATE TABLE NhaCC (
    MaNCC     VARCHAR(20)  PRIMARY KEY,
    TenNCC    VARCHAR(100),
    DiaChi    VARCHAR(255),
    Sdt       VARCHAR(15),
    TrangThai VARCHAR(50)  DEFAULT 'Hoạt động'      -- 'Hoạt động' / 'Ngừng hợp tác' (xoa mem)
);

CREATE TABLE NhanVien (
    MaNV  VARCHAR(20)  PRIMARY KEY,
    HoTen VARCHAR(100),
    Sdt   VARCHAR(15)
);

CREATE TABLE VaiTro (
    MaVT  VARCHAR(20)  PRIMARY KEY,
    TenVT VARCHAR(100) NOT NULL UNIQUE,             -- Admin, Quản lý kho, Nhân viên kho
    MoTa  VARCHAR(255)
);

CREATE TABLE TaiKhoan (
    MaTK        VARCHAR(20)  PRIMARY KEY,
    TenDangNhap VARCHAR(50)  NOT NULL UNIQUE,
    MatKhau     VARCHAR(255) NOT NULL,              -- luu ma bam, khong luu mat khau that
    TrangThai   VARCHAR(50)  DEFAULT 'Hoạt động',   -- 'Hoạt động' / 'Bị khóa'
    MaVT        VARCHAR(20)  NOT NULL,
    MaNV        VARCHAR(20)  NOT NULL UNIQUE,
    FOREIGN KEY (MaVT) REFERENCES VaiTro(MaVT),
    FOREIGN KEY (MaNV) REFERENCES NhanVien(MaNV)
);

CREATE TABLE SanPham (
    MaSP       VARCHAR(20)   PRIMARY KEY,
    TenSP      VARCHAR(100),
    DonViTinh  VARCHAR(20),
    SLTonKho   INT           DEFAULT 0,
    SLToiThieu INT           DEFAULT 10,
    GiaBan     DECIMAL(18,2),
    TrangThai  VARCHAR(50)   DEFAULT 'Đang kinh doanh',  -- 'Đang kinh doanh' / 'Ngừng kinh doanh' (xoa mem)
    MaDM       VARCHAR(20)   NOT NULL,
    MaNCC      VARCHAR(20),
    MaKe       VARCHAR(20),
    CONSTRAINT chk_sp_tonkho   CHECK (SLTonKho >= 0),
    CONSTRAINT chk_sp_toithieu CHECK (SLToiThieu >= 0),
    CONSTRAINT chk_sp_giaban   CHECK (GiaBan > 0),
    FOREIGN KEY (MaDM)  REFERENCES DanhMuc(MaDM),
    FOREIGN KEY (MaNCC) REFERENCES NhaCC(MaNCC),
    FOREIGN KEY (MaKe)  REFERENCES KeHang(MaKe)
);

CREATE TABLE KhachHang (
    MaKH   VARCHAR(20)  PRIMARY KEY,
    TenKH  VARCHAR(100),
    Sdt    VARCHAR(15),
    DiaChi VARCHAR(255)
);

CREATE TABLE PhieuNhapKho (
    MaPN      VARCHAR(20)  PRIMARY KEY,
    NgayNhap  DATETIME     DEFAULT CURRENT_TIMESTAMP,
    TrangThai VARCHAR(50)  DEFAULT 'Hoàn thành',    -- 'Hoàn thành' / 'Hủy'
    GhiChu    VARCHAR(255),
    MaNV      VARCHAR(20)  NOT NULL,
    MaNCC     VARCHAR(20)  NOT NULL,
    FOREIGN KEY (MaNV)  REFERENCES NhanVien(MaNV),
    FOREIGN KEY (MaNCC) REFERENCES NhaCC(MaNCC)
);

CREATE TABLE ChiTietPhieuNhap (
    MaPN          VARCHAR(20) NOT NULL,
    MaSP          VARCHAR(20) NOT NULL,
    SLTheoChungTu INT         DEFAULT 0,            -- so luong ghi tren giay to cua nha cung cap
    SLThucNhap    INT         DEFAULT 0,            -- so luong dem thuc te (dung de tang ton kho)
    GiaNhap       DECIMAL(18,2),
    PRIMARY KEY (MaPN, MaSP),
    CONSTRAINT chk_ctpn_ct  CHECK (SLTheoChungTu >= 0),
    CONSTRAINT chk_ctpn_tt  CHECK (SLThucNhap >= 0),
    CONSTRAINT chk_ctpn_gia CHECK (GiaNhap >= 0),
    FOREIGN KEY (MaPN) REFERENCES PhieuNhapKho(MaPN),
    FOREIGN KEY (MaSP) REFERENCES SanPham(MaSP)
);

CREATE TABLE PhieuXuatKho (
    MaPX      VARCHAR(20)  PRIMARY KEY,
    NgayXuat  DATETIME     DEFAULT CURRENT_TIMESTAMP,
    TrangThai VARCHAR(50)  DEFAULT 'Hoàn thành',    -- 'Hoàn thành' / 'Hủy'
    GhiChu    VARCHAR(255),
    MaNV      VARCHAR(20)  NOT NULL,
    MaKH      VARCHAR(20)  NOT NULL,
    FOREIGN KEY (MaNV) REFERENCES NhanVien(MaNV),
    FOREIGN KEY (MaKH) REFERENCES KhachHang(MaKH)
);

CREATE TABLE ChiTietPhieuXuat (
    MaPX   VARCHAR(20) NOT NULL,
    MaSP   VARCHAR(20) NOT NULL,
    SLXuat INT         NOT NULL,
    GiaBan DECIMAL(18,2),                           -- gia ban luc xuat (lay tu SanPham.GiaBan)
    PRIMARY KEY (MaPX, MaSP),
    CONSTRAINT chk_ctpx_sl CHECK (SLXuat > 0),
    FOREIGN KEY (MaPX) REFERENCES PhieuXuatKho(MaPX),
    FOREIGN KEY (MaSP) REFERENCES SanPham(MaSP)
);

-- ---- Nhom bang KIEM KE (de bai khong yeu cau, co the bo neu thieu thoi gian) ----

CREATE TABLE PhieuKiemKe (
    MaPK      VARCHAR(20)  PRIMARY KEY,
    NgayKiem  DATETIME     DEFAULT CURRENT_TIMESTAMP,
    TrangThai VARCHAR(50)  DEFAULT 'Hoàn thành',
    GhiChu    VARCHAR(255)
);

CREATE TABLE ChiTietPhieuKiem (
    MaPK     VARCHAR(20)  NOT NULL,
    MaSP     VARCHAR(20)  NOT NULL,
    SLSoSach INT          NOT NULL,                 -- so luong tren so sach tai thoi diem kiem
    SLThucTe INT,                                   -- so luong dem thuc te
    LyDo     VARCHAR(255),
    PRIMARY KEY (MaPK, MaSP),
    CONSTRAINT chk_ctpk_ss CHECK (SLSoSach >= 0),
    FOREIGN KEY (MaPK) REFERENCES PhieuKiemKe(MaPK),
    FOREIGN KEY (MaSP) REFERENCES SanPham(MaSP)
);

CREATE TABLE ThamGiaKiemKe (
    MaNV       VARCHAR(20)  NOT NULL,
    MaPK       VARCHAR(20)  NOT NULL,
    VaiTroKiem VARCHAR(100),
    PRIMARY KEY (MaNV, MaPK),
    FOREIGN KEY (MaNV) REFERENCES NhanVien(MaNV),
    FOREIGN KEY (MaPK) REFERENCES PhieuKiemKe(MaPK)
);


-- PHAN 2: DU LIEU MAU DE DEMO

INSERT INTO DanhMuc (MaDM, TenDM) VALUES
('DM01', 'Bút viết'),
('DM02', 'Giấy in'),
('DM03', 'Dụng cụ văn phòng'),
('DM04', 'Sổ tập');

INSERT INTO KeHang (MaKe, TenKe, SucChuaToiDa) VALUES
('K01', 'Kệ A - Bút và dụng cụ', 1000),
('K02', 'Kệ B - Giấy in', 300),
('K03', 'Kệ C - Sổ tập', 500);

INSERT INTO NhaCC (MaNCC, TenNCC, DiaChi, Sdt, TrangThai) VALUES
('NCC01', 'Công ty CP Thiên Long', '90 Đường Tây Thạnh, Quận Tân Phú, TP.HCM', '0281234567', 'Hoạt động'),
('NCC02', 'Công ty CP Hồng Hà', '25 Lý Thường Kiệt, Quận Hoàn Kiếm, Hà Nội', '0241234567', 'Hoạt động'),
('NCC03', 'Công ty TNHH Giấy Double A', '12 Nguyễn Văn Linh, Quận 7, TP.HCM', '0287654321', 'Hoạt động'),
('NCC04', 'Công ty TNHH Văn phòng phẩm Minh Phát', '45 Trần Hưng Đạo, Quận 5, TP.HCM', '0289998888', 'Ngừng hợp tác');

INSERT INTO NhanVien (MaNV, HoTen, Sdt) VALUES
('NV01', 'Nguyễn Văn An', '0901000001'),
('NV02', 'Trần Thị Bình', '0901000002'),
('NV03', 'Lê Văn Cường', '0901000003'),
('NV04', 'Phạm Thị Dung', '0901000004');

INSERT INTO VaiTro (MaVT, TenVT, MoTa) VALUES
('VT01', 'Admin', 'Quản lý tài khoản, nhân viên và phân quyền'),
('VT02', 'Quản lý kho', 'Quản lý sản phẩm, nhà cung cấp, phiếu nhập và thống kê'),
('VT03', 'Nhân viên kho', 'Xem hàng hóa, lập phiếu nhập và phiếu xuất');

-- Mat khau demo cua ca 3 tai khoan la: 123456
-- Dang dung ma bam SHA-256. Neu code backend dung kieu bam khac (vd bcrypt)
-- thi chi can thay chuoi MatKhau cho khop.
INSERT INTO TaiKhoan (MaTK, TenDangNhap, MatKhau, TrangThai, MaVT, MaNV) VALUES
('TK01', 'admin',    SHA2('123456', 256), 'Hoạt động', 'VT01', 'NV01'),
('TK02', 'quanly',   SHA2('123456', 256), 'Hoạt động', 'VT02', 'NV02'),
('TK03', 'nhanvien', SHA2('123456', 256), 'Hoạt động', 'VT03', 'NV03');

-- SLTonKho da tinh san = tong nhap - tong xuat o cac phieu mau ben duoi
INSERT INTO SanPham (MaSP, TenSP, DonViTinh, SLTonKho, SLToiThieu, GiaBan, TrangThai, MaDM, MaNCC, MaKe) VALUES
('SP01', 'Bút bi Thiên Long TL-027',   'Cây',    150, 20, 5000.00,  'Đang kinh doanh', 'DM01', 'NCC01', 'K01'),
('SP02', 'Bút gel 0.5mm',              'Cây',     80, 20, 8000.00,  'Đang kinh doanh', 'DM01', 'NCC01', 'K01'),
('SP03', 'Giấy A4 Double A 70gsm',     'Ream',    70, 20, 75000.00, 'Đang kinh doanh', 'DM02', 'NCC03', 'K02'),
('SP04', 'Giấy A5 70gsm',              'Ream',    35, 40, 40000.00, 'Đang kinh doanh', 'DM02', 'NCC03', 'K02'),
('SP05', 'Tập học sinh 96 trang',      'Quyển',  120, 30, 9000.00,  'Đang kinh doanh', 'DM04', 'NCC02', 'K03'),
('SP06', 'Sổ lò xo A5',                'Quyển',   50, 15, 22000.00, 'Đang kinh doanh', 'DM04', 'NCC02', 'K03'),
('SP07', 'Kẹp giấy',                   'Hộp',     40, 10, 6000.00,  'Đang kinh doanh', 'DM03', 'NCC01', 'K01'),
('SP08', 'Bấm kim số 10',              'Cái',     30, 10, 18000.00, 'Đang kinh doanh', 'DM03', 'NCC01', 'K01');

INSERT INTO KhachHang (MaKH, TenKH, Sdt, DiaChi) VALUES
('KH01', 'Công ty TNHH ABC',          '0287771111', '10 Lê Lợi, Quận 1, TP.HCM'),
('KH02', 'Trường THPT Nguyễn Trãi',   '0287772222', '55 Nguyễn Trãi, Quận 5, TP.HCM'),
('KH03', 'Cửa hàng Sách Minh Anh',    '0287773333', '78 Cách Mạng Tháng 8, Quận 3, TP.HCM');

-- ---- Phieu nhap ----
INSERT INTO PhieuNhapKho (MaPN, NgayNhap, TrangThai, GhiChu, MaNV, MaNCC) VALUES
('PN01', '2026-10-05 09:00:00', 'Hoàn thành', 'Nhập bút và dụng cụ',          'NV03', 'NCC01'),
('PN02', '2026-10-20 14:30:00', 'Hoàn thành', 'Giấy A5 giao thiếu 5 ream',    'NV03', 'NCC03'),
('PN03', '2026-11-03 10:15:00', 'Hoàn thành', 'Nhập sổ tập và bấm kim',       'NV02', 'NCC02');

INSERT INTO ChiTietPhieuNhap (MaPN, MaSP, SLTheoChungTu, SLThucNhap, GiaNhap) VALUES
('PN01', 'SP01', 200, 200, 3000.00),
('PN01', 'SP02', 100, 100, 5500.00),
('PN01', 'SP07',  50,  50, 3500.00),
('PN02', 'SP03',  80,  80, 62000.00),
('PN02', 'SP04',  45,  40, 30000.00),
('PN03', 'SP05', 150, 150, 6500.00),
('PN03', 'SP06',  60,  60, 15000.00),
('PN03', 'SP08',  30,  30, 12000.00);

-- ---- Phieu xuat ----
INSERT INTO PhieuXuatKho (MaPX, NgayXuat, TrangThai, GhiChu, MaNV, MaKH) VALUES
('PX01', '2026-10-25 08:45:00', 'Hoàn thành', 'Xuất cho công ty ABC',     'NV03', 'KH01'),
('PX02', '2026-11-08 15:00:00', 'Hoàn thành', 'Xuất cho trường học',      'NV03', 'KH02'),
('PX03', '2026-11-15 11:20:00', 'Hoàn thành', 'Xuất cho cửa hàng sách',   'NV02', 'KH03');

INSERT INTO ChiTietPhieuXuat (MaPX, MaSP, SLXuat, GiaBan) VALUES
('PX01', 'SP01', 50, 5000.00),
('PX01', 'SP03', 10, 75000.00),
('PX02', 'SP05', 30, 9000.00),
('PX02', 'SP02', 20, 8000.00),
('PX02', 'SP06', 10, 22000.00),
('PX03', 'SP04',  5, 40000.00),
('PX03', 'SP07', 10, 6000.00);

-- ---- Kiem ke (tuy chon) ----
INSERT INTO PhieuKiemKe (MaPK, NgayKiem, TrangThai, GhiChu) VALUES
('PK01', '2026-11-18 16:00:00', 'Hoàn thành', 'Kiểm kê cuối tháng 11');

INSERT INTO ChiTietPhieuKiem (MaPK, MaSP, SLSoSach, SLThucTe, LyDo) VALUES
('PK01', 'SP01', 150, 148, 'Hao hụt, mất 2 cây'),
('PK01', 'SP03',  70,  70, NULL);

INSERT INTO ThamGiaKiemKe (MaNV, MaPK, VaiTroKiem) VALUES
('NV02', 'PK01', 'Trưởng nhóm kiểm kê'),
('NV04', 'PK01', 'Người đếm hàng');

