package com.openclassrooms.go4lunch.service.places.response.details;

/**
 * Data retrieved from a JSON object, as a response of a Place Details API request.
 * Contains all information the "result" field of the JSON object :
 *      - "formatted_phone_number"
 *      - "opening_hours"
 *      - "website"
 **/
public class ResultDetails {
    public String formatted_phone_number;
    public HoursInfo opening_hours;
    public String website;
}
