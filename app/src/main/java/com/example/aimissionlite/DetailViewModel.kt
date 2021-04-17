package com.example.aimissionlite

import android.content.res.Resources
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.*
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
    private val repository: GoalRepository
) : ViewModel() {
    val allGoals: LiveData<List<Goal>> = repository.allGoals.asLiveData()
    val buttonText = resources.getString(R.string.button_done)
    val goalTitle:MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun setOnCheckedChangeListener(checkedId:Int) {
        //choosenGenre = view.findViewById(checkedId) // todo use data-binding
        println("!!! button clicked. This message comes from VM!!!")
    }

    fun onButtonClicked() {
        println("!!! button clicked. This message comes from VM!!!")
        println("!!! Goal title is ${goalTitle.value}")
        goalTitle.value = "Now we change the goal title in our vm!"
    }

    fun insert(goal: Goal) = viewModelScope.launch {
        repository.insert(goal)
    }

    fun getCurrentDate(): String = LocalDateTime.now().toString()

    class DetailViewModelFactory(
        private val resources: Resources,
        private val repository: GoalRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DetailViewModel(resources, repository) as T
            }
            throw IllegalArgumentException("Unknown viewmodel class")
        }

    }
}