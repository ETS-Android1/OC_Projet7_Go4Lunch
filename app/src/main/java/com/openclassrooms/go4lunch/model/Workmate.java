package com.openclassrooms.go4lunch.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Model class which defines an employees.
 */
public class Workmate {

    private String name;

    private String email;

    private String photoUrl;

    // ID of a selected restaurant
    private String restaurantSelectedID;

    // Name of a selected restaurant
    private String restaurantName;

    private List<String> liked;

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

    public String getEmail() { return email; }

    public String getRestaurantSelectedID() { return restaurantSelectedID; }

    public String getPhotoUrl() { return photoUrl; }

    public String getRestaurantName() { return restaurantName; }

    public List<String> getLiked() {
        return liked;
    }

    // Setter methods
    public void setName(String name) { this.name = name; }

    public void setEmail(String email) { this.email = email; }

    public void setRestaurantSelectedID(String restaurantSelectedID) {
        this.restaurantSelectedID = restaurantSelectedID;
    }

    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public void setLiked(List<String> liked) {
        this.liked = liked;
    }
}

