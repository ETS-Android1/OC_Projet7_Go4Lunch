package com.openclassrooms.go4lunch.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.openclassrooms.go4lunch.R;
import com.openclassrooms.go4lunch.databinding.ActivityMainBinding;
import com.openclassrooms.go4lunch.ui.dialogs.LogoutDialog;
import com.openclassrooms.go4lunch.ui.fragments.ListViewFragment;
import com.openclassrooms.go4lunch.ui.fragments.LocationPermissionFragment;
import com.openclassrooms.go4lunch.ui.fragments.MapViewFragment;
import com.openclassrooms.go4lunch.ui.fragments.WorkmatesFragment;
import com.openclassrooms.go4lunch.ui.receivers.NetworkBroadcastReceiver;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, MainActivityCallback {

    private ActivityMainBinding binding;
    private FirebaseUser user;

    private static final int SIGN_OUT = 10;

    private NetworkBroadcastReceiver networkBroadcastReceiver;

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkIfLocationPermissionIsGranted();
        registerReceiver(networkBroadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(networkBroadcastReceiver);
    }

    private void initializeToolbar() { setSupportActionBar(binding.toolbar); }

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
        user = FirebaseAuth.getInstance().getCurrentUser();
        View header = binding.navigationView.getHeaderView(0);

        TextView userName = header.findViewById(R.id.user_name);
        userName.setText(user.getDisplayName());

        TextView userEmail = header.findViewById(R.id.user_email);
        userEmail.setText(user.getEmail());

        ImageView userAvatar = header.findViewById(R.id.user_avatar);
        Glide.with(this).load(user.getPhotoUrl())
                .apply(RequestOptions.circleCropTransform())
                .into(userAvatar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.search) {
            Log.d("CLICK_MENU", "Search");
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.your_lunch_option :
                Log.d("NAVIGATION", "Click R.id.your_lunch_option");
                break;
            case R.id.settings_options :
                Log.d("NAVIGATION", "Click R.id.settings_options");
                break;
            case R.id.logout_options :
                LogoutDialog dialog = new LogoutDialog(this);
                dialog.show(getSupportFragmentManager(), LogoutDialog.TAG);
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
                    case R.id.map : // Map View Fragment
                        if (getSupportFragmentManager().findFragmentByTag(ListViewFragment.TAG) != null) {
                            if (Objects.requireNonNull(getSupportFragmentManager().findFragmentByTag(ListViewFragment.TAG)).isVisible()) {
                                removeFragment(ListViewFragment.TAG);
                            }
                        }
                        if (getSupportFragmentManager().findFragmentByTag(WorkmatesFragment.TAG) != null) {
                            if (Objects.requireNonNull(getSupportFragmentManager().findFragmentByTag(WorkmatesFragment.TAG)).isVisible()) {
                                removeFragment(WorkmatesFragment.TAG);
                            }
                        }
                        break;
                    case R.id.list : // List View Fragment
                        if (getSupportFragmentManager().findFragmentByTag(WorkmatesFragment.TAG) != null) {
                            if (Objects.requireNonNull(getSupportFragmentManager().findFragmentByTag(WorkmatesFragment.TAG)).isVisible()) {
                                removeFragment(WorkmatesFragment.TAG);
                            }
                        }
                        if (getSupportFragmentManager().findFragmentByTag(ListViewFragment.TAG) == null) {
                            addFragment(ListViewFragment.TAG, ListViewFragment.newInstance());
                        }
                        break;
                    case R.id.workmates : // Workmates Fragment
                        if (getSupportFragmentManager().findFragmentByTag(ListViewFragment.TAG) != null) {
                            if (Objects.requireNonNull(getSupportFragmentManager().findFragmentByTag(ListViewFragment.TAG)).isVisible()) {
                                removeFragment(ListViewFragment.TAG);
                            }
                        }
                        if (getSupportFragmentManager().findFragmentByTag(WorkmatesFragment.TAG) == null) {
                            addFragment(WorkmatesFragment.TAG, WorkmatesFragment.newInstance());
                        }
                        break;
                }
                return false;
            }
        );
    }

    public void removeFragment(String TAG) {
        getSupportFragmentManager().beginTransaction()
                .remove(Objects.requireNonNull(getSupportFragmentManager().findFragmentByTag(TAG)))
                .commit();
    }

    public void addFragment(String TAG, Fragment instance) {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container_view, instance, TAG)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) binding.drawerLayout.closeDrawer(GravityCompat.START);
        else finishAffinity();
    }

    private OnSuccessListener<Void> updateUIAfterRequestCompleted(final int request) {
        return aVoid -> {
            if (request == SIGN_OUT) {
                Snackbar.make(binding.drawerLayout, R.string.snack_bar_logout, Snackbar.LENGTH_SHORT).show();
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }

        };
    }

    /**
     * MainActivityCallback interface implementation :
     * This method is called when user wants to logout by clicking on R.id.logout_options item in
     * Navigation View menu
     */
    @Override
    public void logoutUser() {
        AuthUI.getInstance().delete(this)
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) { finish(); }
                })
                .addOnSuccessListener(this, updateUIAfterRequestCompleted(SIGN_OUT));
    }

    /**
     * This method is used to update the "Network status" bar display.
     * @param status : status of the network
     */
    @Override
    public void updateNetworkInfoBarDisplay(boolean status) {
        if (checkIfBottomBarFragmentsAreDisplayed()) {
            if (status) { // Wifi network of Mobile Data network activated
                ViewPropertyAnimator fadeOutAnim = binding.barConnectivityInfo.animate().alpha(0.0f).setDuration(200);
                fadeOutAnim.withEndAction(() -> binding.barConnectivityInfo.setVisibility(View.GONE));
            }
            else { // No network activated
                binding.barConnectivityInfo.setVisibility(View.VISIBLE);
                ViewPropertyAnimator fadeInAnim = binding.barConnectivityInfo.animate().alpha(1.0f).setDuration(200);
                fadeInAnim.start();
            }
        }
    }

    /**
     * This methods updates all MainActivity UI visibility according to the permission status
     * @param visibility : Visibility value to apply to UIs.
     */
    public void updateToolbarBottomBarAndNetworkBarVisibility(int visibility) {
        binding.barConnectivityInfo.setVisibility(visibility);
        binding.toolbar.setVisibility(visibility);
        binding.bottomNavigationBar.setVisibility(visibility);
    }

    /**
     * This method is used to check if location permission is granted.
     * If yes : MapViewFragment fragment is displayed and all UIs (toolbar, bottom bar)
     * If not : The LocationPermissionFragment fragment is displayed to allow user to
     * authorize location permission.
     */
    public void checkIfLocationPermissionIsGranted() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            updateToolbarBottomBarAndNetworkBarVisibility(View.GONE);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_view, LocationPermissionFragment.newInstance(), LocationPermissionFragment.TAG)
                    .commit();
        }
        else {
            updateToolbarBottomBarAndNetworkBarVisibility(View.VISIBLE);
            if (!checkIfBottomBarFragmentsAreDisplayed()) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container_view, MapViewFragment.newInstance(), MapViewFragment.TAG)
                        .commit();
            }
        }
    }

    public boolean checkIfBottomBarFragmentsAreDisplayed() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(MapViewFragment.TAG);
        return (fragment instanceof MapViewFragment);
    }
}