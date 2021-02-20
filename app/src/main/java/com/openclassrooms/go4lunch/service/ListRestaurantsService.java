package com.openclassrooms.go4lunch.service;

import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.openclassrooms.go4lunch.model.Restaurant;
import com.openclassrooms.go4lunch.service.response.places.PlaceResponse;
import com.openclassrooms.go4lunch.service.request.PlaceService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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

    public ListRestaurantsService() {
        // Initialize list of restaurants
        listRestaurants = new ArrayList<>();

        // Define HTTP traffic interceptor
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Define HTTP client
        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(interceptor).build();

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
     * This method is used to return the result of a GET request using a @{@link PlaceService } interface
     * @param location : Info location of the user
     * @param radius : Detection radius
     * @param type : Type of places to search
     * @return : Result of a GET request
     * @throws IOException : Exception thrown if the GET request fail
     */
    public PlaceResponse findPlacesNearby(String location, int radius, String type) throws IOException {
        clearListRestaurants();
        return service.searchPlaces(location, radius, type).execute().body();
    }

    /**
     * This method is used to update the list of restaurants with details
     * @param list : List of restaurant to update with details for each place
     * @param placesClient : PlacesClient Instance to access Places API methods
     * @param callback : Callback interface
     */
    public void getPlacesDetails(List<Restaurant> list, PlacesClient placesClient, ServiceDetailsCallback callback) {
        // Define data to retrieve from response
        List<Place.Field> fields  = Arrays.asList(Place.Field.WEBSITE_URI,
                Place.Field.PHONE_NUMBER,
                Place.Field.OPENING_HOURS,
                Place.Field.PHOTO_METADATAS);

        try {
            for (int indice = 0; indice < list.size(); indice++) {
                Restaurant restaurant = list.get(indice);

                // Define request
                FetchPlaceRequest request = FetchPlaceRequest.newInstance(restaurant.getId(), fields);
                int finalIndice = indice;
                placesClient.fetchPlace(request).addOnSuccessListener((FetchPlaceResponse fetchPlaceResponse) -> {
                            restaurant.setOpeningHours(fetchPlaceResponse.getPlace().getOpeningHours());
                            restaurant.setPhoneNumber(fetchPlaceResponse.getPlace().getPhoneNumber());
                            restaurant.setWebsiteUri(fetchPlaceResponse.getPlace().getWebsiteUri());
                            restaurant.setPhotoMetadataList(fetchPlaceResponse.getPlace().getPhotoMetadatas());
                            list.set(finalIndice, restaurant);
                            if (finalIndice == list.size()-1) {
                                callback.onPlacesDetailsAvailable(list);
                            }
                        }
                ).addOnFailureListener(Throwable::printStackTrace);
            }
        } catch (NullPointerException exception) {
            exception.printStackTrace();
        }
    }


    /**
     * This method is used to update the list of restaurants with photo
     * @param placesClient : PlacesClient Instance to access Places API methods
     * @param list : List of restaurant to update with details for each place
     * @param callback : Callback interface
     */
    public void getPlacesPhotos(PlacesClient placesClient, List<Restaurant> list, ServicePhotoCallback callback) {
        for (int indice = 0; indice < list.size(); indice++) {
            Restaurant restaurant = list.get(indice);
            try {
                FetchPhotoRequest request = FetchPhotoRequest.newInstance(PhotoMetadata
                        .builder(restaurant.getPhotoMetadataList().get(0).zza())
                        .setHeight(restaurant.getPhotoMetadataList().get(0).getHeight())
                        .setWidth(restaurant.getPhotoMetadataList().get(0).getWidth())
                        .build());

                int finalIndice = indice;
                placesClient.fetchPhoto(request)
                        .addOnSuccessListener(fetchPhotoResponse -> {
                            restaurant.setPhoto(fetchPhotoResponse.getBitmap());
                            list.set(finalIndice, restaurant);
                            if (finalIndice == list.size() - 1) callback.onPhotoAvailable(list);
                        })
                        .addOnFailureListener(Throwable::printStackTrace);
            } catch (NullPointerException exception) {
                exception.printStackTrace();
            }
        }
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
