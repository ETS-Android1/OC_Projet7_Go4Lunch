package com.openclassrooms.go4lunch.ui.fragments.restaurants;

import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.openclassrooms.go4lunch.adapters.ListViewAdapter;
import com.openclassrooms.go4lunch.databinding.FragmentListViewBinding;
import com.openclassrooms.go4lunch.model.Restaurant;
import com.openclassrooms.go4lunch.model.Workmate;
import com.openclassrooms.go4lunch.ui.activities.MainActivity;
import com.openclassrooms.go4lunch.viewmodels.PlacesViewModel;
import com.openclassrooms.go4lunch.viewmodels.WorkmatesViewModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Fragment used to display the list of restaurant in a RecyclerView, using a
 * @{@link ListViewAdapter} adapter
 */
public class ListViewFragment extends Fragment implements ListViewAdapter.OnItemRestaurantClickListener {

    public final static String TAG = "TAG_LIST_VIEW_FRAGMENT";
    private FragmentListViewBinding binding;
    private ListViewAdapter adapter;
    // ViewModels
    private PlacesViewModel placesViewModel;
    private WorkmatesViewModel workmatesViewModel;

    private int numNextPageRequest;

    public ListViewFragment() { /* Empty public constructor */ }

    public static ListViewFragment newInstance() {
        return new ListViewFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeViewModels();
    }

    private void initializeViewModels() {
        placesViewModel = ((MainActivity) requireActivity()).getPlacesViewModel();
        workmatesViewModel = ((MainActivity) requireActivity()).getWorkmatesViewModel();
    }

    private void addObserversToViewModels() {
        // PlaceViewModels
        placesViewModel.getListRestaurants().observe(getViewLifecycleOwner(), newListRestaurants -> {
            if (!adapter.getSwitchList()) { // If displayed list by adapter is list of restaurant
                adapter.updateListRestaurants(newListRestaurants);
                // Update background text
                updateTextBackgroundDisplay(newListRestaurants.size() <= 0);
                // Hide circular progress bar when loading is over
                adapter.updateVisibilityProgressBarStatus(View.INVISIBLE);
            }
        });

        placesViewModel.getListRestaurantsAutocomplete().observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> newListIdAutocomplete) {
                Log.i("PERFORMAUTOCOMPLETE", "ListViewFragment onChanged size : " + newListIdAutocomplete.size());
                if (newListIdAutocomplete.size() == 0) {
                    // Adapter affiche list restaurants
                    adapter.switchListToDisplay(false);
                }
                else {
                    List<Restaurant> currentList = adapter.getListRestaurant();
                    List<Restaurant> newListRestaurantsAutocomplete = new ArrayList<>();
                    boolean found = false;
                    int index = 0;
                    Log.i("PERFORMAUTOCOMPLETE", "ListViewFragment onChanged else : " + newListIdAutocomplete.size());
                    // Get list of Restaurants to display after autocomplete operation
                    for (int i = 0; i < newListIdAutocomplete.size(); i++) {
                        while (index < currentList.size() && !found) {
                            if (currentList.get(index).getPlaceId().equals(newListIdAutocomplete.get(i))) {
                                found = true;
                                newListRestaurantsAutocomplete.add(currentList.get(index));
                            }
                            else index++;
                        }
                    }
                    // Send to adapter
                    adapter.updateListAutocomplete(newListRestaurantsAutocomplete);
                    adapter.switchListToDisplay(true);
                }
            }
        });

        // Workmates
        workmatesViewModel.getListWorkmates().observe(getViewLifecycleOwner(), new Observer<List<Workmate>>() {
            @Override
            public void onChanged(List<Workmate> listWorkmates) {
                adapter.updateListWorkmates(listWorkmates);
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentListViewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        numNextPageRequest = 0;

        initializeRecyclerView();
        addObserversToViewModels();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        Display display;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            display = requireContext().getDisplay();
            display.getRealMetrics(displayMetrics);
        }
        else {
            display = requireActivity().getWindowManager().getDefaultDisplay();
            display.getMetrics(displayMetrics);
        }
    }

    /**
     * This method initializes a RecyclerView used to display all detected restaurants in a list
     */
    public void initializeRecyclerView() {
        // Initialize RecyclerView
        binding.recyclerViewList.setHasFixedSize(true);
        // Initialize LayoutManager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerViewList.setLayoutManager(layoutManager);
        // Initialize Adapter
        adapter = new ListViewAdapter(((MainActivity) requireActivity()).getLocationClient(),
                                       getContext(),
               this);
        binding.recyclerViewList.setAdapter(adapter);


        binding.recyclerViewList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // If end of RecyclerView list
                if (numNextPageRequest < 1) { // Search Nearby API can only returns 2 others pages of locations
                    if (!recyclerView.canScrollVertically(1)) {
                        // Get next places available to display
                        ArrayList<Restaurant> listToUpdate = new ArrayList<>();
                        listToUpdate.addAll(adapter.getListRestaurant());
                        placesViewModel.getNextPlacesNearby(listToUpdate, numNextPageRequest);
                        numNextPageRequest++;
                        // Display circular progress bar
                        adapter.updateVisibilityProgressBarStatus(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    /**
     * ListViewAdapter.OnItemRestaurantClickListener interface implementation.
     * This method is used to handle click on a RecyclerView item
     * @param position : position in the RecyclerView
     */
    @Override
    public void onItemRestaurantClick(int position) {
        Restaurant restaurantToDisplay = adapter.getListRestaurant().get(position);
        ((MainActivity) requireActivity()).setRestaurantToDisplay(restaurantToDisplay);
        ((MainActivity) requireActivity()).addFragment(RestaurantDetailsFragment.newInstance(), RestaurantDetailsFragment.TAG);
        ((MainActivity) requireActivity()).updateBottomBarStatusVisibility(View.GONE);
        ((MainActivity) requireActivity()).updateToolbarStatusVisibility(View.GONE);
    }

    /**
     * This method is used to update the background text visibility status according to "status" boolean value
     * @param status : status value.
     */
    public void updateTextBackgroundDisplay(boolean status) {
        if (status) binding.noRestaurantNearby.setVisibility(View.VISIBLE);
        else binding.noRestaurantNearby.setVisibility(View.INVISIBLE);
    }
}