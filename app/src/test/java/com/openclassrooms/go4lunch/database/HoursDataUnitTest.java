package com.openclassrooms.go4lunch.database;

import com.openclassrooms.go4lunch.service.places.response.details.ClosingHours;
import com.openclassrooms.go4lunch.service.places.response.details.OpeningHours;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;
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
        // Mocking ClosingHours object
        ClosingHours closingHoursMock = Mockito.mock(ClosingHours.class);
        Mockito.when(closingHoursMock.getDay()).thenReturn(0);
        Mockito.when(closingHoursMock.getTime()).thenReturn("1400");

        // Mocking OpeningHours object
        OpeningHours openingHoursMock = Mockito.mock(OpeningHours.class);
        Mockito.when(openingHoursMock.getDay()).thenReturn(0);
        Mockito.when(openingHoursMock.getTime()).thenReturn("0900");

        // Initialize an HoursData object
        String PLACE_ID = "ChIJGz20sdh65kcRCfY0bMPzkVo";
        HoursData hoursData = new HoursData(closingHoursMock, openingHoursMock, PLACE_ID);

        // Check if values in HoursData object fields are correct
        assertEquals(PLACE_ID, hoursData.getRestaurantId());
        assertEquals(0, hoursData.getClosingHours().getDay());
        assertEquals("1400", hoursData.getClosingHours().getTime());
        assertEquals(0, hoursData.getOpeningHours().getDay());
        assertEquals("0900", hoursData.getOpeningHours().getTime());
    }
}
