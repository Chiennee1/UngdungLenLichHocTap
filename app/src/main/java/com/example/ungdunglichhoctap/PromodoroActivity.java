package com.example.ungdunglichhoctap;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class PromodoroActivity extends AppCompatActivity {

    private CardView cardGamification, cardSmartPomodoro, cardTasksIntegration, cardLearningAnalytics;
    private LinearLayout navHome, navTasks, navLichHoc, navProfile;
    private TextView btnBack;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promodoro);
        findViews();
        setListeners();

    }

    private void setListeners() {
        cardGamification.setOnClickListener(v -> {
            Toast.makeText(PromodoroActivity.this, "Chuyeen sang thành tựu", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(PromodoroActivity.this, AchievementActivity.class);
            startActivity(intent);
        });

        cardSmartPomodoro.setOnClickListener(v -> {
            Toast.makeText(PromodoroActivity.this, "Smart Pomodoro feature coming soon!", Toast.LENGTH_SHORT).show();
        });

        cardTasksIntegration.setOnClickListener(v -> {
            Toast.makeText(PromodoroActivity.this, "Tasks Integration feature coming soon!", Toast.LENGTH_SHORT).show();
        });

        cardLearningAnalytics.setOnClickListener(v -> {
            Toast.makeText(PromodoroActivity.this, "Learning Analytics feature coming soon!", Toast.LENGTH_SHORT).show();
        });

        navHome.setOnClickListener(v -> {
            Intent intent = new Intent(PromodoroActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish();
        });

        navTasks.setOnClickListener(v -> {
            Intent intent = new Intent(PromodoroActivity.this, ManagerTaskActivity.class);
            startActivity(intent);
            finish();
        });

        navLichHoc.setOnClickListener(v -> {
            Intent intent = new Intent(PromodoroActivity.this, LichHocActivity.class);
            startActivity(intent);
            finish();
        });

        navProfile.setOnClickListener(v -> {
            Intent intent = new Intent(PromodoroActivity.this, ProfileActivity.class);
            startActivity(intent);
            finish();
        });
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(PromodoroActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish();
        });

    }

    private void findViews() {
        cardGamification = findViewById(R.id.cardGamification);
        cardSmartPomodoro = findViewById(R.id.cardSmartPomodoro);
        cardTasksIntegration = findViewById(R.id.cardTasksIntegration);
        cardLearningAnalytics = findViewById(R.id.cardLearningAnalytics);
        navHome = findViewById(R.id.navHome);
        navTasks = findViewById(R.id.navTasks);
        navLichHoc = findViewById(R.id.navLichHoc);
        navProfile = findViewById(R.id.navProfile);
        btnBack = findViewById(R.id.btnBack);
    }
}
