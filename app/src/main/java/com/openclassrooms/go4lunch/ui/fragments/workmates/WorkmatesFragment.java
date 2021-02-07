package com.openclassrooms.go4lunch.ui.fragments.workmates;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.openclassrooms.go4lunch.R;

public class WorkmatesFragment extends Fragment {

    public final static String TAG = "TAG_WORKMATES_FRAGMENT";

    public WorkmatesFragment() { /* Empty constructor */ }

    public static WorkmatesFragment newInstance() {
        return new WorkmatesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_workmates, container, false);
    }
}