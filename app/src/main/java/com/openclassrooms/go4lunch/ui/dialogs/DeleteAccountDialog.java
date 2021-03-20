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

        builder.setTitle("Delete account")
                .setMessage("This action will permanently delete your account and all associated data. Confirm ?")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.confirmDeleteUser();
                    }
                })
                .setNegativeButton("Cancel", null);
        return builder.create();
    }
}
