package com.openclassrooms.go4lunch.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationBuilderWithBuilderAccessor;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.openclassrooms.go4lunch.BuildConfig;
import com.openclassrooms.go4lunch.R;
import com.openclassrooms.go4lunch.database.Go4LunchDatabase;
import com.openclassrooms.go4lunch.databinding.ActivityMainBinding;
import com.openclassrooms.go4lunch.di.DI;
import com.openclassrooms.go4lunch.model.Restaurant;
import com.openclassrooms.go4lunch.notifications.NotificationHandler;
import com.openclassrooms.go4lunch.repositories.PlacesRepository;
import com.openclassrooms.go4lunch.repositories.WorkmatesRepository;
import com.openclassrooms.go4lunch.ui.dialogs.LogoutDialog;
import com.openclassrooms.go4lunch.ui.fragments.options.OptionsFragment;
import com.openclassrooms.go4lunch.ui.fragments.restaurants.ListViewFragment;
import com.openclassrooms.go4lunch.ui.fragments.permission.LocationPermissionFragment;
import com.openclassrooms.go4lunch.ui.fragments.map.MapViewFragment;
import com.openclassrooms.go4lunch.ui.fragments.restaurants.RestaurantDetailsFragment;
import com.openclassrooms.go4lunch.ui.fragments.workmates.WorkmatesFragment;
import com.openclassrooms.go4lunch.receivers.NetworkBroadcastReceiver;
import com.openclassrooms.go4lunch.utils.AppInfo;
import com.openclassrooms.go4lunch.utils.search.SearchTextWatcher;
import com.openclassrooms.go4lunch.viewmodels.PlacesViewModel;
import com.openclassrooms.go4lunch.viewmodels.WorkmatesViewModel;

