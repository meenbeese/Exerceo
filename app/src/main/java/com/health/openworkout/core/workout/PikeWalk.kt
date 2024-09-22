package com.health.openworkout.core.workout

import com.health.openworkout.R
import com.health.openworkout.core.datatypes.WorkoutItem

class PikeWalk : WorkoutItem() {
    init {
        name = context.getString(R.string.workout_name_pike_walk)
        description = context.getString(R.string.workout_description_pike_walk)
        imagePath = "pike_walk.png"
        videoPath = "pike_walk.mp4"
        isTimeMode = false
    }
}
