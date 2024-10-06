package com.health.openworkout.core.training;

import com.health.openworkout.R;
import com.health.openworkout.core.datatypes.TrainingPlan;
import com.health.openworkout.core.session.SevenMinutesSession;

public class SevenMinutesTraining extends TrainingPlan {
    public SevenMinutesTraining() {
        super();

        setName(getContext().getString(R.string.training_seven_minutes_workout_training));
        setImagePath("sevenMinutesTraining.png");

        for (int i=1; i<=7; i++) {
            SevenMinutesSession session = new SevenMinutesSession();

            session.name = String.format(getContext().getString(R.string.day_unit), i);
            addWorkoutSession(session);
        }
    }
}
