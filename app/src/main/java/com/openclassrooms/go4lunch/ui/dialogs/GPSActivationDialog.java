package com.openclassrooms.go4lunch.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.openclassrooms.go4lunch.R;
import com.openclassrooms.go4lunch.ui.fragments.map.MapViewFragmentCallback;

/**
 * Dialog with which the user can interact to activate GPS device,
 * by using a {@link MapViewFragmentCallback} interface.
 */
public class GPSActivationDialog extends DialogFragment {

    public final static String TAG = "TAG_GPS_ACTIVATION_DIALOG";
    private MapViewFragmentCallback callback;

    public GPSActivationDialog() { /* Empty constructor */ }

    public GPSActivationDialog(MapViewFragmentCallback callback) {
        this.callback = callback;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DialogStyle);

        builder.setTitle(R.string.title_gps_activation_dialog)
                .setMessage(R.string.message_gps_activation_dialog)
                .setPositiveButton(R.string.positive_gps_activation_dialog, (DialogInterface dialog, int which) -> {
                        callback.activateGPS(); // Activate GPS only
                    }
                )
                .setNegativeButton(R.string.negative_gps_activation_dialog, null);
        return builder.create();
    }
}
