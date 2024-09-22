package com.health.openworkout.core.workout

import com.health.openworkout.R
import com.health.openworkout.core.datatypes.WorkoutItem

class PushUpRotation : WorkoutItem() {
    init {
        name = context.getString(R.string.workout_name_push_up_rotation)
        description = context.getString(R.string.workout_description_push_up_rotation)
        imagePath = "push_up_rotation.png"
        videoPath = "push_up_rotation.mp4"
    }
}
