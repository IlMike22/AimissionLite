package com.example.aimissionlite.presentation.settings.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.aimissionlite.BR
import com.example.aimissionlite.R
import com.example.aimissionlite.core.Resource
import com.example.aimissionlite.databinding.FragmentSettingsBinding
import com.example.aimissionlite.presentation.settings.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    var fragment: SettingsFragment? = null
    val TAG = "SettingsFragment"

    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
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

        fragment_settings_header.text = viewModel.getHeaderText()

        viewModel.isDeleteGoalOnStartup.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.onEach { isDeleteGoals ->
                        fragment_settings_check_box_delete_goals.isChecked = isDeleteGoals
                    }
                }
                else -> Log.e(TAG, "Cannot set checkbox. ${response.message}")
            }
        })

        fragment_settings_check_box_delete_goals.setOnClickListener { checkBoxView ->
            viewModel.onDeleteGoalsClicked((checkBoxView as CheckBox).isChecked) // todo maybe critical cast
        }
    }
}