package com.health.openworkout.core.workout

import com.health.openworkout.R
import com.health.openworkout.core.datatypes.WorkoutItem

class AbdominalCrunch : WorkoutItem() {
    init {
        name = context.getString(R.string.workout_name_abdominal_crunch)
        description = context.getString(R.string.workout_description_abdominal_crunch)
        imagePath = "abdonminal_crunch.png"
        videoPath = "abdonminal_crunch.mp4"
    }
}
