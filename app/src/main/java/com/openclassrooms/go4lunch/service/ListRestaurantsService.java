package com.openclassrooms.go4lunch.service;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.RuntimeExecutionException;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.openclassrooms.go4lunch.adapters.ListViewAdapter;
import com.openclassrooms.go4lunch.adapters.ListViewAdapterCallback;
import com.openclassrooms.go4lunch.model.Restaurant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Service to access and update the list of restaurants, with location requests results.
 */
public class ListRestaurantsService {

    private static final List<Restaurant> listRestaurants = new ArrayList<>();

    /**
     * This method is used to search to all restaurants localized around user location
     * @param placesClient : client interface to access Place API methods
     * @param context : context of the view
     */
    public static void searchPlacesInCurrentLocation(PlacesClient placesClient, Context context)  {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getActiveNetworkInfo() != null) {
            // Define data to retrieve from response
            List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME,
                                                          Place.Field.ADDRESS, Place.Field.LAT_LNG,
                                                          Place.Field.TYPES);

            // Initialize request & response
            FindCurrentPlaceRequest request = FindCurrentPlaceRequest.builder(placeFields).build();
            @SuppressLint("MissingPermission")
            Task<FindCurrentPlaceResponse> placeResponse = placesClient.findCurrentPlace(request);

            placeResponse.addOnCompleteListener(task -> {
                // Clear previous list
                clearListRestaurants();
                // Update list with new restaurants
                try {
                    for (PlaceLikelihood place : task.getResult().getPlaceLikelihoods()) {
                        List<Place.Type> types = place.getPlace().getTypes();
                        if (types != null) {
                            if (types.contains(Place.Type.RESTAURANT)) {
                                // Create a new Restaurant with the first data fields
                                Restaurant restaurant = new Restaurant(place.getPlace().getId(),
                                        place.getPlace().getName(),
                                        place.getPlace().getAddress(),
                                        new LatLng(Objects.requireNonNull(place.getPlace().getLatLng()).latitude,
                                                place.getPlace().getLatLng().longitude));
                                getPlaceDetails(placesClient, restaurant);
                                // Update list with new restaurant
                                updateListRestaurants(restaurant);
                            }
                        }
                    }
                } catch(RuntimeExecutionException exception) { exception.printStackTrace(); }
            }).addOnFailureListener(Throwable::printStackTrace);
        }
    }

    /**
     * This method is used to retrieve all data from a restaurant (which cannot be using findCurrentPlace()),
     * @param placesClient : client interface to access Place API methods
     * @param restaurant : restaurant object to update with data
     */
    public static void getPlaceDetails(PlacesClient placesClient, Restaurant restaurant) {

        // Define data to retrieve from response
        List<Place.Field> fields  = Arrays.asList(Place.Field.WEBSITE_URI,
                Place.Field.PHONE_NUMBER,
                Place.Field.OPENING_HOURS,
                Place.Field.PHOTO_METADATAS,
                Place.Field.RATING,
                Place.Field.ADDRESS_COMPONENTS);

        // Define request
        FetchPlaceRequest request = FetchPlaceRequest.newInstance(restaurant.getId(), fields);

        placesClient.fetchPlace(request).addOnSuccessListener((FetchPlaceResponse fetchPlaceResponse) -> {
                    restaurant.setOpeningHours(fetchPlaceResponse.getPlace().getOpeningHours());
                    restaurant.setPhoneNumber(fetchPlaceResponse.getPlace().getPhoneNumber());
                    restaurant.setWebsiteUri(fetchPlaceResponse.getPlace().getWebsiteUri());
                    restaurant.setRating(fetchPlaceResponse.getPlace().getRating());
                    restaurant.setPhotoMetadataList(fetchPlaceResponse.getPlace().getPhotoMetadatas());
        }
        ).addOnFailureListener(Throwable::printStackTrace);
    }

    /**
     * This method is used to retrieve a photo from a restaurant
     * @param placesClient : client interface to access Place API methods
     * @param position : position of the restaurant in the list
     * @param holder : holder in which the photo must be displayed
     * @param callback : callback to the ListViewFragment adapter
     */
    public static void getPlacePhoto(PlacesClient placesClient, int position,
                                     @NonNull ListViewAdapter.ViewHolderListView holder,
                                     ListViewAdapterCallback callback) {

        Restaurant restaurant = listRestaurants.get(position);

        FetchPhotoRequest request = FetchPhotoRequest.newInstance(PhotoMetadata
                .builder(restaurant.getPhotoMetadataList().get(0)
                        .zza())
                .setHeight(restaurant.getPhotoMetadataList().get(0).getHeight())
                .setWidth(restaurant.getPhotoMetadataList().get(0).getWidth())
                .build());

        placesClient.fetchPhoto(request)
                .addOnSuccessListener(fetchPhotoResponse -> {
                    restaurant.setPhoto(fetchPhotoResponse.getBitmap());
                    callback.updateViewHolderWithPhoto(position, holder);

                })
                .addOnFailureListener(Throwable::printStackTrace);
    }

    private static void clearListRestaurants() {
        listRestaurants.clear();
    }

    private static void updateListRestaurants(Restaurant restaurant) {
        listRestaurants.add(restaurant);
    }

    public static List<Restaurant> getListRestaurants() {
        return listRestaurants;
    }
}
