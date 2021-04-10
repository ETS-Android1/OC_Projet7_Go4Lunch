package com.openclassrooms.go4lunch.matchers;

import android.view.View;
import androidx.test.espresso.matcher.BoundedMatcher;
import com.google.android.material.textfield.TextInputLayout;
import com.openclassrooms.go4lunch.autocomplete.SearchTextInputLayoutTest;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

/**
 * Class containing static Matcher method for TextInputEditText Search View
 * from MainActivity (used for autocomplete functionality)
 * (See @{@link SearchTextInputLayoutTest} file).
 */
public class SearchTextInputLayoutMatchers {

    public static Matcher<View> withVisibility(final int visibility) {
        return new BoundedMatcher<View, TextInputLayout>(TextInputLayout.class) {

            @Override
            public void describeTo(Description description) {
                switch (visibility) {
                    case View.VISIBLE:
                        description.appendText("Visibility status must be : VISIBLE");
                        break;
                    case View.INVISIBLE:
                        description.appendText("Visibility status must be : INVISIBLE");
                        break;
                    case View.GONE:
                        description.appendText("Visibility status must be : GONE");
                        break;
                }
            }

            @Override
            protected boolean matchesSafely(TextInputLayout item) {
                return item.getVisibility() == visibility;
            }
        };
    }
}
