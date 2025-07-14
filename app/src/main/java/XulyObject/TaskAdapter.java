package XulyObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ungdunglichhoctap.R;
import DoiTuong.Task;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> taskList;
    private Context context;
    private onTaskClickListener onTaskClickListener;

    public interface onTaskClickListener {
        void onTaskClick(Task task, int position);
        void onTaskComplete(Task task, int position);
        void onTaskDelete(Task task, int position);
    }

    public TaskAdapter(List<Task> taskList, Context context, onTaskClickListener onTaskClickListener) {

        this.taskList = taskList;
        this.context = context;
        this.onTaskClickListener = onTaskClickListener;
    }


    @NonNull
    @Override
        public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.tvTaskTitle.setText(task.getTieuDe());
        holder.tvSubjectName.setText(task.getTenMonHoc());
        if (holder.cbCompleteTask instanceof CheckBox) {
            ((CheckBox) holder.cbCompleteTask).setChecked(task.isDaHoanThanh());
        }
        if(task.getHanChot() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String dueTime = sdf.format(new Date(task.getHanChot()));
            holder.tvDueDate.setText(dueTime);

        } else {
            holder.tvDueDate.setText("Chưa có hạn chót");
        }
        int priorityColor;
        switch (task.getMucDoUuTien()) {
            case "High":
                priorityColor = context.getResources().getColor(R.color.accent_red);
                break;
            case "Medium":
                priorityColor = context.getResources().getColor(R.color.accent_orange);
                break;
            case "Low":
            default:
                priorityColor = context.getResources().getColor(R.color.accent_green);
                break;
        }
        holder.cvPriority.setCardBackgroundColor(priorityColor);
        int iconBgColor;
        int position_mod = position % 4;
        switch (position_mod) {
            case 0:
                iconBgColor = context.getResources().getColor(R.color.accent_orange_light);
                break;
            case 1:
                iconBgColor = context.getResources().getColor(R.color.primary_blue_light);
                break;
            case 2:
                iconBgColor = context.getResources().getColor(R.color.accent_purple_light);
                break;
            case 3:
            default:
                iconBgColor = context.getResources().getColor(R.color.accent_green_light);
                break;
        }
        holder.cvTaskStatus.setCardBackgroundColor(iconBgColor);
        holder.itemView.setOnClickListener(v -> {
            if (onTaskClickListener != null) {
                onTaskClickListener.onTaskClick(task, position);
            }
        });
        holder.cbCompleteTask.setOnClickListener(v -> {
            if (onTaskClickListener != null) {
                onTaskClickListener.onTaskComplete(task, holder.getAdapterPosition());
            }
        });

        holder.btnDeleteTask.setOnClickListener(v -> {
            if (onTaskClickListener != null) {
                onTaskClickListener.onTaskDelete(task, holder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return taskList != null ? taskList.size() : 0;
    }
    public void updateTaskList(List<Task> newTaskList) {
        this.taskList = newTaskList;
        notifyDataSetChanged();
    }
    static class TaskViewHolder extends RecyclerView.ViewHolder {
        public CheckBox cbCompleteTask;
        public View btnDeleteTask;
        TextView tvTaskTitle, tvSubjectName, tvDueDate;
        CardView cvPriority, cvTaskStatus;
        public TaskViewHolder(@NonNull View parent) {
            super(parent);
            tvTaskTitle = itemView.findViewById(R.id.tvTaskTitle);
            tvSubjectName = itemView.findViewById(R.id.tvSubjectName);
            tvDueDate = itemView.findViewById(R.id.tvDueDate);
            cvPriority = itemView.findViewById(R.id.cvPriority);
            cvTaskStatus = itemView.findViewById(R.id.cvTaskIcon);
            btnDeleteTask = itemView.findViewById(R.id.btnDeleteTask);
            cbCompleteTask = itemView.findViewById(R.id.cbCompleteTask);
        }
    }
}
