package com.openclassrooms.go4lunch.autocomplete;

import android.view.View;
import androidx.lifecycle.Lifecycle;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.openclassrooms.go4lunch.R;
import com.openclassrooms.go4lunch.matchers.SearchTextInputLayoutMatchers;
import com.openclassrooms.go4lunch.ui.activities.MainActivity;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.Espresso.onView;

/**
 * File providing tests to check the TextInputEditText Search View from MainActivity
 * implementation on @{@link MainActivity}.
 */
@RunWith(AndroidJUnit4.class)
public class SearchTextInputLayoutTest {

    @Rule
    public final ActivityScenarioRule rule = new ActivityScenarioRule<>(MainActivity.class);


    /**
     * TEST # 1 : Checks that the search EditText field is correctly displayed after a click
     * on the search menu icon, and correctly closed after a "pressed back button" action.
     */
    @Test
    public void test_check_if_search_field_is_displayed_after_click_on_search_icon() {
        rule.getScenario().moveToState(Lifecycle.State.RESUMED);
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

    @After
    public void endTest() {
        rule.getScenario().close();
    }
}
