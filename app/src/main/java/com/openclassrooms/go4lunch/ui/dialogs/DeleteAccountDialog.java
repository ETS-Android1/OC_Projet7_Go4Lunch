package com.openclassrooms.go4lunch.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.openclassrooms.go4lunch.R;
import com.openclassrooms.go4lunch.ui.fragments.options.OptionsFragmentCallback;

/**
 * Confirmation dialog displayed to user when trying to delete its account.
 */
public class DeleteAccountDialog extends DialogFragment {

    public final static String TAG = "TAG_DELETE_ACCOUNT_DIALOG";
    private OptionsFragmentCallback callback;

    public DeleteAccountDialog() { /* Empty constructor */ }

    public DeleteAccountDialog(OptionsFragmentCallback callback) {
        this.callback = callback;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DialogStyle);

        builder.setTitle(getResources().getString(R.string.delete_account_dialog_title))
                .setMessage(getResources().getString(R.string.delete_account_dialog_text))
                .setPositiveButton(getResources().getString(R.string.delete_account_dialog_positive_btn),
                        (dialog, which) -> callback.confirmDeleteUser())
                .setNegativeButton(getResources().getString(R.string.delete_account_dialog_negative_btn), null);
        return builder.create();
    }
}
