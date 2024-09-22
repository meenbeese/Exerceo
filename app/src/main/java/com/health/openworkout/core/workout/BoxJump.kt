package com.health.openworkout.core.workout

import com.health.openworkout.R
import com.health.openworkout.core.datatypes.WorkoutItem

class BoxJump : WorkoutItem() {
    init {
        name = context.getString(R.string.workout_name_box_jump)
        description = context.getString(R.string.workout_description_box_jump)
        imagePath = "box_jump.png"
        videoPath = "box_jump.mp4"
        isTimeMode = false
    }
}
