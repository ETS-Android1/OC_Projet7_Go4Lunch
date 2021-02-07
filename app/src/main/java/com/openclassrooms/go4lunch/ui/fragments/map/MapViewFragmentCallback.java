package com.openclassrooms.go4lunch.ui.fragments.map;

import com.openclassrooms.go4lunch.model.Restaurant;

/**
 * Callback interface to {@link MapViewFragment} fragment
 */
public interface MapViewFragmentCallback {
    void updateMyLocationCursorDisplay(boolean display);
    void searchPlacesInCurrentLocation();
    void updateFloatingButtonIconDisplay(boolean status);
    void activateGPS();
    void displayMarkerInMap(Restaurant restaurant);
}
