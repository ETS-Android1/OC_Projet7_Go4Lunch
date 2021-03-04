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
     * Static method to return an instance of the application database
     * @param context : Context
     * @return : Database
     */
    public static Go4LunchDatabase provideDatabase(Context context) {
        return Go4LunchDatabase.getInstance(context);
    }

    /**
     * Static method to provide an instance of a Thread
     * @return : Executor
     */
    public static Executor provideExecutor() {
        return Executors.newSingleThreadExecutor();
    }

}
