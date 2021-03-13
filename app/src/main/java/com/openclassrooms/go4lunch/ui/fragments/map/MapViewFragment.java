package com.openclassrooms.go4lunch.ui.fragments.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterManager;
import com.openclassrooms.go4lunch.R;
import com.openclassrooms.go4lunch.databinding.FragmentMapViewBinding;
import com.openclassrooms.go4lunch.di.DI;
import com.openclassrooms.go4lunch.model.Restaurant;
import com.openclassrooms.go4lunch.repositories.PlacesRepository;
import com.openclassrooms.go4lunch.ui.activities.MainActivity;
import com.openclassrooms.go4lunch.ui.dialogs.GPSActivationDialog;
import com.openclassrooms.go4lunch.receivers.GPSBroadcastReceiver;
import com.openclassrooms.go4lunch.utils.RestaurantMarkerItem;
import com.openclassrooms.go4lunch.utils.RestaurantRenderer;
import com.openclassrooms.go4lunch.viewmodels.PlacesViewModel;
import java.util.List;

/**
 * This fragment is used to allow user to interact with a Google Map, search for a restaurant
 * and enable/disable GPS functionality.
 */
public class MapViewFragment extends Fragment implements MapViewFragmentCallback, OnMapReadyCallback {

    public final static String TAG = "TAG_MAP_VIEW_FRAGMENT";
    private FragmentMapViewBinding binding;

    // To catch GPS status changed event
    private GPSBroadcastReceiver gpsBroadcastReceiver;

    // To display a Google map in fragment
    private GoogleMap map;

    // To access Location service methods
    private LocationManager locationManager;
    private ConnectivityManager connectivityManager;

    // Location refresh parameters for GPS provider
    private final static long LOCATION_REFRESH_TIME = 15000;
    private final static float LOCATION_REFRESH_DISTANCE = 10;

    // To handle markers cluster display on map
    private ClusterManager<RestaurantMarkerItem> clusterManager;

    // ViewModel to wrap a LiveData containing the list of Restaurants
    private PlacesViewModel placesViewModel;

    // To store current user position and user position saved in previous session
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPrefLatLon;
    private double currentLatUserPosition;
    private double currentLonUserPosition;

    // To store next_page_token attribute value from Nearby Search API GET request
    private SharedPreferences[] sharedPrefNextPageToken;

