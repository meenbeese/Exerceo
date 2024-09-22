package com.health.openworkout.core.workout

import com.health.openworkout.R
import com.health.openworkout.core.datatypes.WorkoutItem

class Squat : WorkoutItem() {
    init {
        name = context.getString(R.string.workout_name_squat)
        description = context.getString(R.string.workout_description_squat)
        imagePath = "squad.png"
        videoPath = "squad.mp4"
    }
}
