package com.health.openworkout.core.workout

import com.health.openworkout.core.datatypes.WorkoutItem

class WorkoutFactory {
    val allWorkoutItems: List<WorkoutItem>
        get() {
            val workoutItemList: MutableList<WorkoutItem> = ArrayList()

            workoutItemList.add(AbdominalCrunch())
            workoutItemList.add(AirSquatBentArms())
            workoutItemList.add(BicycleCrunch())
            workoutItemList.add(BoxJump())
            workoutItemList.add(Burpee())
            workoutItemList.add(CircleCrunch())
            workoutItemList.add(CrossJumps())
            workoutItemList.add(CrossJumpsRotation())
            workoutItemList.add(HighKnees())
            workoutItemList.add(JumpingJack())
            workoutItemList.add(JumpPushUps())
            workoutItemList.add(Lunge())
            workoutItemList.add(PikeWalk())
            workoutItemList.add(Plank())
            workoutItemList.add(PushUpRotation())
            workoutItemList.add(PushUps())
            workoutItemList.add(QuickSteps())
            workoutItemList.add(SidePlank())
            workoutItemList.add(Squat())
            workoutItemList.add(StepUp())
            workoutItemList.add(TricepsDip())
            workoutItemList.add(WallSit())
            workoutItemList.add(DonkeyKick())
            workoutItemList.add(LungeKick())
            workoutItemList.add(MountainClimbers())
            workoutItemList.add(RussianTwist())

            return workoutItemList
        }
}
