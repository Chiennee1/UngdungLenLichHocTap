package DoiTuong;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Subject {
    @Exclude
    public String id;

    public String tenMonHoc;
    public String maPhong;
    public String tenGiaoVien;
    public int soTinChi;

    public Subject() {
    }

    public Subject(String name, String code, String teacherName, int credits) {
        this.tenMonHoc = name;
        this.maPhong = code;
        this.tenGiaoVien = teacherName;
        this.soTinChi = credits;
    }
}
