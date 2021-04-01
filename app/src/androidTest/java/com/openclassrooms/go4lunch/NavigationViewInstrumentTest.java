package com.openclassrooms.go4lunch;

import android.content.Context;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import com.openclassrooms.go4lunch.matchers.NavigationViewMatchers;
import com.openclassrooms.go4lunch.ui.activities.MainActivity;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

/**
 * File providing tests to check NavigationView implementation on @{@link MainActivity}.
 */
@RunWith(AndroidJUnit4.class)
public class NavigationViewInstrumentTest {

    private Context context;

    @Rule
    public final ActivityScenarioRule rule = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setUp() {
        this.context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        // Opening Navigation Drawer
        onView(withContentDescription(R.string.navigation_drawer_open)).perform(click());
    }

    /**
     * TEST # 1 : Checks that all NavigationView items have the correct title.
     */
    @Test
    public void test_check_navigation_view_items_title(){
        // Check titles
        onView(withId(R.id.your_lunch_option)).check(matches(
                NavigationViewMatchers.withTitle(context.getResources()
                                                            .getString(R.string.icon_your_lunch))));
        onView(withId(R.id.settings_options)).check(matches(
                NavigationViewMatchers.withTitle(context.getResources()
                                                            .getString(R.string.icon_settings))));
        onView(withId(R.id.logout_options)).check(matches(
                NavigationViewMatchers.withTitle(context.getResources()
                                                            .getString(R.string.icon_logout))));
    }

    /**
     * TEST # 2 : Checks that the logout dialog is correctly displayed to user after a click
     * on the associated item in the NavigationDrawer.
     */
    @Test
    public void test_check_if_click_on_logout_options_item_displays_logout_dialog() {
        // Click on Logout option item
        onView(withId(R.id.logout_options)).perform(click());

        // Check if dialog is displayed
        onView(withText(context.getResources().getString(R.string.title_logout_dialog))).check(matches(isDisplayed()));
    }

    /**
     * TEST # 2 : Checks that the Options fragment is correctly displayed to user after a click
     * on the associated item in the NavigationDrawer.
     */
    @Test
    public void test_check_if_click_on_settings_options_item_displays_options_fragment() {
        // Click on Logout option item
        onView(withId(R.id.settings_options)).perform(click());
        // Display OptionsFragment
        onView(withId(R.id.fragment_options_root_layout)).check(matches(isDisplayed()));
    }

}
