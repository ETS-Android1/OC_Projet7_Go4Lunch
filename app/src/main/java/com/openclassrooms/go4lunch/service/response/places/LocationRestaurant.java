package com.openclassrooms.go4lunch.service.response.places;

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
}