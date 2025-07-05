package DoiTuong;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

@IgnoreExtraProperties
public class PromodoSessions {
    @Exclude
    public String maPhienLamViec;

    public String maNguoiDung;
    public String maNhiemVu;
    public String tieuDe;
    public String moTa;
    public long thoiGianBatDau;
    public long thoiGianKetThuc;
    public int thoiGianTapTrung;
    public int thoiGianNghi;
    public int thoiGianNghiDai;
    public int soPomodoroDaHoanThanh;
    public int soPomodoroDuKien;
    public boolean daHoanThanh;
    public String trangThai;

    public PromodoSessions() {
    }

    public PromodoSessions(String maNguoiDung, String tieuDe, int thoiGianTapTrung, int thoiGianNghi,
                           int thoiGianNghiDai, int soPomodoroDuKien) {
        this.maNguoiDung = maNguoiDung;
        this.tieuDe = tieuDe;
        this.thoiGianTapTrung = thoiGianTapTrung;
        this.thoiGianNghi = thoiGianNghi;
        this.thoiGianNghiDai = thoiGianNghiDai;
        this.soPomodoroDuKien = soPomodoroDuKien;
        this.soPomodoroDaHoanThanh = 0;
        this.daHoanThanh = false;
        this.trangThai = "chuaBatDau";
        this.thoiGianBatDau = System.currentTimeMillis();
    }

    @Exclude
    public String getMaPhienLamViec() {
        return maPhienLamViec;
    }

    public void setMaPhienLamViec(String maPhienLamViec) {
        this.maPhienLamViec = maPhienLamViec;
    }

    public String getMaNguoiDung() {
        return maNguoiDung;
    }

    public void setMaNguoiDung(String maNguoiDung) {
        this.maNguoiDung = maNguoiDung;
    }

    public String getMaNhiemVu() {
        return maNhiemVu;
    }

    public void setMaNhiemVu(String maNhiemVu) {
        this.maNhiemVu = maNhiemVu;
    }

    public String getTieuDe() {
        return tieuDe;
    }

    public void setTieuDe(String tieuDe) {
        this.tieuDe = tieuDe;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public long getThoiGianBatDau() {
        return thoiGianBatDau;
    }

    public void setThoiGianBatDau(long thoiGianBatDau) {
        this.thoiGianBatDau = thoiGianBatDau;
    }

    public long getThoiGianKetThuc() {
        return thoiGianKetThuc;
    }

    public void setThoiGianKetThuc(long thoiGianKetThuc) {
        this.thoiGianKetThuc = thoiGianKetThuc;
    }

    public int getThoiGianTapTrung() {
        return thoiGianTapTrung;
    }

    public void setThoiGianTapTrung(int thoiGianTapTrung) {
        this.thoiGianTapTrung = thoiGianTapTrung;
    }

    public int getThoiGianNghi() {
        return thoiGianNghi;
    }

    public void setThoiGianNghi(int thoiGianNghi) {
        this.thoiGianNghi = thoiGianNghi;
    }

    public int getThoiGianNghiDai() {
        return thoiGianNghiDai;
    }

    public void setThoiGianNghiDai(int thoiGianNghiDai) {
        this.thoiGianNghiDai = thoiGianNghiDai;
    }

    public int getSoPomodoroDaHoanThanh() {
        return soPomodoroDaHoanThanh;
    }

    public void setSoPomodoroDaHoanThanh(int soPomodoroDaHoanThanh) {
        this.soPomodoroDaHoanThanh = soPomodoroDaHoanThanh;
    }

    public int getSoPomodoroDuKien() {
        return soPomodoroDuKien;
    }

    public void setSoPomodoroDuKien(int soPomodoroDuKien) {
        this.soPomodoroDuKien = soPomodoroDuKien;
    }

    public boolean isDaHoanThanh() {
        return daHoanThanh;
    }

    public void setDaHoanThanh(boolean daHoanThanh) {
        this.daHoanThanh = daHoanThanh;
        if (daHoanThanh) {
            this.thoiGianKetThuc = System.currentTimeMillis();
        }
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    // Phương thức bổ sung
    public void tangSoPomodoroDaHoanThanh() {
        this.soPomodoroDaHoanThanh++;
        if (this.soPomodoroDaHoanThanh >= this.soPomodoroDuKien) {
            this.daHoanThanh = true;
            this.thoiGianKetThuc = System.currentTimeMillis();
        }
    }

    @Exclude
    public Date getNgayBatDau() {
        return new Date(thoiGianBatDau);
    }

    @Exclude
    public Date getNgayKetThuc() {
        return thoiGianKetThuc > 0 ? new Date(thoiGianKetThuc) : null;
    }

    @Exclude
    public int getTongThoiGianTapTrungTheoPhut() {
        return soPomodoroDaHoanThanh * thoiGianTapTrung;
    }

    @Exclude
    public double getPhanTramHoanThanh() {
        if (soPomodoroDuKien == 0) return 0;
        return (soPomodoroDaHoanThanh * 100.0) / soPomodoroDuKien;
    }

    @Override
    public String toString() {
        return "PomodoroSession{" +
                "maPhienLamViec='" + maPhienLamViec + '\'' +
                ", tieuDe='" + tieuDe + '\'' +
                ", soPomodoroDaHoanThanh=" + soPomodoroDaHoanThanh +
                "/" + soPomodoroDuKien +
                ", trangThai='" + trangThai + '\'' +
                ", daHoanThanh=" + daHoanThanh +
                '}';
    }
}
