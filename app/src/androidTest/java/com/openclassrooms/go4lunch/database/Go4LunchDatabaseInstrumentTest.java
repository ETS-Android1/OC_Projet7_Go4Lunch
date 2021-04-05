package com.openclassrooms.go4lunch.database;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * File providing tests to cover DAO class files.
 */
@RunWith(JUnit4.class)
public class Go4LunchDatabaseInstrumentTest {

    private Go4LunchDatabase database;
    private RestaurantData restaurantDataToInsert1;
    private RestaurantData restaurantDataToInsert2;
    private RestaurantData restaurantDataToInsert3;
    private HoursData hoursDataToInsert1;
    private HoursData hoursDataToInsert2;
    private HoursData hoursDataToInsert3;
    private HoursData hoursDataToInsert4;
    private HoursData hoursDataToInsert5;
    private HoursData hoursDataToInsert6;
    private HoursData hoursDataToInsert7;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setup() {
        this.database = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),
                                                                    Go4LunchDatabase.class).build();
        initializeRestaurantObjects();
    }

    @After
    public void closeDatabase() {
        database.close();
    }

    private void initializeRestaurantObjects() {
        // Initialize Data to insert in database
        restaurantDataToInsert1 = new RestaurantData(
                FakeDataTest.RESTAURANT_1_PLACE_ID,
                FakeDataTest.RESTAURANT_1_NAME,
                FakeDataTest.RESTAURANT_1_ADDRESS,
                FakeDataTest.RESTAURANT_1_LATITUDE,
                FakeDataTest.RESTAURANT_1_LONGITUDE,
                FakeDataTest.RESTAURANT_1_RATING,
                FakeDataTest.RESTAURANT_1_NUMBER,
                FakeDataTest.RESTAURANT_1_WEBSITE_URI,
                FakeDataTest.RESTAURANT_1_PHOTO_REFERENCE,
                FakeDataTest.RESTAURANT_1_PHOTO_HEIGHT,
                FakeDataTest.RESTAURANT_1_PHOTO_WIDTH
        );

        restaurantDataToInsert2 = new RestaurantData(
                FakeDataTest.RESTAURANT_2_PLACE_ID,
                FakeDataTest.RESTAURANT_2_NAME,
                FakeDataTest.RESTAURANT_2_ADDRESS,
                FakeDataTest.RESTAURANT_2_LATITUDE,
                FakeDataTest.RESTAURANT_2_LONGITUDE,
                FakeDataTest.RESTAURANT_2_RATING,
                FakeDataTest.RESTAURANT_2_NUMBER,
                FakeDataTest.RESTAURANT_2_WEBSITE_URI,
                FakeDataTest.RESTAURANT_2_PHOTO_REFERENCE,
                FakeDataTest.RESTAURANT_2_PHOTO_HEIGHT,
                FakeDataTest.RESTAURANT_2_PHOTO_WIDTH
        );

        restaurantDataToInsert3 = new RestaurantData(
                FakeDataTest.RESTAURANT_3_PLACE_ID,
                FakeDataTest.RESTAURANT_3_NAME,
                FakeDataTest.RESTAURANT_3_ADDRESS,
                FakeDataTest.RESTAURANT_3_LATITUDE,
                FakeDataTest.RESTAURANT_3_LONGITUDE,
                FakeDataTest.RESTAURANT_3_RATING,
                FakeDataTest.RESTAURANT_3_NUMBER,
                FakeDataTest.RESTAURANT_3_WEBSITE_URI,
                FakeDataTest.RESTAURANT_3_PHOTO_REFERENCE,
                FakeDataTest.RESTAURANT_3_PHOTO_HEIGHT,
                FakeDataTest.RESTAURANT_3_PHOTO_WIDTH
        );

        hoursDataToInsert1 = new HoursData(FakeDataTest.MONDAY_CLOSING_HOURS_RESTAURANT_1,
                FakeDataTest.MONDAY_OPENING_HOURS_RESTAURANT_1,
                FakeDataTest.RESTAURANT_1_PLACE_ID);
        hoursDataToInsert2 = new HoursData(FakeDataTest.TUESDAY_CLOSING_HOURS_RESTAURANT_1,
                FakeDataTest.TUESDAY_OPENING_HOURS_RESTAURANT_1,
                FakeDataTest.RESTAURANT_1_PLACE_ID);
        hoursDataToInsert3 = new HoursData(FakeDataTest.WEDNESDAY_CLOSING_HOURS_RESTAURANT_1,
                FakeDataTest.WEDNESDAY_OPENING_HOURS_RESTAURANT_1,
                FakeDataTest.RESTAURANT_1_PLACE_ID);
        hoursDataToInsert4 = new HoursData(FakeDataTest.THURSDAY_CLOSING_HOURS_RESTAURANT_1,
                FakeDataTest.THURSDAY_OPENING_HOURS_RESTAURANT_1,
                FakeDataTest.RESTAURANT_1_PLACE_ID);
        hoursDataToInsert5 = new HoursData(FakeDataTest.FRIDAY_CLOSING_HOURS_RESTAURANT_1,
                FakeDataTest.FRIDAY_OPENING_HOURS_RESTAURANT_1,
                FakeDataTest.RESTAURANT_1_PLACE_ID);
        hoursDataToInsert6 = new HoursData(FakeDataTest.SATURDAY_CLOSING_HOURS_RESTAURANT_1,
                FakeDataTest.SATURDAY_OPENING_HOURS_RESTAURANT_1,
                FakeDataTest.RESTAURANT_1_PLACE_ID);
        hoursDataToInsert7 = new HoursData(FakeDataTest.SUNDAY_CLOSING_HOURS_RESTAURANT_1,
                FakeDataTest.SUNDAY_OPENING_HOURS_RESTAURANT_1,
                FakeDataTest.RESTAURANT_1_PLACE_ID);
    }
    /**
     * TEST #1 : Check RestaurantDao insertion query.
     */
    @Test
    public void test_insert_restaurant_data_in_database() {
        // Insert Data
        database.restaurantDao().insertRestaurantData(restaurantDataToInsert1);

        // Retrieve Data to check if data was correctly stored
        RestaurantData restaurantDataRead1 = database.restaurantDao().getRestaurantData(1);
        assertNotNull(restaurantDataRead1);

        // Check values
        assertEquals("ChIJGz20sdh65kcRCfY0bMPzkVo", restaurantDataRead1.getPlaceId());
        assertEquals(FakeDataTest.RESTAURANT_1_NAME, restaurantDataRead1.getName());
        assertEquals(FakeDataTest.RESTAURANT_1_ADDRESS, restaurantDataRead1.getAddress());
        assertEquals(FakeDataTest.RESTAURANT_1_LATITUDE, restaurantDataRead1.getLatitude(), 0);
        assertEquals(FakeDataTest.RESTAURANT_1_LONGITUDE, restaurantDataRead1.getLongitude(), 0);
        assertEquals(FakeDataTest.RESTAURANT_1_RATING, restaurantDataRead1.getRating(), 0);
        assertEquals(FakeDataTest.RESTAURANT_1_NUMBER, restaurantDataRead1.getPhoneNumber());
        assertEquals(FakeDataTest.RESTAURANT_1_WEBSITE_URI, restaurantDataRead1.getWebsiteUri());
        assertEquals(FakeDataTest.RESTAURANT_1_PHOTO_REFERENCE, restaurantDataRead1.getPhotoReference());
        assertEquals(FakeDataTest.RESTAURANT_1_PHOTO_HEIGHT, restaurantDataRead1.getPhotoHeight());
        assertEquals(FakeDataTest.RESTAURANT_1_PHOTO_WIDTH, restaurantDataRead1.getPhotoWidth());
    }

    /**
     * TEST #2 : Check RestaurantDao deletion query.
     */
    @Test
    public void test_delete_all_restaurant_data_in_database() {
        // Insert Data
        database.restaurantDao().insertRestaurantData(restaurantDataToInsert1);
        database.restaurantDao().insertRestaurantData(restaurantDataToInsert2);
        database.restaurantDao().insertRestaurantData(restaurantDataToInsert3);

        // Remove All data from database
        int nb_element_deleted = database.restaurantDao().deleteAllRestaurantsData();
        assertEquals(3, nb_element_deleted);

        // Check if database is empty
        nb_element_deleted = database.restaurantDao().deleteAllRestaurantsData();
        assertEquals(0, nb_element_deleted);
    }

    /**
     * TEST #3 : Check HoursDao insertion query.
     */
    @Test
    public void test_insert_hours_data_in_database() {
        // Initialize Data to insert
        HoursData hoursDataToInsert = new HoursData(FakeDataTest.MONDAY_CLOSING_HOURS_RESTAURANT_1,
                                                    FakeDataTest.MONDAY_OPENING_HOURS_RESTAURANT_1,
                                                               FakeDataTest.RESTAURANT_1_PLACE_ID);

        // Insert Data
        database.hoursDao().insertHoursData(hoursDataToInsert);

        // Retrieve Data to check if data was correctly stored
        HoursData hoursDataRead = database.hoursDao().getHoursData(1);
        assertNotNull(hoursDataRead);

        // Check values
        assertEquals(FakeDataTest.MONDAY_CLOSING_HOURS_RESTAURANT_1.getDay(),
                                   hoursDataRead.getClosingHours().getDay());
        assertEquals(FakeDataTest.MONDAY_CLOSING_HOURS_RESTAURANT_1.getTime(),
                                   hoursDataRead.getClosingHours().getTime());
        assertEquals(FakeDataTest.MONDAY_OPENING_HOURS_RESTAURANT_1.getDay(),
                                   hoursDataRead.getOpeningHours().getDay());
        assertEquals(FakeDataTest.MONDAY_OPENING_HOURS_RESTAURANT_1.getTime(),
                                   hoursDataRead.getOpeningHours().getTime());
        assertEquals(FakeDataTest.RESTAURANT_1_PLACE_ID, hoursDataRead.getRestaurantId());
    }

    /**
     * TEST #4 : Check HoursDao deletion query.
     */
    @Test
    public void test_delete_all_hours_data_in_database() {
        // Insert Data
        database.hoursDao().insertHoursData(hoursDataToInsert1);
        database.hoursDao().insertHoursData(hoursDataToInsert2);
        database.hoursDao().insertHoursData(hoursDataToInsert3);
        database.hoursDao().insertHoursData(hoursDataToInsert4);
        database.hoursDao().insertHoursData(hoursDataToInsert5);
        database.hoursDao().insertHoursData(hoursDataToInsert6);
        database.hoursDao().insertHoursData(hoursDataToInsert7);

        // Remove All data from database
        int nb_element_deleted = database.hoursDao().deleteAllHoursData();
        assertEquals(7, nb_element_deleted);

        // Check if database is empty
        nb_element_deleted = database.hoursDao().deleteAllHoursData();
        assertEquals(0, nb_element_deleted);
    }

    /**
     * TEST #5 : Check HoursDao deletion query.
     */
    @Test
    public void test_restaurant_and_hours_dao_interface() {
        // Insert Data in database
        database.restaurantDao().insertRestaurantData(restaurantDataToInsert1);
        database.hoursDao().insertHoursData(hoursDataToInsert1);
        database.hoursDao().insertHoursData(hoursDataToInsert2);
        database.hoursDao().insertHoursData(hoursDataToInsert3);
        database.hoursDao().insertHoursData(hoursDataToInsert4);
        database.hoursDao().insertHoursData(hoursDataToInsert5);
        database.hoursDao().insertHoursData(hoursDataToInsert6);
        database.hoursDao().insertHoursData(hoursDataToInsert7);

        // Define observer
        Observer<List<RestaurantAndHoursData>> observer = restaurantAndHoursData -> {
            assertNotNull(restaurantAndHoursData);
            assertEquals(1, restaurantAndHoursData.size());
            assertNotNull(restaurantAndHoursData.get(0).restaurantData);
            assertNotNull(restaurantAndHoursData.get(0).hoursData);
        };

        // Add observer to LiveData loadAllRestaurantsWithHours() result
        database.restaurantAndHoursDao().loadAllRestaurantsWithHours().observeForever(observer);
    }
}
