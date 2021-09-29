package com.example.aimissionlite

import android.content.res.Resources
import androidx.core.os.bundleOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.example.aimissionlite.data.Converters.Companion.toGenreId
import com.example.aimissionlite.data.Converters.Companion.toPriorityId
import com.example.aimissionlite.data.GoalRepository
import com.example.aimissionlite.models.domain.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class DetailViewModel(
    private val resources: Resources,
    private val repository: GoalRepository,
    val view: DetailFragment
) : ViewModel() {

    private var currentGoal = Goal.EMPTY

    init {
        view.buttonText.value = resources.getString(R.string.fragment_detail_add_goal_button_text)
    }

    fun onButtonClicked() {
        if (currentGoal != Goal.EMPTY) {
            updateGoal()
            return
        }

        createNewGoal()
    }

    fun getAndShowGoal(id: Int) = viewModelScope.launch {
        currentGoal = repository.getGoal(id)
        showGoal(currentGoal)
    }

    private fun updateGoal() {
        val validationStatusCode = isGoalValid(currentGoal)

        currentGoal = currentGoal.copy(
            id = currentGoal.id,
            title = view.goalTitle.value.orEmpty(),
            description = view.goalDescription.value.orEmpty(),
            creationDate = currentGoal.creationDate,
            changeDate = getCurrentDate(),
            isRepeated = currentGoal.isRepeated,
            genre = view.selectedChipGenre.toGenre(),
            status = currentGoal.status,
            priority = view.selectedChipPriority.toPriority()
        )

        if (validationStatusCode == GoalValidationStatusCode.OK) {
            viewModelScope.launch {
                repository.updateGoal(currentGoal)
            }

            navigateToMainFragment()
        }

        view.showValidationResult(validationStatusCode)
    }

    private fun createNewGoal() {
        val currentDate = getCurrentDate()

        val newGoal = Goal(
            id = 0,
            title = view.goalTitle.value.orEmpty(),
            description = view.goalDescription.value.orEmpty(),
            creationDate = currentDate,
            changeDate = currentDate,
            isRepeated = false,
            genre = view.selectedChipGenre.toGenre(),
            status = Status.TODO,
            priority = view.selectedChipPriority.toPriority()
        )

        val validationStatusCode = isGoalValid(newGoal)

        if (validationStatusCode == GoalValidationStatusCode.OK) {
            viewModelScope.launch {
                repository.insert(newGoal)
            }
            navigateToMainFragment()
        }

        view.showValidationResult(validationStatusCode)
    }

    private fun showGoal(goal: Goal) {
        view.setGoalTitle(goal.title)
        view.setGoalDescription(goal.description)
        view.setSelectedChipGenre(goal.genre.toGenreId())
        view.setSelectedChipPriority(goal.priority.toPriorityId())
    }

    private fun getCurrentDate(): String = LocalDateTime.now().toString()

    private fun isGoalValid(goal: Goal): GoalValidationStatusCode {
        goal.apply {
            return when {
                title.isBlank() -> GoalValidationStatusCode.NO_TITLE
                description.isBlank() -> GoalValidationStatusCode.NO_DESCRIPTION
                genre.isGenreNotSet() -> GoalValidationStatusCode.NO_GENRE
                else -> GoalValidationStatusCode.OK
            }
        }
    }

    private fun navigateToMainFragment() {
        view.hideKeyboard(view.activity?.currentFocus)
        val bundle =
            bundleOf(resources.getString(R.string.bundle_argument_goal_title) to view.goalTitle.value)
        findNavController(view).navigate(R.id.action_SecondFragment_to_FirstFragment, bundle)
    }

    companion object {
        private fun MutableLiveData<Int>.toGenre(): Genre =
            when (this.value) {
                R.id.chip_genre_business -> Genre.BUSINESS
                R.id.chip_genre_socialising -> Genre.SOCIALISING
                R.id.chip_genre_fittness -> Genre.FITTNESS
                R.id.chip_genre_money -> Genre.MONEY
                R.id.chip_genre_partnership -> Genre.PARTNERSHIP
                R.id.chip_genre_health -> Genre.HEALTH
                else -> Genre.UNKNOWN
            }

        private fun MutableLiveData<Int>.toPriority(): Priority =
            when (this.value) {
                R.id.chip_priority_low -> Priority.LOW
                R.id.chip_priority_high -> Priority.HIGH
                else -> Priority.NORMAL
            }
    }

    class DetailViewModelFactory(
        private val resources: Resources,
        private val repository: GoalRepository,
        private val view: DetailFragment
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DetailViewModel(resources, repository, view) as T
            }
            throw IllegalArgumentException("Unknown viewmodel class")
        }
    }

    private fun Genre.isGenreNotSet(): Boolean {
        if (this == Genre.UNKNOWN) {
            return true
        }
        return false
    }
}