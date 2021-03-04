package com.openclassrooms.go4lunch.repositories;

import android.net.Uri;
import androidx.lifecycle.LiveData;
import com.openclassrooms.go4lunch.dao.RestaurantDao;
import com.openclassrooms.go4lunch.model.Restaurant;
import com.openclassrooms.go4lunch.service.ListRestaurantsService;
import com.openclassrooms.go4lunch.service.ServiceDetailsCallback;
import com.openclassrooms.go4lunch.service.ServicePhotoCallback;
import com.openclassrooms.go4lunch.service.ServicePlacesCallback;
import com.openclassrooms.go4lunch.service.response.details.DetailsResponse;
import com.openclassrooms.go4lunch.service.response.places.PlaceResponse;
import java.io.IOException;
import java.util.List;

/**
 * Repository class to communicate with the @{@link ListRestaurantsService} service class.
 */
public class PlacesRepository {

    private final ListRestaurantsService listRestaurantsServices;

    private final RestaurantDao restaurantDao;

    public PlacesRepository(RestaurantDao restaurantDao) {
        this.restaurantDao = restaurantDao;
        this.listRestaurantsServices = new ListRestaurantsService();
    }

    public List<Restaurant> getListRestaurants() {
        return listRestaurantsServices.getListRestaurants();
    }

    // Methods to access ListRestaurantsService
    /**
     * This method is used to access the findPlacesNearby() method of the @{@link ListRestaurantsService } service class
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
                    getListRestaurants().size()+1,
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
     * This method is used to update the list of restaurants with their details
     * @param list : List of restaurants
     * @param callback : Callback interface
     * @throws IOException : Exception thrown by getPlacesDetails() method of the @{@link ListRestaurantsService } service class
     */
    public void getPlacesDetails(List<Restaurant> list, ServiceDetailsCallback callback) throws IOException{
        for (int i = 0; i < list.size(); i++) {
            DetailsResponse response = listRestaurantsServices.getPlacesDetails(list.get(i).getPlace_id());
            if (response.result.website != null) list.get(i).setWebsiteUri(Uri.parse(response.result.website));
            if (response.result.formatted_phone_number != null) list.get(i).setPhoneNumber(response.result.formatted_phone_number);
        }
        callback.onPlacesDetailsAvailable(list);
    }

    /**
     * This method is used to update the list of restaurants with their photos
     * @param list : List of restaurants
     * @param callback : Callback interface
     */
    public void getPlacesPhoto(List<Restaurant> list, ServicePhotoCallback callback)  {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getPhotoReference() != null) listRestaurantsServices.getPlacePhoto(list.get(i));
        }
        callback.onPhotoAvailable(list);
    }

    // Methods to access Database Dao
    /**
     * DAO method used to insert a new Restaurant item in database
     * @param restaurant : item to add
     */
    public void insertRestaurant(Restaurant restaurant) {
        restaurantDao.insertRestaurant(restaurant);
    }

    /**
     * DAO method used to update an existing item in database
     * @param restaurant : item to update
     */
    public void updateRestaurant(Restaurant restaurant) {
        restaurantDao.updateRestaurant(restaurant);
    }

    /**
     * DAO method used to delete all data in restaurant_table from database
     */
    public void deleteAllRestaurants() {
        restaurantDao.deleteAllRestaurants();
    }

    /**
     * DAO method used to load all data from restaurant_table
     * @return : LiveData object containing the list of restaurants stored in database
     */
    public LiveData<List<Restaurant>> loadAllRestaurants() {
        return restaurantDao.loadAllRestaurants();
    }
}
