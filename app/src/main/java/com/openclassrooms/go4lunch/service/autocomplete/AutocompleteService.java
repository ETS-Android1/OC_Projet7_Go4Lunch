package com.openclassrooms.go4lunch.service.autocomplete;

import android.Manifest;
import android.location.Location;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.openclassrooms.go4lunch.ui.activities.MainActivityCallback;
import com.openclassrooms.go4lunch.utils.GeometricUtils;
import java.util.ArrayList;
import java.util.List;

public class AutocompleteService {

    private PlacesClient placesClient;
    private FusedLocationProviderClient locationClient;
    private AutocompleteSessionToken token;

    public AutocompleteService(PlacesClient placesClient, FusedLocationProviderClient locationClient) {
        this.placesClient = placesClient;
        this.locationClient = locationClient;
        this.token = AutocompleteSessionToken.newInstance();
    }

    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    public void performAutocompleteRequest(String query, ServiceAutocompleteCallback callback) {
        Log.i("PERFORMAUTOCOMPLETE", "AutocompleteService request ");
        Log.i("PERFORMAUTOCOMPLETE", "AutocompleteService : " + query);
        locationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener(location -> {
                    // Get current location
                    double currentLat = location.getLatitude();
                    double currentLon = location.getLongitude();

                    // Define location restrictions
                    RectangularBounds bounds = RectangularBounds.newInstance(
                            GeometricUtils.getCoordinate(currentLat, currentLon, -500L, -500L),  // Southwest point
                            GeometricUtils.getCoordinate(currentLat, currentLon, 500L, 500L) // Northeast point
                    );

                    // Build Autocomplete request
                    FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                            .setTypeFilter(TypeFilter.ESTABLISHMENT)
                            .setLocationRestriction(bounds)
                            .setOrigin(new LatLng(currentLat, currentLon))
                            .setCountry("FR")
                            .setSessionToken(token)
                            .setQuery(query)
                            .build();

                    // Initialize list to contains "Restaurant" results
                    List<String> autocompleteRestaurantIdList = new ArrayList<>();

                    // Get results
                    placesClient.findAutocompletePredictions(request).addOnSuccessListener(response -> {
                        // Extract Restaurants Id from response
                        for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                            List<Place.Type> types = prediction.getPlaceTypes();
                            boolean found = false;
                            int index = 0;

                            while (index < types.size() && !found) {
                                if (types.get(index) == Place.Type.RESTAURANT) found = true;
                                else index++;
                            }
                            if (found) { // Place is a Restaurant
                                autocompleteRestaurantIdList.add(prediction.getPlaceId());
                            }
                        }
                        // Send list back to MainActivity
                        callback.getAutocompleteResults(autocompleteRestaurantIdList);
                    }).addOnFailureListener(Throwable::printStackTrace);
                });
    }
}
