package com.example.ungdunglichhoctap;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AboutAppActivity extends AppCompatActivity {
    private Button btnReturn;
    private TextView btnBack;
    private TextView tvContactEmail;
    private LinearLayout privacyPolicySection;
    private LinearLayout termsOfServiceSection;
    private LinearLayout licensesSection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thongtinapp);
        findViews();
        setUpListeners();
    }

    private void setUpListeners() {
        btnBack.setOnClickListener(v -> finish());
        btnReturn.setOnClickListener(v -> finish());
        tvContactEmail.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:studymate@email.com"));
            intent.putExtra(Intent.EXTRA_SUBJECT, "Phản hồi về ứng dụng StudyMate");
        if( intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
            Toast.makeText(AboutAppActivity.this, "Không có ứng dụng email nào được cài đặt",
                    Toast.LENGTH_SHORT).show();
            }
        });
        privacyPolicySection.setOnClickListener(v -> {
            Toast.makeText(AboutAppActivity.this, "Chính sách bảo mật sẽ được mở trong trình duyệt",
                    Toast.LENGTH_SHORT).show();
        });
        termsOfServiceSection.setOnClickListener(v -> {
            Toast.makeText(AboutAppActivity.this, "Điều khoản dịch vụ sẽ được mở trong trình duyệt",
                    Toast.LENGTH_SHORT).show();
        });
        licensesSection.setOnClickListener(v -> {
           Toast.makeText(AboutAppActivity.this, "Giấy phép mã nguồn mở sẽ được hiển thị",
                   Toast.LENGTH_SHORT).show();
        });
    }

    private void findViews() {
        btnBack = findViewById(R.id.btnBack);
        btnReturn = findViewById(R.id.btnReturn);
        tvContactEmail = findViewById(R.id.tvContactEmail);
        privacyPolicySection = findViewById(R.id.privacyPolicySection);
        termsOfServiceSection = findViewById(R.id.termsOfServiceSection);
        licensesSection = findViewById(R.id.licensesSection);
    }
}
