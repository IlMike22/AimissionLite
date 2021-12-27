package com.example.aimissionlite.presentation.landing_page

import androidx.lifecycle.*
import com.example.aimissionlite.models.ILandingPageUseCase
import com.example.aimissionlite.models.domain.Goal
import com.example.aimissionlite.models.domain.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LandingPageViewModel @Inject constructor(
    private val useCase: ILandingPageUseCase
) : ViewModel() {
    var isDeleteAllGoals: LiveData<Boolean>? = null

    val allGoals: LiveData<List<Goal>> = useCase.getAllGoals().asLiveData()
    private var lastDeletedGoal: Goal = Goal.EMPTY

    val uiEvent = MutableLiveData<LandingPageUiEvent>()

    init {
        isDeleteAllGoals = useCase.getDeleteGoalsOnStartup().asLiveData()
    }

    fun onAddGoalClicked() {
        uiEvent.postValue(LandingPageUiEvent.NavigateToAddGoal())
    }

    fun onInfoClicked() {
        uiEvent.postValue(LandingPageUiEvent.NavigateToInfo())
    }

    fun onSettingsClicked() {
        uiEvent.postValue(LandingPageUiEvent.NavigateToSettings())
    }

    fun onGoalStatusClicked(goal: Goal?) {
        goal?.apply {
            viewModelScope.launch {
                useCase.updateGoalStatus(
                    id = goal.id,
                    status = getNewGoalStatus(goal.status)
                )
            }
        } ?: println("!!! Goal is null. Cannot update goal status.")
    }

    fun onGoalContainerClicked(goal: Goal?) {
        goal?.apply {
            uiEvent.postValue(LandingPageUiEvent.NavigateToAddGoal(this))
        }
    }

    fun onGoalDeleteClicked(goal: Goal?) {
        lastDeletedGoal = goal ?: Goal.EMPTY
        goal?.apply {
            viewModelScope.launch {
                val isDeleteSucceeded = useCase.deleteGoal(goal)
                if (isDeleteSucceeded.not()) {
                    println("!!! Error while deleting the goal.")
                }

                uiEvent.postValue(LandingPageUiEvent.ShowSnackbar("Goal deleted."))
            }
        } ?: println("!!! Goal is null. Cannot delete goal.")
    }

    fun restoreDeletedGoal() {
        if (lastDeletedGoal != Goal.EMPTY) {
            viewModelScope.launch {
                useCase.insertGoal(lastDeletedGoal)
            }
        }
    }

    suspend fun deleteAllGoals(): Boolean {
        return useCase.deleteAllGoals()
    }

    private fun getNewGoalStatus(oldStatus: Status): Status =
        when (oldStatus) {
            Status.DONE -> Status.TODO
            Status.TODO -> Status.IN_PROGRESS
            Status.IN_PROGRESS -> Status.DONE
            else -> Status.UNKNOWN
        }
}