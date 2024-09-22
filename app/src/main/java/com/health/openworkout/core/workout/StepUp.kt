package com.health.openworkout.core.workout

import com.health.openworkout.R
import com.health.openworkout.core.datatypes.WorkoutItem

class StepUp : WorkoutItem() {
    init {
        name = context.getString(R.string.workout_name_step_up)
        description = context.getString(R.string.workout_description_step_up)
        imagePath = "step_up.png"
        videoPath = "step_up.mp4"
    }
}
