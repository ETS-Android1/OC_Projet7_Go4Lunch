package com.openclassrooms.go4lunch.ui.fragments.restaurants;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import com.openclassrooms.go4lunch.R;
import com.openclassrooms.go4lunch.databinding.FragmentRestaurantDetailsBinding;
import com.openclassrooms.go4lunch.model.Restaurant;
import com.openclassrooms.go4lunch.ui.activities.MainActivity;
import com.openclassrooms.go4lunch.viewmodels.PlacesViewModel;
import java.util.Objects;

/**
 * Fragment used to display the information of a selected Restaurant.
 */
public class RestaurantDetailsFragment extends Fragment {
    public final static String TAG = "TAG_RESTAURANT_DETAILS_FRAGMENT";
    private FragmentRestaurantDetailsBinding binding;
    private Restaurant restaurant;
    private boolean selected = false;
    private PlacesViewModel placesViewModel;

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
        int indiceRestaurant = ((MainActivity) requireActivity()).getIndice();
        initializePlacesViewModel();
        restaurant = Objects.requireNonNull(placesViewModel.getListRestaurants().getValue()).get(indiceRestaurant);
        initializeDetails();
        initializePhotoRestaurant();

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
        params.setMargins(0, ((MainActivity) getActivity()).getStatusBarSize(), 0, 0);
        binding.toolbarDetailsRestaurantFragment.setLayoutParams(params);
    }

    /**
     * This method initializes the appearance of the  status bar for this fragment.
     */
    private void initializeStatusBar() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ((MainActivity) requireActivity()).getWindow().setStatusBarColor(getResources().getColor(R.color.transparent));
        }
    }

    /**
     * This method initializes all views according to fields values of the Restaurant object.
     */
    private void initializeDetails() {
        binding.nameRestaurant.setText(restaurant.getName());
        binding.addressRestaurant.setText(restaurant.getAddress());
    }

    /**
     * This method initializes a PlaceViewModel instance and attaches an observer.
     */
    private void initializePlacesViewModel() {
        placesViewModel = new ViewModelProvider(getActivity()).get(PlacesViewModel.class);
        placesViewModel.getListRestaurants().observe(getViewLifecycleOwner(), list -> {
            // Info available
            initializeDetails();
            initializePhotoRestaurant();
        });
    }

    /**
     * This methods initializes the ImageView used to display the photo of the selected restaurant.
     */
    private void initializePhotoRestaurant() {
        if (restaurant.getPhoto() != null) {
            binding.noPhotoIcon.setVisibility(View.GONE);
            Display display;
            DisplayMetrics displayMetrics = new DisplayMetrics();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                display = getContext().getDisplay();
                display.getRealMetrics(displayMetrics);
            }
            else {
                display = getActivity().getWindowManager().getDefaultDisplay();
                display.getMetrics(displayMetrics);
            }
            int WIDTH_BITMAP_IMAGE = displayMetrics.widthPixels;
            int HEIGHT_BITMAP_IMAGE = binding.photoRestaurant.getLayoutParams().height;
            binding.photoRestaurant.setImageBitmap(Bitmap.createScaledBitmap(
                    restaurant.getPhoto(), WIDTH_BITMAP_IMAGE, HEIGHT_BITMAP_IMAGE, false
            ));
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
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(restaurant.getWebsiteUri().toString()));
            startActivity(webIntent);
        }
        else Toast.makeText(getContext(), "No website url available", Toast.LENGTH_SHORT).show();
    }

}