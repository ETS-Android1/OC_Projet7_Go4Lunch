package com.openclassrooms.go4lunch.repositories;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import com.openclassrooms.go4lunch.dao.HoursDao;
import com.openclassrooms.go4lunch.dao.RestaurantAndHoursDao;
import com.openclassrooms.go4lunch.dao.RestaurantDao;
import com.openclassrooms.go4lunch.database.FakeDataTest;
import com.openclassrooms.go4lunch.di.DI;
import com.openclassrooms.go4lunch.model.Restaurant;
import com.openclassrooms.go4lunch.service.places.ServiceDetailsCallback;
import com.openclassrooms.go4lunch.service.places.ServicePlacesCallback;
import com.openclassrooms.go4lunch.ui.activities.MainActivity;
import com.openclassrooms.go4lunch.utils.AppInfo;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * File providing tests to cover methods from @{@link PlacesRepository} class file.
 */
@RunWith(AndroidJUnit4.class)
public class PlacesRepositoryInstrumentTest {

    private Context context;
    RestaurantDao restaurantDao;
    HoursDao hoursDao;
    RestaurantAndHoursDao restaurantAndHoursDao;

    @Rule
    public final ActivityScenarioRule rule = new ActivityScenarioRule<>(MainActivity.class);

    public PlacesRepository placesRepository;

    private List<Restaurant> listRestaurants;

    @Before
    public void setUp() {
        this.context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        // Initialize parameters
        restaurantDao = DI.provideDatabase(context).restaurantDao();
        restaurantAndHoursDao = DI.provideDatabase(context).restaurantAndHoursDao();
        hoursDao = DI.provideDatabase(context).hoursDao();
        // Clear SharedPreferences file
        context.getSharedPreferences(AppInfo.FILE_PREF_NEXT_PAGE_TOKEN, Context.MODE_PRIVATE)
               .edit().clear().apply();
        // Initialize repository
        placesRepository = new PlacesRepository(restaurantDao, hoursDao, restaurantAndHoursDao,
                                                       context, null, null);
        // Initialize list of restaurants
        initializeListRestaurants();
    }

    private void initializeListRestaurants() {
        Restaurant restaurant1 = new Restaurant(FakeDataTest.RESTAURANT_1_PLACE_ID,
                FakeDataTest.RESTAURANT_1_NAME,
                FakeDataTest.RESTAURANT_1_ADDRESS,
                FakeDataTest.RESTAURANT_1_LATITUDE,
                FakeDataTest.RESTAURANT_1_LONGITUDE,
                FakeDataTest.RESTAURANT_1_RATING);
        Restaurant restaurant2 = new Restaurant(FakeDataTest.RESTAURANT_2_PLACE_ID,
                FakeDataTest.RESTAURANT_2_NAME,
                FakeDataTest.RESTAURANT_2_ADDRESS,
                FakeDataTest.RESTAURANT_2_LATITUDE,
                FakeDataTest.RESTAURANT_2_LONGITUDE,
                FakeDataTest.RESTAURANT_2_RATING);
        Restaurant restaurant3 = new Restaurant(FakeDataTest.RESTAURANT_3_PLACE_ID,
                FakeDataTest.RESTAURANT_3_NAME,
                FakeDataTest.RESTAURANT_3_ADDRESS,
                FakeDataTest.RESTAURANT_3_LATITUDE,
                FakeDataTest.RESTAURANT_3_LONGITUDE,
                FakeDataTest.RESTAURANT_3_RATING);
        listRestaurants = new ArrayList<>();
        listRestaurants.add(restaurant1);
        listRestaurants.add(restaurant2);
        listRestaurants.add(restaurant3);
    }

    /**
     * TEST #1 : test findPlacesNearby() method.
     * Checks if repository correctly retrieves detected restaurants
     * from @{@link com.openclassrooms.go4lunch.service.places.ListRestaurantsService}
     * @throws IOException : exception
     */
    @Test
    public void test_if_method_find_places_nearby_correctly_work() throws IOException {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(AppInfo.FILE_PREF_NEXT_PAGE_TOKEN, Context.MODE_PRIVATE);

        ServicePlacesCallback callback = listRestaurant -> {
            // Check results
            assertFalse(listRestaurant.isEmpty());
            assertNotNull(sharedPreferences.getString(AppInfo.PREF_FIRST_NEXT_PAGE_TOKEN_KEY, null));
        };

        placesRepository.findPlacesNearby("48.8434249,2.2317602", "restaurant", callback);
    }

    /**
     * TEST #2 : test getPlacesDetails() method.
     * Checks if a list of restaurants is correctly updated with details after a Details request
     * @throws IOException : exception
     */
    @Test
    public void test_if_method_get_place_details_correctly_works() throws IOException {

        // Initialize callback to retrieve updated list
        ServiceDetailsCallback callback = (listRestaurant, listOfListHoursData) -> {
            assertFalse(listOfListHoursData.isEmpty());
            for (int i = 0; i < listRestaurant.size(); i++) {
                switch (i) {
                    case 0:
                        assertEquals(FakeDataTest.RESTAURANT_1_WEBSITE_URI, listRestaurant.get(i).getWebsiteUri());
                        assertEquals(FakeDataTest.RESTAURANT_1_NUMBER, listRestaurant.get(i).getPhoneNumber());
                        break;
                    case 1:
                        assertEquals(FakeDataTest.RESTAURANT_2_WEBSITE_URI, listRestaurant.get(i).getWebsiteUri());
                        assertEquals(FakeDataTest.RESTAURANT_2_NUMBER, listRestaurant.get(i).getPhoneNumber());
                        break;
                    case 2:
                        assertEquals(FakeDataTest.RESTAURANT_3_WEBSITE_URI, listRestaurant.get(i).getWebsiteUri());
                        assertEquals(FakeDataTest.RESTAURANT_3_NUMBER, listRestaurant.get(i).getPhoneNumber());
                        break;
                }
            }
        };

        // Request details
        placesRepository.getPlacesDetails(listRestaurants, callback);
    }

    /**
     * TEST #3 : test getNextPlacesNearby() method.
     * Checks if a list of restaurants is correctly updated with details after a Details request
     * @throws IOException : exception
     */
    @Test
    public void test_if_method_get_newt_places_nearby_correctly_works() throws IOException {
        // Service to handle callback results
        ServicePlacesCallback callback = listRestaurant -> assertFalse(listRestaurant.isEmpty());

        // Initialize SharedPreferences file to contain correct next_page_token value
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                                           AppInfo.FILE_PREF_NEXT_PAGE_TOKEN, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(AppInfo.PREF_FIRST_NEXT_PAGE_TOKEN_KEY,
                                           FakeDataTest.NEXT_PLACE_TOKEN_VALUE).apply();

        // Request first page of data
        placesRepository.getNextPlacesNearby(callback, listRestaurants, 0);
    }
}
