package com.openclassrooms.go4lunch.service.places.request;

import com.openclassrooms.go4lunch.service.places.response.details.DetailsResponse;
import com.openclassrooms.go4lunch.service.places.response.places.PlaceResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Interface to generate GET requests to the Places Search API
 */
public interface PlaceService {
    @GET("nearbysearch/json")
    Call<PlaceResponse> searchPlaces(@Query("location") String location, @Query("type") String type);

    @GET("details/json?fields=formatted_phone_number,opening_hours,website")
    Call<DetailsResponse> getPlaceDetails(@Query("place_id") String placeId);

    @GET("nearbysearch/json")
    Call<PlaceResponse> getNextPlacesAvailable(@Query("pagetoken") String nextPlaceToken);
}
