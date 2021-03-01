package com.openclassrooms.go4lunch.service.request;

import android.graphics.Bitmap;

import com.openclassrooms.go4lunch.BuildConfig;
import com.openclassrooms.go4lunch.service.response.details.DetailsResponse;
import com.openclassrooms.go4lunch.service.response.places.PlaceResponse;

import okhttp3.Response;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Interface to generate GET requests to the Places Search API
 */
public interface PlaceService {
    @GET("nearbysearch/json?key=" + BuildConfig.API_KEY + "&rankby=distance")
    Call<PlaceResponse> searchPlaces(@Query("location") String location, @Query("type") String type);

    //@GET("nearbysearch/json?key=" + BuildConfig.API_KEY)
    //Call<PlaceResponse> getNextSetOfPlaces(@Query("pagetoken") String nextPageToken);

    @GET("details/json?fields=formatted_phone_number,opening_hours,website&key=" + BuildConfig.API_KEY)
    Call<DetailsResponse> getPlaceDetails(@Query("place_id") String place_id);

    // https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=CnRtAAAATLZNl354RwP_9UKbQ_5Psy40texXePv4oAlgP4qNEkdIrkyse7rPXYGd9D_Uj1rVsQdWT4oRz4QrYAJNpFX7rzqqMlZw2h2E2y5IKMUZ7ouD_SlcHxYq1yL4KbKUv3qtWgTK0A6QbGh87GB3sscrHRIQiG2RrmU_jF4tENr9wGS_YxoUSSDrYjWmrNfeEHSGSc3FyhNLlBU&key=YOUR_API_KEY
    @GET("photo?key=" + BuildConfig.API_KEY + "&maxwidth=200&maxheight=200")
    void getPlacePhoto(@Query("photo_reference") String photo_reference);
}
