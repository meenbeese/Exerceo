package com.health.openworkout.gui.preference

import android.content.Context
import android.content.res.TypedArray
import android.text.format.DateFormat
import android.util.AttributeSet

import androidx.preference.DialogPreference

import com.health.openworkout.R

import java.util.Calendar

class TimePreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.preferenceStyle,
    defStyleRes: Int = defStyleAttr
) : DialogPreference(context, attrs, defStyleAttr, defStyleRes) {
    internal var timeInMillis: Long = 0
        set(value) {
            field = value
            persistLong(field)
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

    override fun getSummary(): CharSequence? {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeInMillis

        return DateFormat.getTimeFormat(context).format(calendar.time)
    }
}
