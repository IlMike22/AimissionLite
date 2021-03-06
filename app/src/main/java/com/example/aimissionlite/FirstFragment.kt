package com.example.aimissionlite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aimissionlite.models.Goal

class FirstFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button_first).setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerview_goals)
        val goalAdapter = GoalsAdapter()
        recyclerView.adapter = goalAdapter
        recyclerView.layoutManager = LinearLayoutManager(view.context)

        // get data from viewmodel
        val viewModel: GoalViewModel by viewModels {
            GoalViewModel.GoalViewModelFactory((this.activity?.application as AimissionApplication).repository)
        }

        viewModel.allGoals.observe(viewLifecycleOwner, Observer { goals ->
            goals?.let { goals ->
                println("!!! data changed! update adapter and so recyclerview.")
                goalAdapter.submitList(goals)
            }
        })

        // simple sample
        val newGoal = Goal(
            id = 22,
            title = "Hello",
            description = "World")

        viewModel.insert(newGoal)
        println("!!! new goal added to db!")
    }
}