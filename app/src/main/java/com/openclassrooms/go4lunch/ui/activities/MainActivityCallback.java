package com.openclassrooms.go4lunch.ui.activities;

/**
 * Callback interface to {@link MainActivity} activity
 */
public interface MainActivityCallback {
    // From NetworkBroadcastReceiver
    void updateNetworkInfoBarDisplay(boolean status);
    void getPlacesFroMDatabaseOrRetrofitInMapViewFragment();

    // From SearchTextWatcher
    void provideSearchQuery(String query);

    // From LogoutDialog
    void logoutUser();

    // From AuthenticationService
    void updateUIAfterRequestCompleted(boolean operation);
    void exitApplicationAfterError();
}
