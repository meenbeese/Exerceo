package com.health.openworkout.core.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ReminderBootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.hasExtra(AlarmHandler.INTENT_EXTRA_ALARM)) {
            handleAlarm(context)
        }

        if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
            scheduleAlarms(context)
        }
    }

    private fun handleAlarm(context: Context) {
        val alarmHandler = AlarmHandler()
        alarmHandler.showAlarmNotification(context)
    }

    private fun scheduleAlarms(context: Context) {
        val alarmHandler = AlarmHandler()
        alarmHandler.scheduleAlarms(context)
    }
}
