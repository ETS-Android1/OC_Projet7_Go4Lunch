package com.openclassrooms.go4lunch.utils.search;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import com.openclassrooms.go4lunch.ui.activities.MainActivityCallback;

public class SearchTextWatcher implements TextWatcher {

    private MainActivityCallback callback;

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
        Log.i("PERFORMAUTOCOMPLETE", "SearchTextWatcher : " + editable.toString());
        callback.provideSearchQuery(editable.toString());
    }
}
