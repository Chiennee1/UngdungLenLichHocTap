package com.example.ungdunglichhoctap;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import DoiTuong.Lessons;
import DoiTuong.Task;
import DoiTuong.User;

public class DashboardActivity extends AppCompatActivity {
    // Khai báo các biến cho task
    private TextView tvTaskTitle1, tvTaskDueDate1;
    private TextView tvTaskTitle2, tvTaskDueDate2;
    private TextView tvTaskProgress;

    // Khai báo các biến cho lessons
    private TextView tvLessonTitle1, tvLessonTime1, tvLessonRoom1;
    private TextView tvLessonTitle2, tvLessonTime2, tvLessonRoom2;
    private TextView tvLessonTitle3, tvLessonTime3, tvLessonRoom3;

    private TextView tvWelcome;
    private TextView tvDate;
    private TextView tvTimerDisplay;
    private Button btnStartTimer;
    private ProgressBar progressBar;
    private LinearLayout navHome, navTasks, navTimer, navAdd;
    private CountDownTimer countDownTimer;
    private boolean timerRunning = false;
    private long timeLeftInMillis = 25 * 60 * 1000;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private FirebaseManager firebaseManager;

    private androidx.cardview.widget.CardView menuIcon;
    private Dialog popupDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashbroad);
        firebaseManager = new FirebaseManager();
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        initViews();
        loadUserData();
        loadTodayTasks();
        loadTodayLessons();
        displayCurrentDate();
        setupClickListeners();
    }

    private void loadTodayLessons() {
        long startOfDay = getStartOfDay();
        long endOfDay = getEndOfDay();
        firebaseManager.queryUserDataByTimeRange("lessons","thoiGianBatDau", startOfDay, endOfDay, Lessons.class,
                new FirebaseManager.DatabaseCallback<List<Lessons>>() {
                    @Override
                    public void onSucces(List<Lessons> dataResult) {
                        Log.d("DashboardActivity", "Today Lessons: " + dataResult.size() + " lessons.");
                        displayLessons(dataResult);
                    }
                    @Override
                    public void onError(String error) {
                        Log.e("DashboardActivity", "Error loading today lessons: " + error);
                        Toast.makeText(DashboardActivity.this, "Lỗi tải bài học hôm nay: "
                                + error, Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void displayLessons(List<Lessons> lessons) {
        java.util.Collections.sort(lessons, new java.util.Comparator<Lessons>() {
            @Override
            public int compare(Lessons lesson1, Lessons lesson2) {
                return Long.compare(lesson1.getThoiGianBatDau(), lesson2.getThoiGianBatDau());
            }
        });
        hideAllLessonViews();
        int count = Math.min(lessons.size(), 3);
        for (int i = 0; i < count; i++) {
            Lessons lesson = lessons.get(i);
            switch (i) {
                case 0:
                    displayLesson(lesson, tvLessonTitle1, tvLessonTime1, tvLessonRoom1);
                    break;
                case 1:
                    displayLesson(lesson, tvLessonTitle2, tvLessonTime2, tvLessonRoom2);
                    break;
                case 2:
                    displayLesson(lesson, tvLessonTitle3, tvLessonTime3, tvLessonRoom3);
                    break;
            }
        }
    }

    private void hideAllLessonViews() {
        View parent1 = (View) tvLessonTitle1.getParent().getParent().getParent();
        View parent2 = (View) tvLessonTitle2.getParent().getParent().getParent();
        View parent3 = (View) tvLessonTitle3.getParent().getParent().getParent();

        parent1.setVisibility(View.GONE);
        parent2.setVisibility(View.GONE);
        parent3.setVisibility(View.GONE);
    }
    private void displayLesson(Lessons lesson, TextView titleView, TextView timeView, TextView roomView) {
        // Get parent to make visible
        View parent = (View) titleView.getParent().getParent().getParent();
        parent.setVisibility(View.VISIBLE);

        // Set title
        titleView.setText(lesson.getTieuDe());

        // Format and set time
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        String formattedTime = timeFormat.format(new Date(lesson.getThoiGianBatDau()));
        timeView.setText(formattedTime);

        // Set room
        roomView.setText("Phòng " + lesson.getViTriPhong());
    }

    private void loadTodayTasks() {
        firebaseManager.getUserData("tasks", Task.class,
                new FirebaseManager.DatabaseCallback<List<Task>>() {
                    @Override
                    public void onSucces(List<Task> dataResult) {
                        List<Task> todayTasks = filterTasksByDueDate(dataResult);
                        Log.d("DashboardActivity", "Today Tasks: " + todayTasks.size() + " tasks.");
                        displayTasks(todayTasks);
                        updateTaskProgressView(dataResult);
                    }
                    @Override
                    public void onError(String error) {
                        Log.e("DashboardActivity", "Error loading today tasks: " + error);
                        Toast.makeText(DashboardActivity.this, "Lỗi tải nhiệm vụ hôm nay: "
                                + error, Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private List<Task> filterTodayTasks(List<Task> dataResult) {
        List<Task> todayTasks = new ArrayList<>();
        long startOfDay = getStartOfDay();
        long endOfDay = getEndOfDay();
        for(Task task : dataResult) {
            if (task.getThoiGianTao() >= startOfDay && task.getThoiGianTao() <= endOfDay) {
                todayTasks.add(task);
            }
        }
        return todayTasks;
    }
    private List<Task> filterTasksByDueDate(List<Task> allTasks) {
        // Sort tasks by due date
        java.util.Collections.sort(allTasks, new java.util.Comparator<Task>() {
            @Override
            public int compare(Task task1, Task task2) {
                return Long.compare(task1.getThoiGianHoanThanh(), task2.getThoiGianHoanThanh());
            }
        });

        // Get tasks that are not completed and due soon
        List<Task> upcomingTasks = new ArrayList<>();
        for (Task task : allTasks) {
            if (!task.isDaHoanThanh()) {
                upcomingTasks.add(task);
            }

            // Only take the first 2 upcoming tasks
            if (upcomingTasks.size() >= 2) {
                break;
            }
        }

        return upcomingTasks;
    }

    private void displayTasks(List<Task> tasks) {
        // Hide task views by default
        hideAllTaskViews();

        // Display up to 2 tasks
        int count = Math.min(tasks.size(), 2);
        for (int i = 0; i < count; i++) {
            Task task = tasks.get(i);
            switch (i) {
                case 0:
                    displayTask(task, tvTaskTitle1, tvTaskDueDate1);
                    break;
                case 1:
                    displayTask(task, tvTaskTitle2, tvTaskDueDate2);
                    break;
            }
        }
    }

    private void hideAllTaskViews() {
        // Get parent views to hide the entire card if no tasks
        View parent1 = (View) tvTaskTitle1.getParent().getParent().getParent();
        View parent2 = (View) tvTaskTitle2.getParent().getParent().getParent();

        parent1.setVisibility(View.GONE);
        parent2.setVisibility(View.GONE);
    }

    private void displayTask(Task task, TextView titleView, TextView dueDateView) {
        // Get parent to make visible
        View parent = (View) titleView.getParent().getParent().getParent();
        parent.setVisibility(View.VISIBLE);

        // Set title
        titleView.setText(task.getTieuDe());

        // Format and set due date
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd", Locale.getDefault());
        String formattedDate = dateFormat.format(new Date(task.getThoiGianHoanThanh()));
        dueDateView.setText("Due: " + formattedDate);

        // Set color based on urgency
        long currentTime = System.currentTimeMillis();
        long oneDay = 24 * 60 * 60 * 1000;

        if (task.getThoiGianHoanThanh() - currentTime < oneDay) {
            dueDateView.setTextColor(getResources().getColor(R.color.accent_red));
        } else if (task.getThoiGianHoanThanh() - currentTime < 3 * oneDay) {
            dueDateView.setTextColor(getResources().getColor(R.color.accent_orange));
        } else {
            dueDateView.setTextColor(getResources().getColor(R.color.text_secondary));
        }
    }

    private void updateTaskProgressView(List<Task> allTasks) {
        int totalTasks = allTasks.size();
        int completedTasks = 0;

        for (Task task : allTasks) {
            if (task.isDaHoanThanh()) {
                completedTasks++;
            }
        }

        // Update progress text view
        tvTaskProgress.setText(completedTasks + "/" + totalTasks);

        // Update progress bar
        int progressPercentage = totalTasks > 0 ? (completedTasks * 100) / totalTasks : 0;
        progressBar.setProgress(progressPercentage);
    }

    private long getEndOfDay() {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 23);
        calendar.set(java.util.Calendar.MINUTE, 59);
        calendar.set(java.util.Calendar.SECOND, 59);
        calendar.set(java.util.Calendar.MILLISECOND, 999);
        return calendar.getTimeInMillis();
    }

    private long getStartOfDay() {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 0);
        calendar.set(java.util.Calendar.MINUTE, 0);
        calendar.set(java.util.Calendar.SECOND, 0);
        calendar.set(java.util.Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    private void initViews() {
        tvWelcome = findViewById(R.id.tvWelcome);
        tvDate = findViewById(R.id.tvDate);
        progressBar = findViewById(R.id.progressBar);
        tvTimerDisplay = findViewById(R.id.tvTimerDisplay);
        btnStartTimer = findViewById(R.id.btnStartTimer);
        navHome = findViewById(R.id.navHome);
        navTasks = findViewById(R.id.navTasks);
        navTimer = findViewById(R.id.navTimer);
        navAdd = findViewById(R.id.navAdd);
        tvTaskProgress = findViewById(R.id.tvTaskProgress);

        tvTaskTitle1 = findViewById(R.id.task_title_1);
        tvTaskDueDate1 = findViewById(R.id.task_due_date_1);
        tvTaskTitle2 = findViewById(R.id.task_title_2);
        tvTaskDueDate2 = findViewById(R.id.task_due_date_2);

        tvLessonTitle1 = findViewById(R.id.lesson_title_1);
        tvLessonTime1 = findViewById(R.id.lesson_time_1);
        tvLessonRoom1 = findViewById(R.id.lesson_room_1);

        tvLessonTitle2 = findViewById(R.id.lesson_title_2);
        tvLessonTime2 = findViewById(R.id.lesson_time_2);
        tvLessonRoom2 = findViewById(R.id.lesson_room_2);

        tvLessonTitle3 = findViewById(R.id.lesson_title_3);
        tvLessonTime3 = findViewById(R.id.lesson_time_3);
        tvLessonRoom3 = findViewById(R.id.lesson_room_3);

        menuIcon = findViewById(R.id.menuIcon);
    }
    private void displayCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd yyyy", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());
        tvDate.setText("Today's Date: " + currentDate);
    }

    private void setupClickListeners() {
        btnStartTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timerRunning) {
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        });

        navHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DashboardActivity.this, "Bạn đang ở trang chủ", Toast.LENGTH_SHORT).show();
            }
        });

        navTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DashboardActivity.this, "Chuyển đến quản lý nhiệm vụ", Toast.LENGTH_SHORT).show();

            }
        });
        navTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DashboardActivity.this, "Chuyển đến màn hình Pomodoro Timer", Toast.LENGTH_SHORT).show();
                // TODO: Implement timer screen navigation
            }
        });

        navAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent (DashboardActivity.this, AddTaskActivity.class);
                startActivity(intent);
                }
        });

        // Task checkbox clicks
        if (findViewById(R.id.checkboxTask1) != null) {
            findViewById(R.id.checkboxTask1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleTaskCompletion(v, "Submit Essay");
                }
            });
        }

        if (findViewById(R.id.checkboxTask2) != null) {
            findViewById(R.id.checkboxTask2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleTaskCompletion(v, "Group Project");
                }
            });
        }

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });
    }
    private void loadUserData(){
        if(!firebaseManager.isUserLoggedIn()){
            tvWelcome.setText("Chào mừng, người dùng!");
            return;
        }
        firebaseManager.getData("users", firebaseManager.getUserId(),User.class,
                new FirebaseManager.DatabaseCallback<User>() {
                    @Override
                    public void onSucces(User dataResult) {
                        if(dataResult != null && dataResult.getTenHienThi() != null){
                            tvWelcome.setText("Hi, " + dataResult.getTenHienThi() + "!");
                        } else {
                            tvWelcome.setText("Chào mừng, người dùng!");
                        }
                    }

                    @Override
                    public void onError(String error) {
                        tvWelcome.setText("Chào mừng, người dùng!");
                        Toast.makeText(DashboardActivity.this, "Lỗi tải dữ liệu người dùng: "
                                + error, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void toggleTaskCompletion(View checkboxView, String taskName) {
        TextView checkmark = checkboxView.findViewById(R.id.checkmarkText);
        if (checkmark != null) {
            boolean isCompleted = "✓".equals(checkmark.getText().toString());
            if (isCompleted) {
                checkmark.setText("");
                Toast.makeText(this, "Đánh dấu \"" + taskName + "\" chưa hoàn thành", Toast.LENGTH_SHORT).show();
            } else {
                checkmark.setText("✓");
                Toast.makeText(this, "Đánh dấu \"" + taskName + "\" đã hoàn thành", Toast.LENGTH_SHORT).show();
            }
            updateTaskProgress();
        }
    }
    private void updateTaskProgress() {
        int totalTasks = 5;
        int completedTasks = 0;

        TextView checkmark1 = findViewById(R.id.checkboxTask1).findViewById(R.id.checkmarkText);
        if (checkmark1 != null && "✓".equals(checkmark1.getText().toString())) {
            completedTasks++;
        }

        TextView checkmark2 = findViewById(R.id.checkboxTask2).findViewById(R.id.checkmarkText);
        if (checkmark2 != null && "✓".equals(checkmark2.getText().toString())) {
            completedTasks++;
        }

        TextView tvTaskProgress = findViewById(R.id.tvTaskProgress);
        if (tvTaskProgress != null) {
            tvTaskProgress.setText(completedTasks + "/" + totalTasks);
        }
        int progressPercentage = (completedTasks * 100) / totalTasks;
        progressBar.setProgress(progressPercentage);
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerUI();
            }

            @Override
            public void onFinish() {
                timerRunning = false;
                btnStartTimer.setText("Start");
                Toast.makeText(DashboardActivity.this, "Thời gian tập trung đã hoàn thành!", Toast.LENGTH_SHORT).show();

                // Reset timer
                timeLeftInMillis = 25 * 60 * 1000;
                updateTimerUI();
            }
        }.start();

        timerRunning = true;
        btnStartTimer.setText("Pause");
    }

    private void pauseTimer() {
        countDownTimer.cancel();
        timerRunning = false;
        btnStartTimer.setText("Resume");
    }

    private void updateTimerUI() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        tvTimerDisplay.setText(timeLeftFormatted);
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    private void showPopupMenu(View anchorView) {
        // Tạo dialog cho popup menu
        popupDialog = new Dialog(this);
        popupDialog.setContentView(R.layout.popup_menu);
        popupDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        // Tìm và thiết lập sự kiện cho các mục menu
        LinearLayout btnBackupRestore = popupDialog.findViewById(R.id.btnBackupRestore);
        LinearLayout btnProfile = popupDialog.findViewById(R.id.btnProfile);
        LinearLayout btnNotifications = popupDialog.findViewById(R.id.btnNotifications);
        LinearLayout btnSettings = popupDialog.findViewById(R.id.btnSettings);

        btnBackupRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DashboardActivity.this, "Đang phát triển tính năng Backup & Restore", Toast.LENGTH_SHORT).show();
                popupDialog.dismiss();
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, ProfileActivity.class);
                startActivity(intent);
                popupDialog.dismiss();
            }
        });

        btnNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DashboardActivity.this, "Đang phát triển tính năng Thông báo", Toast.LENGTH_SHORT).show();
                popupDialog.dismiss();
            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DashboardActivity.this, "Đang phát triển tính năng Cài đặt", Toast.LENGTH_SHORT).show();
                popupDialog.dismiss();
            }
        });

        // Hiển thị dialog
        popupDialog.show();
    }
}
