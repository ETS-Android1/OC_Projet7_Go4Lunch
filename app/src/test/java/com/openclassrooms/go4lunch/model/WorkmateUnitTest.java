package com.openclassrooms.go4lunch.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertEquals;

/**
 * File providing tests to cover @{@link Workmate} class file.
 */
@RunWith(JUnit4.class)
public class WorkmateUnitTest {

    /**
     * TEST #1 : Checks if a @{@link Workmate} object is correctly instantiated and updated.
     */
    @Test
    public void test_workmate_object_creation() {
        String NAME = "Nicolas Moges";
        String EMAIL = "nicolasmoges@gmail.com";
        String PHOTO_URL = "https://graph.facebook.com/10215112752571861/picture";
        String RESTAURANT_SELECTED_ID = "ChIJW3_CtNh65kcRJDaPERLXgGY";
        String RESTAURANT_NAME = "Itouya Cantine Japonaise";
        List<String> LIKED = Arrays.asList("ChIJGz20sdh65kcRCfY0bMPzkVo",
                                           "ChIJW3_CtNh65kcRJDaPERLXgGY",
                                           "ChIJXYZoq9h65kcRuWrPmnFCQUI");
        // Initialize a Restaurant object
        Workmate workmate = new Workmate(NAME, EMAIL, RESTAURANT_SELECTED_ID, PHOTO_URL, RESTAURANT_NAME);

        // Use setter method list of "liked" restaurant by workmate
        workmate.setLiked(LIKED);

        // Check if values in Workmate object fields are correct
        assertEquals(workmate.getName(), NAME);
        assertEquals(workmate.getEmail(), EMAIL);
        assertEquals(workmate.getPhotoUrl(), PHOTO_URL);
        assertEquals(workmate.getRestaurantName(), RESTAURANT_NAME);
        assertEquals(workmate.getRestaurantSelectedID(), RESTAURANT_SELECTED_ID);
        assertEquals(workmate.getLiked().size(), 3);
        assertEquals(workmate.getLiked().get(0), "ChIJGz20sdh65kcRCfY0bMPzkVo");
        assertEquals(workmate.getLiked().get(1), "ChIJW3_CtNh65kcRJDaPERLXgGY");
        assertEquals(workmate.getLiked().get(2), "ChIJXYZoq9h65kcRuWrPmnFCQUI");
    }
}
