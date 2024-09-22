package com.health.openworkout.core.workout

import com.health.openworkout.R
import com.health.openworkout.core.datatypes.WorkoutItem

class QuickSteps : WorkoutItem() {
    init {
        name = context.getString(R.string.workout_name_quick_steps)
        description = context.getString(R.string.workout_description_quick_steps)
        imagePath = "quick_steps.png"
        videoPath = "quick_steps.mp4"
    }
}
