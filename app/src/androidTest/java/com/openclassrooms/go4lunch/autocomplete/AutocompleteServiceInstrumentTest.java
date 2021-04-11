package com.openclassrooms.go4lunch.autocomplete;

import android.content.Context;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.openclassrooms.go4lunch.service.autocomplete.AutocompleteService;
import com.openclassrooms.go4lunch.service.autocomplete.ServiceAutocompleteCallback;
import com.openclassrooms.go4lunch.ui.activities.MainActivity;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertFalse;

/**
 * File providing tests to cover methods from @{@link AutocompleteService} class file.
 */
@RunWith(AndroidJUnit4.class)
public class AutocompleteServiceInstrumentTest {

    private AutocompleteService service;

    @Rule
    public final ActivityScenarioRule rule = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setUp() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        PlacesClient placesClient = Places.createClient(context);
        FusedLocationProviderClient locationProviderClient = LocationServices
                                                           .getFusedLocationProviderClient(context);
        service = new AutocompleteService(placesClient, locationProviderClient);
    }

    /**
     * Test #1 : checks if an autocomplete request returns a non-empty list of results.
     * NOTE : This test requests a specific restaurant, and works only nearby of its location.
     * The latitude and longitude values returned by the FusedLocationProviderClient after a
     * getCurrentLocation() call must be in the restaurant area location.
     */
    @Test
    public void test_check_if_autocomplete_request_returns_results() {
        ServiceAutocompleteCallback callback = autocompleteIdRestaurantsList ->
                assertFalse(autocompleteIdRestaurantsList.isEmpty());
       service.performAutocompleteRequest("GEMINI", callback);
    }
}
