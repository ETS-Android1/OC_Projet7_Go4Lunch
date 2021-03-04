package com.openclassrooms.go4lunch.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.openclassrooms.go4lunch.model.Restaurant;
import java.util.List;

/**
 * Data Access Object to @{@link com.openclassrooms.go4lunch.database.Go4LunchDatabase} database
 */
@Dao
public interface RestaurantDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertRestaurant(Restaurant restaurant);

    @Update
    void updateRestaurant(Restaurant restaurant);

    @Query("DELETE FROM restaurant_table")
    void deleteAllRestaurants();

    @Query("SELECT * FROM restaurant_table ORDER BY item_id")
    LiveData<List<Restaurant>> loadAllRestaurants();
}
