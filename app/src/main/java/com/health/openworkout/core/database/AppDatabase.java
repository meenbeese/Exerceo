package com.health.openworkout.core.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.health.openworkout.core.datatypes.TrainingPlan;
import com.health.openworkout.core.datatypes.User;
import com.health.openworkout.core.datatypes.WorkoutItem;
import com.health.openworkout.core.datatypes.WorkoutSession;

@Database(entities = {User.class, TrainingPlan.class, WorkoutSession.class, WorkoutItem.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDAO userDAO();
    public abstract TrainingPlanDAO trainingPlanDAO();
    public abstract WorkoutSessionDAO workoutSessionDAO();
    public abstract WorkoutItemDAO workoutItemDAO();

    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.beginTransaction();
            try {
                // Add isVideoMode
                database.execSQL("ALTER TABLE workoutItem ADD isVideoMode INTEGER NOT NULL default 1");

                database.setTransactionSuccessful();
            }
            finally {
                database.endTransaction();
            }
        }
    };
}