package com.openclassrooms.go4lunch.adapters;

import androidx.annotation.NonNull;

/**
 * Callback interface to {@link ListViewAdapter} activity
 */
public interface ListViewAdapterCallback {
    void updateViewHolderWithPhoto(int position, @NonNull ListViewAdapter.ViewHolderListView holder);
}
