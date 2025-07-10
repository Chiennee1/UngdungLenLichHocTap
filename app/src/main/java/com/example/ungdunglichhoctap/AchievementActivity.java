package com.example.ungdunglichhoctap;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AchievementActivity extends AppCompatActivity {
    private LinearLayout navHome, navTasks, navLichHoc, navProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);
        findViews();
        setListeners();

    }

    private void setListeners() {
        navHome.setOnClickListener(v -> {
            Intent intent = new Intent(AchievementActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish();
        });

        navTasks.setOnClickListener(v -> {
            Intent intent = new Intent(AchievementActivity.this, ManagerTaskActivity.class);
            startActivity(intent);
            finish();
        });

        navLichHoc.setOnClickListener(v -> {
            Intent intent = new Intent(AchievementActivity.this, LichHocActivity.class);
            startActivity(intent);
            finish();
        });

        navProfile.setOnClickListener(v -> {
            Intent intent = new Intent(AchievementActivity.this, ProfileActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void findViews() {
        navHome = findViewById(R.id.navHome);
        navTasks = findViewById(R.id.navTasks);
        navLichHoc = findViewById(R.id.navLichHoc);
        navProfile = findViewById(R.id.navProfile);
    }
}
