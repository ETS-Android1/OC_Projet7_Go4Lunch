package com.openclassrooms.go4lunch.service;

import com.openclassrooms.go4lunch.model.Restaurant;
import java.util.List;

/**
 * Callback interface to get an updated list of restaurants with photo for each place
 */
public interface ServicePhotoCallback {
    void onPhotoAvailable(List<Restaurant> listRestaurant);
}
