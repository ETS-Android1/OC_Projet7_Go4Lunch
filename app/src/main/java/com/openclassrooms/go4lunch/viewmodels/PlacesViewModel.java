package com.openclassrooms.go4lunch.viewmodels;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
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
     * @param type : Type of places to search
     */
    public void findPlacesNearby(String location, String type)  {
            executor.execute(() -> {
                try {
                    placesRepository.findPlacesNearby(location, type, newListRestaurants -> {
                        listRestaurants.postValue(newListRestaurants);
                        getPlacesDetails(newListRestaurants);
                    });
                } catch (IOException exception) {
                    exception.printStackTrace();
                }

            });
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
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        });
    }

    /**
     * This method is used to access the getPlacesPhoto() method of the @{@link PlacesRepository } repository class
     * @param list : List of restaurant to update with photos for each place
     */
    public void getPlacesPhoto(List<Restaurant> list) {
        executor.execute(() -> {
                placesRepository.getPlacesPhoto(list, listRestaurants::postValue);
        });
    }
}
