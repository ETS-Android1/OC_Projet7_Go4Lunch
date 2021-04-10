package com.openclassrooms.go4lunch.navigation;

import android.content.Context;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import com.openclassrooms.go4lunch.R;
import com.openclassrooms.go4lunch.matchers.NavigationViewMatchers;
import com.openclassrooms.go4lunch.ui.activities.MainActivity;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.Espresso.onView;
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
        onView(ViewMatchers.withContentDescription(R.string.navigation_drawer_open)).perform(click());
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

    @After
    public void endTest() {
        rule.getScenario().close();
    }
}
