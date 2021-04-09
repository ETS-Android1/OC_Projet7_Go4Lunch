package com.openclassrooms.go4lunch.service.places.response.places;

import androidx.annotation.VisibleForTesting;

/**
 * Data retrieved from a JSON object, as a response of a Search Nearby API request, and containing
 * geometry information of a restaurant.
 * Contains all information the "geometry" field of the JSON object :
 *      - "location"
 **/
public class Geometry {
    public LocationRestaurant location;

    @VisibleForTesting
    public Geometry(LocationRestaurant location) {
        this.location = location;
    }

    public LocationRestaurant getLocation() { return location; }
}