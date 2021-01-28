package com.openclassrooms.go4lunch.service;

import com.openclassrooms.go4lunch.model.Restaurant;
import java.util.ArrayList;

/**
 * This service is used to :
 *      - allows the @{@link com.openclassrooms.go4lunch.ui.fragments.MapViewFragment} to update the list of
 *      restaurants using the @{@link com.openclassrooms.go4lunch.controller.PlacesController}
 *      - allows the @{@link com.openclassrooms.go4lunch.ui.fragments.ListViewFragment} to display the list
 *      of all restaurants in its RecyclerView
 */
public class ListRestaurantsService {

    private static ArrayList<Restaurant> listRestaurants = new ArrayList<>();

    /**
     * This method is used to return the list of all restaurants in localized around user location
     * @return : the list of restaurants.
     */
    public static ArrayList<Restaurant> getListRestaurants() {
        return listRestaurants;
    }

    /**
     * This method is used to update the list of localized restaurants by adding a new one.
     * @param restaurant : new localized restaurant
     */
    public static void updateListRestaurants(Restaurant restaurant) {
        listRestaurants.add(restaurant);
    }

    /**
     * This method is used to clear the list of restaurants.
     */
    public static void clearListRestaurants() {
        listRestaurants.clear();
    }
}
