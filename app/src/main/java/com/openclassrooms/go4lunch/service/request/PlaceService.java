package com.openclassrooms.go4lunch.service.request;

import com.openclassrooms.go4lunch.BuildConfig;
import com.openclassrooms.go4lunch.service.response.places.PlaceResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Interface to generate GET requests to the Places Search API
 */
public interface PlaceService {
    @GET("nearbysearch/json?key=" + BuildConfig.API_KEY)
    Call<PlaceResponse> searchPlaces(@Query("location") String location, @Query("radius") int radius, @Query("type") String type);
}
