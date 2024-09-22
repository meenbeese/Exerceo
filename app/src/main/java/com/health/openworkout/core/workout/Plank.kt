package com.health.openworkout.core.workout

import com.health.openworkout.R
import com.health.openworkout.core.datatypes.WorkoutItem

class Plank : WorkoutItem() {
    init {
        name = context.getString(R.string.workout_name_plank)
        description = context.getString(R.string.workout_description_plank)
        imagePath = "plank.png"
        videoPath = "plank.mp4"
    }
}
