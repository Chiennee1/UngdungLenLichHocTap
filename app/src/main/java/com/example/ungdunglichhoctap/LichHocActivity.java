package com.example.ungdunglichhoctap;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import DoiTuong.Lessons;
import XulyObject.FirebaseManager;
import XulyObject.LessonAdapter;

public class LichHocActivity extends AppCompatActivity {

    private TextView btnBack, textCurrentWeek, textView2;
    private Button btnWeeklyMode, btnDailyMode;
    private RecyclerView recyclerSchedule;
    private FloatingActionButton btnAddClass;
    private LinearLayout navHome, navCalendar, navPomodoro, navProfile;

    private FirebaseManager firebaseManager;
    private List<Lessons> allLessons = new ArrayList<>();
    private List<Lessons> displayedLessons = new ArrayList<>();
    private LessonAdapter lessonAdapter;

    private boolean isWeeklyMode = true;
    private Calendar currentCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lichhoc);

        initViews();
        setupClickListeners();

        firebaseManager = new FirebaseManager();

        // Initialize RecyclerView and adapter
        lessonAdapter = new LessonAdapter(displayedLessons, lesson -> {
            // Handle lesson click - show details dialog
            showLessonDetailsDialog(lesson);
        });

        recyclerSchedule.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerSchedule.setAdapter(lessonAdapter);

        // Set initial view
        updateWeekText();
        loadLessons();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        textCurrentWeek = findViewById(R.id.textCurrentWeek);
        textView2 = findViewById(R.id.textView2);
        btnWeeklyMode = findViewById(R.id.btnWeeklyMode);
        btnDailyMode = findViewById(R.id.btnDailyMode);
        recyclerSchedule = findViewById(R.id.recyclerSchedule);
        btnAddClass = findViewById(R.id.btnAddClass);

        navHome = findViewById(R.id.navHome);
        navCalendar = findViewById(R.id.navCalendar);
        navPomodoro = findViewById(R.id.navPomodoro);
        navProfile = findViewById(R.id.navProfile);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> {
            finish();
        });

        btnWeeklyMode.setOnClickListener(v -> {
            isWeeklyMode = true;
            updateDisplayMode();
        });

        btnDailyMode.setOnClickListener(v -> {
            isWeeklyMode = false;
            updateDisplayMode();
        });

        btnAddClass.setOnClickListener(v -> {
            // Navigate to add class activity
            Intent intent = new Intent(LichHocActivity.this, AddLessonActivity.class);
            startActivity(intent);
        });

        navHome.setOnClickListener(v -> {
            Intent intent = new Intent(LichHocActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish();
        });

        navCalendar.setOnClickListener(v -> {
            // Already in calendar view
        });

        navPomodoro.setOnClickListener(v -> {
            Intent intent = new Intent(LichHocActivity.this, PromodoroActivity.class);
            startActivity(intent);
            finish();
        });

        navProfile.setOnClickListener(v -> {
            Intent intent = new Intent(LichHocActivity.this, ProfileActivity.class);
            startActivity(intent);
        });
    }

    private void loadLessons() {
        if (!firebaseManager.isUserLoggedIn()) {
            Toast.makeText(this, "Vui lòng đăng nhập để xem lịch học", Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseManager.getLessons(new FirebaseManager.DatabaseCallback<List<Lessons>>() {
            @Override
            public void onSucces(List<Lessons> dataResult) {
                allLessons = dataResult;
                // Sort lessons by start time
                Collections.sort(allLessons, new Comparator<Lessons>() {
                    @Override
                    public int compare(Lessons l1, Lessons l2) {
                        return Long.compare(l1.getThoiGianBatDau(), l2.getThoiGianBatDau());
                    }
                });
                updateDisplayMode();
            }

            @Override
            public void onError(String error) {
                Log.e("LichHocActivity", "Error loading lessons: " + error);
                Toast.makeText(LichHocActivity.this, "Lỗi tải lịch học: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateDisplayMode() {
        if (isWeeklyMode) {
            btnWeeklyMode.setBackgroundTintList(getResources().getColorStateList(R.color.primary_blue));
            btnWeeklyMode.setTextColor(getResources().getColor(R.color.accent_orange));
            btnDailyMode.setBackgroundTintList(getResources().getColorStateList(R.color.primary_blue_light));
            btnDailyMode.setTextColor(getResources().getColor(R.color.black));
            displayWeeklyLessons();
        } else {
            btnDailyMode.setBackgroundTintList(getResources().getColorStateList(R.color.primary_blue));
            btnDailyMode.setTextColor(getResources().getColor(R.color.accent_bronze));
            btnWeeklyMode.setBackgroundTintList(getResources().getColorStateList(R.color.primary_blue_light));
            btnWeeklyMode.setTextColor(getResources().getColor(R.color.black));
            displayDailyLessons();
        }
    }

    private void displayWeeklyLessons() {
        // Get first day and last day of current week
        Calendar calendar = (Calendar) currentCalendar.clone();
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        long startOfWeek = getStartOfDay(calendar.getTimeInMillis());

        calendar.add(Calendar.DAY_OF_WEEK, 6);
        long endOfWeek = getEndOfDay(calendar.getTimeInMillis());

        displayedLessons.clear();
        for (Lessons lesson : allLessons) {
            if (lesson.getThoiGianBatDau() >= startOfWeek && lesson.getThoiGianBatDau() <= endOfWeek) {
                displayedLessons.add(lesson);
            }
        }

        lessonAdapter.notifyDataSetChanged();
        updateWeekText();
    }

    private void displayDailyLessons() {
        long startOfDay = getStartOfDay(currentCalendar.getTimeInMillis());
        long endOfDay = getEndOfDay(currentCalendar.getTimeInMillis());

        displayedLessons.clear();
        for (Lessons lesson : allLessons) {
            if (lesson.getThoiGianBatDau() >= startOfDay && lesson.getThoiGianBatDau() <= endOfDay) {
                displayedLessons.add(lesson);
            }
        }

        lessonAdapter.notifyDataSetChanged();
        updateDayText();
    }

    private void updateWeekText() {
        Calendar calendar = (Calendar) currentCalendar.clone();
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        Date startDate = calendar.getTime();

        calendar.add(Calendar.DAY_OF_WEEK, 6);
        Date endDate = calendar.getTime();

        SimpleDateFormat dayMonthFormat = new SimpleDateFormat("d 'tháng' M", Locale.getDefault());
        String weekText = "Tuần của " + dayMonthFormat.format(startDate) + " – " + dayMonthFormat.format(endDate);

        textView2.setText(weekText);
        textCurrentWeek.setText("Tuần hiện tại");
    }

    private void updateDayText() {
        Date currentDate = currentCalendar.getTime();
        SimpleDateFormat dayMonthFormat = new SimpleDateFormat("d 'tháng' M", Locale.getDefault());
        String dayText = "Ngày " + dayMonthFormat.format(currentDate);

        textView2.setText(dayText);
        textCurrentWeek.setText("Ngày hiện tại");
    }

    private long getStartOfDay(long timeInMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    private long getEndOfDay(long timeInMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTimeInMillis();
    }

    private void showLessonDetailsDialog(Lessons lesson) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_lesson_details);

        TextView tvTitle = dialog.findViewById(R.id.tvLessonTitle);
        TextView tvTime = dialog.findViewById(R.id.tvLessonTime);
        TextView tvRoom = dialog.findViewById(R.id.tvLessonRoom);
        TextView tvDescription = dialog.findViewById(R.id.tvLessonDescription);
        Button btnEdit = dialog.findViewById(R.id.btnEditLesson);
        Button btnDelete = dialog.findViewById(R.id.btnDeleteLesson);

        tvTitle.setText(lesson.getTieuDe());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        String dateStr = dateFormat.format(new Date(lesson.getThoiGianBatDau()));
        String startTimeStr = timeFormat.format(new Date(lesson.getThoiGianBatDau()));
        String endTimeStr = timeFormat.format(new Date(lesson.getThoiGianKetThuc()));

        tvTime.setText(dateStr + " " + startTimeStr + " - " + endTimeStr);
        tvRoom.setText("Phòng: " + lesson.getViTriPhong());
        tvDescription.setText(lesson.getMoTa() != null ? lesson.getMoTa() : "Không có mô tả");

        btnEdit.setOnClickListener(v -> {
            dialog.dismiss();
            // Navigate to edit lesson activity
            Intent intent = new Intent(LichHocActivity.this, AddLessonActivity.class);
            intent.putExtra("LESSON_ID", lesson.getMaBuoiHoc());
            startActivity(intent);
        });

        btnDelete.setOnClickListener(v -> {
            dialog.dismiss();
            deleteLesson(lesson);
        });

        dialog.show();
    }

    private void deleteLesson(Lessons lesson) {
        // Confirm deletion
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Xóa lớp học");
        builder.setMessage("Bạn có chắc chắn muốn xóa lớp học này?");
        builder.setPositiveButton("Xóa", (dialog, which) -> {
            if (lesson.getMaBuoiHoc() != null) {
                firebaseManager.deleteLesson(lesson.getMaBuoiHoc(), new FirebaseManager.DatabaseCallback<Boolean>() {
                    @Override
                    public void onSucces(Boolean dataResult) {
                        Toast.makeText(LichHocActivity.this, "Đã xóa lớp học", Toast.LENGTH_SHORT).show();
                        loadLessons(); // Reload all lessons
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(LichHocActivity.this, "Lỗi khi xóa: " + error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        builder.setNegativeButton("Hủy", null);
        builder.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadLessons();
    }
}