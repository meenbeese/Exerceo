package com.health.openworkout.gui.datatypes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController

import com.google.android.material.textview.MaterialTextView
import com.health.openworkout.R
import com.health.openworkout.core.OpenWorkout
import com.health.openworkout.core.datatypes.WorkoutSession
import com.health.openworkout.gui.utils.SoundUtils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.TimeZone

class TrophyFragment : Fragment() {
    private var elapsedTimeView: MaterialTextView? = null
    private var okView: Button? = null
    private var soundUtils: SoundUtils? = null

    private var workoutSession: WorkoutSession? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_trophy, container, false)
        elapsedTimeView = root.findViewById(R.id.elapsedTimeView)
        okView = root.findViewById(R.id.okView)

        val workoutSessionId = TrophyFragmentArgs.fromBundle(requireArguments()).sessionWorkoutId
        workoutSession = OpenWorkout.getInstance().getWorkoutSession(workoutSessionId)

        val elapsedCalendar = Calendar.getInstance()
        elapsedCalendar.timeInMillis = workoutSession?.elapsedSessionTime!!.times(1000)

        val dateFormatter = SimpleDateFormat.getTimeInstance()
        dateFormatter.timeZone = TimeZone.getTimeZone("UTC")

        elapsedTimeView?.text = dateFormatter.format(elapsedCalendar.time)

        soundUtils = OpenWorkout.getInstance().soundUtils
        soundUtils?.playSound(SoundUtils.SOUND.SESSION_COMPLETED)

        okView?.setOnClickListener {
            findNavController(requireActivity(), R.id.nav_host_fragment).navigateUp()
            findNavController(requireActivity(), R.id.nav_host_fragment).navigateUp()
            findNavController(requireActivity(), R.id.nav_host_fragment).navigateUp()
        }

        return root
    }
}
