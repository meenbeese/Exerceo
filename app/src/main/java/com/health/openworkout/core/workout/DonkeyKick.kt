package com.health.openworkout.core.workout

import com.health.openworkout.R
import com.health.openworkout.core.datatypes.WorkoutItem

class DonkeyKick : WorkoutItem() {
    init {
        name = context.getString(R.string.workout_name_donkey_kick)
        description = context.getString(R.string.workout_description_donkey_kick)
        imagePath = "donkey_kick.png"
        videoPath = "donkey_kick.mp4"
        isTimeMode = false
    }
}
