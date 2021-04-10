package com.openclassrooms.go4lunch.utils;

import com.google.android.gms.maps.model.LatLng;
import com.openclassrooms.go4lunch.utils.mapping.RestaurantMarkerItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.assertEquals;

/**
 * File providing tests to cover @{@link RestaurantMarkerItem}
 * class file.
 */
@RunWith(JUnit4.class)
public class RestaurantMarkerItemUnitTest {

    /**
     * TEST #1 : Checks if a @{@link RestaurantMarkerItem} object is correctly instantiated and updated.
     */
    @Test
    public void test_restaurant_marker_item_correctly_instantiated() {
        LatLng LATLNG = new LatLng(48.8437898, 2.2320991);
        String TITLE = "Franprix";
        String SNIPPET = "";
        boolean TYPE = true;
        int INDICE = 1;

        RestaurantMarkerItem restaurantMarkerItem = new RestaurantMarkerItem(
                        LATLNG, TITLE, SNIPPET, TYPE, INDICE
        );

        assertEquals(LATLNG.latitude, restaurantMarkerItem.getPosition().latitude, 0);
        assertEquals(LATLNG.longitude, restaurantMarkerItem.getPosition().longitude, 0);
        assertEquals(TITLE, restaurantMarkerItem.getTitle());
        assertEquals(SNIPPET, restaurantMarkerItem.getSnippet());
        assertEquals(TYPE, restaurantMarkerItem.getType());
        assertEquals(INDICE, restaurantMarkerItem.getIndice());
    }
}
