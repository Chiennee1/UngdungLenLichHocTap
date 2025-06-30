package DoiTuong;

public class User {
    private String maUser;
    private String email;
    private String tenHienThi;
    private String soDienThoai;
    private String anhDaiDien;
    private String tieySu;
    private String ngaySinh;
    private String khoaVien;
    private String maSinhVien;
    private boolean xacThucEmail;

    public User() {
    }

    // Constructor
    public User(String maUser, String email, String tenHienThi, String soDienThoai,
                String anhDaiDien, String tieySu, String ngaySinh, String khoaVien,
                String maSinhVien, boolean daXacThucEmail) {
        this.maUser = maUser;
        this.email = email;
        this.tenHienThi = tenHienThi;
        this.soDienThoai = soDienThoai;
        this.anhDaiDien = anhDaiDien;
        this.tieySu = tieySu;
        this.ngaySinh = ngaySinh;
        this.khoaVien = khoaVien;
        this.maSinhVien = maSinhVien;
        this.xacThucEmail = daXacThucEmail;
    }

    public User(String maUser, String email, String tenHienThi, String anhDaiDien) {
        this.maUser = maUser;
        this.email = email;
        this.tenHienThi = tenHienThi;
        this.anhDaiDien = anhDaiDien;
        this.xacThucEmail = false;
    }

    public String getMaUser() {
        return maUser;
    }

    public void setMaUser(String maUser) {
        this.maUser = maUser;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTenHienThi() {
        return tenHienThi;
    }

    public void setTenHienThi(String tenHienThi) {
        this.tenHienThi = tenHienThi;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getAnhDaiDien() {
        return anhDaiDien;
    }

    public void setAnhDaiDien(String anhDaiDien) {
        this.anhDaiDien = anhDaiDien;
    }

    public String getTieySu() {
        return tieySu;
    }

    public void setTieySu(String tieySu) {
        this.tieySu = tieySu;
    }

    public String getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getKhoaVien() {
        return khoaVien;
    }

    public void setKhoaVien(String khoaVien) {
        this.khoaVien = khoaVien;
    }

    public String getMaSinhVien() {
        return maSinhVien;
    }

    public void setMaSinhVien(String maSinhVien) {
        this.maSinhVien = maSinhVien;
    }

    public boolean isDaXacThucEmail() {
        return xacThucEmail;
    }

    public void setDaXacThucEmail(boolean daXacThucEmail) {
        this.xacThucEmail = daXacThucEmail;
    }

    @Override
    public String toString() {
        return "User{" +
                "maUser='" + maUser + '\'' +
                ", email='" + email + '\'' +
                ", tenHienThi='" + tenHienThi + '\'' +
                ", soDienThoai='" + soDienThoai + '\'' +
                ", anhDaiDien='" + anhDaiDien + '\'' +
                ", khoaVien='" + khoaVien + '\'' +
                ", maSinhVien='" + maSinhVien + '\'' +
                '}';
    }
}
