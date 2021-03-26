package com.openclassrooms.go4lunch.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.openclassrooms.go4lunch.notifications.NotificationHandler;

/**
 * A BroadcastReceiver used to launch a Notification at a time defined by an AlarmManager.
 */
public class AlarmBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHandler notificationHandler = new NotificationHandler(context);
        notificationHandler.createNotification();
    }
}
