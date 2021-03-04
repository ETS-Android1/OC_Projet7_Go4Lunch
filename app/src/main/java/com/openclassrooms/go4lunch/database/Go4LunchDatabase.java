package com.openclassrooms.go4lunch.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.openclassrooms.go4lunch.BuildConfig;
import com.openclassrooms.go4lunch.dao.RestaurantDao;
import com.openclassrooms.go4lunch.model.Restaurant;

/**
 * Go4Lunch application database, containing a table of Restaurants
 */
@Database(entities = {Restaurant.class}, version = BuildConfig.VERSION_CODE, exportSchema = false)
public abstract class Go4LunchDatabase extends RoomDatabase {

    // Dao
    public abstract RestaurantDao restaurantDao();

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
