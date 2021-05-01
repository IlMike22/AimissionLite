package com.example.aimissionlite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import com.example.aimissionlite.DetailViewModel.DetailViewModelFactory
import com.example.aimissionlite.databinding.FragmentDetailBinding

class DetailFragment : IDetailFragment, Fragment() {
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
            repository = (this.activity?.application as AimissionApplication).repository,
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
}