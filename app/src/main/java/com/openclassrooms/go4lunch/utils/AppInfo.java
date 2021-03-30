package com.openclassrooms.go4lunch.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.openclassrooms.go4lunch.BuildConfig;
import static android.content.Context.MODE_PRIVATE;

/**
 * class containING static values for the Go4Lunch application, and method to check app configuration.
 */
public class AppInfo {

    // Database name
    public static final String SQLITE_DATABASE_NAME = "go4lunch_database";

    // Root collection id in Firestore
    public static final String ROOT_COLLECTION_ID = "list_employees";

    // SharedPreferences name files
    public static final String FILE_PREF_NEXT_PAGE_TOKEN = "FILE_PREF_NEXT_PAGE_TOKEN";
    public static final String FILE_FIRESTORE_USER_ID = "FILE_FIRESTORE_USER_ID";
    public static final String FILE_PREF_USER_POSITION = "FILE_PREF_USER_POSITION";
    public static final String FILE_PREF_FIRST_RUN = "FILE_REFS_FIRST_RUN";
    public static final String FILE_PREF_SELECTED_RESTAURANT = "FILE_PREF_SELECTED_RESTAURANT";
    public static final String FILE_PREF_NB_PERMISSION_REQUESTS = "FILE_PREF_NB_PERMISSION_REQUEST";

    // SharedPreferences Options files
    public static final String FILE_OPTIONS = "FILE_OPTIONS";

    // SharedPreferences keys
    public static final String PREF_VERSION_CODE_KEY = "version_code";
    public static final String PREF_FIRST_NEXT_PAGE_TOKEN_KEY = "first_next_page_token";
    public static final String PREF_SECOND_NEXT_PAGE_TOKEN_KEY = "second_next_page_token";
    public static final String PREF_OLD_LAT_POSITION_KEY = "old_lat_position";
    public static final String PREF_OLD_LON_POSITION_KEY = "old_lon_position";
    public static final String PREF_SELECTED_RESTAURANT_KEY = "selected_restaurant";
    public static final String PREF_CLUSTER_OPTION_KEY = "cluster_option";
    public static final String PREF_ALARM_OPTION_STATUS_KEY = "alarm_option_status";
    public static final String PREF_FIRESTORE_USER_ID_KEY = "firestore_user_id";
    public static final String PREF_NB_PERMISSION_REQUESTS = "nb_permission_requests";

    /**
     * Method used to check if user launch the app for the first time
     * @param context : context of the view
     * @return : boolean value
     */
    public static boolean checkIfFirstRunApp(Context context) {

        // Get current version code
        int currentVersionCode = BuildConfig.VERSION_CODE;

        // Get saved version code
        SharedPreferences prefsVersionCode = context.getSharedPreferences(FILE_PREF_FIRST_RUN, MODE_PRIVATE);
        int savedVersionCode = prefsVersionCode.getInt(PREF_VERSION_CODE_KEY, -1);

        if (savedVersionCode == -1 || currentVersionCode > savedVersionCode) { // New install (first run) OR Update app
            SharedPreferences.Editor editor = prefsVersionCode.edit();
            editor.putInt(PREF_VERSION_CODE_KEY, currentVersionCode).apply();
            return true;
        }
        else { // Normal run in same app version
            return false;
        }
    }

    /**
     * This method is used to retrieve the current Status bar size
     * @return : size of the status bar
     */
    public static int getStatusBarSize(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        int statusBarSize = 0;
        statusBarSize = context.getResources().getDimensionPixelSize(resourceId);
        return statusBarSize;
    }
}
