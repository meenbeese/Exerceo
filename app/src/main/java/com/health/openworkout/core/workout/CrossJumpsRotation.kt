package com.health.openworkout.core.workout

import com.health.openworkout.R
import com.health.openworkout.core.datatypes.WorkoutItem

class CrossJumpsRotation : WorkoutItem() {
    init {
        name = context.getString(R.string.workout_name_cross_jump_rotation)
        description = context.getString(R.string.workout_description_cross_jump_rotation)
        imagePath = "cross_jump_rotation.png"
        videoPath = "cross_jump_rotation.mp4"
    }
}
