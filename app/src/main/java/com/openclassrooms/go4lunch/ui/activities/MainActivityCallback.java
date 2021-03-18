package com.openclassrooms.go4lunch.ui.activities;

import java.util.List;

/**
 * Callback interface to {@link MainActivity} activity
 */
public interface MainActivityCallback {
    void getPlacesFroMDatabaseOrRetrofitInMapViewFragment();
    void logoutUser();
    void updateNetworkInfoBarDisplay(boolean status);
    void provideSearchQuery(String query);
}
