package com.example.mediforecast;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MedicineDao {

    @Insert
    void insert(Medicine medicine);

    @Delete
    void delete(Medicine medicine);

    @Update
    void update(Medicine medicine);

    @Query("SELECT * FROM medicines")
    LiveData<List<Medicine>> getAllMedicines();

    @Query("SELECT * FROM medicines WHERE id = :id LIMIT 1")
    Medicine getMedicineById(int id);
}

