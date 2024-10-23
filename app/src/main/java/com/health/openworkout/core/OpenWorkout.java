package com.health.openworkout.core;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.health.openworkout.core.database.AppDatabase;
import com.health.openworkout.core.datatypes.TrainingPlan;
import com.health.openworkout.core.datatypes.User;
import com.health.openworkout.core.datatypes.WorkoutItem;
import com.health.openworkout.core.datatypes.WorkoutSession;
import com.health.openworkout.core.training.AbdominalMuscleTraining;
import com.health.openworkout.core.training.BeginnersTraining;
import com.health.openworkout.core.training.SevenMinutesTraining;
import com.health.openworkout.core.workout.WorkoutFactory;
import com.health.openworkout.gui.utils.SoundUtils;

import java.util.ArrayList;
import java.util.List;

public class OpenWorkout {
    public static boolean DEBUG_MODE = false;
    private static final String DATABASE_NAME = "openWorkout.db";

    private static OpenWorkout instance;
    private final Context context;

    private AppDatabase appDB;
    private User user;

    private final String TAG = getClass().getSimpleName();

    private SoundUtils soundUtils;

    private OpenWorkout(Context aContext) {
        context = aContext;
        soundUtils = new SoundUtils(aContext);

        openDB();
    }

    public static void createInstance(Context aContext) {
        if (instance != null) {
            return;
        }

        instance = new OpenWorkout(aContext);
    }

    public static OpenWorkout getInstance() {
        if (instance == null) {
            throw new RuntimeException("No openWorkout instance created");
        }

        return instance;
    }

    public final Context getContext() {
        return context;
    }

