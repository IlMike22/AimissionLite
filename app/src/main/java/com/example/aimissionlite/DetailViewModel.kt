package com.example.aimissionlite

import android.widget.EditText
import androidx.lifecycle.*
import com.example.aimissionlite.data.GoalRepository
import com.example.aimissionlite.models.domain.Genre
import com.example.aimissionlite.models.domain.Goal
import com.example.aimissionlite.models.domain.Priority
import com.example.aimissionlite.models.domain.Status
import com.google.android.material.chip.Chip
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import java.time.LocalDateTime

class DetailViewModel(private val repository:GoalRepository): ViewModel() {
    val allGoals: LiveData<List<Goal>> = repository.allGoals.asLiveData()
    val buttonText = "Hello VM Data Binding Button"

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