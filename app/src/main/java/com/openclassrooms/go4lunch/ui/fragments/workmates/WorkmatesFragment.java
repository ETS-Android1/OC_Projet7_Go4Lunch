package com.openclassrooms.go4lunch.ui.fragments.workmates;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.openclassrooms.go4lunch.adapters.WorkmatesAdapter;
import com.openclassrooms.go4lunch.databinding.FragmentWorkmatesBinding;
import com.openclassrooms.go4lunch.ui.activities.MainActivity;
import com.openclassrooms.go4lunch.viewmodels.WorkmatesViewModel;

/**
 * Fragment class used to display the list of workmates in a RecyclerView, using a
 * @{@link WorkmatesAdapter} adapter
 */
public class WorkmatesFragment extends Fragment {

    public final static String TAG = "TAG_WORKMATES_FRAGMENT";
    private FragmentWorkmatesBinding binding;

    // View Model containing a MutableLiveData
    private WorkmatesViewModel workmatesViewModel;

    // Adapter to display the list in a RecyclerView
    private WorkmatesAdapter adapter;

    public WorkmatesFragment() { /* Empty constructor */ }

    public static WorkmatesFragment newInstance() {
        return new WorkmatesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize ViewModel
        workmatesViewModel = ((MainActivity) requireActivity()).getWorkmatesViewModel();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentWorkmatesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeRecyclerView();
        addObserverToViewModel();
        workmatesViewModel.getEmployeesInfoFromFirestoreDatabase();
    }

    /**
     * Attaches an observer to the MutableLiveData listWorkmates and refresh the
     * adapter list when an update is detected.
     */
    private void addObserverToViewModel() {
        workmatesViewModel.getListWorkmates().observe(getViewLifecycleOwner(), list -> {
            // Send to adapter
               adapter.updateList(list);
        });
    }

    /**
     * Initializes a RecyclerView used to display all employees in a list
     */
    private void initializeRecyclerView() {
        // Initialize LayoutManager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        // Initialize Adapter
        adapter = new WorkmatesAdapter(getContext(), true);
        // Initialize RecyclerView
        binding.recyclerViewWorkmates.setHasFixedSize(true);
        binding.recyclerViewWorkmates.setLayoutManager(layoutManager);
        binding.recyclerViewWorkmates.setAdapter(adapter);
    }
}