package com.openclassrooms.go4lunch.repositories;

import android.net.Uri;
import android.util.Log;
import androidx.lifecycle.LiveData;
import com.openclassrooms.go4lunch.dao.HoursDao;
import com.openclassrooms.go4lunch.dao.RestaurantAndHoursDao;
import com.openclassrooms.go4lunch.dao.RestaurantDao;
import com.openclassrooms.go4lunch.database.HoursData;
import com.openclassrooms.go4lunch.database.RestaurantAndHoursData;
import com.openclassrooms.go4lunch.model.Restaurant;
import com.openclassrooms.go4lunch.database.RestaurantData;
import com.openclassrooms.go4lunch.service.ListRestaurantsService;
import com.openclassrooms.go4lunch.service.ServiceDetailsCallback;
import com.openclassrooms.go4lunch.service.ServicePhotoCallback;
import com.openclassrooms.go4lunch.service.ServicePlacesCallback;
import com.openclassrooms.go4lunch.service.response.details.DetailsResponse;
import com.openclassrooms.go4lunch.service.response.places.PlaceResponse;
import com.openclassrooms.go4lunch.utils.DataConverters;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository class to communicate with the @{@link ListRestaurantsService} service class.
 */
public class PlacesRepository {

    private final ListRestaurantsService listRestaurantsServices;

    private final RestaurantDao restaurantDao;
    private final HoursDao hoursDao;
    private final RestaurantAndHoursDao restaurantAndHoursDao;

    public PlacesRepository(RestaurantDao restaurantDao, HoursDao hoursDao, RestaurantAndHoursDao restaurantAndHoursDao) {
        this.listRestaurantsServices = new ListRestaurantsService();
        this.restaurantDao = restaurantDao;
        this.hoursDao = hoursDao;
        this.restaurantAndHoursDao = restaurantAndHoursDao;
    }

    public List<Restaurant> getListRestaurants() {
        return listRestaurantsServices.getListRestaurants();
    }

    // Methods to access ListRestaurantsService
    /**
     * This method is used to access the findPlacesNearby() method of the @{@link ListRestaurantsService } service class.
     * @param location : Info location of the user
     * @param type : Type of places to search
     * @param callback : Callback interface
     * @throws IOException : Exception thrown by findPlacesNearby() method of the @{@link ListRestaurantsService } service class
     */
    public void findPlacesNearby(String location, String type, ServicePlacesCallback callback) throws IOException {
        PlaceResponse response = listRestaurantsServices.findPlacesNearby(location, type);
        for (int i = 0; i < response.results.size(); i++) {
            // Initialize new restaurant object
            Restaurant restaurant = new Restaurant(
                    response.results.get(i).place_id,
                    response.results.get(i).name,
                    response.results.get(i).vicinity,
                    response.results.get(i).geometry.location.lat,
                               response.results.get(i).geometry.location.lng,
                    response.results.get(i).rating);

            // Add photo data
            if (response.results.get(i).photos != null) {
                if (response.results.get(i).photos.size() > 0) {
                    restaurant.setPhotoReference(response.results.get(i).photos.get(0).photo_reference);
                    restaurant.setPhotoHeight(response.results.get(i).photos.get(0).height);
                    restaurant.setPhotoWidth(response.results.get(i).photos.get(0).width);
                }
            }

            // Add restaurant to the list
            listRestaurantsServices.updateListRestaurants(restaurant);
        }
        callback.onPlacesAvailable(listRestaurantsServices.getListRestaurants());
    }

    /**
     * This method is used to update the list of restaurants with their details.
     * @param listRestaurant : List of restaurants
     * @param callback : Callback interface
     * @throws IOException : Exception thrown by getPlacesDetails() method of the @{@link ListRestaurantsService } service class
     */
    public void getPlacesDetails(List<Restaurant> listRestaurant, ServiceDetailsCallback callback) throws IOException{
        // Contains each restaurant periods (closing and opening hours of a week) found
        List<HoursData> listHoursData = new ArrayList<>();
        List<List<HoursData>> listOfListHoursData = new ArrayList<>();

        for (int i = 0; i < listRestaurant.size(); i++) {
            DetailsResponse response = listRestaurantsServices.getPlacesDetails(listRestaurant.get(i).getPlaceId());
            if (response.result.website != null) listRestaurant.get(i).setWebsiteUri(Uri.parse(response.result.website));
            if (response.result.formatted_phone_number != null) listRestaurant.get(i).setPhoneNumber(response.result.formatted_phone_number);

            if (response.result.opening_hours != null) {
                if (response.result.opening_hours.periods != null) {
                    for (int j = 0; j < response.result.opening_hours.periods.size(); j++) {
                        HoursData hoursData = new HoursData(response.result.opening_hours.periods.get(j).close,
                                                            response.result.opening_hours.periods.get(j).open,
                                                            listRestaurant.get(i).getPlaceId());
                        listHoursData.add(hoursData);
                    }
                    // Update Restaurant with associated Closing/Opening hours
                    listRestaurant.get(i).setOpeningAndClosingHours(DataConverters.converterHoursDataToOpeningAndClosingHours(listHoursData));
                    // Update list of data (Closing/Opening hours) to send to database
                    ArrayList<HoursData> copy = new ArrayList<>(listHoursData);
                    listOfListHoursData.add(copy);
                }
            }
            listHoursData.clear();
        }
        callback.onPlacesDetailsAvailable(listRestaurant, listOfListHoursData);
    }

    /**
     * This method is used to update the list of restaurants with their photos.
     * @param list : List of restaurants
     * @param callback : Callback interface
     */
    public void getPlacesPhoto(List<Restaurant> list, ServicePhotoCallback callback)  {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getPhotoReference() != null) listRestaurantsServices.getPlacePhoto(list.get(i));
        }
        callback.onPhotoAvailable(list);
    }


    // Methods to access Database RestaurantDao
    /**
     * DAO method used to insert a new RestaurantData item in database.
     * @param restaurantData : item to add
     */
    public void insertRestaurantData(RestaurantData restaurantData) {
        restaurantDao.insertRestaurantData(restaurantData);
    }

    /**
     * DAO method used to delete all data in restaurant_table from database.
     */
    public void deleteAllRestaurantsData() {
        restaurantDao.deleteAllRestaurantsData();
    }

    // Methods to access Database HoursDataDao
    /**
     * DAO method used to insert a new HoursData item in database.
     * @param hoursData : item to add
     */
    public void insertHoursData(HoursData hoursData) {
        hoursDao.insertHoursData(hoursData);
    }

    /**
     * DAO method used to delete all data in horus_table from database.
     */
    public void deleteAllHoursData() {
        hoursDao.deleteAllHoursData();
    }

    // Methods to access Database RestaurantAndHoursDao
    /**
     * DAO method to retrieve all RestaurantData and associated HourData from both tables in
     * database.
     * @return : list of RestaurantData and HoursData
     */
    public LiveData<List<RestaurantAndHoursData>> loadAllRestaurantsWithHours() {
        return restaurantAndHoursDao.loadAllRestaurantsWithHours();
    }
}
