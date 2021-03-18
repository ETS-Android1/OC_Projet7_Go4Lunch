package com.openclassrooms.go4lunch.service.places.response.details;

/**
 * Data retrieved from a JSON object, as a response of a Place Details API request.
 * Contains all information from the root element of the JSON object :
 *      - "result"
 *      - "status"
 **/
public class DetailsResponse {
    public ResultDetails result;
    public String status;
}
