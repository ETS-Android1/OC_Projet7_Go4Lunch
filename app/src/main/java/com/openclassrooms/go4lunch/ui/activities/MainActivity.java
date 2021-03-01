package com.openclassrooms.go4lunch.ui.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.openclassrooms.go4lunch.BuildConfig;
import com.openclassrooms.go4lunch.R;
import com.openclassrooms.go4lunch.databinding.ActivityMainBinding;
import com.openclassrooms.go4lunch.ui.dialogs.LogoutDialog;
import com.openclassrooms.go4lunch.ui.fragments.restaurants.ListViewFragment;
import com.openclassrooms.go4lunch.ui.fragments.permission.LocationPermissionFragment;
import com.openclassrooms.go4lunch.ui.fragments.map.MapViewFragment;
import com.openclassrooms.go4lunch.ui.fragments.restaurants.RestaurantDetailsFragment;
import com.openclassrooms.go4lunch.ui.fragments.workmates.WorkmatesFragment;
import com.openclassrooms.go4lunch.receivers.NetworkBroadcastReceiver;
import com.openclassrooms.go4lunch.viewmodels.PlacesViewModel;
import java.util.Arrays;
import java.util.List;

/**
 * Main activity of the application.
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, MainActivityCallback {

    private ActivityMainBinding binding;
    private static final int SIGN_OUT = 10;
    private static final int AUTOCOMPLETE_REQUEST_CODE = 102;
    private NetworkBroadcastReceiver networkBroadcastReceiver; // To catch Network status changed event
    private FusedLocationProviderClient client; // To get current user position
    private PlacesViewModel placesViewModel; // View model to store list of restaurants
    private PlacesClient placesClient; // To access Places API methods
    private ListViewFragment listViewFragment;
    private WorkmatesFragment workmatesFragment;
    private MapViewFragment mapViewFragment;
    private FragmentManager fragmentManager;
    private boolean initialize = false;
    private int indice;

    private OnSuccessListener<Void> updateUIAfterRequestCompleted(final int request) {
        return aVoid -> {
            if (request == SIGN_OUT) {
                Snackbar.make(binding.drawerLayout, R.string.snack_bar_logout, Snackbar.LENGTH_SHORT).show();
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initializeToolbar();
        initializeDrawerLayout();
        initializeNavigationView();
        loadUserInfoInNavigationView();
        handleBottomNavigationItemsListeners();
        networkBroadcastReceiver = new NetworkBroadcastReceiver(this);
        client = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        placesViewModel = new ViewModelProvider(this).get(PlacesViewModel.class);
        if (!Places.isInitialized()) Places.initialize(getApplicationContext(), BuildConfig.API_KEY);
        placesClient = Places.createClient(this);
        initializeFragments();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!initialize) { checkIfLocationPermissionIsGranted(); }
        registerReceiver(networkBroadcastReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(networkBroadcastReceiver);
    }

    private void initializeFragments() {
        listViewFragment = ListViewFragment.newInstance();
        workmatesFragment = WorkmatesFragment.newInstance();
        mapViewFragment = MapViewFragment.newInstance();
        fragmentManager = getSupportFragmentManager();
    }

    private void initializeToolbar() {
        setSupportActionBar(binding.toolbar);
        if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) binding.toolbar.getLayoutParams();
            params.setMargins(0, getStatusBarSize(), 0, 0);
            binding.toolbar.setLayoutParams(params);
        }
    }

    private void initializeDrawerLayout() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void initializeNavigationView() { binding.navigationView.setNavigationItemSelectedListener(this); }

    /**
     * This methods updates the Navigation View header with user information
     */
    private void loadUserInfoInNavigationView() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        View header = binding.navigationView.getHeaderView(0);

        TextView userName = header.findViewById(R.id.user_name);
        TextView userEmail = header.findViewById(R.id.user_email);
        ImageView userAvatar = header.findViewById(R.id.user_avatar);
        try {
            userName.setText(user.getDisplayName());
            userEmail.setText(user.getEmail());

            Glide.with(this).load(user.getPhotoUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(userAvatar);
        } catch (NullPointerException exception) { exception.printStackTrace(); }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.search) {
            onSearchAutocompleteCalled();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.your_lunch_option:
                Log.d("NAVIGATION", "Click R.id.your_lunch_option");
                break;
            case R.id.settings_options:
                Log.d("NAVIGATION", "Click R.id.settings_options");
                break;
            case R.id.logout_options:
                LogoutDialog dialog = new LogoutDialog(this);
                dialog.show(fragmentManager, LogoutDialog.TAG);
                break;
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    private void handleBottomNavigationItemsListeners() {
        binding.bottomNavigationBar.setOnNavigationItemSelectedListener((@NonNull MenuItem item) -> {
                    item.setChecked(true);
                    switch (item.getItemId()) {
                        case R.id.map: // Map View Fragment
                            if (fragmentManager.findFragmentByTag(ListViewFragment.TAG) != null)
                                if (listViewFragment.isVisible())
                                    hideFragment(listViewFragment);

                            if (fragmentManager.findFragmentByTag(WorkmatesFragment.TAG) != null)
                                if (workmatesFragment.isVisible())
                                    hideFragment(workmatesFragment);

                            break;
                        case R.id.list: // List View Fragment
                            if (fragmentManager.findFragmentByTag(WorkmatesFragment.TAG) != null)
                                if (workmatesFragment.isVisible())
                                    hideFragment(workmatesFragment);

                            if (fragmentManager.findFragmentByTag(ListViewFragment.TAG) == null)
                                addFragment(listViewFragment, ListViewFragment.TAG);
                            else showFragment(listViewFragment);
                            break;

                        case R.id.workmates: // Workmates Fragment
                            if (fragmentManager.findFragmentByTag(ListViewFragment.TAG) != null)
                                if (workmatesFragment.isVisible())
                                    hideFragment(listViewFragment);

                            if (fragmentManager.findFragmentByTag(WorkmatesFragment.TAG) == null)
                                addFragment(workmatesFragment, WorkmatesFragment.TAG);
                            else showFragment(workmatesFragment);
                            break;
                    }
                    return false;
                }
        );
    }

    private void hideFragment(Fragment fragment) { fragmentManager.beginTransaction().hide(fragment).commit(); }

    private void showFragment(Fragment fragment) { fragmentManager.beginTransaction().show(fragment).commit(); }

    public void addFragment(Fragment fragment, String TAG) {
        fragmentManager.beginTransaction().add(R.id.fragment_container_view, fragment, TAG).commit();
    }

    public void addFragmentRestaurantDetails(int indiceRestaurant) {
        indice = indiceRestaurant;
        fragmentManager.beginTransaction().add(R.id.fragment_restaurant_details_container_view,
                new RestaurantDetailsFragment(),
                RestaurantDetailsFragment.TAG).commit();
    }

    public void removeFragment(Fragment fragment) { fragmentManager.beginTransaction().remove(fragment).commit(); }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START))
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        else {
            Fragment fragment = fragmentManager.findFragmentByTag(RestaurantDetailsFragment.TAG);
            if (fragment!= null) {
                if (fragment.isVisible()) {
                    removeFragment(fragment);
                    binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                    updateToolbarStatusVisibility(View.VISIBLE);
                    updateBottomBarStatusVisibility(View.VISIBLE);
                }
            }
            else finishAffinity();
        }
    }

    /**
     * MainActivityCallback interface implementation :
     * This method is called when user wants to logout by clicking on R.id.logout_options item in
     * Navigation View menu
     */
    @Override
    public void logoutUser() {
        AuthUI.getInstance().delete(this)
                .addOnFailureListener(this, exception -> finish())
                .addOnSuccessListener(this, updateUIAfterRequestCompleted(SIGN_OUT));
    }

    /**
     * This method is used to update the "Network status" bar display. It first checks if the permission fragments
     * (AccessSettingsAppFragment and LocationPermissionFragment) are no longer displayed.
     * @param status : status of the network
     */
    @Override
    public void updateNetworkInfoBarDisplay(boolean status) {
        Fragment fragment = fragmentManager.findFragmentByTag(MapViewFragment.TAG);
        if ((fragment instanceof MapViewFragment)) {
            if (status) { // Wifi network of Mobile Data network activated
                ViewPropertyAnimator fadeOutAnim = binding.barConnectivityInfo.animate().alpha(0.0f).setDuration(200);
                fadeOutAnim.withEndAction(() -> binding.barConnectivityInfo.setVisibility(View.GONE));
            } else { // No network activated
                binding.barConnectivityInfo.setVisibility(View.VISIBLE);
                ViewCompat.setElevation(binding.barConnectivityInfo, 10);
                ViewPropertyAnimator fadeInAnim = binding.barConnectivityInfo.animate().alpha(1.0f).setDuration(200);
                fadeInAnim.start();
            }
        }
    }

    public void updateBottomBarStatusVisibility(int visibility) { binding.bottomNavigationBar.setVisibility(visibility); }

    public void updateToolbarStatusVisibility(int visibility) {
        binding.toolbar.setVisibility(visibility); }

    /**
     * This method is used to check if location permission is granted.
     * If yes : MapViewFragment fragment is displayed and all UIs (toolbar, bottom bar)
     * If not : The LocationPermissionFragment fragment is displayed to allow user to
     * authorize location permission.
     */
    public void checkIfLocationPermissionIsGranted() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //updateToolbarBottomBarAndNetworkBarVisibility(View.GONE);
            updateToolbarStatusVisibility(View.GONE);
            updateBottomBarStatusVisibility(View.GONE);
            binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, LocationPermissionFragment.newInstance(), LocationPermissionFragment.TAG)
                    .commit();
        } else {
            //updateToolbarBottomBarAndNetworkBarVisibility(View.VISIBLE);
            binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            updateToolbarStatusVisibility(View.VISIBLE);
            updateBottomBarStatusVisibility(View.VISIBLE);
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, mapViewFragment, MapViewFragment.TAG)
                    .commit();
            initialize = true;
        }
    }

    /**
     * This method is used to configure and display the Autocomplete search bar.
     */
    public void onSearchAutocompleteCalled() {
        // Set the fields to specify which types of place data to return
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS);
        // Start the autocomplete intent
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.OVERLAY, fields).setCountry("FR")
                .setTypeFilter(TypeFilter.ESTABLISHMENT)
                .build(this);

        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i("AUTOCOMPLETERESULT", place.getName());
            }
            else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("AUTOCOMPLETERESULT", "Error : " + status.getStatusMessage());
            }
            else if (resultCode == RESULT_CANCELED) {
                Log.i("AUTOCOMPLETERESULT", "Canceled");
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * This method is used by main activity to enable its child fragment MapViewFragment to
     * search places around current user location.
     */
    @Override
    public void searchPlacesInMapViewFragment() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mapViewFragment.searchPlacesFromCurrentLocation();
        }
    }

    // Getter methods
    public FusedLocationProviderClient getClient() { return this.client; }

    public PlacesClient getPlacesClient() { return placesClient; }

    public int getIndice() { return indice; }

    public int getStatusBarSize() {
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        int statusBarSize = 0;
        statusBarSize = getResources().getDimensionPixelSize(resourceId);
        return statusBarSize;
    }

    public DrawerLayout getDrawerLayout() {
        return binding.drawerLayout;
    }
}