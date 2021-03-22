package com.openclassrooms.go4lunch.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.openclassrooms.go4lunch.receivers.AlarmBroadcastReceiver;
import java.util.Calendar;

public class AlarmHandler {

    private Context context;
    private AlarmManager alarmManager;

    public AlarmHandler(Context context) {
        this.context = context;
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    /**
     * This method is used to initialize an AlarmManager, according to the user-defined hour.
     */
    public void startAlarm(Calendar calendarAlarm) {
        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, intent, 0);


        // If hour of the day has already passed, schedule for next day
        if (Calendar.getInstance().getTimeInMillis() - calendarAlarm.getTimeInMillis() > 0)
            calendarAlarm.add(Calendar.DAY_OF_YEAR, 1);

        // Configure alarm
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendarAlarm.getTimeInMillis(), pendingIntent);
    }

    /**
     * This method is used to cancel an AlarmManager, previously enabled by user.
     */
    public void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, intent, 0);
        alarmManager.cancel(pendingIntent);
    }
}
