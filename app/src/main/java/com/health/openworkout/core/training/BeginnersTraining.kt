package com.health.openworkout.core.training

import com.health.openworkout.R
import com.health.openworkout.core.datatypes.TrainingPlan
import com.health.openworkout.core.session.BeginnersSession

class BeginnersTraining : TrainingPlan() {
    init {
        name = context.getString(R.string.training_beginners_training)
        imagePath = "beginnersTraining.png"

        var stressFac = 1.0f

        for (i in 0..27) {
            // On every week increase the stress factor
            if (i % 8 == 7) {
                stressFac += 0.2f
            }

            val session = BeginnersSession(i % 8, stressFac)

            session.name = String.format(context.getString(R.string.day_unit), i + 1)
            addWorkoutSession(session)
        }
    }
}
