package com.example.aimissionlite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import com.example.aimissionlite.databinding.FragmentSettingsBinding

class SettingsFragment : ISettingsFragment, Fragment() {
    var fragment: SettingsFragment? = null
    override val header: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    private val viewModel: SettingsViewModel by viewModels {
        SettingsViewModel.SettingsViewModelFactory(
            resources = resources,
            repository = (this.activity?.application as AimissionApplication).settingsStore,
            view = this
        )
    }

    override fun setHeader(text: String) {
        header.value = text
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentSettingsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)
        binding.lifecycleOwner = this

        binding.setVariable(BR.viewModel, viewModel) // binding the view model and execute this
        binding.executePendingBindings()

        fragment = this

        return binding.root
    }
}