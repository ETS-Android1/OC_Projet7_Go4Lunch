package com.openclassrooms.go4lunch.service.response.details;

import androidx.room.ColumnInfo;

/**
 * Data retrieved from a JSON object, as a response of a Place Details API request, and representing
 * an opening hours information for a specific day of the week.
 * Contains all information from the "open" field of the JSON object :
 *          - "day"
 *          - "time"
 */
public class OpeningHours {
    @ColumnInfo(name = "day_open") public int day;
    @ColumnInfo(name = "time_open") public String time;
}
