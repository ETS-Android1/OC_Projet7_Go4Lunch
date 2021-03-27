package com.openclassrooms.go4lunch.utils.search;

import android.text.Editable;
import android.text.TextWatcher;
import com.openclassrooms.go4lunch.ui.activities.MainActivityCallback;

/**
 * This class is used to detect any change in EditText field of the autocomplete toolbar, and provide queries.
 */
public class SearchTextWatcher implements TextWatcher {

    private final MainActivityCallback callback;

    public SearchTextWatcher(MainActivityCallback callback) {
        this.callback = callback;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // Not used
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // Not used
    }

    @Override
    public void afterTextChanged(Editable editable) {
        callback.provideSearchQuery(editable.toString());
    }
}
