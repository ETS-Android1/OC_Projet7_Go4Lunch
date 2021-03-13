package com.openclassrooms.go4lunch.service;

import com.openclassrooms.go4lunch.model.Restaurant;
import com.openclassrooms.go4lunch.service.response.details.DetailsResponse;
import com.openclassrooms.go4lunch.service.response.places.PlaceResponse;
import com.openclassrooms.go4lunch.service.request.PlaceService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Service to access and update the list of restaurants, with location requests results.
 */
public class ListRestaurantsService {

    private final List<Restaurant> listRestaurants;

    // Service instance
    private final PlaceService service;
    private final OkHttpClient httpClient;

    public ListRestaurantsService() {
        // Initialize list of restaurants
        listRestaurants = new ArrayList<>();

        // Define HTTP traffic interceptor
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Define HTTP client
        httpClient = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        // Initialize retrofit instance
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/place/")
                .client(httpClient) // Client HTTP
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create service
        service = retrofit.create(PlaceService.class);
    }

    /**
     * This method is used to send a GET request using a @{@link PlaceService } interface, and
     * returns as a result a set of available places nearby user location.
     * @param location : Info location of the user
     * @param type : Type of places to search
     * @return : Result of a GET request
     * @throws IOException : Exception thrown if the GET request fail
     */
    public PlaceResponse findPlacesNearby(String location, String type) throws IOException {
        clearListRestaurants();
        return service.searchPlaces(location, type).execute().body();
    }

    /**
     * This method is used to send a GET request using a @{@link PlaceService} interface, and
     * returns as a result a set of others available places nearby user location.
     * @param nextPlaceToken : String value used to request the other available places
     * @return : available places
     * @throws IOException : Exception thrown if the GET request fail
     */
    public PlaceResponse getNextPlacesNearby(String nextPlaceToken) throws IOException {
        return service.getNextPlacesAvailable(nextPlaceToken).execute().body();
    }

    /**
     * This method is used to return the details of a restaurant with a GET request using a @{@link PlaceService } interface
     * @return : Result of a GET request
     * @throws IOException : Exception thrown if the GET request fail
     */
    public DetailsResponse getPlacesDetails(String place_id) throws IOException{
        return service.getPlaceDetails(place_id).execute().body();
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
