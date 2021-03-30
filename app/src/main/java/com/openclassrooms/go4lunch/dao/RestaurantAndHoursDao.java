package com.openclassrooms.go4lunch.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;
import com.openclassrooms.go4lunch.database.RestaurantAndHoursData;
import java.util.List;

/**
 * Data Access Object to @{@link com.openclassrooms.go4lunch.database.Go4LunchDatabase} database
 * hours_table and restaurant_table tables.
 */
@Dao
public interface RestaurantAndHoursDao {

    /**
     * Get all Restaurant data in database with their associated hours data.
     * @return : LiveData containing the list of all RestaurantAndHours objects
     */
    @Transaction
    @Query("SELECT * FROM restaurant_table")
    LiveData<List<RestaurantAndHoursData>> loadAllRestaurantsWithHours();

}
