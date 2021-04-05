package com.openclassrooms.go4lunch.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.openclassrooms.go4lunch.dao.HoursDao;
import com.openclassrooms.go4lunch.dao.RestaurantAndHoursDao;
import com.openclassrooms.go4lunch.dao.RestaurantDao;
import com.openclassrooms.go4lunch.utils.AppInfo;

/**
 * Go4Lunch application database containing :
 *      - a "restaurant_table" table storing a list of @{@link RestaurantData} object
 *      - a "hours_table" table storing a list of {@link HoursData} object
 */
@Database(entities = {RestaurantData.class, HoursData.class}, version = 1, exportSchema = false)
public abstract class Go4LunchDatabase extends RoomDatabase {

    // Dao
    public abstract RestaurantDao restaurantDao();
    public abstract HoursDao hoursDao();
    public abstract RestaurantAndHoursDao restaurantAndHoursDao();

    // Application Database instance
    private static Go4LunchDatabase instance;

    // Create Database singleton
    public static synchronized Go4LunchDatabase getInstance(Context context) {
        if (instance == null) {
            // Create instance
            instance = Room.databaseBuilder(context, Go4LunchDatabase.class,
                                            AppInfo.SQLITE_DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
