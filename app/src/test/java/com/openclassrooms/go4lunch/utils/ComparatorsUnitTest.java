package com.openclassrooms.go4lunch.utils;

import com.openclassrooms.go4lunch.model.Workmate;
import com.openclassrooms.go4lunch.utils.CustomComparators;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * File providing tests to cover methods from @{@link CustomComparators} class file.
 */
@RunWith(JUnit4.class)
public class ComparatorsUnitTest {

    /**
     * TEST # 1 : Checks if the static class CustomComparators.WorkmateAZComparator correctly compare the alphabetical
     * order of two Workmates passed as parameters.
     */
    @Test
    public void test_workmate_AZ_comparator() {
        // WorkmateAZComparator only based on "name" field
        // Other fields not needed for this test (set to "null")
        Workmate workmate1 = new Workmate("Nicolas", null, null, null, null);
        Workmate workmate2 = new Workmate("Walter", null, null, null, null);
        Workmate workmate3 = new Workmate("Alice", null, null, null, null);

        CustomComparators.WorkmateAZComparator workmateAZComparator = new CustomComparators.WorkmateAZComparator();

        assertTrue(workmateAZComparator.compare(workmate1, workmate2) < 0);
        assertTrue(workmateAZComparator.compare(workmate1, workmate3) > 0);
        assertEquals(0, workmateAZComparator.compare(workmate1, workmate1));
    }

    /**
     * TEST #2 : Checks if the static method CustomComparators.getTimeDiff() correctly returns the difference
     * between two hours.
     */
    @Test
    public void test_time_diff_calculation() {
        // Start Time : 18h44 / End Time : 18h45 / Start Time - End Time = -1 min
        assertEquals(CustomComparators.getTimeDiff(18, 44, 18, 45), -1);

        // Start Time : 00h00 / End Time : 12h00 / Start Time - End Time = -720 min
        assertEquals(CustomComparators.getTimeDiff(0, 0, 12, 0), -720);

        // Start Time : 14h00 / End Time : 3h00 / Start Time - End Time = 660 min
        assertEquals(CustomComparators.getTimeDiff(14, 0, 3, 0), 660);

        // Start Time : 11h11 / End Time : 10h22 / Start Time - End Time = 49 min
        assertEquals(CustomComparators.getTimeDiff(11, 11, 10, 22), 49);

        // Start Time : 8h00 / End Time : 8h00 / Start Time - End Time = 0 min
        assertEquals(CustomComparators.getTimeDiff(8, 0, 8, 0), 0);
    }
}
