package com.random.lucky.spin.wheel.randompicker.room_database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SpinWheelDAO {
    @Insert
    void insertSpinWheel(SpinWheelModel spinWheelModel);

    @Delete
    void deleteSpinWheel(SpinWheelModel spinWheelModel);

    @Update
    void updateSpinWheel(SpinWheelModel spinWheelModel);

    @Query("SELECT * FROM table_spinwheel")
    List<SpinWheelModel> getAllSpinWheel();

    @Query("SELECT * FROM table_spinwheel WHERE type = :type")
    List<SpinWheelModel> getAllSpinWheelByType(String type);
}
