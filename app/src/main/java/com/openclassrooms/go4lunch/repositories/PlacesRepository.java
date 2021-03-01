package com.openclassrooms.go4lunch.repositories;

import android.net.Uri;
import android.util.Log;
import com.google.android.gms.maps.model.LatLng;
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

    private final ListRestaurantsService listRestaurantsServices = new ListRestaurantsService();

    public List<Restaurant> getListRestaurants() {
        return listRestaurantsServices.getListRestaurants();
    }

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
                    response.results.get(i).place_id,
                    response.results.get(i).name,
                    response.results.get(i).vicinity,
                    new LatLng(response.results.get(i).geometry.location.lat,
                               response.results.get(i).geometry.location.lng),
                    response.results.get(i).rating);
            // Add photo data
            if (response.results.get(i).photos != null) {
                if (response.results.get(i).photos.size() > 0) {
                    restaurant.setPhotoReference(response.results.get(i).photos.get(0).photo_reference);
                    restaurant.setPhotoHeight(response.results.get(i).photos.get(0).height);
                    restaurant.setPhotoWidth(response.results.get(i).photos.get(0).width);
                 //   listRestaurantsServices.getPlacePhoto(restaurant);
                }
            }
            // Add restaurant to the list
            listRestaurantsServices.updateListRestaurants(restaurant);
        }
        callback.onPlacesAvailable(listRestaurantsServices.getListRestaurants());
    }

    /**
     * This method is used to update the list of locations with their details
     * @param callback : Callback interface
     * @throws IOException : Exception thrown by getPlacesDetails() method of the @{@link ListRestaurantsService } service class
     */
    public void getPlacesDetails(List<Restaurant> list, ServiceDetailsCallback callback) throws IOException{
        for (int i = 0; i < list.size(); i++) {
            DetailsResponse response = listRestaurantsServices.getPlacesDetails(list.get(i).getId());
            if (response.result.website != null) list.get(i).setWebsiteUri(Uri.parse(response.result.website));
            if (response.result.formatted_phone_number != null) list.get(i).setPhoneNumber(response.result.formatted_phone_number);
        }
        callback.onPlacesDetailsAvailable(list);
    }

    public void getPlacesPhoto(List<Restaurant> list, ServicePhotoCallback callback)  {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getPhotoReference() != null) {
                listRestaurantsServices.getPlacePhoto(list.get(i));
            }
        }
        callback.onPhotoAvailable(list);
    }
/*
    public void getNextSetOfPlaces(String nextPageToken) {

        while (nextPageToken != null) {
            Log.i("NEXTPAGETOKEN", nextPageToken);
            try {
                PlaceResponse response;
                do {
                    response = listRestaurantsServices.getNextSetOfPlaces(nextPageToken);
                    Log.i("NEXTPAGETOKEN", response.status); // TODO () : Préciser type établissement dans requête
                } while (response.status.equals("INVALID_REQUEST"));

                Log.i("NEXTPAGETOKEN", "Size : " + response.results.size());
                for (int i = 0; i < response.results.size(); i++) {
                    Restaurant restaurant = new Restaurant(
                            response.results.get(i).place_id,
                            response.results.get(i).name,
                            response.results.get(i).vicinity,
                            new LatLng(response.results.get(i).geometry.location.lat,
                                    response.results.get(i).geometry.location.lng),
                            response.results.get(i).rating);
                    Log.i("NEXTPAGETOKEN", "Name : " + response.results.get(i).name);
                    Log.i("NEXTPAGETOKEN", "Name : " + response.results.get(i).place_id);
                    Log.i("NEXTPAGETOKEN", "Name : " + response.results.get(i).vicinity);
                    listRestaurantsServices.updateListRestaurants(restaurant);
                }
                nextPageToken = response.next_page_token;
            } catch (IOException exception) {
                exception.printStackTrace();
            }

        }
    }
    */
    /**
     * This method is used to access the getPlacesDetails() method of the @{@link ListRestaurantsService } service class
     * @param listRestaurant : List of restaurant to update with details for each place
     * @param placesClient : PlacesClient Instance to access Places API methods
     * @param callback : Callback interface
     */
    /*   public void getPlacesDetails(List<Restaurant> listRestaurant, PlacesClient placesClient, ServiceDetailsCallback callback) {
        listRestaurantsServices.getPlacesDetails(listRestaurant, placesClient, callback);
    } */

    /**
     * This method is used to access the getPlacesPhotos() method of the @{@link ListRestaurantsService } service class
     * @param placesClient : PlacesClient Instance to access Places API methods
     * @param listRestaurant : List of restaurant to update with photo for each place
     * @param callback : Callback interface
     */
    /*   public void getPlacesPhotos(PlacesClient placesClient, List<Restaurant> listRestaurant, ServicePhotoCallback callback) {
        listRestaurantsServices.getPlacesPhotos(placesClient, listRestaurant, callback);
    }
    */
}
