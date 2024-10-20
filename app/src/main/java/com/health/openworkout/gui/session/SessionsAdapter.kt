package com.health.openworkout.gui.session

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import com.health.openworkout.R
import com.health.openworkout.core.datatypes.WorkoutSession
import com.health.openworkout.gui.datatypes.GenericAdapter

class SessionsAdapter(
    context: Context,
    private val workoutSessionList: List<WorkoutSession>
) : GenericAdapter<SessionsAdapter.ViewHolder?>(context) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_session, parent, false)
        val viewHolder = ViewHolder(view)

        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val workoutSession = workoutSessionList[position]
        holder.nameView.text = workoutSession.name

        if (workoutSession.isFinished) {
            holder.imgView.setImageResource(R.drawable.ic_session_done)
        } else {
            holder.imgView.setImageResource(R.drawable.ic_session_undone)
        }
    }

    override fun getItemId(position: Int): Long {
        return workoutSessionList[position].workoutSessionId
    }

    override fun getItemCount(): Int {
        return workoutSessionList.size
    }

    class ViewHolder(itemView: View) : GenericAdapter.ViewHolder(itemView) {
        var imgView: ShapeableImageView = itemView.findViewById(R.id.imgView)
        var nameView: MaterialTextView = itemView.findViewById(R.id.nameView)
    }
}
