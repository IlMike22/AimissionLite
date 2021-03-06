package com.example.aimissionlite.presentation.detail.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.aimissionlite.BR
import com.example.aimissionlite.MainActivity
import com.example.aimissionlite.R
import com.example.aimissionlite.data.BUNDLE_ID_GOAL
import com.example.aimissionlite.databinding.FragmentDetailBinding
import com.example.aimissionlite.models.domain.*
import com.example.aimissionlite.presentation.detail.ChipGroupName
import com.example.aimissionlite.presentation.detail.DetailState
import com.example.aimissionlite.presentation.detail.DetailUIEvent
import com.example.aimissionlite.presentation.detail.DetailViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailFragment : Fragment() {
    private val viewModel: DetailViewModel by viewModels()
    val TAG = "DetailFragment"

    var isNavigationAlreadyCalled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val navController: NavController = findNavController()

        activity?.toolbar?.setupWithNavController(
            navController = navController,
            configuration = AppBarConfiguration(navController.graph)
        )

        val binding: FragmentDetailBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        binding.lifecycleOwner = this

        binding.setVariable(BR.viewModel, viewModel) // binding the view model and execute this
        binding.executePendingBindings()

        val goalId = arguments?.getInt(BUNDLE_ID_GOAL)
        if (goalId != null && goalId != 0) {
            viewModel.getAndShowGoal(goalId)
        }

        viewModel.buttonText = resources.getString(R.string.fragment_detail_add_goal_button_text)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when (state) {
                        is DetailState.ShowEditGoal -> {
                            setChips(state)
                        }
                    }
                }
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        CoroutineScope(Dispatchers.Main).launch {
            viewModel.uiEvent.collectLatest { uiEvent ->
                when (uiEvent) {
                    is DetailUIEvent.ShowValidationResult -> {
                        showValidationResult(uiEvent.data ?: GoalValidationStatusCode.EMPTY)
                    }
                    is DetailUIEvent.HideKeyboard -> hideKeyboard(activity?.currentFocus)
                    is DetailUIEvent.NavigateToLandingPage -> navigateToLandingPage()
                    is DetailUIEvent.NavigateToSettings -> navigateToSettings()
                    is DetailUIEvent.NavigateToInfo -> navigateToInfo()
                }
            }
        }

        setupChipGroupCheckedChangeListeners()
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

    private fun setupChipGroupCheckedChangeListeners() {
        chip_group_priority.setOnCheckedChangeListener { _, checkedId ->
            viewModel.setSelectedChipGroupItem(
                ChipGroupName.PRIORITY,
                checkedId
            )
        }

        chip_group_genre.setOnCheckedChangeListener { _, checkedId ->
            viewModel.setSelectedChipGroupItem(
                ChipGroupName.GENRE,
                checkedId
            )
        }
    }

    private fun setChips(state: DetailState.ShowEditGoal<Goal>) {
        try {
            val selectedGenreChipId = state.data?.genre?.toSelectedChipId()
            val selectedPriorityChipId = state.data?.priority?.toSelectedChipId()

            val chipGenreToSelect = (chip_group_genre as ChipGroup)[selectedGenreChipId ?: -1]
            (chipGenreToSelect as Chip).isChecked = true

            val chipPriorityToSelect =
                (chip_group_priority as ChipGroup)[selectedPriorityChipId ?: -1]
            (chipPriorityToSelect as Chip).isChecked = true
        } catch (exception: Throwable) {
            Log.e(TAG, "Error setting chip id ${exception.message}")
        }
    }

    private fun navigateToLandingPage() {
        val bundle =
            bundleOf(resources.getString(R.string.bundle_argument_goal_title) to viewModel.goalTitle.value)
        NavHostFragment.findNavController(this)
            .navigate(R.id.action_DetailFragment_to_LandingPageFragment, bundle)
    }

    override fun onResume() {
        isNavigationAlreadyCalled = false
        super.onResume()
    }

    private fun navigateToSettings() {
        if (isNavigationAlreadyCalled.not()) {
            findNavController().navigate(R.id.action_DetailFragment_to_SettingsFragment)
            isNavigationAlreadyCalled = true
        }
    }

    private fun navigateToInfo() {
        if (isNavigationAlreadyCalled.not()) {
            findNavController().navigate(R.id.action_DetailFragment_to_InfoFragment)
            isNavigationAlreadyCalled = true
        }
    }

    private fun showValidationResult(goalValidationStatusCode: GoalValidationStatusCode) {
        when (goalValidationStatusCode.statusCode) {
            ValidationStatusCode.NO_TITLE -> showToast(R.string.fragment_detail_goal_validation_status_no_title)
            ValidationStatusCode.NO_DESCRIPTION -> showToast(R.string.fragment_detail_goal_validation_status_no_description)
            ValidationStatusCode.NO_GENRE -> showToast(R.string.fragment_detail_goal_validation_status_no_genre)
            else -> showSuccessMessage(goalValidationStatusCode.isGoalUpdated)
        }
    }

    private fun showSuccessMessage(isGoalUpdated: Boolean) {
        val messageId = if (isGoalUpdated) {
            R.string.fragment_detail_goal_updated_validation_status_success
        } else {
            R.string.fragment_detail_goal_created_validation_status_success
        }

        showToast(messageId)
    }

    private fun hideKeyboard(currentFocusedView: View?) {
        try {
            (activity as MainActivity).hideKeyboard(currentFocusedView)
        } catch (error: Throwable) {
            Log.e(
                TAG,
                "Unable to call activity for hiding keyboard. Details: $error"
            )
        }
    }

    private fun showToast(id: Int) {
        Toast.makeText(
            this.context,
            getString(id),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun Genre.toSelectedChipId(): Int {
        return when (this) {
            Genre.BUSINESS -> 0
            Genre.FITNESS -> 1
            Genre.MONEY -> 2
            Genre.PARTNERSHIP -> 3
            Genre.SOCIALISING -> 4
            Genre.HEALTH -> 5
            Genre.UNKNOWN -> -1
        }
    }

    private fun Priority.toSelectedChipId(): Int {
        return when (this) {
            Priority.LOW -> 0
            Priority.HIGH -> 1
            Priority.NORMAL -> 2
            Priority.UNKNOWN -> -1
        }
    }
}