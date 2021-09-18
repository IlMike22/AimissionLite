package com.example.aimissionlite

import android.content.res.Resources
import androidx.lifecycle.*
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.aimissionlite.data.GoalRepository
import com.example.aimissionlite.models.domain.Goal
import com.example.aimissionlite.models.domain.Status
import kotlinx.coroutines.launch

class LandingPageViewModel(
    private val goalRepository: GoalRepository,
    settingsRepository: SettingsRepository,
    val view: LandingPageFragment
) : ViewModel() {
    var isDeleteAllGoals: LiveData<Boolean>? = null
    val allGoals: LiveData<List<Goal>> = goalRepository.allGoals.asLiveData()
    val navController: NavController = view.findNavController()
    private var lastDeletedGoal: Goal = Goal.EMPTY

    init {
        isDeleteAllGoals = settingsRepository.getDeleteGoalsOnStartup().asLiveData()
    }

    fun onAddGoalClicked() {
        navController.navigate(R.id.action_LandingPageFragment_to_AddGoalFragment)
    }

    fun onInfoClicked() {
        navController.navigate(R.id.action_LandingPageFragment_to_InfoFragment)
    }

    fun onSettingsClicked() {
        navController.navigate(R.id.action_LandingPageFragment_to_SettingsFragment)
    }

    fun onGoalStatusClicked(goal: Goal?) {
        goal?.apply {
            viewModelScope.launch {
                goalRepository.updateStatus(
                    id = goal.id,
                    status = getNewGoalStatus(goal.status)
                )
            }
        } ?: println("!!! Goal is null. Cannot update goal status.")
    }

    fun onGoalDeleteClicked(goal: Goal?) {
        lastDeletedGoal = goal ?: Goal.EMPTY
        goal?.apply {
            viewModelScope.launch {
                val isDeleteSucceeded = goalRepository.deleteGoal(goal)
                if (isDeleteSucceeded.not()) {
                    println("!!! Error while deleting the goal.")
                }

                view.showDeleteGoalSucceededSnackbar("Goal was successfuly deleted")
            }
        } ?: println("!!! Goal is null. Cannot delete goal.")
    }

    fun restoreDeletedGoal() {
        if (lastDeletedGoal != Goal.EMPTY) {
            viewModelScope.launch {
                goalRepository.insert(lastDeletedGoal)
            }
        }
    }

    suspend fun deleteAllGoals(): Boolean {
        return goalRepository.deleteAll()
    }

    private fun getNewGoalStatus(oldStatus: Status): Status =
        when (oldStatus) {
            Status.DONE -> Status.TODO
            Status.TODO -> Status.IN_PROGRESS
            Status.IN_PROGRESS -> Status.DONE
            else -> Status.UNKOWN
        }

    class LandingPageViewModelFactory(
        private val goalRepository: GoalRepository,
        private val settingsRepository: SettingsRepository,
        private val view: LandingPageFragment,
        private val resources: Resources
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LandingPageViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return LandingPageViewModel(
                    goalRepository,
                    settingsRepository,
                    view
                ) as T
            }
            throw IllegalArgumentException("Unknown view model class")
        }
    }
}