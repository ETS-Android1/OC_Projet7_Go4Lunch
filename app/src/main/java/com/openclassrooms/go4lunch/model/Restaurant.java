package com.openclassrooms.go4lunch.model;

import android.graphics.Bitmap;
import android.net.Uri;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.OpeningHours;
import com.google.android.libraries.places.api.model.PhotoMetadata;

import java.util.List;

/**
 * Model class which define a restaurant, and containing all data retrieve from Place API
 */
public class Restaurant {

    private final String id;
    private final String name;
    private final String address;
    private final LatLng latLng;
    private OpeningHours openingHours;
    private String phoneNumber;
    private Uri websiteUri;
    private List<PhotoMetadata> photoMetadataList;
    private Bitmap photo;
    private Double rating;

    public Restaurant(String id, String name, String address, LatLng latLng) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.latLng = latLng;
    }

    // Getter methods
    public String getId() { return id; }

    public String getName() { return name; }

    public String getAddress() { return address; }

    public LatLng getLatLng() { return latLng; }

    public OpeningHours getOpeningHours() { return openingHours; }

    public String getPhoneNumber() { return phoneNumber; }

    public Uri getWebsiteUri() { return websiteUri; }

    public Bitmap getPhoto() { return photo; }

    public Double getRating() { return rating; }

    public List<PhotoMetadata> getPhotoMetadataList() { return this.photoMetadataList; }

    // Setter methods
    public void setOpeningHours(OpeningHours openingHours) { this.openingHours = openingHours; }

    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public void setWebsiteUri(Uri websiteUri) { this.websiteUri = websiteUri; }

    public void setPhoto(Bitmap photo) { this.photo = photo; }

    public void setRating(Double rating) { this.rating = rating; }

    public void setPhotoMetadataList(List<PhotoMetadata> photoMetadataList) { this.photoMetadataList = photoMetadataList; }
}