package com.health.openworkout.gui.preference

import android.content.Context
import android.content.res.TypedArray
import android.text.format.DateFormat
import android.util.AttributeSet

import androidx.fragment.app.FragmentActivity
import androidx.preference.Preference

import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.health.openworkout.R

import java.util.Calendar

class TimePreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.preferenceStyle,
    defStyleRes: Int = defStyleAttr
) : Preference(context, attrs, defStyleAttr, defStyleRes) {
    internal var timeInMillis: Long = 0
        set(value) {
            field = value
            persistLong(field)
            summary = DateFormat.getTimeFormat(context).format(Calendar.getInstance().apply {
                timeInMillis = value
            }.time)
        }

    init {
        summary = DateFormat.getTimeFormat(context).format(Calendar.getInstance().apply {
            timeInMillis = timeInMillis
        }.time)
    }

    override fun onGetDefaultValue(a: TypedArray, index: Int): Any {
        val defaultTime = Calendar.getInstance()
        defaultTime[Calendar.HOUR_OF_DAY] = 16
        defaultTime[Calendar.MINUTE] = 0

        return defaultTime.timeInMillis
    }

    override fun onSetInitialValue(defaultValue: Any?) {
        timeInMillis = if (shouldPersist()) {
            getPersistedLong(timeInMillis)
        } else {
            defaultValue as? Long ?: 0L
        }
    }

    public override fun onClick() {
        super.onClick()

        val calendar = Calendar.getInstance().apply {
            timeInMillis = timeInMillis
        }
        val is24hour = DateFormat.is24HourFormat(context)
        val timeFormat = if (is24hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H

        val timePicker = MaterialTimePicker.Builder()
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

            if (callChangeListener(timeInMillis)) {
                this.timeInMillis = timeInMillis
            }
        }

        timePicker.show((context as FragmentActivity).supportFragmentManager, "MATERIAL_TIME_PICKER")
    }
}
