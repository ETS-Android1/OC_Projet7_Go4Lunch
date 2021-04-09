package com.openclassrooms.go4lunch.service.places;

import com.openclassrooms.go4lunch.di.DI;
import com.openclassrooms.go4lunch.model.Restaurant;
import com.openclassrooms.go4lunch.service.places.request.PlaceService;
import com.openclassrooms.go4lunch.service.places.response.details.DetailsResponse;
import com.openclassrooms.go4lunch.service.places.response.places.PlaceResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import retrofit2.Response;

/**
 * Service class used to access and update the list of restaurants, with location requests results.
 */
public class ListRestaurantsService {

    private final List<Restaurant> listRestaurants;

    // Service instance
    private final PlaceService service;

    public ListRestaurantsService() {
        // Initialize list of restaurants
        listRestaurants = new ArrayList<>();

        // Create service
        service = DI.provideRetrofit().create(PlaceService.class);
    }

    /**
     * Sends a GET request using a @{@link PlaceService } interface, and
     * returns as a result a set of available places nearby user location.
     * @param location : Info location of the user
     * @param type : Type of places to search
     * @return : Result of a GET request
     * @throws IOException : Exception thrown if the GET request fail
     */
    public PlaceResponse findPlacesNearby(String location, String type) throws IOException {
        clearListRestaurants();
        Response<PlaceResponse> response = service.searchPlaces(location, type).execute();
        if (response.isSuccessful()) {
            return response.body();
        }
        throw new IOException(Objects.requireNonNull(response.errorBody()).toString());
    }

    /**
     * Sends a GET request using a @{@link PlaceService} interface, and
     * returns as a result a set of others available places nearby user location.
     * @param nextPlaceToken : String value used to request the other available places
     * @return : Available places
     * @throws IOException : Exception thrown if the GET request fail
     */
    public PlaceResponse getNextPlacesNearby(String nextPlaceToken) throws IOException {
        Response<PlaceResponse> response = service.getNextPlacesAvailable(nextPlaceToken).execute();
        if (response.isSuccessful()) {
            return response.body();
        }
        throw new IOException(Objects.requireNonNull(response.errorBody()).toString());
    }

    /**
     * Returns the details of a restaurant with a GET request using a @{@link PlaceService } interface
     * @return : Result of a GET request
     * @throws IOException : Exception thrown if the GET request fail
     */
    public DetailsResponse getPlacesDetails(String placeId) throws IOException{
        Response<DetailsResponse> response = service.getPlaceDetails(placeId).execute();
        if (response.isSuccessful()) {
            return response.body();
        }
        else throw new IOException(Objects.requireNonNull(response.errorBody()).toString());
    }

    private void clearListRestaurants() {
        listRestaurants.clear();
    }

    /**
     * This method updates a list of restaurant by adding a new one
     * @param restaurant : New restaurant to add
     */
    public void updateListRestaurants(Restaurant restaurant) {
        listRestaurants.add(restaurant);
    }

    /**
     * This method return the list of existing restaurants
     * @return : List of restaurants
     */
    public List<Restaurant> getListRestaurants() {
        return listRestaurants;
    }
}
