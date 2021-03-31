package com.openclassrooms.go4lunch.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * File providing tests to cover @{@link OpeningAndClosingHours} class file.
 */
@RunWith(JUnit4.class)
public class OpeningAndClosingHoursUnitTest {

    /**
     * TEST #1 : Checks if a @{@link OpeningAndClosingHours} object is correctly instantiated and updated.
     */
    @Test
    public void test_opening_and_hours_object_creation() {
        // Initialize object
        OpeningAndClosingHours openingAndClosingHours = new OpeningAndClosingHours();

        // Add Hours for each day of the week
        // Monday : Closed no hours
        // Tuesday : 12:00-14:00 / 19:30-23:00
        openingAndClosingHours.addOpeningHours(2, "1200"); // Tuesday, 12:00
        openingAndClosingHours.addClosingHours(2, "1400"); // Tuesday, 14:00
        openingAndClosingHours.addOpeningHours(2, "1930"); // Tuesday, 19:30
        openingAndClosingHours.addClosingHours(2, "2300"); // Tuesday, 23:00
        // Wednesday : 12:00-14:00 / 19:30-23:00
        openingAndClosingHours.addOpeningHours(3, "1200"); // Wednesday, 12:00
        openingAndClosingHours.addClosingHours(3, "1400"); // Wednesday, 14:00
        openingAndClosingHours.addOpeningHours(3, "1930"); // Wednesday, 19:30
        openingAndClosingHours.addClosingHours(3, "2300"); // Wednesday, 23:00
        // Thursday : 12:00-14:00 / 19:30-23:00
        openingAndClosingHours.addOpeningHours(4, "1200"); // Thursday, 12:00
        openingAndClosingHours.addClosingHours(4, "1400"); // Thursday, 14:00
        openingAndClosingHours.addOpeningHours(4, "1930"); // Thursday, 19:30
        openingAndClosingHours.addClosingHours(4, "2300"); // Thursday, 23:00
        // Friday : 19:00-23:30
        openingAndClosingHours.addOpeningHours(5, "1900"); // Friday, 19:00
        openingAndClosingHours.addClosingHours(5, "2330"); // Friday, 23:30
        // Saturday : 18:30-23:30
        openingAndClosingHours.addOpeningHours(6, "1830"); // Friday, 18:30
        openingAndClosingHours.addClosingHours(6, "2330"); // Friday, 23:30
        // Sunday : 12:00-14:00
        openingAndClosingHours.addOpeningHours(0, "1200"); // Sunday, 12:00
        openingAndClosingHours.addClosingHours(0, "1400"); // Sunday, 14:00

        // Check size Opening Hours
        assertEquals(0, openingAndClosingHours.getMondayOpeningHours().size());
        assertEquals(2, openingAndClosingHours.getTuesdayOpeningHours().size());
        assertEquals(2, openingAndClosingHours.getWednesdayOpeningHours().size());
        assertEquals(2, openingAndClosingHours.getThursdayOpeningHours().size());
        assertEquals(1, openingAndClosingHours.getFridayOpeningHours().size());
        assertEquals(1, openingAndClosingHours.getSaturdayOpeningHours().size());
        assertEquals(1, openingAndClosingHours.getSundayOpeningHours().size());

        // Check size Closing Hours
        assertEquals(0, openingAndClosingHours.getMondayClosingHours().size());
        assertEquals(2, openingAndClosingHours.getTuesdayClosingHours().size());
        assertEquals(2, openingAndClosingHours.getWednesdayClosingHours().size());
        assertEquals(2, openingAndClosingHours.getThursdayClosingHours().size());
        assertEquals(1, openingAndClosingHours.getFridayClosingHours().size());
        assertEquals(1, openingAndClosingHours.getSaturdayClosingHours().size());
        assertEquals(1, openingAndClosingHours.getSundayClosingHours().size());

        // Check values Opening Hours
        // Monday
        assertTrue(openingAndClosingHours.getMondayOpeningHours().isEmpty());
        assertTrue(openingAndClosingHours.getMondayClosingHours().isEmpty());
        // Tuesday
        assertEquals("1200", openingAndClosingHours.getTuesdayOpeningHours().get(0));
        assertEquals("1400", openingAndClosingHours.getTuesdayClosingHours().get(0));
        assertEquals("1930", openingAndClosingHours.getTuesdayOpeningHours().get(1));
        assertEquals("2300", openingAndClosingHours.getTuesdayClosingHours().get(1));
        // Wednesday
        assertEquals("1200", openingAndClosingHours.getWednesdayOpeningHours().get(0));
        assertEquals("1400", openingAndClosingHours.getWednesdayClosingHours().get(0));
        assertEquals("1930", openingAndClosingHours.getWednesdayOpeningHours().get(1));
        assertEquals("2300", openingAndClosingHours.getWednesdayClosingHours().get(1));
        // Thursday
        assertEquals("1200", openingAndClosingHours.getThursdayOpeningHours().get(0));
        assertEquals("1400", openingAndClosingHours.getThursdayClosingHours().get(0));
        assertEquals("1930", openingAndClosingHours.getThursdayOpeningHours().get(1));
        assertEquals("2300", openingAndClosingHours.getThursdayClosingHours().get(1));
        // Friday
        assertEquals("1900", openingAndClosingHours.getFridayOpeningHours().get(0));
        assertEquals("2330", openingAndClosingHours.getFridayClosingHours().get(0));
        // Saturday
        assertEquals("1830", openingAndClosingHours.getSaturdayOpeningHours().get(0));
        assertEquals("2330", openingAndClosingHours.getSaturdayClosingHours().get(0));
        // Sunday
        assertEquals("1200", openingAndClosingHours.getSundayOpeningHours().get(0));
        assertEquals("1400", openingAndClosingHours.getSundayClosingHours().get(0));
    }
}