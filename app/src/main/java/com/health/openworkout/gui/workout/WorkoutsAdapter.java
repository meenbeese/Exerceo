package com.health.openworkout.gui.workout;

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
import com.health.openworkout.core.datatypes.WorkoutItem;
import com.health.openworkout.gui.datatypes.GenericAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class WorkoutsAdapter extends GenericAdapter<WorkoutsAdapter.ViewHolder> {
    private final List<WorkoutItem> workoutItemList;
    private Context context;

    private final String TAG = getClass().getSimpleName();

    public WorkoutsAdapter(Context aContext, List<WorkoutItem> workoutItemList) {
        super(aContext);
        this.context = aContext;
        this.workoutItemList = workoutItemList;
    }

    @Override
    public WorkoutsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        WorkoutItem workoutItem = workoutItemList.get(position);
        holder.nameView.setText(workoutItem.getName());

        if (workoutItem.isFinished()) {
            holder.doneView.setVisibility(View.VISIBLE);
        } else {
            holder.doneView.setVisibility(View.INVISIBLE);
        }

        if (workoutItem.isTimeMode()) {
            holder.detailedView.setText(String.format(context.getString(R.string.label_work_duration_item_info), workoutItem.getWorkoutTime()));
        } else {
            holder.detailedView.setText(String.format(context.getString(R.string.label_repetition_item_info), workoutItem.getRepetitionCount()));
        }

        holder.prepView.setText(String.format(context.getString(R.string.label_prep_duration_item_info), workoutItem.getPrepTime()));
        holder.breakView.setText(String.format(context.getString(R.string.label_break_duration_item_info), workoutItem.getBreakTime()));

        try {
            if (workoutItem.isImagePathExternal()) {
                holder.imgView.setImageURI(Uri.parse(workoutItem.getImagePath()));
            } else {
                String subFolder;
                if (OpenWorkout.getInstance().getCurrentUser().isMale()) {
                    subFolder = "male";
                } else {
                    subFolder = "female";
                }

                InputStream ims = context.getAssets().open("image/" + subFolder + "/" + workoutItem.getImagePath());
                holder.imgView.setImageDrawable(Drawable.createFromStream(ims, null));

                ims.close();
            }
        } catch (IOException ex) {
            holder.imgView.setImageResource(R.drawable.ic_no_file);
            Log.e(TAG, ex.toString());
        } catch (SecurityException ex) {
            holder.imgView.setImageResource(R.drawable.ic_no_file);
            Toast.makeText(context, context.getString(R.string.error_no_access_to_file) + " " + workoutItem.getImagePath(), Toast.LENGTH_SHORT).show();
            Log.e(TAG, ex.toString());
        }

        switch (getMode()) {
            case VIEW:
                break;
            case EDIT:
                holder.doneView.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public long getItemId(int position) {
        return workoutItemList.get(position).getWorkoutItemId();
    }

    @Override
    public int getItemCount() {
        return workoutItemList.size();
    }

    static class ViewHolder extends GenericAdapter.ViewHolder {
        MaterialTextView prepView;
        ShapeableImageView imgView;
        MaterialTextView nameView;
        MaterialTextView detailedView;
        ShapeableImageView doneView;
        MaterialTextView breakView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            prepView = itemView.findViewById(R.id.prepView);
            imgView = itemView.findViewById(R.id.imgView);
            nameView = itemView.findViewById(R.id.nameView);
            detailedView = itemView.findViewById(R.id.detailedView);
            doneView = itemView.findViewById(R.id.doneView);
            breakView = itemView.findViewById(R.id.breakView);
        }
    }
}
