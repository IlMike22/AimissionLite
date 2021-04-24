package com.example.aimissionlite

import android.content.res.Resources
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.*
import androidx.navigation.Navigation.findNavController
import com.example.aimissionlite.data.GoalRepository
import com.example.aimissionlite.models.domain.Genre
import com.example.aimissionlite.models.domain.Goal
import com.example.aimissionlite.models.domain.Priority
import com.example.aimissionlite.models.domain.Status
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import java.time.LocalDateTime

class DetailViewModel(
    private val resources: Resources,
    private val repository: GoalRepository,
    private val view: DetailFragment
) : ViewModel() {
    val allGoals: LiveData<List<Goal>> = repository.allGoals.asLiveData()
    val buttonText = resources.getString(R.string.button_done)

    val goalTitle: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val selectedChipGenre:MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    val selectedChipPriority:MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    val goalDescription: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun onButtonClicked() {
        println("!!! button clicked. This message comes from VM!!!")
        println("!!! Goal title is ${goalTitle.value}")
        goalTitle.value = "Now we change the goal title in our vm!"

        val currentDate = getCurrentDate()

        val newGoal = Goal(
            id = 0,
            title = goalTitle.value?:"",
            description = goalDescription.value?:"",
            creationDate = currentDate,
            changeDate = currentDate,
            isRepeated = false,
            genre = selectedChipGenre.toGenre(),
            status = Status.UNKOWN,
            priority = selectedChipPriority.toPriority()
        )

        println("!!! do we have now all the data in $newGoal?")

        //addGoal(newGoal)

//        val bundle =
//            bundleOf(resources.getString(R.string.bundle_argument_goal_title) to goalTitle)
//        findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment, bundle)

    }

    fun insert(goal: Goal) = viewModelScope.launch {
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