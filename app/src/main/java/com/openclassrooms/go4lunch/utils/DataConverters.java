package com.openclassrooms.go4lunch.utils;

import android.net.Uri;
import androidx.room.TypeConverter;
import com.openclassrooms.go4lunch.database.HoursData;
import com.openclassrooms.go4lunch.model.OpeningAndClosingHours;
import java.util.List;

/**
 * This class contains several converters for database data format conversion.
 */
public class DataConverters {

    /**
     * To convert a Uri to add in database
     * @param webSiteUri : Uri
     * @return : converted String
     */
    @TypeConverter
    public static String converterUriToString(Uri webSiteUri) {
        if (webSiteUri != null) return webSiteUri.toString();
        else return null;
    }

    /**
     * To convert a String downloaded from database in Uri
     * @param webSiteUriString : String
     * @return : converted Uri
     */
    @TypeConverter
    public static Uri converterStringToUri(String webSiteUriString) {
        if (webSiteUriString != null) return Uri.parse(webSiteUriString);
        return null;
    }

    /**
     * To convert an HoursData object from database in an OpeningAndClosingHours object
     * @param hoursData : HoursData to convert
     * @return : converted OpeningAndClosingHours object
     */
    public static OpeningAndClosingHours converterHoursDataToOpeningAndClosingHours(List<HoursData> hoursData) {
        OpeningAndClosingHours openingAndClosingHours = new OpeningAndClosingHours();

        for (int i = 0; i < hoursData.size(); i++) {
            openingAndClosingHours.addOpeningHours(hoursData.get(i).openingHours.day, hoursData.get(i).openingHours.time);
            openingAndClosingHours.addClosingHours(hoursData.get(i).closingHours.day, hoursData.get(i).closingHours.time);
        }
        return  openingAndClosingHours;
    }
}
