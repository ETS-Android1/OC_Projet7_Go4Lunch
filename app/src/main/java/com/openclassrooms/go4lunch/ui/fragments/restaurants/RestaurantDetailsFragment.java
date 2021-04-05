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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.bumptech.glide.Glide;
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

/**
 * Fragment class used to display the information of a selected Restaurant.
 */
public class RestaurantDetailsFragment extends Fragment {

    public static final String TAG = "TAG_RESTAURANT_DETAILS_FRAGMENT";
    private FragmentRestaurantDetailsBinding binding;
    private Restaurant restaurant;

    // Parameter defining if the current restaurant has been selected or not by user
    private boolean selected = false;

    // View model
    private WorkmatesViewModel workmatesViewModel;

    // Adapter to display the list of Workmates
    private WorkmatesAdapter adapter;

    // To handle SharedPreferences file updates
    private SharedPreferences.Editor editor;

    // Parameters to handle the list of liked restaurants for the current user
    private boolean likeStatus = false;
    private boolean alreadyInDatabase = false;
    List<String> listLikedRestaurants = new ArrayList<>();

    // Document id of the current user in Firestore database
    private String documentID;

    public RestaurantDetailsFragment() {/* Empty constructor */}

    public static RestaurantDetailsFragment newInstance() {
        return new RestaurantDetailsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRestaurantDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDocumentReferenceId();
        initializeStatusBar();
        initializeRecyclerView();
        initializeViewModels();
        restaurant = ((MainActivity) requireActivity()).getRestaurantToDisplay();
        checkIfRestaurantWasLiked();
        initializeDetails();
        initializePhotoRestaurant();
        initializeSharedPreferences();
        updateFloatingActionButtonIconDisplay();
        handleFloatingButtonClicks();
        handleButtonsClicks();
    }

    /**
     * Initializes document user id from firestore database.
     */
    public void initDocumentReferenceId() {
        SharedPreferences sharedPrefFirestoreUserId = requireContext()
                .getSharedPreferences(AppInfo.FILE_FIRESTORE_USER_ID, Context.MODE_PRIVATE);
        documentID = sharedPrefFirestoreUserId
                                        .getString(AppInfo.PREF_FIRESTORE_USER_ID_KEY, "");
    }

    /**
     * Initializes SharedPreferences objects to access SharedPreferences file.
     */
    @SuppressLint("CommitPrefEdits")
    public void initializeSharedPreferences() {
        // SharedPreferences to save user restaurant choice
        SharedPreferences sharedPrefSelection = requireContext()
                 .getSharedPreferences(AppInfo.FILE_PREF_SELECTED_RESTAURANT, Context.MODE_PRIVATE);
        editor = sharedPrefSelection.edit();

        // Check if a selection is saved in SharedPreferences
        String savedRestaurantJSON =
                   sharedPrefSelection.getString(AppInfo.PREF_SELECTED_RESTAURANT_KEY, "");

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
        // Initialize LayoutManager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        // Initialize Adapter
        adapter = new WorkmatesAdapter(getContext(), false);
        // Initialize RecyclerView
        binding.recyclerViewWorkmatesRestaurant.setHasFixedSize(true);
        binding.recyclerViewWorkmatesRestaurant.setLayoutManager(layoutManager);
        binding.recyclerViewWorkmatesRestaurant.setAdapter(adapter);
    }

