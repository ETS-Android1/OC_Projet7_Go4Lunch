package com.openclassrooms.go4lunch.service;

import com.openclassrooms.go4lunch.service.places.response.details.DetailsResponse;
import com.openclassrooms.go4lunch.service.places.response.places.PlaceResponse;
import com.openclassrooms.go4lunch.service.places.request.PlaceService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import static org.junit.Assert.assertTrue;

/**
 * File providing tests to cover methods from @{@link PlaceService} class file.
 */
@RunWith(JUnit4.class)
public class PlaceServiceUnitTest {

    public PlaceService service;
    public Retrofit retrofit;

    @Before
    public void setupService() {
        // Define HTTP traffic interceptor
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Define HTTP client
        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/place/")
                .client(httpClient) // Client HTTP
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(PlaceService.class);
    }

    /**
     * TEST # 1 : Checks a GET request sent using PlaceService interface, to retrieve a list of
     * places, is done successfully.
     */
    @Test
    public void test_check_if_search_places_request_is_performed_with_success() throws IOException {
        // Initialize parameters for request
        double LATITUDE = 48.8434249;
        double LONGITUDE = 2.2317602;
        String TYPE = "restaurant";

        // Send request
        Response<PlaceResponse> response = service
                .searchPlaces(LATITUDE + "," + LONGITUDE,TYPE).execute();

        // Check if request response is received without error
        assertTrue(response.isSuccessful());
    }

    /**
     * TEST # 2 : Checks a GET request sent using PlaceService interface, to retrieve details for a
     * specific place, is done successfully.
     */
    @Test
    public void test_check_if_place_details_request_is_performed_with_success() throws IOException {
        // Initialize parameter for request
        String PLACE_ID = "ChIJXYZoq9h65kcRuWrPmnFCQUI";

        // Send request
        Response<DetailsResponse> response = service.getPlaceDetails(PLACE_ID).execute();

        // Check if request response is received without error
        assertTrue(response.isSuccessful());
    }

    /**
     * TEST # 1 : Checks a GET request sent using PlaceService interface, to retrieve a list of
     * others available places, is done successfully.
     */
    @Test
    public void test_check_if_next_places_available_request_is_performed_with_success()
            throws IOException {
        // Initialize parameter for request
        String NEXT_PAGE_TOKEN =
              "ATtYBwK3-qY8wyxZ2byfb3urVnRYS5STDMihRVp0N1u-" +
              "5uy1IImbxjM5ZKJaz2nEj1buzg6BBX8gvkMu20gpjFhSz9j3BR_XWZU-" +
              "nrVKcVjxJ-Mao2R_msXDDdKXUDi7Ix7hMi3v7vzsPFXfTLo6hj9zjAavg0PJS-" +
              "EGnzHKc3Gu48f0cQGEMRwlowcMfJ-PvG4CR3OrOeqqDG-Y-SSXxmA59W26b_yFlQOinM" +
              "-Nn177_HB9bqj_5aU0OqV7joLJ9e-bqBpTAIJq75WCFVHu8PbI97oUSiMTOjFm6-" +
              "30jDs0Wm43_nKkd23VstDAPpR9i6qVnN-sn-i8PxGNHjGrrfan4cor3MV0NIciwuetEBL70O-" +
             "R_nbP6P6RB5yXNxAp7xsZHJQ5u0aYFsxmrmfYqS6XgGndKmgOiw-OynkzEGahqVpxI9azyte1_F3nZ1a4VXs";

        // Send request
        Response<PlaceResponse> response = service.getNextPlacesAvailable(NEXT_PAGE_TOKEN).execute();

        // Check if request response is received without error
        assertTrue(response.isSuccessful());
    }
}
