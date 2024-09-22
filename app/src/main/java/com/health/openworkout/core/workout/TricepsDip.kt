package com.health.openworkout.core.workout

import com.health.openworkout.R
import com.health.openworkout.core.datatypes.WorkoutItem

class TricepsDip : WorkoutItem() {
    init {
        name = context.getString(R.string.workout_name_triceps_dip)
        description = context.getString(R.string.workout_description_triceps_dip)
        imagePath = "tricep_dips.png"
        videoPath = "tricep_dips.mp4"
    }
}
