package com.openclassrooms.go4lunch.notifications;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import com.google.gson.Gson;
import com.openclassrooms.go4lunch.R;
import com.openclassrooms.go4lunch.model.Restaurant;
import com.openclassrooms.go4lunch.ui.activities.MainActivity;
import com.openclassrooms.go4lunch.utils.AppInfo;

/**
 * This class is used to launch notifications to user
 */
public class NotificationHandler {

    private final Context context;
    private static NotificationManager manager;
    private final String CHANNELID = "CHANNELID";

    public NotificationHandler(Context context) {
        this.context = context;
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    /**
     * Creates and launch a notification.
     */
    public void createNotification() {
        // Get restaurant name
        SharedPreferences sharedPrefSelection = context.getSharedPreferences(AppInfo.FILE_PREF_SELECTED_RESTAURANT, Context.MODE_PRIVATE);
        String savedRestaurantJSON = sharedPrefSelection.getString(AppInfo.PREF_SELECTED_RESTAURANT_KEY, "");
        if (!savedRestaurantJSON.equals("")) {
            Gson gson = new Gson();
            Restaurant restaurant = gson.fromJson(savedRestaurantJSON, Restaurant.class);
            String title = context.getResources().getString(R.string.app_name) + " " +
                    context.getResources().getString(R.string.notification_reservation) + " "
                    + restaurant.getName();
            String text = restaurant.getAddress();

            // Build notification
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNELID)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setAutoCancel(true)
                    .setPriority(Notification.PRIORITY_DEFAULT);

            // Create pendingIntent
            Intent intent = new Intent(context, MainActivity.class);
            intent.setAction(savedRestaurantJSON);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            builder.setContentIntent(pendingIntent);

            // Add channel
            createChannel();

            // Display notification
            int notificationId = 0;
            manager.notify(notificationId, builder.build());
        }
    }

    /**
     * Creates a channel to associate with the current notification.
     */
    private void createChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel channel = new NotificationChannel(CHANNELID, "channel", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Description");
            manager.createNotificationChannel(channel);
        }
    }

    /**
     * Displays a RestaurantDetailsFragment after user clicked on notification
     * @param activity : main activity
     */
    public static void getActionFromNotification(Activity activity) {
        if (activity.getIntent().getAction() != null) {
            Gson gson = new Gson();
            String restaurantNotification = activity.getIntent().getAction();
            Restaurant restaurantToDisplay = gson.fromJson(restaurantNotification, Restaurant.class);
            ((MainActivity) activity).setRestaurantToDisplay(restaurantToDisplay);
            ((MainActivity) activity).displayRestaurantDetailsFragment();
        }
    }
}
