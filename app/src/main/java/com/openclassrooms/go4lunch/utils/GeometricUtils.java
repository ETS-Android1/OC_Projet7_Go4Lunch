package com.openclassrooms.go4lunch.utils;

import com.google.android.gms.maps.model.LatLng;

public class GeometricUtils {

    public static double EARTH_RADIUS = 6371000.0;

    public static LatLng getCoordinate(double latitudeRef, double longitudeRef, long dx, long dy) {
        double newLatitude = latitudeRef + (dy / EARTH_RADIUS) * (180 / Math.PI);
        double newLongitude = longitudeRef + (dx / EARTH_RADIUS) * (180 / Math.PI) / Math.cos(latitudeRef * Math.PI / 180);
        return new LatLng(newLatitude, newLongitude);
    }
}
