package com.openclassrooms.go4lunch.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.assertEquals;

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
        openingAndClosingHours.add(ScheduleType.OPEN, 2, "1200");
        openingAndClosingHours.add(ScheduleType.CLOSE, 2, "1400");
        openingAndClosingHours.add(ScheduleType.OPEN, 2, "1930");
        openingAndClosingHours.add(ScheduleType.CLOSE, 2, "2300");
        // Wednesday : 12:00-14:00 / 19:30-23:00
        openingAndClosingHours.add(ScheduleType.OPEN, 3, "1200");
        openingAndClosingHours.add(ScheduleType.CLOSE, 3, "1400");
        openingAndClosingHours.add(ScheduleType.OPEN, 3, "1930");
        openingAndClosingHours.add(ScheduleType.CLOSE, 3, "2300");
        // Thursday : 12:00-14:00 / 19:30-23:00
        openingAndClosingHours.add(ScheduleType.OPEN, 4, "1200");
        openingAndClosingHours.add(ScheduleType.CLOSE, 5, "1400");
        openingAndClosingHours.add(ScheduleType.OPEN, 4, "1930");
        openingAndClosingHours.add(ScheduleType.CLOSE, 4, "2300");
        // Friday : 19:00-23:30
        openingAndClosingHours.add(ScheduleType.OPEN, 5, "1900");
        openingAndClosingHours.add(ScheduleType.CLOSE, 5, "2330");
        // Saturday : 18:30-23:30
        openingAndClosingHours.add(ScheduleType.OPEN, 6, "1830");
        openingAndClosingHours.add(ScheduleType.CLOSE, 6, "2330");
        // Sunday : 12:00-14:00
        openingAndClosingHours.add(ScheduleType.OPEN, 0, "1200");
        openingAndClosingHours.add(ScheduleType.CLOSE, 0, "1400");

        // Check size Opening Hours
        assertEquals(0, openingAndClosingHours.getHours(ScheduleType.OPEN, 1).size());
        assertEquals(2, openingAndClosingHours.getHours(ScheduleType.OPEN, 2).size());
        assertEquals(2, openingAndClosingHours.getHours(ScheduleType.OPEN, 3).size());
        assertEquals(2, openingAndClosingHours.getHours(ScheduleType.OPEN, 4).size());
        assertEquals(1, openingAndClosingHours.getHours(ScheduleType.OPEN, 5).size());
        assertEquals(1, openingAndClosingHours.getHours(ScheduleType.OPEN, 6).size());
        assertEquals(1, openingAndClosingHours.getHours(ScheduleType.OPEN, 0).size());

        // Check size Closing Hours
        assertEquals(0, openingAndClosingHours.getHours(ScheduleType.CLOSE, 1).size());
        assertEquals(2, openingAndClosingHours.getHours(ScheduleType.CLOSE, 2).size());
        assertEquals(2, openingAndClosingHours.getHours(ScheduleType.CLOSE, 3).size());
        assertEquals(2, openingAndClosingHours.getHours(ScheduleType.CLOSE, 4).size());
        assertEquals(1, openingAndClosingHours.getHours(ScheduleType.CLOSE, 5).size());
        assertEquals(1, openingAndClosingHours.getHours(ScheduleType.CLOSE, 6).size());
        assertEquals(1, openingAndClosingHours.getHours(ScheduleType.CLOSE, 0).size());

        // Check values Opening Hours
        // Tuesday
        assertEquals("1200", openingAndClosingHours.getHours(ScheduleType.OPEN, 2).get(0));
        assertEquals("1400", openingAndClosingHours.getHours(ScheduleType.OPEN, 2).get(1));
        assertEquals("1930", openingAndClosingHours.getHours(ScheduleType.CLOSE, 2).get(0));
        assertEquals("2300", openingAndClosingHours.getHours(ScheduleType.CLOSE, 2).get(1));
        // Wednesday
        assertEquals("1200", openingAndClosingHours.getHours(ScheduleType.OPEN, 3).get(0));
        assertEquals("1400", openingAndClosingHours.getHours(ScheduleType.OPEN, 3).get(1));
        assertEquals("1930", openingAndClosingHours.getHours(ScheduleType.CLOSE, 3).get(0));
        assertEquals("2300", openingAndClosingHours.getHours(ScheduleType.CLOSE, 3).get(1));
        // Thursday
        assertEquals("1200", openingAndClosingHours.getHours(ScheduleType.OPEN, 4).get(0));
        assertEquals("1400", openingAndClosingHours.getHours(ScheduleType.OPEN, 4).get(1));
        assertEquals("1930", openingAndClosingHours.getHours(ScheduleType.CLOSE, 4).get(0));
        assertEquals("2300", openingAndClosingHours.getHours(ScheduleType.CLOSE, 4).get(1));
        // Friday
        assertEquals("1900", openingAndClosingHours.getHours(ScheduleType.OPEN, 5).get(0));
        assertEquals("2330", openingAndClosingHours.getHours(ScheduleType.CLOSE, 5).get(1));
        // Saturday
        assertEquals("1830", openingAndClosingHours.getHours(ScheduleType.OPEN, 6).get(0));
        assertEquals("2330", openingAndClosingHours.getHours(ScheduleType.CLOSE, 6).get(1));
        // Sunday
        assertEquals("1200", openingAndClosingHours.getHours(ScheduleType.OPEN, 0).get(0));
        assertEquals("1400", openingAndClosingHours.getHours(ScheduleType.CLOSE, 0).get(1));
    }
}