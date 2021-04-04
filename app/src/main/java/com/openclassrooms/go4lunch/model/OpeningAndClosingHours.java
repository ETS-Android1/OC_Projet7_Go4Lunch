package com.openclassrooms.go4lunch.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *  Model class defining all closing and opening hours information, wrapped in a
 *  Map<Integer, DaySchedule>.
 */
public class OpeningAndClosingHours {

    // Contains all hours associated with a key value (representing a day of the week)
    private Map<Integer, DaySchedule> schedules = new HashMap<>();

    /**
     * Gets hours for a specified day.
     * @param day : day of the week
     * @return : hours (DaySchedule)
     */
    private DaySchedule getDayOrInit(int day) {
        DaySchedule schedule = schedules.get(day);
        if (schedule == null) {
            schedule = new DaySchedule();
            schedules.put(day, schedule);
        }
        return schedule;
    }

    /**
     * Retrieves specified type of hours for a day.
     * @param type : closing or opening hours
     * @param day : day of the week
     * @return : closing or opening hours for a day
     */
    public ArrayList<String> getHours(ScheduleType type, int day) {
        return getDayOrInit(day).get(type);
    }

    /**
     * Adds a new hours value to schedules HashMap.
     * @param type : closing or opening hours
     * @param day : day of the week
     * @param hours : hours to store
     */
    public void add(ScheduleType type, int day, String hours) {
        DaySchedule schedule = getDayOrInit(day);
        schedule.add(type, hours);
    }
}

