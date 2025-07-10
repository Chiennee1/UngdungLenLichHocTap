package com.example.ungdunglichhoctap;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import DoiTuong.Lessons;
import XulyObject.FirebaseManager;

public class AddLessonActivity extends AppCompatActivity {

    private TextView btnBack, tvTitle;
    private EditText etLessonTitle, etRoom, etDescription;
    private TextView tvStartDate, tvStartTime, tvEndTime;
    private Spinner spinnerLessonType;
    private Button btnSaveLesson;

    private FirebaseManager firebaseManager;
    private String lessonId;
    private Lessons currentLesson;

    private Calendar startCalendar = Calendar.getInstance();
    private Calendar endCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lesson);

        firebaseManager = new FirebaseManager();
        initViews();
        setupClickListeners();

        // Check if we're editing an existing lesson
        lessonId = getIntent().getStringExtra("LESSON_ID");
        if (lessonId != null) {
            tvTitle.setText("Chỉnh Sửa Lớp Học");
            loadLessonData();
        } else {
            tvTitle.setText("Thêm Lớp Học Mới");
            // Set default end time to 1.5 hours after start time
            endCalendar.add(Calendar.MINUTE, 90);
            updateDateTimeViews();
        }
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        tvTitle = findViewById(R.id.tvTitle);
        etLessonTitle = findViewById(R.id.etLessonTitle);
        etRoom = findViewById(R.id.etRoom);
        etDescription = findViewById(R.id.etDescription);
        tvStartDate = findViewById(R.id.tvStartDate);
        tvStartTime = findViewById(R.id.tvStartTime);
        tvEndTime = findViewById(R.id.tvEndTime);
        spinnerLessonType = findViewById(R.id.spinnerLessonType);
        btnSaveLesson = findViewById(R.id.btnSaveLesson);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());

        tvStartDate.setOnClickListener(v -> showDatePicker());
        tvStartTime.setOnClickListener(v -> showStartTimePicker());
        tvEndTime.setOnClickListener(v -> showEndTimePicker());

        btnSaveLesson.setOnClickListener(v -> saveLesson());
    }

    private void loadLessonData() {
        firebaseManager.getData("users/" + firebaseManager.getUserId() + "/lessons", lessonId, Lessons.class,
                new FirebaseManager.DatabaseCallback<Lessons>() {
                    @Override
                    public void onSucces(Lessons dataResult) {
                        currentLesson = dataResult;
                        populateFields(dataResult);
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(AddLessonActivity.this, "Lỗi: " + error, Toast.LENGTH_SHORT).show();
                        Log.e("AddLessonActivity", "Error loading lesson: " + error);
                    }
                });
    }

    private void populateFields(Lessons lesson) {
        etLessonTitle.setText(lesson.getTieuDe());
        etRoom.setText(lesson.getViTriPhong());
        etDescription.setText(lesson.getMoTa());

        // Set the spinners based on lesson type
        // You need to find the position in the adapter
        String lessonType = lesson.getLoaiBuoiHoc();
        for (int i = 0; i < spinnerLessonType.getAdapter().getCount(); i++) {
            if (spinnerLessonType.getAdapter().getItem(i).toString().equals(lessonType)) {
                spinnerLessonType.setSelection(i);
                break;
            }
        }

        // Set the calendars
        startCalendar.setTimeInMillis(lesson.getThoiGianBatDau());
        endCalendar.setTimeInMillis(lesson.getThoiGianKetThuc());

        updateDateTimeViews();
    }

    private void updateDateTimeViews() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        tvStartDate.setText(dateFormat.format(startCalendar.getTime()));
        tvStartTime.setText(timeFormat.format(startCalendar.getTime()));
        tvEndTime.setText(timeFormat.format(endCalendar.getTime()));
    }

    private void showDatePicker() {
        new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    startCalendar.set(Calendar.YEAR, year);
                    startCalendar.set(Calendar.MONTH, month);
                    startCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    // Also update end calendar to the same date
                    endCalendar.set(Calendar.YEAR, year);
                    endCalendar.set(Calendar.MONTH, month);
                    endCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    updateDateTimeViews();
                },
                startCalendar.get(Calendar.YEAR),
                startCalendar.get(Calendar.MONTH),
                startCalendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private void showStartTimePicker() {
        new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {
                    startCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    startCalendar.set(Calendar.MINUTE, minute);

                    // If end time is before start time, adjust it
                    if (endCalendar.getTimeInMillis() <= startCalendar.getTimeInMillis()) {
                        endCalendar.setTimeInMillis(startCalendar.getTimeInMillis());
                        endCalendar.add(Calendar.MINUTE, 90); // Default duration 1.5 hours
                    }

                    updateDateTimeViews();
                },
                startCalendar.get(Calendar.HOUR_OF_DAY),
                startCalendar.get(Calendar.MINUTE),
                true
        ).show();
    }

    private void showEndTimePicker() {
        new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {
                    endCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    endCalendar.set(Calendar.MINUTE, minute);

                    // If end time is before start time, show warning
                    if (endCalendar.getTimeInMillis() <= startCalendar.getTimeInMillis()) {
                        Toast.makeText(AddLessonActivity.this,
                                "Thời gian kết thúc phải sau thời gian bắt đầu",
                                Toast.LENGTH_SHORT).show();
                        endCalendar.setTimeInMillis(startCalendar.getTimeInMillis());
                        endCalendar.add(Calendar.MINUTE, 90);
                    }

                    updateDateTimeViews();
                },
                endCalendar.get(Calendar.HOUR_OF_DAY),
                endCalendar.get(Calendar.MINUTE),
                true
        ).show();
    }

    private void saveLesson() {
        String title = etLessonTitle.getText().toString().trim();
        String room = etRoom.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String lessonType = spinnerLessonType.getSelectedItem().toString();

        // Validate fields
        if (TextUtils.isEmpty(title)) {
            etLessonTitle.setError("Vui lòng nhập tên lớp học");
            return;
        }

        if (TextUtils.isEmpty(room)) {
            etRoom.setError("Vui lòng nhập phòng học");
            return;
        }

        // Create or update lesson object
        Lessons lesson;
        if (currentLesson != null) {
            lesson = currentLesson;
        } else {
            lesson = new Lessons();
        }

        lesson.setTieuDe(title);
        lesson.setViTriPhong(room);
        lesson.setMoTa(description);
        lesson.setLoaiBuoiHoc(lessonType);
        lesson.setThoiGianBatDau(startCalendar.getTimeInMillis());
        lesson.setThoiGianKetThuc(endCalendar.getTimeInMillis());

        if (lessonId != null) {
            // Update existing lesson
            lesson.setMaBuoiHoc(lessonId);
            firebaseManager.updateLesson(lessonId, lesson, new FirebaseManager.DatabaseCallback<Lessons>() {
                @Override
                public void onSucces(Lessons dataResult) {
                    Toast.makeText(AddLessonActivity.this, "Đã cập nhật lớp học", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(AddLessonActivity.this, "Lỗi: " + error, Toast.LENGTH_SHORT).show();
                    Log.e("AddLessonActivity", "Error updating lesson: " + error);
                }
            });
        } else {
            // Create new lesson
            firebaseManager.createLesson(lesson, new FirebaseManager.DatabaseCallback<String>() {
                @Override
                public void onSucces(String dataResult) {
                    Toast.makeText(AddLessonActivity.this, "Đã thêm lớp học mới", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(AddLessonActivity.this, "Lỗi: " + error, Toast.LENGTH_SHORT).show();
                    Log.e("AddLessonActivity", "Error creating lesson: " + error);
                }
            });
        }
    }
}