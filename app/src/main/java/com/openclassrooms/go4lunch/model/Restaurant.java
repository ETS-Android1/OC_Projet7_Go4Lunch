package com.openclassrooms.go4lunch.model;

import android.graphics.Bitmap;
import android.net.Uri;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.OpeningHours;

/**
 * Model class which define a restaurant, and containing all data retrieve from Place API
 */
public class Restaurant {

    private String id;
    private String name;
    private String address;
    private LatLng latLng;
    private OpeningHours openingHours;
    private String phoneNumber;
    private Uri websiteUri;
    private Bitmap photo;

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

    // Setter methods
    public void setOpeningHours(OpeningHours openingHours) { this.openingHours = openingHours; }

    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public void setWebsiteUri(Uri websiteUri) { this.websiteUri = websiteUri; }

    public void setPhoto(Bitmap photo) { this.photo = photo; }
}
