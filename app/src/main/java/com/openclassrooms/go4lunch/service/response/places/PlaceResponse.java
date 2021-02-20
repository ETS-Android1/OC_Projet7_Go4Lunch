package com.openclassrooms.go4lunch.service.response.places;

import java.util.List;

/**
 * JSON object (root field) returned after a GET request to Places Search API
 */
public class PlaceResponse {
    public List<ResultPlacesResponse> results;
}
