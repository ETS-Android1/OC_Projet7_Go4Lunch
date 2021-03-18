package com.openclassrooms.go4lunch.service.places.response.details;

/**
 * Data retrieved from a JSON object, as a response of a Place Details API request, and representing
 * all closing/opening hours information for each day of the week.
 * Contains all information the "periods" field of the JSON object :
 *      - "close"
 *      - "open"
 **/
public class Period {
    public ClosingHours close;
    public OpeningHours open;
}
