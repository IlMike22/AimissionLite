package com.example.aimissionlite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch

class LandingPageFragment : ILandingPageFragment, Fragment() {
    val viewModel: LandingPageViewModel by viewModels {
        LandingPageViewModel.LandingPageViewModelFactory(
            goalRepository = (this.activity?.application as AimissionApplication).goalRepository,
            settingsRepository = (this.activity?.application as AimissionApplication).settingsRepository,
            view = this,
            resources = resources
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.toolbar?.setupWithNavController(
            navController = viewModel.navController,
            configuration = AppBarConfiguration(viewModel.navController.graph)
        )
        return inflater.inflate(R.layout.fragment_landing_page, container, false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_info -> {
                viewModel.onInfoClicked()
                true
            }
            R.id.action_settings -> {
                viewModel.onSettingsClicked()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        println("!!! set observer (again?)")
        viewModel.isDeleteAllGoals?.observe(viewLifecycleOwner, { isDeleteAllGoals ->
            isDeleteAllGoals?.let { deleteGoals ->
                if (deleteGoals) {
                    viewModel.viewModelScope.launch {
                        if (viewModel.deleteAllGoals()) {
                            Toast.makeText(
                                context,
                                "Goals were successfully deleted!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                context,
                                "Something went wrong while trying to delete all goals!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        })


        if (arguments != null) {
            val goalTitle =
                arguments?.getString(resources.getString(R.string.bundle_argument_goal_title))
            Toast.makeText(activity, "Goal $goalTitle successfully created!", Toast.LENGTH_SHORT)
                .show()
        }

        val fabAddGoal = view.findViewById<ExtendedFloatingActionButton>(R.id.fab_add_goal)
        fabAddGoal.setOnClickListener {
            viewModel.onAddGoalClicked()
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerview_goals)

        val goalAdapter = GoalsAdapter(
            viewModel = viewModel,
            resources = resources
        )

        recyclerView.adapter = goalAdapter
        recyclerView.layoutManager = LinearLayoutManager(view.context)

        // if option "delete goals on startup" is activated, this process will start here and now..
        viewModel.allGoals.observe(viewLifecycleOwner, Observer { goals ->
            goals?.let { currentGoals ->
                goalAdapter.submitList(currentGoals)
            }
        })
    }
}