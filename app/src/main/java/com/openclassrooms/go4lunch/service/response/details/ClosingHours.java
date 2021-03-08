package com.openclassrooms.go4lunch.service.response.details;

import androidx.room.ColumnInfo;

/**
 * Data retrieved from a JSON object, as a response of a Place Details API request, and representing
 * a closing hours information for a specific day of the week.
 * Contains all information from the "close" field of the JSON object :
 *          - "day"
 *          - "time"
 */
public class ClosingHours {
    @ColumnInfo(name = "day_close") public int day;
    @ColumnInfo(name = "time_close") public String time;
}
