package com.example.ungdunglichhoctap;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import DoiTuong.Subject;
import DoiTuong.Task;
import XulyObject.FirebaseManager;

public class AddTaskActivity extends AppCompatActivity {
    private FirebaseManager firebaseManager;
    private TextInputEditText editTaskTitle, edtTaskDescriptiokn;
    private TextInputLayout tilSubjectName;
    private AutoCompleteTextView actvSubjectName;
    private TextView tvSelectTime, tvSelectDate;
    private TextView btnBack;
    private CardView btnDatePicker, btnTimePicker,
            chipHigh, chipMedium, chipLow;
    private LinearLayout navHome, navCalendar, navPomodoro, navProfile;
    private Button btnSaveTask, btnCancelTask;
    private Calendar calendar = Calendar.getInstance();
    private String selectPriority = "Medium";
    private List<Subject> subjectList = new ArrayList<>();
    private ArrayAdapter<String> subjectAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        firebaseManager = new FirebaseManager();
        calendar = Calendar.getInstance();
        findViews();
        setupListeners();
        loadSubjects();
        setupSubjectInput();
    }

    private void setupSubjectInput() {
        subjectAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line,
                new ArrayList<>());
        actvSubjectName.setAdapter(subjectAdapter);
        // Thiết lập bộ lọc để chỉ hiển thị khi người dùng gõ ít nhất 1 ký tự
       actvSubjectName.setThreshold(1);

    }

    private void loadSubjects() {
        firebaseManager.getSubjects(new FirebaseManager.DatabaseCallback<List<Subject>>() {
            @Override
            public void onSucces(List<Subject> dataResult) {
                subjectList.clear();
                subjectList.addAll(dataResult);
                List<String> subjectNames = new ArrayList<>();
                for (Subject subject : subjectList) {
                    if (!TextUtils.isEmpty(subject.getTenMonHoc()))
                        subjectNames.add(subject.getTenMonHoc());
                }
                subjectAdapter.clear();
                subjectAdapter.addAll(subjectNames);
                subjectAdapter.notifyDataSetChanged();
                Log.d("AddTaskActivity", "Loaded subjects: " + subjectNames.size());
            }

            @Override
            public void onError(String error) {
                Log.d("AddTaskActivity", "Error loading subjects: " + error);
                Toast.makeText(AddTaskActivity.this, "Lỗi khi tải danh sách môn học: "
                        + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupListeners() {
        btnDatePicker.setOnClickListener(v -> showDatePicker());
        btnTimePicker.setOnClickListener(v -> showTimePicker());
        chipHigh.setOnClickListener(v -> {
            selectPriority = ("High");
            updateChipColors();
        });
        chipMedium.setOnClickListener(v -> {
            selectPriority = ("Medium");
            updateChipColors();
        });
        chipLow.setOnClickListener(v -> {
            selectPriority = ("Low");
            updateChipColors();
        });
        btnSaveTask.setOnClickListener(v -> saveTask());

        btnCancelTask.setOnClickListener(v -> finish());

        navHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddTaskActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        navCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Calendar
                Intent intent = new Intent(AddTaskActivity.this, LichHocActivity.class);
                startActivity(intent);
                finish();
            }
        });

        navProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Profile
                Intent intent = new Intent(AddTaskActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });
        navPomodoro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Pomodoro
                Intent intent = new Intent(AddTaskActivity.this, PromodoroActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void updateChipColors() {
        chipHigh.setCardBackgroundColor(selectPriority.equals("High") ? getResources()
                .getColor(R.color.accent_red) : getResources()
                .getColor(R.color.accent_red_light));
        chipMedium.setCardBackgroundColor(selectPriority.equals("Medium") ? getResources()
                .getColor(R.color.accent_orange) : getResources()
                .getColor(R.color.accent_orange_light));
        chipLow.setCardBackgroundColor(selectPriority.equals("Low") ? getResources()
                .getColor(R.color.accent_green) : getResources()
                .getColor(R.color.accent_green_light));
    }

    private void showTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minute) -> {
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);
                    tvSelectTime.setText(String.format("%02d:%02d", hourOfDay, minute));
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
        );
        timePickerDialog.show();
}
    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    tvSelectDate.setText(String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void saveTask(){
    String title = editTaskTitle.getText().toString();
    String description = edtTaskDescriptiokn.getText().toString();
    String subjectName = actvSubjectName.getText().toString();
    if(TextUtils.isEmpty(title)){
        editTaskTitle.setError("Vui lòng nhập tiêu đề công việc");
        return;
    }
    if(tvSelectDate.getText().toString().equals("Chọn ngày")
    || tvSelectTime.getText().toString().equals("Chọn giờ")) {
        Toast.makeText(this, "Vui lòng chọn ngày và giờ", Toast.LENGTH_SHORT).show();
        return;
    }
    if(TextUtils.isEmpty(subjectName)) {
        tilSubjectName.setError("Vui lòng chọn môn học");
        return;}

        Task task = new Task();
        task.setTieuDe(title);
        task.setTenMonHoc(subjectName);
        task.setMoTa(description);
        task.setHanChot(calendar.getTimeInMillis());
        task.setMucDoUuTien(selectPriority);
        task.setDaHoanThanh(false);
        task.setThoiGianTao(System.currentTimeMillis());

        btnSaveTask.setEnabled(false);
        btnSaveTask.setText("Đang lưu...");
        Log.d("AddTaskActivity", "Đang lưu nhiệm vụ vào Firebase");
        firebaseManager.createUserData("tasks", task, new FirebaseManager.DatabaseCallback<String>() {
            @Override
            public void onSucces(String dataResult) {
                Log.d("AddTaskActivity", "Lưu nhiệm vụ thành công");
                Toast.makeText(AddTaskActivity.this, "Thêm nhiệm vụ thành công", Toast.LENGTH_SHORT).show();
                task.setMaNhiemVu(dataResult);
                updateTaskwithID(task, dataResult);
                finish();
            }
            @Override
            public void onError(String error) {
                Log.e("AddTaskActivity", "Lỗi khi lưu nhiệm vụ: " + error);
                Toast.makeText(AddTaskActivity.this,"Thêm nhiệm vụ thất bại: " + error, Toast.LENGTH_SHORT).show();
                btnSaveTask.setEnabled(true);
                btnSaveTask.setText("Lưu Công Việc");
            }
        });

}
    private void updateTaskwithID(Task task, String dataResult) {
       firebaseManager.updateUserData("tasks",dataResult, task, new FirebaseManager.DatabaseCallback<Task>(){
           @Override
           public void onSucces(Task dataResult) {
               Toast.makeText(AddTaskActivity.this, "Cập nhật nhiệm vụ thành công", Toast.LENGTH_SHORT).show();
               finish();
           }

           @Override
           public void onError(String error) {
               Log.e("AddTaskActivity", "Lỗi khi cập nhật ID task: " + error);
                Toast.makeText(AddTaskActivity.this, "Cập nhật ID nhiệm vụ thất bại: " + error, Toast.LENGTH_SHORT).show();
                finish();
           }
       });
    }

    public void findViews() {
        editTaskTitle = findViewById(R.id.etTaskTitle);
        edtTaskDescriptiokn = findViewById(R.id.etTaskDescription);
        btnBack = findViewById(R.id.btnBack);
        tilSubjectName = findViewById(R.id.tilSubjectName);
        actvSubjectName = findViewById(R.id.etSubjectName);
        tvSelectTime = findViewById(R.id.tvSelectedTime);
        tvSelectDate = findViewById(R.id.tvSelectedDate);
        btnDatePicker = findViewById(R.id.btnDatePicker);
        btnTimePicker = findViewById(R.id.btnTimePicker);
        chipHigh = findViewById(R.id.chipHigh);
        chipMedium = findViewById(R.id.chipMedium);
        chipLow = findViewById(R.id.chipLow);
        btnSaveTask = findViewById(R.id.btnSaveTask);
        btnCancelTask = findViewById(R.id.btnCancel);
        navHome = findViewById(R.id.navHome);
        navCalendar = findViewById(R.id.navCalendar);
        navPomodoro = findViewById(R.id.navPomodoro);
        navProfile = findViewById(R.id.navProfile);

        updateDateDisplay();
    }
    private void updateDateDisplay() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        tvSelectDate.setText(sdf.format(calendar.getTime()));
    }
}