    // Listener of user position updates
    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                editor = sharedPrefNextPageToken[0].edit();
                editor.clear();
                editor = sharedPrefNextPageToken[1].edit();
                editor.clear();
                searchPlacesFromCurrentLocation();
            }
        }

        @Override
        public void onProviderEnabled(@NonNull String provider) { /* NOT USED */ }

        @Override
        public void onProviderDisabled(@NonNull String provider) { /* NOT USED */ }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) { /* NOT USED */ }
    };

    public MapViewFragment() { /* Empty constructor */ }

    public static MapViewFragment newInstance() {
        return new MapViewFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        placesViewModel = new ViewModelProvider(requireActivity()).get(PlacesViewModel.class); // Initialize View Model
        placesViewModel.setRepository(new PlacesRepository(DI.provideDatabase(getContext()).restaurantDao(),
                                                           DI.provideDatabase(getContext()).hoursDao(),
                                                           DI.provideDatabase(getContext()).restaurantAndHoursDao(),
                                                           getContext()));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMapViewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        connectivityManager = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        gpsBroadcastReceiver = new GPSBroadcastReceiver(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(this);

        // Update map with Restaurant items markers
        placesViewModel.getListRestaurants().observe(getViewLifecycleOwner(), this::displayMarkersInMap);

        // Initialize SharedPreferences for user position
        String PREF_USER_POSITION = "pref_user_position";
        sharedPrefLatLon = requireContext().getSharedPreferences(PREF_USER_POSITION, Context.MODE_PRIVATE);
    }

    @Override
    public void onResume() {
        super.onResume();
        requireActivity().registerReceiver(gpsBroadcastReceiver, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));
        updateFloatingButtonIconDisplay(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER));
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME, LOCATION_REFRESH_DISTANCE, locationListener);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        requireActivity().unregisterReceiver(gpsBroadcastReceiver);

        // Stop location listener when app goes on background
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationManager.removeUpdates(locationListener);
        }

        // Save position
        editor = sharedPrefLatLon.edit();
        editor.putLong("old_lat_position", Double.doubleToRawLongBits(currentLatUserPosition)).apply();
        editor.putLong("old_lon_position", Double.doubleToRawLongBits(currentLonUserPosition)).apply();
    }

    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    public void getPlacesFromDatabaseOrRetrofitRequest() {
        placesViewModel.getPlacesRepository().getPlacesFromDatabaseOrRetrofitRequest(
                                                ((MainActivity) requireActivity()),
                                                sharedPrefLatLon,
                                        this);
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
                           } else { // GPS deactivated
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
        ((MainActivity) requireActivity()).getClient().getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener((Location location) -> {
                        // Update Camera
                        CameraUpdate cameraUpdate = CameraUpdateFactory
                                .newLatLngZoom(
                                        new LatLng(location.getLatitude(),
                                                location.getLongitude()), 18.0f);
                        if (update) map.animateCamera(cameraUpdate);
                        else map.moveCamera(cameraUpdate);
                          }
                ).addOnFailureListener(Throwable::printStackTrace);
    }


    /**
     * This method is to launch a places search to detect all restaurants around user location in a defined radius.
     */
    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    @Override
    public void searchPlacesFromCurrentLocation() {

        ((MainActivity) requireActivity()).getClient().getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener(location -> {
                    currentLatUserPosition = location.getLatitude();
                    currentLonUserPosition = location.getLongitude();
                    placesViewModel.findPlacesNearby(location.getLatitude() +"," + location.getLongitude(),"restaurant");
                });
    }

    /**
     * This method is used to update FloatingActionButton Icon display according to GPS activation status.
     * @param status : GPS activation status.
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void updateFloatingButtonIconDisplay(boolean status) {
        if (status) {
            binding.fabLocation.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_gps_fixed_24dp_dark_grey));
            binding.fabLocation.setSupportImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.dark_grey)));
        } else {
            binding.fabLocation.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_gps_off_24dp_red));
            binding.fabLocation.setSupportImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
        }
    }

    @Override
    public void activateGPS() { startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)); }

    /**
     * This method is used to update the map by displaying a custom marker for each detected restaurant aroung
     * user location.
     * @param listRestaurants : list of detected restaurants.
     */
    public void displayMarkersInMap(List<Restaurant> listRestaurants) {
        clusterManager.clearItems();
        if (listRestaurants != null) {
            for (int indice = 0; indice < listRestaurants.size(); indice++) {
                RestaurantMarkerItem item = new RestaurantMarkerItem(
                        new LatLng(listRestaurants.get(indice).getLatitude(),
                                   listRestaurants.get(indice).getLongitude()),
                                   listRestaurants.get(indice).getName(), null, false, indice);
                clusterManager.addItem(item);
                clusterManager.cluster();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // Initialize map
            initializeMapOptions(googleMap);
            // Handle floating action button icon update
            handleFloatingActionButtonListener();
            // Initialize map cluster manager
            initializeClusterManager();
            // Initialize current position + search for places
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                centerCursorInCurrentLocation(false);
                if (connectivityManager.getActiveNetworkInfo() != null) {
                    getPlacesFromDatabaseOrRetrofitRequest();
                }
            }
            // Enable click interactions on cluster items window
            handleClusterClickInteractions();
        }
    }

    /**
     * This method is used to handle all user "clicking" interactions with clusters and markers on map.
     */
    private void handleClusterClickInteractions() {
        clusterManager.setOnClusterItemInfoWindowClickListener(item -> {
            ((MainActivity) requireActivity()).addFragmentRestaurantDetails(item.getIndice());
            ((MainActivity) requireActivity()).getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            ((MainActivity) requireActivity()).updateBottomBarStatusVisibility(View.GONE);
            ((MainActivity) requireActivity()).updateToolbarStatusVisibility(View.GONE);
        });
        clusterManager.setOnClusterClickListener(cluster -> {
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                    new LatLng(cluster.getPosition().latitude, cluster.getPosition().longitude)
                    ,15.0f);
            map.animateCamera(cameraUpdate);
            return true;
        });
    }

    /**
     * This method initializes a map configuration.
     * @param googleMap : map instance to initialize
     */
    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    private void initializeMapOptions(GoogleMap googleMap) {
        map = googleMap;
        map.setMyLocationEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.getUiSettings().setCompassEnabled(true);
        map.getUiSettings().setMapToolbarEnabled(false);
    }

    /**
     * This method initializes a cluster manager for markers and clusters display on map.
     */
    @SuppressLint("PotentialBehaviorOverride")
    private void initializeClusterManager() {
        clusterManager = new ClusterManager<>(requireContext(), map);
        // Initialize Renderer for cluster
        RestaurantRenderer restaurantRenderer = new RestaurantRenderer(getActivity(), map, clusterManager);
        restaurantRenderer.setMinClusterSize(10);
        clusterManager.setRenderer(restaurantRenderer);
        // Set listener for marker clicks
        map.setOnCameraIdleListener(clusterManager);
        map.setOnMarkerClickListener(clusterManager);
    }

    /**
     * This methods is used to launch a loadAllRestaurantsWithHours request to database and
     * send the result to view model to perform a data restoration.
     */
    @Override
    public void restoreListFromDatabase() {
        placesViewModel.getPlacesRepository().loadAllRestaurantsWithHours()
                .observe(getViewLifecycleOwner(), restaurantAndHoursData
                        -> placesViewModel.restoreData(restaurantAndHoursData));
    }
}