package com.health.openworkout.core.training

import com.health.openworkout.R
import com.health.openworkout.core.datatypes.TrainingPlan
import com.health.openworkout.core.session.SevenMinutesSession

class SevenMinutesTraining : TrainingPlan() {
    init {
        name = context.getString(R.string.training_seven_minutes_workout_training)
        imagePath = "sevenMinutesTraining.png"

        for (i in 1..7) {
            val session = SevenMinutesSession()

            session.name = String.format(context.getString(R.string.day_unit), i)
            addWorkoutSession(session)
        }
    }
}
