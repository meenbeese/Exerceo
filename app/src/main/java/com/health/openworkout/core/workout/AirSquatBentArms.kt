package com.health.openworkout.core.workout

import com.health.openworkout.R
import com.health.openworkout.core.datatypes.WorkoutItem

class AirSquatBentArms : WorkoutItem() {
    init {
        name = context.getString(R.string.workout_name_air_squad_bent_arms)
        description = context.getString(R.string.workout_description_air_squad_bent_arms)
        imagePath = "air_squad_bent_arms.png"
        videoPath = "air_squad_bent_arms.mp4"
        isTimeMode = false
    }
}
