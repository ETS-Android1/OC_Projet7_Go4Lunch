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
        String ID = "ChIJGz20sdh65kcRCfY0bMPzkVo";
        String NAME = "Franprix";
        String ADDRESS = "80 Avenue Jean Baptiste Clement, Boulogne-Billancourt";
        double LATITUDE = 48.8437898;
        double LONGITUDE = 2.2320991;
        double RATING = 3.4;
        String NUMBER = "01 46 03 93 70";
        String WEBSITE_URI = "https://www.franprix.fr/magasins/5575";
        String PHOTO_REFERENCE = "ATtYBwIJO4zRghygCxwMS7VGe2o-U9VPD_tOktAYaf6LJ3Ay1YQHaFHFmfTAiF9mSzHLk7PKJyf6Ky-kRvgCmTDwj"
                                + "_9aURPdmLLZrK-DAjr1zieyxLKaf1A-lboLIBcY4SornCkG2yPshwrwlFmxzpcvxjv7FoC58N4W-aqaufY6ir8s9tEv";
        int PHOTO_HEIGHT = 4000;
        int PHOTO_WIDTH = 4000;
        boolean SELECTED = true;

        // Initialize a Restaurant object
        Restaurant restaurant1  = new Restaurant(ID, NAME, ADDRESS, LATITUDE, LONGITUDE, RATING);

        // Use setter methods to instantiate others fields
        restaurant1.setPhoneNumber(NUMBER);
        restaurant1.setWebsiteUri(WEBSITE_URI);
        restaurant1.setPhotoReference(PHOTO_REFERENCE);
        restaurant1.setPhotoHeight(PHOTO_HEIGHT);
        restaurant1.setPhotoWidth(PHOTO_WIDTH);
        restaurant1.setSelected(SELECTED);

        // Check if values in Restaurant object fields are correct
        assertEquals(restaurant1.getPlaceId(), ID);
        assertEquals(restaurant1.getName(), NAME);
        assertEquals(restaurant1.getAddress(), ADDRESS);
        assertEquals(restaurant1.getLatitude(), LATITUDE, 0);
        assertEquals(restaurant1.getLongitude(), LONGITUDE, 0);
        assertEquals(restaurant1.getRating(), RATING, 0);
        assertEquals(restaurant1.getPhoneNumber(), NUMBER);
        assertEquals(restaurant1.getWebsiteUri(), WEBSITE_URI);
        assertEquals(restaurant1.getPhotoReference(), PHOTO_REFERENCE);
        assertEquals(restaurant1.getPhotoHeight(), PHOTO_HEIGHT);
        assertEquals(restaurant1.getPhotoWidth(), PHOTO_WIDTH);
        assertEquals(restaurant1.getSelected(), SELECTED);
    }
}
