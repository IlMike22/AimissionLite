package com.example.aimissionlite.presentation.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.aimissionlite.AimissionApplication
import com.example.aimissionlite.R
import com.example.aimissionlite.data.Converters.Companion.toGenreId
import com.example.aimissionlite.data.Converters.Companion.toPriorityId
import com.example.aimissionlite.domain.common.repository.IGoalRepository
import com.example.aimissionlite.models.domain.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: IGoalRepository,
    app: Application
) : AndroidViewModel(app) {
    val uiEvent = MutableSharedFlow<DetailUIEvent<GoalValidationStatusCode>>()
    private val resourceProvider = getApplication<AimissionApplication>()

    private val _state = MutableStateFlow(DetailState.ShowEditGoal(Goal.EMPTY))
    val state = _state.asStateFlow()

    private var currentGoal = Goal.EMPTY
    var buttonText: String = resourceProvider.getString(R.string.button_done)

    val goalTitle = MutableStateFlow<String?>(null)
    val goalDescription = MutableStateFlow<String?>(null)
    val selectedChipGenre = MutableStateFlow<Int?>(null)
    val selectedChipPriority = MutableStateFlow<Int?>(null)


    fun onSaveGoalButtonClicked() {
        if (currentGoal != Goal.EMPTY) {
            updateGoal()
            return
        }

        createNewGoal()
    }

    fun getAndShowGoal(id: Int) = viewModelScope.launch {
        currentGoal = repository.getGoal(id)

        goalTitle.value = currentGoal.title
        goalDescription.value = currentGoal.description
        selectedChipGenre.value = currentGoal.genre.toGenreId()
        selectedChipPriority.value = currentGoal.priority.toPriorityId()

        showGoal(currentGoal)
    }

    private fun updateGoal() {
        val validationStatusCode = isGoalValid(currentGoal)

        currentGoal = currentGoal.copy(
            id = currentGoal.id,
            title = goalTitle.value.orEmpty(),
            description = goalDescription.value.orEmpty(),
            creationDate = currentGoal.creationDate,
            changeDate = getCurrentDate(),
            isRepeated = currentGoal.isRepeated,
            genre = selectedChipGenre.toGenre() ?: Genre.UNKNOWN,
            status = currentGoal.status,
            priority = selectedChipPriority.toPriority() ?: Priority.UNKNOWN
        )

        if (validationStatusCode == GoalValidationStatusCode.OK) {
            viewModelScope.launch {
                repository.updateGoal(currentGoal)
            }

            navigateToMainFragment()
        }

        viewModelScope.launch {
            uiEvent.emit(DetailUIEvent.ShowValidationResult(validationStatusCode))
        }
    }

    private fun createNewGoal() {
        val currentDate = getCurrentDate()

        val newGoal = Goal(
            id = 0,
            title = goalTitle.value.orEmpty(),
            description = goalDescription.value.orEmpty(),
            creationDate = currentDate,
            changeDate = currentDate,
            isRepeated = false,
            genre = selectedChipGenre.toGenre(),
            status = Status.TODO,
            priority = selectedChipPriority.toPriority()
        )

        val validationStatusCode = isGoalValid(newGoal)

        if (validationStatusCode == GoalValidationStatusCode.OK) {
            viewModelScope.launch {
                repository.insert(newGoal)
            }
            navigateToMainFragment()
        }

        viewModelScope.launch {
            uiEvent.emit(DetailUIEvent.ShowValidationResult(validationStatusCode))
        }
    }

    private fun showGoal(goal: Goal) {
        _state.value = DetailState.ShowEditGoal(goal)
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
        viewModelScope.launch {
            uiEvent.emit(DetailUIEvent.HideKeyboard())
            uiEvent.emit(DetailUIEvent.NavigateToLandingPage())
        }
    }

    companion object {
        private fun MutableStateFlow<Int?>.toGenre(): Genre =
            when (this.value) {
                R.id.chip_genre_business -> Genre.BUSINESS
                R.id.chip_genre_socialising -> Genre.SOCIALISING
                R.id.chip_genre_fitness -> Genre.FITNESS
                R.id.chip_genre_money -> Genre.MONEY
                R.id.chip_genre_partnership -> Genre.PARTNERSHIP
                R.id.chip_genre_health -> Genre.HEALTH
                else -> Genre.UNKNOWN
            }

        private fun MutableStateFlow<Int?>.toPriority(): Priority =
            when (this.value) {
                R.id.chip_priority_low -> Priority.LOW
                R.id.chip_priority_high -> Priority.HIGH
                else -> Priority.NORMAL
            }
    }

    private fun Genre.isGenreNotSet(): Boolean {
        if (this == Genre.UNKNOWN) {
            return true
        }
        return false
    }
}