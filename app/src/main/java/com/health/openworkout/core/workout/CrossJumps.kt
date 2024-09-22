package com.health.openworkout.core.workout

import com.health.openworkout.R
import com.health.openworkout.core.datatypes.WorkoutItem

class CrossJumps : WorkoutItem() {
    init {
        name = context.getString(R.string.workout_name_cross_jump)
        description = context.getString(R.string.workout_description_cross_jump)
        imagePath = "cross_jump.png"
        videoPath = "cross_jump.mp4"
    }
}
