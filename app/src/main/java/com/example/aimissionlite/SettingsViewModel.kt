package com.example.aimissionlite

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class SettingsViewModel(
    resources: Resources,
    val repository: SettingsRepository,
    val view: SettingsFragment
) : ViewModel() {

    init {
        view.setHeader(resources.getString(R.string.fragment_settings_header_text))
    }

    fun onDeleteGoalsClicked(isEnabled:Boolean) {
        println("!!! delete goals button clicked!")
        viewModelScope.launch {
            repository.setDeleteGoalsOnStartup(isEnabled)
        }
    }

    class SettingsViewModelFactory(
        private val resources: Resources,
        private val repository: SettingsRepository,
        private val view: SettingsFragment
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SettingsViewModel(resources, repository, view) as T
            }
            throw IllegalArgumentException("Unknown viewmodel class")
        }
    }
}
