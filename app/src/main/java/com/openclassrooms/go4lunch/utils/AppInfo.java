package com.openclassrooms.go4lunch.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.openclassrooms.go4lunch.BuildConfig;
import static android.content.Context.MODE_PRIVATE;

public class AppInfo {

    public static boolean checkIfFirstRunApp(Context context) {
        final String PREFS_FIRST_RUN = "first_run_app";
        final String PREF_VERSION_CODE_KEY = "version_code";

        // Get current version code
        int currentVersionCode = BuildConfig.VERSION_CODE;

        // Get saved version code
        SharedPreferences prefsVersionCode = context.getSharedPreferences(PREFS_FIRST_RUN, MODE_PRIVATE);
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
}
