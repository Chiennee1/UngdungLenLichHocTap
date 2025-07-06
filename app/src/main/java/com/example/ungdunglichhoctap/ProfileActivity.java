package com.example.ungdunglichhoctap;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

import DoiTuong.PromodoSessions;
import DoiTuong.User;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";
    private TextView tvTotalStudyTime, tvPomodoroSessions, tvCompletedTasks, tvStudyStreak;
    private View btnChangeProfilePhoto;
    private static final int PICK_IMAGE_REQUEST = 1001;

    // UI components
    private TextView tvUserName, tvFullName, tvEmail, tvStudentId, tvSchool, tvMajor;
    private TextView btnBack, btnEditPersonal, btnViewAllAchievements;
    private Button btnEditProfile;
    private LinearLayout btnLogout;
    private ImageView imgProfile;

    // Firebase
    private FirebaseManager firebaseManager;
    private User currentUser;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        firebaseManager = new FirebaseManager();
        storageReference = FirebaseStorage.getInstance().getReference();

        initViews();
        setupClickListeners();
        loadUserData();
        loadStudyStatistics();
    }

    private void initViews() {
        tvUserName = findViewById(R.id.tvUserName);
        tvFullName = findViewById(R.id.tvFullName);
        tvEmail = findViewById(R.id.tvEmail);
        tvStudentId = findViewById(R.id.tvStudentId);
        tvSchool = findViewById(R.id.tvSchool);
        tvMajor = findViewById(R.id.tvMajor);
        imgProfile = findViewById(R.id.imgProfile);
        btnBack = findViewById(R.id.btnBack);
        btnEditPersonal = findViewById(R.id.btnEditPersonal);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnLogout = findViewById(R.id.btnLogout);
        btnViewAllAchievements = findViewById(R.id.btnViewAllAchievements);
        tvTotalStudyTime = findViewById(R.id.tvTotalStudyTime);
        tvPomodoroSessions = findViewById(R.id.tvPomodoroSessions);
        tvCompletedTasks = findViewById(R.id.tvCompletedTasks);
        tvStudyStreak = findViewById(R.id.tvStudyStreak);
        btnChangeProfilePhoto = (View) imgProfile.getParent();
    }

    private void setupClickListeners() {
        // Back button
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        if (btnEditProfile != null) {
            btnEditProfile.setOnClickListener(v -> {
                TextView tvUserNameLabel = findViewById(R.id.tvUserName);

                EditText edtDisplayName = new EditText(this);
                edtDisplayName.setId(View.generateViewId());
                edtDisplayName.setText(currentUser.getTenHienThi());
                edtDisplayName.setTextSize(22);
                edtDisplayName.setTextColor(getResources().getColor(R.color.text_primary));
                edtDisplayName.setGravity(tvUserNameLabel.getGravity());

                ViewGroup parentUserName = (ViewGroup) tvUserNameLabel.getParent();
                int indexUserName = parentUserName.indexOfChild(tvUserNameLabel);

                parentUserName.removeView(tvUserNameLabel);
                parentUserName.addView(edtDisplayName, indexUserName);

                btnEditProfile.setText("Lưu");

                btnEditProfile.setOnClickListener(saveView -> {
                    String displayName = edtDisplayName.getText().toString().trim();

                    if (displayName.isEmpty()) {
                        Toast.makeText(this, "Vui lòng nhập tên hiển thị", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (currentUser != null) {
                        currentUser.setTenHienThi(displayName);
                        updateUserInfo();
                    }

                    parentUserName.removeView(edtDisplayName);
                    parentUserName.addView(tvUserNameLabel, indexUserName);
                    tvUserNameLabel.setText(displayName);

                    btnEditProfile.setText("Chỉnh sửa hồ sơ");
                    btnEditProfile.setOnClickListener(view -> setupClickListeners());
                });
            });
        }

        if (btnEditPersonal != null) {
            btnEditPersonal.setOnClickListener(v -> showEditPersonalDialog());
        }

        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> logoutUser());
        }

        if (btnViewAllAchievements != null) {
            btnViewAllAchievements.setOnClickListener(v -> {
                Toast.makeText(this, "Tính năng đang phát triển", Toast.LENGTH_SHORT).show();
            });
        }

        // Change profile photo
        if (btnChangeProfilePhoto != null) {
            btnChangeProfilePhoto.setOnClickListener(v -> openImagePicker());
        }
    }

    private void showEditPersonalDialog() {
        TextView tvFullNameLabel = findViewById(R.id.tvFullName);
        TextView tvStudentIdLabel = findViewById(R.id.tvStudentId);
        TextView tvSchoolLabel = findViewById(R.id.tvSchool);
        TextView tvMajorLabel = findViewById(R.id.tvMajor);
        TextView btnEdit = findViewById(R.id.btnEditPersonal);

        EditText edtFullName = new EditText(this);
        edtFullName.setId(View.generateViewId());
        edtFullName.setText(currentUser.getHoTen());
        edtFullName.setTextSize(16);
        edtFullName.setTextColor(getResources().getColor(R.color.text_primary));

        EditText edtStudentId = new EditText(this);
        edtStudentId.setId(View.generateViewId());
        edtStudentId.setText(currentUser.getMaSinhVien());
        edtStudentId.setTextSize(16);
        edtStudentId.setTextColor(getResources().getColor(R.color.text_primary));

        EditText edtSchool = new EditText(this);
        edtSchool.setId(View.generateViewId());
        edtSchool.setText(currentUser.getKhoaVien());
        edtSchool.setTextSize(16);
        edtSchool.setTextColor(getResources().getColor(R.color.text_primary));

        EditText edtMajor = new EditText(this);
        edtMajor.setId(View.generateViewId());
        edtMajor.setText(currentUser.getNganh());
        edtMajor.setTextSize(16);
        edtMajor.setTextColor(getResources().getColor(R.color.text_primary));

        // Lưu trữ các TextView gốc để khôi phục sau này
        ViewGroup parentFullName = (ViewGroup) tvFullNameLabel.getParent();
        ViewGroup parentStudentId = (ViewGroup) tvStudentIdLabel.getParent();
        ViewGroup parentSchool = (ViewGroup) tvSchoolLabel.getParent();
        ViewGroup parentMajor = (ViewGroup) tvMajorLabel.getParent();

        int indexFullName = parentFullName.indexOfChild(tvFullNameLabel);
        int indexStudentId = parentStudentId.indexOfChild(tvStudentIdLabel);
        int indexSchool = parentSchool.indexOfChild(tvSchoolLabel);
        int indexMajor = parentMajor.indexOfChild(tvMajorLabel);

        parentFullName.removeView(tvFullNameLabel);
        parentFullName.addView(edtFullName, indexFullName);

        parentStudentId.removeView(tvStudentIdLabel);
        parentStudentId.addView(edtStudentId, indexStudentId);

        parentSchool.removeView(tvSchoolLabel);
        parentSchool.addView(edtSchool, indexSchool);

        parentMajor.removeView(tvMajorLabel);
        parentMajor.addView(edtMajor, indexMajor);

        btnEdit.setText("Lưu");

        btnEdit.setOnClickListener(v -> {
            String fullName = edtFullName.getText().toString().trim();
            String studentId = edtStudentId.getText().toString().trim();
            String school = edtSchool.getText().toString().trim();
            String major = edtMajor.getText().toString().trim();

            if (currentUser != null) {
                currentUser.setHoTen(fullName);
                currentUser.setMaSinhVien(studentId);
                currentUser.setKhoaVien(school);
                currentUser.setNganh(major);
                updateUserInfo();
            }

            parentFullName.removeView(edtFullName);
            parentFullName.addView(tvFullNameLabel, indexFullName);
            tvFullNameLabel.setText(fullName);

            parentStudentId.removeView(edtStudentId);
            parentStudentId.addView(tvStudentIdLabel, indexStudentId);
            tvStudentIdLabel.setText(studentId);

            parentSchool.removeView(edtSchool);
            parentSchool.addView(tvSchoolLabel, indexSchool);
            tvSchoolLabel.setText(school);

            parentMajor.removeView(edtMajor);
            parentMajor.addView(tvMajorLabel, indexMajor);
            tvMajorLabel.setText(major);

            btnEdit.setText("Sửa");
            btnEdit.setOnClickListener(view -> showEditPersonalDialog());
        });
    }

    private void loadUserData() {
        if (!firebaseManager.isUserLoggedIn()) {
            Toast.makeText(this, "Vui lòng đăng nhập để xem thông tin cá nhân", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ProfileActivity.this, SiginActivity.class));
            finish();
            return;
        }
        firebaseManager.getUserInfo(new FirebaseManager.DatabaseCallback<User>() {
            @Override
            public void onSucces(User dataResult) {
                currentUser = dataResult;
                displayUserData();
                loadProfileImage();
            }
            @Override
            public void onError(String error) {
                Log.e(TAG, "Error loading user data: " + error);
                Toast.makeText(ProfileActivity.this, "Lỗi tải thông tin người dùng: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayUserData() {
        if (currentUser == null) return;
        tvUserName.setText(currentUser.getTenHienThi() != null ? currentUser.getTenHienThi() : "Chưa đặt tên");
        tvFullName.setText(currentUser.getHoTen() != null ? currentUser.getHoTen() : "Chưa cập nhật");
        tvEmail.setText(currentUser.getEmail() != null ? currentUser.getEmail() : "Chưa cập nhật");
        tvStudentId.setText(currentUser.getMaSinhVien() != null ? currentUser.getMaSinhVien() : "Chưa cập nhật");
        tvSchool.setText(currentUser.getKhoaVien() != null ? currentUser.getKhoaVien() : "Chưa cập nhật");
        tvMajor.setText(currentUser.getNganh() != null ? currentUser.getNganh() : "Chưa cập nhật");
    }

    private void loadProfileImage() {
        if (currentUser == null || currentUser.getAnhDaiDien() == null) return;
        String imageUrl = currentUser.getAnhDaiDien();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Picasso.get()
                    .load(imageUrl)
                    .placeholder(R.drawable.default_profile_image)
                    .error(R.drawable.default_profile_image)
                    .into(imgProfile);
        }
    }

    private void loadStudyStatistics() {
        firebaseManager.getUserData(FirebaseManager.DATA_TYPE_PROMODOTIME, PromodoSessions.class, new FirebaseManager.DatabaseCallback<List<PromodoSessions>>() {
            @Override
            public void onSucces(List<PromodoSessions> dataResult) {
                int totalMinutes = 0;
                for (PromodoSessions session : dataResult) {
                    totalMinutes += session.getTongThoiGianTapTrungTheoPhut();
                }

                int hours = totalMinutes / 60;
                int minutes = totalMinutes % 60;
                tvTotalStudyTime.setText(hours + " giờ " + minutes + " phút");
                tvPomodoroSessions.setText(dataResult.size() + " phiên");
            }

            @Override
            public void onError(String error) {
                Log.e(TAG, "Error loading pomodoro statistics: " + error);
                tvTotalStudyTime.setText("0 giờ 0 phút");
                tvPomodoroSessions.setText("0 phiên");
            }
        });
        firebaseManager.getUserData(FirebaseManager.DATA_TYPE_TASKS, DoiTuong.Task.class, new FirebaseManager.DatabaseCallback<List<DoiTuong.Task>>() {
            @Override
            public void onSucces(List<DoiTuong.Task> dataResult) {
                int completedCount = 0;
                for (DoiTuong.Task task : dataResult) {
                    if (task.isDaHoanThanh()) {
                        completedCount++;
                    }
                }
                tvCompletedTasks.setText(completedCount + " nhiệm vụ");
            }

            @Override
            public void onError(String error) {
                Log.e(TAG, "Error loading task statistics: " + error);
                tvCompletedTasks.setText("0 nhiệm vụ");
            }
        });

        calculateStudyStreak();
    }

    private void calculateStudyStreak() {
        if (currentUser == null) return;
        firebaseManager.getUserData(FirebaseManager.DATA_TYPE_PROMODOTIME, PromodoSessions.class, new FirebaseManager.DatabaseCallback<List<PromodoSessions>>() {
            @Override
            public void onSucces(List<PromodoSessions> dataResult) {
                int studyStreak = dataResult.size();
                tvStudyStreak.setText(studyStreak + " ngày");
            }

            @Override
            public void onError(String error) {
                Log.e(TAG, "Error loading study streak: " + error);
                tvStudyStreak.setText("0 ngày");
            }
        });
    }
    private void updateUserInfo() {
        if (currentUser == null) return;

        String userId = firebaseManager.getUserId();
        if (userId == null) {
            Toast.makeText(this, "Không thể xác định người dùng hiện tại", Toast.LENGTH_SHORT).show();
            return;
        }

        showLoading(true);
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
        userRef.setValue(currentUser)
            .addOnSuccessListener(aVoid -> {
                showLoading(false);
                Toast.makeText(ProfileActivity.this, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
                displayUserData();
            })
            .addOnFailureListener(e -> {
                showLoading(false);
                Log.e(TAG, "Error updating user data: " + e.getMessage());
                Toast.makeText(ProfileActivity.this, "Lỗi cập nhật thông tin: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
    }

    private void logoutUser() {
        new AlertDialog.Builder(this)
                .setTitle("Đăng xuất")
                .setMessage("Bạn có chắc chắn muốn đăng xuất?")
                .setPositiveButton("Đăng xuất", (dialog, which) -> {
                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(ProfileActivity.this, "Đã đăng xuất", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ProfileActivity.this, SiginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            uploadProfileImage(imageUri);
        }
    }
    private void uploadProfileImage(Uri imageUri) {
        if (firebaseManager.getUserId() == null) return;
        showLoading(true);
        StorageReference profileImagesRef = storageReference.child("profile_images/" + firebaseManager.getUserId() + ".jpg");
        profileImagesRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    profileImagesRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();

                        if (currentUser != null) {
                            currentUser.setAnhDaiDien(imageUrl);

                            updateUserInfo();

                            Picasso.get().load(imageUri).into(imgProfile);
                        }
                    });
                })
                .addOnFailureListener(e -> {
                    showLoading(false);
                    Toast.makeText(ProfileActivity.this, "Lỗi khi tải lên ảnh: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void showLoading(boolean isLoading) {
    }
}
