package com.health.openworkout.core.workout

import com.health.openworkout.R
import com.health.openworkout.core.datatypes.WorkoutItem

class SidePlank : WorkoutItem() {
    init {
        name = context.getString(R.string.workout_name_side_plank)
        description = context.getString(R.string.workout_description_side_plank)
        imagePath = "side_plank.png"
        videoPath = "side_plank.mp4"
    }
}
