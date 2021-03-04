package com.openclassrooms.go4lunch.model;

import android.graphics.Bitmap;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import com.google.android.libraries.places.api.model.OpeningHours;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import java.util.List;

/**
 * Model class which define a restaurant, and containing all data retrieve from Place API
 */
@Entity(tableName = "restaurant_table")
public class Restaurant {

    @NonNull
    @PrimaryKey
    private final int item_id;

    @NonNull
    private String place_id;

    @NonNull
    private String name;

    @NonNull
    private String address;

    private double latitude;

    private double longitude;

    @Ignore
    private OpeningHours openingHours;

    private String phoneNumber;

    @Ignore
    private Uri websiteUri;

    private double rating;

    // Photo data
    @Ignore
    private List<PhotoMetadata> photoMetadataList;
    @Ignore
    private Bitmap photo;
    private String photoReference;
    private int photoHeight;
    private int photoWidth;

    public Restaurant(int item_id, @NonNull String place_id, @NonNull String name, @NonNull String address, double latitude, double longitude, double rating) {
        this.item_id = item_id;
        this.place_id = place_id;
        this.name = name;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
        this.rating = rating;
    }

    // Getter methods
    public int getItem_id() { return item_id; }

    public String getPlace_id() { return place_id; }

    public String getName() { return name; }

    public String getAddress() { return address; }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public OpeningHours getOpeningHours() { return openingHours; }

    public String getPhoneNumber() { return phoneNumber; }

    public Uri getWebsiteUri() { return websiteUri; }

    public Bitmap getPhoto() { return photo; }

    public double getRating() { return rating; }

    public List<PhotoMetadata> getPhotoMetadataList() { return this.photoMetadataList; }

    public String getPhotoReference() { return photoReference; }

    public int getPhotoHeight() { return photoHeight; }

    public int getPhotoWidth() { return photoWidth; }

    // Setter methods
    public void setPlaceId(String place_id) {
        this.place_id = place_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setOpeningHours(OpeningHours openingHours) { this.openingHours = openingHours; }

    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public void setWebsiteUri(Uri websiteUri) { this.websiteUri = websiteUri; }

    public void setPhoto(Bitmap photo) { this.photo = photo; }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setPhotoMetadataList(List<PhotoMetadata> photoMetadataList) { this.photoMetadataList = photoMetadataList; }

    public void setPhotoReference(String photoReference) { this.photoReference = photoReference; }

    public void setPhotoHeight(int photoHeight) { this.photoHeight = photoHeight; }

    public void setPhotoWidth(int photoWidth) { this.photoWidth = photoWidth; }
}
