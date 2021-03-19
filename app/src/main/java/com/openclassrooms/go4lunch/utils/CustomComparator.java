package com.openclassrooms.go4lunch.utils;

import com.openclassrooms.go4lunch.model.Workmate;
import java.util.Comparator;

public class CustomComparator {

    /**
     * Comparator to sort Workmate name from A to Z
     */
    public static class WorkmateAZComparator implements Comparator<Workmate> {
        @Override
        public int compare(Workmate left, Workmate right) {
            return left.getName().toUpperCase().compareTo(right.getName().toUpperCase());
        }
    }
}
