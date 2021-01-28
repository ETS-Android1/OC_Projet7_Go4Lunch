package com.openclassrooms.go4lunch.ui.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.openclassrooms.go4lunch.BuildConfig;
import com.openclassrooms.go4lunch.R;
import com.openclassrooms.go4lunch.controller.PlacesController;
import com.openclassrooms.go4lunch.databinding.FragmentMapViewBinding;
import com.openclassrooms.go4lunch.ui.dialogs.GPSActivationDialog;
import com.openclassrooms.go4lunch.receivers.GPSBroadcastReceiver;

/**
 * This fragment is used to allow user to interact with a Google Map, search for a restaurant
 * and enable/disable GPS functionality.
 */
public class MapViewFragment extends Fragment implements MapViewFragmentCallback, OnMapReadyCallback, LocationListener {

    public final static String TAG = "TAG_MAP_VIEW_FRAGMENT";
    private FragmentMapViewBinding binding;

    // To catch GPS status changed event
    private GPSBroadcastReceiver gpsBroadcastReceiver;

    // To display a Google map in fragment
    private GoogleMap map;

    // To access Location service methods
    private LocationManager locationManager;

    // To get current user position
    private FusedLocationProviderClient client;

    // Location refresh parameters for GPS provider
    private final static long LOCATION_REFRESH_TIME = 60000;
    private final static float LOCATION_REFRESH_DISTANCE = 150;

    // Google Places
    private PlacesClient placesClient; // To access Places API methods

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
        locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        gpsBroadcastReceiver = new GPSBroadcastReceiver(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                                         .findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(this);

        client = LocationServices.getFusedLocationProviderClient(requireContext()); // Initialize Location provider

        if (!Places.isInitialized()) {
            Places.initialize(requireActivity().getApplicationContext(), BuildConfig.API_KEY);
        }

        // Initialize client to access Place API
        placesClient = Places.createClient(requireContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        requireActivity().registerReceiver(gpsBroadcastReceiver, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));
        updateFloatingButtonIconDisplay(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER));

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                                                   LOCATION_REFRESH_DISTANCE, this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        requireActivity().unregisterReceiver(gpsBroadcastReceiver);
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationManager.removeUpdates(this);
        }
    }

    /**
     * This method handle the click events on FloatingActionButton according to GPS activations status :
     *      - If GPS is disabled : Display dialog interface to user
     *      - If GPS is enabled : move + zoom in current user position
     */
    private void handleFloatingActionButtonListener() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (map != null) {
                binding.fabLocation.setOnClickListener((View v) -> {
                        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) { // GPS Activated
                            centerCursorInCurrentLocation(true);
                        }
                        else { // GPS deactivated
                            GPSActivationDialog dialog = new GPSActivationDialog(this);
                            dialog.show(getParentFragmentManager(), GPSActivationDialog.TAG);
                        }
                    }
                );
            }
        }
    }

    /**
     * This method gets current user location and zoom map camera in this location, according to the type
     * of update passed in parameter (boolean) :
     *      - If false : first call when map is displayed -> initialize user position
     *      - If true : next calls -> repositioning user position
     * @param update : Type of camera update
     */
    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    public void centerCursorInCurrentLocation(boolean update) {
        client.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener((Location location) -> {
                    CameraUpdate cameraUpdate = CameraUpdateFactory
                            .newLatLngZoom(new LatLng(location.getLatitude(),
                                            location.getLongitude()),
                                         18.0f);
                        if (update) map.animateCamera(cameraUpdate);
                        else map.moveCamera(cameraUpdate);
                    }
                );
    }

    // -------------- MapViewFragmentCallback interface implementation --------------
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

    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    @Override
    public void searchPlacesInCurrentLocation() {
        PlacesController.searchPlacesInCurrentLocation(placesClient);
    }

    // -------------- OnMapReadyCallback interface implementation --------------
    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Initialize map
            map = googleMap;
            map.setMyLocationEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(false);
            map.getUiSettings().setCompassEnabled(true);

            // Initialize current position
            centerCursorInCurrentLocation(false);

            // Handle floating action button icon update
            handleFloatingActionButtonListener();

            // Initialize places
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) searchPlacesInCurrentLocation();
        }
    }

    // -------------- LocationListener interface implementation --------------
    @Override
    public void onLocationChanged(@NonNull Location location) { }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) { }

    @Override
    public void onProviderEnabled(@NonNull String provider) { }

    @Override
    public void onProviderDisabled(@NonNull String provider) { }
}