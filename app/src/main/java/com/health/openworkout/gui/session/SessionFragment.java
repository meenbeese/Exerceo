package com.health.openworkout.gui.session;

import android.app.AlertDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textview.MaterialTextView;
import com.health.openworkout.R;
import com.health.openworkout.core.OpenWorkout;
import com.health.openworkout.core.datatypes.TrainingPlan;
import com.health.openworkout.core.datatypes.WorkoutItem;
import com.health.openworkout.core.datatypes.WorkoutSession;
import com.health.openworkout.gui.datatypes.GenericAdapter;
import com.health.openworkout.gui.datatypes.GenericFragment;
import com.health.openworkout.gui.datatypes.GenericSettingsFragment;

import java.util.List;

public class SessionFragment extends GenericFragment {
    private RecyclerView sessionsView;

    private TrainingPlan trainingPlan;
    private List<WorkoutSession> workoutSessionList;

    private FloatingActionButton expandableButton;
    private FloatingActionButton addButton;
    private LinearLayout addLayout;
    private Animation animFabOpen, animFabClose, animFabClock, animFabAntiClock;
    private boolean isExpandable;

    private SessionsAdapter sessionsAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_session, container, false);

        sessionsView = root.findViewById(R.id.sessionsView);

        sessionsView.setHasFixedSize(true);
        sessionsView.setLayoutManager(new GridLayoutManager(getContext(), getNumberOfColumns()));

        isExpandable = false;

        expandableButton = root.findViewById(R.id.expandableButton);
        addButton = root.findViewById(R.id.addButton);
        addLayout = root.findViewById(R.id.addLayout);

        animFabClose = AnimationUtils.loadAnimation(getContext(), R.anim.fab_close);
        animFabOpen = AnimationUtils.loadAnimation(getContext(), R.anim.fab_open);
        animFabClock = AnimationUtils.loadAnimation(getContext(), R.anim.fab_rotate_clock);
        animFabAntiClock = AnimationUtils.loadAnimation(getContext(), R.anim.fab_rotate_anticlock);

        expandableButton.setOnClickListener(v -> {
            if (isExpandable) {
                addLayout.setVisibility(View.GONE);
                addLayout.startAnimation(animFabClose);
                expandableButton.startAnimation(animFabAntiClock);
                isExpandable = false;
            } else {
                addLayout.setVisibility(View.VISIBLE);
                addLayout.startAnimation(animFabOpen);
                expandableButton.startAnimation(animFabClock);
                isExpandable = true;
            }
        });

        addButton.setOnClickListener(v -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            alert.setTitle(getString(R.string.label_input_create_days));
            final EditText input = new EditText(getContext());
            input.setText("3", MaterialTextView.BufferType.EDITABLE);
            input.setInputType(InputType.TYPE_CLASS_NUMBER);
            input.setRawInputType(Configuration.KEYBOARD_12KEY);
            alert.setView(input);
            alert.setPositiveButton(getString(R.string.label_ok), (dialog, whichButton) -> {
                if (!input.getText().toString().isEmpty()) {
                    int startNr = trainingPlan.getWorkoutSessions().size() + 1;
                    int offsetNr = Integer.valueOf(input.getText().toString());

                    for (int nr=startNr; nr < (startNr + offsetNr); nr++) {
                        WorkoutSession workoutSession = new WorkoutSession();
                        workoutSession.name = String.format(getString(R.string.day_unit), nr);
                        workoutSession.trainingPlanId = trainingPlan.getTrainingPlanId();
                        workoutSession.orderNr = nr;
                        trainingPlan.addWorkoutSession(workoutSession);
                        OpenWorkout.getInstance().insertWorkoutSession(workoutSession);
                        getAdapter().notifyItemInserted(nr);
                        sessionsView.scrollToPosition(startNr);
                    }

                    loadFromDatabase();
                }
            });
            alert.setNegativeButton(getString(R.string.label_cancel), (dialog, whichButton) -> {
                // empty
            });
            alert.show();
        });

        loadFromDatabase();

        return root;
    }

    @Override
    protected String getTitle() {
        return trainingPlan.getName();
    }

    @Override
    protected GenericAdapter getAdapter() {
        return sessionsAdapter;
    }

    @Override
    protected RecyclerView getRecyclerView() {
        return sessionsView;
    }

    @Override
    protected List getItemList() {
        return workoutSessionList;
    }

    @Override
    protected void onSelectCallback(int position) {
        WorkoutSession workoutSession = workoutSessionList.get(position);

        SessionFragmentDirections.ActionSessionFragmentToWorkoutFragment action = SessionFragmentDirections.actionSessionFragmentToWorkoutFragment();
        action.setTitle(workoutSession.name);
        action.setSessionWorkoutId(workoutSession.workoutSessionId);
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(action);
    }

    @Override
    protected void onEditCallback(int position) {
        WorkoutSession workoutSession = workoutSessionList.get(position);

        SessionFragmentDirections.ActionSessionsFragmentToSessionSettingsFragment action = SessionFragmentDirections.actionSessionsFragmentToSessionSettingsFragment();
        action.setWorkoutSessionId(workoutSession.workoutSessionId);
        action.setMode(GenericSettingsFragment.SETTING_MODE.EDIT);
        action.setTitle(getString(R.string.label_edit));
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(action);
    }

    @Override
    protected void onDeleteCallback(int position) {
        OpenWorkout.getInstance().deleteWorkoutSession(workoutSessionList.get(position));
        Toast.makeText(getContext(), String.format(getString(R.string.label_delete_toast), workoutSessionList.get(position).name), Toast.LENGTH_SHORT).show();
        getItemList().remove(position);
    }

    @Override
    protected void onDuplicateCallback(int position) {
        WorkoutSession origWorkoutSession = workoutSessionList.get(position);
        WorkoutSession duplicateWorkoutSession = origWorkoutSession.clone();

        duplicateWorkoutSession.workoutSessionId = 0;
        workoutSessionList.add(position, duplicateWorkoutSession);
        saveToDatabase();

        long workoutSessionId = OpenWorkout.getInstance().insertWorkoutSession(duplicateWorkoutSession);
        duplicateWorkoutSession.workoutSessionId = workoutSessionId;
    }

    @Override
    protected void onResetClick() {
        for (WorkoutSession workoutSession : workoutSessionList) {
            workoutSession.setFinished(false);

            for (WorkoutItem workoutItem : workoutSession.getWorkoutItems()) {
                workoutItem.setFinished(false);
                OpenWorkout.getInstance().updateWorkoutItem(workoutItem);
            }

            OpenWorkout.getInstance().updateWorkoutSession(workoutSession);
        }
    }

    @Override
    protected void loadFromDatabase() {
        long trainingPlanId = SessionFragmentArgs.fromBundle(getArguments()).getTrainingPlanId();
        trainingPlan = OpenWorkout.getInstance().getTrainingPlan(trainingPlanId);

        workoutSessionList = trainingPlan.getWorkoutSessions();

        sessionsAdapter = new SessionsAdapter(getContext(), workoutSessionList);
        sessionsAdapter.setMode(getMode());
        sessionsView.setAdapter(sessionsAdapter);
    }

    @Override
    protected void saveToDatabase() {
        for (int i=0; i<workoutSessionList.size(); i++) {
            workoutSessionList.get(i).orderNr = i;
            OpenWorkout.getInstance().updateWorkoutSession(workoutSessionList.get(i));
        }
    }

    private int getNumberOfColumns() {
        View view = View.inflate(getContext(), R.layout.item_session, null);
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int width = view.getMeasuredWidth();
        int count = (getResources().getDisplayMetrics().widthPixels - sessionsView.getPaddingLeft() - sessionsView.getPaddingRight()) / width;
        int remaining = (getResources().getDisplayMetrics().widthPixels - sessionsView.getPaddingLeft() - sessionsView.getPaddingRight()) - width * count;
        if (remaining > width - 15)
            count++;
        return count;
    }
}
