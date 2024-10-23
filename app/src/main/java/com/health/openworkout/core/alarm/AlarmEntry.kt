package com.health.openworkout.core.alarm

import java.util.Calendar

class AlarmEntry(
    @JvmField val dayOfWeek: Int,
    private val timeInMillis: Long
) : Comparable<AlarmEntry> {

    val nextTimestamp: Calendar
        get() = Calendar.getInstance().apply {
            timeInMillis = this@AlarmEntry.timeInMillis

            val alarmCal = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, get(Calendar.HOUR_OF_DAY))
                set(Calendar.MINUTE, get(Calendar.MINUTE))
                set(Calendar.SECOND, 0)
                set(Calendar.DAY_OF_WEEK, this@AlarmEntry.dayOfWeek)

                if (before(Calendar.getInstance())) {
                    add(Calendar.DAY_OF_YEAR, 7)
                }
            }

            this.timeInMillis = alarmCal.timeInMillis
        }

    override fun equals(other: Any?): Boolean {
        return other is AlarmEntry &&
                dayOfWeek == other.dayOfWeek &&
                timeInMillis == other.timeInMillis
    }

    override fun hashCode(): Int {
        return 31 * dayOfWeek + timeInMillis.hashCode()
    }

    override fun compareTo(other: AlarmEntry): Int {
        return compareValuesBy(this, other, { it.dayOfWeek }, { it.timeInMillis })
    }
}
