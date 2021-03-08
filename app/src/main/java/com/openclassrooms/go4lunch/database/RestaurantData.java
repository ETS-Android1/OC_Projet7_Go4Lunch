package com.openclassrooms.go4lunch.database;

import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Data to store in a row of the @{@link Go4LunchDatabase} restaurant_table table.
 */
@Entity(tableName = "restaurant_table")
public class RestaurantData {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "item_id") private int itemId;

    @NonNull
    @ColumnInfo(name = "place_id") private final String placeId;

    private String name;

    @NonNull
    private final String address;

    private final double latitude;

    private final double longitude;

    @ColumnInfo(name = "phone_number") private final String phoneNumber;

    @ColumnInfo(name = "website_uri") private final Uri websiteUri;

    private double rating;

    @ColumnInfo(name = "photo_reference") private final String photoReference;

    @ColumnInfo(name = "photo_height") private final int photoHeight;

    @ColumnInfo(name = "photo_width") private final int photoWidth;

    public RestaurantData(@NonNull String placeId, @NonNull String name, @NonNull String address,
                          double latitude, double longitude, double rating,
                          String phoneNumber, Uri websiteUri,
                          String photoReference, int photoHeight, int photoWidth) {
        this.placeId = placeId;
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.rating = rating;
        this.phoneNumber = phoneNumber;
        this.websiteUri = websiteUri;
        this.photoReference = photoReference;
        this.photoHeight = photoHeight;
        this.photoWidth = photoWidth;
    }

    // Getters
    public int getItemId() { return itemId; }

    @NonNull
    public String getPlaceId() { return placeId; }

    public String getName() { return name; }

    @NonNull
    public String getAddress() { return address; }

    public double getLatitude() { return latitude; }

    public double getLongitude() { return longitude; }

    public String getPhoneNumber() { return phoneNumber; }

    public Uri getWebsiteUri() { return websiteUri; }

    public double getRating() { return rating; }

    public String getPhotoReference() { return photoReference; }

    public int getPhotoHeight() { return photoHeight; }

    public int getPhotoWidth() { return photoWidth; }

    // Setters
    public void setItemId(int itemId) { this.itemId = itemId; }

    public void setName(String name) { this.name = name; }

    public void setRating(double rating) { this.rating = rating; }

}
