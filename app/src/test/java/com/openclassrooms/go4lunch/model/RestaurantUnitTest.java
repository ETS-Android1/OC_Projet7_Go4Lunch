package com.openclassrooms.go4lunch.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.assertEquals;

/**
 * File providing tests to cover @{@link Restaurant} class file.
 */
@RunWith(JUnit4.class)
public class RestaurantUnitTest {

    /**
     * TEST #1 : Checks if a @{@link Restaurant} object is correctly instantiated and updated.
     */
    @Test
    public void test_restaurant_object_creation() {
        String PLACE_ID = "ChIJGz20sdh65kcRCfY0bMPzkVo";
        String NAME = "Franprix";
        String ADDRESS = "80 Avenue Jean Baptiste Clement, Boulogne-Billancourt";
        double LATITUDE = 48.8437898;
        double LONGITUDE = 2.2320991;
        double RATING = 3.4;
        String NUMBER = "01 46 03 93 70";
        String WEBSITE_URI = "https://www.franprix.fr/magasins/5575";
        String PHOTO_REFERENCE = "ATtYBwIJO4zRghygCxwMS7VGe2o-U9VPD_tOktAYaf6LJ3Ay1Y" +
                                 "QHaFHFmfTAiF9mSzHLk7PKJyf6Ky-kRvgCmTDwj"
                                + "_9aURPdmLLZrK-DAjr1zieyxLKaf1A-lboLIBcY4SornCkG" +
                                  "2yPshwrwlFmxzpcvxjv7FoC58N4W-aqaufY6ir8s9tEv";
        int PHOTO_HEIGHT = 4000;
        int PHOTO_WIDTH = 4000;
        boolean SELECTED = true;

        // Initialize a Restaurant object
        Restaurant restaurant1  = new Restaurant(PLACE_ID, NAME, ADDRESS,
                                                 LATITUDE, LONGITUDE, RATING);

        // Use setter methods to instantiate others fields
        restaurant1.setPhoneNumber(NUMBER);
        restaurant1.setWebsiteUri(WEBSITE_URI);
        restaurant1.setPhotoReference(PHOTO_REFERENCE);
        restaurant1.setPhotoHeight(PHOTO_HEIGHT);
        restaurant1.setPhotoWidth(PHOTO_WIDTH);
        restaurant1.setSelected(SELECTED);

        // Check if values in Restaurant object fields are correct
        assertEquals(PLACE_ID, restaurant1.getPlaceId());
        assertEquals(NAME, restaurant1.getName());
        assertEquals(ADDRESS, restaurant1.getAddress());
        assertEquals(LATITUDE, restaurant1.getLatitude(), 0);
        assertEquals(LONGITUDE, restaurant1.getLongitude(), 0);
        assertEquals(RATING, restaurant1.getRating(), 0);
        assertEquals(NUMBER, restaurant1.getPhoneNumber());
        assertEquals(WEBSITE_URI, restaurant1.getWebsiteUri());
        assertEquals(PHOTO_REFERENCE, restaurant1.getPhotoReference());
        assertEquals(PHOTO_HEIGHT, restaurant1.getPhotoHeight());
        assertEquals(PHOTO_WIDTH, restaurant1.getPhotoWidth());
        assertEquals(SELECTED, restaurant1.getSelected());
    }
}
