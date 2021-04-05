package com.openclassrooms.go4lunch.matchers;

import android.view.View;
import androidx.test.espresso.matcher.BoundedMatcher;
import com.google.android.material.textfield.TextInputEditText;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

/**
 * Class containing static Matcher method for TextInputEditText Search View
 * from MainActivity (used for autocomplete functionality)
 * (See @{@link com.openclassrooms.go4lunch.SearchTextInputEditTextTest} file).
 */
public class SearchTextInputEditTextMatchers {

    public static Matcher<View> withVisibility(final int visibility) {
        return new BoundedMatcher<View, TextInputEditText>(TextInputEditText.class) {

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
            protected boolean matchesSafely(TextInputEditText item) {
                return item.getVisibility() == visibility;
            }
        };
    }
}
