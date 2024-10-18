package com.health.openworkout.core.alarm;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.health.openworkout.MainActivity;
import com.health.openworkout.R;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static android.content.Context.NOTIFICATION_SERVICE;

public class AlarmHandler {
    public static final String INTENT_EXTRA_ALARM = "alarmIntent";
    private static final int ALARM_NOTIFICATION_ID = 0x01;

    private final String TAG = getClass().getSimpleName();

    public void scheduleAlarms(Context context) {
        AlarmEntryReader reader = AlarmEntryReader.construct(context);
        Set<AlarmEntry> alarmEntries = reader.getEntries();

        disableAllAlarms(context);
        enableAlarms(context, alarmEntries);
    }

    private void enableAlarms(Context context, Set<AlarmEntry> alarmEntries) {
        for (AlarmEntry alarmEntry : alarmEntries) {
            enableAlarm(context, alarmEntry);
        }
    }

    private void enableAlarm(Context context, AlarmEntry alarmEntry) {
        int dayOfWeek = alarmEntry.getDayOfWeek();
        Calendar nextAlarmTimestamp = alarmEntry.getNextTimestamp();

        setRepeatingAlarm(context, dayOfWeek, nextAlarmTimestamp);
    }

    private void setRepeatingAlarm(Context context, int dayOfWeek, Calendar nextAlarmTimestamp) {
        Log.d(TAG,"Set repeating alarm for " + nextAlarmTimestamp.getTime());
        PendingIntent alarmPendingIntent = getPendingAlarmIntent(context, dayOfWeek);
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, nextAlarmTimestamp.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY * 7, alarmPendingIntent);
    }

    private List<PendingIntent> getWeekdaysPendingAlarmIntent(Context context) {
        final int[] dayOfWeeks = {Calendar.MONDAY, Calendar.TUESDAY,
                Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY,
                Calendar.SATURDAY, Calendar.SUNDAY};

        List<PendingIntent> pendingIntents = new LinkedList<>();

        for (int dayOfWeek : dayOfWeeks) {
            pendingIntents.add(getPendingAlarmIntent(context, dayOfWeek));
        }

        return pendingIntents;
    }

    private PendingIntent getPendingAlarmIntent(Context context, int dayOfWeek) {
        Intent alarmIntent = new Intent(context, ReminderBootReceiver.class);
        alarmIntent.putExtra(INTENT_EXTRA_ALARM, true);

        return PendingIntent.getBroadcast(context, dayOfWeek, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void disableAllAlarms(Context context) {
        Log.d(TAG, "Disable all alarm handlers");
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        List<PendingIntent> pendingIntents = getWeekdaysPendingAlarmIntent(context);

        for (PendingIntent pendingIntent : pendingIntents) {
            alarmMgr.cancel(pendingIntent);
        }
    }

    public void showAlarmNotification(Context context) {
        AlarmEntryReader reader = AlarmEntryReader.construct(context);
        String notifyText = reader.getNotificationText();

        Intent notifyIntent = new Intent(context, MainActivity.class);

        PendingIntent notifyPendingIntent =
                PendingIntent.getActivity(context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "openWorkout_notify");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(
                    "openWorkout_notify",
                    "openWorkout notification",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        Notification notification = mBuilder.setSmallIcon(R.drawable.ic_openworkout)
                                            .setContentTitle(context.getString(R.string.app_name))
                                            .setContentText(notifyText)
                                            .setAutoCancel(true)
                                            .setContentIntent(notifyPendingIntent)
                                            .build();

        NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(ALARM_NOTIFICATION_ID, notification);
    }
}
