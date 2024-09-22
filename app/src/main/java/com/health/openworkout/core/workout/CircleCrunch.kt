package com.health.openworkout.core.workout

import com.health.openworkout.R
import com.health.openworkout.core.datatypes.WorkoutItem

class CircleCrunch : WorkoutItem() {
    init {
        name = context.getString(R.string.workout_name_circle_crunch)
        description = context.getString(R.string.workout_description_circle_crunch)
        imagePath = "circle_crunch.png"
        videoPath = "circle_crunch.mp4"
        isTimeMode = false
    }
}
