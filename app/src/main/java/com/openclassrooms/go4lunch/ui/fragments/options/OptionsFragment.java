package com.openclassrooms.go4lunch.ui.fragments.options;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.openclassrooms.go4lunch.R;
import com.openclassrooms.go4lunch.alarm.AlarmHandler;
import com.openclassrooms.go4lunch.databinding.FragmentOptionsBinding;
import com.openclassrooms.go4lunch.ui.dialogs.DeleteAccountDialog;
import com.openclassrooms.go4lunch.utils.AppInfo;
import java.util.Calendar;

/**
 * Fragment used to display all options configurable by user.
 */
public class OptionsFragment extends Fragment implements OptionsFragmentCallback {

    public final static String TAG = "TAG_OPTIONS_FRAGMENT";
    private FragmentOptionsBinding binding;

    // SharedPreferences
    private SharedPreferences sharedPrefClusterOption;
    private SharedPreferences sharedPrefAlarmOption;
    private SharedPreferences.Editor editor;

    // Parameter for Alarm option
    private AlarmHandler alarmHandler;
    private Calendar calendarAlarm;
    public OptionsFragment() { /* Required empty public constructor */ }

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
        // Initialization
        initializeSharedPreferences();
        initializeMapOptionDisplay();
        initializeCalendar();
        initializeAlarmOption();

        // Handle views interactions
        handleClickOnDeleteButton();
        handleSwitchInteractions();
        handleCheckBoxButton();
    }

    private void initializeSharedPreferences() {
        sharedPrefClusterOption = requireContext().getSharedPreferences(AppInfo.FILE_OPTIONS, Context.MODE_PRIVATE);
        sharedPrefAlarmOption = requireContext().getSharedPreferences(AppInfo.FILE_OPTIONS, Context.MODE_PRIVATE);
    }

    private void initializeMapOptionDisplay() {
        boolean checked = sharedPrefClusterOption.getBoolean(AppInfo.PREF_CLUSTER_OPTION_KEY, false);
        binding.switchOptionCluster.setChecked(checked);
    }

    private void initializeCalendar() {
        calendarAlarm = Calendar.getInstance();
        calendarAlarm.set(Calendar.HOUR_OF_DAY, 12);
        calendarAlarm.set(Calendar.MINUTE, 0);
        calendarAlarm.set(Calendar.SECOND, 0);
    }

    /**
     * This method initializes alarm hour displayed according to a previous configuration by user. If user
     * has not configured an alarm yet, then a default hour is displayed.
     */
    private void initializeAlarmOption() {
        // Checkbox
        boolean checked = sharedPrefAlarmOption.getBoolean(AppInfo.PREF_ALARM_OPTION_STATUS_KEY, false);
        binding.checkboxAlarm.setChecked(checked);

        // Text Mode
        String textMode;
        if (checked) textMode = requireContext().getResources().getString(R.string.alarm_option_activated_text);
        else  textMode = requireContext().getResources().getString(R.string.alarm_option_deactivated_text);
        binding.textAlarm.setText(textMode);

        // Alarm
        alarmHandler = new AlarmHandler(requireContext());
        if (checked) alarmHandler.startAlarm(calendarAlarm);
    }

    /**
     * This method is used to update the "Alarm" text option according to the "mode" parameter value,
     * and display a Toast to user.
     * @param status : boolean value defining the state of "Alarm" option
     */
    private void updateTextModeAlarmOption(boolean status) {
        String textStatus;
        if (status) textStatus = requireContext().getResources().getString(R.string.alarm_option_activated_text);
        else textStatus = requireContext().getResources().getString(R.string.alarm_option_deactivated_text);
        displayToastAlarm(status);
        binding.textAlarm.setText(textStatus);
    }

    /**
     * This method display a Toast indicating either the alarm mode has been activated or not.
     * @param status : status of the alarm mode
     */
    private void displayToastAlarm(boolean status) {
        if (status) {
            if (Calendar.getInstance().getTimeInMillis() - calendarAlarm.getTimeInMillis() > 0) {
                Toast.makeText(requireContext(), getResources()
                        .getString(R.string.toast_alarm_activated_tomorrow), Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(requireContext(), getResources()
                        .getString(R.string.toast_alarm_activated_today), Toast.LENGTH_SHORT).show();
            }

        }
        else Toast.makeText(requireContext(), getResources()
                  .getString(R.string.toast_alarm_deactivated), Toast.LENGTH_SHORT).show();
    }

    /**
     * This methods updates the SharedPreference "PREF_ALARM_OPTION_STATUS_KEY" value according
     * to the checkBox state value.
     */
    private void handleCheckBoxButton() {
        binding.checkboxAlarm.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor = sharedPrefAlarmOption.edit();
            editor.putBoolean(AppInfo.PREF_ALARM_OPTION_STATUS_KEY, isChecked).apply();
            updateTextModeAlarmOption(isChecked);
            if (isChecked) alarmHandler.startAlarm(calendarAlarm);
            else  alarmHandler.cancelAlarm();
        });
    }

    /**
     * This method is used to display a confirmation Dialog to user.
     */
    private void handleClickOnDeleteButton() {
        DeleteAccountDialog dialog = new DeleteAccountDialog(this);
        binding.textOptionDelete.setOnClickListener(v -> dialog.show(getParentFragmentManager(), DeleteAccountDialog.TAG));
    }

    /**
     * This methods updates the SharedPreference "PREF_CLUSTER_OPTION_KEY" value according
     * to the switch state value.
     */
    private void handleSwitchInteractions() {
        binding.switchOptionCluster.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor = sharedPrefClusterOption.edit();
            editor.putBoolean(AppInfo.PREF_CLUSTER_OPTION_KEY, isChecked);
            editor.apply();
        });
    }


    @Override
    public void confirmDeleteUser() {
        // TODO("Not implemented yet)
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.delete();
            Toast.makeText(requireContext(), "Account deleted", Toast.LENGTH_SHORT).show();
        }
    }
}