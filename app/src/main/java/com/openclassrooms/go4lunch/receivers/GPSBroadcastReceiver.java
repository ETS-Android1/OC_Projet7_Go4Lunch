package com.openclassrooms.go4lunch.receivers;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.util.Log;
import androidx.core.content.ContextCompat;
import com.openclassrooms.go4lunch.ui.fragments.MapViewFragmentCallback;

/**
 * This class is used to detect any "GPS status change" event and send to information to a {@link MapViewFragmentCallback} callback
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
        callback.updateFloatingButtonIconDisplay(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER));

        if (connectivityManager.getActiveNetworkInfo() != null) {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    callback.searchPlacesInCurrentLocation();
                }
            }
        }
    }

}
