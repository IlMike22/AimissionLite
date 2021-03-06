package com.example.aimissionlite

import androidx.lifecycle.*
import com.example.aimissionlite.data.GoalRepository
import com.example.aimissionlite.models.Goal
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class GoalViewModel(private val repository: GoalRepository) : ViewModel() {
    val allGoals: LiveData<List<Goal>> = repository.allGoals.asLiveData()

    fun insert(goal: Goal) = viewModelScope.launch {
        repository.insert(goal)
    }

    class GoalViewModelFactory(private val repository: GoalRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(GoalViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return GoalViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown viewmodel class")
        }
    }
}