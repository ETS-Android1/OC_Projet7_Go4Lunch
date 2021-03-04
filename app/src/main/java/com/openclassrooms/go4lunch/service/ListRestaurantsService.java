package com.openclassrooms.go4lunch.service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import androidx.annotation.NonNull;
import com.openclassrooms.go4lunch.BuildConfig;
import com.openclassrooms.go4lunch.model.Restaurant;
import com.openclassrooms.go4lunch.service.response.details.DetailsResponse;
import com.openclassrooms.go4lunch.service.response.places.PlaceResponse;
import com.openclassrooms.go4lunch.service.request.PlaceService;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
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
     * This method is used to return the result of a GET request using a @{@link PlaceService } interface
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
     * This method is used to return the details of a restaurant with a GET request using a @{@link PlaceService } interface
     * @return : Result of a GET request
     * @throws IOException : Exception thrown if the GET request fail
     */
    public DetailsResponse getPlacesDetails(String place_id) throws IOException{
        return service.getPlaceDetails(place_id).execute().body();
    }

    /**
     * This method is used to update each @{@link Restaurant} object with its associated photo, which is the result
     * of an HTTP GET request.
     * @param restaurant : Restaurant to update
     */
    public void getPlacePhoto(Restaurant restaurant) {
        Request request = new Request.Builder()
                .url("https://maps.googleapis.com/maps/api/place/photo?&maxwidth=400&maxheight=400&photo_reference=" + restaurant.getPhotoReference() + "&key=" + BuildConfig.API_KEY)
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException exception) {
                exception.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.body() != null) {
                    Bitmap bitmap = Bitmap.createBitmap(BitmapFactory.decodeStream(response.body().byteStream()));
                    restaurant.setPhoto(bitmap);
                }
            }
        });
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
