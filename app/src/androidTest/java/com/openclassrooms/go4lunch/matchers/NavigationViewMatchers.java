package com.openclassrooms.go4lunch.matchers;

import android.view.View;
import androidx.test.espresso.matcher.BoundedMatcher;
import com.google.android.material.internal.NavigationMenuItemView;
import com.openclassrooms.go4lunch.NavigationViewInstrumentTest;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

/**
 * Class containing static Matcher method for NavigationView testing
 * (See @{@link NavigationViewInstrumentTest} file).
 */
public class NavigationViewMatchers {

    public static Matcher<View> withTitle(final String title) {
        return new BoundedMatcher<View, NavigationMenuItemView>(NavigationMenuItemView.class) {

            /**
             * Describes expected behavior.
             * @param description : description of the expected behavior (BottomNavigationItemView
             *                    "checked" status must be equal to "isChecked
             */
            @Override
            public void describeTo(Description description) {
                description.appendText("NavigationView item title must be : " + title);
            }

            /**
             * Checks if item "checked" status equals specified "isChecked" value.
             * @param item : BottomNavigationItemView
             * @return : Matcher result
             */
            @Override
            protected boolean matchesSafely(NavigationMenuItemView item) {
                return item.getItemData().getTitle().equals(title);
            }
        };
    }
}
