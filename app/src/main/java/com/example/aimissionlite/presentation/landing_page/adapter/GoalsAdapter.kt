package com.example.aimissionlite.presentation.landing_page.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.aimissionlite.R
import com.example.aimissionlite.models.domain.Goal
import com.example.aimissionlite.models.domain.Status
import com.example.aimissionlite.presentation.landing_page.LandingPageViewModel

class GoalsAdapter(
    private val viewModel: LandingPageViewModel,
    private val resources: Resources
) :
    ListAdapter<Goal, GoalsAdapter.GoalViewHolder>(GoalComparator()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        return GoalViewHolder.create(
            parent = parent,
            viewModel = viewModel
        )
    }

    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {
        val currentGoal = getItem(position)
        holder.bind(currentGoal, resources)
    }

    class GoalViewHolder(goalView: View, private val viewModel: LandingPageViewModel) :
        RecyclerView.ViewHolder(goalView) {
        private val goalTitle: TextView = goalView.findViewById(R.id.goal_title)
        private val goalDescription: TextView = goalView.findViewById(R.id.goal_description)
        private val goalButtonStatusChange: ImageButton =
            goalView.findViewById(R.id.goal_button_status_change)
        private val goalButtonDeleteGoal:ImageButton = goalView.findViewById(R.id.goal_button_delete_goal)
        private val goalContainer: LinearLayout = goalView.findViewById(R.id.goal_container)

        fun bind(goal: Goal?, resources:Resources) {
            goalTitle.text = goal?.title
            goalDescription.text = goal?.description
            when (goal?.status) {
                Status.TODO -> goalButtonStatusChange.setImageDrawable(resources.getDrawable(R.drawable.ic_launcher_background_red))
                Status.IN_PROGRESS -> goalButtonStatusChange.setImageDrawable(resources.getDrawable(
                    R.drawable.ic_launcher_background_yellow
                ))
                Status.DONE -> goalButtonStatusChange.setImageDrawable(resources.getDrawable(R.drawable.ic_launcher_background_green))
                else -> println("!! Unknown status. Dont know which color the button should has.")
            }

            goalButtonStatusChange.setOnClickListener { _ ->
                viewModel.onGoalStatusClicked(goal)
            }

            goalButtonDeleteGoal.setOnClickListener { _ ->
                viewModel.onGoalDeleteClicked(goal)
            }

            goalContainer.setOnClickListener { _ ->
                viewModel.onGoalContainerClicked(goal)
            }
        }

        companion object {
            fun create(parent: ViewGroup, viewModel: LandingPageViewModel): GoalViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.goal_item, parent, false)
                return GoalViewHolder(
                    goalView = view,
                    viewModel = viewModel
                )
            }
        }
    }

    class GoalComparator : DiffUtil.ItemCallback<Goal>() {
        override fun areItemsTheSame(oldGoal: Goal, newGoal: Goal): Boolean {
            return oldGoal == newGoal
        }

        override fun areContentsTheSame(oldGoal: Goal, newGoal: Goal): Boolean {
            return oldGoal.title == newGoal.title &&
                    oldGoal.description == newGoal.description
        }
    }
}