package edu.fgcu.habithero.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import java.util.List;

@Dao
public interface HabitDao {
    @Query("SELECT * FROM habit ORDER BY id DESC")
    LiveData<List<Habit>> getAll();

    @Insert
    void insert(Habit h);

    @Update
    void update(Habit h);

    @Delete
    void delete(Habit h);
}
