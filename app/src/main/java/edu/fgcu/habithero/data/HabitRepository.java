package edu.fgcu.habithero.data;

import android.app.Application;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class HabitRepository {
    private final HabitDao habitDao;
    private final LiveData<List<Habit>> allHabits;
    private final Executor executor = Executors.newSingleThreadExecutor();

    public HabitRepository(Application app) {
        HabitDatabase db = HabitDatabase.getInstance(app);
        habitDao = db.habitDao();
        allHabits = habitDao.getAll();
    }

    public LiveData<List<Habit>> getAllHabits() {
        return allHabits;
    }

    public void insert(final Habit h) {
        executor.execute(() -> habitDao.insert(h));
    }

    public void update(final Habit h) {
        executor.execute(() -> habitDao.update(h));
    }

    public void delete(final Habit h) {
        executor.execute(() -> habitDao.delete(h));
    }
}
