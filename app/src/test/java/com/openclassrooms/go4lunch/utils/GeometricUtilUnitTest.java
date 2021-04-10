package com.openclassrooms.go4lunch.utils;

import com.google.android.gms.maps.model.LatLng;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.assertEquals;

/**
 * File providing tests to cover @{@link GeometricUtils} class file.
 */
@RunWith(JUnit4.class)
public class GeometricUtilUnitTest {

    /**
     * TEST #1 : Checks if the static method getCoordinate() returns a new LatLng
     * object with a precision of 0.01.
     */
    @Test
    public void test_if_get_coordinate_method_return_correct_lat_lng_object() {
        double latitudeRef = 49.059875797433016;
        double longitudeRef = 2.3252438127133095;

        LatLng latLng = GeometricUtils.getCoordinate(latitudeRef, longitudeRef, 500, 500);
        assertEquals(49.064372405462606, latLng.latitude, 0.01);
        assertEquals(2.3252438127133095, latLng.longitude, 0.01);
    }
}
