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
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*

class LandingPageFragment : ILandingPageFragment, Fragment() {
    val viewModel: LandingPageViewModel by viewModels {
        LandingPageViewModel.LandingPageViewModelFactory(
            repository = (this.activity?.application as AimissionApplication).goalRepository,
            landingpageRepository = (this.activity?.application as AimissionApplication).landingPageRepository,
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

        // if option "delete goals on startip" is activated, this process will start here and now..
        if (viewModel.deleteAllGoalsIfEnabled()) {
            Toast.makeText(context, "Goals were successfully deleted!", Toast.LENGTH_SHORT).show()
        }

        viewModel.allGoals.observe(viewLifecycleOwner, Observer { goals ->
            goals?.let { currentGoals ->
                goalAdapter.submitList(currentGoals)
            }
        })
    }

//    override fun onResume() {
//        val toolbar = (activity as MainActivity?)?.supportActionBar
//        toolbar?.setIcon(null)
//        super.onResume()
//    }
}