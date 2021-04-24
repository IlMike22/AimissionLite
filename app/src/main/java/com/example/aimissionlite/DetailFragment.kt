package com.example.aimissionlite

import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.aimissionlite.DetailViewModel.DetailViewModelFactory
import com.example.aimissionlite.models.domain.Genre
import com.example.aimissionlite.models.domain.Goal
import com.example.aimissionlite.models.domain.Priority
import com.example.aimissionlite.models.domain.Status
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.example.aimissionlite.databinding.FragmentDetailBinding

class DetailFragment : IDetailFragment, Fragment() {
    private val viewModel: DetailViewModel by viewModels {
        DetailViewModelFactory(
            resources = resources,
            repository = (this.activity?.application as AimissionApplication).repository,
            view = this
        )
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

        return binding.getRoot()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val chipGroupGenre =
            view.findViewById<ChipGroup>(R.id.chip_group_genre) // todo use data-binding
        val chipGroupPriority =
            view.findViewById<ChipGroup>(R.id.chip_group_priority) // todo use data-binding



        chipGroupPriority.setOnCheckedChangeListener { group, checkedId ->
//            choosenPriority = view.findViewById(checkedId) // todo use data-binding
//            Toast.makeText(activity, "${choosenPriority?.text} clicked", Toast.LENGTH_SHORT)
//                .show()
        }

//        view.findViewById<Button>(R.id.button_save_goal).setOnClickListener {

    }

    override fun addGoal(goal: Goal) {
        viewModel.insert(goal)
    }

    companion object {

    }
}