package com.health.openworkout.core.workout

import com.health.openworkout.R
import com.health.openworkout.core.datatypes.WorkoutItem

class BicycleCrunch : WorkoutItem() {
    init {
        name = context.getString(R.string.workout_name_bicycle_crunch)
        description = context.getString(R.string.workout_description_bicycle_crunch)
        imagePath = "bicycle_crunch.png"
        videoPath = "bicycle_crunch.mp4"
        isTimeMode = false
    }
}
