package com.health.openworkout.core.workout

import com.health.openworkout.R
import com.health.openworkout.core.datatypes.WorkoutItem

class JumpPushUps : WorkoutItem() {
    init {
        name = context.getString(R.string.workout_name_jump_push_ups)
        description = context.getString(R.string.workout_description_jump_push_ups)
        imagePath = "jump_push_ups.png"
        videoPath = "jump_push_ups.mp4"
        isTimeMode = false
    }
}
