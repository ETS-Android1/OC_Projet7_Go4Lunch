package com.openclassrooms.go4lunch.repositories;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.util.Log;
import androidx.annotation.RequiresPermission;
import androidx.lifecycle.LiveData;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.openclassrooms.go4lunch.dao.HoursDao;
import com.openclassrooms.go4lunch.dao.RestaurantAndHoursDao;
import com.openclassrooms.go4lunch.dao.RestaurantDao;
import com.openclassrooms.go4lunch.database.HoursData;
import com.openclassrooms.go4lunch.database.RestaurantAndHoursData;
import com.openclassrooms.go4lunch.model.Restaurant;
import com.openclassrooms.go4lunch.database.RestaurantData;
import com.openclassrooms.go4lunch.service.autocomplete.AutocompleteService;
import com.openclassrooms.go4lunch.service.autocomplete.ServiceAutocompleteCallback;
import com.openclassrooms.go4lunch.service.places.ListRestaurantsService;
import com.openclassrooms.go4lunch.service.places.ServiceDetailsCallback;
import com.openclassrooms.go4lunch.service.places.ServicePlacesCallback;
import com.openclassrooms.go4lunch.service.places.response.details.DetailsResponse;
import com.openclassrooms.go4lunch.service.places.response.places.PlaceResponse;
import com.openclassrooms.go4lunch.service.places.response.places.ResultPlaces;
import com.openclassrooms.go4lunch.ui.activities.MainActivity;
import com.openclassrooms.go4lunch.ui.activities.MainActivityCallback;
import com.openclassrooms.go4lunch.ui.fragments.map.MapViewFragmentCallback;
import com.openclassrooms.go4lunch.utils.AppInfo;
import com.openclassrooms.go4lunch.utils.DataConverters;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository class to communicate with the @{@link ListRestaurantsService} service class.
 */
public class PlacesRepository {

    // Services
    private final ListRestaurantsService listRestaurantsServices;
    private final AutocompleteService autocompleteService;

    // Dao
    private final RestaurantDao restaurantDao;
    private final HoursDao hoursDao;
    private final RestaurantAndHoursDao restaurantAndHoursDao;

    // SharedPreferences
    private final SharedPreferences[] sharedPrefNextPageToken;
    private SharedPreferences.Editor editor;

    private final Context context;

