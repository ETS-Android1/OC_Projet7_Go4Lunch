package com.openclassrooms.go4lunch.repositories;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.openclassrooms.go4lunch.dao.HoursDao;
import com.openclassrooms.go4lunch.dao.RestaurantAndHoursDao;
import com.openclassrooms.go4lunch.dao.RestaurantDao;
import com.openclassrooms.go4lunch.model.Restaurant;
import com.openclassrooms.go4lunch.service.places.response.places.Geometry;
import com.openclassrooms.go4lunch.service.places.response.places.LocationRestaurant;
import com.openclassrooms.go4lunch.service.places.response.places.Photo;
import com.openclassrooms.go4lunch.service.places.response.places.ResultPlaces;
import com.openclassrooms.go4lunch.utils.AppInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.util.List;
import static  org.junit.Assert.assertEquals;

/**
 * File providing tests to cover methods from @{@link PlacesRepository} class file.
 */
@RunWith(JUnit4.class)
public class PlacesRepositoryUnitTest {

    @Mock public RestaurantDao restaurantDao;
    @Mock public HoursDao hoursDao;
    @Mock public RestaurantAndHoursDao restaurantAndHoursDao;
    @Mock public PlacesClient placesClient;
    @Mock public FusedLocationProviderClient locationClient;
    @Mock public Context context;
    @Mock public LocationRestaurant locationRestaurant;
    @Mock public Geometry geometry;
    @Mock public Photo photo;
    @Mock public List<Photo> listPhotos;
    @Mock SharedPreferences sharedPreferences;
    public PlacesRepository placesRepository;
    public ResultPlaces resultPlaces;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        // Describe behavior of mocked classes
        Mockito.when(context.getSharedPreferences(AppInfo.FILE_PREF_NEXT_PAGE_TOKEN,
                Context.MODE_PRIVATE)).thenReturn(sharedPreferences);
        // Initialize repositories
        placesRepository = new PlacesRepository(restaurantDao, hoursDao, restaurantAndHoursDao,
                                                        context, placesClient, locationClient);
    }

    /**
     * TEST #1 : test method initializeRestaurantObject()
     * Checks if a Restaurant object is correctly created using ResultPlaces object.
     */
    @Test
    public void test_restaurant_object_initialization() {
        // Initialize data
        String NAME = "GEMINI Boulogne";
        String ADDRESS = "86 Avenue Jean Baptiste Clement, Boulogne-Billancourt";
        String PLACE_ID = "ChIJXYZoq9h65kcRuWrPmnFCQUI";
        double RATING = 4.0;
        double LATITUDE = 48.8434249;
        double LONGITUDE = 2.2317602;
        int HEIGHT = 4032;
        int WIDTH = 3024;
        String PHOTO_REFERENCE = "ATtYBwIfVw6sZ_bOeFhcYNffkjG7hPjbGi58Bkyt0vFlyppfSt9cyg5wIb7AdcojcHz" +
                                 "wjgJb5e0eKy-wk8goCZoEg9FzdjP9LXXBiiCAfjTx7h4phHBa6ZIOzQy5VI0tiNQEllj" +
                                 "WSSefzb2LAQF6uGngsweyWKDcTI7dUCZCibC9siqqtj7I";

        // Describe behavior
        resultPlaces = Mockito.mock(ResultPlaces.class);
        Mockito.when(resultPlaces.getName()).thenReturn(NAME);
        Mockito.when(resultPlaces.getVicinity()).thenReturn(ADDRESS);
        Mockito.when(resultPlaces.getPlace_id()).thenReturn(PLACE_ID);
        Mockito.when(resultPlaces.getRating()).thenReturn(RATING);
        Mockito.when(resultPlaces.getGeometry()).thenReturn(geometry);

        Mockito.when(geometry.getLocation()).thenReturn(locationRestaurant);
        Mockito.when(locationRestaurant.getLng()).thenReturn(LONGITUDE);
        Mockito.when(locationRestaurant.getLat()).thenReturn(LATITUDE);

        Mockito.when(resultPlaces.getPhotos()).thenReturn(listPhotos);
        Mockito.when(listPhotos.get(0)).thenReturn(photo);
        Mockito.when(photo.getHeight()).thenReturn(HEIGHT);
        Mockito.when(photo.getWidth()).thenReturn(WIDTH);
        Mockito.when(photo.getPhoto_reference()).thenReturn(PHOTO_REFERENCE);
        Mockito.when(listPhotos.isEmpty()).thenReturn(false);

        // test method
        Restaurant restaurant = placesRepository.initializeRestaurantObject(resultPlaces);
        assertEquals(NAME,restaurant.getName());
        assertEquals(ADDRESS, restaurant.getAddress());
        assertEquals(PLACE_ID, restaurant.getPlaceId());
        assertEquals(RATING, restaurant.getRating(), 0);
        assertEquals(LATITUDE, restaurant.getLatitude(), 0);
        assertEquals(LONGITUDE, restaurant.getLongitude(), 0);
        assertEquals(HEIGHT, restaurant.getPhotoHeight());
        assertEquals(PHOTO_REFERENCE, restaurant.getPhotoReference());
        assertEquals(WIDTH, restaurant.getPhotoWidth());
    }
}
