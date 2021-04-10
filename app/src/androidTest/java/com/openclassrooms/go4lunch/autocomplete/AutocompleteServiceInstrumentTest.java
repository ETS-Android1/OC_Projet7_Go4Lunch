package com.openclassrooms.go4lunch.autocomplete;

import android.content.Context;
import android.view.View;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.openclassrooms.go4lunch.R;
import com.openclassrooms.go4lunch.matchers.SearchTextInputLayoutMatchers;
import com.openclassrooms.go4lunch.service.autocomplete.AutocompleteService;
import com.openclassrooms.go4lunch.service.autocomplete.ServiceAutocompleteCallback;
import com.openclassrooms.go4lunch.ui.activities.MainActivity;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
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
        FusedLocationProviderClient locationProviderClient = LocationServices.getFusedLocationProviderClient(context);
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

    @Test
    public void test_check_if_search_field_is_displayed_after_click_on_search_icon() {
        // Click on Search icon
        onView(ViewMatchers.withId(R.id.search)).perform(click());
        // Check visibility of Search EditText field (View.VISIBLE)
        onView(withId(R.id.text_input_layout_autocomplete))
                .check(matches(SearchTextInputLayoutMatchers.withVisibility(View.VISIBLE)));
        // Close Search EditText field by clicking on back button
        Espresso.pressBack();
        // Check visibility of Search EditText field (View.GONE)
        onView(withId(R.id.text_input_layout_autocomplete))
                .check(matches(SearchTextInputLayoutMatchers.withVisibility(View.GONE)));
    }
}
