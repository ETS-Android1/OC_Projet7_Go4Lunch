package com.openclassrooms.go4lunch.ui.fragments.options;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.openclassrooms.go4lunch.databinding.FragmentOptionsBinding;
import com.openclassrooms.go4lunch.ui.dialogs.DeleteAccountDialog;
import com.openclassrooms.go4lunch.utils.AppInfo;

public class OptionsFragment extends Fragment implements OptionsFragmentCallback {

    public final static String TAG = "TAG_OPTIONS_FRAGMENT";
    private FragmentOptionsBinding binding;

    private SharedPreferences sharedPrefClusterOption;
    private SharedPreferences.Editor editor;

    public OptionsFragment() {
        // Required empty public constructor
    }


    public static OptionsFragment newInstance() {
        return new OptionsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOptionsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        handleClickOnDeleteButton();
        handleSwitchInteractions();

        sharedPrefClusterOption = getContext().getSharedPreferences(AppInfo.FILE_OPTIONS, Context.MODE_PRIVATE);
        editor = sharedPrefClusterOption.edit();
    }

    private void handleClickOnDeleteButton() {
        DeleteAccountDialog dialog = new DeleteAccountDialog(this);
        binding.textOptionDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show(getParentFragmentManager(), DeleteAccountDialog.TAG);
            }
        });
    }

    private void handleSwitchInteractions() {
        binding.switchOptionCluster.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("cluster_option", isChecked);
                editor.apply();
            }
        });
    }

    @Override
    public void confirmDeleteUser() {
        // TODO("Not implemented yet)
    }
}