package com.openclassrooms.go4lunch.service.places.response.details;

import androidx.annotation.VisibleForTesting;

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

    @VisibleForTesting
    public ResultDetails(String formattedPhoneNumber, HoursInfo hoursInfo, String website) {
        this.formatted_phone_number = formattedPhoneNumber;
        this.opening_hours = hoursInfo;
        this.website = website;
    }

    public String getFormattedPhoneNumber() { return formatted_phone_number; }

    public HoursInfo getOpeningHours() { return opening_hours; }

    public String getWebsite() { return website; }
}
