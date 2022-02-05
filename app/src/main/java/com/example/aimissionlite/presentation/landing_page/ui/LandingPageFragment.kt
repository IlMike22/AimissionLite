package com.example.aimissionlite.presentation.landing_page.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aimissionlite.R
import com.example.aimissionlite.data.BUNDLE_ID_GOAL
import com.example.aimissionlite.presentation.landing_page.LandingPageUiEvent
import com.example.aimissionlite.presentation.landing_page.LandingPageViewModel
import com.example.aimissionlite.presentation.landing_page.adapter.GoalsAdapter
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_landing_page.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LandingPageFragment : Fragment() {
    val viewModel: LandingPageViewModel by viewModels()
    var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val navController: NavController = findNavController()

        activity?.toolbar?.setupWithNavController(
            navController = navController,
            configuration = AppBarConfiguration(navController.graph)
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

        viewModel.isDeleteAllGoals?.observe(viewLifecycleOwner) { isDeleteAllGoals ->
            isDeleteAllGoals?.let { completeGoals ->
                if (completeGoals) {
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
        }

        job = CoroutineScope(Dispatchers.Main).launch {
            viewModel.uiEvent.collectLatest { uiEvent ->
                when (uiEvent) {
                    is LandingPageUiEvent.ShowSnackbar -> {
                        showDeleteGoalSucceededSnackbar(
                            uiEvent.message ?: "Unknown error"
                        )
                    }
                    is LandingPageUiEvent.NavigateToSettings -> {
                        findNavController().navigate(R.id.action_LandingPageFragment_to_SettingsFragment)
                        job?.cancel()

                    }
                    is LandingPageUiEvent.NavigateToInfo -> {
                        findNavController().navigate(R.id.action_LandingPageFragment_to_InfoFragment)
                        job?.cancel()
                    }
                    is LandingPageUiEvent.NavigateToDetail -> {
                        val bundle = bundleOf(BUNDLE_ID_GOAL to uiEvent.goal?.id)
                        findNavController().navigate(R.id.action_LandingPageFragment_to_DetailFragment, bundle)
                        job?.cancel()
                    }
                }
            }

            job?.apply {
                if (isActive) {
                    cancel()
                }
            }
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

        CoroutineScope(Dispatchers.Main).launch {
            viewModel.allGoals.collectLatest { goals ->
                goals.let { currentGoals ->
                    goalAdapter.submitList(currentGoals)
                }
            }
        }
    }

    private fun showDeleteGoalSucceededSnackbar(text: String) {
        val snackbar = Snackbar.make(landing_page_container, text, Snackbar.LENGTH_LONG)
        snackbar.setAction("UNDO") {
            println("!!! revert button clicked!")
            viewModel.restoreDeletedGoal()
        }

        snackbar.show()
    }
}