    public PlacesRepository(RestaurantDao restaurantDao,
                            HoursDao hoursDao,
                            RestaurantAndHoursDao restaurantAndHoursDao,
                            Context context,
                            PlacesClient placesClient,
                            FusedLocationProviderClient locationClient,
                            MainActivityCallback callback) {
        // Initialize services
        this.listRestaurantsServices = new ListRestaurantsService();
        this.autocompleteService = new AutocompleteService(placesClient, locationClient, callback);

        // Initialize DAOs
        this.restaurantDao = restaurantDao;
        this.hoursDao = hoursDao;
        this.restaurantAndHoursDao = restaurantAndHoursDao;

        // Initialize parameters for SharedPreferences
        String PREF_FIRST_NEXT_PAGE_TOKEN = "pref_first_next_page_token";
        String PREF_SECOND_NEXT_PAGE_TOKEN = "pref_second_next_page_token";
        sharedPrefNextPageToken = new SharedPreferences[2];
        sharedPrefNextPageToken[0] = context.getSharedPreferences(PREF_FIRST_NEXT_PAGE_TOKEN, Context.MODE_PRIVATE);
        sharedPrefNextPageToken[1] = context.getSharedPreferences(PREF_SECOND_NEXT_PAGE_TOKEN, Context.MODE_PRIVATE);

        // Get context
        this.context = context;
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
            Restaurant restaurant = initializeRestaurantObject(response.results.get(i));

            // Add restaurant to the list
            listRestaurantsServices.updateListRestaurants(restaurant);

            // Save next page token
            if (response.next_page_token != null) {
                editor = sharedPrefNextPageToken[0].edit();
                editor.putString("first_next_page_token", response.next_page_token).apply();
            }
        }
        callback.onPlacesAvailable(listRestaurantsServices.getListRestaurants());
    }

    public void getNextPlacesNearby(ServicePlacesCallback callback, List<Restaurant> listRestaurants, int numNextPageToken) throws IOException {
        String nextPage_Token;
        nextPage_Token = sharedPrefNextPageToken[numNextPageToken].getString("first_next_page_token", null);
        PlaceResponse response = listRestaurantsServices.getNextPlacesNearby(nextPage_Token);

        for (int i = 0; i < response.results.size(); i++) {
            // Initialize new restaurant object
            Restaurant restaurant = initializeRestaurantObject(response.results.get(i));

            // Add restaurant to the list
            listRestaurants.add(restaurant);

            // Save next page token
            if (response.next_page_token != null) {
                switch (numNextPageToken) {
                    case 0:
                        editor = sharedPrefNextPageToken[0].edit();
                        editor.putString("first_next_page_token", response.next_page_token).apply();
                        break;
                    case 1:
                        editor = sharedPrefNextPageToken[1].edit();
                        editor.putString("second_next_page_token", response.next_page_token).apply();
                        break;
                }
            }
        }
        callback.onPlacesAvailable(listRestaurants);
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
     * This method creates a Restaurant object, by extracting data from a ResultPlaces
     * @param results : from GET response
     * @return : Restaurant object
     */
    private Restaurant initializeRestaurantObject(ResultPlaces results) {
        Restaurant restaurant = new Restaurant(
                results.place_id,
                results.name,
                results.vicinity,
                results.geometry.location.lat,
                results.geometry.location.lng,
                results.rating);

        // Add photo data
        if (results.photos != null) {
            if (results.photos.size() > 0) {
                restaurant.setPhotoReference(results.photos.get(0).photo_reference);
                restaurant.setPhotoHeight(results.photos.get(0).height);
                restaurant.setPhotoWidth(results.photos.get(0).width);
            }
        }
        return restaurant;
    }

    // Methods to access AutocompleteService
    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    public void performAutocompleteRequest(String query, ServiceAutocompleteCallback callback) {
        Log.i("PERFORMAUTOCOMPLETE", "PlacesRepository performAutocompleteRequest : " + query);
        autocompleteService.performAutocompleteRequest(query, callback);
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

    // Other methods
    /**
     * This method is used to check the current user location and compare with the previous saved value,
     * to determine if a new search request is necessary or if data can be reloading from database.
     */
    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    public void getPlacesFromDatabaseOrRetrofitRequest(MainActivity activity, SharedPreferences sharedPrefLatLon, MapViewFragmentCallback callback) {
        activity.getLocationClient().getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener(location -> {
                    double currentLatUserPosition;
                    double currentLonUserPosition;
                    double savedLatUserPosition;
                    double savedLonUserPosition;

                    // Get current location
                    currentLatUserPosition = location.getLatitude();
                    currentLonUserPosition = location.getLongitude();

                    if (AppInfo.checkIfFirstRunApp(activity.getApplicationContext())) {
                        callback.searchPlacesFromCurrentLocation();
                    }
                    else {
                        // Get previous location
                        savedLatUserPosition = Double.longBitsToDouble(sharedPrefLatLon.getLong("old_lat_position", Double.doubleToRawLongBits(currentLatUserPosition)));
                        savedLonUserPosition = Double.longBitsToDouble(sharedPrefLatLon.getLong("old_lon_position", Double.doubleToRawLongBits(currentLonUserPosition)));
                        // Check distance
                        float[] result = new float[1];
                        Location.distanceBetween(currentLatUserPosition, currentLonUserPosition, savedLatUserPosition, savedLonUserPosition, result);
                        // Get locations
                        if (result[0] < 800) { // distance < 800m : reload locations from database
                            callback.restoreListFromDatabase();
                        }
                        else { // >= 800m : search places
                            callback.searchPlacesFromCurrentLocation();
                        }
                    }
                });
    }
}
