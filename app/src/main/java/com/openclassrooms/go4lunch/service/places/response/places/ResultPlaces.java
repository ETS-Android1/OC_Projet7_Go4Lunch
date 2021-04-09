package com.openclassrooms.go4lunch.service.places.response.places;

import androidx.annotation.VisibleForTesting;

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

    @VisibleForTesting
    public ResultPlaces(String name, String place_id, String vicinity, double rating) {
        this.name = name;
        this.place_id = place_id;
        this.vicinity = vicinity;
        this.rating = rating;
    }

    // Getters
    public Geometry getGeometry() { return geometry; }

    public String getName() { return name; }

    public String getPlace_id() { return place_id; }

    public String getVicinity() { return vicinity; }

    public double getRating() { return rating; }

    public List<Photo> getPhotos() { return photos; }

}