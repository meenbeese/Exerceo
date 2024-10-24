package com.health.openworkout.gui.training;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.health.openworkout.R;
import com.health.openworkout.core.OpenWorkout;
import com.health.openworkout.core.datatypes.TrainingPlan;
import com.health.openworkout.core.datatypes.WorkoutItem;
import com.health.openworkout.core.datatypes.WorkoutSession;
import com.health.openworkout.gui.datatypes.GenericAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class TrainingsAdapter extends GenericAdapter<TrainingsAdapter.ViewHolder> {
    private List<TrainingPlan> trainingPlanList;
    private Context context;

    private final String TAG = getClass().getSimpleName();

    public TrainingsAdapter(Context aContext, List<TrainingPlan> trainingPlanList) {
        super(aContext);
        this.context = aContext;
        this.trainingPlanList = trainingPlanList;
    }

    @Override
    public TrainingsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_training, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        TrainingPlan trainingPlan = trainingPlanList.get(position);
        holder.nameView.setText(trainingPlan.getName());

        try {
            if (trainingPlan.isImagePathExternal()) {
                Uri imgUri = Uri.parse(trainingPlan.getImagePath());
                holder.imgView.setImageURI(imgUri);
            } else {
                    InputStream ims = context.getAssets().open("image/" + trainingPlan.getImagePath());
                    holder.imgView.setImageDrawable(Drawable.createFromStream(ims, null));
                    ims.close();
            }
            } catch (IOException ex) {
                Log.e(TAG, ex.toString());
            } catch (SecurityException ex) {
                holder.imgView.setImageResource(R.drawable.ic_no_file);
                Toast.makeText(context, context.getString(R.string.error_no_access_to_file) + " " + trainingPlan.getImagePath(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, ex.toString());
            }

        if (!trainingPlan.getWorkoutSessions().isEmpty() &&
                trainingPlan.getWorkoutSessionSize() == trainingPlan.finishedSessionSize()) {

            trainingPlan.setCountFinishedTraining(trainingPlan.getCountFinishedTraining() + 1);

            for (WorkoutSession workoutSession : trainingPlan.getWorkoutSessions()) {
                workoutSession.setFinished(false);

                for (WorkoutItem workoutItem : workoutSession.getWorkoutItems()) {
                    workoutItem.setFinished(false);
                    OpenWorkout.getInstance().updateWorkoutItem(workoutItem);
                }

                OpenWorkout.getInstance().updateWorkoutSession(workoutSession);
            }
        }

        if (trainingPlan.getCountFinishedTraining() == 0) {
            holder.trophyView.setBackgroundResource(R.drawable.ic_trophy_disabled);
        } else {
            holder.trophyView.setBackgroundResource(R.drawable.ic_trophy_enabled);
            holder.trophyView.setText(Integer.toString(trainingPlan.getCountFinishedTraining()));
        }

        holder.detailedView.setText(String.format(context.getString(R.string.label_session_size_completed), trainingPlan.finishedSessionSize(), trainingPlan.getWorkoutSessionSize()));

        switch (getMode()) {
            case VIEW -> holder.trophyView.setVisibility(View.VISIBLE);
            case EDIT -> holder.trophyView.setVisibility(View.GONE);
        }
    }

    @Override
    public long getItemId(int position) {
        return trainingPlanList.get(position).getTrainingPlanId();
    }

    @Override
    public int getItemCount() {
        return trainingPlanList.size();
    }

    static class ViewHolder extends GenericAdapter.ViewHolder {
        ShapeableImageView imgView;
        MaterialTextView nameView;
        MaterialTextView detailedView;
        MaterialTextView trophyView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgView = itemView.findViewById(R.id.imgView);
            nameView = itemView.findViewById(R.id.nameView);
            detailedView = itemView.findViewById(R.id.detailedView);
            trophyView = itemView.findViewById(R.id.trophyView);

            setExportVisible(true);
            setPublishVisible(true);
        }
    }
}
