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
    private val resources: Resources,
    private val repository: GoalRepository,
    private val landingpageRepository: LandingPageRepository,
    val view: LandingPageFragment
) : ViewModel() {
    val allGoals: LiveData<List<Goal>> = repository.allGoals.asLiveData()
    val navController: NavController = view.findNavController()

    fun onAddGoalClicked() {
        navController.navigate(R.id.action_LandingPageFragment_to_AddGoalFragment)
    }

    fun onInfoClicked() {
        // call this navigation pattern to show info fragment
        println("!!! navigate to info from vm")
        navController.navigate(R.id.action_LandingPageFragment_to_InfoFragment)
    }

    fun onSettingsClicked() {
        // call this navigation pattern to show settings fragment
        println("!!! navigate to settings from vm")
        navController.navigate(R.id.action_LandingPageFragment_to_SettingsFragment)
    }

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

    fun deleteAllGoalsIfEnabled(): Boolean {
        /**
         * Use this method to run all the operations which were set in settings fragment that have to run on startup.
         * For example if checkbox "delete all goals on startup" is active, every time the app starts all goals have
         * to be deleted. This has to be done in this function.
         */

        val isDeleteAllGoals = landingpageRepository.getDeleteGoalsOnStartup().asLiveData()
        var result = false
        // TODO value is null here.. go on the next time.
        isDeleteAllGoals.value?.apply {
            if (this) {
                viewModelScope.launch {
                    result = landingpageRepository.deleteAllGoals(repository)
                }
            }
        }
        return result
    }

    private fun getNewGoalStatus(oldStatus: Status): Status =
        when (oldStatus) {
            Status.DONE -> Status.TODO
            Status.TODO -> Status.IN_PROGRESS
            Status.IN_PROGRESS -> Status.DONE
            else -> Status.UNKOWN
        }

    class LandingPageViewModelFactory(
        private val repository: GoalRepository,
        private val landingpageRepository: LandingPageRepository, // todo remove one repo, there should always be only one specific for this view
        private val view: LandingPageFragment,
        private val resources: Resources
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LandingPageViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return LandingPageViewModel(resources, repository, landingpageRepository, view) as T
            }
            throw IllegalArgumentException("Unknown viewmodel class")
        }
    }
}