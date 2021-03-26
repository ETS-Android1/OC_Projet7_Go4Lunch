package com.openclassrooms.go4lunch.service.autocomplete;

import java.util.List;

/**
 * Callback interface to handle autocomplete results
 */
public interface ServiceAutocompleteCallback {
    void getAutocompleteResults(List<String> autocompleteIdRestaurantsList);
}
