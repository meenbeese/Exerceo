package com.health.openworkout.gui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Toast

import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController

import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import com.health.openworkout.R
import com.health.openworkout.core.OpenWorkout
import com.health.openworkout.core.datatypes.TrainingPlan
import com.health.openworkout.core.datatypes.User

class HomeFragment : Fragment() {
    private var startView: ShapeableImageView? = null
    private var detailTrainingView: ShapeableImageView? = null
    private var trainingNameView: Spinner? = null
    private var sessionProgressBar: ProgressBar? = null
    private var sessionView: MaterialTextView? = null
    private var avatarGroup: RadioGroup? = null

    private var openWorkout: OpenWorkout? = null
    private var user: User? = null
    private var userTrainingPlan: TrainingPlan? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        openWorkout = OpenWorkout.getInstance()

        startView = root.findViewById(R.id.startView)

        startView?.setOnClickListener {
            userTrainingPlan = openWorkout?.getTrainingPlan(user!!.trainingsPlanId)
            if (userTrainingPlan != null) {
                val nextUserWorkoutSession = userTrainingPlan!!.nextWorkoutSession
                if (nextUserWorkoutSession != null) {
                    if (nextUserWorkoutSession.getWorkoutItems().isNotEmpty()) {
                        val action = HomeFragmentDirections.actionHomeFragmentToWorkoutFragmentSlide()
                        action.setTitle(nextUserWorkoutSession.name!!)
                        action.setSessionWorkoutId(nextUserWorkoutSession.workoutSessionId)

                        findNavController(requireActivity(), R.id.nav_host_fragment).navigate(action)
                    } else {
                        Toast.makeText(context, String.format(getString(R.string.error_no_workout_items), nextUserWorkoutSession.name), Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, String.format(getString(R.string.error_no_sessions), userTrainingPlan!!.name), Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, R.string.error_no_trainings, Toast.LENGTH_SHORT).show()
            }
        }

        detailTrainingView = root.findViewById(R.id.detailTrainingView)
        sessionProgressBar = root.findViewById(R.id.sessionProgressBar)
        sessionView = root.findViewById(R.id.sessionView)
        trainingNameView = root.findViewById(R.id.trainingNameView)

        user = openWorkout?.currentUser
        userTrainingPlan = openWorkout?.getTrainingPlan(user!!.trainingsPlanId)

        //  If user training plan was deleted
        if (userTrainingPlan == null) {
            // Abort if all training plans were deleted
            if (openWorkout!!.getTrainingPlans()!!.isEmpty()) {
                return root
            } else {
                // Get the first one in training plan list and update user training plan id if exist
                userTrainingPlan = openWorkout!!.getTrainingPlans()[0]
                user?.trainingsPlanId = userTrainingPlan!!.trainingPlanId
                openWorkout?.updateUser(user)
            }
        }

        val spinnerArrayAdapter = ArrayAdapter(
            requireActivity(),
            R.layout.support_simple_spinner_dropdown_item,
            openWorkout!!.getTrainingPlans()
        )

        spinnerArrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        trainingNameView?.setAdapter(spinnerArrayAdapter)
        trainingNameView?.setSelection(0, false)

        trainingNameView?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                val selectedTrainingPlan = spinnerArrayAdapter.getItem(position)

                user?.trainingsPlanId = selectedTrainingPlan!!.trainingPlanId
                openWorkout?.updateUser(user)
                updateProgressBar(selectedTrainingPlan!!)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        for (i in 0 until spinnerArrayAdapter.count) {
            if (userTrainingPlan?.trainingPlanId == spinnerArrayAdapter.getItem(i)?.trainingPlanId) {
                trainingNameView?.setSelection(i)
            }
        }

        avatarGroup = root.findViewById(R.id.avatarGroup)

        if (user!!.isMale) {
            avatarGroup?.check(R.id.radioMale)
        } else {
            avatarGroup?.check(R.id.radioFemale)
        }

        avatarGroup?.setOnCheckedChangeListener { group: RadioGroup, _: Int ->
            val checkedRadioId = group.checkedRadioButtonId
            when (checkedRadioId) {
                R.id.radioMale -> user?.isMale = true
                R.id.radioFemale -> user?.isMale = false
            }
            openWorkout?.updateUser(user)
        }

        detailTrainingView?.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToTrainingFragment()
            findNavController(requireActivity(), R.id.nav_host_fragment).navigate(action)
        }

        updateProgressBar(userTrainingPlan!!)

        return root
    }

    private fun updateProgressBar(trainingPlan: TrainingPlan) {
        sessionView?.text = "(${trainingPlan.finishedSessionSize()}/${trainingPlan.workoutSessionSize})"
        sessionProgressBar?.max = trainingPlan.workoutSessionSize
        sessionProgressBar?.post {
            sessionProgressBar?.progress = trainingPlan.finishedSessionSize()
        }
    }
}
