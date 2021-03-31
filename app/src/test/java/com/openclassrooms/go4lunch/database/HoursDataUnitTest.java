package com.openclassrooms.go4lunch.database;

import com.openclassrooms.go4lunch.service.places.response.details.ClosingHours;
import com.openclassrooms.go4lunch.service.places.response.details.OpeningHours;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.assertEquals;

/**
 * File providing tests to cover @{@link HoursData} class file.
 */
@RunWith(JUnit4.class)
public class HoursDataUnitTest {

    /**
     * TEST #1 : Checks if a @{@link HoursData} object is correctly instantiated.
     */
    @Test
    public void test_hours_data_object_creation() {
        ClosingHours closingHours = new ClosingHours(0, "1400");
        OpeningHours openingHours = new OpeningHours(0, "0900");
        String PLACE_ID = "ChIJGz20sdh65kcRCfY0bMPzkVo";

        // Initialize an HoursData object
        HoursData hoursData = new HoursData(closingHours, openingHours, PLACE_ID);

        // Check if values in HoursData object fields are correct
        assertEquals(PLACE_ID, hoursData.getRestaurantId());
        assertEquals(0, hoursData.getClosingHours().getDay());
        assertEquals("1400", hoursData.getClosingHours().getTime());
        assertEquals(0, hoursData.getOpeningHours().getDay());
        assertEquals("0900", hoursData.getOpeningHours().getTime());
    }
}
