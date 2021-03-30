package com.openclassrooms.go4lunch.ui.fragments.map;

/**
 * Callback interface to {@link MapViewFragment} fragment
 */
public interface MapViewFragmentCallback {
    // Used in MapViewFragment and GPSBroadcastReceiver
    void getPlacesFromDatabaseOrRetrofitRequest();
    void updateFloatingButtonIconDisplay(boolean status);
    // Used in GPSActivationDialog
    void activateGPS();
    // Used in PlacesRepository
    void restoreListFromDatabase();
    void searchPlacesFromCurrentLocation();
}
