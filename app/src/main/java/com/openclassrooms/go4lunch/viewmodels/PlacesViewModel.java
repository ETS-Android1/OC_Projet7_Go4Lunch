package com.openclassrooms.go4lunch.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.openclassrooms.go4lunch.model.Restaurant;
import com.openclassrooms.go4lunch.repositories.PlacesRepository;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * ViewModel class used to store a list of detected restaurant in a MutableLiveData object.
 */
public class PlacesViewModel extends ViewModel {

    // Repository to access a service
    private final PlacesRepository placesRepository;
    // Executor to launch all repository accesses
    private final Executor executor;
    // To store the list of restaurant
    private final MutableLiveData<List<Restaurant>> listRestaurants = new MutableLiveData<>();

    public PlacesViewModel() {
        this.placesRepository = new PlacesRepository();
        this.executor = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Restaurant>> getListRestaurants() {
        return listRestaurants;
    }


    /**
     * This method is used to access the findPlacesNearby() method of the @{@link PlacesRepository } repository class
     * @param location : Info location of the user
     * @param radius : Detection radius
     * @param type : Type of places to search
     * @param placesClient : PlacesClient Instance to access Places API methods
     */
    public void findPlacesNearby(String location, int radius, String type, PlacesClient placesClient)  {
            executor.execute(() -> {
                try {
                    placesRepository.findPlacesNearby(location, radius, type, newListRestaurant -> {
                        listRestaurants.postValue(newListRestaurant);
                        getPlacesDetails(placesClient, newListRestaurant);
                    });
                } catch (IOException exception) {
                    exception.printStackTrace();
                }

            });
    }

    /**
     * This method is used to access the getPlacesDetails() method of the @{@link PlacesRepository } repository class
     * @param placesClient : PlacesClient Instance to access Places API methods
     * @param list : List of restaurant to update with details for each place
     */
    public void getPlacesDetails(PlacesClient placesClient, List<Restaurant> list) {
        executor.execute(() -> placesRepository.getPlacesDetails(list, placesClient, newListRestaurant -> {
            listRestaurants.postValue(newListRestaurant);
            getPlacesPhotos(placesClient, newListRestaurant);
        }));
    }

    /**
     * This method is used to access the getPlacesPhotos() method of the @{@link PlacesRepository } repository class
     * @param placesClient : PlacesClient Instance to access Places API methods
     * @param list : List of restaurant to update with photo for each place
     */
    public void getPlacesPhotos(PlacesClient placesClient, List<Restaurant> list) {
        executor.execute(() -> placesRepository.getPlacesPhotos(placesClient, list, listRestaurants::postValue));
    }
}
