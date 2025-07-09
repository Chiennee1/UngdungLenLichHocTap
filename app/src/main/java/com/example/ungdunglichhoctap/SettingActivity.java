package com.example.ungdunglichhoctap;


import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class SettingActivity extends AppCompatActivity {

    private TextView btnBack;
    private LinearLayout layoutNotificationSettings;
    private LinearLayout layoutThemeSettings;
    private LinearLayout layoutReminderSettings;
    private LinearLayout layoutAboutApp;
    private LinearLayout layoutBackupRestore;
    private LinearLayout layoutLogout;
    private FirebaseAuth mAuth;
    private FirebaseManager firebaseManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mAuth = FirebaseAuth.getInstance();
        firebaseManager = new FirebaseManager();
        findViews();
        setUpClickListeners();
    }
    private void  setUpClickListeners() {
        btnBack.setOnClickListener(v -> finish());
        layoutNotificationSettings.setOnClickListener(v -> {
            Intent intent = new Intent(SettingActivity.this, ThongBaoActivity.class);
            Toast.makeText(SettingActivity.this, "Chức năng này đang được phát triển", Toast.LENGTH_SHORT).show();
            startActivity(intent);
        });
        layoutAboutApp.setOnClickListener(v -> {
            Intent intent = new Intent(SettingActivity.this, AboutAppActivity.class);
            startActivity(intent);
        });
        layoutBackupRestore.setOnClickListener(v -> {
            Intent intent = new Intent(SettingActivity.this, BackupRestoreActivity.class);
            startActivity(intent);
        });
        layoutLogout.setOnClickListener(v -> logoutUser());
        layoutThemeSettings.setOnClickListener(v -> showThemeSelection());
        layoutReminderSettings.setOnClickListener(v -> {
            Toast.makeText(this, "Chức năng này đang được phát triển", Toast.LENGTH_SHORT).show();
        });
        }
    private void showThemeSelection() {
        String [] themes = {"Light", "Dark", "System Default"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn chủ đề");
        builder.setItems(themes, (dialog, which) -> {
            SharedPreferences sharedPreferences = getSharedPreferences("app_setting", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            switch (which) {
                case 0:
                    editor.putString("app_theme", "Light");
                    Toast.makeText(this, "Chủ đề sáng đã được chọn", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    editor.putString("app_theme", "Dark");
                    Toast.makeText(this, "Chủ đề tối đã được chọn", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    editor.putString("app_theme", "system");
                    Toast.makeText(this, "Chủ đề hệ thống đã được chọn", Toast.LENGTH_SHORT).show();
                    break;
            }
            editor.apply();
            applyTheme();
            Toast.makeText(SettingActivity.this, "Chủ đề đã được thay đổi", Toast.LENGTH_SHORT).show();
        });
        builder.show();
    }
    private void applyTheme() {
        SharedPreferences sharedPreferences = getSharedPreferences("app_setting", MODE_PRIVATE);
        String theme = sharedPreferences.getString("app_theme", "system");
        TextView themeTextView = findViewById(R.id.txtTheme);
        if(themeTextView != null) {
            switch (theme) {
                case "light":
                    getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.background_light));
                    themeTextView.setText("Chủ đề: Sáng");
                    break;
                case "dark":
                    getWindow().getDecorView().setBackgroundColor(getResources().
                            getColor(R.color.accent_bronze));
                    themeTextView.setText("Chủ đề: Tối");
                    break;
                case "system":
                    getWindow().getDecorView().setBackgroundColor(getResources().
                            getColor(R.color.primary_blue_light));
                    themeTextView.setText("Chủ đề: Hệ thống");
                    break;
                default:
                    themeTextView.setText("Chủ đề: Mặc định");
            }
        }
        Intent intent = new Intent(this, DashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void findViews() {
        btnBack = findViewById(R.id.btnBack);
        layoutNotificationSettings = findViewById(R.id.layoutNotificationSettings);
        layoutThemeSettings = findViewById(R.id.layoutThemeSettings);
        layoutReminderSettings = findViewById(R.id.layoutReminderSettings);
        layoutAboutApp = findViewById(R.id.layoutAboutApp);
        layoutBackupRestore = findViewById(R.id.layoutBackupRestore);
        layoutLogout = findViewById(R.id.layoutLogout);
    }

    private void logoutUser() {
        new AlertDialog.Builder(this)
                .setTitle("Đăng xuất")
                .setMessage("Bạn có chắc chắn muốn đăng xuất?")
                .setPositiveButton("Đăng xuất", (dialog, which) -> {
                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(SettingActivity.this, "Đã đăng xuất", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SettingActivity.this, SiginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
