package edu.fgcu.habithero.ui.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import edu.fgcu.habithero.R;
import edu.fgcu.habithero.data.Habit;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;

public class HabitAdapter extends ListAdapter<Habit, HabitAdapter.HabitViewHolder> {

    public interface HabitItemListener {
        void onToggleToday(Habit habit, View anchorView);
        void onLongPress(Habit habit);
    }

    private final HabitItemListener listener;

    protected HabitAdapter(HabitItemListener listener) {
        super(DIFF);
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<Habit> DIFF = new DiffUtil.ItemCallback<Habit>() {
        @Override
        public boolean areItemsTheSame(@NonNull Habit oldItem, @NonNull Habit newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Habit oldItem, @NonNull Habit newItem) {
            return oldItem.title.equals(newItem.title)
                    && java.util.Objects.equals(oldItem.note, newItem.note)
                    && oldItem.streak == newItem.streak
                    && oldItem.lastCompletedEpochDay == newItem.lastCompletedEpochDay
                    && oldItem.frequency.equals(newItem.frequency);
        }
    };

    @NonNull
    @Override
    public HabitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_habit, parent, false);
        return new HabitViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HabitViewHolder holder, int position) {
        Habit h = getItem(position);

        holder.bind(h);
    }

    class HabitViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvNote, tvStreak;
        CheckBox toggleToday;

        HabitViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvNote = itemView.findViewById(R.id.tvNote);
            tvStreak = itemView.findViewById(R.id.tvStreak);
            toggleToday = itemView.findViewById(R.id.toggleToday);

            itemView.setOnLongClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    listener.onLongPress(getItem(pos));
                }
                return true;
            });

            toggleToday.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    listener.onToggleToday(getItem(pos), v);
                }
            });
        }

        void bind(Habit h) {
            tvTitle.setText(h.title);
            if (h.note == null || h.note.trim().isEmpty()) {
                tvNote.setVisibility(View.GONE);
            } else {
                tvNote.setText(h.note);
                tvNote.setVisibility(View.VISIBLE);
            }

            long todayEpoch = LocalDate.now(ZoneId.systemDefault()).toEpochDay();
            // toggle state based on whether lastCompletedEpochDay == today
            toggleToday.setChecked(h.lastCompletedEpochDay == todayEpoch);

            boolean allowed;
            DayOfWeek today = LocalDate.now().getDayOfWeek();
            switch (h.frequency) {
                case "WEEKDAYS":
                    allowed = (today != DayOfWeek.SATURDAY && today != DayOfWeek.SUNDAY);
                    break;

                case "WEEKENDS":
                    allowed = (today == DayOfWeek.SATURDAY || today == DayOfWeek.SUNDAY);
                    break;

                default:
                    allowed = true;
            }
            tvStreak.setText("🔥" + h.streak);
        }
    }
}
