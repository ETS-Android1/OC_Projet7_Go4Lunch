package com.openclassrooms.go4lunch.utils;

import com.openclassrooms.go4lunch.model.Workmate;
import java.util.Comparator;

/**
 * Contains comparators
 */
public class CustomComparators {

    /**
     * Comparator to sort Workmate name from A to Z
     */
    public static class WorkmateAZComparator implements Comparator<Workmate> {
        @Override
        public int compare(Workmate left, Workmate right) {
            return left.getName().toUpperCase().compareTo(right.getName().toUpperCase());
        }
    }

    /**
     * Comparator to check the time difference between two hours.
     * @param startHour : hours start hour
     * @param startMinutes : minutes start hour
     * @param endHour : hours end hour
     * @param endMinutes : minutes end hour
     * @return : difference
     */
    public static int getTimeDiff(int startHour, int startMinutes, int endHour, int endMinutes) {
        int startTime = startHour*60 + startMinutes;
        int endTime = endHour*60 + endMinutes;
        return startTime-endTime;
    }
}
