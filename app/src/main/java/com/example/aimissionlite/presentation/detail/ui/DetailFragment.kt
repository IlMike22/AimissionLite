package com.example.aimissionlite.presentation.detail.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import com.example.aimissionlite.*
import com.example.aimissionlite.presentation.detail.DetailViewModel.DetailViewModelFactory
import com.example.aimissionlite.data.BUNDLE_ID_GOAL
import com.example.aimissionlite.databinding.FragmentDetailBinding
import com.example.aimissionlite.models.domain.GoalValidationStatusCode
import com.example.aimissionlite.presentation.detail.DetailViewModel

class DetailFragment : IDetailFragment, Fragment() {
    var detailFragment: DetailFragment? = null

    override val goalTitle: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    override val buttonText: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    override val selectedChipGenre: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    override val selectedChipPriority: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    override val goalDescription: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    private val viewModel: DetailViewModel by viewModels {
        DetailViewModelFactory(
            resources = resources,
            repository = (this.activity?.application as AimissionApplication).goalRepository,
            view = this
        )
    }

    override fun setSelectedChipPriority(id: Int) {
        selectedChipPriority.value = id
    }

    override fun setSelectedChipGenre(id: Int) {
        selectedChipGenre.value = id
    }

    override fun setGoalTitle(title: String) {
        goalTitle.value = title
    }

    override fun setGoalDescription(text: String) {
        goalDescription.value = text
    }

    override fun setButtonText(text: String) {
        buttonText.value = text
    }

    override fun showValidationResult(validationStatusCode: GoalValidationStatusCode) {
        when (validationStatusCode) {
            GoalValidationStatusCode.NO_TITLE -> showToast(R.string.fragment_detail_goal_validation_status_no_title)
            GoalValidationStatusCode.NO_DESCRIPTION -> showToast(R.string.fragment_detail_goal_validation_status_no_description)
            GoalValidationStatusCode.NO_GENRE -> showToast(R.string.fragment_detail_goal_validation_status_no_genre)
            else -> showToast(R.string.fragment_detail_goal_validation_status_success)
        }
    }

    override fun hideKeyboard(currentFocusedView: View?) {
        try {
            (activity as MainActivity).hideKeyboard(currentFocusedView)
        } catch (error: Throwable) {
            Log.e(
                "AimissionLite",
                "DetailFragment: Unable to call activity for hiding keyboard. Details: $error"
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentDetailBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        binding.lifecycleOwner = this

        binding.setVariable(BR.viewModel, viewModel) // binding the view model and execute this
        binding.executePendingBindings()

        detailFragment = this

        val goalId = arguments?.getInt(BUNDLE_ID_GOAL)
        if (goalId != null && goalId != 0) {
            viewModel.getAndShowGoal(goalId)
        }

        return binding.root
    }

    private fun showToast(id: Int) {
        Toast.makeText(
            this.context,
            getString(id),
            Toast.LENGTH_SHORT
        ).show()
    }
}