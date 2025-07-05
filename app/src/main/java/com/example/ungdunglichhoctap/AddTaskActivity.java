package com.example.ungdunglichhoctap;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

import DoiTuong.Task;

public class AddTaskActivity extends AppCompatActivity {
    private FirebaseManager firebaseManager;
    private TextInputEditText editTaskTitle, edtTaskDescriptiokn;
    private TextView tvSelectTime, tvSelectDate;
    private CardView btnDatePicker, btnTimePicker,
            chipHigh, chipMedium, chipLow;
    private Button btnAddTask, btnCancelTask;
    private Calendar calendar;
    private String selectPriority = "Medium";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        firebaseManager = new FirebaseManager();
        calendar = Calendar.getInstance();
        findViews();
        setupListeners();

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
        btnAddTask.setOnClickListener(v -> saveTask());
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
    if(TextUtils.isEmpty(title)){
        editTaskTitle.setError("Vui lòng nhập tiêu đề công việc");
        return;
    }
    if(tvSelectDate.getText().toString().equals("Chọn ngày")
    || tvSelectTime.getText().toString().equals("Chọn giờ")) {
        Toast.makeText(this, "Vui lòng chọn ngày và giờ", Toast.LENGTH_SHORT).show();
        return;
    }
        Task task = new Task();
        task.setTieuDe(title);
        task.setMoTa(description);
        task.setHanChot(calendar.getTimeInMillis());
        task.setMucDoUuTien(selectPriority);
        task.setDaHoanThanh(false);
        task.setThoiGianTao(System.currentTimeMillis());
        firebaseManager.createUserData("tasks", task, new FirebaseManager.DatabaseCallback<String>() {
            @Override
            public void onSucces(String dataResult) {
                Toast.makeText(AddTaskActivity.this, "Thêm nhiệm vụ thành công", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(AddTaskActivity.this,"Thêm nhiệm vụ thất bại: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void findViews() {
        editTaskTitle = findViewById(R.id.etTaskTitle);
        edtTaskDescriptiokn = findViewById(R.id.etTaskDescription);
        tvSelectTime = findViewById(R.id.tvSelectedTime);
        tvSelectDate = findViewById(R.id.tvSelectedDate);
        btnDatePicker = findViewById(R.id.btnDatePicker);
        btnTimePicker = findViewById(R.id.btnTimePicker);
        chipHigh = findViewById(R.id.chipHigh);
        chipMedium = findViewById(R.id.chipMedium);
        chipLow = findViewById(R.id.chipLow);
        btnAddTask = findViewById(R.id.btnAddTask);
        btnCancelTask = findViewById(R.id.btnCancel);

    }
}
