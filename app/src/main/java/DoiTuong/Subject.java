package DoiTuong;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Subject {
    @Exclude
    public String maMonHoc;

    public String tenMonHoc;
    public String tenGiaoVien;
    public int soTinChi;
    public String maPhong;
    public boolean isSelected;

    public Subject() {
        this.isSelected = false;
    }

    public Subject(String tenMonHoc, String tenGiaoVien, int soTinChi, String maPhong) {
        this.tenMonHoc = tenMonHoc;
        this.tenGiaoVien = tenGiaoVien;
        this.soTinChi = soTinChi;
        this.maPhong = maPhong;
        this.isSelected = false;
    }

    @Exclude
    public String getMaMonHoc() {
        return maMonHoc;
    }

    public void setMaMonHoc(String maMonHoc) {
        this.maMonHoc = maMonHoc;
    }

    public String getTenMonHoc() {
        return tenMonHoc;
    }

    public void setTenMonHoc(String tenMonHoc) {
        this.tenMonHoc = tenMonHoc;
    }

    public String getTenGiaoVien() {
        return tenGiaoVien;
    }

    public void setTenGiaoVien(String tenGiaoVien) {
        this.tenGiaoVien = tenGiaoVien;
    }

    public int getSoTinChi() {
        return soTinChi;
    }

    public void setSoTinChi(int soTinChi) {
        this.soTinChi = soTinChi;
    }

    public String getMaPhong() {
        return maPhong;
    }

    public void setMaPhong(String maPhong) {
        this.maPhong = maPhong;
    }

    @Exclude
    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}