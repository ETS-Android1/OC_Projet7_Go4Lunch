package com.openclassrooms.go4lunch.service.places.response.details;

import androidx.annotation.VisibleForTesting;
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

    /**
     * Used for testing only. See DataConvertersUnitTest.java file.
     * @param day : Day of the week
     * @param time : Opening hour - format "HHMM"
     */
    @VisibleForTesting
    public OpeningHours(int day, String time) {
        this.day = day;
        this.time = time;
    }

    /**
     * Used for testing only. See HoursDataUnitTest.java file.
     */
    @VisibleForTesting
    public int getDay() {
        return day;
    }

    /**
     * Used for testing only. See HoursDataUnitTest.java file.
     */
    @VisibleForTesting
    public String getTime() {
        return time;
    }
}
