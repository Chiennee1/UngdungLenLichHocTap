package com.example.ungdunglichhoctap;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import DoiTuong.Task;

public class ManagerTaskActivity extends AppCompatActivity {
    private static final String TAG = "ManagerTaskActivity";

    // UI elements
    private Button btnAllTasks, btnToday, btnCompleted, btnAddTask;
    private TextView btnBack, btnMenu;
    private LinearLayout navHome, navCalendar, navHoSo;

    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference tasksRef;

    // Task data
    private List<Task> taskList;
    private String currentFilter = "all";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taskmanager);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            // Nếu người dùng chưa đăng nhập, chuyển hướng đến màn hình đăng nhập
            Intent intent = new Intent(this, SiginActivity.class); // Thay DashboardActivity bằng màn hình đăng nhập
            startActivity(intent);
            finish();
            return;
        }

        // Nếu người dùng đã đăng nhập, tiếp tục khởi tạo
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        tasksRef = database.getReference("users").child(currentUser.getUid()).child("tasks");

        initializeViews();
        setupClickListeners();
        loadTasksFromFirebase();
    }

    private void initializeViews() {
        // Buttons
        btnAllTasks = findViewById(R.id.btnAllTasks);
        btnToday = findViewById(R.id.btnToday);
        btnCompleted = findViewById(R.id.btnCompleted);
        btnAddTask = findViewById(R.id.btnAddTask);
        btnBack = findViewById(R.id.btnBack);
        btnMenu = findViewById(R.id.btnMenu);

        navHome = findViewById(R.id.navHome);
        navCalendar = findViewById(R.id.navCalendar);
        navHoSo = findViewById(R.id.navHoSo);

        taskList = new ArrayList<>();
    }

    private void setupClickListeners() {
        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Nhấn nút thêm công việc");
                try {
                    Intent intent = new Intent(ManagerTaskActivity.this, AddTaskActivity.class);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        Log.d(TAG, "Bắt đầu chuyển sang AddTaskActivity");
                        startActivity(intent);
                    } else {
                        Log.e(TAG, "Không thể resolve AddTaskActivity");
                        Toast.makeText(ManagerTaskActivity.this,
                                "Không thể mở màn hình thêm công việc",
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Lỗi khi mở AddTaskActivity: " + e.getMessage(), e);
                    Toast.makeText(ManagerTaskActivity.this,
                            "Lỗi: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnAllTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentFilter = "all";
                filterTasks();
                updateFilterButtonsUI(btnAllTasks);
            }
        });

        btnToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Apply "Today" filter
                currentFilter = "today";
                filterTasks();
                updateFilterButtonsUI(btnToday);
            }
        });

        btnCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentFilter = "completed";
                filterTasks();
                updateFilterButtonsUI(btnCompleted);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        navHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Home
                Intent intent = new Intent(ManagerTaskActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        navCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Calendar
                Intent intent = new Intent(ManagerTaskActivity.this, LichHocActivity.class);
                startActivity(intent);
                finish();
            }
        });

        navHoSo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Profile
                Intent intent = new Intent(ManagerTaskActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void updateFilterButtonsUI(Button activeButton) {
        btnAllTasks.setBackgroundTintList(getColorStateList(android.R.color.transparent));
        btnAllTasks.setTextColor(getResources().getColor(R.color.text_secondary));

        btnToday.setBackgroundTintList(getColorStateList(android.R.color.transparent));
        btnToday.setTextColor(getResources().getColor(R.color.text_secondary));

        btnCompleted.setBackgroundTintList(getColorStateList(android.R.color.transparent));
        btnCompleted.setTextColor(getResources().getColor(R.color.text_secondary));

        // Highlight the active button
        activeButton.setBackgroundTintList(getColorStateList(R.color.primary_blue));
        activeButton.setTextColor(getResources().getColor(R.color.text_white));
    }

    private void loadTasksFromFirebase() {
        tasksRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                taskList.clear();

                for (DataSnapshot taskSnapshot : dataSnapshot.getChildren()) {
                    try {
                        Task task = taskSnapshot.getValue(Task.class);
                        if (task != null) {
                            task.setMaNhiemVu(taskSnapshot.getKey());
                            taskList.add(task);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing task: " + e.getMessage());
                    }
                }

                // Filter tasks based on current filter
                filterTasks();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Database error: " + databaseError.getMessage());
                Toast.makeText(ManagerTaskActivity.this, "Lỗi khi tải danh sách công việc", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterTasks() {
        List<Task> filteredList = new ArrayList<>();

        switch (currentFilter) {
            case "all":
                // Show all tasks
                filteredList.addAll(taskList);
                break;

            case "today":
                // Show upcoming/today tasks
                Calendar today = Calendar.getInstance();
                today.set(Calendar.HOUR_OF_DAY, 0);
                today.set(Calendar.MINUTE, 0);
                today.set(Calendar.SECOND, 0);
                today.set(Calendar.MILLISECOND, 0);

                long todayMillis = today.getTimeInMillis();

                for (Task task : taskList) {
                    if (!task.isDaHoanThanh() && task.getHanChot() >= todayMillis) {
                        filteredList.add(task);
                    }
                }
                break;

            case "completed":
                // Show completed tasks
                for (Task task : taskList) {
                    if (task.isDaHoanThanh()) {
                        filteredList.add(task);
                    }
                }
                break;
        }

        // Update UI with filtered tasks
        updateTaskUI(filteredList);
    }

    private void updateTaskUI(List<Task> filteredTasks) {

        Log.d(TAG, "Displaying " + filteredTasks.size() + " tasks with filter: " + currentFilter);

        for (Task task : filteredTasks) {
            Log.d(TAG, task.toString());
        }

        // TODO: Implement RecyclerView adapter for task display
    }

}
