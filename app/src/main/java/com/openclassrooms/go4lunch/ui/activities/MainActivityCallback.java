package com.openclassrooms.go4lunch.ui.activities;

/**
 * Callback interface to {@link MainActivity} activity
 */
public interface MainActivityCallback {
    void searchCurrentLocationInMapViewFragment();
    void logoutUser();
    void updateNetworkInfoBarDisplay(boolean status);
}