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
    private final Double rating;


    // -------- TEST -------------
    // Photo data
    private String photoReference;
    private int photoHeight;
    private int photoWidth;
    // ---------------------------
    public Restaurant(String id, String name, String address, LatLng latLng, double rating) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.latLng = latLng;
        this.rating = rating;
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

    // -------- TEST -------------
    public String getPhotoReference() { return photoReference; }

    public int getPhotoHeight() { return photoHeight; }

    public int getPhotoWidth() { return photoWidth; }
    // ---------------------------

    // Setter methods
    public void setOpeningHours(OpeningHours openingHours) { this.openingHours = openingHours; }

    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public void setWebsiteUri(Uri websiteUri) { this.websiteUri = websiteUri; }

    public void setPhoto(Bitmap photo) { this.photo = photo; }

    public void setPhotoMetadataList(List<PhotoMetadata> photoMetadataList) { this.photoMetadataList = photoMetadataList; }

    // -------- TEST -------------
    public void setPhotoReference(String photoReference) { this.photoReference = photoReference; }

    public void setPhotoHeight(int photoHeight) { this.photoHeight = photoHeight; }

    public void setPhotoWidth(int photoWidth) { this.photoWidth = photoWidth; }
    // ---------------------------
}
