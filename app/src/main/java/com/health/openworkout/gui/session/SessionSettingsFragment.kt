package com.health.openworkout.gui.session

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import com.google.android.material.textview.MaterialTextView
import com.health.openworkout.R
import com.health.openworkout.core.OpenWorkout
import com.health.openworkout.core.datatypes.WorkoutSession
import com.health.openworkout.gui.datatypes.GenericSettingsFragment

class SessionSettingsFragment : GenericSettingsFragment() {
    private var workoutSession: WorkoutSession? = null

    private var imgView: ImageView? = null
    private var nameView: MaterialTextView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_sessionsettings, container, false)

        imgView = root.findViewById(R.id.imgView)
        nameView = root.findViewById(R.id.nameView)

        mode = SessionSettingsFragmentArgs.fromBundle(requireArguments()).mode

        return root
    }

    override fun getTitle(): String? {
        return workoutSession?.name
    }

    override fun loadFromDatabase(mode: SETTING_MODE) {
        when (mode) {
            SETTING_MODE.ADD -> workoutSession = WorkoutSession()
            SETTING_MODE.EDIT -> {
                val workoutSessionId = SessionSettingsFragmentArgs.fromBundle(requireArguments()).workoutSessionId
                workoutSession = OpenWorkout.getInstance().getWorkoutSession(workoutSessionId)
            }
        }

        if (workoutSession!!.isFinished) {
            imgView?.setImageResource(R.drawable.ic_session_done)
        } else {
            imgView?.setImageResource(R.drawable.ic_session_undone)
        }

        nameView?.text = workoutSession?.name
    }

    override fun saveToDatabase(mode: SETTING_MODE): Boolean {
        workoutSession?.name = nameView?.text.toString()

        when (mode) {
            SETTING_MODE.ADD -> {
                val trainingPlanId = SessionSettingsFragmentArgs.fromBundle(requireArguments()).trainingPlanId

                workoutSession?.trainingPlanId = trainingPlanId
                OpenWorkout.getInstance().insertWorkoutSession(workoutSession)
            }

            SETTING_MODE.EDIT -> OpenWorkout.getInstance().updateWorkoutSession(workoutSession)
        }

        return true
    }
}
