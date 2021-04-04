package com.openclassrooms.go4lunch.model;

import java.util.ArrayList;

/**
 * Model class containing the list of hours (closing and opening) for a day
 */
public class DaySchedule {

    // List of opening hours
    private final ArrayList<String> open;

    // List of closing hours
    private final ArrayList<String> closed;

    public DaySchedule() {
        open = new ArrayList<>();
        closed = new ArrayList<>();
    }

    /**
     * Add a new hour to "open" or "closed" ArrayList field according to the ScheduleType value.
     * @param type : Type of hours
     * @param hour : Hours value
     */
    public void add(ScheduleType type, String hour) {
        if (type == ScheduleType.CLOSE) closed.add(hour);
        else open.add(hour);
    }

    /**
     * Return "open" or "closed" ArrayList according to the ScheduleType value.
     * @param type : Type of hours
     * @return : List of hours
     */
    public ArrayList<String> get(ScheduleType type) {
        if (type == ScheduleType.CLOSE) return closed;
        else return open;
    }
}
