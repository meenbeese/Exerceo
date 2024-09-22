package com.health.openworkout.core.workout

import com.health.openworkout.R
import com.health.openworkout.core.datatypes.WorkoutItem

class Lunge : WorkoutItem() {
    init {
        name = context.getString(R.string.workout_name_lunge)
        description = context.getString(R.string.workout_description_lunge)
        imagePath = "lunge.png"
        videoPath = "lunge.mp4"
    }
}
