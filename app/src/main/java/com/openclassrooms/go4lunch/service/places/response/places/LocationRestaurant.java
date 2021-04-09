package com.openclassrooms.go4lunch.service.places.response.places;

import androidx.annotation.VisibleForTesting;

/**
 * Data retrieved from a JSON object, as a response of a Search Nearby API request, and containing
 * location information of a restaurant.
 * Contains all information the "location" field of the JSON object :
 *      - "lat"
 *      - "lon"
 **/
public class LocationRestaurant {
    public double lat;
    public double lng;

    @VisibleForTesting
    public LocationRestaurant(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() { return lat; }

    public double getLng() { return lng; }
}