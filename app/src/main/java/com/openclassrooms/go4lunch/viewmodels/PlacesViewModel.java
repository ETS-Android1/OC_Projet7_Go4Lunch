package com.openclassrooms.go4lunch.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.openclassrooms.go4lunch.database.RestaurantAndHoursData;
import com.openclassrooms.go4lunch.di.DI;
import com.openclassrooms.go4lunch.database.HoursData;
import com.openclassrooms.go4lunch.model.Restaurant;
import com.openclassrooms.go4lunch.database.RestaurantData;
import com.openclassrooms.go4lunch.repositories.PlacesRepository;
import com.openclassrooms.go4lunch.service.ServiceDetailsCallback;
import com.openclassrooms.go4lunch.utils.DataConverters;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * ViewModel class used to store a list of detected restaurant in a MutableLiveData object.
 */
public class PlacesViewModel extends ViewModel {

    // Repository to access a service
    private PlacesRepository placesRepository;

    // Executor to launch all repository accesses
    private final Executor executor = DI.provideExecutor();

    // To store the list of restaurant
    private final MutableLiveData<List<Restaurant>> listRestaurants = new MutableLiveData<>();

    public PlacesViewModel(){ /* Empty constructor */ }

    public MutableLiveData<List<Restaurant>> getListRestaurants() {
        return listRestaurants;
    }

    // Getter/Setter
    public void setRepository(PlacesRepository placesRepository) { this.placesRepository = placesRepository; }

    public PlacesRepository getPlacesRepository() {
        return placesRepository;
    }

    // Methods to access PlacesRepository -> ListRestaurantsService methods
    /**
     * This method is used to access the findPlacesNearby() method of the @{@link PlacesRepository } repository class.
     * @param location : Info location of the user
     * @param type : Type of places to search
     */
    public void findPlacesNearby(String location, String type) {
            executor.execute(() -> {
                try {
                    placesRepository.findPlacesNearby(location, type, newListRestaurants -> {
                        listRestaurants.postValue(newListRestaurants);
                        getPlacesDetails(newListRestaurants);
                    });
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
            );
    }

    /**
     * This method is used to access the getPlacesDetails() method of the @{@link PlacesRepository } repository class.
     * @param list : List of restaurant to update with details for each place
     */
    public void getPlacesDetails(List<Restaurant> list) {
        executor.execute(() -> {
            try {
                placesRepository.getPlacesDetails(list, (newListRestaurants, listOfListHoursData) -> {
                    listRestaurants.postValue(newListRestaurants);
                    // Store list of periods in database
                    updateDatabaseHoursDataTable(listOfListHoursData);
                    // Get photos
                    getPlacesPhoto(newListRestaurants);
                });
            } catch (IOException exception) { exception.printStackTrace(); }
        });
    }

    /**
     * This method is used to access the getPlacesPhoto() method of the @{@link PlacesRepository } repository class.
     * @param list : List of restaurant to update with photos for each place
     */
    public void getPlacesPhoto(List<Restaurant> list) {
        executor.execute(() -> {
                placesRepository.getPlacesPhoto(list, newListRestaurant -> {
                    listRestaurants.postValue(newListRestaurant);
                    updateDatabaseRestaurantTable(newListRestaurant);
                });
        });
    }

    // Methods to access PlacesRepository -> RestaurantDao methods

    /**
     * This method handles the insertion operation of a new RestaurantData object in restaurant_table.
     * @param restaurantData : Data to insert
     */
    public void insertRestaurantData(RestaurantData restaurantData) {
        executor.execute(() -> placesRepository.insertRestaurantData(restaurantData));
    }

    /**
     * This method handles the deletion of all RestaurantData objects stored in restaurant_table .
     */
    public void deleteAllRestaurantsData() {
        executor.execute(placesRepository::deleteAllRestaurantsData);
    }

    /**
     * This method handles the update of restaurant_table with a new list of Restaurant (result of a
     * search request).
     * @param list : Data to store in restaurant_table
     */
    private void updateDatabaseRestaurantTable(List<Restaurant> list) {
        deleteAllRestaurantsData();

        for (int i = 0; i < list.size(); i++) {
            RestaurantData restaurantData = new RestaurantData(list.get(i).getPlaceId(), list.get(i).getName(), list.get(i).getAddress(),
                    list.get(i).getLatitude(), list.get(i).getLongitude(), list.get(i).getRating(),
                    list.get(i).getPhoneNumber(), list.get(i).getWebsiteUri(),
                    list.get(i).getPhotoReference(), list.get(i).getPhotoHeight(), list.get(i).getPhotoWidth());
            insertRestaurantData(restaurantData);
        }
    }

    // Methods to access placeRepository -> HoursDao methods
    /**
     * This method handles the insertion of a new HoursData object in hours_table.
     * @param hoursData : Data to insert
     */
    public void insertHoursData(HoursData hoursData) {
        executor.execute(() -> placesRepository.insertHoursData(hoursData));
    }

    /**
     * This method handles the deletion of all HoursData objects stored in hours_table .
     */
    public void deleteAllHoursData() {
        executor.execute(() -> placesRepository.deleteAllHoursData());
    }

    /**
     * This method handles the update of hours_table with a list of several lists containing the closing/opening
     * hours information, each one associated with a restaurant.
     * @param listOfListHoursData : Data to store in hours_table
     */
    private void updateDatabaseHoursDataTable(List<List<HoursData>> listOfListHoursData) {
        deleteAllHoursData();
        for (int i = 0; i < listOfListHoursData.size(); i++) {
           for (int j = 0; j < listOfListHoursData.get(i).size(); j++) {
               insertHoursData(listOfListHoursData.get(i).get(j));
           }
        }
    }

    // Other methods
    /**
     * This method handles the restoration of all Restaurant data and OpeningAndClosingHours data, from
     * a list of RestaurantAndHoursData retrieved from a RestaurantAndHoursDao request.
     * @param restaurantAndHoursData : Data from a RestaurantAndHoursDao request
     */
    public void restoreData(List<RestaurantAndHoursData> restaurantAndHoursData) {
        List<Restaurant> oldListRestaurants = new ArrayList<>();

        for (int i = 0; i < restaurantAndHoursData.size(); i++) {
            RestaurantData restaurantData = restaurantAndHoursData.get(i).restaurantData;
            Restaurant restaurant = new Restaurant(restaurantData.getPlaceId(), restaurantData.getName(), restaurantData.getAddress(),
                    restaurantData.getLatitude(), restaurantData.getLongitude(), restaurantData.getRating());

                    restaurant.setPhotoReference(restaurantData.getPhotoReference());
                    restaurant.setPhotoWidth(restaurantData.getPhotoWidth());
                    restaurant.setPhotoHeight(restaurantData.getPhotoHeight());
                    restaurant.setWebsiteUri(restaurantData.getWebsiteUri());
                    restaurant.setPhoneNumber(restaurantData.getPhoneNumber());

            List<HoursData> hoursData = restaurantAndHoursData.get(i).hoursData;
            restaurant.setOpeningAndClosingHours(DataConverters.converterHoursDataToOpeningAndClosingHours(hoursData));
            oldListRestaurants.add(restaurant);
        }

        listRestaurants.postValue(oldListRestaurants);
    }
}
