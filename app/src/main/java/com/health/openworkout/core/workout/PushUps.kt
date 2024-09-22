package com.health.openworkout.core.workout

import com.health.openworkout.R
import com.health.openworkout.core.datatypes.WorkoutItem

class PushUps : WorkoutItem() {
    init {
        name = context.getString(R.string.workout_name_push_ups)
        description = context.getString(R.string.workout_description_push_ups)
        imagePath = "push_ups.png"
        videoPath = "push_ups.mp4"
    }
}
