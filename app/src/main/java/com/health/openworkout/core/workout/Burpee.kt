package com.health.openworkout.core.workout

import com.health.openworkout.R
import com.health.openworkout.core.datatypes.WorkoutItem

class Burpee : WorkoutItem() {
    init {
        name = context.getString(R.string.workout_name_burpee)
        description = context.getString(R.string.workout_description_burpee)
        imagePath = "burpee.png"
        videoPath = "burpee.mp4"
        isTimeMode = false
    }
}
