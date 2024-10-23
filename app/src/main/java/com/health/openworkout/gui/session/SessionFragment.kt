package com.health.openworkout.gui.session

import android.app.AlertDialog
import android.content.res.Configuration
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast

import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.health.openworkout.R
import com.health.openworkout.core.OpenWorkout
import com.health.openworkout.core.datatypes.TrainingPlan
import com.health.openworkout.core.datatypes.WorkoutSession
import com.health.openworkout.gui.datatypes.GenericFragment
import com.health.openworkout.gui.datatypes.GenericSettingsFragment

class SessionFragment : GenericFragment() {

    private lateinit var sessionsView: RecyclerView
    private lateinit var trainingPlan: TrainingPlan
    private lateinit var workoutSessionList: MutableList<WorkoutSession>

    private lateinit var expandableButton: FloatingActionButton
    private lateinit var addButton: FloatingActionButton
    private lateinit var addLayout: LinearLayout
    private lateinit var animFabOpen: Animation
    private lateinit var animFabClose: Animation
    private lateinit var animFabClock: Animation
    private lateinit var animFabAntiClock: Animation
    private var isExpandable = false

    private lateinit var sessionsAdapter: SessionsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_session, container, false)

        sessionsView = root.findViewById(R.id.sessionsView)
        sessionsView.setHasFixedSize(true)
        sessionsView.layoutManager = GridLayoutManager(context, getNumberOfColumns())

        expandableButton = root.findViewById(R.id.expandableButton)
        addButton = root.findViewById(R.id.addButton)
        addLayout = root.findViewById(R.id.addLayout)

        animFabClose = AnimationUtils.loadAnimation(context, R.anim.fab_close)
        animFabOpen = AnimationUtils.loadAnimation(context, R.anim.fab_open)
        animFabClock = AnimationUtils.loadAnimation(context, R.anim.fab_rotate_clock)
        animFabAntiClock = AnimationUtils.loadAnimation(context, R.anim.fab_rotate_anticlock)

        expandableButton.setOnClickListener {
            if (isExpandable) {
                addLayout.visibility = View.GONE
                addLayout.startAnimation(animFabClose)
                expandableButton.startAnimation(animFabAntiClock)
                isExpandable = false
            } else {
                addLayout.visibility = View.VISIBLE
                addLayout.startAnimation(animFabOpen)
                expandableButton.startAnimation(animFabClock)
                isExpandable = true
            }
        }

        addButton.setOnClickListener {
            showCreateDaysDialog()
        }

        loadFromDatabase()

        return root
    }

    private fun showCreateDaysDialog() {
        val alert = AlertDialog.Builder(requireContext())
        alert.setTitle(getString(R.string.label_input_create_days))
        val input = EditText(context).apply {
            setText("3")
            inputType = InputType.TYPE_CLASS_NUMBER
            setRawInputType(Configuration.KEYBOARD_12KEY)
        }
        alert.setView(input)
        alert.setPositiveButton(getString(R.string.label_ok)) { _, _ ->
            val inputText = input.text.toString()
            if (inputText.isNotEmpty()) {
                val startNr = trainingPlan.workoutSessions.size + 1
                val offsetNr = inputText.toInt()

                for (nr in startNr until (startNr + offsetNr)) {
                    val workoutSession = WorkoutSession().apply {
                        name = getString(R.string.day_unit, nr)
                        trainingPlanId = trainingPlan.trainingPlanId
                        orderNr = nr.toLong()
                    }
                    trainingPlan.addWorkoutSession(workoutSession)
                    OpenWorkout.getInstance().insertWorkoutSession(workoutSession)
                    getAdapter().notifyItemInserted(nr)
                    sessionsView.scrollToPosition(startNr)
                }

                loadFromDatabase()
            }
        }
        alert.setNegativeButton(getString(R.string.label_cancel), null)
        alert.show()
    }

    override fun getTitle(): String = trainingPlan.name

    override fun getAdapter(): SessionsAdapter = sessionsAdapter

    override fun getRecyclerView(): RecyclerView = sessionsView

    override fun getItemList(): MutableList<WorkoutSession> = workoutSessionList

    override fun onSelectCallback(position: Int) {
        val workoutSession = workoutSessionList[position]
        val action = SessionFragmentDirections.actionSessionFragmentToWorkoutFragment().apply {
            title = workoutSession.name.toString()
            sessionWorkoutId = workoutSession.workoutSessionId
        }
        findNavController().navigate(action)
    }

    override fun onEditCallback(position: Int) {
        val workoutSession = workoutSessionList[position]
        val action = SessionFragmentDirections.actionSessionsFragmentToSessionSettingsFragment().apply {
            workoutSessionId = workoutSession.workoutSessionId
            mode = GenericSettingsFragment.SETTING_MODE.EDIT
            title = getString(R.string.label_edit)
        }
        findNavController().navigate(action)
    }

    override fun onDeleteCallback(position: Int) {
        OpenWorkout.getInstance().deleteWorkoutSession(workoutSessionList[position])
        Toast.makeText(
            context,
            getString(R.string.label_delete_toast, workoutSessionList[position].name),
            Toast.LENGTH_SHORT
        ).show()
        getItemList().removeAt(position)
    }

    override fun onDuplicateCallback(position: Int) {
        val origWorkoutSession = workoutSessionList[position]
        val duplicateWorkoutSession = origWorkoutSession.clone().apply {
            workoutSessionId = 0
        }
        workoutSessionList.add(position, duplicateWorkoutSession)
        saveToDatabase()

        val workoutSessionId = OpenWorkout.getInstance().insertWorkoutSession(duplicateWorkoutSession)
        duplicateWorkoutSession.workoutSessionId = workoutSessionId
    }

    override fun onResetClick() {
        workoutSessionList.forEach { workoutSession ->
            workoutSession.isFinished = false
            workoutSession.getWorkoutItems().forEach { workoutItem ->
                workoutItem.setFinished(false)
                OpenWorkout.getInstance().updateWorkoutItem(workoutItem)
            }
            OpenWorkout.getInstance().updateWorkoutSession(workoutSession)
        }
    }

    override fun loadFromDatabase() {
        val trainingPlanId = SessionFragmentArgs.fromBundle(requireArguments()).trainingPlanId
        trainingPlan = OpenWorkout.getInstance().getTrainingPlan(trainingPlanId)
        workoutSessionList = trainingPlan.workoutSessions
        sessionsAdapter = SessionsAdapter(requireContext(), workoutSessionList).apply {
            mode = mode
        }
        sessionsView.adapter = sessionsAdapter
    }

    override fun saveToDatabase() {
        workoutSessionList.forEachIndexed { i, workoutSession ->
            workoutSession.orderNr = i.toLong()
            OpenWorkout.getInstance().updateWorkoutSession(workoutSession)
        }
    }

    private fun getNumberOfColumns(): Int {
        val view = View.inflate(context, R.layout.item_session, null)
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        val width = view.measuredWidth
        val screenWidth = resources.displayMetrics.widthPixels
        val count = (screenWidth - sessionsView.paddingLeft - sessionsView.paddingRight) / width
        val remaining = (screenWidth - sessionsView.paddingLeft - sessionsView.paddingRight) - width * count
        return if (remaining > width - 15) count + 1 else count
    }
}
