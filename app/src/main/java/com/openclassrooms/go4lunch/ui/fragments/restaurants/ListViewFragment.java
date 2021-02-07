package com.openclassrooms.go4lunch.ui.fragments.restaurants;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.openclassrooms.go4lunch.adapters.ListViewAdapter;
import com.openclassrooms.go4lunch.databinding.FragmentListViewBinding;
import com.openclassrooms.go4lunch.ui.activities.MainActivity;

/**
 * Fragment used to display the list of restaurant in a RecyclerView, using a
 * @{@link ListViewAdapter} adapter
 */
public class ListViewFragment extends Fragment {

    public final static String TAG = "TAG_LIST_VIEW_FRAGMENT";
    private FragmentListViewBinding binding;
    private ListViewAdapter adapter;

    public ListViewFragment() { /* Empty public constructor */ }

    public static ListViewFragment newInstance() {
        return new ListViewFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentListViewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Initialize RecyclerView
        binding.recyclerViewList.setHasFixedSize(true);

        // Initialize LayoutManager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerViewList.setLayoutManager(layoutManager);

        // Initialize Adapter
        adapter = new ListViewAdapter( ((MainActivity) getActivity()).getClient(), getContext(),
                ((MainActivity) requireActivity()).getPlacesViewModel(),
                ((MainActivity) requireActivity()).getPlacesClient());
        binding.recyclerViewList.setAdapter(adapter);

        // Add observer to placesViewModel
        ((MainActivity) getActivity()).getPlacesViewModel().getListRestaurants()
                .observe(getViewLifecycleOwner(), newList ->
                        adapter.updateList(newList)
                );
    }
}