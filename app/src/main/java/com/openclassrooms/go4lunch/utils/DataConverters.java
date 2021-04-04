package com.openclassrooms.go4lunch.utils;

import com.openclassrooms.go4lunch.database.HoursData;
import com.openclassrooms.go4lunch.model.OpeningAndClosingHours;
import com.openclassrooms.go4lunch.model.ScheduleType;

import java.util.List;

/**
 * Class containing several converters for database data format conversion.
 */
public class DataConverters {

    /**
     * To convert an HoursData object from database in an OpeningAndClosingHours object
     * @param hoursData : HoursData to convert
     *      * @return : Converted OpeningAndClosingHours object
     */
    public static OpeningAndClosingHours converterHoursDataToOpeningAndClosingHours(
            List<HoursData> hoursData) {
        OpeningAndClosingHours openingAndClosingHours = new OpeningAndClosingHours();

        for (int i = 0; i < hoursData.size(); i++){
            openingAndClosingHours.add(ScheduleType.CLOSE,
                                       hoursData.get(i).closingHours.day,
                                       hoursData.get(i).closingHours.time);
            openingAndClosingHours.add(ScheduleType.OPEN,
                                       hoursData.get(i).openingHours.day,
                                       hoursData.get(i).openingHours.time);
        }
        return  openingAndClosingHours;
    }
}
