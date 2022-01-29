package com.example.aimissionlite.presentation.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.aimissionlite.AimissionApplication
import com.example.aimissionlite.R
import com.example.aimissionlite.data.Converters.Companion.toGenreId
import com.example.aimissionlite.data.Converters.Companion.toPriorityId
import com.example.aimissionlite.domain.detail.use_case.IDetailUseCase
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
    private val useCase: IDetailUseCase,
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

    var selectedChipGenre: Int? = null
    var selectedChipPriority: Int? = null


    fun setSelectedChipGroupItem(chipGroup: ChipGroupName, selectedId: Int) {
        when (chipGroup) {
            ChipGroupName.GENRE -> {
                selectedChipGenre = selectedId
            }
            ChipGroupName.PRIORITY -> {
                selectedChipPriority = selectedId
            }
        }
    }

    fun onSaveGoalButtonClicked() {
        if (currentGoal != Goal.EMPTY) {
            val newGoal = Goal(
                id = currentGoal.id,
                title = goalTitle.value.orEmpty(),
                description = goalDescription.value.orEmpty(),
                creationDate = currentGoal.creationDate,
                changeDate = getCurrentDate(),
                isRepeated = currentGoal.isRepeated,
                genre = selectedChipGenre?.toGenre() ?: Genre.UNKNOWN,
                status = currentGoal.status,
                priority = selectedChipPriority?.toPriority() ?: Priority.UNKNOWN
            )

            updateGoal(newGoal)
            return
        }

        createNewGoal()
    }

    fun getAndShowGoal(id: Int) = viewModelScope.launch {
        currentGoal = useCase.getGoal(id)

        goalTitle.value = currentGoal.title
        goalDescription.value = currentGoal.description
        selectedChipGenre = currentGoal.genre.toGenreId()
        selectedChipPriority = currentGoal.priority.toPriorityId()

        showGoal(currentGoal)
    }

    private fun updateGoal(newGoal: Goal) {
        if (newGoal == currentGoal) {
            navigateToMainFragment()
            return
        }

        currentGoal = newGoal

        val validationStatusCode = GoalValidationStatusCode(
            statusCode = isGoalValid(currentGoal),
            isGoalUpdated = true
        )

        if (validationStatusCode.statusCode == ValidationStatusCode.OK) {
            viewModelScope.launch {
                useCase.updateGoal(currentGoal)
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
            genre = selectedChipGenre?.toGenre() ?: Genre.UNKNOWN,
            status = Status.TODO,
            priority = selectedChipPriority?.toPriority() ?: Priority.UNKNOWN
        )

        val goalValidationStatusCode = GoalValidationStatusCode(
            statusCode = isGoalValid(newGoal),
            isGoalUpdated = false
        )


        if (goalValidationStatusCode.statusCode == ValidationStatusCode.OK) {
            viewModelScope.launch {
                useCase.insert(newGoal)
            }

            navigateToMainFragment()
        }

        viewModelScope.launch {
            uiEvent.emit(DetailUIEvent.ShowValidationResult(goalValidationStatusCode))
        }
    }

    private fun showGoal(goal: Goal) {
        _state.value = DetailState.ShowEditGoal(goal)
    }

    private fun getCurrentDate(): String = LocalDateTime.now().toString()

    private fun isGoalValid(goal: Goal): ValidationStatusCode {
        goal.apply {
            return when {
                title.isBlank() -> ValidationStatusCode.NO_TITLE
                description.isBlank() -> ValidationStatusCode.NO_DESCRIPTION
                genre.isGenreNotSet() -> ValidationStatusCode.NO_GENRE
                else -> ValidationStatusCode.OK
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
        private fun Int.toGenre(): Genre =
            when (this) {
                R.id.chip_genre_business -> Genre.BUSINESS
                R.id.chip_genre_socialising -> Genre.SOCIALISING
                R.id.chip_genre_fitness -> Genre.FITNESS
                R.id.chip_genre_money -> Genre.MONEY
                R.id.chip_genre_partnership -> Genre.PARTNERSHIP
                R.id.chip_genre_health -> Genre.HEALTH
                else -> Genre.UNKNOWN
            }

        private fun Int.toPriority(): Priority =
            when (this) {
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