package com.example.aimissionlite.presentation.info

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.aimissionlite.core.Resource
import com.example.aimissionlite.domain.info.use_case.GetInformationUseCase

class InfoViewModel(
) : ViewModel() {

    val information = MutableLiveData<Resource<Map<String, String>>>()

    init {
        setInformation()
    }

    private fun setInformation() {
        information.postValue(Resource.Loading())
        information.postValue(
            Resource.Success(
                data = GetInformationUseCase().invoke()
            )
        )
    }

    class InfoViewModelFactory(
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(InfoViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return InfoViewModel() as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}