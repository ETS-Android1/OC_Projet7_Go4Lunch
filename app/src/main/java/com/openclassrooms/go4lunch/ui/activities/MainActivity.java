package com.openclassrooms.go4lunch.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.openclassrooms.go4lunch.R;
import com.openclassrooms.go4lunch.databinding.ActivityMainBinding;
import com.openclassrooms.go4lunch.ui.dialogs.LogoutDialog;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, MainActivityCallback{

    private ActivityMainBinding binding;
    private FirebaseUser user;
    private static final int SIGN_OUT = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initializeToolbar();
        initializeDrawerLayout();
        initializeNavigationView();
        loadUserInfoInNavigationView();
    }

    private void initializeToolbar() {
        setSupportActionBar(binding.toolbar);
    }

    private void initializeDrawerLayout() {
        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toogle);
        toogle.syncState();
    }

    private void initializeNavigationView() {
        binding.navigationView.setNavigationItemSelectedListener(this);
    }

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

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START))
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        else super.onBackPressed();
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
     * MainActivityCallback interface implementation
     */
    @Override
    public void logoutUser() {
        AuthUI.getInstance().delete(this)
                .addOnSuccessListener(this, updateUIAfterRequestCompleted(SIGN_OUT));
    }
}