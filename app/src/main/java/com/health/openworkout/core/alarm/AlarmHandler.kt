package com.health.openworkout.core.alarm

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log

import androidx.core.app.NotificationCompat

import com.health.openworkout.MainActivity
import com.health.openworkout.R
import com.health.openworkout.core.alarm.AlarmEntryReader.Companion.construct

import java.util.Calendar
import java.util.LinkedList

class AlarmHandler {
    private val TAG: String = javaClass.simpleName

    fun scheduleAlarms(context: Context) {
        val reader = construct(context)
        val alarmEntries = reader.entries

        disableAllAlarms(context)
        enableAlarms(context, alarmEntries)
    }

    private fun enableAlarms(context: Context, alarmEntries: Set<AlarmEntry>) {
        for (alarmEntry in alarmEntries) {
            enableAlarm(context, alarmEntry)
        }
    }

    private fun enableAlarm(context: Context, alarmEntry: AlarmEntry) {
        val dayOfWeek = alarmEntry.dayOfWeek
        val nextAlarmTimestamp = alarmEntry.nextTimestamp

        setRepeatingAlarm(context, dayOfWeek, nextAlarmTimestamp)
    }

    private fun setRepeatingAlarm(context: Context, dayOfWeek: Int, nextAlarmTimestamp: Calendar) {
        Log.d(TAG, "Set repeating alarm for " + nextAlarmTimestamp.time)
        val alarmPendingIntent = getPendingAlarmIntent(context, dayOfWeek)
        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmMgr.setInexactRepeating(
            AlarmManager.RTC_WAKEUP, nextAlarmTimestamp.timeInMillis,
            AlarmManager.INTERVAL_DAY * 7, alarmPendingIntent
        )
    }

    private fun getWeekdaysPendingAlarmIntent(context: Context): List<PendingIntent> {
        val dayOfWeeks = intArrayOf(
            Calendar.MONDAY, Calendar.TUESDAY,
            Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY,
            Calendar.SATURDAY, Calendar.SUNDAY
        )

        val pendingIntents: MutableList<PendingIntent> = LinkedList()

        for (dayOfWeek in dayOfWeeks) {
            pendingIntents.add(getPendingAlarmIntent(context, dayOfWeek))
        }

        return pendingIntents
    }

    private fun getPendingAlarmIntent(context: Context, dayOfWeek: Int): PendingIntent {
        val alarmIntent = Intent(
            context,
            ReminderBootReceiver::class.java
        )
        alarmIntent.putExtra(INTENT_EXTRA_ALARM, true)

        return PendingIntent.getBroadcast(
            context,
            dayOfWeek,
            alarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    fun disableAllAlarms(context: Context) {
        Log.d(TAG, "Disable all alarm handlers")
        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntents = getWeekdaysPendingAlarmIntent(context)

        for (pendingIntent in pendingIntents) {
            alarmMgr.cancel(pendingIntent)
        }
    }

    fun showAlarmNotification(context: Context) {
        val reader = construct(context)
        val notifyText = reader.notificationText

        val notifyIntent = Intent(context, MainActivity::class.java)

        val notifyPendingIntent = PendingIntent.getActivity(context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val mBuilder = NotificationCompat.Builder(context, "openWorkout_notify")

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(
            "openWorkout_notify",
            "openWorkout notification",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)

        val notification = mBuilder.setSmallIcon(R.drawable.ic_openworkout)
            .setContentTitle(context.getString(R.string.app_name))
            .setContentText(notifyText)
            .setAutoCancel(true)
            .setContentIntent(notifyPendingIntent)
            .build()

        val mNotifyMgr = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotifyMgr.notify(ALARM_NOTIFICATION_ID, notification)
    }

    companion object {
        const val INTENT_EXTRA_ALARM: String = "alarmIntent"
        private const val ALARM_NOTIFICATION_ID = 0x01
    }
}
