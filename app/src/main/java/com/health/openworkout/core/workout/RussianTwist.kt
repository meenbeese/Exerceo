package com.health.openworkout.core.workout

import com.health.openworkout.R
import com.health.openworkout.core.datatypes.WorkoutItem

class RussianTwist : WorkoutItem() {
    init {
        name = context.getString(R.string.workout_name_russian_twist)
        description = context.getString(R.string.workout_description_russian_twist)
        imagePath = "russian_twist.png"
        videoPath = "russian_twist.mp4"
        isTimeMode = false
    }
}
