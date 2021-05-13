package com.example.aimissionlite

import android.content.res.Resources
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.aimissionlite.data.GoalRepository
import com.example.aimissionlite.models.domain.Goal

class MainViewModel(
    private val resources: Resources,
    private val repository: GoalRepository,
    val view: MainFragment
) : ViewModel() {
    val allGoals: LiveData<List<Goal>> = repository.allGoals.asLiveData()

    fun onGoalClicked(view: View) {
        println("go on here!")
//        val goal: LiveData<Goal> = repository.getGoal(0).asLiveData() // todo remove that 0 later
//        val updatedGoal = goal.value?.copy(
//            status = Status.DONE
//        )
//
//        if (updatedGoal != null) { // todo maybe it is smarter to diretly update the goal instead of get, edit and save again
//            viewModelScope.launch {
//                repository.insert(updatedGoal)
//            }
//        }
    }

    class MainViewModelFactory(
        private val repository: GoalRepository,
        private val view: MainFragment,
        private val resources: Resources
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(resources, repository, view) as T
            }
            throw IllegalArgumentException("Unknown viewmodel class")
        }
    }
}