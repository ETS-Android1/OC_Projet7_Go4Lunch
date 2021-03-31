package com.openclassrooms.go4lunch.database;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.assertEquals;

/**
 * File providing tests to cover @{@link RestaurantData} class file.
 */
@RunWith(JUnit4.class)
public class RestaurantDataUnitTest {

    /**
     * TEST #1 : Checks if a @{@link RestaurantData} object is correctly instantiated.
     */
    @Test
    public void test_restaurant_data_object_creation() {
        String PLACE_ID = "ChIJGz20sdh65kcRCfY0bMPzkVo";
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

        // Initialize a RestaurantData object
        RestaurantData restaurantData = new RestaurantData(PLACE_ID, NAME, ADDRESS, LATITUDE, LONGITUDE,
                                                           RATING, NUMBER, WEBSITE_URI, PHOTO_REFERENCE,
                                                           PHOTO_HEIGHT, PHOTO_WIDTH);

        // Check if values in RestaurantData object fields are correct
        assertEquals(PLACE_ID, restaurantData.getPlaceId());
        assertEquals(NAME, restaurantData.getName());
        assertEquals(ADDRESS, restaurantData.getAddress());
        assertEquals(LATITUDE, restaurantData.getLatitude(), 0);
        assertEquals(LONGITUDE, restaurantData.getLongitude(), 0);
        assertEquals(RATING, restaurantData.getRating(), 0);
        assertEquals(NUMBER, restaurantData.getPhoneNumber());
        assertEquals(WEBSITE_URI, restaurantData.getWebsiteUri());
        assertEquals(PHOTO_REFERENCE, restaurantData.getPhotoReference());
        assertEquals(PHOTO_HEIGHT, restaurantData.getPhotoHeight());
        assertEquals(PHOTO_WIDTH, restaurantData.getPhotoWidth());
    }
}