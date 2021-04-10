package com.openclassrooms.go4lunch.matchers;

import android.view.View;
import androidx.test.espresso.matcher.BoundedMatcher;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.openclassrooms.go4lunch.navigation.BottomNavigationViewInstrumentTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

/**
 * Class containing static Matcher method for BottomNavigationView testing
 * (See @{@link BottomNavigationViewInstrumentTest} file).
 */
public class BottomNavigationViewMatchers {

    public static Matcher<View> withItemChecked(final boolean isChecked) {
        return new BoundedMatcher<View, BottomNavigationItemView>(BottomNavigationItemView.class) {

            /**
             * Describes expected behavior.
             * @param description : description of the expected behavior (BottomNavigationItemView
             *                    "checked" status must be equal to "isChecked
             */
            @Override
            public void describeTo(Description description) {
                description.appendText("BottomNavigationItemView with \"isChecked\": " + isChecked);
            }

            /**
             * Checks if item "checked" status equals specified "isChecked" value.
             * @param item : BottomNavigationItemView
             * @return : Matcher result
             */
            @Override
            protected boolean matchesSafely(BottomNavigationItemView item) {
                return item.getItemData().isChecked() == isChecked;
            }
        };
    }

    public static Matcher<View> withTitle(final String title) {
        return new BoundedMatcher<View, BottomNavigationItemView>(BottomNavigationItemView.class) {

            /**
             * Describes expected behavior.
             * @param description : description of the expected behavior (BottomNavigationItemView
             *                      title value must be equals to "title"
             */
            @Override
            public void describeTo(Description description) {
                description.appendText("BottomNavigationItemView \"title\" must be : " + title);
            }

            /**
             * Checks if item title equals specified "title" value.
             * @param item : BottomNavigationItemView
             * @return : Matcher result
             */
            @Override
            protected boolean matchesSafely(BottomNavigationItemView item) {
                return item.getItemData().getTitle().equals(title);
            }
        };
    }
}
