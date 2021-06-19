package com.example.aimissionlite

import android.content.res.Resources
import androidx.lifecycle.*
import com.example.aimissionlite.data.GoalRepository
import com.example.aimissionlite.models.domain.Goal
import com.example.aimissionlite.models.domain.Status
import kotlinx.coroutines.launch

class LandingPageViewModel(
    private val resources: Resources,
    private val repository: GoalRepository,
    val view: LandingPageFragment
) : ViewModel() {
    val allGoals: LiveData<List<Goal>> = repository.allGoals.asLiveData()

    fun onGoalStatusClicked(goal: Goal?) {
        goal?.apply {
            viewModelScope.launch {
                repository.updateStatus(
                    id = goal.id,
                    status = getNewGoalStatus(goal.status)
                )
            }
        } ?: println("!!! Goal is null. Cannot update goal status.")
    }

    private fun getNewGoalStatus(oldStatus: Status): Status =
        when (oldStatus) {
            Status.DONE -> Status.TODO
            Status.TODO -> Status.IN_PROGRESS
            Status.IN_PROGRESS -> Status.DONE
            else -> Status.UNKOWN
        }

    class MainViewModelFactory(
        private val repository: GoalRepository,
        private val view: LandingPageFragment,
        private val resources: Resources
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LandingPageViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return LandingPageViewModel(resources, repository, view) as T
            }
            throw IllegalArgumentException("Unknown viewmodel class")
        }
    }
}