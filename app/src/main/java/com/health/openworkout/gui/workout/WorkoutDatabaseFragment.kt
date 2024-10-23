package com.health.openworkout.gui.workout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.health.openworkout.R
import com.health.openworkout.core.OpenWorkout
import com.health.openworkout.core.datatypes.WorkoutItem
import com.health.openworkout.gui.datatypes.GenericSettingsFragment

class WorkoutDatabaseFragment : Fragment() {
    private var workoutsView: RecyclerView? = null
    private var workoutItemList: List<WorkoutItem>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_workoutdatabase, container, false)

        workoutsView = root.findViewById(R.id.workoutsView)
        workoutsView?.setHasFixedSize(true)
        workoutsView?.setLayoutManager(LinearLayoutManager(context))

        loadFromDatabase()

        return root
    }

    fun loadFromDatabase() {
        workoutItemList = OpenWorkout.getInstance().allUniqueWorkoutItems

        val workoutsDatabaseAdapter = WorkoutsDatabaseAdapter(context, workoutItemList)

        workoutsDatabaseAdapter.setOnItemClickListener { position: Int, _: View? ->
            val workoutSessionId = WorkoutFragmentArgs.fromBundle(requireArguments()).sessionWorkoutId
            val action = WorkoutDatabaseFragmentDirections.actionWorkoutDatabaseFragmentToWorkoutSettingsFragment()

            action.setMode(GenericSettingsFragment.SETTING_MODE.ADD)
            action.setTitle(getString(R.string.label_add))
            action.setSessionWorkoutId(workoutSessionId)
            action.setWorkoutItemId(workoutItemList!![position].workoutItemId)

            findNavController(requireActivity(), R.id.nav_host_fragment).navigate(action)
        }

        workoutsView?.adapter = workoutsDatabaseAdapter
    }
}
