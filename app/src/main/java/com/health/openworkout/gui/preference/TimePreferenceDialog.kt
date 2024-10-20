package com.health.openworkout.gui.preference

import android.app.Dialog
import android.os.Bundle
import android.text.format.DateFormat

import androidx.preference.PreferenceDialogFragmentCompat

import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.health.openworkout.R

import java.util.Calendar

class TimePreferenceDialog : PreferenceDialogFragmentCompat() {
    private lateinit var calendar: Calendar
    private lateinit var timePicker: MaterialTimePicker

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        calendar = Calendar.getInstance()

        (preference as? TimePreference)?.timeInMillis?.let { timeInMillis ->
            calendar.timeInMillis = timeInMillis
            val is24hour = DateFormat.is24HourFormat(context)
            val timeFormat = if (is24hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H

            timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(timeFormat)
                .setHour(calendar.get(Calendar.HOUR_OF_DAY))
                .setMinute(calendar.get(Calendar.MINUTE))
                .setTitleText(R.string.select_time)
                .build()

            timePicker.addOnPositiveButtonClickListener {
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

            timePicker.show(parentFragmentManager, "MATERIAL_TIME_PICKER")
        }

        return super.onCreateDialog(savedInstanceState)
    }

    override fun onDialogClosed(positiveResult: Boolean) {}

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
