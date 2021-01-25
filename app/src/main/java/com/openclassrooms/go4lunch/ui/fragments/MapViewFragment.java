package com.openclassrooms.go4lunch.ui.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.openclassrooms.go4lunch.R;
import com.openclassrooms.go4lunch.databinding.FragmentMapViewBinding;
import com.openclassrooms.go4lunch.ui.dialogs.GPSActivationDialog;
import com.openclassrooms.go4lunch.ui.receivers.GPSBroadcastReceiver;

/**
 * This fragment is used to allow user to interact with a Google Map, search for a restaurant
 * and enable/disable GPS functionality.
 */
public class MapViewFragment extends Fragment implements MapViewFragmentCallback{

    public final static String TAG = "TAG_MAP_VIEW_FRAGMENT";
    private FragmentMapViewBinding binding;
    private LocationManager locationManager;
    private GPSBroadcastReceiver gpsBroadcastReceiver; // To catch GPS status changed event

    public MapViewFragment() { /* Empty constructor */ }

    public static MapViewFragment newInstance() {
        return new MapViewFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMapViewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        gpsBroadcastReceiver = new GPSBroadcastReceiver(this);
        handleFloatingActionButtonListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(gpsBroadcastReceiver, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));
        updateFloatingButtonIconDisplay(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER));
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(gpsBroadcastReceiver);
    }

    /**
     * This method is used to update FloatingActionButton Icon display according to GPS activation
     * status.
     * @param status : GPS activation status.
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void updateFloatingButtonIconDisplay(boolean status) {
        if (status) {
            binding.fabLocation.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_gps_fixed_24dp_dark_grey));
            binding.fabLocation.setSupportImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.dark_grey)));
        }
        else {
            binding.fabLocation.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_gps_off_24dp_red));
            binding.fabLocation.setSupportImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
        }
    }

    @Override
    public void activateGPS() { startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)); }

    /**
     * This method handle the click events on FloatingActionButton.
     */
    private void handleFloatingActionButtonListener() {
        binding.fabLocation.setOnClickListener((View v) -> {
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) { // GPS Activated

                }
                else { // GPS deactivated
                    GPSActivationDialog dialog = new GPSActivationDialog(this);
                    dialog.show(getParentFragmentManager(), GPSActivationDialog.TAG);
                }
            }
        );
    }
}