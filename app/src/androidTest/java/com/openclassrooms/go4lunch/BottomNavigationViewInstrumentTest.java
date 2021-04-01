package com.openclassrooms.go4lunch;

import android.content.Context;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.openclassrooms.go4lunch.matchers.BottomNavigationViewMatchers;
import com.openclassrooms.go4lunch.ui.activities.MainActivity;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.Espresso.onView;

/**
 * File providing tests to check @{@link BottomNavigationView} implementation on @{@link MainActivity}.
 */
@RunWith(AndroidJUnit4.class)
public class BottomNavigationViewInstrumentTest {

    private Context context;

    @Rule
    public final ActivityScenarioRule rule = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setUp() {
        this.context = InstrumentationRegistry.getInstrumentation().getTargetContext();
    }

    /**
     * TEST # 1 : Checks that all BottomNavigationView items have the correct title.
     */
    @Test
    public void test_check_bottom_navigation_view_items_title() {
       onView(withId(R.id.map)).check(matches(BottomNavigationViewMatchers.withTitle(context.getString(R.string.icon_map_view))));
       onView(withId(R.id.list)).check(matches(BottomNavigationViewMatchers.withTitle(context.getString(R.string.icon_list_view))));
       onView(withId(R.id.workmates)).check(matches(BottomNavigationViewMatchers.withTitle(context.getString(R.string.icon_workmates))));
    }

    /**
     * TEST # 2 : Checks that all BottomNavigationView items have the "checked" status at launch.
     */
    @Test
    public void test_check_bottom_navigation_view_item_selection() {
        onView(withId(R.id.map)).check(matches(BottomNavigationViewMatchers.withItemChecked(true)));
        onView(withId(R.id.list)).check(matches(BottomNavigationViewMatchers.withItemChecked(false)));
        onView(withId(R.id.workmates)).check(matches(BottomNavigationViewMatchers.withItemChecked(false)));
    }

    /**
     * TEST # 3 : Checks that all BottomNavigationView items "checked" status are correctly updated
     * after click.
     */
    @Test
    public void test_check_items_status_after_click() {
        // Click on second item
        onView(withId(R.id.map)).perform(click());
        onView(withId(R.id.map)).check(matches(BottomNavigationViewMatchers.withItemChecked(true)));
        onView(withId(R.id.list)).check(matches(BottomNavigationViewMatchers.withItemChecked(false)));
        onView(withId(R.id.workmates)).check(matches(BottomNavigationViewMatchers.withItemChecked(false)));

        // Click on third item
        onView(withId(R.id.list)).perform(click());
        onView(withId(R.id.map)).check(matches(BottomNavigationViewMatchers.withItemChecked(false)));
        onView(withId(R.id.list)).check(matches(BottomNavigationViewMatchers.withItemChecked(true)));
        onView(withId(R.id.workmates)).check(matches(BottomNavigationViewMatchers.withItemChecked(false)));

        // Click on first
        onView(withId(R.id.workmates)).perform(click());
        onView(withId(R.id.map)).check(matches(BottomNavigationViewMatchers.withItemChecked(false)));
        onView(withId(R.id.list)).check(matches(BottomNavigationViewMatchers.withItemChecked(false)));
        onView(withId(R.id.workmates)).check(matches(BottomNavigationViewMatchers.withItemChecked(true)));
    }

    /**
     * TEST # 4 : Checks that clicking on an BottomNavigationView item will display the correct fragment.
     */
    @Test
    public void test_check_if_fragment_is_displayed_after_click_on_item() {
        // Display ListViewFragment
        onView(withId(R.id.list)).perform(click());
        onView(withId(R.id.fragment_list_view_root_layout)).check(matches(isDisplayed()));

        // Display WorkmatesFragment
        onView(withId(R.id.workmates)).perform(click());
        onView(withId(R.id.fragment_workmates_root_layout)).check(matches(isDisplayed()));

        // Display MapViewFragment
        onView(withId(R.id.map)).perform(click());
        onView(withId(R.id.fragment_map_root_layout)).check(matches(isDisplayed()));
    }
}
