package com.openclassrooms.go4lunch.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.openclassrooms.go4lunch.BuildConfig;
import static android.content.Context.MODE_PRIVATE;

public class AppInfo {

    // SharedPreferences name files
    public static final String FILE_PREF_NEXT_PAGE_TOKEN = "FILE_PREF_NEXT_PAGE_TOKEN";
    public static final String FILE_USER_ID = "FILE_USER_ID";
    public static final String FILE_PREF_USER_POSITION = "FILE_PREF_USER_POSITION";
    public static final String FILE_PREF_FIRST_RUN = "FILE_REFS_FIRST_RUN";
    public static final String FILE_PREF_SELECTED_RESTAURANT = "FILE_PREF_SELECTED_RESTAURANT";

    // SharedPreferences Options files
    public static final String FILE_OPTIONS = "FILE_OPTIONS";

    /**
     * Method used to check if user launch the app for the first time
     * @param context : context of the view
     * @return : boolean value
     */
    public static boolean checkIfFirstRunApp(Context context) {
        final String PREF_VERSION_CODE_KEY = "version_code";

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
