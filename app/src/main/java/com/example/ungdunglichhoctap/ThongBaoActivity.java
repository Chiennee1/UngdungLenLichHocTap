package com.example.ungdunglichhoctap;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;

public class ThongBaoActivity extends AppCompatActivity {
    private TextView btnBack;
    private TextView btnMarkAllRead;
    private Button btnAllNotifications, btnTaskNotifications, btnSystemNotifications;
    private Button btnClearAll;
    private LinearLayout emptyState;
    private NestedScrollView nonificationScrollView;
    private CardView[] notificationCards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        findView();
        setupListeners();
        setUpFilterTabs();

    }

    private void setUpFilterTabs() {
        btnAllNotifications.setOnClickListener(v -> {
            setActiveTab(btnAllNotifications);
            showAllNotifications();
        });
        btnTaskNotifications.setOnClickListener(v -> {
            setActiveTab(btnTaskNotifications);
            filterNonifications("task");
        });
        btnSystemNotifications.setOnClickListener(v -> {
            setActiveTab(btnSystemNotifications);
            filterNonifications("system");
        });
    }

    private void filterNonifications(String filterType) {
        boolean hasVisibleCards = false;
        for(int i = 0; i < notificationCards.length; i++) {
            CardView card = notificationCards[i];
            LinearLayout layout = (LinearLayout) card.getChildAt(0);
            LinearLayout contentLayout = (LinearLayout) layout.getChildAt(1);
            TextView titleTextView = (TextView) contentLayout.getChildAt(0);
            String title = titleTextView.getText().toString();
            boolean isVisible = false;
            if (filterType.equals("task")) {
                isVisible = title.contains("nhiệm vụ") ||
                        title.contains("Nhiệm vụ") ||
                        title.contains("Hạn nộp") ||
                        title.contains("Pomodoro");
            } else if (filterType.equals("system")) {
                isVisible = title.contains("hệ thống") ||
                        title.contains("Cập nhật") ||
                        title.contains("Huy hiệu") ||
                        title.contains("Chuỗi học tập");
            }

            card.setVisibility(isVisible ? View.VISIBLE : View.GONE);
            if(isVisible) {
                hasVisibleCards = true;
            }
        }
        upDateEmptyState();
    }

    private void showAllNotifications() {
        for(CardView card : notificationCards) {
            card.setVisibility(View.VISIBLE);
        }
        upDateEmptyState();
    }

    private void upDateEmptyState() {
        boolean hasVisibleCards = false;
        for (CardView card : notificationCards) {
            if (card.getVisibility() == View.VISIBLE) {
                hasVisibleCards = true;
                break;
            }
        }
        emptyState.setVisibility(hasVisibleCards ? View.GONE : View.VISIBLE);
        nonificationScrollView.setVisibility(hasVisibleCards ? View.VISIBLE : View.GONE);
    }

    private void setActiveTab(Button activeButton) {
        btnAllNotifications.setBackgroundTintList(null);
        btnAllNotifications.setTextColor(ContextCompat.getColor(this, R.color.text_secondary));
        btnAllNotifications.setBackgroundResource(android.R.color.transparent);
        btnAllNotifications.setBackgroundResource(R.color.border_light);
        btnAllNotifications.setWidth(1);

        btnTaskNotifications.setBackgroundTintList(null);
        btnTaskNotifications.setTextColor(ContextCompat.getColor(this, R.color.text_secondary));
        btnTaskNotifications.setBackgroundResource(android.R.color.transparent);
        btnTaskNotifications.setBackgroundResource(R.color.border_light);
        btnTaskNotifications.setWidth(1);

        btnSystemNotifications.setBackgroundTintList(null);
        btnSystemNotifications.setTextColor(ContextCompat.getColor(this, R.color.text_secondary));
        btnSystemNotifications.setBackgroundResource(android.R.color.transparent);
        btnSystemNotifications.setBackgroundResource(R.color.border_light);
        btnSystemNotifications.setWidth(1);

        activeButton.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.primary_blue));
        activeButton.setTextColor(ContextCompat.getColor(this, R.color.text_white));
        activeButton.setWidth(0);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());
        btnMarkAllRead.setOnClickListener(v -> {MarkNonificationAsRead();
        Toast.makeText(this, "Tất cả thông báo đã được đánh dấu là đã đọc", Toast.LENGTH_SHORT).show();
        });
        btnClearAll.setOnClickListener(v -> {
            showClearConfirmationDialog();
        });
    }

    private void showClearConfirmationDialog() {
         new AlertDialog.Builder(this)
                 .setTitle("Xóa tất cả thông báo")
                 .setMessage("Bạn có chắc chắn muốn xóa tất cả thông báo không?")
                 .setPositiveButton("Đồng ý", (dialog, which) -> {
                     clearNotifications();
                 })
                 .setNegativeButton("Hủy", null)
                 .show();
    }

    private void clearNotifications() {
        for (CardView card : notificationCards) {
            card.setVisibility(View.GONE);
        }
        emptyState.setVisibility(View.VISIBLE);
        nonificationScrollView.setVisibility(View.GONE);
        Toast.makeText(this, "Tất cả thông báo đã được xóa", Toast.LENGTH_SHORT).show();
    }

    private void MarkNonificationAsRead() {
        for (CardView card : notificationCards) {
            card.setAlpha(0.8f);
        }
        upDateEmptyState();
    }

    private void findView() {
        btnBack = findViewById(R.id.btnBack);
        btnMarkAllRead = findViewById(R.id.btnMarkAllRead);
        btnAllNotifications = findViewById(R.id.btnAllNotifs);
        btnTaskNotifications = findViewById(R.id.btnTaskNotifs);
        btnSystemNotifications = findViewById(R.id.btnSystemNotifs);
        btnClearAll = findViewById(R.id.btnClearNotifications);
        emptyState = findViewById(R.id.emptyState);
        nonificationScrollView = findViewById(R.id.notificationsScrollView);
        LinearLayout notificationContainer = (LinearLayout) nonificationScrollView.getChildAt(0);
        int cardCount = 0;
        for(int i = 0; i < notificationContainer.getChildCount(); i++) {

            if (notificationContainer.getChildAt(i) instanceof CardView) {
                cardCount++;
            }
        }

        notificationCards = new CardView[cardCount];
        int index = 0;
        for(int i = 0; i < notificationContainer.getChildCount(); i++) {
            View child = notificationContainer.getChildAt(i);
            if (child instanceof CardView) {
                notificationCards[index++] = (CardView) child;
                final int finalIndex = index - 1;
                child.setOnClickListener(v -> hanlderNotificationClick(finalIndex));
            }

        }

    }
    private void hanlderNotificationClick(int finalIndex) {
        Toast.makeText(this, "Đã nhấn vào thông báo " + (finalIndex + 1), Toast.LENGTH_SHORT).show();
        View child = notificationCards[finalIndex];
        child.setAlpha(0.8f);
    }
}
