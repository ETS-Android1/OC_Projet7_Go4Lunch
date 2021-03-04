package com.openclassrooms.go4lunch.ui.fragments.map;

/**
 * Callback interface to {@link MapViewFragment} fragment
 */
public interface MapViewFragmentCallback {
    void checkLocationSharedPreferences();
    void updateFloatingButtonIconDisplay(boolean status);
    void activateGPS();
}
