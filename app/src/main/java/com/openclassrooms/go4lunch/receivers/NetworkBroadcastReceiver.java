package com.openclassrooms.go4lunch.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.openclassrooms.go4lunch.ui.activities.MainActivityCallback;

/**
 * A BroadcastReceiver class used to detect any "network change" event and send the information
 * to a {@link MainActivityCallback} callback
 */
public class NetworkBroadcastReceiver extends BroadcastReceiver {

    private final MainActivityCallback callback;

    public NetworkBroadcastReceiver(MainActivityCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        callback.updateNetworkInfoBarDisplay(networkInfo != null);

        if (!isInitialStickyBroadcast()) { // This broadcast must only detect connectivity status changes
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            if (networkInfo != null) {
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    callback.getPlacesFroMDatabaseOrRetrofitInMapViewFragment();
                }
            }
        }
    }
}
