package com.example.aimissionlite

import android.content.res.Resources
import androidx.core.os.bundleOf
import androidx.lifecycle.*
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.example.aimissionlite.data.GoalRepository
import com.example.aimissionlite.models.domain.Genre
import com.example.aimissionlite.models.domain.Goal
import com.example.aimissionlite.models.domain.Priority
import com.example.aimissionlite.models.domain.Status
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import java.time.LocalDateTime

class DetailViewModel(
    private val resources: Resources,
    private val repository: GoalRepository,
    val view: DetailFragment
) : ViewModel() {

    init {
        view.buttonText.value = "Fertig"
        val allGoals: LiveData<List<Goal>> = repository.allGoals.asLiveData()
    }

    fun onButtonClicked() {
        val currentDate = getCurrentDate()

        val newGoal = Goal(
            id = 0,
            title = view.goalTitle.value ?: "",
            description = view.goalDescription.value ?: "",
            creationDate = currentDate,
            changeDate = currentDate,
            isRepeated = false,
            genre = view.selectedChipGenre.toGenre(),
            status = Status.TODO,
            priority = view.selectedChipPriority.toPriority()
        )

        println("!!! do we have now all the data in $newGoal?")

        addGoal(newGoal)

        val bundle =
            bundleOf(resources.getString(R.string.bundle_argument_goal_title) to view.goalTitle.value)
        findNavController(view).navigate(R.id.action_SecondFragment_to_FirstFragment, bundle)

    }

    private fun addGoal(goal: Goal) = viewModelScope.launch {
        repository.insert(goal)
    }

    fun getCurrentDate(): String = LocalDateTime.now().toString()

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
}