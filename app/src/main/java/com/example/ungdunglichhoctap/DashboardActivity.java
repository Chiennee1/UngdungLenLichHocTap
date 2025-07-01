package com.example.ungdunglichhoctap;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import DoiTuong.Lesson;
import DoiTuong.Task;
import DoiTuong.User;
import DoiTuong.PomodoroSession;
import DoiTuong.Subject;

public class DashboardActivity extends AppCompatActivity {

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
        firebaseManager.queryUserDataByTimeRange("lessons","thoiGianBatDau", startOfDay, endOfDay,Lesson.class,
                new FirebaseManager.DatabaseCallback<List<Lesson>>() {
                    @Override
                    public void onSucces(List<Lesson> dataResult) {
                        Log.d("DashboardActivity", "Today Lessons: " + dataResult.size() + " lessons.");
                    }
                    @Override
                    public void onError(String error) {
                        Log.e("DashboardActivity", "Error loading today lessons: " + error);
                        Toast.makeText(DashboardActivity.this, "Lỗi tải bài học hôm nay: "
                                + error, Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void loadTodayTasks() {
        firebaseManager.getUserData("tasks", Task.class,
                new FirebaseManager.DatabaseCallback<List<Task>>() {
                    @Override
                    public void onSucces(List<Task> dataResult) {
                        List<Task> todayTasks = filterTodayTasks(dataResult);
                        Log.d("DashboardActivity", "Today Tasks: " + todayTasks.size() + " tasks.");
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
        CardView checkboxTask1 = findViewById(R.id.checkboxTask1);
        CardView checkboxTask2 = findViewById(R.id.checkboxTask2);
    }

    private void displayCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd yyyy", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());
        tvDate.setText("Today's Date: " + currentDate);
    }

    private void setupClickListeners() {
        // Pomodoro timer button
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
                Toast.makeText(DashboardActivity.this, "Thêm hoạt động mới", Toast.LENGTH_SHORT).show();
                // TODO: Implement add activity dialog or screen
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
}
