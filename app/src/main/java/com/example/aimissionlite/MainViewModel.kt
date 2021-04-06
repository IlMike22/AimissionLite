package com.example.aimissionlite

import androidx.lifecycle.*
import com.example.aimissionlite.data.GoalRepository
import com.example.aimissionlite.models.domain.Goal
import java.lang.IllegalArgumentException

class MainViewModel(private val repository: GoalRepository) : ViewModel() {
    val allGoals: LiveData<List<Goal>> = repository.allGoals.asLiveData()

    class MainViewModelFactory(private val repository: GoalRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown viewmodel class")
        }
    }
}