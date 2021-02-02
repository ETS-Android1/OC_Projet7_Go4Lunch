package com.openclassrooms.go4lunch.viewmodels;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.openclassrooms.go4lunch.adapters.ListViewAdapter;
import com.openclassrooms.go4lunch.adapters.ListViewAdapterCallback;
import com.openclassrooms.go4lunch.model.Restaurant;
import com.openclassrooms.go4lunch.repositories.PlacesRepository;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PlacesViewModel extends ViewModel {

    private final PlacesRepository placesRepository;
    private final Executor executor;
    private MutableLiveData<List<Restaurant>> listRestaurants = new MutableLiveData<>();

    public PlacesViewModel() {
        this.placesRepository = new PlacesRepository();
        this.executor = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Restaurant>> getListRestaurants() {
        if (listRestaurants == null) listRestaurants = new MutableLiveData<>();
        else listRestaurants.setValue(placesRepository.getListRestaurants());
        return listRestaurants;
    }

    public void searchPlacesInCurrentLocation(PlacesClient placesClient, Context context) {
        executor.execute(() -> placesRepository.searchPlacesInCurrentLocation(placesClient, context));
    }

    public void getPlaceDetails(PlacesClient placesClient, Restaurant restaurant) {
        executor.execute(() -> placesRepository.getPlaceDetails(placesClient, restaurant));
    }

    public void getPlacePhoto(PlacesClient placesClient, int position,
                              @NonNull ListViewAdapter.ViewHolderListView holder,
                              ListViewAdapterCallback callback) {
        executor.execute(() -> placesRepository.getPlacePhoto(placesClient, position, holder, callback));
    }
}
