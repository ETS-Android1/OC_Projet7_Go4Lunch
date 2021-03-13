package com.openclassrooms.go4lunch.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import com.openclassrooms.go4lunch.BuildConfig;
import com.openclassrooms.go4lunch.dao.HoursDao;
import com.openclassrooms.go4lunch.dao.RestaurantAndHoursDao;
import com.openclassrooms.go4lunch.dao.RestaurantDao;
import com.openclassrooms.go4lunch.utils.DataConverters;

/**
 * Go4Lunch application database, containing a table of Restaurants
 */
@Database(entities = {RestaurantData.class, HoursData.class}, version = BuildConfig.VERSION_CODE, exportSchema = false)
@TypeConverters({DataConverters.class})
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
            instance = Room.databaseBuilder(context, Go4LunchDatabase.class, "go4lunch_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
