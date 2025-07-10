package DoiTuong;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;

import java.io.Serializable;
import java.util.Date;

public class Task implements Serializable {
    private String maNhiemVu;
    private String tieuDe;
    private String moTa;
    private String tenMonHoc;
    private String mucDoUuTien;
    private Long hanChot;
    private Long thoiGianTao;
    private Long thoiGianHoanThanh;
    private boolean daHoanThanh;

    public Task() {
        this.thoiGianTao = System.currentTimeMillis();
    }

    public Task(String maNhiemVu, String tieuDe, String moTa, String tenMonHoc, String mucDoUuTien, Date hanChot) {
        this.maNhiemVu = maNhiemVu;
        this.tieuDe = tieuDe;
        this.moTa = moTa;
        this.tenMonHoc = tenMonHoc;
        this.mucDoUuTien = mucDoUuTien;
        this.hanChot = hanChot != null ? hanChot.getTime() : null;
        this.thoiGianTao = System.currentTimeMillis();
        this.daHoanThanh = false;
    }

    public Long getHanChot() {
        return hanChot;
    }

    public void setHanChot(Long hanChot) {
        this.hanChot = hanChot;
    }

    public Long getThoiGianHoanThanh() {
        return thoiGianHoanThanh;
    }

    public void setThoiGianHoanThanh(Long thoiGianHoanThanh) {
        this.thoiGianHoanThanh = thoiGianHoanThanh;
    }

    public Long getThoiGianTao() {
        return thoiGianTao;
    }

    public void setThoiGianTao(Long thoiGianTao) {
        this.thoiGianTao = thoiGianTao;
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

    public String getTenMonHoc() {
        return tenMonHoc;
    }

    public void setTenMonHoc(String tenMonHoc) {
        this.tenMonHoc = tenMonHoc;
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
        if (daHoanThanh) {
            this.thoiGianHoanThanh = System.currentTimeMillis();
        } else {
            this.thoiGianHoanThanh = null;
        }
    }

    @Override
    public String toString() {
        return "Task{" +
                "maNhiemVu='" + maNhiemVu + '\'' +
                ", tieuDe='" + tieuDe + '\'' +
                ", moTa='" + moTa + '\'' +
                ", tenMonHoc='" + tenMonHoc + '\'' +
                ", mucDoUuTien='" + mucDoUuTien + '\'' +
                ", hanChot=" + getHanChot() +
                ", daHoanThanh=" + daHoanThanh +
                '}';
    }
}