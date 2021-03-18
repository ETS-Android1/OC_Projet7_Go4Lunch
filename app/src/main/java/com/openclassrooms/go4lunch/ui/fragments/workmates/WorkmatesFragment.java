package com.openclassrooms.go4lunch.ui.fragments.workmates;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.openclassrooms.go4lunch.adapters.WorkmatesAdapter;
import com.openclassrooms.go4lunch.databinding.FragmentWorkmatesBinding;
import com.openclassrooms.go4lunch.model.Workmate;
import com.openclassrooms.go4lunch.repositories.WorkmatesRepository;
import com.openclassrooms.go4lunch.viewmodels.WorkmatesViewModel;
import java.util.ArrayList;

/**
 * Fragment used to display the list of workmates in a RecyclerView, using a
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
        workmatesViewModel = new ViewModelProvider(requireActivity()).get(WorkmatesViewModel.class);
        workmatesViewModel.setWorkmatesRepository(new WorkmatesRepository());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentWorkmatesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        workmatesViewModel.getEmployeesInfoFromFirestoreDatabase();
        initializeRecyclerView();
        addObserverToViewModel();
        addListenerToDatabaseCollection();
    }

    /**
     * This method is used to attach an observer to the MutableLiveData listWorkmates and refresh the
     * adapter list when an update is detected.
     */
    private void addObserverToViewModel() {
        workmatesViewModel.getListEmployees().observe(getViewLifecycleOwner(), list -> {
            // Send to adapter
            adapter.updateList(list);
        });
    }

    /**
     * This method is used to attach a listener to the database collection and updates the MutableLiveData
     * listWorkmates every time an update is detected.
     */
    private void addListenerToDatabaseCollection() {
        FirebaseFirestore dbFirestore = FirebaseFirestore.getInstance();
        CollectionReference collectionRef = dbFirestore.collection("list_employees");
        collectionRef.addSnapshotListener((value, error) ->
                // Update MutableLiveData
                workmatesViewModel.getEmployeesInfoFromFirestoreDatabase());
    }

    /**
     * This method initializes a RecyclerView used to display all employees in a list
     */
    private void initializeRecyclerView() {
        // Initialize LayoutManager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        // Initialize Adapter
        adapter = new WorkmatesAdapter(getContext());
        // Initialize RecyclerView
        binding.recyclerViewWorkmates.setHasFixedSize(true);
        binding.recyclerViewWorkmates.setLayoutManager(layoutManager);
        binding.recyclerViewWorkmates.setAdapter(adapter);
    }
}