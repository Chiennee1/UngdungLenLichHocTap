package com.example.ungdunglichhoctap;

public class User {
    private String userId;
    private String email;
    private String displayName;
    private String phoneNumber;
    private String photoUrl;
    private String bio;
    private String birthDate;
    private String department;
    private String studentId;
    private boolean isEmailVerified;
    public User() {
    }

    // Constructor
    public User(String userId, String email, String displayName, String phoneNumber,
                String photoUrl, String bio, String birthDate, String department,
                String studentId, boolean isEmailVerified) {
        this.userId = userId;
        this.email = email;
        this.displayName = displayName;
        this.phoneNumber = phoneNumber;
        this.photoUrl = photoUrl;
        this.bio = bio;
        this.birthDate = birthDate;
        this.department = department;
        this.studentId = studentId;
        this.isEmailVerified = isEmailVerified;
    }

    // Constructor với thông tin cơ bản
    public User(String userId, String email, String displayName, String photoUrl) {
        this.userId = userId;
        this.email = email;
        this.displayName = displayName;
        this.photoUrl = photoUrl;
        this.isEmailVerified = false;
    }

    // Getters và Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public boolean isEmailVerified() {
        return isEmailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        isEmailVerified = emailVerified;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", email='" + email + '\'' +
                ", displayName='" + displayName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", department='" + department + '\'' +
                ", studentId='" + studentId + '\'' +
                '}';
    }
}
