package com.openclassrooms.go4lunch.ui.fragments.restaurants;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.openclassrooms.go4lunch.BuildConfig;
import com.openclassrooms.go4lunch.R;
import com.openclassrooms.go4lunch.adapters.WorkmatesAdapter;
import com.openclassrooms.go4lunch.databinding.FragmentRestaurantDetailsBinding;
import com.openclassrooms.go4lunch.model.Restaurant;
import com.openclassrooms.go4lunch.model.Workmate;
import com.openclassrooms.go4lunch.ui.activities.MainActivity;
import com.openclassrooms.go4lunch.utils.AppInfo;
import com.openclassrooms.go4lunch.utils.RatingDisplayHandler;
import com.openclassrooms.go4lunch.viewmodels.PlacesViewModel;
import com.openclassrooms.go4lunch.viewmodels.WorkmatesViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Fragment used to display the information of a selected Restaurant.
 */
public class RestaurantDetailsFragment extends Fragment {

    public final static String TAG = "TAG_RESTAURANT_DETAILS_FRAGMENT";
    private FragmentRestaurantDetailsBinding binding;
    private Restaurant restaurant;
    private boolean selected = false;

    // ViewModels
    private PlacesViewModel placesViewModel;
    private WorkmatesViewModel workmatesViewModel;

    // Adapter to display the list of Workmates
    private WorkmatesAdapter adapter;

    // SharedPreferences to save user restaurant choice
    private SharedPreferences sharedPrefSelection;
    private SharedPreferences.Editor editor;
    private String savedRestaurantJSON;
    private String KEY_SELECTED_RESTAURANT = "KEY_SELECTED_RESTAURANT";
    public RestaurantDetailsFragment() {/* Empty constructor */}

