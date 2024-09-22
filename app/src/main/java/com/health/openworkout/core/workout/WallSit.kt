package com.health.openworkout.core.workout

import com.health.openworkout.R
import com.health.openworkout.core.datatypes.WorkoutItem

class WallSit : WorkoutItem() {
    init {
        name = context.getString(R.string.workout_name_wall_sit)
        description = context.getString(R.string.workout_description_wall_sit)
        imagePath = "wall_sit.png"
        videoPath = "wall_sit.mp4"
    }
}
