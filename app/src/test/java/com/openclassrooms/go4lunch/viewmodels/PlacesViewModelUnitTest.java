package com.openclassrooms.go4lunch.viewmodels;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;
import com.openclassrooms.go4lunch.model.Restaurant;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * File providing tests to cover @{@link PlacesViewModel} class file.
 */
@RunWith(JUnit4.class)
public class PlacesViewModelUnitTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    private PlacesViewModel placesViewModel;


    @Before
    public void setup(){
        placesViewModel = new PlacesViewModel();
    }

    /**
     * TEST #1 : Check if sending a new value to the MutableLiveData will correctly notify the attached
     * observer, and update the list of restaurants.
     */
    @Test
    public void test_check_if_list_of_restaurants_mutable_live_data_is_correctly_updated() {
        // Initialize List of restaurants
        List<Restaurant> listRestaurants = new ArrayList<>();
        String PLACE_ID = "ChIJGz20sdh65kcRCfY0bMPzkVo";
        String NAME = "Franprix";
        String ADDRESS = "80 Avenue Jean Baptiste Clement, Boulogne-Billancourt";
        double LATITUDE = 48.8437898;
        double LONGITUDE = 2.2320991;
        double RATING = 3.4;
        Restaurant restaurant = new Restaurant(PLACE_ID, NAME, ADDRESS, LATITUDE, LONGITUDE, RATING);
        listRestaurants.add(restaurant);

        // Create observer
        Observer<List<Restaurant>> observer = new Observer<List<Restaurant>>() {
            @Override
            public void onChanged(List<Restaurant> listRestaurants) {
                assertNotNull(listRestaurants);
                assertEquals(1, listRestaurants.size());
                assertEquals(PLACE_ID, listRestaurants.get(0).getPlaceId());
                assertEquals(NAME, listRestaurants.get(0).getName());
                assertEquals(ADDRESS, listRestaurants.get(0).getAddress());
                assertEquals(LATITUDE, listRestaurants.get(0).getLatitude(), 0);
                assertEquals(LONGITUDE, listRestaurants.get(0).getLongitude(), 0);
                assertEquals(RATING, listRestaurants.get(0).getRating(), 0);
                placesViewModel.getListRestaurants().removeObserver(this);
            }
        };

        // Add observer to MutableLiveData
        placesViewModel.getListRestaurants().observeForever(observer);

        // Set new value to MutableLiveData
        placesViewModel.getListRestaurants().setValue(listRestaurants);
    }

    /**
     * TEST #2 : Check if sending a new value to the MutableLiveData will correctly notify the attached
     * observer, and update the list of restaurants ids.
     */
    @Test
    public void test_check_if_list_autocomplete_mutable_live_data_is_correctly_updated() {
        // Initialize List of restaurants id
        String ID_1 = "ChIJGz20sdh65kcRCfY0bMPzkVo";
        String ID_2 = "ChIJA_5Kq9h65kcRvIzH3sGqB5Q";
        String ID_3 = "ChIJXYZoq9h65kcRuWrPmnFCQUI";
        List<String> listRestaurantIds = Arrays.asList(ID_1, ID_2, ID_3);

        // Create observer
        Observer<List<String>> observer = new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> newListRestaurantIds) {
                assertNotNull(newListRestaurantIds);
                assertEquals(3, newListRestaurantIds.size());
                for (int i = 0; i < newListRestaurantIds.size(); i++) {
                    switch (i) {
                        case 0:
                            assertEquals(ID_1, newListRestaurantIds.get(0));
                            break;
                        case 1:
                            assertEquals(ID_2, newListRestaurantIds.get(1));
                            break;
                        case 2:
                            assertEquals(ID_3, newListRestaurantIds.get(2));
                            break;
                    }
                }
            }
        };
        // Add observer to MutableLiveData
        placesViewModel.getListRestaurantsAutocomplete().observeForever(observer);

        // Set new value to MutableLiveData
        placesViewModel.getListRestaurantsAutocomplete().setValue(listRestaurantIds);
    }
}