    /**
     * Initializes the appearance of the  status bar for this fragment.
     */
    private void initializeStatusBar() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            requireActivity().getWindow()
                             .setStatusBarColor(getResources().getColor(R.color.transparent));
    }

    /**
     * Initializes all views according to fields values of the Restaurant object.
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    private void initializeDetails() {
        binding.nameRestaurant.setText(restaurant.getName());
        binding.addressRestaurant.setText(restaurant.getAddress());
        RatingDisplayHandler.displayRating(binding.noteStar1, binding.noteStar2, binding.noteStar3,
                        binding.noteStar4, binding.noteStar5, restaurant.getRating(), getContext());
    }

    /**
     * Initializes a PlaceViewModel instance and attaches an observer.
     */
    private void initializeViewModels() {
        // ViewModels
        PlacesViewModel placesViewModel =
                                new ViewModelProvider(requireActivity()).get(PlacesViewModel.class);
        placesViewModel.getListRestaurants().observe(getViewLifecycleOwner(), list -> {
            // Info available
            initializeDetails();
            initializePhotoRestaurant();
        });

        workmatesViewModel = new ViewModelProvider(requireActivity()).get(WorkmatesViewModel.class);
        workmatesViewModel.getListWorkmates().observe(getViewLifecycleOwner(), listWorkmates -> {
            ArrayList<Workmate> listFiltered = new ArrayList<>();
            for (int i = 0; i < listWorkmates.size(); i++) {
                if (listWorkmates.get(i).getRestaurantSelectedID().equals(restaurant.getPlaceId()))
                    listFiltered.add(listWorkmates.get(i));
            }
             adapter.updateList(listFiltered);
        });
    }

    /**
     * Initializes the ImageView used to display the photo of the selected restaurant.
     */
    private void initializePhotoRestaurant() {
        if (restaurant.getPhotoReference() != null) {
            binding.noPhotoIcon.setVisibility(View.GONE);
            Glide.with(requireContext())
                 .load("https://maps.googleapis.com/maps/api/place/photo?" +
                              "&maxwidth=400&maxheight=400&photo_reference="
                              + restaurant.getPhotoReference() + "&key=" + BuildConfig.API_KEY)
                 .centerCrop()
                 .override(binding.noPhotoIcon.getWidth(), binding.noPhotoIcon.getHeight())
                 .into(binding.photoRestaurant);
        }
    }

    /**
     * Updates the FloatingActionButton display according to the "selected" boolean value.
     */
    private void updateFloatingActionButtonIconDisplay() {
        if (selected) binding.fabSelect.setSupportImageTintList(
                              ColorStateList.valueOf(getResources().getColor(R.color.light_green)));
        else binding.fabSelect.setSupportImageTintList(
                            ColorStateList.valueOf(getResources().getColor(R.color.light_grey_95)));
    }


    /**
     * Handles user click interactions with "Selection restaurant" FloatingActionButton.
     */
    private void handleFloatingButtonClicks() {
        binding.fabSelect.setOnClickListener(v -> {
            selected = !selected;
            updateFloatingActionButtonIconDisplay();
            // Init SharedPreferences
            SharedPreferences sharedPrefFirestoreUserId = requireContext().getSharedPreferences(
                    AppInfo.FILE_FIRESTORE_USER_ID,
                    Context.MODE_PRIVATE);
            String firestoreDocumentId = sharedPrefFirestoreUserId
                                      .getString(AppInfo.PREF_FIRESTORE_USER_ID_KEY, null);

            if (selected) {
                // Update SharedPreferences
                String restaurantJSONtoSave = new Gson().toJson(restaurant);
                editor.putString(AppInfo.PREF_SELECTED_RESTAURANT_KEY, restaurantJSONtoSave);
                // Update Firestore database
                workmatesViewModel.updateDocumentReferenceCurrentUser(restaurant.getName(),
                                                                      restaurant.getPlaceId(),
                                                                      firestoreDocumentId);
            }
            else {
                editor.putString(AppInfo.PREF_SELECTED_RESTAURANT_KEY, "");
                workmatesViewModel.updateDocumentReferenceCurrentUser("",
                                                                    "",
                                                                     firestoreDocumentId);
            }
            editor.apply();
        });
    }

    /**
     * Handles user click interactions with all options buttons.
     */
    @SuppressLint("ClickableViewAccessibility")
    private void handleButtonsClicks() {
        binding.buttonCall.setOnClickListener(v -> launchCallIntent());

        binding.buttonLike.setOnClickListener(v -> {
            likeStatus = !likeStatus;
            updateLikeButtonDisplay();
        });

        binding.buttonWebsite.setOnClickListener(v -> openWebSite());
    }

    /**
     * Launches an Intent.ACTION_DIAL intent after a click on the "Call" option button.
     */
    private void launchCallIntent() {
        if (restaurant.getPhoneNumber() != null) {
            Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel",
                                                               restaurant.getPhoneNumber(),
                                                                           null));
            startActivity(callIntent);
        }
        else Toast.makeText(getContext(), "No phone number available", Toast.LENGTH_SHORT).show();
    }

    /**
     * Launches an Intent.ACTION_VIEW intent with a URI website after a click on the
     * "WEBSITE" option button.
     */
    private void openWebSite() {
        if (restaurant.getWebsiteUri() != null) {
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(restaurant.getWebsiteUri()));
            startActivity(webIntent);
        }
        else Toast.makeText(getContext(), "No website url available", Toast.LENGTH_SHORT).show();
    }

    /**
     * Updates "Like" button display.
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    private void updateLikeButtonDisplay() {
        if (likeStatus)
            binding.buttonLike.setCompoundDrawablesWithIntrinsicBounds(null,
                    requireContext().getResources().getDrawable(
                                 R.drawable.ic_baseline_star_24dp_orange), null, null);
        else
            binding.buttonLike.setCompoundDrawablesWithIntrinsicBounds(null,
                    requireContext().getResources().getDrawable(
                          R.drawable.ic_baseline_star_border_24dp_orange), null, null);
    }

    /**
     * Gets the list of liked restaurants by current user.
     */
    @SuppressWarnings("unchecked")
    private void checkIfRestaurantWasLiked() {
        if (!documentID.equals("")) {
            workmatesViewModel.getDocumentReferenceCurrentUser(documentID).get().addOnSuccessListener(documentSnapshot -> {
                // Get list of restaurant liked by user
                listLikedRestaurants = (List<String>) documentSnapshot.get("liked");
                // Check if list contains the current restaurant
                try {
                    if (listLikedRestaurants != null ) {
                        int j = 0;
                        while (j < listLikedRestaurants.size() && !likeStatus) {
                            if (listLikedRestaurants.get(j).equals(restaurant.getPlaceId())) {
                                likeStatus = true;
                                alreadyInDatabase = true;
                            }
                            else j++;
                        }
                    }
                } catch (NullPointerException exception) {
                    exception.printStackTrace();
                }
                // Update button status
                updateLikeButtonDisplay();
            });
        }
    }

    /**
     * Updates the list of liked restaurants and send it to the WorkmatesService to update
     * the Firestore database.
     */
    public void updateFirestoreWithLikeStatus() {
        // Update list of liked restaurant for the current user
        if (alreadyInDatabase && !likeStatus) // The restaurant was initially liked
                                              // and is now disliked by user
            listLikedRestaurants.remove(restaurant.getPlaceId());
        if (!alreadyInDatabase && likeStatus) // The restaurant was initially not liked
                                              // and is now liked by user.
            listLikedRestaurants.add(restaurant.getPlaceId());
        // Send list the BDD
        workmatesViewModel.updateCurrentUserListOfLikedRestaurant(documentID, listLikedRestaurants);
    }
}