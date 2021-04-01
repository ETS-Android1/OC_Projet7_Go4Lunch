package com.openclassrooms.go4lunch.ui.fragments.permission;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.openclassrooms.go4lunch.R;
import com.openclassrooms.go4lunch.databinding.FragmentLocationPermissionBinding;
import com.openclassrooms.go4lunch.ui.activities.MainActivity;
import com.openclassrooms.go4lunch.ui.fragments.map.MapViewFragment;
import com.openclassrooms.go4lunch.utils.AppInfo;

/**
 * Fragment class used to display the UI interface with with the user can authorize the
 * location access permission from device.
 */
public class LocationPermissionFragment extends Fragment {

    public final static String TAG = "TAG_LOCATION_PERMISSION_FRAGMENT";
    private static final int LOCATION_PERMISSION_CODE = 100;
    private SharedPreferences.Editor editor;
    private int nbRequests;
    private FragmentLocationPermissionBinding binding;

    public LocationPermissionFragment() { /* Empty constructor */ }

    public static LocationPermissionFragment newInstance() {
        return new LocationPermissionFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLocationPermissionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeNbPermissionRequests();
        handlePermissionButtonListener();
    }

    /**
     * Checks the number of permission requests which have already been displayed to user.
     */
    private void initializeNbPermissionRequests() {
        SharedPreferences nbPermissionRequestSaved = getContext()
                .getSharedPreferences(AppInfo.FILE_PREF_NB_PERMISSION_REQUESTS, Context.MODE_PRIVATE);
        editor = nbPermissionRequestSaved.edit();
        nbRequests = nbPermissionRequestSaved.getInt(AppInfo.PREF_NB_PERMISSION_REQUESTS, 0);
    }

    /**
     * Handles user interaction with "Enable Permission" button
     */
    private void handlePermissionButtonListener() {
        binding.buttonPermissionLocation.setOnClickListener((View v) -> {
                    // Update number of sent requests
                    nbRequests++;
                    editor.putInt(AppInfo.PREF_NB_PERMISSION_REQUESTS, nbRequests);
                    editor.apply();

                    if (nbRequests <= 1) { // First request permission
                        ActivityCompat.requestPermissions(requireActivity(), new String[] {
                                Manifest.permission.ACCESS_FINE_LOCATION},
                                LOCATION_PERMISSION_CODE);
                    }
                    else { // If user checked Checkbox "Don't ask again"
                        // If "Do not ask again" checkbox has been checked
                        if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                            requireActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container_view, AccessSettingsAppFragment.newInstance(),
                                            AccessSettingsAppFragment.TAG)
                                    .commit();
                        }
                        // Others request permission
                        else {
                            ActivityCompat.requestPermissions(requireActivity(), new String[] {
                                    Manifest.permission.ACCESS_FINE_LOCATION},
                                    LOCATION_PERMISSION_CODE);
                        }
                    }
                }
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) && grantResults[0]
                    == PackageManager.PERMISSION_GRANTED) {
                // Reset number of requests
                editor.putInt(AppInfo.PREF_NB_PERMISSION_REQUESTS, 0);
                editor.apply();
                // Let user access the bottom bar navigation fragments
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container_view, MapViewFragment.newInstance(),
                                 MapViewFragment.TAG)
                        .commit();
                ((MainActivity) requireActivity()).updateNavigationAndBottomBarDisplay(View.VISIBLE);
            }
        }
    }
}