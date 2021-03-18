package com.openclassrooms.go4lunch.service.places.response.places;

import java.util.List;

/**
 * Data retrieved from a JSON object, as a response of a Search Nearby API request, and containing
 * all information of a restaurant.
 * Contains all information the "results" field of the JSON object :
 *      - "geometry"
 *      - "place_id"
 *      - "name"
 *      - "vicinity"
 *      - "rating"
 *      - "photos"
 **/
public class ResultPlaces {
    public Geometry geometry;
    public String name;
    public String place_id;
    public String vicinity;
    public double rating;
    public List<Photo> photos;
}