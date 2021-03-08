package com.openclassrooms.go4lunch.service.response.places;

/**
 * Data retrieved from a JSON object, as a response of a Search Nearby API request, and containing
 * photo information of a restaurant.
 * Contains all information the "photos" field of the JSON object :
 *      - "height"
 *      - "photo_reference"
 *      - "width"
 **/
public class Photo {
    public int height;
    public String photo_reference;
    public int width;
}
