package com.health.openworkout.gui.workout;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.textview.MaterialTextView;
import com.health.openworkout.R;
import com.health.openworkout.core.OpenWorkout;
import com.health.openworkout.core.datatypes.WorkoutItem;
import com.health.openworkout.gui.datatypes.GenericSettingsFragment;
import com.health.openworkout.gui.utils.FileDialogHelper;

import java.io.IOException;
import java.io.InputStream;

public class WorkoutSettingsFragment extends GenericSettingsFragment {
    private WorkoutItem workoutItem;

    private ShapeableImageView imgView;
    private MaterialTextView nameView;
    private MaterialTextView descriptionView;
    private MaterialTextView prepTimeView;
    private MaterialTextView workoutTimeView;
    private MaterialTextView breakTimeView;
    private MaterialTextView repetitionCountView;
    private MaterialSwitch timeModeView;
    private TableRow workoutTimeRow;
    private TableRow repetitionCountRow;
    private MaterialSwitch videoModeView;
    private TableRow videoCardRow;
    private MaterialCardView videoCardView;
    private VideoView videoView;

    private FileDialogHelper fileDialogHelper;
    private boolean isImageDialogRequest;

    private final String TAG = getClass().getSimpleName();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_workoutsettings, container, false);

        fileDialogHelper = new FileDialogHelper(this);

        imgView = root.findViewById(R.id.imgView);
        nameView = root.findViewById(R.id.nameView);
        descriptionView = root.findViewById(R.id.descriptionView);
        prepTimeView = root.findViewById(R.id.prepTimeView);
        workoutTimeView = root.findViewById(R.id.workoutTimeView);
        breakTimeView = root.findViewById(R.id.breakTimeView);
        repetitionCountView = root.findViewById(R.id.repetitionCountView);
        timeModeView = root.findViewById(R.id.timeModeView);
        workoutTimeRow = root.findViewById(R.id.workoutTimeRow);
        repetitionCountRow = root.findViewById(R.id.repetitionCountRow);
        videoModeView = root.findViewById(R.id.videoModeView);
        videoCardRow = root.findViewById(R.id.videoCardRow);
        videoCardView = root.findViewById(R.id.videoCardView);
        videoView = root.findViewById(R.id.videoView);

        videoView.setOnPreparedListener(mp -> mp.setLooping(true));

        timeModeView.setOnCheckedChangeListener((buttonView, isChecked) -> {
            workoutItem.setTimeMode(isChecked);
            refreshTimeModeState();
        });

        videoModeView.setOnCheckedChangeListener((buttonView, isChecked) -> {
            workoutItem.setVideoMode(isChecked);
            refreshVideoModeState();
        });

        imgView.setOnClickListener(v -> {
            isImageDialogRequest = true;
            fileDialogHelper.openImageFileDialog();
        });

        // support for SDK version <= 23 videoView onClickListener is not called
        videoCardView.setOnClickListener(v -> {
            isImageDialogRequest = false;
            fileDialogHelper.openVideoFileDialog();
        });

        videoView.setOnClickListener(v -> {
            isImageDialogRequest = false;
            fileDialogHelper.openVideoFileDialog();
        });

        setMode(WorkoutSettingsFragmentArgs.fromBundle(getArguments()).getMode());

        return root;
    }

    @Override
    protected String getTitle() {
        return workoutItem.getName();
    }

    @Override
    protected void loadFromDatabase(SETTING_MODE mode) {
        long workoutItemId = WorkoutSettingsFragmentArgs.fromBundle(getArguments()).getWorkoutItemId();

        switch (mode) {
            case ADD:
                if (workoutItemId != -1L) {
                    workoutItem = OpenWorkout.getInstance().getWorkoutItem(workoutItemId).clone();
                    workoutItem.setWorkoutItemId(0);
                } else {
                    workoutItem = new WorkoutItem();
                }
                break;
            case EDIT:
                workoutItem = OpenWorkout.getInstance().getWorkoutItem(workoutItemId);
                break;
        }

        try {
            if (workoutItem.isImagePathExternal()) {
                imgView.setImageURI(Uri.parse(workoutItem.getImagePath()));
            } else {
                String subFolder;
                if (OpenWorkout.getInstance().getCurrentUser().isMale) {
                    subFolder = "male";
                } else {
                    subFolder = "female";
                }

                InputStream ims = getContext().getAssets().open("image/" + subFolder + "/" + workoutItem.getImagePath());
                imgView.setImageDrawable(Drawable.createFromStream(ims, null));

                ims.close();
            }
        } catch (IOException ex) {
            Log.e(TAG, ex.toString());
        } catch (SecurityException ex) {
            imgView.setImageResource(R.drawable.ic_no_file);
            Toast.makeText(getContext(), getContext().getString(R.string.error_no_access_to_file) + " " + workoutItem.getImagePath(), Toast.LENGTH_SHORT).show();
            Log.e(TAG, ex.toString());
        }

        try {
            if (workoutItem.isVideoPathExternal()) {
                videoView.setVideoURI(Uri.parse(workoutItem.getVideoPath()));
            } else {
                if (OpenWorkout.getInstance().getCurrentUser().isMale) {
                    videoView.setVideoPath("content://com.health.openworkout.videoprovider/video/male/" + workoutItem.getVideoPath());
                } else {
                    videoView.setVideoPath("content://com.health.openworkout.videoprovider/video/female/" + workoutItem.getVideoPath());
                }
            }
        } catch (SecurityException ex) {
            videoView.setVideoURI(null);
            Toast.makeText(getContext(), getContext().getString(R.string.error_no_access_to_file) + " " + workoutItem.getVideoPath(), Toast.LENGTH_SHORT).show();
            Log.e(TAG, ex.toString());
        }

        videoView.start();

        nameView.setText(workoutItem.getName());
        descriptionView.setText(workoutItem.getDescription());
        prepTimeView.setText(Integer.toString(workoutItem.getPrepTime()));
        workoutTimeView.setText(Integer.toString(workoutItem.getWorkoutTime()));
        breakTimeView.setText(Integer.toString(workoutItem.getBreakTime()));
        repetitionCountView.setText(Integer.toString(workoutItem.getRepetitionCount()));
        videoModeView.setChecked(workoutItem.isVideoMode());
        timeModeView.setChecked(workoutItem.isTimeMode());

        refreshTimeModeState();
        refreshVideoModeState();
    }

    private void refreshTimeModeState() {
        if (workoutItem.isTimeMode()) {
            workoutTimeRow.setVisibility(View.VISIBLE);
            repetitionCountRow.setVisibility(View.GONE);
        } else {
            workoutTimeRow.setVisibility(View.GONE);
            repetitionCountRow.setVisibility(View.VISIBLE);
        }
    }

    private void refreshVideoModeState() {
        if (workoutItem.isVideoMode()) {
            videoCardRow.setVisibility(View.VISIBLE);
        } else {
            videoCardRow.setVisibility(View.GONE);
        }
    }

    @Override
    protected boolean saveToDatabase(SETTING_MODE mode) {
        boolean checkFormat = true;

        workoutItem.setName(nameView.getText().toString());
        workoutItem.setDescription(descriptionView.getText().toString());
        if (prepTimeView.getText().toString().isEmpty()) {
            prepTimeView.setError(getString(R.string.error_empty_text));
            checkFormat = false;
        } else {
            workoutItem.setPrepTime(Integer.valueOf(prepTimeView.getText().toString()));
        }
        if (workoutTimeView.getText().toString().isEmpty()) {
            workoutTimeView.setError(getString(R.string.error_empty_text));
            checkFormat = false;
        } else {
            workoutItem.setWorkoutTime(Integer.valueOf(workoutTimeView.getText().toString()));
        }
        if (breakTimeView.getText().toString().isEmpty()) {
            breakTimeView.setError(getString(R.string.error_empty_text));
            checkFormat = false;
        } else {
            workoutItem.setBreakTime(Integer.valueOf(breakTimeView.getText().toString()));
        }
        if (repetitionCountView.getText().toString().isEmpty()) {
            repetitionCountView.setError(getString(R.string.error_empty_text));
            checkFormat = false;
        } else {
            workoutItem.setRepetitionCount(Integer.valueOf(repetitionCountView.getText().toString()));
        }

        workoutItem.setTimeMode(timeModeView.isChecked());
        workoutItem.setVideoMode(videoModeView.isChecked());

        switch (mode) {
            case ADD:
                long workoutSessionId = WorkoutSettingsFragmentArgs.fromBundle(getArguments()).getSessionWorkoutId();

                workoutItem.setWorkoutSessionId(workoutSessionId);
                workoutItem.setOrderNr(OpenWorkout.getInstance().getWorkoutSession(workoutSessionId).getWorkoutItems().size()+1);
                OpenWorkout.getInstance().insertWorkoutItem(workoutItem);
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigateUp();
                break;
            case EDIT:
                OpenWorkout.getInstance().updateWorkoutItem(workoutItem);
                break;
        }

        return checkFormat;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        fileDialogHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (fileDialogHelper.onActivityResult(requestCode, resultCode, data)) {
            Uri uri = data.getData();

            if (isImageDialogRequest) {
                imgView.setImageURI(uri);
                workoutItem.setImagePath(uri.toString());
                workoutItem.setImagePathExternal(true);
            } else {
                videoView.setVideoURI(uri);
                videoView.start();
                workoutItem.setVideoPath(uri.toString());
                workoutItem.setVideoPathExternal(true);
            }
        }
    }
}
