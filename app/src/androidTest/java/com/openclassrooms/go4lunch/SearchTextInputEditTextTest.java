package com.openclassrooms.go4lunch;

import android.view.View;
import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.openclassrooms.go4lunch.matchers.SearchTextInputEditTextMatchers;
import com.openclassrooms.go4lunch.ui.activities.MainActivity;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isFocused;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.Espresso.onView;

/**
 * File providing tests to check the TextInputEditText Search View from MainActivity
 * implementation on @{@link MainActivity}.
 */
@RunWith(AndroidJUnit4.class)
public class SearchTextInputEditTextTest {

    @Rule
    public final ActivityScenarioRule rule = new ActivityScenarioRule<>(MainActivity.class);

    /**
     * TEST # 1 : Checks that the search EditText field is correctly displayed after a click
     * on the search menu icon, and correctly closed after a "pressed back button" action.
     */
    @Test
    public void test_check_if_search_field_is_displayed_after_click_on_search_icon() {
        // Click on Search icon
        onView(withId(R.id.search)).perform(click());
        // Check visibility of Search EditText field (View.VISIBLE)
        onView(withId(R.id.text_input_edit_autocomplete))
                .check(matches(SearchTextInputEditTextMatchers.withVisibility(View.VISIBLE)));
        // Close Search EditText field by clicking on back button
        Espresso.pressBack();
        // Check visibility of Search EditText field (View.GONE)
        onView(withId(R.id.text_input_edit_autocomplete))
                .check(matches(SearchTextInputEditTextMatchers.withVisibility(View.GONE)));
    }

    /**
     * TEST # 2 : Checks that the search EditText field is correctly focused when user clicks on it.
     */
    @Test
    public void test_check_if_search_field_is_focused_after_click() {
        // Display Search EditText field by clicking on Search icon
        onView(withId(R.id.search)).perform(click());
        // Click on EditText field
        onView(withId(R.id.text_input_edit_autocomplete)).perform(click());
        // Check if EditText field is focused
        onView(withId(R.id.text_input_edit_autocomplete)).
                check(matches(isFocused()));
        // Close software keyboard
        closeSoftKeyboard();
        // Hide Search EditText field
        Espresso.pressBack();
    }
}
