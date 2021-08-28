package com.example.aimissionlite

import android.content.res.Resources
import androidx.lifecycle.*
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class SettingsViewModel(
    resources: Resources,
    val repository: SettingsRepository,
    val view: SettingsFragment
) : ViewModel() {
    val isDeleteGoalsOnStartup: LiveData<Boolean> =
        repository.getDeleteGoalsOnStartup().asLiveData()

    init {
        view.setHeader(resources.getString(R.string.fragment_settings_header_text))
    }

    fun onDeleteGoalsClicked(isEnabled: Boolean) {
        println("!!! delete goals button clicked!")
        viewModelScope.launch {
            repository.setDeleteGoalsOnStartup(isEnabled)
        }
    }

    fun getSettingsFromDataStore(): Flow<Boolean>{
        return repository.getDeleteGoalsOnStartup()
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
