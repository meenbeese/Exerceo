package com.health.openworkout.core.alarm;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.health.openworkout.R;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class AlarmEntryReader {
    private final Set<AlarmEntry> alarmEntries;
    private final String alarmNotificationText;

    private AlarmEntryReader(Set<AlarmEntry> alarmEntries, String alarmNotificationText) {
        this.alarmEntries = alarmEntries;
        this.alarmNotificationText = alarmNotificationText;
    }

    public Set<AlarmEntry> getEntries()
    {
        return alarmEntries;
    }

    public String getNotificationText()
    {
        return alarmNotificationText;
    }

    public static AlarmEntryReader construct(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        Set<String> reminderDays = prefs.getStringSet("reminderDays", new HashSet<>());
        Long reminderTimeInMillis = prefs.getLong("reminderTime", System.currentTimeMillis());
        String notifyText = prefs.getString("reminderNotifyText", context.getResources().getString(R.string.preference_reminder_default_text));

        Set<AlarmEntry> alarms = new TreeSet<>();

        for (String dayOfWeek : reminderDays) {
            AlarmEntry alarm = getAlarmEntry(dayOfWeek, reminderTimeInMillis);
            alarms.add(alarm);
        }

        return new AlarmEntryReader(alarms, notifyText);
    }

    private static AlarmEntry getAlarmEntry(String dayOfWeek, Long reminderTimeInMillis) {

        return switch (dayOfWeek) {
            case "Monday" -> new AlarmEntry(Calendar.MONDAY, reminderTimeInMillis);
            case "Tuesday" -> new AlarmEntry(Calendar.TUESDAY, reminderTimeInMillis);
            case "Wednesday" -> new AlarmEntry(Calendar.WEDNESDAY, reminderTimeInMillis);
            case "Thursday" -> new AlarmEntry(Calendar.THURSDAY, reminderTimeInMillis);
            case "Friday" -> new AlarmEntry(Calendar.FRIDAY, reminderTimeInMillis);
            case "Saturday" -> new AlarmEntry(Calendar.SATURDAY, reminderTimeInMillis);
            default -> new AlarmEntry(Calendar.SUNDAY, reminderTimeInMillis);
        };
    }
}
