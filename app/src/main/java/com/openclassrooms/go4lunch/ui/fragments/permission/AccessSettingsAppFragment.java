package com.openclassrooms.go4lunch.ui.fragments.permission;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.openclassrooms.go4lunch.databinding.FragmentAccessSettingsAppBinding;

/**
 * Fragment class used to display the UI interface with which the user can access the device
 * settings, and let the user authorize the Location permission access in the Go4Lunch section.
 */
public class AccessSettingsAppFragment extends Fragment {

    public final static String TAG = "TAG_ACCESS_SETTINGS_APP_FRAGMENT";
    private FragmentAccessSettingsAppBinding binding;

    public AccessSettingsAppFragment() { /* Empty constructor */ }

    public static AccessSettingsAppFragment newInstance() {
        return new AccessSettingsAppFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAccessSettingsAppBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        handleSettingsAccessButtonListener();
    }

    /**
     * Accesses the settings activity for Go4Lunch application.
     */
    private void handleSettingsAccessButtonListener() {
        binding.buttonPermissionLocation.setOnClickListener((View v) -> {
                    Intent intent =
                          new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.parse("package:com.openclassrooms.go4lunch"));
                    startActivity(intent);
                }
        );
    }
}