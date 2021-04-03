package com.openclassrooms.go4lunch.dao;

import androidx.annotation.VisibleForTesting;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.openclassrooms.go4lunch.database.RestaurantData;

/**
 * Data Access Object to @{@link com.openclassrooms.go4lunch.database.Go4LunchDatabase} database
 * restaurant_table.
 */
@Dao
public interface RestaurantDao {

    /**
     * Inserts a new RestaurantData object in restaurant_table in database.
     * @param restaurantData : New RestaurantData object to add to table
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertRestaurantData(RestaurantData restaurantData);

    /**
     * Deletes all data in restaurant_table.
     */
    @Query("DELETE FROM restaurant_table")
    int deleteAllRestaurantsData();

    @VisibleForTesting
    @Query("SELECT * FROM restaurant_table WHERE item_id = :id")
    RestaurantData getRestaurantData(int id);
}
