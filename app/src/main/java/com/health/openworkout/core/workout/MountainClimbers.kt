package com.health.openworkout.core.workout

import com.health.openworkout.R
import com.health.openworkout.core.datatypes.WorkoutItem

class MountainClimbers : WorkoutItem() {
    init {
        name = context.getString(R.string.workout_name_mountain_climbers)
        description = context.getString(R.string.workout_description_mountain_climbers)
        imagePath = "mountain_climbers.png"
        videoPath = "mountain_climbers.mp4"
    }
}
