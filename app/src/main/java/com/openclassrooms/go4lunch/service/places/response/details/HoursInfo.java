package com.openclassrooms.go4lunch.service.places.response.details;

import androidx.annotation.VisibleForTesting;

import java.util.List;

/**
 * Data retrieved from a JSON object, as a response of a Place Details API request, and representing
 * hours information for a Restaurant.
 * Contains all information from the "opening_hours" field of the JSON object :
 *          - "periods"
 */
public class HoursInfo {

    public List<Period> periods;

    @VisibleForTesting
    public HoursInfo(List<Period> periods) {
        this.periods = periods;
    }

    public List<Period> getPeriods() { return periods; }
}
