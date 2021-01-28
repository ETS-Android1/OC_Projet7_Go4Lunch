package com.openclassrooms.go4lunch.ui.fragments;

/**
 * Callback interface to {@link MapViewFragment} fragment
 */
public interface MapViewFragmentCallback {
    void searchPlacesInCurrentLocation();
    void updateFloatingButtonIconDisplay(boolean status);
    void activateGPS();
}
