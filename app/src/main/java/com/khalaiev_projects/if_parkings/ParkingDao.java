package com.khalaiev_projects.if_parkings;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface ParkingDao {

    @Insert
    void insert(Parking parking);

    @Query("SELECT * FROM parkings_table ORDER BY id ASC")
    LiveData<List<Parking>> getAllParkings();

//    @Query("SELECT * FROM parkings_table WHERE id = :id ")
//    Parking getParking(int id);

    @Query("DELETE FROM parkings_table WHERE id = :id")
    void deleteParking(int id);

    @Query("DELETE FROM parkings_table")
    void deleteAllParkings();

    @Query("SELECT COUNT(*) FROM parkings_table")
    int getCount();
}
