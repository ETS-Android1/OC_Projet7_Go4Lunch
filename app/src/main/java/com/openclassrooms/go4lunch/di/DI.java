package com.openclassrooms.go4lunch.di;

import android.content.Context;
import com.openclassrooms.go4lunch.database.Go4LunchDatabase;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Dependency injection class
 */
public class DI {

    /**
     * Returns an instance of the application database.
     * @param context : Context
     * @return : Go4Lunch SQLite Database
     */
    public static Go4LunchDatabase provideDatabase(Context context) {
        return Go4LunchDatabase.getInstance(context);
    }

    /**
     * Provides an instance of an Executor object to use to execute Runnable tasks outside the main thread.
     * @return : Executor object
     */
    public static Executor provideExecutor() {
        return Executors.newSingleThreadExecutor();
    }

}
