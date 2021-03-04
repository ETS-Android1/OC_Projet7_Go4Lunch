package com.openclassrooms.go4lunch.service;

import com.openclassrooms.go4lunch.model.Restaurant;
import java.util.List;

/**
 * Callback interface to get a list of restaurants detected around user location
 */
public interface ServicePlacesCallback {
    void onPlacesAvailable(List<Restaurant> listRestaurant);
}
