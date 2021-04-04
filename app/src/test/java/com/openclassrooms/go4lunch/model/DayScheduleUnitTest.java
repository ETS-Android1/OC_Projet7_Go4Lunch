package com.openclassrooms.go4lunch.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.assertEquals;

/**
 * File providing tests to cover @{@link DaySchedule} class file.
 */
@RunWith(JUnit4.class)
public class DayScheduleUnitTest {

    /**
     * TEST #1 : Checks if a @{@link DaySchedule} object is correctly instantiated and updated.
     */
    @Test
    public void test_day_schedule_object_creation() {
        // Initialize object
        DaySchedule daySchedule = new DaySchedule();

        // Add hours
        daySchedule.add(ScheduleType.OPEN, "1200");
        daySchedule.add(ScheduleType.CLOSE, "1400");
        daySchedule.add(ScheduleType.OPEN, "1930");
        daySchedule.add(ScheduleType.CLOSE, "2330");

        // Check values
        assertEquals(2, daySchedule.get(ScheduleType.OPEN).size());
        assertEquals(2, daySchedule.get(ScheduleType.CLOSE).size());
        assertEquals("1200", daySchedule.get(ScheduleType.OPEN).get(0));
        assertEquals("1400", daySchedule.get(ScheduleType.CLOSE).get(0));
        assertEquals("1930", daySchedule.get(ScheduleType.OPEN).get(1));
        assertEquals("2330", daySchedule.get(ScheduleType.CLOSE).get(1));
    }
}
