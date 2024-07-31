package com.health.openworkout.core.workout;

import com.health.openworkout.R;
import com.health.openworkout.core.datatypes.WorkoutItem;

public class PikeWalk extends WorkoutItem {
    public PikeWalk() {
        super();
        setName(getContext().getString(R.string.workout_name_pike_walk));
        setDescription(getContext().getString(R.string.workout_description_pike_walk));
        setImagePath("pike_walk.png");
        setVideoPath("pike_walk.mp4");
        setTimeMode(false);
    }
}
