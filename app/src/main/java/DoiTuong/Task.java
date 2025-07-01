package DoiTuong;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Task {
    @Exclude
    public String maNhiemVu;

    public String tieuDe;
    public String moTa;
    public long hanChot; // Hạn chót (timestamp)
    public String mucDoUuTien;
    public boolean daHoanThanh;
    public String maMonHoc;
    public String maBuoiHoc;
    public String maNguoiTao;
    public Map<String, Boolean> danhSachNguoiThucHien;
    public long thoiGianTao;
    public long thoiGianHoanThanh;
    public long thoiGianNhacNho;

    public Task() {
        // Constructor mặc định cần thiết cho Firebase
        danhSachNguoiThucHien = new HashMap<>();
        this.thoiGianTao = System.currentTimeMillis();
    }
    public Task(String tieuDe, String moTa, long hanChot, String mucDoUuTien, String maNguoiTao) {
        this.tieuDe = tieuDe;
        this.moTa = moTa;
        this.hanChot = hanChot;
        this.mucDoUuTien = mucDoUuTien;
        this.maNguoiTao = maNguoiTao;
        this.daHoanThanh = false;
        this.danhSachNguoiThucHien = new HashMap<>();
        this.thoiGianTao = System.currentTimeMillis();
        this.danhSachNguoiThucHien.put(maNguoiTao, true); // Tự động thêm người tạo vào danh sách người được giao
    }

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
        if (daHoanThanh) {
            this.thoiGianHoanThanh = System.currentTimeMillis();
        } else {
            this.thoiGianHoanThanh = 0;
        }
    }

    public String getMaMonHoc() {
        return maMonHoc;
    }

    public void setMaMonHoc(String maMonHoc) {
        this.maMonHoc = maMonHoc;
    }

    public String getMaBuoiHoc() {
        return maBuoiHoc;
    }

    public void setMaBuoiHoc(String maBuoiHoc) {
        this.maBuoiHoc = maBuoiHoc;
    }

    public String getMaNguoiTao() {
        return maNguoiTao;
    }

    public void setMaNguoiTao(String maNguoiTao) {
        this.maNguoiTao = maNguoiTao;
    }

    public Map<String, Boolean> getDanhSachNguoiThucHien() {
        return danhSachNguoiThucHien;
    }

    public void setDanhSachNguoiThucHien(Map<String, Boolean> danhSachNguoiThucHien) {
        this.danhSachNguoiThucHien = danhSachNguoiThucHien;
    }

    public void themNguoiThucHien(String maUser) {
        if (danhSachNguoiThucHien == null) {
            danhSachNguoiThucHien = new HashMap<>();
        }
        danhSachNguoiThucHien.put(maUser, true);
    }

    public void xoaNguoiThucHien(String maUser) {
        if (danhSachNguoiThucHien != null) {
            danhSachNguoiThucHien.remove(maUser);
        }
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

    public long getThoiGianNhacNho() {
        return thoiGianNhacNho;
    }

    public void setThoiGianNhacNho(long thoiGianNhacNho) {
        this.thoiGianNhacNho = thoiGianNhacNho;
    }

    @Exclude
    public Date getNgayHanChot() {
        return new Date(hanChot);
    }

    @Exclude
    public boolean daQuaHan() {
        return !daHoanThanh && System.currentTimeMillis() > hanChot;
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
