package com.openclassrooms.go4lunch.database;

import androidx.room.Embedded;
import androidx.room.Relation;
import java.util.List;

/**
 * This class defines a One-to-one relationship between a row of the @{@link Go4LunchDatabase}
 * restaurant_table table and a row of the hours_table table.
 */
public class RestaurantAndHoursData {

    @Embedded
    public RestaurantData restaurantData;

    @Relation(parentColumn = "place_id", entityColumn = "restaurant_id")
    public List<HoursData> hoursData;
}
