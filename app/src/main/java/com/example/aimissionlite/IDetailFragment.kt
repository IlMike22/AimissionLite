package com.example.aimissionlite

import androidx.lifecycle.MutableLiveData
import com.example.aimissionlite.models.domain.GoalValidationStatusCode

interface IDetailFragment {
    val selectedChipPriority: MutableLiveData<Int>
    val goalTitle: MutableLiveData<String>
    val selectedChipGenre: MutableLiveData<Int>
    val goalDescription: MutableLiveData<String>
    val buttonText: MutableLiveData<String>

    fun setSelectedChipPriority(id: Int)
    fun setSelectedChipGenre(id: Int)
    fun setGoalTitle(title: String)
    fun setGoalDescription(text: String)
    fun setButtonText(text: String)

    fun showValidationResult(validationStatusCode: GoalValidationStatusCode)
    fun hideKeyboard()
}