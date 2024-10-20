package com.health.openworkout.gui.preference

import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import android.widget.TimePicker

import androidx.preference.PreferenceDialogFragmentCompat

import com.health.openworkout.R

import java.util.Calendar

class TimePreferenceDialog : PreferenceDialogFragmentCompat() {
    private lateinit var calendar: Calendar
    private lateinit var timePicker: TimePicker

    override fun onBindDialogView(view: View) {
        super.onBindDialogView(view)

        timePicker = view.findViewById(R.id.timePicker)
        calendar = Calendar.getInstance()

        (preference as? TimePreference)?.timeInMillis?.let { timeInMillis ->
            calendar.timeInMillis = timeInMillis
            val is24hour = DateFormat.is24HourFormat(context)

            timePicker.apply {
                setIs24HourView(is24hour)
                hour = calendar.get(Calendar.HOUR_OF_DAY)
                minute = calendar.get(Calendar.MINUTE)
            }
        }
    }

    override fun onDialogClosed(positiveResult: Boolean) {
        if (positiveResult) {
            val hours = timePicker.hour
            val minutes = timePicker.minute

            calendar.apply {
                set(Calendar.HOUR_OF_DAY, hours)
                set(Calendar.MINUTE, minutes)
            }

            val timeInMillis = calendar.timeInMillis

            (preference as? TimePreference)?.let { pref ->
                if (pref.callChangeListener(timeInMillis)) {
                    pref.timeInMillis = timeInMillis
                    pref.summary = DateFormat.getTimeFormat(context).format(calendar.time)
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(key: String?): TimePreferenceDialog {
            return TimePreferenceDialog().apply {
                arguments = Bundle(1).apply {
                    putString(ARG_KEY, key)
                }
            }
        }
    }
}
