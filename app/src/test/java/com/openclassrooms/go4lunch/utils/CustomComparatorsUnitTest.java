package com.openclassrooms.go4lunch.utils;

import com.openclassrooms.go4lunch.model.Workmate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * File providing tests to cover methods from @{@link CustomComparators} class file.
 */
@RunWith(JUnit4.class)
public class CustomComparatorsUnitTest {

    /**
     * TEST # 1 : Checks if the static class CustomComparators.WorkmateAZComparator
     * correctly compare the alphabetical order of two Workmates passed as parameters.
     */
    @Test
    public void test_workmate_AZ_comparator() {
        // WorkmateAZComparator only based on "name" field
        Workmate workmateMock1 = Mockito.mock(Workmate.class);
        Mockito.when(workmateMock1.getName()).thenReturn("Nicolas");

        Workmate workmateMock2 = Mockito.mock(Workmate.class);
        Mockito.when(workmateMock2.getName()).thenReturn("Walter");

        Workmate workmateMock3 = Mockito.mock(Workmate.class);
        Mockito.when(workmateMock3.getName()).thenReturn("Alice");

        // Check if comparison between two Workmate objects works
        CustomComparators.WorkmateAZComparator workmateAZComparator =
                new CustomComparators.WorkmateAZComparator();
        assertTrue(workmateAZComparator.compare(workmateMock1, workmateMock2) < 0);
        assertTrue(workmateAZComparator.compare(workmateMock1, workmateMock3) > 0);
        assertEquals(0, workmateAZComparator.compare(workmateMock1, workmateMock1));

        // Check if ordering list of Workmate objects using this Comparator works
        List<Workmate> listWorkmates = Arrays.asList(workmateMock1, workmateMock2, workmateMock3);
        Collections.sort(listWorkmates, new CustomComparators.WorkmateAZComparator());
        assertEquals(workmateMock3, listWorkmates.get(0));
        assertEquals(workmateMock1, listWorkmates.get(1));
        assertEquals(workmateMock2, listWorkmates.get(2));
    }

    /**
     * TEST #2 : Checks if the static method CustomComparators.getTimeDiff() correctly returns the difference
     * between two hours.
     */
    @Test
    public void test_time_diff_calculation() {
        // Start Time : 18h44 / End Time : 18h45 / Start Time - End Time = -1 min
        assertEquals(CustomComparators.getTimeDiff(18, 44,
                18, 45), -1);

        // Start Time : 00h00 / End Time : 12h00 / Start Time - End Time = -720 min
        assertEquals(CustomComparators.getTimeDiff(0, 0,
                12, 0), -720);

        // Start Time : 14h00 / End Time : 3h00 / Start Time - End Time = 660 min
        assertEquals(CustomComparators.getTimeDiff(14, 0,
                3, 0), 660);

        // Start Time : 11h11 / End Time : 10h22 / Start Time - End Time = 49 min
        assertEquals(CustomComparators.getTimeDiff(11, 11,
                10, 22), 49);

        // Start Time : 8h00 / End Time : 8h00 / Start Time - End Time = 0 min
        assertEquals(CustomComparators.getTimeDiff(8, 0,
                8, 0), 0);
    }
}
