package com.openclassrooms.go4lunch.model;

import java.util.ArrayList;

/**
 * This class contains all closing/opening hours information of a Restaurant, for each day of
 * the week.
 */
public class OpeningAndClosingHours {

    public  OpeningAndClosingHours() {}

    // Closing hours
    private final ArrayList<String> mondayClosingHours = new ArrayList<>();
    private final ArrayList<String> tuesdayClosingHours = new ArrayList<>();
    private final ArrayList<String> wednesdayClosingHours = new ArrayList<>();
    private final ArrayList<String> thursdayClosingHours = new ArrayList<>();
    private final ArrayList<String> fridayClosingHours = new ArrayList<>();
    private final ArrayList<String> saturdayClosingHours = new ArrayList<>();
    private final ArrayList<String> sundayClosingHours = new ArrayList<>();

    // Opening hours
    private final ArrayList<String> mondayOpeningHours = new ArrayList<>();
    private final ArrayList<String> tuesdayOpeningHours = new ArrayList<>();
    private final ArrayList<String> wednesdayOpeningHours = new ArrayList<>();
    private final ArrayList<String> thursdayOpeningHours = new ArrayList<>();
    private final ArrayList<String> fridayOpeningHours = new ArrayList<>();
    private final ArrayList<String> saturdayOpeningHours = new ArrayList<>();
    private final ArrayList<String> sundayOpeningHours = new ArrayList<>();

    /**
     * This method adds new opening hours information in the associated day list.
     * @param day : day
     * @param hours : opening hours in String format
     */
    public void addOpeningHours(int day, String hours) {
        switch (day) {
            case 0:
                sundayOpeningHours.add(hours);
                break;
            case 1:
                mondayOpeningHours.add(hours);
                break;
            case 2:
                tuesdayOpeningHours.add(hours);
                break;
            case 3:
                wednesdayOpeningHours.add(hours);
                break;
            case 4:
                thursdayOpeningHours.add(hours);
                break;
            case 5:
                fridayOpeningHours.add(hours);
                break;
            case 6:
                saturdayOpeningHours.add(hours);
                break;
        }
    }

    /**
     * This method adds new closing hours information in the associated day list.
     * @param day : day
     * @param hours : closing hours in String format
     */
    public void addClosingHours(int day, String hours) {
        switch (day) {
            case 0:
                sundayClosingHours.add(hours);
                break;
            case 1:
                mondayClosingHours.add(hours);
                break;
            case 2:
                tuesdayClosingHours.add(hours);
                break;
            case 3:
                wednesdayClosingHours.add(hours);
                break;
            case 4:
                thursdayClosingHours.add(hours);
                break;
            case 5:
                fridayClosingHours.add(hours);
                break;
            case 6:
                saturdayClosingHours.add(hours);
                break;
        }
    }

    // Getter methods
    public ArrayList<String> getMondayClosingHours() { return mondayClosingHours; }

    public ArrayList<String> getTuesdayClosingHours() { return tuesdayClosingHours; }

    public ArrayList<String> getWednesdayClosingHours() { return wednesdayClosingHours; }

    public ArrayList<String> getThursdayClosingHours() { return thursdayClosingHours; }

    public ArrayList<String> getFridayClosingHours() { return fridayClosingHours; }

    public ArrayList<String> getSaturdayClosingHours() { return saturdayClosingHours; }

    public ArrayList<String> getSundayClosingHours() { return sundayClosingHours; }

    public ArrayList<String> getMondayOpeningHours() { return mondayOpeningHours; }

    public ArrayList<String> getTuesdayOpeningHours() { return tuesdayOpeningHours; }

    public ArrayList<String> getWednesdayOpeningHours() { return wednesdayOpeningHours; }

    public ArrayList<String> getThursdayOpeningHours() { return thursdayOpeningHours; }

    public ArrayList<String> getFridayOpeningHours() { return fridayOpeningHours; }

    public ArrayList<String> getSaturdayOpeningHours() { return saturdayOpeningHours; }

    public ArrayList<String> getSundayOpeningHours() { return sundayOpeningHours; }

}

