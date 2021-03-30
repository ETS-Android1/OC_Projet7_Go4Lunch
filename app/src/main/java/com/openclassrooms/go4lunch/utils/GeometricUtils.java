package com.openclassrooms.go4lunch.utils;

import com.google.android.gms.maps.model.LatLng;

/**
 * Class providing coordinates point, defined by a LatLng object.
 */
public class GeometricUtils {

    public static double EARTH_RADIUS = 6371000.0;

    /**
     * Provides new coordinates for a translation of "dx" meters and "dy" meters
     * @param latitudeRef : Old latitue
     * @param longitudeRef : Old longitude
     * @param dx : Translation value
     * @param dy : Translation value
     * @return : New LatLng object
     */
    public static LatLng getCoordinate(double latitudeRef, double longitudeRef, long dx, long dy) {
        double newLatitude = latitudeRef + (dy / EARTH_RADIUS) * (180 / Math.PI);
        double newLongitude = longitudeRef + (dx / EARTH_RADIUS) * (180 / Math.PI) / Math.cos(latitudeRef * Math.PI / 180);
        return new LatLng(newLatitude, newLongitude);
    }
}
