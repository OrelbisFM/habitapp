package edu.fgcu.habithero.vm;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import edu.fgcu.habithero.data.Habit;
import edu.fgcu.habithero.data.HabitRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

public class HabitViewModel extends AndroidViewModel {
    private final HabitRepository repo;
    private final LiveData<List<Habit>> habits;

    public HabitViewModel(@NonNull Application application) {
        super(application);
        repo = new HabitRepository(application);
        habits = repo.getAllHabits();
    }

    public LiveData<List<Habit>> getHabits() { return habits; }

    public void addHabit(String title, String note, String frequency) {
        Habit h = new Habit(title, note, frequency);
        repo.insert(h);
    }

    public void delete(Habit habit) {
        repo.delete(habit);
    }

    private boolean isTodayAllowed(String frequency) {
        DayOfWeek today = LocalDate.now().getDayOfWeek();
        switch (frequency) {
            case "WEEKDAYS":
                return today != DayOfWeek.SATURDAY && today != DayOfWeek.SUNDAY;

            case "WEEKENDS":
                return today == DayOfWeek.SATURDAY || today == DayOfWeek.SUNDAY;

            default:
                return true;
        }
    }
    public boolean toggleToday(Habit habit) {
        if (!isTodayAllowed(habit.frequency)) {
            return false;
        }

        long todayEpoch = LocalDate.now(ZoneId.systemDefault()).toEpochDay();
        long last = habit.lastCompletedEpochDay;

        if (last == todayEpoch) {
            // unmark today's completion -> decrement streak (min 0) and set lastCompletedEpochDay to yesterday
            habit.streak = Math.max(0, habit.streak - 1);
            habit.lastCompletedEpochDay = todayEpoch - 1; // or Long.MIN_VALUE to indicate last was earlier
        } else if (last == todayEpoch - 1) {
            // consecutive day -> increment streak
            habit.streak = habit.streak + 1;
            habit.lastCompletedEpochDay = todayEpoch;
        } else {
            // break in streak -> reset to 1
            habit.streak = 1;
            habit.lastCompletedEpochDay = todayEpoch;
        }

        repo.update(habit);
        return true;
    }
}
