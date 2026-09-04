package edu.fgcu.habithero.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "habit")
public class Habit {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @NonNull
    public String title;

    public String note;

    // "DAILY", "WEEKDAYS", "WEEKENDS"
    @NonNull
    public String frequency;

    public int streak;

    public long lastCompletedEpochDay;

    public Habit(@NonNull String title, String note, @NonNull String frequency) {
        this.title = title;
        this.note = note;
        this.frequency = frequency;
        this.streak = 0;
        this.lastCompletedEpochDay = Long.MIN_VALUE;
    }
}
