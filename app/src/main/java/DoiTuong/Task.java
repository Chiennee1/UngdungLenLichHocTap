package DoiTuong;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

@IgnoreExtraProperties
public class Task {
    @Exclude
    public String maNhiemVu;

    public String tieuDe;
    public String moTa;
    public long hanChot;
    public String mucDoUuTien;
    public boolean daHoanThanh;
    public String maMonHoc;
    public long thoiGianTao;
    public long thoiGianHoanThanh;
    public String hinhAnh;

    public Task() {
        this.thoiGianTao = System.currentTimeMillis();
        this.daHoanThanh = false;
    }

    public Task(String tieuDe, String moTa, long hanChot, String mucDoUuTien, String maMonHoc) {
        this.tieuDe = tieuDe;
        this.moTa = moTa;
        this.hanChot = hanChot;
        this.mucDoUuTien = mucDoUuTien;
        this.maMonHoc = maMonHoc;
        this.daHoanThanh = false;
        this.thoiGianTao = System.currentTimeMillis();
    }

    // Getters and Setters
    @Exclude
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

    public long getHanChot() {
        return hanChot;
    }

    public void setHanChot(long hanChot) {
        this.hanChot = hanChot;
    }

    public String getMucDoUuTien() {
        return mucDoUuTien;
    }

    public void setMucDoUuTien(String mucDoUuTien) {
        this.mucDoUuTien = mucDoUuTien;
    }

    public boolean isDaHoanThanh() {
        return daHoanThanh;
    }

    public void setDaHoanThanh(boolean daHoanThanh) {
        this.daHoanThanh = daHoanThanh;
    }

    public String getMaMonHoc() {
        return maMonHoc;
    }

    public void setMaMonHoc(String maMonHoc) {
        this.maMonHoc = maMonHoc;
    }

    public long getThoiGianTao() {
        return thoiGianTao;
    }

    public void setThoiGianTao(long thoiGianTao) {
        this.thoiGianTao = thoiGianTao;
    }

    public long getThoiGianHoanThanh() {
        return thoiGianHoanThanh;
    }

    public void setThoiGianHoanThanh(long thoiGianHoanThanh) {
        this.thoiGianHoanThanh = thoiGianHoanThanh;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    @Exclude
    public boolean isQuaHan() {
        return !daHoanThanh && System.currentTimeMillis() > hanChot;
    }
    @Exclude
    public Date getNgayHanChot() {
        return new Date(hanChot);
    }
    @Override
    public String toString() {
        return "Task{" +
                "maNhiemVu='" + maNhiemVu + '\'' +
                ", tieuDe='" + tieuDe + '\'' +
                ", hanChot=" + new Date(hanChot) +
                ", mucDoUuTien='" + mucDoUuTien + '\'' +
                ", daHoanThanh=" + daHoanThanh +
                ", maMonHoc='" + maMonHoc + '\'' +
                '}';
    }
}