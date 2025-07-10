package XulyObject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import DoiTuong.Lessons;
import com.example.ungdunglichhoctap.R;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.LessonViewHolder> {

    private List<Lessons> lessonsList;
    private OnLessonClickListener listener;

    public interface OnLessonClickListener {
        void onLessonClick(Lessons lesson);
    }

    public LessonAdapter(List<Lessons> lessonsList, OnLessonClickListener listener) {
        this.lessonsList = lessonsList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public LessonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lesson, parent, false);
        return new LessonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LessonViewHolder holder, int position) {
        Lessons lesson = lessonsList.get(position);
        holder.bind(lesson, listener);
    }

    @Override
    public int getItemCount() {
        return lessonsList.size();
    }

    static class LessonViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView tvTitle, tvTime, tvRoom, tvStatus;

        public LessonViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardViewLesson);
            tvTitle = itemView.findViewById(R.id.tvLessonTitle);
            tvTime = itemView.findViewById(R.id.tvLessonTime);
            tvRoom = itemView.findViewById(R.id.tvLessonRoom);
            tvStatus = itemView.findViewById(R.id.tvLessonStatus);
        }

        void bind(Lessons lesson, OnLessonClickListener listener) {
            tvTitle.setText(lesson.getTieuDe());

            // Format time: "13:30 - 15:00"
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            String startTime = timeFormat.format(new Date(lesson.getThoiGianBatDau()));
            String endTime = timeFormat.format(new Date(lesson.getThoiGianKetThuc()));
            tvTime.setText(startTime + " - " + endTime);

            tvRoom.setText("Phòng " + lesson.getViTriPhong());

            // Set status
            long currentTime = System.currentTimeMillis();
            if (currentTime < lesson.getThoiGianBatDau()) {
                // Upcoming class
                tvStatus.setText("Sắp diễn ra");
                tvStatus.setTextColor(itemView.getContext().getResources().getColor(R.color.accent_gold));
            } else if (currentTime >= lesson.getThoiGianBatDau() && currentTime <= lesson.getThoiGianKetThuc()) {
                tvStatus.setText("Đang diễn ra");
                tvStatus.setTextColor(itemView.getContext().getResources().getColor(R.color.accent_green));
            } else {
                tvStatus.setText("Đã kết thúc");
                tvStatus.setTextColor(itemView.getContext().getResources().getColor(R.color.text_secondary));
            }

            // Set weekday text if it's the first day shown or different from previous day
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(lesson.getThoiGianBatDau());

            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE, dd/MM", new Locale("vi", "VN"));
            tvStatus.setText(dayFormat.format(calendar.getTime()));

            cardView.setOnClickListener(v -> {
                listener.onLessonClick(lesson);
            });
        }
    }
}