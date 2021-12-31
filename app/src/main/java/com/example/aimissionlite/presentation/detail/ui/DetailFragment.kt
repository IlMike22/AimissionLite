package com.example.aimissionlite.presentation.detail.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import com.example.aimissionlite.BR
import com.example.aimissionlite.MainActivity
import com.example.aimissionlite.R
import com.example.aimissionlite.data.BUNDLE_ID_GOAL
import com.example.aimissionlite.databinding.FragmentDetailBinding
import com.example.aimissionlite.models.domain.GoalValidationStatusCode
import com.example.aimissionlite.presentation.detail.DetailState
import com.example.aimissionlite.presentation.detail.DetailUIEvent
import com.example.aimissionlite.presentation.detail.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailFragment : Fragment() {
    private val viewModel: DetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
                            edit_text_title.setText(state.data?.title)
                            edit_text_description.setText(state.data?.description)

//                        _goalDescription.value = goal.description
//                        _selectedChipGenre?.value = goal.genre.toGenreId()
//                        _selectedChipPriority?.value = goal.priority.toPriorityId()
                        }
                    }
                }
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.goalTitle?.observe(viewLifecycleOwner, { title ->
            viewModel.setGoalTitle(title)
        })

        viewModel.goalDescription?.observe(viewLifecycleOwner, { description ->
            viewModel.setGoalDescription(description)
        })

        viewModel.selectedChipGenre?.observe(viewLifecycleOwner, { genre ->
            viewModel.setSelectedChipGenre(genre)
        })

        viewModel.selectedChipPriority?.observe(viewLifecycleOwner, { priority ->
            viewModel.setSelectedChipPriority(priority)
        })

        CoroutineScope(Dispatchers.Main).launch {
            viewModel.uiEvent.collectLatest { uiEvent ->
                when (uiEvent) {
                    is DetailUIEvent.ShowValidationResult -> {
                        showValidationResult(uiEvent.data ?: GoalValidationStatusCode.UNKNOWN)
                    }
                    is DetailUIEvent.HideKeyboard -> hideKeyboard(activity?.currentFocus)
                    is DetailUIEvent.NavigateToLandingPage -> navigateToLandingPage()
                }
            }

        }
    }

    private fun navigateToLandingPage() {
        val bundle =
            bundleOf(resources.getString(R.string.bundle_argument_goal_title) to viewModel.goalTitle?.value)
        NavHostFragment.findNavController(this)
            .navigate(R.id.action_SecondFragment_to_FirstFragment, bundle)
    }

    private fun showValidationResult(validationStatusCode: GoalValidationStatusCode) {
        when (validationStatusCode) {
            GoalValidationStatusCode.NO_TITLE -> showToast(R.string.fragment_detail_goal_validation_status_no_title)
            GoalValidationStatusCode.NO_DESCRIPTION -> showToast(R.string.fragment_detail_goal_validation_status_no_description)
            GoalValidationStatusCode.NO_GENRE -> showToast(R.string.fragment_detail_goal_validation_status_no_genre)
            else -> showToast(R.string.fragment_detail_goal_validation_status_success)
        }
    }

    private fun hideKeyboard(currentFocusedView: View?) {
        try {
            (activity as MainActivity).hideKeyboard(currentFocusedView)
        } catch (error: Throwable) {
            Log.e(
                "AimissionLite",
                "DetailFragment: Unable to call activity for hiding keyboard. Details: $error"
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
}