package com.example.aimissionlite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class LandingPageFragment : ILandingPageFragment, Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_landing_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments != null) {
            val goalTitle =
                arguments?.getString(resources.getString(R.string.bundle_argument_goal_title))
            Toast.makeText(activity, "Goal $goalTitle successfully created!", Toast.LENGTH_SHORT)
                .show()
        }

        val viewModel: LandingPageViewModel by viewModels {
            LandingPageViewModel.MainViewModelFactory(
                repository = (this.activity?.application as AimissionApplication).repository,
                view = this,
                resources = resources
            )
        }

        val fabAddGoal = view.findViewById<ExtendedFloatingActionButton>(R.id.fab_add_goal)
        fabAddGoal.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerview_goals)

        val goalAdapter = GoalsAdapter(
            viewModel = viewModel,
            resources = resources
        )

        recyclerView.adapter = goalAdapter
        recyclerView.layoutManager = LinearLayoutManager(view.context)


        viewModel.allGoals.observe(viewLifecycleOwner, Observer { goals ->
            goals?.let { goals ->
                goalAdapter.submitList(goals)
            }
        })
    }
}