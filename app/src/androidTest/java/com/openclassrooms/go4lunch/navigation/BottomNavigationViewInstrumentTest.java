package com.openclassrooms.go4lunch.navigation;

import android.content.Context;
import androidx.lifecycle.Lifecycle;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.openclassrooms.go4lunch.R;
import com.openclassrooms.go4lunch.matchers.BottomNavigationViewMatchers;
import com.openclassrooms.go4lunch.ui.activities.MainActivity;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
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
        rule.getScenario().moveToState(Lifecycle.State.RESUMED);
    }

    @After
    public void endTest() {
        rule.getScenario().close();
    }

    /**
     * TEST # 1 : Checks that all BottomNavigationView items have the correct title.
     */
    @Test
    public void test_check_bottom_navigation_view_items_title() {
       onView(ViewMatchers.withId(R.id.map)).check(matches(BottomNavigationViewMatchers
                               .withTitle(context.getString(R.string.icon_map_view))));
       onView(withId(R.id.list)).check(matches(BottomNavigationViewMatchers
                                .withTitle(context.getString(R.string.icon_list_view))));
       onView(withId(R.id.workmates)).check(matches(BottomNavigationViewMatchers
                                     .withTitle(context.getString(R.string.icon_workmates))));
    }

    /**
     * TEST # 2 : Checks that all BottomNavigationView items have the "checked" status at launch.
     */
    @Test
    public void test_check_bottom_navigation_view_item_selection() {
        onView(withId(R.id.map)).check(matches(BottomNavigationViewMatchers
                                .withItemChecked(true)));
        onView(withId(R.id.list)).check(matches(BottomNavigationViewMatchers
                                 .withItemChecked(false)));
        onView(withId(R.id.workmates)).check(matches(BottomNavigationViewMatchers
                                      .withItemChecked(false)));
    }
}
