package com.health.openworkout.core.workout

import com.health.openworkout.R
import com.health.openworkout.core.datatypes.WorkoutItem

class HighKnees : WorkoutItem() {
    init {
        name = context.getString(R.string.workout_name_high_knees)
        description = context.getString(R.string.workout_description_high_knees)
        imagePath = "high_knees.png"
        videoPath = "high_knees.mp4"
    }
}
