package com.openclassrooms.go4lunch.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.openclassrooms.go4lunch.di.DI;
import com.openclassrooms.go4lunch.model.Restaurant;
import com.openclassrooms.go4lunch.repositories.PlacesRepository;
import java.io.IOException;
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

    public void setRepository(PlacesRepository placesRepository) {
        this.placesRepository = placesRepository;
    }

    // Methods to access PlacesRepository -> ListRestaurantsService methods
    /**
     * This method is used to access the findPlacesNearby() method of the @{@link PlacesRepository } repository class
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
     * This method is used to access the getPlacesDetails() method of the @{@link PlacesRepository } repository class
     * @param list : List of restaurant to update with details for each place
     */
    public void getPlacesDetails(List<Restaurant> list) {
        executor.execute(() -> {
            try {
                placesRepository.getPlacesDetails(list, newListRestaurants -> {
                    listRestaurants.postValue(newListRestaurants);
                    getPlacesPhoto(newListRestaurants);
                });
            } catch (IOException exception) { exception.printStackTrace(); }
        });
    }

    /**
     * This method is used to access the getPlacesPhoto() method of the @{@link PlacesRepository } repository class
     * @param list : List of restaurant to update with photos for each place
     */
    public void getPlacesPhoto(List<Restaurant> list) {
        executor.execute(() -> {
                placesRepository.getPlacesPhoto(list, newListRestaurant -> {
                    listRestaurants.postValue(newListRestaurant);
                    updateDatabaseWithPlacesNearbyResults(newListRestaurant);
                });
        });
    }

    public void restorePlacesPhoto(List<Restaurant> listFromDataBase) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                // Post data from DB on ListRestaurant when available
                placesRepository.getPlacesPhoto(listFromDataBase, listRestaurants::postValue);
            }
        });
    }

    private void updateDatabaseWithPlacesNearbyResults(List<Restaurant> list) {
        deleteAllRestaurants();
        for (int i = 0; i < list.size(); i++) {
            insertRestaurant(list.get(i));
        }
    }

    // Methods to access PlacesRepository -> RestaurantDao methods
    public void insertRestaurant(Restaurant restaurant) {
        executor.execute(() -> placesRepository.insertRestaurant(restaurant));
    }

    public void updateRestaurant(Restaurant restaurant) {
        executor.execute(() -> placesRepository.updateRestaurant(restaurant));
    }

    public void deleteAllRestaurants() {
        executor.execute(placesRepository::deleteAllRestaurants);
    }

    public PlacesRepository getPlacesRepository() {
        return placesRepository;
    }

}
