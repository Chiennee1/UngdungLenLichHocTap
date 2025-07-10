package com.example.ungdunglichhoctap;

import static android.text.format.DateUtils.getRelativeTimeSpanString;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Date;

import XulyObject.FirebaseManager;

public class BackupRestoreActivity extends AppCompatActivity {
    private TextView btnBack;
    private TextView btnHelp;
    private Button btnBackupNow;
    private Button btnRestoreData;
    private TextView tvLastBackupDate;
    private Switch switchAutoBackup;
    private LinearLayout layoutBackupFrequency;
    private TextView tvBackupFrequency;
    private FirebaseManager firebaseManager;
    private FirebaseAuth mAuth;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup_restore);
        mAuth = FirebaseAuth.getInstance();
        firebaseManager = new FirebaseManager();
        sharedPreferences = getSharedPreferences("Backup_Setting", MODE_PRIVATE);
        findViews();
        setUpIntinalData();
        setUpClickListeners();
    }

    private void setUpClickListeners() {
        btnBack.setOnClickListener(v -> finish());
        btnHelp.setOnClickListener(v -> showHelpDialog());
        btnBackupNow.setOnClickListener(v -> performBackup());
        btnRestoreData.setOnClickListener(v -> confirmRestore());
        switchAutoBackup.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("auto_backup", isChecked);
            editor.apply();
            Log.d("BackupRestoreActivity", "Auto backup set to: " + isChecked);
            Toast.makeText(BackupRestoreActivity.this, isChecked ? "Auto backup enabled" : "Auto backup disabled",
                    Toast.LENGTH_SHORT).show();
        });
        layoutBackupFrequency.setOnClickListener(v -> showBackupFrequencyDialog());
    }

    private void showBackupFrequencyDialog() {
        String options[] = {"Daily", "Weekly", "Monthly"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chon tan suat sao luu")
                .setItems(options, (dialog, which) -> {
                    String selectedFrequency = options[which];
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("backup_frequency", selectedFrequency);
                    editor.apply();
                    tvBackupFrequency.setText(selectedFrequency);
                    Log.d("BackupRestoreActivity", "Backup frequency set to: " + selectedFrequency);
                    Toast.makeText(BackupRestoreActivity.this, "Tần suất sao lưu đã được cập nhật: " + selectedFrequency,
                            Toast.LENGTH_SHORT).show();
                })
                .show();

    }


    private void confirmRestore() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xac nhan phuc hoi du lieu")
                .setMessage("Ban co chac chan muon phuc hoi du lieu? Tat ca du lieu hien tai se bi ghi de.")
                .setPositiveButton("Xac nhan", (dialog, which) -> restoreData())
                .setNegativeButton("Huy", null)
                .show();
    }

    private void restoreData() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Dang phuc hoi du lieu")
                .setMessage("Dang tai ve du lieu cua ban tu Google Drive. Vui long cho toi mot chut...")
                .setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null) {
            dialog.dismiss();
            Toast.makeText(this, "Ban chua dang nhap", Toast.LENGTH_SHORT).show();
            return;
        }
        firebaseManager.restoreData(currentUser.getUid(), new FirebaseManager.DatabaseCallback<Boolean>() {
            @Override
            public void onSucces(Boolean dataResult) {
                builder.create().dismiss();
                Toast.makeText(BackupRestoreActivity.this, "Phuc hoi thanh cong", Toast.LENGTH_SHORT).show();
                Log.d("BackupRestoreActivity", "Restore successful");
            }

            @Override
            public void onError(String error) {
                dialog.dismiss();
                Log.e("BackupRestoreActivity", "Restore failed: " + error);
                Toast.makeText(BackupRestoreActivity.this, "Phuc hoi that bai: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void performBackup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Dang sao luu du lieu")
                .setMessage("Dang tai ve du lieu cua ban len Google Drive. Vui long cho toi mot chut...")
                .setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null) {
            dialog.dismiss();
            Toast.makeText(this, "Ban chua dang nhap", Toast.LENGTH_SHORT).show();
            return;
        }
        firebaseManager.backupData(currentUser.getUid(), new FirebaseManager.DatabaseCallback<Boolean>() {
            @Override
            public void onSucces(Boolean dataResult) {
                dialog.dismiss();
                long lastBackupTime = System.currentTimeMillis();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putLong("last_backup_time", lastBackupTime);
                editor.apply();
                Date date = new Date(lastBackupTime);
                String fomattedDate = android.text.format.DateFormat.format(
                        "dd/MM/yyyy HH:mm:ss", date).toString();
                tvLastBackupDate.setText(fomattedDate);
                Toast.makeText(BackupRestoreActivity.this,
                        "Sao luu thanh cong", Toast.LENGTH_SHORT).show();
                Log.d("BackupRestoreActivity", "Backup successful");

            }
            @Override
            public void onError(String error) {
            dialog.dismiss();
                Log.e("BackupRestoreActivity", "Backup failed: " + error);
                Toast.makeText(BackupRestoreActivity.this,
                        "Sao luu that bai: " + error, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void showHelpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tro giup Sao Luu và Phuc Hoi")
                .setMessage("• Sao lưu: Lưu trữ tất cả dữ liệu học tập của bạn lên Google Drive.\n\n" +
                "• Khôi phục: Tải lại dữ liệu đã sao lưu trước đó. Lưu ý rằng dữ liệu hiện tại sẽ bị ghi đè.\n\n" +
                "• Tự động sao lưu: Khi bật, ứng dụng sẽ tự động sao lưu dữ liệu theo tần suất đã chọn.\n\n" +
                "• Tần suất: Bạn có thể chọn sao lưu hàng ngày, hàng tuần hoặc hàng tháng.")
                .setPositiveButton("OK", null)
                .show();
    }

    private void setUpIntinalData() {
        boolean autoBackup = sharedPreferences.getBoolean("auto_backup", true);
        switchAutoBackup.setChecked(autoBackup);
        String backupFrequency = sharedPreferences.getString("backup_frequency", "Daily");
        tvBackupFrequency.setText(backupFrequency);
        long lastBackupTime = sharedPreferences.getLong("last_backup_time", 0);
        if(lastBackupTime > 0){
            Date lastBackupDate = new Date(lastBackupTime);
            String formattedDate = android.text.format.DateFormat.format("dd/MM/yyyy HH:mm:ss", lastBackupDate).toString();
            tvLastBackupDate.setText("Last Backup: " + formattedDate);
            String relativeTime = getRelativeTimeSpanString(lastBackupTime).toString();
            ((TextView)findViewById(R.id.tvLastBackupDate)).setText("Last Backup: " + relativeTime);

        }
    }
    private void findViews() {
        btnBack = findViewById(R.id.btnBack);
        btnHelp = findViewById(R.id.btnHelp);
        btnBackupNow = findViewById(R.id.btnBackupNow);
        btnRestoreData = findViewById(R.id.btnRestoreData);
        tvLastBackupDate = findViewById(R.id.tvLastBackupDate);
        switchAutoBackup = findViewById(R.id.switchAutoBackup);
        layoutBackupFrequency = findViewById(R.id.layoutBackupFrequency);
        tvBackupFrequency = findViewById(R.id.tvBackupFrequency);
    }
}
