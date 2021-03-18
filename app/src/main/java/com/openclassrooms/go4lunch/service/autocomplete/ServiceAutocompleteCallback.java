package com.openclassrooms.go4lunch.service.autocomplete;

import java.util.List;

public interface ServiceAutocompleteCallback {
    void getAutocompleteResults(List<String> autocompleteIdRestaurantsList);
}
