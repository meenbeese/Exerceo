package com.health.openworkout.core.workout

import com.health.openworkout.R
import com.health.openworkout.core.datatypes.WorkoutItem

class JumpingJack : WorkoutItem() {
    init {
        name = context.getString(R.string.workout_name_jumping_jack)
        description = context.getString(R.string.workout_description_jumping_jack)
        imagePath = "jumping_jack.png"
        videoPath = "jumping_jack.mp4"
    }
}
