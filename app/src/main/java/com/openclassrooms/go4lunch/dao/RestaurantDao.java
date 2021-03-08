package com.openclassrooms.go4lunch.dao;

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
     * This method is used to insert a new RestaurantData object in restaurant_table in database.
     * @param restaurantData : new RestaurantData object to add to table
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertRestaurantData(RestaurantData restaurantData);

    /**
     * This method is used to delete all data in restaurant_table.
     */
    @Query("DELETE FROM restaurant_table")
    void deleteAllRestaurantsData();
}
