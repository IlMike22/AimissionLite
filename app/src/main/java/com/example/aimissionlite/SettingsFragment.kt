package com.example.aimissionlite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.aimissionlite.databinding.FragmentSettingsBinding
import kotlinx.android.synthetic.main.activity_main.*

class SettingsFragment : ISettingsFragment, Fragment() {
    var fragment: SettingsFragment? = null
    override val header: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    private val viewModel: SettingsViewModel by viewModels {
        SettingsViewModel.SettingsViewModelFactory(
            resources = resources,
            repository = (this.activity?.application as AimissionApplication).settingsRepository,
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val deleteGoalsOnStartupCheckBox =
            view.findViewById<CheckBox>(R.id.fragment_settings_check_box_delete_goals)

        viewModel.isDeleteGoalsOnStartup.observe(viewLifecycleOwner, Observer { isGoalsDeleted ->
            deleteGoalsOnStartupCheckBox.isChecked = isGoalsDeleted
        })

        deleteGoalsOnStartupCheckBox.setOnClickListener { checkBoxView ->
            viewModel.onDeleteGoalsClicked((checkBoxView as CheckBox).isChecked) // todo maybe critical cast
        }
    }
}