/**
 * Main activity of the application.
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, MainActivityCallback {

    private ActivityMainBinding binding;
    private boolean initialize = false;

    // For Sign out firebase operation
    private static final int SIGN_OUT = 10;

    // Receiver
    private NetworkBroadcastReceiver networkBroadcastReceiver; // To catch Network status changed event

    // Fragments
    private ListViewFragment listViewFragment;
    private WorkmatesFragment workmatesFragment;
    private MapViewFragment mapViewFragment;
    private OptionsFragment optionsFragment;
    private FragmentManager fragmentManager;

    // Indice of the corresponding Restaurant object in the list
    private Restaurant restaurantToDisplay;

    // Place API client
    private PlacesClient placesClient;

    // Location client
    private FusedLocationProviderClient locationClient; // To get current user position

    // OnSuccess listener for logout operation
    private OnSuccessListener<Void> updateUIAfterRequestCompleted(final int request) {
        return aVoid -> {
            if (request == SIGN_OUT) {
                Snackbar.make(binding.drawerLayout, R.string.snack_bar_logout, Snackbar.LENGTH_SHORT).show();
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        };
    }

    // ViewModels
    private PlacesViewModel placesViewModel;
    private WorkmatesViewModel workmatesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Initialize views
        initializeToolbar();
        initializeDrawerLayout();
        initializeNavigationView();
        initializeSearchEditTextListener();
        loadUserInfoInNavigationView();
        handleBottomNavigationItemsListeners();
        // Broadcast Receiver initialization
        networkBroadcastReceiver = new NetworkBroadcastReceiver(this);
        // To access Places API methods
        if (!Places.isInitialized()) Places.initialize(getApplicationContext(), BuildConfig.API_KEY);
        placesClient = Places.createClient(this);
        // To access user location
        locationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        // Initialize child fragments
        initializeFragments();
        // Initialize view models
        initializeViewModels();

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

    private void initializeViewModels() {
        // Places
        placesViewModel = new ViewModelProvider(this).get(PlacesViewModel.class);
        placesViewModel.setRepository(new PlacesRepository(DI.provideDatabase(this).restaurantDao(),
                DI.provideDatabase(this).hoursDao(),
                DI.provideDatabase(this).restaurantAndHoursDao(),
                this,
                placesClient,
                locationClient,
                this));

        // Workmates
        workmatesViewModel = new ViewModelProvider(this).get(WorkmatesViewModel.class);
        workmatesViewModel.setWorkmatesRepository(new WorkmatesRepository());
        addListenerToDatabaseCollection();
    }

    /**
     * This method is used to attach a listener to the database collection and updates the MutableLiveData
     * listWorkmates every time an update in database is detected.
     */
    private void addListenerToDatabaseCollection() {
        FirebaseFirestore dbFirestore = FirebaseFirestore.getInstance();
        CollectionReference collectionRef = dbFirestore.collection("list_employees");
        collectionRef.addSnapshotListener((value, error) ->
                // Update MutableLiveData
                workmatesViewModel.getEmployeesInfoFromFirestoreDatabase());
    }

    private void initializeFragments() {
        listViewFragment = ListViewFragment.newInstance();
        workmatesFragment = WorkmatesFragment.newInstance();
        mapViewFragment = MapViewFragment.newInstance();
        optionsFragment = OptionsFragment.newInstance();
        fragmentManager = getSupportFragmentManager();
    }

    private void initializeToolbar() {
        setSupportActionBar(binding.toolbar);
        if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) binding.toolbar.getLayoutParams();
            params.setMargins(0, AppInfo.getStatusBarSize(this), 0, 0);
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

    private void initializeSearchEditTextListener() {
        SearchTextWatcher searchTextWatcher = new SearchTextWatcher(this);
        binding.textInputEditAutocomplete.addTextChangedListener(searchTextWatcher);
    }

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
        if (item.getItemId() == R.id.search) onSearchAutocompleteCalled();
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.your_lunch_option:
                onClickYourLunchOptionIcon();
                break;
            case R.id.settings_options:
                onClickOptionsIcon();
                break;
            case R.id.logout_options:
                LogoutDialog dialog = new LogoutDialog(this);
                dialog.show(fragmentManager, LogoutDialog.TAG);
                break;
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * This method handles click interaction of "Your lunch" icon, by displaying the RestaurantDetailsFragment
     * with user Restaurant selection, otherwise a Toast is displayed.
     */
    private void onClickYourLunchOptionIcon() {
        SharedPreferences sharedPrefSelection = getSharedPreferences(AppInfo.FILE_PREF_SELECTED_RESTAURANT, Context.MODE_PRIVATE);
        String savedRestaurantJSON = sharedPrefSelection.getString(AppInfo.PREF_SELECTED_RESTAURANT_KEY, "");
        if (!savedRestaurantJSON.equals("")) {
            Gson gson = new Gson();
            setRestaurantToDisplay(gson.fromJson(savedRestaurantJSON, Restaurant.class));
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container_view, RestaurantDetailsFragment.newInstance(), RestaurantDetailsFragment.TAG).commit();
            getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            updateBottomBarStatusVisibility(View.GONE);
            updateToolbarStatusVisibility(View.GONE);
        }
        else Toast.makeText(this, getResources().getString(R.string.toast_your_lunch), Toast.LENGTH_SHORT).show();
    }

    private void onClickOptionsIcon() {
        fragmentManager.beginTransaction()
                .add(R.id.fragment_container_view, optionsFragment, OptionsFragment.TAG).commit();
        updateBottomBarStatusVisibility(View.GONE);
    }

    /**
     * This method handles interactions with Bottom navigation bar, and displays
     * fragment according to the selected item.
     */
    @SuppressLint("NonConstantResourceId")
    private void handleBottomNavigationItemsListeners() {
        binding.bottomNavigationBar.setOnNavigationItemSelectedListener((@NonNull MenuItem item) -> {
                    item.setChecked(true);
                    switch (item.getItemId()) {
                        case R.id.map: // Map View Fragment
                            if (fragmentManager.findFragmentByTag(ListViewFragment.TAG) != null)
                                if (listViewFragment.isVisible())
                                    fragmentManager.beginTransaction().hide(listViewFragment).commit();

                            if (fragmentManager.findFragmentByTag(WorkmatesFragment.TAG) != null)
                                if (workmatesFragment.isVisible())
                                    fragmentManager.beginTransaction().hide(workmatesFragment).commit();
                            break;
                        case R.id.list: // List View Fragment
                            if (fragmentManager.findFragmentByTag(WorkmatesFragment.TAG) != null)
                                if (workmatesFragment.isVisible())
                                    fragmentManager.beginTransaction().hide(workmatesFragment).commit();

                            if (fragmentManager.findFragmentByTag(ListViewFragment.TAG) == null)
                                fragmentManager.beginTransaction()
                                        .add(R.id.fragment_container_view, listViewFragment, ListViewFragment.TAG).commit();
                            else fragmentManager.beginTransaction().show(listViewFragment).commit();
                            break;

                        case R.id.workmates: // Workmates Fragment
                            if (fragmentManager.findFragmentByTag(ListViewFragment.TAG) != null)
                                if (listViewFragment.isVisible())
                                    fragmentManager.beginTransaction().hide(listViewFragment).commit();

                            if (fragmentManager.findFragmentByTag(WorkmatesFragment.TAG) == null)
                                fragmentManager.beginTransaction()
                                        .add(R.id.fragment_container_view, workmatesFragment, WorkmatesFragment.TAG).commit();
                            else fragmentManager.beginTransaction().show(workmatesFragment).commit();
                            break;
                    }
                    return false;
                }
        );
    }

    public void displayRestaurantDetailsFragment() {
        fragmentManager.beginTransaction().add(R.id.fragment_container_view,
                RestaurantDetailsFragment.newInstance(), RestaurantDetailsFragment.TAG).commit();
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        updateBottomBarStatusVisibility(View.GONE);
        updateToolbarStatusVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START))
            // Close DrawerLayout if displayed
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        else if (binding.textInputLayoutAutocomplete.getVisibility() == View.VISIBLE){
            // Hide search field if displayed
            binding.textInputLayoutAutocomplete.setVisibility(View.GONE);
        }
        else {
            // Close RestaurantDetailsFragment if displayed
            Fragment fragment = fragmentManager.findFragmentByTag(RestaurantDetailsFragment.TAG);
            if (fragmentManager.findFragmentByTag(RestaurantDetailsFragment.TAG)!= null) {
                if (fragment.isVisible()) {
                    ((RestaurantDetailsFragment) fragment).updateFirestoreWithLikeStatus();
                    fragmentManager.beginTransaction().remove(fragment).commit();
                    binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                    updateToolbarStatusVisibility(View.VISIBLE);
                    updateBottomBarStatusVisibility(View.VISIBLE);
                }
            }
            else if (optionsFragment.isVisible()) {
                fragmentManager.beginTransaction().remove(optionsFragment).commit();
                binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                updateToolbarStatusVisibility(View.VISIBLE);
                updateBottomBarStatusVisibility(View.VISIBLE);
                mapViewFragment.updateRestaurantRenderer(); // Apply options updates
            }
            else {
                // Close app
                Go4LunchDatabase.getInstance(getApplicationContext()).close();
                finishAffinity();
            }
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
     * MainActivityCallback interface implementation :
     * @param query : query to use to perform an Autocomplete request
     */
    @Override
    public void provideSearchQuery(String query) {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Log.i("PERFORMAUTOCOMPLETE", "MainActivity provideSearchQuery : " + query);
            placesViewModel.performAutocompleteRequest(query, getApplicationContext());
        }
        else Toast.makeText(getApplicationContext(), "GPS not enabled", Toast.LENGTH_SHORT).show();
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

    public void updateToolbarStatusVisibility(int visibility) { binding.toolbar.setVisibility(visibility); }

    /**
     * This method is used to check if location permission is granted.
     * If yes : MapViewFragment fragment is displayed and all UIs (toolbar, bottom bar)
     * If not : The LocationPermissionFragment fragment is displayed to allow user to
     * authorize location permission.
     */
    public void checkIfLocationPermissionIsGranted() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            updateToolbarStatusVisibility(View.GONE);
            updateBottomBarStatusVisibility(View.GONE);
            binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, LocationPermissionFragment.newInstance(), LocationPermissionFragment.TAG)
                    .commit();
        } else {
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
        binding.textInputLayoutAutocomplete.setVisibility(View.VISIBLE);
    }

    /**
     * This method is used by main activity to enable its child fragment MapViewFragment to
     * search places around current user location.
     */
    @Override
    public void getPlacesFroMDatabaseOrRetrofitInMapViewFragment() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mapViewFragment.getPlacesFromDatabaseOrRetrofitRequest();
        }
    }

    // Getter methods
    public FusedLocationProviderClient getLocationClient() { return this.locationClient; }

    public Restaurant getRestaurantToDisplay() { return restaurantToDisplay; }

    public DrawerLayout getDrawerLayout() { return binding.drawerLayout; }

    public PlacesViewModel getPlacesViewModel() { return this.placesViewModel; }

    public WorkmatesViewModel getWorkmatesViewModel() { return this.workmatesViewModel; }

    // Setter methods
    public void setRestaurantToDisplay(Restaurant restaurantToDisplay) { this.restaurantToDisplay = restaurantToDisplay; }
}