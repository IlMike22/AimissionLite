package com.example.aimissionlite.presentation.info

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aimissionlite.core.Resource
import com.example.aimissionlite.domain.info.use_case.GetInformationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InfoViewModel @Inject constructor(
    val getInformationUseCase: GetInformationUseCase
) : ViewModel() {

    val information = MutableLiveData<Resource<Map<String, String>>>()

    init {
        setInformation()
    }

    private fun setInformation() {
        information.postValue(Resource.Loading())
        information.postValue(
            Resource.Success(
                data = getInformationUseCase()
            )
        )
    }
}