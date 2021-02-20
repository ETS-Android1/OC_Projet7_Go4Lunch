package com.openclassrooms.go4lunch.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import com.openclassrooms.go4lunch.ui.fragments.map.MapViewFragmentCallback;

/**
 * This class is used to detect any "GPS status change" event and send to information
 * to a {@link MapViewFragmentCallback} callback
 */
public class GPSBroadcastReceiver extends BroadcastReceiver {

    private final MapViewFragmentCallback callback;

    public GPSBroadcastReceiver(MapViewFragmentCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean providerStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        callback.updateFloatingButtonIconDisplay(providerStatus);

        if (connectivityManager.getActiveNetworkInfo() != null) {
            if (providerStatus) callback.searchPlacesFromCurrentLocation();
        }
    }
}
