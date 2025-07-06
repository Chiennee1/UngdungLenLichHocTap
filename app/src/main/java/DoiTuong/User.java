package DoiTuong;

public class User {
    private String maUser;
    private String email;
    private String tenHienThi;
    private String anhDaiDien;
    private String maSinhVien;
    private boolean xacThucEmail;
    private String hoTen;
    private String nganh;
    private String khoaVien;
    public User() {
    }

    public User(String maUser, String email, String tenHienThi,
                String anhDaiDien, String maSinhVien, boolean daXacThucEmail,
                String hoTen, String nganh, String khoaVien) {
        this.maUser = maUser;
        this.email = email;
        this.tenHienThi = tenHienThi;
        this.anhDaiDien = anhDaiDien;
        this.maSinhVien = maSinhVien;
        this.xacThucEmail = daXacThucEmail;
        this.hoTen = hoTen;
        this.nganh = nganh;
        this.khoaVien = khoaVien;
    }

    // Constructor rút gọn cho đăng ký nhanh
    public User(String maUser, String email, String tenHienThi, String anhDaiDien) {
        this.maUser = maUser;
        this.email = email;
        this.tenHienThi = tenHienThi;
        this.anhDaiDien = anhDaiDien;
        this.xacThucEmail = false;
    }

    // Các getter và setter
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

    public String getAnhDaiDien() {
        return anhDaiDien;
    }

    public void setAnhDaiDien(String anhDaiDien) {
        this.anhDaiDien = anhDaiDien;
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

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getNganh() {
        return nganh;
    }

    public void setNganh(String nganh) {
        this.nganh = nganh;
    }

    @Override
    public String toString() {
        return "User{" +
                "maUser='" + maUser + '\'' +
                ", email='" + email + '\'' +
                ", tenHienThi='" + tenHienThi + '\'' +
                ", hoTen='" + hoTen + '\'' +
                ", anhDaiDien='" + anhDaiDien + '\'' +
                ", maSinhVien='" + maSinhVien + '\'' +
                ", khoaVien='" + khoaVien + '\'' +
                ", nganh='" + nganh + '\'' +
                ", xacThucEmail=" + xacThucEmail +
                '}';
    }
}