    public static RestaurantDetailsFragment newInstance() {
        return new RestaurantDetailsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRestaurantDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeToolbar();
        initializeStatusBar();
        initializeRecyclerView();
        initializeViewModels();
        restaurant = ((MainActivity) requireActivity()).getRestaurantToDisplay();
        initializeDetails();
        initializePhotoRestaurant();
        initializeSharedPreferences();
        updateFloatingActionButtonIconDisplay();
        handleFloatingButtonClicks();
        handleButtonsClicks();
    }

    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) { menu.clear(); }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            requireActivity().onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void initializeSharedPreferences() {
        sharedPrefSelection = requireContext().getSharedPreferences(AppInfo.FILE_PREF_SELECTED_RESTAURANT, Context.MODE_PRIVATE);
        editor = sharedPrefSelection.edit();

        // Check if a selection is saved in SharedPreferences
        savedRestaurantJSON = sharedPrefSelection.getString(KEY_SELECTED_RESTAURANT, "");

        if (!savedRestaurantJSON.equals("")) {
            // If yes, deserialize the data
            Gson gson = new Gson();
            Restaurant restaurantSaved = gson.fromJson(savedRestaurantJSON, Restaurant.class);
            // Compare to current restaurant displayed
            if (restaurantSaved.getPlaceId().equals(restaurant.getPlaceId())) {
                selected = true;
            }
        }
    }

    private void initializeRecyclerView() {
        // Initialize LayoutMananger
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        // Initialize Adapter
        adapter = new WorkmatesAdapter(getContext(), false);
        // Initialize RecyclerView
        binding.recyclerViewWorkmatesRestaurant.setHasFixedSize(true);
        binding.recyclerViewWorkmatesRestaurant.setLayoutManager(layoutManager);
        binding.recyclerViewWorkmatesRestaurant.setAdapter(adapter);
    }

    /**
     * This method initializes the appearance of the parent activity toolbar for this fragment.
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    private void initializeToolbar() {
        // Add SupportActionBar
        ((MainActivity) requireActivity()).setSupportActionBar(binding.toolbarDetailsRestaurantFragment);
        // Configure Toolbar
        Objects.requireNonNull(((MainActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(((MainActivity) requireActivity()).getSupportActionBar()).setTitle("");
        Objects.requireNonNull(((MainActivity) requireActivity()).getSupportActionBar())
                .setHomeAsUpIndicator(getResources()
                        .getDrawable(R.drawable.ic_baseline_arrow_back_24dp_white));
        // Set margin
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) binding.toolbarDetailsRestaurantFragment.getLayoutParams();
        params.setMargins(0, AppInfo.getStatusBarSize(requireContext()), 0, 0);
        binding.toolbarDetailsRestaurantFragment.setLayoutParams(params);
    }

    /**
     * This method initializes the appearance of the  status bar for this fragment.
     */
    private void initializeStatusBar() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            requireActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.transparent));
        }
    }

    /**
     * This method initializes all views according to fields values of the Restaurant object.
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    private void initializeDetails() {
        binding.nameRestaurant.setText(restaurant.getName());
        binding.addressRestaurant.setText(restaurant.getAddress());
        RatingDisplayHandler.displayRating(binding.noteStar1, binding.noteStar2, binding.noteStar3,
                        binding.noteStar4, binding.noteStar5, restaurant.getRating(), getContext());
    }

    /**
     * This method initializes a PlaceViewModel instance and attaches an observer.
     */
    private void initializeViewModels() {
        placesViewModel = new ViewModelProvider(requireActivity()).get(PlacesViewModel.class);
        placesViewModel.getListRestaurants().observe(getViewLifecycleOwner(), list -> {
            // Info available
            initializeDetails();
            initializePhotoRestaurant();
        });

        workmatesViewModel = new ViewModelProvider(requireActivity()).get(WorkmatesViewModel.class);
        workmatesViewModel.getListWorkmates().observe(getViewLifecycleOwner(), new Observer<List<Workmate>>() {
            @Override
            public void onChanged(List<Workmate> listWorkmates) {
                ArrayList<Workmate> listFiltered = new ArrayList<>();
                for (int i = 0; i < listWorkmates.size(); i++) {
                    if (listWorkmates.get(i).getRestaurantSelectedID().equals(restaurant.getPlaceId())) {
                        listFiltered.add(listWorkmates.get(i));
                    }
                    adapter.updateList(listFiltered);
                }

            }
        });
    }

    /**
     * This methods initializes the ImageView used to display the photo of the selected restaurant.
     */
    private void initializePhotoRestaurant() {
        if (restaurant.getPhotoReference() != null) {
            binding.noPhotoIcon.setVisibility(View.GONE);
            Glide.with(requireContext())
                 .load("https://maps.googleapis.com/maps/api/place/photo?&maxwidth=400&maxheight=400&photo_reference="
                         + restaurant.getPhotoReference() + "&key=" + BuildConfig.API_KEY)
                 .centerCrop()
                 .override(binding.noPhotoIcon.getWidth(), binding.noPhotoIcon.getHeight())
                 .into(binding.photoRestaurant);
        }
    }

    /**
     * This methods updates the FloatingActionButton display according to the "selected" boolean value.
     */
    private void updateFloatingActionButtonIconDisplay() {
        if (selected) binding.fabSelect.setSupportImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.light_green)));
        else binding.fabSelect.setSupportImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.light_grey_95)));
    }


    /**
     * This method handles user click interactions with "Selection restaurant" FloatingActionButton.
     */
    private void handleFloatingButtonClicks() {
        binding.fabSelect.setOnClickListener(v -> {
            selected = !selected;
            updateFloatingActionButtonIconDisplay();

            if (selected) {
                String restaurantJSONtoSave = new Gson().toJson(restaurant);
                editor.putString(KEY_SELECTED_RESTAURANT, restaurantJSONtoSave);
            }
            else {
                editor.putString(KEY_SELECTED_RESTAURANT, "");
            }
            editor.apply();
        });
    }

    /**
     * This method handles user click interactions with all options buttons.
     */
    @SuppressLint("ClickableViewAccessibility")
    private void handleButtonsClicks() {
        binding.buttonCall.setOnClickListener(v -> launchCallIntent());
        binding.buttonLike.setOnClickListener(v -> { });
        binding.buttonWebsite.setOnClickListener(v -> openWebSite());
    }

    /**
     * This method in used to launch an Intent.ACTION_DIAL intent after a click on the "Call" option button.
     */
    private void launchCallIntent() {
        if (restaurant.getPhoneNumber() != null) {
            Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", restaurant.getPhoneNumber(), null));
            startActivity(callIntent);
        }
        else Toast.makeText(getContext(), "No phone number available", Toast.LENGTH_SHORT).show();
    }

    /**
     * This method is used to launch an Intent.ACTION_VIEW intent with a URI website after a click on the
     * "WEBSITE" option button.
     */
    private void openWebSite() {
        if (restaurant.getWebsiteUri() != null) {
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(restaurant.getWebsiteUri()));
            startActivity(webIntent);
        }
        else Toast.makeText(getContext(), "No website url available", Toast.LENGTH_SHORT).show();
    }

}