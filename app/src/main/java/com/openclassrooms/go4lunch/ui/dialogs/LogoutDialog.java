package com.openclassrooms.go4lunch.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.openclassrooms.go4lunch.R;
import com.openclassrooms.go4lunch.ui.activities.MainActivityCallback;

/**
 * Dialog with which the user can interact to logout from his Go4Lunch account,
 * by using a {@link MainActivityCallback} interface.
 */
public class LogoutDialog extends DialogFragment {

    public final static String TAG = "TAG_LOGOUT_DIALOG";
    private MainActivityCallback listener;

    public LogoutDialog() {/* Empty constructor */}

    public LogoutDialog(MainActivityCallback listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DialogStyle);

        builder.setTitle(R.string.title_logout_dialog)
                .setMessage(R.string.message_logout_dialog)
                .setPositiveButton(R.string.positive_btn_logout_dialog, (DialogInterface dialog, int which) -> {
                        listener.logoutUser(); // Logout action
                    }
                )
                .setNegativeButton(R.string.negative_btn_logout_dialog, null);
        return builder.create();
    }
}
