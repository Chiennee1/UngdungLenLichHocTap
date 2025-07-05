package DoiTuong;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

@IgnoreExtraProperties
public class Lessons {
    @Exclude
    public String maBuoiHoc;

    public String maMonHoc;
    public String tieuDe;
    public long thoiGianBatDau;
    public long thoiGianKetThuc;
    public String viTriPhong;
    public String moTa;
    public boolean daHoanThanh;
    public long thoiGianTao;
    public String loaiBuoiHoc; // "Lý thuyết", "Thực hành", "Seminar"
    public boolean coNhacNho;
    public long thoiGianNhacNho; // Phút trước buổi học để nhắc
    public String ghiChu;

    public Lessons() {
        this.thoiGianTao = System.currentTimeMillis();
        this.coNhacNho = true;
        this.thoiGianNhacNho = 15;
        this.loaiBuoiHoc = "Lý thuyết";
        this.daHoanThanh = false;
    }

    public Lessons(String maMonHoc, String tieuDe, long thoiGianBatDau, long thoiGianKetThuc, String viTriPhong, String moTa) {
        this.maMonHoc = maMonHoc;
        this.tieuDe = tieuDe;
        this.thoiGianBatDau = thoiGianBatDau;
        this.thoiGianKetThuc = thoiGianKetThuc;
        this.viTriPhong = viTriPhong;
        this.moTa = moTa;
        this.daHoanThanh = false;
        this.thoiGianTao = System.currentTimeMillis();
        this.coNhacNho = true;
        this.thoiGianNhacNho = 15;
        this.loaiBuoiHoc = "Lý thuyết";
    }

    public String getMaBuoiHoc() {
        return maBuoiHoc;
    }

    public void setMaBuoiHoc(String maBuoiHoc) {
        this.maBuoiHoc = maBuoiHoc;
    }

    public String getMaMonHoc() {
        return maMonHoc;
    }

    public void setMaMonHoc(String maMonHoc) {
        this.maMonHoc = maMonHoc;
    }

    public String getTieuDe() {
        return tieuDe;
    }

    public void setTieuDe(String tieuDe) {
        this.tieuDe = tieuDe;
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

    public String getViTriPhong() {
        return viTriPhong;
    }

    public void setViTriPhong(String viTriPhong) {
        this.viTriPhong = viTriPhong;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public boolean isDaHoanThanh() {
        return daHoanThanh;
    }

    public void setDaHoanThanh(boolean daHoanThanh) {
        this.daHoanThanh = daHoanThanh;
    }

    public long getThoiGianTao() {
        return thoiGianTao;
    }

    public void setThoiGianTao(long thoiGianTao) {
        this.thoiGianTao = thoiGianTao;
    }

    public String getLoaiBuoiHoc() {
        return loaiBuoiHoc;
    }

    public void setLoaiBuoiHoc(String loaiBuoiHoc) {
        this.loaiBuoiHoc = loaiBuoiHoc;
    }

    public boolean isCoNhacNho() {
        return coNhacNho;
    }

    public void setCoNhacNho(boolean coNhacNho) {
        this.coNhacNho = coNhacNho;
    }

    public long getThoiGianNhacNho() {
        return thoiGianNhacNho;
    }

    public void setThoiGianNhacNho(long thoiGianNhacNho) {
        this.thoiGianNhacNho = thoiGianNhacNho;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    @Exclude
    public long getThoiLuongPhut() {
        return (thoiGianKetThuc - thoiGianBatDau) / (1000 * 60);
    }

    @Exclude
    public boolean dangDienRa() {
        long currentTime = System.currentTimeMillis();
        return currentTime >= thoiGianBatDau && currentTime <= thoiGianKetThuc;
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "maBuoiHoc='" + maBuoiHoc + '\'' +
                ", tieuDe='" + tieuDe + '\'' +
                ", maMonHoc='" + maMonHoc + '\'' +
                ", thoiGianBatDau=" + new Date(thoiGianBatDau) +
                ", loaiBuoiHoc='" + loaiBuoiHoc + '\'' +
                ", daHoanThanh=" + daHoanThanh +
                '}';
    }
}