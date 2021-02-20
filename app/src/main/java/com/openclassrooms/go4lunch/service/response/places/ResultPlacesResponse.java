package com.openclassrooms.go4lunch.service.response.places;

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
}