package com.openclassrooms.go4lunch.notification;

import android.content.Context;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SdkSuppress;
import androidx.test.platform.app.InstrumentationRegistry;
import com.openclassrooms.go4lunch.notifications.NotificationHandler;
import com.openclassrooms.go4lunch.ui.activities.MainActivity;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * File providing tests to check @{@link NotificationHandler} class implementation
 */
@RunWith(AndroidJUnit4.class)
public class NotificationHandlerInstrumentTest {

    private Context context;

    @Rule
    public final ActivityScenarioRule rule = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setUp() {
        this.context = InstrumentationRegistry.getInstrumentation().getTargetContext();
    }

    /**
     * TEST #1 : Checks if a notification channel is correctly created for API >= 26.
     */
    @Test
    @SdkSuppress(minSdkVersion = 26)
    public void test_notification_channel_creation() {
        String CHANNEL_ID = "CHANNEL_ID";
        NotificationHandler notificationHandler = new NotificationHandler(context);
        notificationHandler.createChannel();

        assertNotNull(notificationHandler.getManager().getNotificationChannel(CHANNEL_ID));
        assertEquals("Description", notificationHandler.getManager()
                                               .getNotificationChannel(CHANNEL_ID).getDescription());
        assertEquals("channel", notificationHandler.getManager()
                                                      .getNotificationChannel(CHANNEL_ID).getName());
    }
}
