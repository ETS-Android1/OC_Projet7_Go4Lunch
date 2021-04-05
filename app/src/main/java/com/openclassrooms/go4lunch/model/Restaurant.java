package com.openclassrooms.go4lunch.model;

import androidx.annotation.NonNull;

/**
 * Model class defining a restaurant, and containing all data retrieved from a Place API search,
 * or from the "restaurant_table" table of the @{@link com.openclassrooms.go4lunch.database.Go4LunchDatabase}
 * database instance.
 */
public class Restaurant {

    private final String placeId;

    @NonNull
    private String name;

    @NonNull
    private final String address;

    private final double latitude;

    private final double longitude;

    private OpeningAndClosingHours openingAndClosingHours;

    private String phoneNumber;

    private String websiteUri;

    private double rating;

    private String photoReference;

    private int photoHeight;

    private int photoWidth;

    private boolean selected;

    public Restaurant(@NonNull String placeId, @NonNull String name, @NonNull String address,
                      double latitude, double longitude, double rating) {
        this.placeId = placeId;
        this.name = name;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
        this.rating = rating;
        this.selected = false;
    }

    // Getter methods
    public String getPlaceId() { return placeId; }

    @NonNull
    public String getName() { return name; }

    @NonNull
    public String getAddress() { return address; }

    public OpeningAndClosingHours getOpeningAndClosingHours() { return openingAndClosingHours; }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getPhoneNumber() { return phoneNumber; }

    public String getWebsiteUri() { return websiteUri; }

    public double getRating() { return rating; }

    public String getPhotoReference() { return photoReference; }

    public int getPhotoHeight() { return photoHeight; }

    public int getPhotoWidth() { return photoWidth; }

    public boolean getSelected() { return selected; }

    // Setter methods

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public void setOpeningAndClosingHours(OpeningAndClosingHours openingAndClosingHours) {
        this.openingAndClosingHours = openingAndClosingHours;
    }

    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public void setWebsiteUri(String websiteUri) { this.websiteUri = websiteUri; }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public void setPhotoReference(String photoReference) { this.photoReference = photoReference; }

    public void setPhotoHeight(int photoHeight) { this.photoHeight = photoHeight; }

    public void setPhotoWidth(int photoWidth) { this.photoWidth = photoWidth; }

    public void setSelected(boolean selected) { this.selected = selected; }
}
