package com.health.openworkout.core.session

import com.health.openworkout.core.datatypes.WorkoutSession
import com.health.openworkout.core.workout.AbdominalCrunch
import com.health.openworkout.core.workout.HighKnees
import com.health.openworkout.core.workout.JumpingJack
import com.health.openworkout.core.workout.Lunge
import com.health.openworkout.core.workout.Plank
import com.health.openworkout.core.workout.PushUpRotation
import com.health.openworkout.core.workout.PushUps
import com.health.openworkout.core.workout.SidePlank
import com.health.openworkout.core.workout.Squat
import com.health.openworkout.core.workout.StepUp
import com.health.openworkout.core.workout.TricepsDip
import com.health.openworkout.core.workout.WallSit

class SevenMinutesSession : WorkoutSession() {
    init {
        addWorkout(JumpingJack())
        addWorkout(WallSit())
        addWorkout(PushUps())
        addWorkout(AbdominalCrunch())
        addWorkout(StepUp())
        addWorkout(Squat())
        addWorkout(TricepsDip())
        addWorkout(Plank())
        addWorkout(HighKnees())
        addWorkout(Lunge())
        addWorkout(PushUpRotation())
        addWorkout(SidePlank())
        for (w in this.getWorkoutItems()) {
            w.breakTime = 10
        }
    }
}
