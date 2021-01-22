package com.openclassrooms.go4lunch.ui.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
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
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        callback.updateFloatingButtonIconDisplay(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER));
    }

}
