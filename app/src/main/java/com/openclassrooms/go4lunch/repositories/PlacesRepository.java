package com.openclassrooms.go4lunch.repositories;

import android.content.Context;
import androidx.annotation.NonNull;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.openclassrooms.go4lunch.adapters.ListViewAdapter;
import com.openclassrooms.go4lunch.adapters.ListViewAdapterCallback;
import com.openclassrooms.go4lunch.model.Restaurant;
import com.openclassrooms.go4lunch.service.ListRestaurantsService;
import com.openclassrooms.go4lunch.ui.fragments.map.MapViewFragmentCallback;
import java.util.List;

/**
 * Repository class to communicate with the @{@link ListRestaurantsService} service class.
 */
public class PlacesRepository {

    public List<Restaurant> getListRestaurants() {
        return ListRestaurantsService.getListRestaurants();
    }

    public void searchPlacesInCurrentLocation(PlacesClient placesClient, Context context, MapViewFragmentCallback callback) {
        ListRestaurantsService.searchPlacesInCurrentLocation(placesClient, context, callback);
    }

    public void getPlaceDetails(PlacesClient placesClient, int position,
                                @NonNull ListViewAdapter.ViewHolderListView holder,
                                ListViewAdapterCallback callback) {
        ListRestaurantsService.getPlaceDetails(placesClient, position, holder, callback);
    }
}
