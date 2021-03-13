package com.example.aimissionlite.models

import androidx.lifecycle.*
import com.example.aimissionlite.MainViewModel
import com.example.aimissionlite.data.GoalRepository
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import java.time.DateTimeException
import java.time.LocalDateTime

class DetailViewModel(private val repository:GoalRepository): ViewModel() {
    val allGoals: LiveData<List<Goal>> = repository.allGoals.asLiveData()

    fun insert(goal: Goal) = viewModelScope.launch {
        repository.insert(goal)
    }

    fun getCurrentDate():String = LocalDateTime.now().toString()

    class DetailViewModelFactory(private val repository:GoalRepository):ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DetailViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown viewmodel class")
        }

    }
}