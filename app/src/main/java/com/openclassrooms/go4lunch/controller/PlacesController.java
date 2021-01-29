package com.openclassrooms.go4lunch.controller;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;
import androidx.annotation.RequiresPermission;
import com.google.android.gms.maps.model.LatLng;
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
import com.openclassrooms.go4lunch.model.Restaurant;
import com.openclassrooms.go4lunch.service.ListRestaurantsService;
import java.util.Arrays;
import java.util.List;

/**
 * This class is used to retrieve data for all restaurants localized around the user location, and
 * to store theses values in a @{@link ListRestaurantsService} object.
 */
public class PlacesController {

    /**
     * This method is used to search to all restaurants localized around user location
     * @param placesClient : client interface to access Place API methods
     */
    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    public static void searchPlacesInCurrentLocation(PlacesClient placesClient, Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getActiveNetworkInfo() != null) {
            // Define data to retrieve from response
            List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME,
                    Place.Field.ADDRESS, Place.Field.LAT_LNG,
                    Place.Field.TYPES);

            // Initialize request
            FindCurrentPlaceRequest request = FindCurrentPlaceRequest.builder(placeFields).build();
            @SuppressLint("MissingPermission") Task<FindCurrentPlaceResponse> placeResponse = placesClient.findCurrentPlace(request);

            placeResponse.addOnCompleteListener(task -> {
                // Clear previous list
                ListRestaurantsService.clearListRestaurants();
                // Update list with new restaurants
                for (PlaceLikelihood place : task.getResult().getPlaceLikelihoods()) {
                    List<Place.Type> types = place.getPlace().getTypes();
                    if (types != null) {
                        if (types.contains(Place.Type.RESTAURANT)) {

                            // Create a new Restaurant with the first data fields
                            Restaurant restaurant = new Restaurant(place.getPlace().getId(),
                                    place.getPlace().getName(),
                                    place.getPlace().getAddress(),
                                    new LatLng(place.getPlace().getLatLng().latitude,
                                            place.getPlace().getLatLng().longitude));

                            // Get other data
                            getPlaceDetails(place.getPlace().getId(), placesClient, restaurant);
                        }
                    }
                }
            }).addOnFailureListener(Throwable::printStackTrace);
        }
    }

    /**
     * This method is used to retrieve all data from a restaurant (which cannot be using findCurrentPlace()),
     * according to its id.
     * @param placeId : id of the restaurant
     * @param placesClient : client interface to access Place API methods
     */
    public static void getPlaceDetails(String placeId, PlacesClient placesClient, Restaurant restaurant) {
        // Define data to retrieve from response
        List<Place.Field> fields  = Arrays.asList(Place.Field.WEBSITE_URI,
                                                  Place.Field.PHONE_NUMBER,
                                                  Place.Field.OPENING_HOURS,
                                                  Place.Field.PHOTO_METADATAS,
                                                  Place.Field.RATING,
                                                  Place.Field.ADDRESS_COMPONENTS);

        FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, fields);

        placesClient.fetchPlace(request).addOnSuccessListener((FetchPlaceResponse fetchPlaceResponse) -> {
                restaurant.setOpeningHours(fetchPlaceResponse.getPlace().getOpeningHours());
                restaurant.setPhoneNumber(fetchPlaceResponse.getPlace().getPhoneNumber());
                restaurant.setWebsiteUri(fetchPlaceResponse.getPlace().getWebsiteUri());
                restaurant.setRating(fetchPlaceResponse.getPlace().getRating());
                if (fetchPlaceResponse.getPlace().getPhotoMetadatas().size() > 0)
                getPlacePhoto(placesClient, restaurant, fetchPlaceResponse.getPlace().getPhotoMetadatas());
            }
        ).addOnFailureListener(Throwable::printStackTrace);
    }

    /**
     * This method is used to retrieve a photo from a restaurant,
     * according to its photo reference.
     * @param placesClient : client interface to access Place API methods
     * @param restaurant : restaurant localized around the current user location
     * @param metadataList : list of photos metadata associated with a restaurant
     */
    public static void getPlacePhoto(PlacesClient placesClient, Restaurant restaurant, List<PhotoMetadata> metadataList) {
        FetchPhotoRequest request = FetchPhotoRequest.newInstance(PhotoMetadata
                                                                  .builder(metadataList.get(0)
                                                                           .zza())
                                                                           .setHeight(metadataList.get(0).getHeight())
                                                                           .setWidth(metadataList.get(0).getWidth())
                                                                           .build());

        placesClient.fetchPhoto(request)
                .addOnSuccessListener(fetchPhotoResponse -> {
                    restaurant.setPhoto(fetchPhotoResponse.getBitmap());
                    ListRestaurantsService.updateListRestaurants(restaurant);
                })
                .addOnFailureListener(Throwable::printStackTrace);
    }
}
