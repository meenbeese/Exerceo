package com.health.openworkout.core.session

import com.health.openworkout.core.datatypes.WorkoutItem
import com.health.openworkout.core.datatypes.WorkoutSession
import com.health.openworkout.core.workout.AbdominalCrunch
import com.health.openworkout.core.workout.BicycleCrunch
import com.health.openworkout.core.workout.Burpee
import com.health.openworkout.core.workout.CircleCrunch
import com.health.openworkout.core.workout.CrossJumps
import com.health.openworkout.core.workout.HighKnees
import com.health.openworkout.core.workout.JumpingJack
import com.health.openworkout.core.workout.Lunge
import com.health.openworkout.core.workout.PikeWalk
import com.health.openworkout.core.workout.Plank
import com.health.openworkout.core.workout.PushUpRotation
import com.health.openworkout.core.workout.QuickSteps
import com.health.openworkout.core.workout.SidePlank
import com.health.openworkout.core.workout.Squat
import com.health.openworkout.core.workout.WallSit

class AbdominalMuscleSession(dayNr: Int, private val stressFac: Float) : WorkoutSession() {
    init {
        when (dayNr) {
            0 -> {
                addWorkoutTime(JumpingJack(), 15)
                addWorkoutRep(CircleCrunch(), 5)
                addWorkoutRep(PikeWalk(), 2)
                addWorkoutTime(SidePlank(), 5)
                addWorkoutRep(AbdominalCrunch(), 5)
                addWorkoutTime(Plank(), 5)
            }
            1 -> {
                addWorkoutTime(JumpingJack(), 15)
                addWorkoutRep(AbdominalCrunch(), 10)
                addWorkoutTime(SidePlank(), 5)
                addWorkoutTime(HighKnees(), 15)
                addWorkoutRep(CircleCrunch(), 10)
                addWorkoutTime(Plank(), 10)
            }
            2 -> {
                addWorkoutTime(JumpingJack(), 18)
                addWorkoutRep(Burpee(), 4)
                addWorkoutTime(SidePlank(), 10)
                addWorkoutRep(PushUpRotation(), 10)
                addWorkoutRep(AbdominalCrunch(), 10)
                addWorkoutTime(Plank(), 15)
            }
            3 -> {
                addWorkoutTime(JumpingJack(), 22)
                addWorkoutRep(BicycleCrunch(), 10)
                addWorkoutRep(Lunge(), 12)
                addWorkoutRep(PikeWalk(), 4)
                addWorkoutTime(Plank(), 15)
            }
            4 -> {
                addWorkoutTime(JumpingJack(), 25)
                addWorkoutTime(CrossJumps(), 20)
                addWorkoutTime(SidePlank(), 20)
                addWorkoutRep(AbdominalCrunch(), 12)
                addWorkoutRep(Squat(), 12)
                addWorkoutTime(Plank(), 18)
            }
            5 -> {
                addWorkoutTime(HighKnees(), 30)
                addWorkoutRep(AbdominalCrunch(), 15)
                addWorkoutRep(PikeWalk(), 15)
                addWorkoutTime(QuickSteps(), 15)
                addWorkoutRep(CircleCrunch(), 15)
                addWorkoutTime(Plank(), 20)
            }
            6 -> {
                addWorkoutTime(JumpingJack(), 30)
                addWorkoutRep(Burpee(), 5)
                addWorkoutTime(WallSit(), 20)
                addWorkoutRep(AbdominalCrunch(), 16)
                addWorkoutTime(Plank(), 22)
            }
            7 -> {
                addWorkoutTime(JumpingJack(), 35)
                addWorkoutRep(AbdominalCrunch(), 16)
                addWorkoutTime(HighKnees(), 15)
                addWorkoutRep(CircleCrunch(), 15)
                addWorkoutTime(SidePlank(), 20)
                addWorkoutTime(Plank(), 25)
            }
        }
    }

    private fun addWorkoutTime(workoutItem: WorkoutItem, time: Int) {
        workoutItem.workoutTime = Math.round(time * stressFac)
        addWorkout(workoutItem)
    }

    private fun addWorkoutRep(workoutItem: WorkoutItem, rep: Int) {
        workoutItem.isTimeMode = false
        workoutItem.repetitionCount = Math.round(rep * stressFac)
        addWorkout(workoutItem)
    }
}
