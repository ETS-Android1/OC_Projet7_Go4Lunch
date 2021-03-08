package com.openclassrooms.go4lunch.dao;

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
     * This method is used to insert a new HoursData object in hours_table in database.
     * @param hoursData : new HoursData object to add to table
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertHoursData(HoursData hoursData);

    /**
     * This method is used to delete all data in hours_table.
     */
    @Query("DELETE FROM hours_table")
    void deleteAllHoursData();
}
