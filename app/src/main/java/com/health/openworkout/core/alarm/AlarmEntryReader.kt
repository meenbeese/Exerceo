package com.health.openworkout.core.alarm

import android.content.Context
import android.preference.PreferenceManager

import com.health.openworkout.R

import java.util.Calendar
import java.util.TreeSet

class AlarmEntryReader private constructor(
    val entries: Set<AlarmEntry>,
    val notificationText: String
) {
    companion object {
        @JvmStatic
        fun construct(context: Context): AlarmEntryReader {
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)

            val reminderDays = prefs.getStringSet("reminderDays", HashSet())!!
            val reminderTimeInMillis = prefs.getLong("reminderTime", System.currentTimeMillis())
            val notifyText = prefs.getString("reminderNotifyText", context.resources.getString(R.string.preference_reminder_default_text))!!

            val alarms: MutableSet<AlarmEntry> = TreeSet()

            for (dayOfWeek in reminderDays) {
                val alarm = getAlarmEntry(dayOfWeek, reminderTimeInMillis)
                alarms.add(alarm)
            }

            return AlarmEntryReader(alarms, notifyText)
        }

        private fun getAlarmEntry(dayOfWeek: String, reminderTimeInMillis: Long): AlarmEntry {
            return when (dayOfWeek) {
                "Monday" -> AlarmEntry(Calendar.MONDAY, reminderTimeInMillis)
                "Tuesday" -> AlarmEntry(Calendar.TUESDAY, reminderTimeInMillis)
                "Wednesday" -> AlarmEntry(Calendar.WEDNESDAY, reminderTimeInMillis)
                "Thursday" -> AlarmEntry(Calendar.THURSDAY, reminderTimeInMillis)
                "Friday" -> AlarmEntry(Calendar.FRIDAY, reminderTimeInMillis)
                "Saturday" -> AlarmEntry(Calendar.SATURDAY, reminderTimeInMillis)
                else -> AlarmEntry(Calendar.SUNDAY, reminderTimeInMillis)
            }
        }
    }
}
