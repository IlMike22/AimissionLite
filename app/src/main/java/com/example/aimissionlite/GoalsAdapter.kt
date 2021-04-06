package com.example.aimissionlite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.aimissionlite.models.domain.Goal

class GoalsAdapter : ListAdapter<Goal, GoalsAdapter.GoalViewHolder>(GoalComparator()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        return GoalViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {
        val currentGoal = getItem(position)
        holder.bind(currentGoal)
    }

    class GoalViewHolder(goalView: View) : RecyclerView.ViewHolder(goalView) {
        private val goalTitle: TextView = goalView.findViewById(R.id.goal_title)
        private val goalDescription: TextView = goalView.findViewById(R.id.goal_description)

        fun bind(goal: Goal?) {
            goalTitle.text = goal?.title
            goalDescription.text = goal?.description
        }

        companion object {
            fun create(parent: ViewGroup): GoalViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.goal_item, parent, false)
                return GoalViewHolder(view)
            }
        }
    }

    class GoalComparator : DiffUtil.ItemCallback<Goal>() {
        override fun areItemsTheSame(oldItem: Goal, newItem: Goal): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Goal, newItem: Goal): Boolean {
            return oldItem.title == newItem.title &&
                    oldItem.description == newItem.description // todo add the missing values of goal here later
        }
    }
}