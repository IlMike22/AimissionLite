package com.example.aimissionlite.presentation.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aimissionlite.core.Resource
import com.example.aimissionlite.domain.settings.use_case.implementation.SettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val useCase: SettingsUseCase
) : ViewModel() {
    val isDeleteGoalOnStartup = MutableLiveData<Resource<Flow<Boolean>>>()

    init {
        val result = useCase.getDeleteGoalsOnStartup()
        isDeleteGoalOnStartup.postValue(Resource.Success(result))
    }


    fun onDeleteGoalsClicked(isEnabled: Boolean) {
        println("!!! delete goals button clicked!")
        viewModelScope.launch {
            useCase.setDeleteGoalsOnStartup(isEnabled)
        }
    }

    fun getHeaderText() = useCase.getHeaderText()

    fun getSettingsFromDataStore(): Flow<Boolean> {
        return useCase.getDeleteGoalsOnStartup()
    }
}

