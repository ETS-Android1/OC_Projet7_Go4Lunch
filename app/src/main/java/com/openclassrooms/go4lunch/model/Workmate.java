package com.openclassrooms.go4lunch.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Model class which defining information for a workmate, and containing all data retrieved from an associated
 * Document in the Firestore database.
 */
public class Workmate {

    private String name;

    private final String email;

    private final String photoUrl;

    // ID of a selected restaurant
    private final String restaurantSelectedID;

    // Name of a selected restaurant
    private final String restaurantName;

    // Contains list of "liked" restaurants by a workmate
    private final List<String> liked;

    public Workmate(String name, String email, String restaurantSelectedID, String photoUrl, String restaurantName) {
        this.name = name;
        this.email = email;
        this.photoUrl = photoUrl;
        this.restaurantSelectedID = restaurantSelectedID;
        this.restaurantName = restaurantName;
        this.liked = new ArrayList<>();
    }

    // Getter methods
    public String getName() { return name; }

    public String getRestaurantSelectedID() { return restaurantSelectedID; }

    public String getPhotoUrl() { return photoUrl; }

    public String getRestaurantName() { return restaurantName; }

    // Setter methods
    public void setName(String name) { this.name = name; }
}

