package com.health.openworkout.core.workout

import com.health.openworkout.R
import com.health.openworkout.core.datatypes.WorkoutItem

class LungeKick : WorkoutItem() {
    init {
        name = context.getString(R.string.workout_name_lunge_kick)
        description = context.getString(R.string.workout_description_lunge_kick)
        imagePath = "lunge_kick.png"
        videoPath = "lunge_kick.mp4"
        isTimeMode = false
    }
}
