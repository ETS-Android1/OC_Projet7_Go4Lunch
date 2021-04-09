package com.openclassrooms.go4lunch.service.places.response.places;

import androidx.annotation.VisibleForTesting;

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

    @VisibleForTesting
    public Photo(int height, String photo_reference, int width) {
        this.height = height;
        this.photo_reference = photo_reference;
        this.width = width;
    }

    public int getHeight() { return height; }

    public String getPhoto_reference() { return photo_reference; }

    public int getWidth() { return width; }
}
