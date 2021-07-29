package com.example.aimissionlite

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SettingsViewModel(
    resources: Resources,
    val repository: SettingsRepository,
    val view: SettingsFragment
) : ViewModel() {

    init {
        view.setHeader(resources.getString(R.string.fragment_settings_header_text))
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
