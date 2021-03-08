package com.openclassrooms.go4lunch.utils;

public class TimeComparator {

    public static int getTimeDiff(int startHour, int startMinutes, int endHour, int endMinutes) {
        int startTime = startHour*60 + startMinutes;
        int endTime = endHour*60 + endMinutes;
        return startTime-endTime;
    }
}
