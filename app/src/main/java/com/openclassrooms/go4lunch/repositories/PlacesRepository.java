package com.openclassrooms.go4lunch.repositories;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.openclassrooms.go4lunch.model.Restaurant;
import com.openclassrooms.go4lunch.service.ListRestaurantsService;
import com.openclassrooms.go4lunch.service.ServiceDetailsCallback;
import com.openclassrooms.go4lunch.service.ServicePhotoCallback;
import com.openclassrooms.go4lunch.service.ServicePlacesCallback;
import com.openclassrooms.go4lunch.service.response.places.PlaceResponse;
import java.io.IOException;
import java.util.List;

/**
 * Repository class to communicate with the @{@link ListRestaurantsService} service class.
 */
public class PlacesRepository {

    private final ListRestaurantsService listRestaurantsServices = new ListRestaurantsService();

    public List<Restaurant> getListRestaurants() {
        return listRestaurantsServices.getListRestaurants();
    }

    /**
     * This method is used to access the findPlacesNearby() method of the @{@link ListRestaurantsService } service class
     * @param location : Info location of the user
     * @param radius : Detection radius
     * @param type : Type of places to search
     * @param callback : Callback interface
     * @throws IOException : Exception thrown by findPlacesNearby() method of the @{@link ListRestaurantsService } service class
     */
    public void findPlacesNearby(String location, int radius, String type, ServicePlacesCallback callback) throws IOException {
        PlaceResponse response = listRestaurantsServices.findPlacesNearby(location, radius, type);
        for (int i = 0; i < response.results.size(); i++) {
            Restaurant restaurant = new Restaurant(
                    response.results.get(i).place_id,
                    response.results.get(i).name,
                    response.results.get(i).vicinity,
                    new LatLng(response.results.get(i).geometry.location.lat,
                               response.results.get(i).geometry.location.lng),
                    response.results.get(i).rating);
            listRestaurantsServices.updateListRestaurants(restaurant);
        }
        callback.onPlacesAvailable(listRestaurantsServices.getListRestaurants());
    }

    /**
     * This method is used to access the getPlacesDetails() method of the @{@link ListRestaurantsService } service class
     * @param listRestaurant : List of restaurant to update with details for each place
     * @param placesClient : PlacesClient Instance to access Places API methods
     * @param callback : Callback interface
     */
    public void getPlacesDetails(List<Restaurant> listRestaurant, PlacesClient placesClient, ServiceDetailsCallback callback) {
        listRestaurantsServices.getPlacesDetails(listRestaurant, placesClient, callback);
    }

    /**
     * This method is used to access the getPlacesPhotos() method of the @{@link ListRestaurantsService } service class
     * @param placesClient : PlacesClient Instance to access Places API methods
     * @param listRestaurant : List of restaurant to update with photo for each place
     * @param callback : Callback interface
     */
    public void getPlacesPhotos(PlacesClient placesClient, List<Restaurant> listRestaurant, ServicePhotoCallback callback) {
        listRestaurantsServices.getPlacesPhotos(placesClient, listRestaurant, callback);
    }

}
