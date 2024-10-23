package com.health.openworkout.core.datatypes

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Keep
@Entity
open class WorkoutSession : Comparable<WorkoutSession>, Cloneable {
    @JvmField
    @PrimaryKey(autoGenerate = true)
    var workoutSessionId: Long = 0

    @JvmField
    @ColumnInfo
    var trainingPlanId: Long = 0

    @JvmField
    @ColumnInfo
    var orderNr: Long = -1L

    @JvmField
    @ColumnInfo
    var name: String? = null

    @ColumnInfo
    var isFinished: Boolean = false

    @Ignore
    private var workoutItems: MutableList<WorkoutItem>

    init {
        workoutItems = ArrayList()
    }

    public override fun clone(): WorkoutSession {
        val clone: WorkoutSession
        try {
            clone = super.clone() as WorkoutSession
        } catch (e: CloneNotSupportedException) {
            throw RuntimeException("failed to clone WorkoutSession", e)
        }

        for (workoutItem in clone.workoutItems) {
            workoutItem.workoutItemId = 0
        }

        return clone
    }

    fun addWorkout(workoutItem: WorkoutItem): WorkoutItem {
        workoutItems.add(workoutItem)

        return workoutItem
    }

    fun setWorkoutItems(workoutItems: MutableList<WorkoutItem>) {
        this.workoutItems = workoutItems
    }

    fun getWorkoutItems(): List<WorkoutItem> {
        return workoutItems
    }

    fun getNextWorkoutItem(workoutItemOrderNr: Long): WorkoutItem? {
        // Run two iterations. In the first one check only future workoutItems. In the second one, check also workoutItems at the beginning.
        for (workoutItem in workoutItems) {
            if (!workoutItem.isFinished && workoutItem.orderNr >= workoutItemOrderNr) {
                return workoutItem
            }
        }
        for (workoutItem in workoutItems) {
            if (!workoutItem.isFinished) {
                return workoutItem
            }
        }

        return null
    }

    val elapsedSessionTime: Long
        get() {
            var elapsedSessionTime: Long = 0

            for (workoutItem in workoutItems) {
                elapsedSessionTime += workoutItem.elapsedTime
            }

            return elapsedSessionTime
        }

    override fun compareTo(other: WorkoutSession): Int {
        if (this.orderNr == -1L || other.orderNr == -1L) {
            return (this.workoutSessionId - other.workoutSessionId).toInt()
        }

        return (this.orderNr - other.orderNr).toInt()
    }
}
