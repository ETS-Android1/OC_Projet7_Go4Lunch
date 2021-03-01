package com.openclassrooms.go4lunch.service.response.places;

import com.google.android.libraries.places.api.model.PlusCode;
import com.openclassrooms.go4lunch.service.response.details.OpeningHoursResponse;

import java.util.List;

/**
 * "results" field of a JSON object returned after a GET request to Places Search API
 */
public class ResultPlacesResponse {
    public GeometryResponse geometry;
    public String id;
    public String name;
    public String place_id;
    public String vicinity;
    public double rating;
    public OpeningHoursResponse opening_hours;
    public List<PhotoResponse> photos;

}