    private void openDB() {
        appDB = Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME)
                .allowMainThreadQueries()
                .addCallback(new RoomDatabase.Callback() {
                    @Override
                    public void onOpen(SupportSQLiteDatabase db) {
                        super.onOpen(db);
                        db.setForeignKeyConstraintsEnabled(true);
                    }
                })
                .addMigrations(AppDatabase.MIGRATION_1_2)
                .build();
    }

    public void initTrainingPlans() {
        List<TrainingPlan> trainingPlanList = appDB.trainingPlanDAO().getAll();

        if (trainingPlanList.isEmpty()) {
            appDB.workoutItemDAO().clear();

            long trainingPlanId = insertTrainingPlan(new SevenMinutesTraining());
            insertTrainingPlan(new BeginnersTraining());
            insertTrainingPlan(new AbdominalMuscleTraining());

            WorkoutFactory workoutFactory = new WorkoutFactory();
            appDB.workoutItemDAO().insertAll(workoutFactory.getAllWorkoutItems());

            user = new User();
            user.trainingsPlanId = trainingPlanId;
            appDB.userDAO().insert(user);
        }

        user = appDB.userDAO().getAll().get(0);
    }

    public User getCurrentUser() {
        return user == null ? new User() : user;
    }

    public void printTrainingPlans() {
        Log.d(TAG, "################ TRAINING PLAN PRINTOUT #####################");
        List<TrainingPlan> trainingPlanList = appDB.trainingPlanDAO().getAll();

        for (TrainingPlan singleTrainingPlan : trainingPlanList) {
            Log.d(TAG, "- Training Plan " + singleTrainingPlan.getName() + " Id " + singleTrainingPlan.getTrainingPlanId());
            List<WorkoutSession> workoutSessionList = appDB.workoutSessionDAO().getAll(singleTrainingPlan.getTrainingPlanId());

            for (WorkoutSession singleWorkoutSession : workoutSessionList) {
                Log.d(TAG, "-- WorkoutSession " + singleWorkoutSession.name + " Id " + singleWorkoutSession.workoutSessionId);
                List<WorkoutItem> workoutItemList = appDB.workoutItemDAO().getAll(singleWorkoutSession.workoutSessionId);

                for (WorkoutItem singleWorkItem : workoutItemList) {
                    Log.d(TAG, "---- WorkoutItem " + singleWorkItem.getName() + " Id " + singleWorkItem.getWorkoutItemId());
                }
            }
        }
    }

    public List<TrainingPlan> getTrainingPlans() {
        List<TrainingPlan> trainingPlanList = new ArrayList<>();

        List<TrainingPlan> dbTrainingPlanList = appDB.trainingPlanDAO().getAll();
        for (TrainingPlan dbTrainingPlan : dbTrainingPlanList) {
            trainingPlanList.add(getTrainingPlan(dbTrainingPlan.getTrainingPlanId()));
        }

        return trainingPlanList;
    }

    public TrainingPlan getTrainingPlan(long trainingPlanId) {
        TrainingPlan singleTrainingPlan = appDB.trainingPlanDAO().getSome(trainingPlanId);

        if (singleTrainingPlan != null) {
            List<WorkoutSession> workoutSessionList = appDB.workoutSessionDAO().getAll(singleTrainingPlan.getTrainingPlanId());
            singleTrainingPlan.setWorkoutSessions(workoutSessionList);

            for (WorkoutSession singleWorkoutSession : workoutSessionList) {
                List<WorkoutItem> workoutItemList = appDB.workoutItemDAO().getAll(singleWorkoutSession.workoutSessionId);
                singleWorkoutSession.setWorkoutItems(workoutItemList);
            }
        }

        return singleTrainingPlan;
    }

    public WorkoutSession getWorkoutSession(long workoutSessionId) {
        WorkoutSession singleWorkoutSession = appDB.workoutSessionDAO().getSome(workoutSessionId);

        List<WorkoutItem> workoutItemList = appDB.workoutItemDAO().getAll(singleWorkoutSession.workoutSessionId);
        singleWorkoutSession.setWorkoutItems(workoutItemList);

        return singleWorkoutSession;
    }

    public WorkoutItem getWorkoutItem(long workoutItemId) {
        return appDB.workoutItemDAO().getSome(workoutItemId);
    }

    public List<WorkoutItem> getAllUniqueWorkoutItems() {
        return appDB.workoutItemDAO().getAllUnique();
    }

    public long insertTrainingPlan(TrainingPlan trainingPlan) {
        long trainingPlanId = appDB.trainingPlanDAO().insert(trainingPlan);
        for (WorkoutSession workoutSession : trainingPlan.getWorkoutSessions()) {
            workoutSession.trainingPlanId = trainingPlanId;
            long workoutSessionId = appDB.workoutSessionDAO().insert(workoutSession);

            for (WorkoutItem workoutItem : workoutSession.getWorkoutItems()) {
                workoutItem.setWorkoutSessionId(workoutSessionId);
                appDB.workoutItemDAO().insert(workoutItem);
            }
        }

        return trainingPlanId;
    }

    public long insertWorkoutSession(WorkoutSession workoutSession) {
        long workoutSessionId = appDB.workoutSessionDAO().insert(workoutSession);

        for (WorkoutItem workoutItem : workoutSession.getWorkoutItems()) {
            workoutItem.setWorkoutSessionId(workoutSessionId);
            appDB.workoutItemDAO().insert(workoutItem);
        }

        return workoutSessionId;
    }

    public long insertWorkoutItem(WorkoutItem workoutItem) {
        long workoutItemId = appDB.workoutItemDAO().insert(workoutItem);

        return workoutItemId;
    }

    public void deleteTrainingPlan(TrainingPlan trainingPlan) {
        for (WorkoutSession workoutSession : trainingPlan.getWorkoutSessions()) {
            appDB.workoutItemDAO().deleteAll(workoutSession.workoutSessionId);
        }

        appDB.workoutSessionDAO().deleteAll(trainingPlan.getTrainingPlanId());
        appDB.trainingPlanDAO().delete(trainingPlan);
    }

    public void deleteWorkoutSession(WorkoutSession workoutSession) {
        appDB.workoutItemDAO().deleteAll(workoutSession.workoutSessionId);

        appDB.workoutSessionDAO().delete(workoutSession);
    }

    public void deleteWorkoutItem(WorkoutItem workoutItem) {
        appDB.workoutItemDAO().delete(workoutItem);
    }

    public void updateWorkoutItem(WorkoutItem workoutItem) {
        appDB.workoutItemDAO().update(workoutItem);
    }

    public void updateWorkoutSession(WorkoutSession workoutSession) {
        appDB.workoutSessionDAO().update(workoutSession);
    }

    public void updateTrainingPlan(TrainingPlan trainingPlan) {
        appDB.trainingPlanDAO().update(trainingPlan);
    }

    public void updateUser(User user) {
        appDB.userDAO().update(user);
    }

    public SoundUtils getSoundUtils() {
        return soundUtils;
    }
}
