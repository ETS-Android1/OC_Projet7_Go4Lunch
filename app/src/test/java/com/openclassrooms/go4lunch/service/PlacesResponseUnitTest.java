package com.openclassrooms.go4lunch.service;

import com.openclassrooms.go4lunch.service.places.response.places.Geometry;
import com.openclassrooms.go4lunch.service.places.response.places.LocationRestaurant;
import com.openclassrooms.go4lunch.service.places.response.places.Photo;
import com.openclassrooms.go4lunch.service.places.response.places.PlaceResponse;
import com.openclassrooms.go4lunch.service.places.response.places.ResultPlaces;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;

/**
 * File providing tests to cover the following classes :
 * - @{@link Geometry}
 * - @{@link LocationRestaurant}
 * - @{@link Photo}
 * - @{@link PlaceResponse}
 */
@RunWith(JUnit4.class)
public class PlacesResponseUnitTest {

    /**
     * TEST #1 : Checks if a @{@link Geometry} object is correctly instantiated.
     */
    @Test
    public void test_geometry_object_creation() {
        LocationRestaurant locationRestaurant = Mockito.mock(LocationRestaurant.class);
        double latitude = 48.8437898;
        double longitude = 2.2320991;
        Mockito.when(locationRestaurant.getLat()).thenReturn(latitude);
        Mockito.when(locationRestaurant.getLng()).thenReturn(longitude);

        Geometry geometry = new Geometry(locationRestaurant);
        assertEquals(latitude, geometry.getLocation().getLat(), 0);
        assertEquals(longitude, geometry.getLocation().getLng(), 0);
    }

    /**
     * TEST #2 : Checks if a @{@link LocationRestaurant} object is correctly instantiated.
     */
    @Test
    public void test_location_restaurant_object_creation() {
        double latitude = 48.8437898;
        double longitude = 2.2320991;
        LocationRestaurant locationRestaurant = new LocationRestaurant(latitude, longitude);
        assertEquals(latitude, locationRestaurant.getLat(), 0);
        assertEquals(longitude, locationRestaurant.getLng(), 0);
    }

    /**
     * TEST #3 : Checks if a @{@link Photo} object is correctly instantiated.
     */
    @Test
    public void test_photo_object_creation() {
        int height = 4000;
        int width = 4000;
        String photo_reference =  "ATtYBwIJO4zRghygCxwMS7VGe2o-U9VPD_" +
                                  "tOktAYaf6LJ3Ay1YQHaFHFmfTAiF9mSzHL" +
                                  "k7PKJyf6Ky-kRvgCmTDwj_9aURPdmLLZrK-" +
                                  "DAjr1zieyxLKaf1A-lboLIBcY4SornCkG2yP" +
                                  "shwrwlFmxzpcvxjv7FoC58N4W-aqaufY6ir8s9tEv";

        Photo photo = new Photo(height, photo_reference, width);
        assertEquals(height, photo.getHeight());
        assertEquals(photo_reference, photo.getPhoto_reference());
        assertEquals(width, photo.getWidth());
    }

    /**
     * TEST #4 : Checks if a @{@link ResultPlaces} object is correctly instantiated.
     */
    @Test
    public void test_result_places_object_creation() {
        Photo photo = Mockito.mock(Photo.class);
        Geometry geometry = Mockito.mock(Geometry.class);
        LocationRestaurant locationRestaurant = Mockito.mock(LocationRestaurant.class);

        String name = "Franprix";
        String placeId = "ChIJGz20sdh65kcRCfY0bMPzkVo";
        String address = "80 Avenue Jean Baptiste Clement, Boulogne-Billancourt";
        double latitude = 48.8437898;
        double longitude = 2.2320991;
        double rating = 3.4;
        List<Photo> listPhotos = new ArrayList<>();
        int width = 3000;
        int height = 4000;
        String photoReference = "ATtYBwIsyLWYtnORpDvrsQzJlsBzH7t3xU9dSHF2NH27h" +
                                "3Lv5D_x-3rNYNu1khai5zkveaqKMZ0f4aTKlFE7bOVTW_" +
                                "ncgCra-1xPU2PKF5FS8MLsXHomX3c5HDVI_JG-TmFtkxr" +
                                "logc37A4JbW1PtbfnjB6plsYB0Rtaaq2OJkM9yh9W73nv";

        listPhotos.add(photo);

        ResultPlaces resultPlaces = new ResultPlaces(name, placeId, address, rating,
                                                                              geometry, listPhotos);

        Mockito.when(geometry.getLocation()).thenReturn(locationRestaurant);
        Mockito.when(locationRestaurant.getLat()).thenReturn(latitude);
        Mockito.when(locationRestaurant.getLng()).thenReturn(longitude);
        Mockito.when(photo.getWidth()).thenReturn(width);
        Mockito.when(photo.getHeight()).thenReturn(height);
        Mockito.when(photo.getPhoto_reference()).thenReturn(photoReference);

        assertEquals(name, resultPlaces.getName());
        assertEquals(address, resultPlaces.getVicinity());
        assertEquals(rating, resultPlaces.getRating(), 0);
        assertEquals(placeId, resultPlaces.getPlace_id());
        assertEquals(latitude, resultPlaces.getGeometry().getLocation().getLat(), 0);
        assertEquals(longitude, resultPlaces.getGeometry().getLocation().getLng(), 0);
        assertEquals(photoReference, resultPlaces.getPhotos().get(0).getPhoto_reference());
        assertEquals(height, resultPlaces.getPhotos().get(0).getHeight());
        assertEquals(width, resultPlaces.getPhotos().get(0).getWidth());
    }
}
