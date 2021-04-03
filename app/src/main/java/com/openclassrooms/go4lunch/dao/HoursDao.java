package com.openclassrooms.go4lunch.dao;

import androidx.annotation.VisibleForTesting;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.openclassrooms.go4lunch.database.HoursData;

/**
 * Data Access Object to @{@link com.openclassrooms.go4lunch.database.Go4LunchDatabase} database
 * hours_table.
 */
@Dao
public interface HoursDao {

    /**
     * Inserts a new HoursData object in hours_table in database.
     * @param hoursData : New HoursData object to add to table
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertHoursData(HoursData hoursData);

    /**
     * Deletes all data in hours_table.
     */
    @Query("DELETE FROM hours_table")
    int deleteAllHoursData();

    @VisibleForTesting
    @Query("SELECT * FROM hours_table WHERE id = :id")
    HoursData getHoursData(int id);
}
